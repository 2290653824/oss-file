package com.zj.ossfileupload.service;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.model.PutObjectResult;
import com.sun.xml.internal.ws.api.ha.StickyFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.PriorityQueue;

/**
 * @author zhengjian
 * @date 2023-06-27 20:18
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService{

    @Value("${oss.endpoint}")
    private String endpoint;

    @Value("${oss.bucketName}")
    private String bucketName;

    @Value("${oss.rootPath}")
    private String rootPath;

    @Value("${oss.accessKey}")
    private String accessKey;

    @Value("${oss.accessValue}")
    private String accessValue;

    @Value("${oss.returnBaseUrl}")
    private String returnBaseUrl;

    @Override
    public String upload(MultipartFile file) {
        String originalFilename = getFileNameByTime()+"_"+file.getOriginalFilename();

        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";

        // 填写Bucket名称，例如examplebucket。
        String bucketName = "other-file-manager";
        // 填写Object完整路径，例如exampledir/exampleobject.txt。Object完整路径中不能包含Bucket名称。
        String objectName = rootPath+originalFilename;

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKey,accessValue);
        PutObjectResult putObjectResult = null;
        String uploadSuccessUrl = null;
        try {

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(file.getBytes());
            putObjectResult = ossClient.putObject(bucketName, objectName, byteArrayInputStream);
             uploadSuccessUrl = getUploadSuccessUrl(objectName);
        } catch (OSSException oe) {
            log.error("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            log.error("Error Message:{}" , oe.getErrorMessage());
            log.error("Error Code:{}" ,oe.getErrorCode());
            log.error("Request ID:{}" , oe.getRequestId());
            log.error("Host ID:{}" , oe.getHostId());
        } catch (ClientException ce) {
            log.error("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            log.error("Error Message:{}" , ce.getMessage());
        } catch (IOException e) {
            log.error("other file error:{}",e.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return uploadSuccessUrl==null?"":uploadSuccessUrl;
    }


    public String getUploadSuccessUrl(String objectName) throws UnsupportedEncodingException {
        return returnBaseUrl+objectName;
    }

    public String getFileNameByTime(){
        LocalDateTime now = LocalDateTime.now();
        // 定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        // 格式化日期时间
        return now.format(formatter);
    }


}
