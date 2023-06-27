package com.zj.ossfileupload.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author zhengjian
 * @date 2023-06-27 20:18
 */
public interface FileService {

    String upload(MultipartFile file);
}
