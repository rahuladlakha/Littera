package com.rahul.littera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FirstActivity extends AppCompatActivity {
   private static Bitmap userImageBitmap;
   private static URL imageUrl;
   private static GoogleSignInAccount currUser;
   private static FirstActivity instance;
   public static void signout(){
       FirebaseAuth.getInstance().signOut();
               instance.getSignInInfo();
          Toast.makeText(instance, "Successfully signed out !", Toast.LENGTH_SHORT).show();
   }

    public static void getUserImage(ImageView imgView) {
        if (imageUrl == null) return;
        GetImageTask task = new GetImageTask();
        try { userImageBitmap = task.execute(imageUrl).get();
             imgView.setImageBitmap(userImageBitmap);
        } catch (Exception e){
            e.printStackTrace();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        instance = this;

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
                } else if (id == R.id.productivityItem){
                    setFragment(ProductivityFragment.getInstance());
                } else if (id == R.id.profileItem){
                    setFragment( ProfileFragment.getInstance());
                }
                return true;
            }
        });
    }

    private void getSignInInfo(){
        currUser = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if ( currUser != null ){
            Toast.makeText(this, "Curr user not null ", Toast.LENGTH_SHORT).show();
            String personName = currUser.getDisplayName();
            String personEmail = currUser.getEmail();
            String personId = currUser.getId();
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
            URL url = urls[0];
            HttpURLConnection http = null;
            try {
                http = (HttpURLConnection) url.openConnection();
                InputStream in = http.getInputStream();
                Bitmap image = BitmapFactory.decodeStream(in);
                return image;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


    }
}