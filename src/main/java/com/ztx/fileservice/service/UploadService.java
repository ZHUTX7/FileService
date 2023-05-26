package com.ztx.fileservice.service;

import com.ztx.fileservice.enums.BucketEnum;
import com.ztx.fileservice.exception.ApiException;
import com.ztx.fileservice.mapper.UserPhotoMapper;
import com.ztx.fileservice.pojo.dto.UserPhoto;
import com.ztx.fileservice.vo.UploadPhotoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UploadService {

    @Resource
    UserPhotoMapper userPhotoMapper;
    @Resource
    MinioService minioService;
    public List<UploadPhotoVO> uploadUserPhoto(List<MultipartFile> files, String userId, Integer minPhotoIndex) {
        if (StringUtils.isEmpty(userId)) {
            throw new ApiException(500, "用户id不能为空");
        }
        //文件在存储桶中的key
        int index = minPhotoIndex == null ? 0 : minPhotoIndex;
        List<UploadPhotoVO> result = new ArrayList<>();
        for (MultipartFile e : files) {
            UploadPhotoVO vo = new UploadPhotoVO();
            //获取文件名
            String key = userId + index;
            String fileName = e.getOriginalFilename();
            List<String> FILE_WHILE_EXT_LIST = Arrays.asList("JPG", "PNG", "JPEG");
            Assert.notNull(fileName, "File name can not be empty");
            String fileExtName = fileName.substring(fileName.lastIndexOf(".") + 1);
            if (!FILE_WHILE_EXT_LIST.contains(fileExtName.toUpperCase())) {
                log.error("文件格式不正确");
                continue;
            }
            fileName = key + "." + fileExtName;
            //准备将MultipartFile类型转为File类型
            try {
                //创建存储对象的请求
                String url = minioService.pushObject(e, fileName, BucketEnum.IMG.getCode());
                //将url存入数据库
                UserPhoto userPhoto = new UserPhoto();
                userPhoto.setId(userId + "" + index);
                userPhoto.setUserId(userId);
                userPhoto.setPhotoUrl(url);
                userPhoto.setPhotoIndex(index);
                userPhoto.setPhotoCreateTime(new Date());
                userPhotoMapper.insertUserPhoto(userPhoto);
                vo.setPhotoUrl(url);
                vo.setIndex(index);
                vo.setPhotoId(userPhoto.getId());
                result.add(vo);
                index++;
            } catch (IOException q) {
                q.printStackTrace();
                throw new ApiException(500, "照片上传失败");

            } catch (Exception exception) {
                log.error("照片上传失败{}",exception);
                throw new ApiException(500, "照片上传失败");
            }

        }
        return result;
    }

    public Map<String ,Object> uploadEmoji(MultipartFile file, String msgId) {
        if (StringUtils.isEmpty(msgId)) {
            throw new ApiException(500, "msgId不能为空");
        }
        //文件在存储桶中的key
        String fileName = file.getOriginalFilename();
        List<String> FILE_WHILE_EXT_LIST = Arrays.asList("JPG", "PNG", "JPEG");
        Assert.notNull(fileName, "File name can not be empty");
        String fileExtName = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (!FILE_WHILE_EXT_LIST.contains(fileExtName.toUpperCase())) {
            log.error("文件格式不正确");
            throw new ApiException(500, "文件格式不正确");
        }
        fileName = msgId + "." + fileExtName;
        //准备将MultipartFile类型转为File类型
        try {
            //创建存储对象的请求
            String url = minioService.pushObject(file, fileName, BucketEnum.EMOJI.getCode());
            //将url存入数据库
            Map<String ,Object> result = new HashMap<>();
            result.put("url",url);
            return  result;
        } catch (IOException q) {
            q.printStackTrace();
            throw new ApiException(500, "图片上传失败");

        } catch (Exception exception) {
            log.error("图片上传失败{}",exception);
            throw new ApiException(500, "图片上传失败");
        }
    }
}
