package com.mauriciotogneri.androidutils.uibinder.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import androidx.annotation.IdRes;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(METHOD)
@Retention(RUNTIME)
public @interface OnItemLongClick
{
    @IdRes int value();
}