package com.sx.jk.common.cfg;

import com.sx.jk.common.shiro.TokenFilter;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Configuration
public class SwaggerCfg implements InitializingBean {
  @Autowired
  private Environment environment;
  private boolean enable;

  @Bean
  public Docket metadataDocket() {
    return groupDocket(
            "元数据",
            "/(dict.*|plate.*)",
            "元数据模块文档",
            "数据字典类型、数据字典条目、省份、城市");
  }

  @Bean
  public Docket examDocket() {
    return groupDocket(
            "考试",
            "/exam.*",
            "考试模块文档",
            "考场、科1科4、科2科3");
  }

  @Bean
  public Docket userDocket() {
    return groupDocket(
            "用户",
            "/sys.*|",
            "用户模块文档",
            "用户、角色、资源权限");
  }

  private Docket groupDocket(String group, String regex, String title, String desc) {
    return basicDocket()
            .groupName(group)
            .apiInfo(apiInfo(title, desc))
            .select()
            .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
            .paths(PathSelectors.regex(regex))
            .build();
  }

  // @Bean
  // public Docket docket(){
  //   return basicDocket()
  //             .apiInfo(apiInfo("小码哥驾考系统接口文档", "一个测试的接口文档"))
  //             .select()
  //             .apis(RequestHandlerSelectors.basePackage("com.sx.jk.controller"))
  //             .build();
  // }

  private Docket basicDocket() {
    RequestParameter token = new RequestParameterBuilder()
            .name(TokenFilter.HEADER_TOKEN)
            .description("用户登录令牌")
            .in(ParameterType.HEADER)
            .build();

    return new Docket(DocumentationType.SWAGGER_2)
            .globalRequestParameters(List.of(token))
            .ignoredParameterTypes(
                    HttpSession.class,
                    HttpServletRequest.class,
                    HttpServletResponse.class)
            .enable(enable);
  }

  private ApiInfo apiInfo(String title, String desc) {
    return new ApiInfoBuilder()
            .title(title)
            .description(desc)
            .version("1.0.0")
            .build();
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    enable = environment.acceptsProfiles(Profiles.of("dev", "test"));
  }
}
