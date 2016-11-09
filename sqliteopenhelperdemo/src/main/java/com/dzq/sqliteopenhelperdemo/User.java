package com.dzq.sqliteopenhelperdemo;

/**
 * Description:
 * Created by duzhiqi on 2016/11/4.
 */

public class User {
    private int age;
    private String FirstName;
    private String LastName;

    public User() {
    }

    public User(int age, String firstName, String lastName) {
        this.age = age;
        FirstName = firstName;
        LastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    @Override
    public String toString() {
        return "User{" +
                "age=" + age +
                ", FirstName='" + FirstName + '\'' +
                ", LastName='" + LastName + '\'' +
                '}';
    }
}
