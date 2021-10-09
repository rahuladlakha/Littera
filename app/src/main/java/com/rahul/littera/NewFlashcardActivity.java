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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_flashcard);
        if (getSupportActionBar() != null) getSupportActionBar().hide();


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
                                        DataManager.getInstance().save(FirstActivity.sharedPreferences);
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
                                DataManager.getInstance().save(FirstActivity.sharedPreferences);
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
            new Flashcard(front, back, spinner.getSelectedItem().toString());
            super.onBackPressed();
        }
    }
}