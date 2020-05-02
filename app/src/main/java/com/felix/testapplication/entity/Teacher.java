package com.felix.testapplication.entity;


import com.felix.annotation.DaoUtils;
import com.felix.testapplication.BuildConfig;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
@DaoUtils(configuration = BuildConfig.DAOPACKAGE)
public class Teacher {
    @Id(autoincrement = true)
    long id;
    String name;
    int age;

    @Generated(hash = 81005218)
    public Teacher(long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    @Generated(hash = 1630413260)
    public Teacher() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String toString() {
        return String.format("Teacher [id = %s, name = %s, age = %s]", id, name, age);
    }
}
