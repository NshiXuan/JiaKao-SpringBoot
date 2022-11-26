package com.sx.jk.pojo.vo.req.save;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class SysResourceReqVo {
  //主键
  @ApiModelProperty("【大于0代表更新，否则代表添加】")
  private Short id;

  @NotBlank(message = "昵称不能为空")
  @ApiModelProperty(value = "昵称【不能为空】", required = true)
  private String name;

  @ApiModelProperty("链接")
  private String uri;

  @ApiModelProperty("权限标识")
  private String permission;

  @Range(min = 0,max = 2,message = "类型只能是0、1、2")
  @ApiModelProperty(value = "资源类型（0是目录，1是菜单，2是按钮）",required = true)
  private Short type;
  //图标
  @ApiModelProperty("图标")
  private String icon;
  //序号
  @Min(value = 0, message = "序号不能是负数")
  @ApiModelProperty("排列顺序，不能是负数，默认0，值越大，就排在越前面")
  private Short sn;
  //父资源id
  @ApiModelProperty("父资源id【如果是目录。0，不是目录，大于0，默认0】")
  private Short parentId;
}
