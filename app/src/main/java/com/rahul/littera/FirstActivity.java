package com.rahul.littera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FirstActivity extends AppCompatActivity {
    private String username, useremail, userId;
    public static SharedPreferences sharedPreferences;
    private static Bitmap userImageBitmap;
   private static URL imageUrl;
   private static FirebaseUser currUser;
   public static FirstActivity instance;
   public static void signout(){
       FirebaseAuth.getInstance().signOut();
       instance.getSignInInfo();
   }

    public static Bitmap getUserImage(ImageView imgView) {
        if (imageUrl == null) return null;
        Bitmap bitmap = null;
        GetImageTask task = new GetImageTask();
        try { //bitmap = task.execute(imageUrl).get();
            URL url = imageUrl;
            HttpURLConnection http = null;
                http = (HttpURLConnection) url.openConnection();
                InputStream in = http.getInputStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
        }
        return  bitmap;
    }
    public static String[] getUserInfo(){
       return new String[]{instance.username,instance.useremail, instance.userId};
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        instance = this;
        sharedPreferences = this.getSharedPreferences("com.rahul.littera", Context.MODE_PRIVATE);
         boolean result = DataManager.getInstance().retrieveSaved();
         Log.i("Retrieval result", Boolean.toString(result));
        ActionBar actionBar = getSupportActionBar();
                if (actionBar != null ){
                    actionBar.setIcon(R.drawable.ic_notes);
                    actionBar.setHomeButtonEnabled(true);
                    actionBar.setTitle("Litera");
                }
        getSignInInfo();
        BottomNavigationView btm = (BottomNavigationView) findViewById(R.id.botomNavigationView);
        btm.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FirstActivity.this.startAnimations();
                int id = item.getItemId();
                if (id == R.id.tasksItem){
                    setFragment(TasksFragment.getInstance());
                } else if (id == R.id.notesItem ){
                    setFragment( NotesFragment.getInstance());
                } else if (id == R.id.flashcardsItem){
                    setFragment(FlashcardsFragment.getInstance());
                } else if (id == R.id.profileItem){
                    setFragment( ProfileFragment.getInstance());
                }
                return true;
            }
        });
        /*
        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){

                   // Toast.makeText(FirstActivity.this , "No user logged in", Toast.LENGTH_SHORT).show();
                }
            }
        });
        */
    }

    private void getSignInInfo(){
      currUser = FirebaseAuth.getInstance().getCurrentUser();

        if ( currUser != null ){
            username = currUser.getDisplayName();
            useremail = currUser.getEmail();
            userId = currUser.getUid();
            //Toast.makeText(this, personId + personEmail + personName , Toast.LENGTH_SHORT).show();
           try{ if (imageUrl == null ) imageUrl = new URL(currUser.getPhotoUrl().toString());}
           catch (Exception e){e.printStackTrace(); }
           // Toast.ma= (URL)keText(this, personId +" "+ personName + " " + personEmail, Toast.LENGTH_LONG).show();
        } else { 
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
            finish();
        }

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
                } else if (view.getId() == R.id.newFlashcardFAB){
                    Intent intent = new Intent(FirstActivity.this, NewFlashcardActivity.class);
                    intent.putExtra("initiated from","flashcardsFragment");
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
    static class GetImageTask extends AsyncTask<URL,Void , Bitmap> {

        @Override
        protected Bitmap doInBackground(URL... urls) {

            return null;
        }


    }
}