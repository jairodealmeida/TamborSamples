package com.tambor.orm.database.dao.statement.transacts;


public interface Insertable extends Transactionable{

    public StringBuilder getParameters();

}
