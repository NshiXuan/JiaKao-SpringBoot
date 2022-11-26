package com.sx.jk.pojo.vo.req.save;

import com.sx.jk.common.foreign.anno.ForeignField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PlateRegionReqVo {
  @ApiModelProperty("【大于0代表更新，否则代表添加】")
  private String id;

  @NotBlank(message = "名称不能为空")
  @ApiModelProperty(value = "名称【不能为空】", required = true)
  private String name;

  @NotNull
  @Length(min = 1, max = 1, message = "车牌的长度只能为1")
  @ApiModelProperty(value = "车牌【车牌的长度只能为1，比如粤、桂、A等】", required = true)
  private String plate;

  @ApiModelProperty("父区域id【如果是城市，父区域id大于0，如果是省份，父区域id为0,默认0】")
  private String parentId;
}
