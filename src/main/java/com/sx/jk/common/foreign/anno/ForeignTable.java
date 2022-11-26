package com.sx.jk.common.foreign.anno;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ForeignTable {
    /**
     * 表名
     */
    String value() default "";

    /**
     * 表名
     */
    String name() default "";
}
