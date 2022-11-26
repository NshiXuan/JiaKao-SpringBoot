package com.sx.jk.pojo.vo.list;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("系统资源")
public class SysResourceVo {
  //主键
  @ApiModelProperty("id")
  private Short id;
  //名称
  @ApiModelProperty("名称")
  private String name;
  //链接地址
  @ApiModelProperty("链接")
  private String uri;
  //权限标识
  @ApiModelProperty("权限标识")
  private String permission;
  //资源
  @ApiModelProperty("类型（0是目录，1是菜单，2是按钮）")
  private Short type;
  //图标
  @ApiModelProperty("图标")
  private String icon;
  //序号
  @ApiModelProperty("序号")
  private Short sn;
  //父资源id
  @ApiModelProperty("父资源id")
  private Short parentId;
}
