package com.rahul.littera;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class AddNoteTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note_task);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
    }
}