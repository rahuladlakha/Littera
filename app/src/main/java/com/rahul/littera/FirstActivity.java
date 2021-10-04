package com.rahul.littera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.animation.Animator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        ActionBar actionBar = getSupportActionBar();
                if (actionBar != null ){
                    actionBar.setIcon(R.drawable.ic_notes);
                    actionBar.setHomeButtonEnabled(true);
                    actionBar.setTitle("Litera");
                }
        getSignInInfo();

    }

    private void getSignInInfo(){
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if ( account != null ){
            String personName = account.getDisplayName();
            String personEmail = account.getEmail();
            String personId = account.getId();
            Uri personPhoto = account.getPhotoUrl();
           // Toast.makeText(this, personId +" "+ personName + " " + personEmail, Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
            finish();
        }
        BottomNavigationView btm = (BottomNavigationView) findViewById(R.id.botomNavigationView);
        btm.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
              FirstActivity.this.startAnimations();
               int id = item.getItemId();
               if (id == R.id.tasksItem){
                   setFragment(new TasksFragment());
               } else if (id == R.id.notesItem ){
                   setFragment(new NotesFragment());
               } else if (id == R.id.productivityItem){
                   setFragment(new ProductivityFragment());
               } else if (id == R.id.profileItem){
                   setFragment(new ProfileFragment());
               }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }
    public void newTaskNote(View view){
        view.animate().rotationBy(90).setDuration(1000).withEndAction(new Runnable() {
            @Override
            public void run() {
                if (view.getId() == R.id.newTaskFAB){
                    Intent intent = new Intent(FirstActivity.this, AddNoteTaskActivity.class);
                    intent.putExtra("initiated from","tasksFragment");
                    startActivity(intent);
                } else if (view.getId() == R.id.newNoteFAB){
                    Intent intent = new Intent(FirstActivity.this, AddNoteTaskActivity.class);
                    intent.putExtra("initiated from","notesFragment");
                    startActivity(intent);
                }
            }
        });

    }


    private void setFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, fragment)
                .commit();
    }

    public void startAnimations() {
        View v = findViewById(R.id.settingsItem);
        v.animate().rotationBy(9).setDuration(30).withEndAction(new Runnable() {
            @Override
            public void run() {
                v.animate().rotationBy(9).setDuration(30).withEndAction(this);
            }
        });
    }


}