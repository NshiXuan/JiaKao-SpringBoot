package com.sx.jk.common.prop;

import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@ConfigurationProperties("jk")
@Component
@Data
public class JkProperties implements ApplicationContextAware {
  private Cfg cfg;
  private Upload upload;
  private static JkProperties properties;

  // @Component会把JkProperties对象放到Spring容器里面，此时会调用setApplicationContext生命周期函数，此时我们把JkProperties对象this赋值给properties
  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    properties = this;
  }

  public static JkProperties get() {
    return properties;
  }

  @Data
  public static class Cfg {
    private String[] corsOrigins;
  }

  @Data
  public static class Upload {
    private String basePath;
    private String uploadPath;
    private String imagePath;
    private String videoPath;

    public String getImageRelativePath() {
      return uploadPath + imagePath;
    }

    public String getVideoRelativePath() {
      return uploadPath + videoPath;
    }
  }

}
