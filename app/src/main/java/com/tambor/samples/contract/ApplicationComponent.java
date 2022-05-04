package com.tambor.samples.contract;

import android.content.SharedPreferences;

import com.tambor.samples.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ApplicationModule.class
})
public interface ApplicationComponent {
    SharedPreferences providePreferences();
}
