package com.luoyu.blog.service.search.impl;

import com.luoyu.blog.common.constants.ElasticSearchConstants;
import com.luoyu.blog.common.constants.RabbitMqConstants;
import com.luoyu.blog.common.util.ElasticSearchUtils;
import com.luoyu.blog.common.util.JsonUtils;
import com.luoyu.blog.common.util.RabbitMQUtils;
import com.luoyu.blog.entity.video.Video;
import com.luoyu.blog.entity.video.dto.VideoDTO;
import com.luoyu.blog.entity.video.vo.VideoVO;
import com.luoyu.blog.mapper.video.VideoMapper;
import com.luoyu.blog.service.search.VideoEsServer;
import com.rabbitmq.client.Channel;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class VideoEsServerImpl implements VideoEsServer {

    @Autowired
    private ElasticSearchUtils elasticSearchUtils;

    @Resource
    private RabbitMQUtils rabbitmqUtils;

    @Autowired
    private VideoMapper videoMapper;

    @Override
    public boolean initVideoList() throws Exception {
        if(elasticSearchUtils.deleteIndex(ElasticSearchConstants.LUOYUBLOG_SEARCH_VIDEO_INDEX)){
            if(elasticSearchUtils.createIndex(ElasticSearchConstants.LUOYUBLOG_SEARCH_VIDEO_INDEX)){
                List<VideoDTO> videoDTOList = videoMapper.selectVideoDTOList();
                XxlJobLogger.log("初始化es视频数据，查到个数：{}", videoDTOList.size());
                log.info("初始化es视频数据，查到个数：{}", videoDTOList.size());
                if(!CollectionUtils.isEmpty(videoDTOList)){
                    videoDTOList.forEach(x -> {
                        VideoVO videoVO = new VideoVO();
                        BeanUtils.copyProperties(x, videoVO);
                        rabbitmqUtils.sendByRoutingKey(RabbitMqConstants.LUOYUBLOG_VIDEO_TOPIC_EXCHANGE, RabbitMqConstants.TOPIC_ES_VIDEO_ADD_ROUTINGKEY, JsonUtils.objectToJson(videoVO));
                    });
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 新增视频，rabbitmq监听器，添加到es中
     * @return
     */
    @RabbitListener(queues = RabbitMqConstants.LUOYUBLOG_ES_VIDEO_ADD_QUEUE)
    public void addListener(Message message, Channel channel){
        try {
            //手动确认消息已经被消费
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            if(message.getBody() != null){
                Video video = JsonUtils.jsonToObject(new String(message.getBody()), Video.class);
                if (video != null){
                    elasticSearchUtils.addDocument(ElasticSearchConstants.LUOYUBLOG_SEARCH_VIDEO_INDEX, video.getId().toString(), JsonUtils.objectToJson(video));
                    log.info("新增视频，rabbitmq监听器，添加到es中成功：id:" + new String(message.getBody()));
                }
            }else {
                log.info("新增视频，rabbitmq监听器，添加到es中失败：video:" + new String(message.getBody()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("新增视频，rabbitmq监听器，手动确认消息已经被消费失败，video:" + new String(message.getBody()));
        }
    }

    /**
     * 更新视频，rabbitmq监听器，更新到es
     * @return
     */
    @RabbitListener(queues = RabbitMqConstants.LUOYUBLOG_ES_VIDEO_UPDATE_QUEUE)
    public void updateListener(Message message, Channel channel){
        try {
            //手动确认消息已经被消费
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            if(message.getBody() != null){
                Video video = JsonUtils.jsonToObject(new String(message.getBody()), Video.class);
                if (video != null){
                    elasticSearchUtils.updateDocument(ElasticSearchConstants.LUOYUBLOG_SEARCH_VIDEO_INDEX, video.getId().toString(), JsonUtils.objectToJson(video));
                    log.info("更新视频，rabbitmq监听器，更新到es成功：id:" + new String(message.getBody()));
                }
            }else {
                log.info("更新视频，rabbitmq监听器，更新到es失败：video:" + new String(message.getBody()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("更新视频，rabbitmq监听器，手动确认消息已经被消费失败，video:" + new String(message.getBody()));
        }
    }

    /**
     * 删除视频，rabbitmq监听器，从es中删除
     * @return
     */
    @RabbitListener(queues = RabbitMqConstants.LUOYUBLOG_ES_VIDEO_DELETE_QUEUE)
    public void deleteListener(Message message, Channel channel){
        try {
            //手动确认消息已经被消费
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            StringBuffer videoIdsS = new StringBuffer();
            if(message.getBody() != null){
                Integer[] videoIds = JsonUtils.jsonToObject(new String(message.getBody()), Integer[].class);
                for (int i = 0; i < videoIds.length; i++) {
                    elasticSearchUtils.deleteDocument(ElasticSearchConstants.LUOYUBLOG_SEARCH_VIDEO_INDEX, videoIds[i].toString());
                    videoIdsS.append(videoIds[i] + ",");
                }
                log.info("删除视频，rabbitmq监听器，从es中删除成功：id:" + videoIdsS);
            }else {
                log.info("删除视频，rabbitmq监听器，从es中删除失败：video:" + videoIdsS);
            }
        } catch (Exception e) {
            e.printStackTrace();
            StringBuffer articleIdsS = null;
            Integer[] articleIds = JsonUtils.jsonToObject(new String(message.getBody()), Integer[].class);
            for (int i = 0; i < articleIds.length; i++) {
                articleIdsS.append(i + ",");
            }
            log.info("删除视频，rabbitmq监听器，手动确认消息已经被消费失败，video:" + articleIdsS);
        }
    }

    @Override
    public List<VideoDTO> searchVideoList(String keyword) throws Exception {
        List<String> highlightBuilderList = Arrays.asList("title", "alternateName");
        List<Map<String, Object>> searchRequests = elasticSearchUtils.searchRequest(ElasticSearchConstants.LUOYUBLOG_SEARCH_VIDEO_INDEX, keyword, highlightBuilderList, highlightBuilderList);
        List<VideoDTO> videoDTOList = new ArrayList<>();
        for(Map<String, Object> x : searchRequests){
            VideoDTO videoDTO = new VideoDTO();
            videoDTO.setId(Integer.valueOf(x.get("id").toString()));
            videoDTO.setCover(x.get("cover").toString());
            videoDTO.setVideoUrl(x.get("videoUrl").toString());
            videoDTO.setAlternateName(x.get("alternateName").toString());
            videoDTO.setScore(x.get("score").toString());
            videoDTO.setCreateTime(LocalDateTime.parse(x.get("createTime").toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            videoDTO.setUpdateTime(LocalDateTime.parse(x.get("updateTime").toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            videoDTO.setWatchNum(Long.valueOf(x.get("watchNum").toString()));
            videoDTO.setCommentNum(Long.valueOf(x.get("commentNum").toString()));
            videoDTO.setTitle(x.get("title").toString());
            videoDTO.setAuthor(x.get("author").toString());
            videoDTO.setSynopsis(x.get("synopsis").toString());
            videoDTO.setLikeNum(Long.valueOf(x.get("likeNum").toString()));
            videoDTOList.add(videoDTO);
        }
        return videoDTOList;
    }

}