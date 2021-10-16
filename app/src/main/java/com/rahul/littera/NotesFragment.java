package com.rahul.littera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/*
 * A simple {@link Fragment} subclass.
 * Use the {@link NotesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotesFragment extends Fragment {
    private static NotesFragment instance;
    public static NotesFragment getInstance(){
        if (instance == null) instance = new NotesFragment();
        return instance;
    }

   // static int currTag = 0;
   private static ListView notesListview;
   private static  MyAdapter myAdapter;
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

        if ( Data.getInstance().notes == null ) Data.getInstance().notes = new ArrayList<StringPair>();
        notesListview = (ListView) getView().findViewById(R.id.notesListView);

        myAdapter = new MyAdapter(getActivity(),Data.getInstance().notes);
        notesListview.setAdapter(myAdapter);
        notesListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(FirstActivity.instance, AddNoteTaskActivity.class);
                intent.putExtra("initiated from","notesFragment");
                intent.putExtra("note index", i);
                startActivity(intent);
            }
        });
       notesListview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                int index = i;
                new AlertDialog.Builder(FirstActivity.instance)
                        .setIcon(R.drawable.ic_delete_twotone)
                        .setTitle("Do you want to delete this note ?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Data.getInstance().notes.remove(index);
                                myAdapter.notifyDataSetChanged();
                                DataManager.getInstance().save();
                                Toast.makeText(FirstActivity.instance, "Note successfully deleted !", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Do nothing
                            }
                        }).show();
                return  true;
            }

        });

    }
    public static void newNote(StringPair sp){
        Data.getInstance().notes.add(sp);
        myAdapter.notifyDataSetChanged();
        DataManager.getInstance().save();
    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        ArrayList<StringPair> arr;
        public MyAdapter(Context c, ArrayList<StringPair> arr){
            super(c,R.layout.notesview,R.id.noteTextView);
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
            TextView des = (TextView) v.findViewById(R.id.noteDescription);
            txt.setText(arr.get(position).s1);
            des.setText(arr.get(position).s2);
          //  v.setTag(currTag);
          //  currTag++;
            return v;
        }
    }
}
