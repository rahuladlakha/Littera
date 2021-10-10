package com.rahul.littera;

import java.io.Serializable;
import java.util.Date;

public class StringPair implements Serializable {
    public String s1, s2;

    //The below two fields are only for tasks
    boolean taskCompleted = false;
    public int taskTag;
    public Date taskDeletion = null ; // this field will tell when should a task be deleted
    public StringPair(String s1, String s2){
        this.s1 = s1;
        this.s2 = s2;
    }
}
