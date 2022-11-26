package com.sx.jk.pojo.vo.req.save;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SysRoleReqVo {
  //主键
  @ApiModelProperty("【大于0代表更新，否则代表添加】")
  private Short id;
  //角色名称

  @NotBlank(message = "名称不能为空")
  @ApiModelProperty(value = "名称不能为空",required = true)
  private String name;

  @ApiModelProperty("资源id，多个id之间用逗号隔开")
  private String resourceIds;
}
