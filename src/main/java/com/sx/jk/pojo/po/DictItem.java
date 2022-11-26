package com.sx.jk.pojo.po;

import com.sx.jk.common.foreign.anno.ForeignField;
import lombok.Data;

@Data
public class DictItem {
  //主键
  private String id;
  //名称
  private String name;
  //值
  private String value;
  //排列顺序，默认0，值越大，就排在越前面
  private String sn;
  //是否禁用，0启用，1禁用
  private Short disabled;
  //所属的类型
  @ForeignField(DictType.class)
  private String typeId;
}
