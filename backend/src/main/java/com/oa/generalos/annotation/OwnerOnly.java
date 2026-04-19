package com.oa.generalos.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OwnerOnly {
    String ownerField() default "userId";
    
    String entityClass() default "";
}
