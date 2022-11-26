package com.sx.jk.common.util;

import com.sx.jk.common.exception.CommonException;
import com.sx.jk.pojo.vo.DataJsonVo;
import com.sx.jk.pojo.vo.JsonVo;
import com.sx.jk.pojo.vo.PageJsonVo;
import com.sx.jk.pojo.vo.PageVo;
import com.sx.jk.pojo.vo.req.page.PageReqVo;
import com.sx.jk.pojo.result.CodeMsg;
import org.springframework.util.StringUtils;
import org.springframework.validation.ObjectError;
import org.springframework.validation.BindException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

public class JsonVos {
  private static final String K_COUNT = "count";

  public static JsonVo error(String msg) {
    return new JsonVo(false, msg);
  }

  public static JsonVo error(int code, String msg) {
    return new JsonVo(code, msg);
  }

  public static JsonVo error(CodeMsg msg) {
    return new JsonVo(msg);
  }

  public static JsonVo error() {
    return new JsonVo(false);
  }

  // public static JsonVo error(Throwable t) {
  //   if (t instanceof CommonException) {
  //     CommonException e = (CommonException) t;
  //     return new JsonVo(e.getCode(), e.getMessage());
  //   } else if (t instanceof BindException) {
  //     BindException be = (BindException) t;
  //     List<ObjectError> errors = be.getBindingResult().getAllErrors();
  //     // 函数式编程的方式：stream
  //     List<String> defaultMsgs = errors.stream()
  //             .map(ObjectError::getDefaultMessage)
  //             .collect(Collectors.toList());
  //     String msg = StringUtils.collectionToDelimitedString(defaultMsgs, ", ");
  //     return error(msg);
  //   } else if (t instanceof ConstraintViolationException) {
  //     ConstraintViolationException cve = (ConstraintViolationException) t;
  //     List<String> msgs = cve.getConstraintViolations()
  //             .stream().map(ConstraintViolation::getMessage)
  //             .collect(Collectors.toList());
  //     String msg = StringUtils.collectionToDelimitedString(msgs, ", ");
  //     return error(msg);
  //   } else {
  //     return error(t.getMessage());
  //   }
  // }

  public static JsonVo ok(CodeMsg msg) {
    return new JsonVo(msg);
  }

  public static JsonVo ok(String msg) {
    return new JsonVo(true, msg);
  }

  public static <T> DataJsonVo<T> ok(T data) {
    return new DataJsonVo<>(data);
  }

  public static <T> PageJsonVo<T> ok(PageVo<T> pageVo) {
    PageJsonVo<T> pageJsonVo = new PageJsonVo<>();
    pageJsonVo.setCount(pageVo.getCount());
    pageJsonVo.setData(pageVo.getData());
    return pageJsonVo;
  }

  public static JsonVo ok() {
    return new JsonVo();
  }

  public static <T> T raise(String msg) throws CommonException {
    throw new CommonException(msg);
  }

  public static <T> T raise(CodeMsg codeMsg) throws CommonException {
    throw new CommonException(codeMsg);
  }
}
