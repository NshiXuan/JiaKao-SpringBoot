package com.sx.jk.pojo.po;

import com.sx.jk.common.foreign.anno.ForeignField;
import lombok.Data;

@Data
public class ExamPlace {
  //主键
  private String id;
  //名称
  private String name;
  //考场是哪个省份的
  @ForeignField(PlateRegion.class)
  private String provinceId;
  //考场是哪个城市的
  @ForeignField(PlateRegion.class)
  private String cityId;
  //考场的具体地址
  private String address;
  //纬度
  private Double latitude;
  //经度
  private Double longitude;
}
