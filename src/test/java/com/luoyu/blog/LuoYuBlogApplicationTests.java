package com.luoyu.blog;

import com.luoyu.blog.common.api.IPApi;
import com.luoyu.blog.common.constants.ElasticSearchConstants;
import com.luoyu.blog.common.constants.RedisKeyConstants;
import com.luoyu.blog.common.util.ElasticSearchUtils;
import com.luoyu.blog.common.util.MinioUtils;
import com.luoyu.blog.common.util.SnowFlakeUtil;
import com.luoyu.blog.entity.article.Article;
import com.luoyu.blog.entity.sys.IPInfo;
import com.luoyu.blog.service.log.LogViewService;
import com.luoyu.blog.service.search.ArticleEsServer;
import com.luoyu.blog.service.search.VideoEsServer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@SpringBootTest
class LuoYuBlogApplicationTests {

    @Autowired
    private ElasticSearchUtils elasticSearchUtils;

    @Autowired
    private ArticleEsServer articleEsServer;

    @Autowired
    private VideoEsServer videoEsServer;

    @Autowired
    private IPApi ipApi;

    @Autowired
    private LogViewService logViewService;

    @Autowired
    private SnowFlakeUtil snowFlakeUtil;

    @Autowired
    private MinioUtils minioUtils;

    @Test
    void testEnumUtil() {
        log.info("测试中!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

    @Test
    void contextLoads() {
        log.info("测试中!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!{}");
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("ss mm HH dd MM ? yyyy");
        String dateString = formatter.format(date);
        log.info(dateString);
    }

    @Test
    void test1() {
        // 10位的秒级别的时间戳
        long time1 = 1527767665;
        String result1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time1 * 1000));
        System.out.println("10位数的时间戳（秒）--->Date:" + result1);
        Date date1 = new Date(time1*1000);   //对应的就是时间戳对应的Date
        // 13位的秒级别的时间戳
        String time2 = "1515730332000d";
        String result2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Double.valueOf(time2));
        System.out.println(result2);
    }

    @Test
    void testESCreateIndex() throws Exception {
        log.info(String.valueOf(elasticSearchUtils.createIndex(ElasticSearchConstants.LUOYUBLOG_SEARCH_ARTICLE_INDEX)));
    }

    @Test
    void testESSearchRequest() throws Exception {
//        List<Map<String, Object>> list = elasticSearchUtils.searchRequest(ElasticSearchConstants.LUOYUBLOG_SEARCH_ARTICLE_INDEX, "落雨");
//        list.forEach(x -> {
//            log.info(x.toString());
//        });
    }

    @Test
    void testCleanCityInfo() throws Exception {
        logViewService.cleanCityInfo();
    }

    @Test
    void testString() throws Exception {
        String str = RedisKeyConstants.CHAT_USER_PREFIX + "123";
        int pos = str.lastIndexOf(":");
        String str2 = str.substring(pos+1);
        log.info(str2);
    }

    @Test
    void testId() throws Exception {
        long s = snowFlakeUtil.snowflakeId(1, 1);
        long s1 = snowFlakeUtil.snowflakeId();
        System.out.println(s);
        System.out.println(s1);
    }

    @Test
    void testESSearchAllRequest() throws Exception {
        elasticSearchUtils.searchAllRequest(ElasticSearchConstants.LUOYUBLOG_SEARCH_ARTICLE_INDEX).forEach(x -> log.info(x.toString()));
    }

    @Test
    void testESBulkRequest() throws Exception {
        List<Article> libs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Article article = new Article();
            article.setId(i);
            article.setTitle("测试落雨文章标题" + i);
            article.setContent("测试文章内容，哈哈哈" + i);
            libs.add(article);
        }
        log.info(String.valueOf(elasticSearchUtils.bulkRequest(ElasticSearchConstants.LUOYUBLOG_SEARCH_ARTICLE_INDEX, libs)));
    }

    @Test
    void testESDeleteIndex() throws Exception {
        log.info(String.valueOf(elasticSearchUtils.deleteIndex(ElasticSearchConstants.LUOYUBLOG_SEARCH_ARTICLE_INDEX)));
    }

    @Test
    void testESInitArticle() throws Exception {
        videoEsServer.initVideoList();
        Thread.sleep(60000);
    }

    @Test
    void testMinio() throws Exception {
//        minioUtils.deleteObjectName("video", "63411811a9fd435e0e56a45162ce3fc9/0.chunk");
        String uploadChunkUrl = minioUtils.createUploadChunkUrl("video", "xxxxx", 0, 604800);
        log.info(uploadChunkUrl);
    }

    @Test
    void testIPApi() throws Exception {
        IPInfo ipInfo = ipApi.getIpInfo("183.15.182.152");
        log.info(ipInfo.toString());
    }

    @BeforeEach
    void testBefore(){
        log.info("测试开始!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

    @AfterEach
    void testAfter(){
        log.info("测试结束!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

}
