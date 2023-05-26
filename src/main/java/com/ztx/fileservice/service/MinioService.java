package com.ztx.fileservice.service;

import com.ztx.fileservice.enums.BucketEnum;
import com.ztx.fileservice.exception.ApiException;
import com.ztx.fileservice.utils.JWTUtils;
import com.ztx.fileservice.vo.UploadPhotoVO;
import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Service
public class MinioService {
    @Resource
    private MinioClient minioClient;

    @Value("${minio.endpoint}")
    private String endpoint;


//    @Bean
//    public void createBucket() {
//        try{
//            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(BucketEnum.IMG.getCode()).build())) {
//                minioClient.makeBucket(MakeBucketArgs.builder().bucket(BucketEnum.IMG.getCode()).build());
//            }
//            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(BucketEnum.EMOJI.getCode()).build())) {
//                minioClient.makeBucket(MakeBucketArgs.builder().bucket(BucketEnum.EMOJI.getCode()).build());
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//            log.error("FileService初始化失败,检查minio是否正常运行及密码是否正确");
//        }
//
//    }

    public String uploadObject( MultipartFile file){

        try {
            // 检查文件是否为空
            if (file.isEmpty()) {
                return null;
            }
            // 获取文件名
            String fileName = file.getOriginalFilename();
            // 将文件上传到MinIO
            ObjectWriteResponse response =   minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket("img")
                            .object(fileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            String fileURL = endpoint + "/" + "img" + "/" + fileName;
            return fileURL;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public String pushObject(MultipartFile file, String fileName,String bucketName) throws Exception {
        ObjectWriteResponse response =   minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );
        String fileURL = endpoint + "/" + bucketName + "/" + fileName;
        return fileURL;
    }


}
