package com.sx.jk.pojo.vo.req.page;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class KeywordPageReqVo extends PageReqVo {
  @ApiModelProperty("搜索关键字")
  private String keyword;
}
