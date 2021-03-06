package com.tambor.orm.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.prefs.Preferences;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tambor.orm.database.dao.entity.Entity;
import com.tambor.orm.database.dao.entity.annotation.GPAEntity;
import com.tambor.orm.database.dao.entity.annotation.GPAField;
import com.tambor.orm.database.dao.entity.annotation.GPAFieldBean;
import com.tambor.orm.database.dao.entity.annotation.GPAPrimaryKey;
import com.tambor.orm.database.dao.model.FieldTO;
import com.tambor.orm.database.dao.model.TransferObject;
import com.tambor.orm.database.dao.statement.StatementFactory;
import com.tambor.orm.database.dao.statement.operation.OrderByStatement;
import com.tambor.orm.database.dao.statement.operation.SelectStatement;
import com.tambor.orm.database.dao.statement.operation.StatementArguments;
import com.tambor.orm.database.dao.statement.operation.WhereStatement;
import com.tambor.orm.utils.CollectionUtils;
import com.tambor.orm.utils.DateUtil;
import com.tambor.orm.utils.EntityUtil;

public class Repository<T>  {
	
	private ArrayList<FieldTO> primaryKeyTos;
	
	private ArrayList<FieldTO> foreignKeyTos;
	
	private ArrayList<FieldTO> fieldTos;
	
	private SQLiteDatabase db;  
	private Class<?> entity;
	
	protected Context context;
	
	public String getTableName() {
		Annotation annoClass = entity.getAnnotation(GPAEntity.class);
		GPAEntity anno = (GPAEntity)annoClass;
		String tableName = anno.name();
		return tableName;
	}


	
	public Repository( 
			Context ctx, 
			String dataBaseName, 
			String dataBasePath,
			int dataBaseVersion,
			Class<?> entity){
	    this.context = ctx;
	    this.entity = entity;

		try{
		    SQLiteHelper dbHelper = new SQLiteHelper(ctx,
		    		dataBaseName, 
		       		dataBaseVersion,   
		       		null,
                    createDeleteScript()
			);
		    db = dbHelper.getWritableDatabase();
 			if(db!=null){
				//db.execSQL(createDeleteScript());
	        	db.execSQL(createScript());
	        }
		} catch (Exception e) {
			Log.e("ERROR",e.getLocalizedMessage(),e);
		}
	}
	
	/**
	 * 
	 */
	public void close(){
        db.close();
    }
	/**
	 * 
	 * @return
	 */
	public SQLiteDatabase getDb(){
		return db;
	}

	public String createDeleteScript() {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("drop table if exists " +getTableName()+";");
			Log.i("GPALOG", sb.toString());
			return sb.toString();

		} catch (Exception e) {
			Log.e("GPALOG" , e.getMessage(),e);
		}
		return null;
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */

