package com.xiaoke.exception;

import com.xiaoke.common.core.constant.Constant;
import com.xiaoke.common.core.enums.ResultStatusEnum;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.entity.system.entity.Error;
import com.xiaoke.exception.event.SysErrorEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一异常处理
 *
 * @author xiaoke
 */
@RestControllerAdvice
@Slf4j
public class AllExceptionHandler {

    @Value("${spring.profiles.active}")
    private String active;

    @Autowired
    private ApplicationEventPublisher publisher;


    /**
     * 自定义异常统一处理
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<R> customException(CustomException exception) {
        log.error("自定义异常，错误码：{}，异常信息：{}", exception.getCode(), exception.getMessage());
        HttpHeaders responseHeaders = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.valueOf(exception.getCode());
        if (exception.getCode() != ResultStatusEnum.NOT_LOGIN.getCode()) {
            this.saveError(exception);
        }
        return new ResponseEntity<R>(R.failed(exception.getMessage()), responseHeaders, httpStatus);
    }


    /**
     * 所有异常报错
     *
     * @param exception
     * @return String
     * @throws Exception
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<R> allExceptionHandler(Exception exception) {
        log.error("系统异常，异常信息：{}", exception.getMessage());
        HttpHeaders responseHeaders = new HttpHeaders();

        R<Object> failed = null;
        //生产环境
        if (Constant.PROD.equals(active)) {
            failed = R.failed("程序异常，请联系工作人员");
        }
        //开发环境
        else if (Constant.DEV.equals(active)) {
            exception.printStackTrace();
            failed = R.failed(exception.getMessage());
        }
        this.saveError(exception);
        return new ResponseEntity<R>(failed, responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 保存异常
     */
    private void saveError(Exception exception) {
        Error error = new Error();
        StackTraceElement stackTraceElement = exception.getStackTrace()[0];
        error.setTitle(exception.getMessage());
        StringBuffer message = new StringBuffer();
        for (StackTraceElement traceElement : exception.getStackTrace()) {
            message.append(traceElement);
            message.append("\n");
        }
        error.setType("1");
        error.setPath(stackTraceElement.getClassName());
        error.setMessage(message.toString());
        publisher.publishEvent(new SysErrorEvent(error));
    }
}
