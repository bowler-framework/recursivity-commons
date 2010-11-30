package com.recursivity.commons.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by IntelliJ IDEA.
 * User: wfaler
 * Date: Nov 30, 2010
 * Time: 1:19:58 AM
 * To change this template use File | Settings | File Templates.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Named {
    String value();
}
