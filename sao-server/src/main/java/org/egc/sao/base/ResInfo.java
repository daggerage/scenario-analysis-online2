package org.egc.sao.base;

public enum ResInfo {
    SUCCESS(200,"请求成功"),
    BAD_REQUEST(400,"请求错误"),
    NOT_FOUND(404,"未找到"),
    CONFILICT(409,"与现有资源冲突"),
    AUTH_FAIL(466,"认证失败"),
    INTERNAL_ERROR(500,"系统内部错误");


    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    ResInfo(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
