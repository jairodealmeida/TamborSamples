package com.tambor.orm.database.dao.statement.transacts;

import com.tambor.orm.database.dao.statement.operation.WhereStatement;

public interface Selectable {
	public WhereStatement where = new WhereStatement();
	public StringBuilder createStatement();
	
}
