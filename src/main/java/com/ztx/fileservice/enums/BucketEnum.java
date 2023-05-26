package com.ztx.fileservice.enums;

public enum BucketEnum implements CodeEnum<String>{
    //好友关系类型
    IMG("img","头像照片"),
    EMOJI("emoji","表情包");

    private String code;

    private String title;

    BucketEnum(String code, String title) {
        this.code = code;
        this.title = title;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getTitle() {
        return title;
    }
}
