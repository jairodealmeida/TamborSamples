package com.tambor.samples.module;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    private static final String PREF_KEY = "pk";
    private Application app;
    public ApplicationModule(Application app ){
        this.app = app;
    }

    @Provides
    @Singleton
    public SharedPreferences providePreferences(){
        return this.app.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
    }

}
