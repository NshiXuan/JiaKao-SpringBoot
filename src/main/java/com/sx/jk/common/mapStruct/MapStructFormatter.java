package com.sx.jk.common.mapStruct;

import org.mapstruct.Qualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Date;

public class MapStructFormatter {
  @Qualifier
  @Target(ElementType.METHOD) // 代表该注解用在方法身上
  @Retention(RetentionPolicy.CLASS) // 代表注解在编译期间使用
  public @interface Date2Millis { }

  // 这个注解必须要加上@Qualifier
  @Date2Millis
  public static Long date2Millis(Date date) {
    if (date == null) return null;
    // 传成时间戳
    return date.getTime();
  }

  @Qualifier
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.CLASS)
  public @interface Millis2Date { }

  @Date2Millis
  public static Date millis2Date (Long mills) {
    if (mills == null) return null;
    // 传成时间戳
    return new Date(mills);
  }
}
