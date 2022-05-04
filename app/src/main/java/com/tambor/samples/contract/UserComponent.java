package com.tambor.samples.contract;

import android.content.SharedPreferences;

import com.tambor.samples.MainActivity;
import com.tambor.samples.database.models.User;
import com.tambor.samples.module.UserModule;
import com.tambor.samples.scope.UserActivity;

import dagger.Component;

@UserActivity
@Component(
        dependencies = {
                ApplicationComponent.class
        },
        modules = {
                UserModule.class
        }
)
public interface UserComponent {
    public void inject(User mainActivity);
    public User providerUser();
    SharedPreferences providePreferences();
}
