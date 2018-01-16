package com.yunchang.spring.visitor.core.annotation;

import java.lang.annotation.*;

/**
 * 标注可以用于反射的方法<br/>
 * 注意，同一个类中，不允许有2个或以上同名的使用该注解的方法!
 * Created by jasontujun on 2018/1/10.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ReflectiveMethod {

    /**
     * 标明方法接收的参数名列表
     * @return 如果返回值不为空，则表示方法入参名字列表(必须和方法定义的参数列表顺序一致)。<br/>
     *          如果返回值为空列表，则方法的参数必须有且只有一个，且为Map<String,String>类型
     */
    String[] params() default {};

    /**
     * 标明该方法的返回结果是否缓存
     * @return 默认返回false，表示不缓存；返回true，表示缓存。
     */
    boolean cacheResult() default false;
}
