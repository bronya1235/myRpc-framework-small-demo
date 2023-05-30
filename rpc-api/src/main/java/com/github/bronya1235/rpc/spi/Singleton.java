package com.github.bronya1235.rpc.spi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: Bao
 * @Date: 2023/5/30-05-30-9:32
 * @Description com.github.bronya1235.rpc.spi
 * @Function
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Singleton {
}
