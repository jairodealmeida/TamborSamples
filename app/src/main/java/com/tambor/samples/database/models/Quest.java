package com.tambor.samples.database.models;

import com.tambor.orm.database.dao.entity.Entity;
import com.tambor.orm.database.dao.entity.annotation.GPAEntity;
import com.tambor.orm.database.dao.entity.annotation.GPAField;
import com.tambor.orm.database.dao.entity.annotation.GPAFieldBean;
import com.tambor.orm.database.dao.entity.annotation.GPAPrimaryKey;

import javax.inject.Inject;

@GPAEntity(name="tb_quest")
public class Quest extends Entity {
    @GPAPrimaryKey(name = "id", ignore = true, type = Entity.INTEGER)
    private Integer id;
    @GPAField(name="desc", type = Entity.VARCHAR)
    private String desc;

    @GPAField(name="value", type = Entity.BOOLEAN)
    private Boolean value;


    @GPAField(name="observation", type = Entity.VARCHAR)
    private String observation;

    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    @GPAFieldBean(name="user", type = Entity.BEAN, clazz=User.class)
    //@Inject
     User user;
    //@Inject
    public Quest(){}
    /*public Quest(User user){
        this.user = user;
    }*/

    public Quest(Integer id, String desc, Boolean value, String observation) {
        this.id = id;
        this.desc = desc;
        this.value = value;
        this.observation = observation;
    }

    @Override
    public Long getId() {
        return Long.parseLong(id.toString());
    }

    @Override
    public void setId(Long id) {
        this.id = Integer.parseInt(id.toString());
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String description) {
        this.desc = desc;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
