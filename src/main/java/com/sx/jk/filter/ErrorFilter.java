package com.sx.jk.filter;

import javax.servlet.*;
import java.io.IOException;

public class ErrorFilter implements Filter {
  public static final String ERROR_URL = "/handleError";

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    // 进入下一个链式调用
    try {
      filterChain.doFilter(servletRequest, servletResponse);
    } catch (Exception e) {
      // 设置携带参数，把异常携带给ErrorController
      servletRequest.setAttribute(ERROR_URL, e);

      // 重定向到ErrorController
      servletRequest.getRequestDispatcher(ERROR_URL).forward(servletRequest, servletResponse);
    }
  }
}
