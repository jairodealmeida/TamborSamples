package com.tambor.orm.database.dao.statement.transacts;


import com.tambor.orm.database.dao.statement.operation.WhereStatement;

public interface Updatable extends Transactionable {
    WhereStatement where = new WhereStatement();

    StringBuilder getParameters();

}
