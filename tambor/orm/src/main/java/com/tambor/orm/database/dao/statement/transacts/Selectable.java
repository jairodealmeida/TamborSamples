package com.tambor.orm.database.dao.statement.transacts;


import com.tambor.orm.database.dao.statement.operation.WhereStatement;

public interface Selectable {
    WhereStatement where = new WhereStatement();

    StringBuilder createStatement();

}
