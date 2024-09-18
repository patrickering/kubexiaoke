

package com.xiaoke.common.core.utils;

import com.xiaoke.common.core.constant.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 响应信息主体
 *
 * @param <T>
 * @author xiaoke
 */
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "响应信息主体")
public class R<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    @ApiModelProperty(value = "返回标记：成功标记=0，失败标记=1")
    private int code;

    @Getter
    @Setter
    @ApiModelProperty(value = "返回信息")
    private String msg;

    @Getter
    @Setter
    @ApiModelProperty(value = "业务状态")
    private Boolean state;


    @Getter
    @Setter
    @ApiModelProperty(value = "数据")
    private T data;

    public static <T> R<T> ok() {
        return restResult(null, Constant.SUCCESS, null, true);
    }

    public static <T> R<T> ok(T data) {
        return restResult(data, Constant.SUCCESS, null, true);
    }

    public static <T> R<T> ok(T data, String msg) {
        return restResult(data, Constant.SUCCESS, msg, true);
    }

    public static <T> R<T> failed() {
        return restResult(null, Constant.FAIL, null, false);
    }

    public static <T> R<T> failed(String msg) {
        return restResult(null, Constant.FAIL, msg, false);
    }

    public static <T> R<T> failed(T data) {
        return restResult(data, Constant.FAIL, null, false);
    }

    public static <T> R<T> failed(T data, String msg) {
        return restResult(data, Constant.FAIL, msg, false);
    }

    public static <T> R<T> restResult(T data, int code, String msg, Boolean state) {
        R<T> apiResult = new R<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        apiResult.setState(state);
        return apiResult;
    }
}
