package com.yunchang.spring.visitor.core.annotation;

import java.lang.annotation.*;

/**
 * 标注反射的Service的名字
 * Created by jasontujun on 2018/1/10.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ReflectiveService {
	String value();
}
