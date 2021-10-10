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

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;

public class TasksFragment extends Fragment implements View.OnClickListener {
    private static ListView tasksListview;
    private static MyAdapter myAdapter ;
    private static TasksFragment instance = null;
    public static long msInADay = 24*60*60*1000;
    public static TasksFragment getInstance(){
        if (instance == null) instance = new TasksFragment();
        return instance;
    }

    private void setTaskChecked(TextView txt, TextView des, RadioButton rb, StringPair sp){
        sp.taskCompleted = true;
        sp.taskDeletion = new Date(new Date().getTime() + msInADay);
        rb.setButtonDrawable(R.drawable.ic_radio_button_checked);
        des.setVisibility(View.INVISIBLE);
        txt.setTextColor(FirstActivity.instance.getResources().getColor(R.color.grey));
        txt.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        DataManager.getInstance().save();
    }
    private  void setTaskUnchecked(TextView txt, TextView des, RadioButton rb, StringPair sp){
        sp.taskCompleted = false;
        sp.taskDeletion = null;
        rb.setButtonDrawable(R.drawable.ic_radio_button_unchecked);
        des.setVisibility(View.VISIBLE);
        txt.setTextColor(FirstActivity.instance.getResources().getColor(R.color.sky_blue));
        txt.setPaintFlags(txt.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));//setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        DataManager.getInstance().save();
    }

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

      if (Data.getInstance().tasks == null)  Data.getInstance().tasks = new ArrayList<StringPair>();
        tasksListview = (ListView) getView().findViewById(R.id.tasksListView);
        for (int i = 0; i < Data.getInstance().tasks.size(); i++ ){
             StringPair sp = Data.getInstance().tasks.get(i);
             if (sp.taskCompleted && sp.taskDeletion.before(new Date())) Data.getInstance().tasks.remove(sp);
        }
        DataManager.getInstance().save();

        myAdapter = new MyAdapter(getActivity(),Data.getInstance().tasks);
        tasksListview.setAdapter(myAdapter);
        tasksListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                 @Override
                                                 public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                     if (Data.getInstance().tasks.get(i).taskCompleted){
                                                         Toast.makeText(FirstActivity.instance, "Uncheck the task to open it !", Toast.LENGTH_SHORT).show();
                                                         return;
                                                     }
                                                     Intent intent = new Intent(FirstActivity.instance, AddNoteTaskActivity.class);
                                                     intent.putExtra("initiated from","tasksFragment");
                                                     intent.putExtra("task index", i);
                                                     startActivity(intent);
                                                 }
                                             });
        tasksListview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!Data.getInstance().tasks.get(i).taskCompleted){
                    Toast.makeText(FirstActivity.instance, "Check the task first to delete it !", Toast.LENGTH_SHORT).show();
                    return true;
                } int index = i;
                new AlertDialog.Builder(FirstActivity.instance)
                        .setIcon(android.R.drawable.ic_menu_delete)
                        .setTitle("Do you want to delete this task ?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Data.getInstance().tasks.remove(index);
                                myAdapter.notifyDataSetChanged();
                                DataManager.getInstance().save();
                                Toast.makeText(FirstActivity.instance, "Task successfully deleted !", Toast.LENGTH_SHORT).show();
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

                FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.newTaskFAB);
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

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View view) {
        RadioButton rb = ((RadioButton) view );
      if ( rb != null && rb.isChecked() ){ //rb.setButtonDrawable(R.drawable.ic_radio_button_checked);
      String tag = rb.getTag().toString();
      View v = null;
      int taskIndex = 0;
          for (int i = 0; i < tasksListview.getCount() ; i++)
              if (tasksListview.getChildAt(i).getTag().toString().equals(tag)) {
                  v = tasksListview.getChildAt(i); break;
              }

         for (int i = 0; i < Data.getInstance().tasks.size() ; i++)
              if (Data.getInstance().tasks.get(i).taskTag == Integer.parseInt(tag)) {
                  Data.getInstance().tasks.get(i).taskCompleted = !Data.getInstance().tasks.get(i).taskCompleted;
                  taskIndex = i;
                  break;
              }
                 if (v != null && !Data.getInstance().tasks.get(taskIndex).taskCompleted){
                     this.setTaskUnchecked((TextView) v.findViewById(R.id.taskTextView),v.findViewById(R.id.taskDescription), rb ,Data.getInstance().tasks.get(taskIndex));
                 } else if (v != null ) {
                     this.setTaskChecked((TextView) v.findViewById(R.id.taskTextView),v.findViewById(R.id.taskDescription), rb, Data.getInstance().tasks.get(taskIndex));
                     Toast.makeText(FirstActivity.instance,"Completed tasks are deleted 24 hours after being marked completed", Toast.LENGTH_SHORT).show();
                 }
      }
    }

    public static void newTask(StringPair sp){
        sp.taskTag = ++Data.getInstance().taskTag;
        Data.getInstance().tasks.add(sp);
        myAdapter.notifyDataSetChanged();
        DataManager.getInstance().save();
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
           rb.setTag(arr.get(position).taskTag);
           rb.setOnClickListener(TasksFragment.this);
           v.setTag(arr.get(position).taskTag);

            rb.setFocusable(false); // if there are any focusable or clickable items in your custom listview listviews onItemClickListener wont work
                                    // you need to manuually set focusable attribute to false on each clickable item to make OnItemClickListener active
                                    // while also keeping the other views clickable

           if (arr.get(position).taskCompleted){
               if (v != null && des != null ) {
                   TasksFragment.getInstance().setTaskChecked(txt, des, rb, arr.get(position));
               }
           }
           return v;
       }
   }
}