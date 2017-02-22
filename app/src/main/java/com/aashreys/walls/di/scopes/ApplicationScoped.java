package com.aashreys.walls.di.scopes;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by aashreys on 18/02/17.
 */

@Scope
@Documented
@Retention(RUNTIME)
public @interface ApplicationScoped {}
