package com.ztx.fileservice.exception;


import com.ztx.fileservice.pojo.result.SysJSONResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApiException extends RuntimeException {
    private static final long serialVersionUID = 8140508447825501039L;
    private SysJSONResult sysJSONResult;

    public ApiException(SysJSONResult sysJSONResult) {
        this.sysJSONResult = sysJSONResult;
        //非服务器错误不打印日志
        if(sysJSONResult.getStatus()>500){
            log.error("调用API错误 : "+sysJSONResult.getMsg());
        }

    }

    public ApiException() {
        this.sysJSONResult = new SysJSONResult();
        sysJSONResult.setMsg("调用错误");
        sysJSONResult.setStatus(500);
    }

    public ApiException(Integer code,String msg){
        this.sysJSONResult = new SysJSONResult();
        sysJSONResult.setStatus(code);
        sysJSONResult.setMsg(msg);
    }

    public SysJSONResult getSysJSONResult() {
        return sysJSONResult;
    }
}