package com.rahul.littera;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/*
 * A simple {@link Fragment} subclass.
 * Use the {@link FlashcardsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FlashcardsFragment extends Fragment {
    private static FlashcardsFragment instance;
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
}