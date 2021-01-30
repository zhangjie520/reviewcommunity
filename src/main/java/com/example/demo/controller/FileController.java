package com.example.demo.controller;

import cn.ucloud.ufile.annotation.UcloudParam;
import com.example.demo.dto.FileDTO;
import com.example.demo.exception.CustomErrorCode;
import com.example.demo.exception.CustomException;
import com.example.demo.provider.UCloudProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Author: codeape
 * @Date: 2021/1/28 21:57
 * @Version: 1.0
 */
@Controller
public class FileController {
    @Autowired
    private UCloudProvider uCloudProvider;
    @RequestMapping("/file/upload")
    @ResponseBody
    public FileDTO upload(HttpServletRequest request){
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("editormd-image-file");
        try {
            String fileUrl = uCloudProvider.upload(file.getInputStream(), file.getOriginalFilename(), file.getContentType());
            FileDTO fileDTO = new FileDTO();
            fileDTO.setSuccess(1);
            fileDTO.setMessage("上传成功");
            fileDTO.setUrl(fileUrl);
            return fileDTO;

        } catch (IOException e) {
            throw new CustomException(CustomErrorCode.FILE_UPLOAD_FILE);
        }
//        FileDTO fileDTO = new FileDTO();
//        fileDTO.setSuccess(1);
//        fileDTO.setMessage("上传成功");
//        fileDTO.setUrl("/img/advertisement1.png");
//        return fileDTO;
    }
}
