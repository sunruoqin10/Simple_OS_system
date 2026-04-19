package com.oa.generalos.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequirePermission {
    String[] value();
    Logical logical() default Logical.AND;
    
    enum Logical {
        AND, OR
    }
}