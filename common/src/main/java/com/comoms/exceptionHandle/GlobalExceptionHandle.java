package com.comoms.exceptionHandle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Twilight
 * @desc 全局异常处理
 * @createTime 2019-01-21-00:39
 */
@ControllerAdvice
public class GlobalExceptionHandle {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandle.class);

    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter out = response.getWriter();
        log.info("=====================全局异常信息捕获=======================");
        log.info("请求路径::" + request.getProtocol() + request.getRemoteAddr() + request.getRemotePort());
        ex.printStackTrace();
        if (ex instanceof MyException) {
            out.print(ex.toString());
        } else {
            StackTraceElement exObj = ex.getStackTrace()[0];
            MyException myException = new MyException();
            myException.setMsg(ex.getMessage());
            myException.setThrowLocaltion("类名：" + exObj.getClassName()
                    + "方法：" + exObj.getMethodName()
                    + "行数：" + exObj.getLineNumber());
            myException.setCode("666");
            myException.setRequestUrl(request.getRequestURL().toString());
            out.print(myException.toString());
        }
        out.close();
        return null;
    }
}
