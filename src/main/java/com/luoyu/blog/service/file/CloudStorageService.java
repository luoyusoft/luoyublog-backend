package com.luoyu.blog.service.file;

import com.luoyu.blog.common.util.DateUtils;
import com.luoyu.blog.common.config.CloudStorageProperties;
import com.luoyu.blog.entity.file.vo.FileResourceVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

/**
 * CloudStorageService
 *
 * @author luoyu
 * @date 2018/10/19 18:47
 * @description
 */
public abstract class CloudStorageService {

    /** 云存储配置信息 */
    protected CloudStorageProperties config;

    /**
     * 文件上传
     * @return        返回http地址
     */
    public abstract FileResourceVO upload(MultipartFile file, Integer fileModule);

    /**
     * 文件上传
     * @param data    文件字节数组
     * @param path    文件路径，包含文件名
     * @return        返回http地址
     */
    public abstract String upload(byte[] data, String path);

    /**
     * 文件上传
     * @param data     文件字节数组
     * @param suffix   后缀
     * @return         返回http地址
     */
    public abstract String uploadSuffix(byte[] data, String suffix);

    /**
     * 文件上传
     * @param inputStream   字节流
     * @param path          文件路径，包含文件名
     * @return              返回http地址
     */
    public abstract String upload(InputStream inputStream, String path);

    /**
     * 文件上传
     * @param inputStream  字节流
     * @param suffix       后缀
     * @return             返回http地址
     */
    public abstract String uploadSuffix(InputStream inputStream, String suffix);

}
