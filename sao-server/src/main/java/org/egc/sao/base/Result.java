package org.egc.sao.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class Result<T> {
    private String msg;
    private int status;
    private T data;

    public Result(ResInfo rc, T data) {
        this.msg = rc.getMsg();
        this.status = rc.getCode();
        this.data = data;
    }
}
