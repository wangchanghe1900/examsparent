package cn.unicom.exams.model.web;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author 王长河
 * @create 2019-11-12 14:31
 */
public class Response implements Serializable {
    private static final long serialVersionUID = -8093410049944609356L;

    private Integer code;
    private String msg;
    private Object data;
    private Long  timestamp;

    public Response(Integer code, String msg, Object data, Long timestamp) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.timestamp = timestamp;
    }


    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }


    public Response() {
    }

    public Response(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.timestamp= LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    public Response(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
        this.timestamp= LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Response{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                ", timestamp=" + timestamp +
                '}';
    }
}
