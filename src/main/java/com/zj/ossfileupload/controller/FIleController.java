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
import javax.servlet.http.HttpServletResponse;

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
    public Result<Object> uploadFile(MultipartFile file, HttpServletResponse httpServletResponse){
        try{
            String res = fileService.upload(file);
            if(!res.equals("")){
                log.info("file:[{}] 上传成功");
                httpServletResponse.addHeader("Access-Control-Allow-Origin", "*"); // 允许来自 localhost:3000 的跨域请求
                httpServletResponse.addHeader("Access-Control-Allow-Methods", "GET, POST"); // 允许使用 GET 和 POST 方法
                httpServletResponse.addHeader("Access-Control-Allow-Headers", "Content-Type");
                return Result.success(res);
            }
        }catch (Exception e){
            log.error("file upload fail:{}",e.getMessage());
        }
        return Result.error(500,"上传失败");
    }
}
