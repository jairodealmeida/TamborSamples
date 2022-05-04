package com.tambor.orm.database.dao.statement.transacts;


import com.tambor.orm.database.dao.statement.operation.WhereStatement;

public interface Deletable extends Transactionable{
    public WhereStatement where = new WhereStatement();
}
