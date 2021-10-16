package com.rahul.littera;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class NewFlashcardActivity extends AppCompatActivity {
    EditText frontEdittext;
    EditText backEdittext;
    Spinner spinner;
    ArrayAdapter<String> adapter;

    private int cardIndex = -1; // this will be greater than 0 only when this activity was initiated from the FlashcardActvity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_flashcard);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        cardIndex = -1;
        this.initiate(); // this method is to initiate all the views

        if (getIntent().getStringExtra("initiated from") != null && getIntent().getStringExtra("initiated from").equals("FlashcardActivity")){
            int index = getIntent().getIntExtra("index", -1);
            if ( index != -1){
                cardIndex = index;
                Flashcard card = Data.getInstance().flashcards.get(index);
                frontEdittext.setText(card.front);
                backEdittext.setText(card.back);
                spinner.setSelection(Data.getInstance().cardgroups.indexOf(card.cardgrp));

            }
        }
    }

    public void initiate(){
        frontEdittext = findViewById(R.id.frontEdittext);
        backEdittext = findViewById(R.id.backedittext);
        spinner = (Spinner) findViewById(R.id.subjectSpinner);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Data.getInstance().cardgroups);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spinner.getSelectedItem().toString().equalsIgnoreCase("New Cardgroup")){
                    View dialogedittextLayout = getLayoutInflater().inflate(R.layout.dialog_edit_text,null);
                    new AlertDialog.Builder(NewFlashcardActivity.this)
                            .setTitle("Create new cardgroup")
                            .setIcon(R.drawable.ic_addicon)
                            .setView(dialogedittextLayout)
                            .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String sub = ((EditText)dialogedittextLayout.findViewById(R.id.newCardgpEdittext)).getText().toString();
                                    if (sub != null && !sub.trim().isEmpty()) {
                                        Data.getInstance().cardgroups.add(0,sub);
                                        adapter.notifyDataSetChanged();
                                        spinner.setSelection(0);
                                        DataManager.getInstance().save();
                                    }
                                }
                            }).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        FlashcardsFragment.getInstance().refresh();
        String tmp = (spinner.getSelectedItem() != null) ? spinner.getSelectedItem().toString() : null;
        if (tmp == null || tmp.equals("New Cardgroup")){
            View dialogedittextLayout = getLayoutInflater().inflate(R.layout.dialog_edit_text,null);
            new AlertDialog.Builder(NewFlashcardActivity.this)
                    .setTitle("Create new cardgroup")
                    .setIcon(R.drawable.ic_addicon)
                    .setView(dialogedittextLayout)
                    .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String sub = ((EditText)dialogedittextLayout.findViewById(R.id.newCardgpEdittext)).getText().toString();
                            if (sub != null && !sub.trim().isEmpty()) {
                                Data.getInstance().cardgroups.add(0,sub);
                                adapter.notifyDataSetChanged();
                                spinner.setSelection(0);
                                DataManager.getInstance().save();
                            }
                        }
                    }).show();
        } else {
            String front = frontEdittext.getText().toString();
            String back = backEdittext.getText().toString();
            if (front.trim().isEmpty()) {
                super.onBackPressed();
                return;
            }
            if (cardIndex >= 0){
                Flashcard card = Data.getInstance().flashcards.get(cardIndex);
                card.front = front;
                card.back = back;
                if (!card.cardgrp.equals(spinner.getSelectedItem().toString()))
                    Toast.makeText(this, "You can't change cardgroup while editing a previous card.", Toast.LENGTH_SHORT).show();
                DataManager.getInstance().save();
            } else {
                Flashcard newCard =  new Flashcard(front, back, spinner.getSelectedItem().toString());
            }
            FlashcardsFragment.getInstance().refresh();
            super.onBackPressed();
        }
    }
}