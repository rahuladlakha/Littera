package com.rahul.littera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

/*
 * A simple {@link Fragment} subclass.
 * Use the {@link NotesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotesFragment extends Fragment {
   // static int currTag = 0;
    static ArrayList<String> notes ;
    static ListView notesListview;
    public NotesFragment() {
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
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        notes = new ArrayList<String>();
        notes.add("Thankyou George lhaklh i iawifyiu ihsjakhf kjlh khajf kla fkla  \n ajfkjal \nlahf ");
        notes.add("Im fine");
        notesListview = (ListView) getView().findViewById(R.id.notesListView);

        MyAdapter adapter = new MyAdapter(getActivity(),notes);
        notesListview.setAdapter(adapter);

    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        ArrayList<String> arr;
        public MyAdapter(Context c, ArrayList<String> arr){
            super(c,R.layout.tasksview,R.id.taskTextView);
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
            View v = layoutInflater.inflate(R.layout.notesview,parent,false);
            TextView txt = (TextView) v.findViewById(R.id.noteTextView);
            txt.setText(arr.get(position));
          //  v.setTag(currTag);
          //  currTag++;
            return v;
        }
    }
}
