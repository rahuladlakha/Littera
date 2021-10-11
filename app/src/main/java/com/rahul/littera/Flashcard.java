package com.rahul.littera;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Flashcard implements Comparable<Flashcard>, Serializable {
    private int fib1, fib2;
    public String front, back , cardgrp;
    private Date nextDate;

    public static final int EASY = 1178;
    public static final int MEDIUM = 1179;
    public static final int HARD = 1180;


    public static long msInADay = 24*60*60*1000;
    public Flashcard(String front, String back, String cdgp){
        this.front = front; this.back = back; this.nextDate = new Date(); this.cardgrp = cdgp;
        this.fib1 = 0; fib2 = 1;
        Data.getInstance().flashcards.add(this);
        DataManager.getInstance().save();
    }
    public static ArrayList<Flashcard> getPendingCards(){
        ArrayList<Flashcard> cards = new ArrayList<>();
        Collections.sort(Data.getInstance().flashcards);
        Date curr = new Date();
        Flashcard c;
        for ( int i = 0;i < Data.getInstance().flashcards.size() && (c = Data.getInstance().flashcards.get(i)).nextDate.before(curr);i++ ){
            cards.add(c);
           // c.nextDate = new Date(c.nextDate.getTime() + (c.fab1 + c.fab2)*msInADay);
           // int tmp = c.fab1; c.fab1 = c.fab2;c.fab2 = tmp + c.fab2;
        }
        return cards;
    }
    public int rateCard(int rating){ int fibonacci = (this.fib1 + this.fib2);
        if (fibonacci <= 2) fibonacci = 2;
        if (rating == EASY){
          // no change in fibonacci
        } else if (rating == MEDIUM){
          fibonacci = (fibonacci*3)/4;
        } else if ( rating == HARD ){
            fibonacci /= 3;
            if ( fib2 > fib1 ) {
               fib2 = fib1;
            }
        }
        this.nextDate = new Date(this.nextDate.getTime() + fibonacci*msInADay);
        this.fib1 = this.fib2;this.fib2 = fibonacci;
        return fibonacci;
    }

    @Override
    public int compareTo(Flashcard flashcard) {
        if (this.nextDate.after(flashcard.nextDate)) return 1;
        else if (this.nextDate.before(flashcard.nextDate)) return -1;
        else return 0;
    }
}