	public String createScript() {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("create table if not exists ");
			sb.append(getTableName());
			sb.append(createStatementParams(entity,true));
			Log.i("GPALOG", sb.toString());
			return sb.toString();	
			
		} catch (Exception e) {
			Log.e("GPALOG" , e.getMessage(),e); 
		}
		return null;
		
	}
	public Field[] getDeclaredFields(Class<?> entity){

		return EntityUtil.getDeclaredFields(true,entity);
	}

	/**
	 * 
	 * @return
	 */
	public String updateScript(){
		//TODO make with api
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public String deleteScript(){
		//TODO make with api
		return null;
	}
	/**
	 * method to prepare the statments by transact entity persistences
	 * using to get a fields of entitys and setting a obfuscate values
	 * in statement sql
	 * Translate a entity class to FieldTO (field transfer objects)
	 * this objects TO are using in persistence atributs of tables
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public String createStatementParams(Class<?> entity, boolean usePrimaryKey) 
			throws Exception{
		
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		Field[] fields = getDeclaredFields(entity);
		List<Field> annoFields = new ArrayList<Field>();
		EntityUtil.getAnnotationFields(fields,annoFields);
		//trunk entity to persistence
		for(int i=0; i<annoFields.size(); i++){
			Field reflectionField = annoFields.get(i);
			if(reflectionField!=null){
				reflectionField.setAccessible(true);
				Annotation annoField = reflectionField.getAnnotation(GPAField.class);
				Annotation annoFieldPK = reflectionField.getAnnotation(GPAPrimaryKey.class);
				Annotation annoFieldBean = reflectionField.getAnnotation(GPAFieldBean.class);
				/* 
				 ainda falta validar a chave prim???ria do objeto
				 por enquanto so esta prevendo pk usando sequence no banco
				 objeto id sempre  gerado no banco por uma sequence
				*/
				if(annoFieldPK!=null && annoFieldPK instanceof GPAPrimaryKey){
					GPAPrimaryKey pk = (GPAPrimaryKey)annoFieldPK;
					String name = pk.name();
					sb.append( name + " integer primary key");
					if(pk.ignore() == true){
						sb.append(" autoincrement ");
					}
					if(i<annoFields.size()-1){
						sb.append(",");
					}
					continue;
				}
				if(annoField!=null && annoField instanceof GPAField){
					GPAField field = (GPAField)annoField;
					String name = field.name();
					Class<?> type = reflectionField.getType();
					sb.append( name + " " + getSqLiteType(type));
					if(i<annoFields.size()-1){
						sb.append(",");
					}
					continue;
				}
				if(annoFieldBean!=null && annoFieldBean instanceof GPAFieldBean){
					GPAFieldBean field = (GPAFieldBean)annoFieldBean;
					String name = field.name();
					Class<?> type = reflectionField.getType();
					//sb.append( name + " " + getSqLiteType(type));
					sb.append( name + " bigint");
					if(i<annoFields.size()-1){
						sb.append(",");
					}
					continue;
				}
			}
		}
		sb.append(");");
		return sb.toString();
	}
	/**
	 * TODO alternate from Class types to parametrized Datatypes, 
	 * method to get SqLite data types
	 * http://www.sqlite.org/datatype3.html
	 */
	
	private String getSqLiteType(Class<?> value){
		String sn = value.getSimpleName();
		if(sn.equalsIgnoreCase("string")){
			return "text";
		}else if(
			sn.equalsIgnoreCase("int") || 
		   	sn.equalsIgnoreCase("integer") ||
            sn.equalsIgnoreCase("boolean")){
				return "integer";
		}else if(
			sn.equalsIgnoreCase("long") ||  
			sn.equalsIgnoreCase("bigdecimal")){
				return "bigint";
		} else if(
			sn.equalsIgnoreCase("double")|| 
			sn.equalsIgnoreCase("float")){
				return "float";
		} else if(
			sn.equalsIgnoreCase("byte[]")){
				return "blob";
		}else if(
			sn.equalsIgnoreCase("date")){
				return "timestamp";
		} else{
			throw new NullPointerException("type not found " + sn);
		}
	}
	/**
	 * method to prepare the statments by transact entity persistences
	 * using to get a fields of entitys and setting a obfuscate values
	 * in statement sql
	 * Translate a entity class to FieldTO (field transfer objects)
	 * this objects TO are using in persistence atributs of tables
	 * TODO alter by annotation field name
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private void prepareFields(Entity entity)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		primaryKeyTos = new ArrayList<FieldTO>();
		fieldTos = new ArrayList<FieldTO>();
		foreignKeyTos = new ArrayList<FieldTO>();
		Field[] fields = getDeclaredFields(entity.getClass());
		
		//trunk entity to persistence
		for(int i=0; i<fields.length; i++){
			Field reflectionField = fields[i];
			if(reflectionField!=null){
				reflectionField.setAccessible(true);
				Annotation annoField = reflectionField.getAnnotation(GPAField.class);
				Annotation annoFieldPK = reflectionField.getAnnotation(GPAPrimaryKey.class);
				Annotation annoFieldBean = reflectionField.getAnnotation(GPAFieldBean.class);
				/* 
				 ainda falta validar a chave primria do objeto
				 por enquanto so esta prevendo pk usando sequence no banco
				 objeto id sempre  gerado no banco por uma sequence
				*/
				if(annoFieldPK!=null && annoFieldPK instanceof GPAPrimaryKey){
					GPAPrimaryKey pk = (GPAPrimaryKey)annoFieldPK;
					//if(pk.ignore() == true){
					//	continue;
					//}else{
					String name = pk.name();
					Object value = reflectionField.get(entity);
					primaryKeyTos.add(new FieldTO(name, value));
					continue;
					//}
				}
				if(annoField!=null && annoField instanceof GPAField){
					GPAField field = (GPAField)annoField;
					String name = field.name();
					Object value = reflectionField.get(entity);
					fieldTos.add(new FieldTO(name, value));
					continue;
				}
				if(annoFieldBean!=null && annoFieldBean instanceof GPAFieldBean){
					GPAFieldBean field = (GPAFieldBean)annoFieldBean;
					String name = field.name();
					//TODO use this to instance FK
					Class<?> clazz = field.clazz();
					Object value = reflectionField.get(entity);
					foreignKeyTos.add(new FieldTO(name, value));
					continue;
				}
			}
		}
	}
	/**
	 * method will use to insert objects
	 * @return "success or fail"
	 */	
	public long insert(Entity entity){
		
		try {
			entity.setStatus(EntityUtil.STATUS.NEWER.name());
			this.prepareFields(entity);
			String tableName = this.getTableName();
			TransferObject to = new TransferObject(
						tableName, 
						primaryKeyTos, fieldTos, foreignKeyTos, 
						TransferObject.INSERT_TYPE);
			
			return transactStatements(to, entity.isPkInformed());
		} catch (Exception e) {
			Log.e("GPALOG" , e.getMessage(),e); 
		}
		return 0;
	}

	public long upsert(Entity entity, Class<?> clazz){
		try {
			if(selectById(entity, clazz)==null){
				entity.setStatus(EntityUtil.STATUS.NEWER.name());
				return insert(entity);
			}else{
				entity.setStatus(EntityUtil.STATUS.UPDATED.name());
				return update(entity);
			}
		} catch (Exception e) {
			Log.e("GPALOG" , e.getMessage(),e);
		}
		return 0;
	}

	public long insert(List<Entity> entities){
		try {
			int sum = 0;
			getDb().beginTransaction();
			for (Entity entity : entities) {
				entity.setStatus(EntityUtil.STATUS.NEWER.name());
				sum += insert(entity);	
			}
			getDb().setTransactionSuccessful();	
			return sum;
		} catch (Exception e) {
			Log.e("GPALOG" , e.getMessage(),e); 
		}finally{
			getDb().endTransaction();
		}
		return 0;
	}
	public long upsert(List<Entity> entities, Class<?> clazz){
		try {
			int sum = 0;
			getDb().beginTransaction();
			for (Entity entity : entities) {
				if(selectById(entity, clazz)==null){
					entity.setStatus(EntityUtil.STATUS.NEWER.name());
					sum += insert(entity);	
				}else{
					entity.setStatus(EntityUtil.STATUS.UPDATED.name());
					sum += update(entity);	
				}
			}
			getDb().setTransactionSuccessful();	
			return sum;
		} catch (Exception e) {
			Log.e("GPALOG" , e.getMessage(),e); 
		}finally{
			getDb().endTransaction();
		}
		return 0;
		
		
	}
	public long update(List<Entity> entities){
		try {
			int sum = 0;
			getDb().beginTransaction();
			for (Entity entity : entities) {
				//entity.setStatus(EntityUtil.STATUS.UPDATED.name());
				sum += update(entity);	
			}
			getDb().setTransactionSuccessful();	
			return sum;
		} catch (Exception e) {
			Log.e("GPALOG" , e.getMessage(),e); 
		}finally{
			getDb().endTransaction();
		}
		return 0;
	}
	public long delete(List<Entity> entities){
		try {
			int sum = 0;
			getDb().beginTransaction();
			for (Entity entity : entities) {
				entity.setStatus(EntityUtil.STATUS.DELETED.name());
				sum += delete(entity);	
			}
			getDb().setTransactionSuccessful();	
			return sum;
		} catch (Exception e) {
			Log.e("GPALOG" , e.getMessage(),e); 
		}finally{
			getDb().endTransaction();
		}
		return 0;
	}
	/**
	 * method will use to update objects
	 * @return "success or fail"
	 */
	public long update(Entity entity){
		try {
			//entity.setStatus(EntityUtil.STATUS.UPDATED.name());
			this.prepareFields(entity);
			String tableName = this.getTableName();
			TransferObject to = new TransferObject(
						tableName,
						primaryKeyTos, fieldTos, foreignKeyTos,
						TransferObject.UPDATE_TYPE);
			return updateStatements(to);
		} catch (Exception e) {
			Log.e("GPALOG" , e.getMessage(),e); 
		}
		return 0;
	}
	
	/**
	 * method will use to delete objects
	 * @return "success or fail"
	 */
	public long delete(Entity entity){
		try {
			entity.setStatus(EntityUtil.STATUS.DELETED.name());
			this.prepareFields(entity);
			String tableName = this.getTableName();
			TransferObject to = new TransferObject(
						tableName,
						primaryKeyTos, fieldTos, foreignKeyTos,
						TransferObject.DELETE_TYPE);
			

			
			return deleteStatements(to);
		} catch (Exception e) {
			Log.e("GPALOG" , e.getMessage(),e); 
		}
		return 0;
	}
	
	
	 public long deleteBy(WhereStatement whereClause){
		try {
			long result = deleteStatements(whereClause);
			return result;
		} catch (Exception e) {
			Log.e("GPALOG", e.getMessage(),e);
		} finally {
			
		}
		return 0;
	}

	
	
	public long removeAll(){
		try {
			int rl = getDb().delete(this.getTableName(), "1", null);
			//getDb().execSQL("delete from "+ this.getTableName());
			return Long.parseLong(""+rl);
		
		} catch (Exception e) {
			Log.e("GPALOG" , e.getMessage(),e); 
		}finally {
			//getDb().close();
		}
		return 0;
	}




	public void selectAll(List<List<Entity>> entryes, Class<?> entity, int pagination){
		List<Entity> entrys = new ArrayList<>();
		selectAll(entrys, entity);
		if(entrys!=null && entrys.size()>0) {
			entryes.addAll( CollectionUtils.split(entrys, pagination) );
		}else{
			Log.i("INFO", "List without entrys");
		}
	}

	public void selectAll(List<Entity> entryes, Class<?> entity){
		long start, end;
		start = (new Date()).getTime();
		List<TransferObject> items = new ArrayList<TransferObject>();
		Cursor c = null;
		try {
			String tableName = this.getTableName();
			c = getDb().query(
					tableName, null, null, null, null, null, null);
			c.moveToFirst();
			while(!c.isAfterLast()){
				TransferObject bean = this.fill(c);
				entryes.add( fillVO(bean, entity) );
				c.moveToNext();
			}
			c.close();

		} catch (Exception e) {
			Log.e("GPALOG" , e.getMessage(),e);
		}finally{
			if(c!=null){c.close();}
			end = (new Date()).getTime();
			Log.i("GPALOG", "Time to query: " + (end - start) + " ms");
		}

	}


	public List<TransferObject> selectAll(Class<?> entity){
		long start, end;
		start = (new Date()).getTime();
		List<TransferObject> items = new ArrayList<TransferObject>();
		Cursor c = null;
		try {
			String tableName = this.getTableName();
			c = getDb().query(
			tableName, null, null, null, null, null, null);  
			c.moveToFirst();
			while(!c.isAfterLast()){  
				TransferObject bean = this.fill(c);
				items.add( bean );
				c.moveToNext();  
			} 
			c.close();
			return items;
		} catch (Exception e) {
			Log.e("GPALOG" , e.getMessage(),e); 
		}finally{
			if(c!=null){c.close();}
			end = (new Date()).getTime();
			Log.i("GPALOG", "Time to query: " + (end - start) + " ms");
		}
		return null;
	}
	
	public List<TransferObject> select(StringBuilder sql, String[] args){
		long start, end;
		start = (new Date()).getTime();
		Cursor c = null;
		try {
			List<TransferObject> items = new ArrayList<TransferObject>();
			//String[] selectionArgs = new String[]{String.valueOf( entity.getMovapitenculturaid() )};
			c = getDb().rawQuery(sql.toString(), args);
			c.moveToFirst();  
			while(!c.isAfterLast()){  
				TransferObject bean = this.fill(c);
	        	items.add( bean ); 
	        	c.moveToNext();
			}
			return items;
		} catch (Exception e) {
			Log.e("GPALOG" , e.getMessage(),e); 
		}finally{
			if(c!=null){c.close();}
			end = (new Date()).getTime();
			Log.i("GPALOG", "Time to query: " + (end - start) + " ms");
		}
		return null;
	}
	/**
	 * Method to get resultset collections from database
	 * this methodo works with select sintax

	 * @return List<TransferObject> select entities result from database
	 */
	public TransferObject selectSingle(WhereStatement whereClause, OrderByStatement orderByClause){
		long start, end;
	    start = (new Date()).getTime();
	    String tableName = this.getTableName();
	   // List<TransferObject> items = new ArrayList<TransferObject>();
	    Cursor c = null;
	    StatementArguments arguments = new StatementArguments(tableName);
	    if(whereClause!=null){
	    	arguments.setWhereClause(whereClause.createWhereStatement().toString());
	    }
	    if(orderByClause!=null){
	    	arguments.setOrderByClause(orderByClause.createWhereStatement().toString());
	    }
	    SelectStatement sql = new SelectStatement(arguments);
		try {
			c = getDb().rawQuery(sql.createStatement().toString(), whereClause.getArguments());
			Log.i("GPA", sql.createStatement().toString());
			c.moveToFirst();  
	        if(!c.isAfterLast()){  
	        	TransferObject bean = this.fill(c);
	        	return bean; 
	        }      
		} catch (Exception e) {
			Log.e("GPALOG" , e.getMessage(),e); 
		}finally{
			   if(c!=null){c.close();}
	           end = (new Date()).getTime();
	           Log.i("GPALOG", "Time to query: " + (end - start) + " ms");
		}
		return null;
	}
	/**
	 * Method to get resultset collections from database
	 * this methodo works with select sintax

	 * @return List<TransferObject> select entities result from database
	 */
	public List<TransferObject> select(WhereStatement whereClause, OrderByStatement orderByClause){
		long start, end;
	    start = (new Date()).getTime();
	    String tableName = this.getTableName();
	    List<TransferObject> items = new ArrayList<TransferObject>();
	    Cursor c = null;
	    StatementArguments arguments = new StatementArguments(tableName);
	    if(whereClause!=null){
	    	arguments.setWhereClause(whereClause.createWhereStatement().toString());
	    }
	    if(orderByClause!=null){
	    	arguments.setOrderByClause(orderByClause.createWhereStatement().toString());
	    }
	    SelectStatement sql = new SelectStatement(arguments);
		try {
			c = getDb().rawQuery(sql.createStatement().toString(), whereClause.getArguments());
			Log.i("GPA", sql.createStatement().toString());
	        if(c.moveToFirst()){
				while(!c.isAfterLast()){
					TransferObject bean = this.fill(c);
					items.add( bean );
					c.moveToNext();
				}
			}
	        c.close();
	        return items;
		} catch (Exception e) {
			Log.e("GPALOG" , e.getMessage(),e); 
		}finally{
			   if(c!=null){c.close();}
	           end = (new Date()).getTime();
	           Log.i("GPALOG", "Time to query: " + (end - start) + " ms");
		}
		return null;
	}
	
	
	
	public List<TransferObject> selectById(Entity request){
		List<FieldTO> fields = request.getPKFieldTos();
		WhereStatement whereClause = new WhereStatement(fields);
	    return select(whereClause,null);
	}
	public Entity selectById(Entity request, Class<?> typeClass){
		try {
			List<FieldTO> fields = request.getPKFieldTos();
			WhereStatement where = new WhereStatement(fields);
			TransferObject to = selectSingle(where,null);
			if(to!=null){
				return fillVO(to, typeClass);
			}
		} catch (Exception e) {
			Log.e("GPALOG", e.getLocalizedMessage(),e);
		}
		return null;
	}

	public void selectByStatus(EntityUtil.STATUS status, Class<?> typeClass, List<Entity> result){
		try {
			FieldTO f = new FieldTO("status", status.name());
			WhereStatement where = new WhereStatement(f);
			List<TransferObject> tos = select(where);
			if(tos!=null){
				for (TransferObject transferObject : tos) {
					result.add( fillVO(transferObject, typeClass) );
				}
			}
		} catch (Exception e) {
			Log.e("GPALOG", e.getLocalizedMessage(),e);
		}
	}


	public List<Entity> selectListById(Entity request, Class<?> typeClass){
		try {
			List<FieldTO> fields = request.getPKFieldTos();
			WhereStatement where = new WhereStatement(fields);
			List<TransferObject> tos = select(where);
			if(tos!=null){
				List<Entity> result = new ArrayList<Entity>();
				for (TransferObject transferObject : tos) {
					result.add( fillVO(transferObject, typeClass) );
				}
				return result;
			}
		} catch (Exception e) {
			Log.e("GPALOG", e.getLocalizedMessage(),e);
		}	
		return null;
	}

	public Entity selectSingleById(Entity request, Class<?> typeClass){
		try {
			List<FieldTO> fields = request.getPKFieldTos();
			WhereStatement where = new WhereStatement(fields);
			List<TransferObject> tos = select(where);
			if(tos!=null){
				for (TransferObject transferObject : tos) {
					return ( fillVO(transferObject, typeClass) );
				}
			}
		} catch (Exception e) {
			Log.e("GPALOG", e.getLocalizedMessage(),e);
		}
		return null;
	}


	public Entity fillVO(TransferObject to, Class<?> typeClass){ 
		try {
			Entity e = (Entity)typeClass.newInstance();
			e.valuable(to);
			return e;
		} catch (Exception e) {
			Log.e("GPALOG",e.getLocalizedMessage(),e);
		}
		return null;
	}
	/**
	 * Method to get resultset collections from database
	 * this methodo works with select sintax

	 * @return List<TransferObject> select entities result from database
	 */
	public List<TransferObject> select(WhereStatement whereClause){
	    return select(whereClause,null);
	}
	public  List<TransferObject> rawQuery(StringBuilder sql, String[] args){
	    long start, end;
	    start = (new Date()).getTime();
	    List<TransferObject> items = new ArrayList<TransferObject>();
	    Cursor c = null;
		try {
			c = getDb().rawQuery(sql.toString(), args);
			Log.i("GPA", sql.toString());
	        c.moveToFirst();  
	        while(!c.isAfterLast()){  
	        	TransferObject bean = this.fill(c);
	        	items.add( bean ); 
	        	c.moveToNext();  
	        } 
	        c.close();
	        return items;
		} catch (Exception e) {
			Log.e("GPALOG" , e.getMessage(),e); 
		}finally{
			   if(c!=null){c.close();}
	           end = (new Date()).getTime();
	           Log.i("GPALOG", "Time to query: " + (end - start) + " ms");
		}
		return null;
		
	}


	public Long getCount(){
		long start, end;
		start = (new Date()).getTime();
		String tableName = getTableName();
		Cursor c = null;
		try {
			c = getDb().query(
					tableName,
					new String[]{"count(*)"},
					null,
					null,
					null,
					null,
					null);
			c.moveToFirst();
			if(!c.isAfterLast()){
				Long value = c.getLong( 0 );
				return value;
			}
		} catch (Exception e) {
			Log.e("GPALOG" , e.getMessage(),e);
		}finally{
			if(c!=null){c.close();}
			end = (new Date()).getTime();
			Log.i("GPALOG", "Time to query: " + (end - start) + " ms");
		}
		return null;
	}
	/**
	 * Method to get resultset collections from database
	 * this methodo works with select sintax

	 * @return ArrayList<TransferObject> select entities result from database
	 */
	public TransferObject selectMax(String maxField){
		long start, end;
		start = (new Date()).getTime();
		String tableName = getTableName();
		Cursor c = null;
		try {
			c = getDb().query(
					tableName,
					new String[]{"max( " + maxField + " ) as id "},
					null,
					null,
					null,
					null,
					null);
			c.moveToFirst();
			if(!c.isAfterLast()){
				int index = c.getColumnIndex("id");
				if(index<0) return null;
				Long value = c.getLong( index);
				ArrayList<FieldTO> fieldsResult = new ArrayList<FieldTO>();
				fieldsResult.add(new FieldTO("max", value));
				TransferObject bean = new TransferObject(tableName,
						primaryKeyTos, fieldsResult, foreignKeyTos,
						TransferObject.READ_TYPE);
				return bean;
			}
		} catch (Exception e) {
			Log.e("GPALOG" , e.getMessage(),e);
		}finally{
			if(c!=null){c.close();}
			end = (new Date()).getTime();
			Log.i("GPALOG", "Time to query: " + (end - start) + " ms");
		}
		return null;
	}
	/**
	 * Method to transact object into database
	 * @param feature - Instantiate transfer object
	 * @return String success or fail
	 * @throws Exception
	 */
	private Cursor selectStatements(TransferObject feature)  throws Exception {
	    //ContentValues cv = new ContentValues();  
	    List<FieldTO> fields = feature.getFields();
	    String[] selectionArgs = new String[fields.size()];
	    for (int i=0; i<fields.size();i++) {
	    	FieldTO fieldTO = fields.get(i);
	    	String name = fieldTO.getName();
	    	Object value = fieldTO.getValue();
	    	selectionArgs[i] = String.valueOf(value);

		}
	   // return getDb().insert(getTableName(), null, cv);
		StatementFactory statement = new StatementFactory(feature);
        StringBuilder sqls = statement.createStatementSQL();
        Cursor c = getDb().rawQuery(sqls.toString(), selectionArgs);
	    return c;
	}
	private long transactStatements(TransferObject feature, boolean pkInfomed)  throws Exception {
		ContentValues cv = new ContentValues();  
		List<FieldTO> pkFields = feature.getPrimaryKeys();
		List<FieldTO> fields = feature.getFields();
	    List<FieldTO> fkFields = feature.getForeignKeys();
	    if(pkInfomed){
	    	EntityUtil.parseFieldsToContentValues(pkFields, cv);
	    }
	    EntityUtil.parseFieldsToContentValues(fields, cv);
	    EntityUtil.parseFieldsToContentValues(fkFields, cv);
	    
	    return getDb().insertOrThrow(getTableName(), null, cv);
	}
	
	private long updateStatements(TransferObject feature)  throws Exception {
		ContentValues cv = new ContentValues();  
		List<FieldTO> fields = feature.getFields();
		EntityUtil.parseFieldsToContentValues(fields, cv);
		
	    List<FieldTO> pkfields = feature.getPrimaryKeys();
	    StringBuilder whereClause = new StringBuilder();
	    List<String> args = new ArrayList<String>();
	    for (int i=0; i<pkfields.size();i++) {
	    	FieldTO pkFieldTO = pkfields.get(i);
	    	String name = pkFieldTO.getName();
	    	Object value = pkFieldTO.getValue();
	    	whereClause.append(name + "=?");
	    	args.add(value.toString());
	    }
	    //String[] whereArgs = new String[args.size()];
	    String[] whereArgs = args.toArray(new String[args.size()]);
	    if(whereClause.length()>0 && whereArgs!=null && whereArgs.length>0){
	    	return getDb().update(getTableName(), cv,whereClause.toString(), whereArgs);
	    }else{
	    	return 0;
	    }
	}
	private long deleteStatements(TransferObject feature)  throws Exception {
		ContentValues cv = new ContentValues();  
	
	    List<FieldTO> pkfields = feature.getPrimaryKeys();
	    StringBuilder whereClause = new StringBuilder();
	    List<String> args = new ArrayList<String>();
	    for (int i=0; i<pkfields.size();i++) {
	    	FieldTO pkFieldTO = pkfields.get(i);
	    	String name = pkFieldTO.getName();
	    	Object value = pkFieldTO.getValue();
	    	whereClause.append(name + "=?");
	    	args.add(value.toString());
	    }
	    //String[] whereArgs = new String[args.size()];
	    String[] whereArgs = args.toArray(new String[args.size()]);
	    if(whereClause.length()>0 && whereArgs!=null && whereArgs.length>0){
	    	return getDb().delete(getTableName(), whereClause.toString(), whereArgs);
	    }else{
	    	return 0;
	    }
	}
	private long deleteStatements(WhereStatement whereClause )  throws Exception {
	    StringBuilder where = whereClause.createWhereStatement();
	    String[] args = whereClause.getArguments();
	    return getDb().delete(getTableName(), where.toString(), args);
	}
	/*
	private long transact2(TransferObject feature)  throws Exception {
		switch (feature.getTransactionType()) {
		case TransferObject.DELETE_TYPE:
			getDb().
		break;

		default:
			break;
		}
	}*/
	/**
	 * Method to transact objects into database
	 * @param features - Instantiate transfer objects
	 * @return String success or fail
	 * @throws Exception
	 */
	private long transact(ArrayList<TransferObject> features)  throws Exception {
	    long start, end;
	    start = (new Date()).getTime();
        if(features!=null && features.size()>0){
        	db.beginTransaction();
        	for (TransferObject transferObject : features) {
        		boolean hasPK = (transferObject.getPrimaryKeys()!=null && transferObject.getPrimaryKeys().size()>0);
        		if(transactStatements(transferObject, hasPK)<=0){
        			throw new NullPointerException("Insert error");
        		}
			}
        	db.setTransactionSuccessful();
        	db.endTransaction();
            end = (new Date()).getTime();
            Log.i("GPALOG","Time to query: " + (end - start) + " ms");
            return features.size();        
        }else{
            throw new NullPointerException("don't have features to transact");
        } 
    }/*
	private TransferObject fill(Cursor c){
		List<FieldTO> fields = new ArrayList<FieldTO>();
		String[] names = c.getColumnNames();
	    for (int i=0; i<names.length; i++ ) {
	    	String name = names[i];
	    	//Object value = fieldTO.getValue();
	    	if(!c.isNull(i)){
	    		
	    		if(value instanceof String)		{fields.add( new FieldTO(name, (String) value ));}
		    	if(value instanceof Integer)	{fields.add( new FieldTO(name, (Integer) value));}
		    	if(value instanceof Long)		{fields.add( new FieldTO(name, (Long) value));} 
		    	if(value instanceof BigDecimal) {fields.add( new FieldTO(name, (Long) value));}
		    	if(value instanceof Double) 	{fields.add( new FieldTO(name, (Double) value));}
		    	if(value instanceof Float)		{fields.add( new FieldTO(name, (Float) value));}
		    	if(value instanceof byte[])		{fields.add( new FieldTO(name, (byte[]) value ));} 
		    	if(value instanceof Short)		{fields.add( new FieldTO(name, (Short) value));}
	    	}
		}
		
	
	
		for(int i=0; i< c.getColumnCount(); i++){
			fields.add(new FieldTO(c.getColumnName(i),c.getBlob(i)));
		}
		
		
		TransferObject to = new TransferObject(
				getTableName(),
				fieldTos,
				TransferObject.READ_TYPE);
		return to;
	}*/
	
	
	private TransferObject fill(Cursor c) throws IllegalAccessException, InstantiationException{
		
		ArrayList<FieldTO> fieldsResult = new ArrayList<FieldTO>();
		
		Field[] fields = getDeclaredFields(entity);
		
		//trunk entity to persistence
		for(int i=0; i<fields.length; i++){
			Field reflectionField = fields[i];
			if(reflectionField!=null){
				reflectionField.setAccessible(true);
				
				Annotation annoField = reflectionField.getAnnotation(GPAField.class);
				
				/* 
				 ainda falta validar a chave primria do objeto
				 por enquanto so esta prevendo pk usando sequence no banco
				 objeto id sempre  gerado no banco por uma sequence
				*/
				
				if(annoField!=null && annoField instanceof GPAField){
					
					
					GPAField field = (GPAField)annoField;
					String name = field.name();
					int type = field.type();
			    	//if(!c.isNull(i)){
			    		fieldsResult.add( getFieldAt(c, name, type, i) );
			    	//}
					continue;
				}else{
					Annotation annoFieldPK = reflectionField.getAnnotation(GPAPrimaryKey.class);
					if(annoFieldPK!=null && annoFieldPK instanceof GPAPrimaryKey){
						GPAPrimaryKey field = (GPAPrimaryKey)annoFieldPK;
						String name = field.name();
						int type = field.type();
						//if(!c.isNull(i)){
							fieldsResult.add( getFieldAt(c, name, type, i) );
						//}
						continue;
					}else{
						Annotation annoFieldBean = reflectionField.getAnnotation(GPAFieldBean.class);
						if(annoFieldBean!=null && annoFieldBean instanceof GPAFieldBean){
							GPAFieldBean field = (GPAFieldBean)annoFieldBean;
							String name = field.name();
							int type = field.type();
							//if(!c.isNull(i)){
								fieldsResult.add( getFieldAt(c, name, type, i) );
							//}
							continue;
						}
					}
				}

	
			}
		}
		TransferObject to = new TransferObject(
				getTableName(),
				primaryKeyTos, fieldsResult, foreignKeyTos,
				TransferObject.READ_TYPE);
		return to;
	}
	
	private FieldTO getFieldAt(Cursor c, String name, int type, int columnIndex) {
		try {
			return new FieldTO(name, getValueFromName(c,name,type));
		} catch (Exception e) {
			Log.e("GPALOG" , "fail to mapping " + name + " : " + e.getMessage(),e); 
		}
	    return null;
	}
	private Object getValueFromName(Cursor c, String name, int type) throws IndexOutOfBoundsException {
		int index = c.getColumnIndex(name);
		if(index<0) {
			throw new IndexOutOfBoundsException(name + " index not found");
		}
		switch (type) {
			case Entity.VARCHAR:
			case Entity.BOOLEAN:
				return c.getString(index);
			case Entity.INTEGER:
				return c.getInt(index);
			case Entity.LONG:
			case Entity.BEAN:
				return c.getLong(index);
			case Entity.FLOAT:
				return  c.getFloat(index);
			case Entity.DATE:
				return DateUtil.parseDate( c.getString(index) );
			case Entity.BLOB:
				return  c.getBlob(index);
			case Entity.DOUBLE:
				return c.getDouble(index);
			default:
				com.tambor.orm.utils.Log.e(name + " not found");
				return null;
		}
	}

}
