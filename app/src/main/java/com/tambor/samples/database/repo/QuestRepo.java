package com.tambor.samples.database.repo;

import android.content.Context;

import com.tambor.orm.database.Repository;
import com.tambor.samples.database.Config;
import com.tambor.samples.database.models.Quest;


public class QuestRepo extends Repository<Quest> {
    public QuestRepo(Context ctx) {
        super(ctx, Config.DATABASE_NAME, Config.getDBPath(ctx), Config.DATABASE_VERSION, Config.class);
    }
}
