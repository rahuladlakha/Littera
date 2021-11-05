package com.rahul.littera;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class AddNoteTaskActivity extends AppCompatActivity {
    EditText titleEditText ;
    EditText desEditText;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note_task);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        titleEditText = (EditText) findViewById(R.id.titleEditText);
        desEditText = (EditText) findViewById(R.id.descriptionEditText);

        if (getIntent().getStringExtra("initiated from").equals("tasksFragment")){
            int i = getIntent().getIntExtra("task index", -1);
            if (i != -1){
                StringPair curr = Data.getInstance().tasks.get(i);
                titleEditText.setText(curr.s1);
                desEditText.setText(curr.s2);
            }
        } else if ((getIntent().getStringExtra("initiated from").equals("notesFragment"))) {
            int i = getIntent().getIntExtra("note index", -1);
            if (i != -1) {
                StringPair curr = Data.getInstance().notes.get(i);
                titleEditText.setText(curr.s1);
                desEditText.setText(curr.s2);
            }
        }
    }

    public void saveTaskNote(View view){
        String s1 = titleEditText.getText().toString(), s2 =  desEditText.getText().toString();
        if ((s1 == null || s1.trim().isEmpty()) && (s2 == null || s2.trim().isEmpty())){ super.onBackPressed(); return; }
        if ( s1 == null || s1.trim().isEmpty()) s1 = "<Untitled>";

        if (getIntent().getStringExtra("initiated from").equals("tasksFragment")){
            int i = getIntent().getIntExtra("task index", -1);
            if (i != -1) {
                Data.getInstance().tasks.remove(i);
            }
            TasksFragment.newTask(new StringPair(s1,s2));
        } else if ((getIntent().getStringExtra("initiated from").equals("notesFragment"))){
            int i = getIntent().getIntExtra("note index", -1);
            if (i != -1) {
                Data.getInstance().notes.remove(i);
            }
            NotesFragment.newNote(new StringPair(s1, s2));
        }
        super.onBackPressed();

    }
    @Override
    public void onBackPressed() {
        String s1 = titleEditText.getText().toString(), s2 =  desEditText.getText().toString();
        if ((s1 == null || s1.trim().isEmpty()) && (s2 == null || s2.trim().isEmpty())){ super.onBackPressed(); return; }
        new AlertDialog.Builder(this).setTitle("Select an action")
                .setIcon(R.drawable.ic_info_twotone)
                .setMessage("Do you want to save your changes !")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        saveTaskNote(null);
                    }
                })
                .setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AddNoteTaskActivity.super.onBackPressed();
                    }
                }).show();

    }
}