package com.tambor.orm.database.dao.entity.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface GPAFieldBean {
	String name();
	int type();
	Class<?> clazz();
}
