package cn.unicom.exams.model.web;

import java.io.Serializable;

/**
 * @author 王长何
 * @create 2019-12-30 14:56
 */
public class WebResponse implements Serializable {
    private static final long serialVersionUID = -6104252215004366973L;

    private Integer code;
    private String msg;
    private Integer count;
    private Object data;
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public WebResponse(Integer code, String msg, Integer count, Object data) {
        this.code = code;
        this.msg = msg;
        this.count = count;
        this.data = data;
    }

    public WebResponse() {
    }

    public WebResponse(Integer code, String msg, Integer count) {
        this.code = code;
        this.msg = msg;
        this.count = count;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


}
