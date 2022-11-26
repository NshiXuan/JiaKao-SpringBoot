package com.sx.jk.pojo.vo.list;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("区域")
public class PlateRegionVo {
  @ApiModelProperty("id")
  private String id;

  @ApiModelProperty("名称")
  private String name;

  @ApiModelProperty("车牌")
  private String plate;

  @ApiModelProperty("父区域id")
  private String parentId;
}
