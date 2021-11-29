package cn.com.mfish.common.core.web;

import cn.com.mfish.common.core.constants.Constants;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;

/**
 * 请求返回泛型结果
 *
 * @author qiufeng
 * @date 2021/3/16 14:34
 */
@ApiModel("结果通用参数")
public class AjaxTResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 成功
     */
    public static final int SUCCESS = Constants.SUCCESS;

    /**
     * 失败
     */
    public static final int FAIL = Constants.FAIL;

    private int code;

    private String msg;

    private T data;

    public static <T> AjaxTResult<T> ok() {
        return restResult(null, SUCCESS, null);
    }

    public static <T> AjaxTResult<T> ok(T data) {
        return restResult(data, SUCCESS, null);
    }

    public static <T> AjaxTResult<T> ok(T data, String msg) {
        return restResult(data, SUCCESS, msg);
    }

    public static <T> AjaxTResult<T> fail() {
        return restResult(null, FAIL, null);
    }

    public static <T> AjaxTResult<T> fail(String msg) {
        return restResult(null, FAIL, msg);
    }

    public static <T> AjaxTResult<T> fail(T data) {
        return restResult(data, FAIL, null);
    }

    public static <T> AjaxTResult<T> fail(T data, String msg) {
        return restResult(data, FAIL, msg);
    }

    public static <T> AjaxTResult<T> fail(int code, String msg) {
        return restResult(null, code, msg);
    }

    private static <T> AjaxTResult<T> restResult(T data, int code, String msg) {
        AjaxTResult<T> apiResult = new AjaxTResult<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
