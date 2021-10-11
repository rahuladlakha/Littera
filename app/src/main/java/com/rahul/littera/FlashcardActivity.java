package com.rahul.littera;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class FlashcardActivity extends AppCompatActivity {
    TextView cardTextView = null;
    static boolean frontsideshown = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);
        getSupportActionBar().hide();
        cardTextView = (TextView) findViewById(R.id.cardTextView);
        frontsideshown = true;
    }

    public void onFlip(View view ){
        cardTextView.animate().rotationYBy(90).setDuration(400).withEndAction(new Runnable() {
            @Override
            public void run() {
                cardTextView.animate().rotationY(-90).setDuration(0).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        cardTextView.animate().rotationYBy(90).setDuration(400);
                    }
                });

            }
        });
    }
}