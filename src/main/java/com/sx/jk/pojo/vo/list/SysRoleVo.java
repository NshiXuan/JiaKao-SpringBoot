package com.sx.jk.pojo.vo.list;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("系统角色")
public class SysRoleVo {
  //主键
  @ApiModelProperty("id")
  private Short id;
  //角色名称
  @ApiModelProperty("名称")
  private String name;
}
