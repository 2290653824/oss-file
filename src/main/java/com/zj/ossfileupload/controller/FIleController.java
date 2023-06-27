package com.zj.ossfileupload.controller;

import com.zj.ossfileupload.common.Result;
import com.zj.ossfileupload.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author zhengjian
 * @date 2023-06-27 20:14
 */
@RestController
@RequestMapping("/file")
@Slf4j
public class FIleController {

    @Autowired
    private FileService fileService;

    @PostMapping("/uploadFile")
    public Result<Object> uploadFile(MultipartFile file){
        try{
            String res = fileService.upload(file);
            if(!res.equals("")){
                log.info("file:[{}] 上传成功");
                return Result.success(res);
            }
        }catch (Exception e){
            log.error("file upload fail:{}",e.getMessage());
        }
        return Result.error(500,"上传失败");
    }
}
