package com.sx.jk.pojo.vo.req.save;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class DictItemReqVo {
  @ApiModelProperty("【大于0代表更新，否则代表添加】")
  private String id;

  @NotBlank(message = "名称不能为空")
  @ApiModelProperty(value = "名称【不能为空】", required = true)
  private String name;

  @NotBlank(message = "值不能为空")
  @ApiModelProperty(value = "值【不能为空】", required = true)
  private String value;

  @Min(value = 0, message = "序号不能是负数")
  @ApiModelProperty("排列顺序，不能是负数，默认0，值越大，就排在越前面")
  private String sn;

  @Range(min = 0, max = 1, message = "disable只能是0或者1")
  @ApiModelProperty("是否禁用，0启用，1禁用，默认0")
  private Short disabled;

  @NotNull
  @ApiModelProperty(value = "数据字典类型的id", required = true)
  private String typeId;
}
