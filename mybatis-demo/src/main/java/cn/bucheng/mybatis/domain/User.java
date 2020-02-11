package cn.bucheng.mybatis.domain;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable{
    private Integer id;
    /**
     * 用户姓名
     */
    private String name;
    /**
     * 用户生日
     */
    private Date    birthday;
    /**
     * 用户性别
     */
    private String  sex;
    /**
     * 用户地址
     */
    private String  address;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return  "User{" +
                "id=" + id +
                ", username='" + name + '\'' +
                ", birthday=" + birthday +
                ", sex='" + sex + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}