package com.rahul.littera;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class FlashcardActivity extends AppCompatActivity {
    TextView cardTextView = null;
    static boolean frontsideshown = true;
    Flashcard currFlashcard;
    FloatingActionButton doneFAB;
    private AlertDialog doneDialog;
    private Queue<Flashcard> flashcardQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);
        getSupportActionBar().hide();
        doneFAB = (FloatingActionButton) findViewById(R.id.doneFashcardFAB);
        doneFAB.setEnabled(false);
        ArrayList<Integer> indices = getIntent().getIntegerArrayListExtra("indices");
        flashcardQueue = new LinkedList<>();
        for (int i = 0; i < indices.size(); i++){
            flashcardQueue.add(FlashcardsFragment.pendingcards.get(indices.get(i)));
        }

        currFlashcard = flashcardQueue.remove();

        cardTextView = (TextView) findViewById(R.id.cardTextView);
        cardTextView.setMovementMethod(new ScrollingMovementMethod());
        cardTextView.setText(currFlashcard.front);
        frontsideshown = true;
    }

    public void card_rated(View view){ int nextcard = -1;
        if (view.getId() == R.id.easyButton) {
            nextcard = currFlashcard.rateCard(Flashcard.EASY);
        } else if (view.getId() == R.id.mediumButton) {
            nextcard = currFlashcard.rateCard(Flashcard.MEDIUM);
        } else if (view.getId() == R.id.hardButton) {
            nextcard = currFlashcard.rateCard(Flashcard.HARD);
        }
        doneDialog.dismiss();
        Toast.makeText(this, "This flashcard will appear again after " + nextcard + " days !", Toast.LENGTH_SHORT).show();

        if (!flashcardQueue.isEmpty())
            currFlashcard = flashcardQueue.remove();
        else {
            FlashcardsFragment.getInstance().refresh();
            finish();
        }

        cardTextView.setText(currFlashcard.front);
        frontsideshown = true;
        doneFAB.setEnabled(false);
    }

    public void onFABClick(View view ){
        if (view.getId() == R.id.flipFashcardFAB) {
            cardTextView.animate().rotationYBy(90).setDuration(400).withEndAction(new Runnable() {
                @Override
                public void run() {
                    cardTextView.animate().rotationY(-90).setDuration(0).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            if (frontsideshown) {
                                cardTextView.setText(currFlashcard.back);
                                frontsideshown = false;
                                doneFAB.setEnabled(true);
                            } else {
                                cardTextView.setText(currFlashcard.front);
                                frontsideshown = true;
                            }
                            cardTextView.animate().rotationYBy(90).setDuration(400);
                        }
                    });

                }
            });
        } else if (view.getId() == R.id.deleteFashcardFAB){
            new AlertDialog.Builder(this)
                    .setTitle("Are you sure ?")
                    .setMessage("Do you really want to delete this flashcard ?")
                    .setIcon(R.drawable.ic_delete_twotone)
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Data.getInstance().flashcards.remove(currFlashcard);
                            DataManager.getInstance().save();
                            Toast.makeText(FlashcardActivity.this, "Flashcard deleted !", Toast.LENGTH_SHORT).show();
                            // refresh the activity to get new list of flashcards
                            if (!flashcardQueue.isEmpty())
                                currFlashcard = flashcardQueue.remove();
                            else {
                                FlashcardsFragment.getInstance().refresh();
                                finish();
                            }

                            cardTextView.setText(currFlashcard.front);
                            frontsideshown = true;
                            doneFAB.setEnabled(false);

                        }
                    }).show();
        } else if (view.getId() == R.id.editFashcardFAB){
            Intent intent = new Intent(this, NewFlashcardActivity.class);
            intent.putExtra("initiated from", "FlashcardActivity");
            intent.putExtra("index",Data.getInstance().flashcards.indexOf(currFlashcard));
            startActivity(intent);
            finish();

        } else if (view.getId() == R.id.doneFashcardFAB){
            View dialogLayout = getLayoutInflater().inflate(R.layout.dialog_rate_flashcard,null);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("How would you rate this flashcard ?")
                    .setIcon(R.drawable.ic_notes)
                    .setView(dialogLayout);
                    doneDialog = builder.show();

        }
    }

    @Override
    public void onBackPressed() {
        FlashcardsFragment.getInstance().refresh();
        super.onBackPressed();
    }
}