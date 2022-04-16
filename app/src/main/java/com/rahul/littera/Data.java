package com.rahul.littera;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Data implements Serializable {
    private static final long serialVersionUID = 1L; //Never change this otherwise you may have to face problems during deserialization
    public byte[] userImage ;      // Since bitmap is not serializable profile image will be saved as a byte array
    public int taskTag = 0; // this field is only for tasks
    public ArrayList<Flashcard> flashcards = new ArrayList<>();
    public ArrayList<String> cardgroups = new ArrayList<>();
    public ArrayList<StringPair> tasks = new ArrayList<>();
    public HashMap<Long, StringPair> tasksmap = new HashMap<Long, StringPair>();
    public  ArrayList<StringPair> notes  = new ArrayList<>();  //All these arraylists will only be created once in lifetime since Data is s singleton class
    public static Data instance;
    private Data(){} // so that new instances can't be created
    public static Data getInstance(){
        if ( instance == null) instance = new Data();
        if (!instance.cardgroups.contains("New Cardgroup")) instance.cardgroups.add("New Cardgroup");
        return instance;
    }
}
