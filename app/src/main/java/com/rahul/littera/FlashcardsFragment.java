package com.rahul.littera;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/*
 * A simple {@link Fragment} subclass.
 * Use the {@link FlashcardsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FlashcardsFragment extends Fragment {
    private static FlashcardsFragment instance;
    private static ArrayList<StringPair> arr ;
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
        List<Flashcard> list = Flashcard.getPendingCards();
        if (list != null)
        for (Flashcard card : list){
            Toast.makeText(getActivity(), card.front + card.back, Toast.LENGTH_SHORT).show();
        }
        if ( arr == null) arr = new ArrayList<StringPair>();
        listView = (ListView) getActivity().findViewById(R.id.flashcardsListview);
        arr.add(new StringPair("English", "4"));
        arr.add(new StringPair("Biology","7"));
        adapter = new MyAdapter(getActivity(), arr);
        listView.setAdapter(adapter);
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

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        ArrayList<StringPair> arr;
        public MyAdapter(Context c, ArrayList<StringPair> arr){
            super(c,R.layout.subjects_view,R.id.subjectTextview);
            this.context = c;
            this.arr = arr;
        }

        @Override
        public int getCount() {
            return arr.size();
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(R.layout.subjects_view,parent,false);
            TextView txt = (TextView) v.findViewById(R.id.subjectTextview);
            TextView des = (TextView) v.findViewById(R.id.pendingCardsTextview);
            txt.setText(arr.get(position).s1);
            des.setText(arr.get(position).s2);
            //  v.setTag(currTag);
            //  currTag++;
            return v;
        }
    }
}

