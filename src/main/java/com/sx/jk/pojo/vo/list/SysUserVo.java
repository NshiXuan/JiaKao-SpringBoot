package com.sx.jk.pojo.vo.list;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("系统用户")
public class SysUserVo {
  //主键
  @ApiModelProperty("id")
  private String id;
  //昵称
  @ApiModelProperty("昵称")
  private String nickname;
  //登录用的用户名
  @ApiModelProperty("用户名")
  private String username;

  //最后一次登录的时间
  @ApiModelProperty("最后一次登录的时间")
  private Long loginTime;
  //账号的状态，0是正常，1是锁定
  @ApiModelProperty("状态，0是正常，1是锁定")
  private Short status;
}
