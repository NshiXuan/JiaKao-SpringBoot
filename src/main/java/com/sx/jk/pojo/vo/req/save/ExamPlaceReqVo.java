package com.sx.jk.pojo.vo.req.save;

import com.sx.jk.common.foreign.anno.ForeignField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ExamPlaceReqVo {
  @ApiModelProperty("【大于0代表更新，否则代表添加】")
  private String id;

  @NotBlank(message = "名称不能为空")
  @ApiModelProperty(value = "名称【不能为空】", required = true)
  private String name;

  @NotNull
  @ApiModelProperty(value = "省份id", required = true)
  private String provinceId;

  @NotNull
  @ApiModelProperty(value = "城市id", required = true)
  private String cityId;

  @ApiModelProperty("地址")
  private String address;

  @ApiModelProperty("纬度")
  private Double latitude;

  @ApiModelProperty("经度")
  private Double longitude;
}
