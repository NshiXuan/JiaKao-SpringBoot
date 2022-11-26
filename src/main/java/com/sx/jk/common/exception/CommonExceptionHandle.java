package com.sx.jk.common.exception;

import com.sx.jk.common.util.JsonVos;
import com.sx.jk.common.util.Streams;
import com.sx.jk.pojo.result.CodeMsg;
import com.sx.jk.pojo.vo.JsonVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

// @ControllerAdvice
// 只拦截控制器的异常 拦截器的异常拦截不了
@RestControllerAdvice
@Slf4j
public class CommonExceptionHandle {
  @ExceptionHandler(Throwable.class)
  @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
  public JsonVo handle(Throwable t) throws Exception {
    log.error("handle", t);

    if (t instanceof CommonException) {
      return handle((CommonException) t);
    } else if (t instanceof BindException) {
      return handle((BindException) t);
    } else if (t instanceof ConstraintViolationException) {
      return handle((ConstraintViolationException) t);
    } else if (t instanceof ServletException) {
      return handle((CommonException) t.getCause());
    }else if (t instanceof AuthorizationException){
      return JsonVos.error(CodeMsg.NO_PERMISSION);
    }

    // 其它异常 处理cause的异常，递归到cause为上面的4种异常或者没有cause异常
    Throwable cause = t.getCause();
    if (cause != null) {
      return handle(cause);
    }

    // 没有cause的异常
    return JsonVos.error(t.getMessage());
  }

  private JsonVo handle(CommonException ce) {
    return JsonVos.error(ce.getCode(), ce.getMessage());

  }

  private JsonVo handle(BindException be) {
    List<ObjectError> errors = be.getBindingResult().getAllErrors();
    // 函数式编程的方式：stream
    // List<String> defaultMsgs = errors.stream()
    //         .map(ObjectError::getDefaultMessage)
    //         .collect(Collectors.toList());
    // 通过Streams工具类简写
    List<String> defaultMsgs = Streams.map(errors, ObjectError::getDefaultMessage);

    String msg = StringUtils.collectionToDelimitedString(defaultMsgs, ", ");
    return JsonVos.error(msg);
  }

  private JsonVo handle(ConstraintViolationException cve) {
    // List<String> msgs = cve.getConstraintViolations()
    //         .stream().map(ConstraintViolation::getMessage)
    //         .collect(Collectors.toList());
    // 通过Streams工具类简写
    List<String> msgs = Streams.map(cve.getConstraintViolations(), ConstraintViolation::getMessage);

    String msg = StringUtils.collectionToDelimitedString(msgs, ", ");
    return JsonVos.error(msg);
  }
}
