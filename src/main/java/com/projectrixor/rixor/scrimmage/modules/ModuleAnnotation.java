package com.projectrixor.rixor.scrimmage.modules;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleAnnotation
{
    public abstract String name();

    public abstract Class<? extends Module>[] depends() default {};
}
