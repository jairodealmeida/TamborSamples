package com.tambor.samples.database.models;


import com.tambor.orm.database.dao.entity.Entity;
import com.tambor.orm.database.dao.entity.annotation.GPAEntity;
import com.tambor.orm.database.dao.entity.annotation.GPAField;
import com.tambor.orm.database.dao.entity.annotation.GPAPrimaryKey;

@GPAEntity(name="tb_user")
public class User extends Entity {

    public static final String SESSION_KEY = "SessionKey";

    @GPAPrimaryKey(name = "id", ignore = true, type = Entity.INTEGER)
    private Integer id;
    @GPAField(name="full_name", type = Entity.VARCHAR)
    private String fullName;
    @GPAField(name="user_name", type = Entity.VARCHAR)
    private String userName;
    @GPAField(name="user_password", type = Entity.VARCHAR)
    private String userPassword;

    public User(){
        super();
    }
    
    /**
     * Used by insert without id, database are responsible
     * to generate a sequential id to primary key
     */
    public User(String fullName,
                String userName,
                String userPassword) {
        super();
        this.fullName = fullName;
        this.userName = userName;
        this.userPassword = userPassword;
    }
    /**
     * in this case the entity are instantied with the primary key information
     */
    public User(Integer id,
                String fullName,
                String userName,
                String userPassword) {
        super();
        this.id = id;
        this.fullName = fullName;
        this.userName = userName;
        this.userPassword = userPassword;
    }

    @Override
    public Long getId() {
        return Long.parseLong(id.toString());
    }

    @Override
    public void setId(Long id) {
        this.id = Integer.parseInt(id.toString());
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}