package com.luoyu.blog.service.log.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyu.blog.common.api.IPApi;
import com.luoyu.blog.common.util.PageUtils;
import com.luoyu.blog.common.util.Query;
import com.luoyu.blog.entity.log.LogView;
import com.luoyu.blog.entity.sys.IPInfo;
import com.luoyu.blog.mapper.log.LogViewMapper;
import com.luoyu.blog.service.log.LogViewService;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * LogViewServiceImpl
 *
 * @author luoyu
 * @date 2018/10/25 15:36
 * @description
 */
@Slf4j
@Service
public class LogViewServiceImpl extends ServiceImpl<LogViewMapper, LogView> implements LogViewService {

    @Autowired
    private IPApi ipApi;

    /**
     * 分页查询日志
     * @param page
     * @param limit
     * @param module
     * @return
     */
    @Override
    public PageUtils queryPage(Integer page, Integer limit, Integer module) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", String.valueOf(page));
        params.put("limit", String.valueOf(limit));

        IPage<LogView> logViewIPage = baseMapper.selectPage(new Query<LogView>(params).getPage(),
                new QueryWrapper<LogView>().lambda()
                .eq(module != null, LogView::getModule,module)
                .orderByDesc(LogView::getCreateTime)
        );
        return new PageUtils(logViewIPage);
    }

    @Override
    public void cleanCityInfo() {
        log.info("开始清洗log_view表");
        XxlJobLogger.log("开始清洗log_view表");
        Integer maxId = baseMapper.selectMaxId();
        if (maxId == null || maxId < 1){
            return;
        }
        for (int start = 0, end = 500; start < maxId; start += 500, end += 500) {
            List<LogView> logViews = baseMapper.selectLogViewsByPage(start, end);
            logViews.forEach(logViewsItem -> {
                try {
                    IPInfo ipInfo = ipApi.getIpInfo(logViewsItem.getIp());
                    logViewsItem.setCountry(ipInfo.getCountry());
                    logViewsItem.setRegion(ipInfo.getRegionName());
                    logViewsItem.setCity(ipInfo.getCity());
                    logViewsItem.setUpdateTime(LocalDateTime.now());
                    baseMapper.updateLogViewById(logViewsItem);
                    log.info("清洗成功：{}", logViewsItem.toString());
                    XxlJobLogger.log("清洗成功：{}", logViewsItem.toString());
                    Thread.sleep(1000);
                }catch (Exception e){
                    log.info("清洗失败：" + e);
                    XxlJobLogger.log("清洗失败：" + e);
                }
            });
        }
        log.info("清洗log_view表结束");
        XxlJobLogger.log("清洗log_view表结束");
    }

}
