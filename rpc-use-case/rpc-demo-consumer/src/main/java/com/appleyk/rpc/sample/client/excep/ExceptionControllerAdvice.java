package com.appleyk.rpc.sample.client.excep;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * <p>全局（API）异常拦截类，有异常了，把异常返回给Client，而不是将异常留在Server端</p>
 *
 * @author appleyk
 * @version V.0.1.1
 * @blob https://blog.csdn.net/appleyk
 * @github https://github.com/kobeyk
 * @date created on 17:20 2021/5/24
 */
@CrossOrigin
@ControllerAdvice
@RestControllerAdvice
public class ExceptionControllerAdvice {
    @ResponseBody
    @ExceptionHandler({Exception.class})
    public ResponseEntity errorHandler(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}
