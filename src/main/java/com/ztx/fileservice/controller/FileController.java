package com.ztx.fileservice.controller;

import com.ztx.fileservice.pojo.result.SysJSONResult;
import com.ztx.fileservice.service.MinioService;
import com.ztx.fileservice.service.UploadService;
import com.ztx.fileservice.utils.JWTUtils;
import com.ztx.fileservice.utils.ResultVOUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    private MinioService minioService;
    @Resource
    private UploadService uploadService;

    @PostMapping("/uploadPhoto")
    public SysJSONResult uploadFile(@RequestParam("file") List<MultipartFile> files, HttpServletRequest request) {
        String token = request.getHeader("token");
        String userId;
        if(token==null||token.equals("")){
             userId =  "10001";
        }else{
            userId =  JWTUtils.getClaim(token,"userId");
        }

        Integer photoIndex = Integer.parseInt(request.getParameter("photoIndex")) ;
        if(files.isEmpty()||files.size()==0){
            return ResultVOUtil.error(401,"照片为空！");
        }
        return ResultVOUtil.success(uploadService.uploadUserPhoto(files,userId,photoIndex));
    }
    @PostMapping("/uploadEmoji")
    public SysJSONResult uploadEmoji(@RequestParam("file")MultipartFile files, HttpServletRequest request) {
        String msgId = request.getParameter("msgId")  ;
        return ResultVOUtil.success(uploadService.uploadEmoji(files,msgId));
    }



    @PostMapping("/upload")
    public SysJSONResult uploadFile(@RequestParam("file") MultipartFile file) {
        minioService.uploadObject(file);
        return ResultVOUtil.success();
    }


}
