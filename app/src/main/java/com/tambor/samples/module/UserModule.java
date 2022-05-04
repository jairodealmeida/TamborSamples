package com.tambor.samples.module;

import com.tambor.samples.database.models.Quest;
import com.tambor.samples.database.models.User;
import com.tambor.samples.scope.UserActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class UserModule {
    @Provides
    @UserActivity
    public User provideUSer(){
        return  new User();
    }

    @Provides
    @UserActivity
    public Quest provideUQuest(){
        return  new Quest();
    }
}
