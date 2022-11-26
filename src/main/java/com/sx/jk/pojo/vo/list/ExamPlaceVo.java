package com.sx.jk.pojo.vo.list;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("考场")
public class ExamPlaceVo {
  @ApiModelProperty("id")
  private String id;

  @ApiModelProperty("名称")
  private String name;

  @NotNull
  @ApiModelProperty("省份id")
  private String provinceId;

  @NotNull
  @ApiModelProperty("城市id")
  private String cityId;

  @ApiModelProperty("地址")
  private String address;

  @ApiModelProperty("纬度")
  private Double latitude;

  @ApiModelProperty("经度")
  private Double longitude;
}
