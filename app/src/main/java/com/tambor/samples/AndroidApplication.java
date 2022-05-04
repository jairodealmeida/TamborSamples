package com.tambor.samples;

import android.app.Application;

import com.tambor.samples.contract.ApplicationComponent;
import com.tambor.samples.contract.DaggerApplicationComponent;
import com.tambor.samples.module.ApplicationModule;

public class AndroidApplication  extends Application {

    private ApplicationComponent applicationComponent ;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }
    public ApplicationComponent getApplicationComponent(){
        return applicationComponent;
    }
}
