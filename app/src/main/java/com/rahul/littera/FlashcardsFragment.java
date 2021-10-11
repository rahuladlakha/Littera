package com.rahul.littera;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/*
 * A simple {@link Fragment} subclass.
 * Use the {@link FlashcardsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FlashcardsFragment extends Fragment {
    private static FlashcardsFragment instance;
    private static ArrayList<String> subjects ;
    private static HashMap<String ,ArrayList<Integer>> numCards;  //numCards map subject to number of pending cards for that subject
    public static ArrayList<Flashcard> pendingcards;   //pendingcards contain the pending cards for this time
                                                       //and will be used in next activity to get cards for each subject

    private static ListView listView;
    private static MyAdapter adapter;
    public static FlashcardsFragment getInstance(){
        if (instance == null) instance = new FlashcardsFragment();
        return instance;
    }

    public FlashcardsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_flashcards, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pendingcards = (ArrayList<Flashcard>) Flashcard.getPendingCards();
        subjects = new ArrayList<String>();
        numCards = new HashMap<String, ArrayList<Integer>>();

        // populating arr with subject names and pending cards in each subject
       for ( String sp : Data.getInstance().cardgroups) {
           if (sp.equals("New Cardgroup")) continue;
           subjects.add(sp);
           numCards.put(sp,new ArrayList<Integer>());
       }
       for (int i = 0; i < pendingcards.size(); i++){
           Flashcard currCard = pendingcards.get(i);
           numCards.get(currCard.cardgrp).add(i);
       }
       subjects.sort(new Comparator<String>() {
           @Override
           public int compare(String s, String t1) {
               if (numCards.get(s).size() > numCards.get(t1).size()) return -1;
               else if (numCards.get(s).size() < numCards.get(t1).size()) return +1;
               else return 0;
           }
       });

        listView = (ListView) getActivity().findViewById(R.id.flashcardsListview);
        adapter = new MyAdapter(getActivity());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (numCards.get(subjects.get(i)).size() == 0) {
                    Toast.makeText(FirstActivity.instance, "No pending cards to review in this deck :)", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(FirstActivity.instance, FlashcardActivity.class);
                intent.putIntegerArrayListExtra("indices", numCards.get(subjects.get(i)));
                startActivity(intent);
            }
        });
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.newFlashcardFAB);
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                view.animate().translationXBy(500).rotation(360).setDuration(2000);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.animate().translationXBy(-500).rotation(-360).setDuration(2000);
                    }
                }, 5000); return true;
            }
        });

    }

    public void refresh(){

    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        public MyAdapter(Context c){
            super(c,R.layout.subjects_view,R.id.subjectTextview);
            this.context = c;
        }

        @Override
        public int getCount() {
            return subjects.size();
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(R.layout.subjects_view,parent,false);
            TextView txt = (TextView) v.findViewById(R.id.subjectTextview);
            TextView des = (TextView) v.findViewById(R.id.pendingCardsTextview);
            txt.setText(subjects.get(position));
            int cardCount = numCards.get(subjects.get(position)).size();
            if (cardCount != 0 )
            des.setText(Integer.toString(cardCount));
            else des.setVisibility(View.INVISIBLE);
            //  v.setTag(currTag);
            //  currTag++;
            return v;
        }
    }
}

