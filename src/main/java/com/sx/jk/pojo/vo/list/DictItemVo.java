package com.sx.jk.pojo.vo.list;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("数据字典条目")
public class DictItemVo {
  @ApiModelProperty("id")
  private String id;

  @ApiModelProperty(value = "名称")
  private String name;

  @ApiModelProperty("值")
  private String value;

  @ApiModelProperty("序号")
  private String sn;

  @ApiModelProperty("是否禁用")
  private Short disabled;

  @ApiModelProperty("数据字典类型的id")
  private String typeId;
}
