package com.rahul.littera;

import java.io.Serializable;
import java.util.ArrayList;

public class Data implements Serializable {
    public static ArrayList<Flashcard> flashcards = new ArrayList<Flashcard>();
    public static ArrayList<String> cardgroups;
    public ArrayList<StringPair> pendingTasks ;
    public  ArrayList<StringPair> completedTasks ;
    public  ArrayList<StringPair> notes ;
    public static Data instance;
    public static Data getInstance(){
        if ( instance == null) instance = new Data();
        return instance;
    }
}
