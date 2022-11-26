package com.sx.jk.controller;

import com.sx.jk.filter.ErrorFilter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ErrorController {
  @RequestMapping(ErrorFilter.ERROR_URL)
  public void handle(HttpServletRequest request) throws Exception{
    // 抛出异常
    throw (Exception) request.getAttribute(ErrorFilter.ERROR_URL);
  }
}
