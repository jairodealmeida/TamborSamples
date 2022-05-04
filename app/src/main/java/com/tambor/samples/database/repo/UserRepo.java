package com.tambor.samples.database.repo;

import android.content.Context;

import com.tambor.orm.database.Repository;
import com.tambor.samples.database.Config;
import com.tambor.samples.database.models.User;


public class UserRepo extends Repository<User> {
    public UserRepo(Context ctx) {
        super(ctx, Config.DATABASE_NAME, Config.getDBPath(ctx), Config.DATABASE_VERSION, Config.class);
    }
}
