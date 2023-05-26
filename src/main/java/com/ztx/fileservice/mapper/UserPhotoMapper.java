package com.ztx.fileservice.mapper;

import com.ztx.fileservice.pojo.dto.UserPhoto;
import com.ztx.fileservice.utils.MyMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPhotoMapper extends MyMapper<UserPhoto> {
    //查询用户图片
    @Select("select * from user_photo where user_id = #{userId} and photo_index = #{photoIndex}")
    UserPhoto queryUserPhoto(String userId,Integer photoIndex);
    @Select("select * from user_photo where user_id = #{userId} ")
    List<UserPhoto> queryUserPhotoList(String userId);
    //插入用户图片
    @Insert("insert into user_photo (id,user_id,photo_url,photo_index,photo_create_time) values (#{id},#{userId},#{photoUrl},#{photoIndex},#{photoCreateTime})")
    int insertUserPhoto(UserPhoto userPhoto);
    //更新用户图片索引
    @Update("update user_photo set photo_index = #{photoIndex} where user_id = #{userId} and photo_url = #{photoUrl}")
    int updateUserPhotoIndex(String userId,String photoUrl,Integer photoIndex);
}
