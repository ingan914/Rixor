package com.projectrixor.rixor.scrimmage.modules;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleDescription
{
  public abstract String name();

  public abstract Class<? extends Module>[] depends() default {};
}
