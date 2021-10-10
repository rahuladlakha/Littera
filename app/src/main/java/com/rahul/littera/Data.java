package com.rahul.littera;

import java.io.Serializable;
import java.util.ArrayList;

public class Data implements Serializable {
    private static final long serialVersionUID = 1L; //Never change this otherwise you may have to face problems during deserialization

    public int taskTag = 0; // this field is only for tasks
    public ArrayList<Flashcard> flashcards = new ArrayList<>();
    public ArrayList<String> cardgroups = new ArrayList<>();
    public ArrayList<StringPair> tasks = new ArrayList<>();
    public  ArrayList<StringPair> notes  = new ArrayList<>();  //All these arraylists will only be created once in lifetime since Data is s singleton class
    public static Data instance;
    private Data(){} // so that new instances can't be created
    public static Data getInstance(){
        if ( instance == null) instance = new Data();
        if (!instance.cardgroups.contains("New Cardgroup")) instance.cardgroups.add("New Cardgroup");
        return instance;
    }
}
