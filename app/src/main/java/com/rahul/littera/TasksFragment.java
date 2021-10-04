package com.rahul.littera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.Objects;

import java.util.ArrayList;

/*
 * A simple {@link Fragment} subclass.
 * Use the {@link TasksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TasksFragment extends Fragment implements View.OnClickListener {
    static int currTag = 0;
    private static ArrayList<StringPair> pendingTasks ;
    private static ListView tasksListview;
    private static MyAdapter myAdapter ;

    public TasksFragment() {
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
        return inflater.inflate(R.layout.fragment_tasks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        pendingTasks = new ArrayList<StringPair>();
        tasksListview = (ListView) getView().findViewById(R.id.tasksListView);

        myAdapter = new MyAdapter(getActivity(),pendingTasks);
        tasksListview.setAdapter(myAdapter);

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View view) {
        RadioButton rb = ((RadioButton) view );
      if ( rb != null && rb.isChecked() ){ rb.setButtonDrawable(R.drawable.ic_radio_button_checked);
          View v = tasksListview.getChildAt(Integer.valueOf(rb.getTag().toString()));
          if (v != null && v.findViewById(R.id.taskDescription) != null ) {
              v.findViewById(R.id.taskDescription).setVisibility(View.INVISIBLE);
              ((TextView) v.findViewById(R.id.taskTextView)).setTextColor(R.color.grey);
              ((TextView) v.findViewById(R.id.taskTextView)).setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
          }
      }
    }
    public static void newTask(StringPair sp){
        pendingTasks.add(sp);
        myAdapter.notifyDataSetChanged();
    }

    class MyAdapter extends ArrayAdapter<String>{
        Context context;
        ArrayList<StringPair> arr;
        public MyAdapter(Context c, ArrayList<StringPair> arr){
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
           View v = layoutInflater.inflate(R.layout.tasksview,parent,false);
           TextView txt = (TextView) v.findViewById(R.id.taskTextView);
           TextView des = (TextView ) v.findViewById(R.id.taskDescription);
            RadioButton rb = (RadioButton) v.findViewById(R.id.radioButton);
           txt.setText(arr.get(position).s1);
           des.setText(arr.get(position).s2);
           rb.setTag(currTag);
           rb.setOnClickListener(TasksFragment.this);
           v.setTag(currTag);
           currTag++;
           return v;
       }
   }
}