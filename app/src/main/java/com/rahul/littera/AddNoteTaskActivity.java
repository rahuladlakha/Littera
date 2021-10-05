package com.rahul.littera;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
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
    }

    @Override
    public void onBackPressed() {  String s1 = titleEditText.getText().toString(), s2 =  desEditText.getText().toString();
        if ((s1 == null || s1.trim().isEmpty()) && (s2 == null || s2.trim().isEmpty())){ super.onBackPressed(); return; }
        if ( s1 == null || s1.trim().isEmpty()) s1 = "<Untitled>";

       if (getIntent().getStringExtra("initiated from").equals("tasksFragment")){
            TasksFragment.newTask(new StringPair(s1,s2));
       } else if ((getIntent().getStringExtra("initiated from").equals("notesFragment"))){
           NotesFragment.newNote(new StringPair(s1, s2));
        }
        super.onBackPressed();
    }
}