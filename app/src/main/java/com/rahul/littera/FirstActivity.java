package com.rahul.littera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.api.LabelProto;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FirstActivity extends AppCompatActivity {
    // Attribution for unsplash pics
    //Photo by <a href="https://unsplash.com/@lukechesser?utm_source=unsplash&utm_medium=referral&utm_content=creditCopyText">Luke Chesser</a> on <a href="https://unsplash.com/s/photos/color-gradient?utm_source=unsplash&utm_medium=referral&utm_content=creditCopyText">Unsplash</a>
    // (background on profile fragment )

    //  Photo by <a href="https://unsplash.com/@tamanna_rumee?utm_source=unsplash&utm_medium=referral&utm_content=creditCopyText">Tamanna Rumee</a> on <a href="https://unsplash.com/s/photos/pencil?utm_source=unsplash&utm_medium=referral&utm_content=creditCopyText">Unsplash</a>
    //  Photo by <a href="https://unsplash.com/@tamanna_rumee?utm_source=unsplash&utm_medium=referral&utm_content=creditCopyText">Tamanna Rumee</a> on <a href="https://unsplash.com/s/photos/pencil?utm_source=unsplash&utm_medium=referral&utm_content=creditCopyText">Unsplash</a>
    // ( the two pencil images )

    //  Dont forget to attribute the app icon designer also
    // Attribution for app icon -
    // <div>Icons made by <a href="https://www.freepik.com" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a></div>
    // attributions to be given on description in the playstore and giving attribution is a must

    public String username, useremail, userId;
    public static SharedPreferences sharedPreferences;
    private static Bitmap userImageBitmap;
   private static URL imageUrl;
   private static FirebaseUser currUser;
   public static FirstActivity instance;
   public static StorageReference storageReference;
   public static DatabaseReference databaseReference;

    public static Bitmap getUserImage() {
        if (imageUrl == null) return null;
        Bitmap bitmap = null;
        try {
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

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("alarmNotificaton", "Task Alarm", importance);
            channel.setDescription("Lets you receive task alarm notifications");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        instance = this;
        createNotificationChannel(); // this step needs to be done only once but since its crucial to be able
                   // to send notificatoins, do this in the first activity that opens to keep you safe.

      //getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.grey));
       /*
       ActionBar actionBar = getSupportActionBar();
                if (actionBar != null ){
                    actionBar.setIcon(R.drawable.ic_notes);
                    actionBar.setHomeButtonEnabled(true);
                    actionBar.setTitle("Litera");
                }
        */
        try {
            getSignInInfo();
           // Log.i("user id", userId);
            sharedPreferences = this.getSharedPreferences("com.rahul.littera", Context.MODE_PRIVATE);

            databaseReference = FirebaseDatabase.getInstance().getReference("Users");
            databaseReference.child(userId).child("Name").setValue(username);
            databaseReference.child(userId).child("Email").setValue(useremail);
            storageReference = FirebaseStorage.getInstance().getReference().child("UserData").child(userId);

            boolean result = DataManager.getInstance().retrieveSaved();
            Log.i("Retrieval result", Boolean.toString(result));


        } catch (Exception e){
            e.printStackTrace();
        }
            new UploadImageTask().execute();
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
       /*
       *Animation - settings option keeps rotating
        View v = findViewById(R.id.settingsItem);
        v.animate().rotationBy(9).setDuration(30).withEndAction(new Runnable() {
            @Override
            public void run() {
                v.animate().rotationBy(9).setDuration(30).withEndAction(this);
            }
        });

        */
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       if (item.getItemId() == R.id.logoutItem) {
           SignUpActivity.signout();
           Intent intent = new Intent(this, SignUpActivity.class);
           startActivity(intent);
       }
        return super.onOptionsItemSelected(item);
    }

    static class UploadImageTask extends AsyncTask<Void,Void ,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Bitmap bitmap = getUserImage();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                byte[] arr = bos.toByteArray();
                UploadTask task = storageReference.child(instance.userId + ".jpg").putBytes(arr);
                task.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("Upload", "UserImage upload failed reason: " + e.toString());
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.i("Upload", "UserImage uploaded successfully !");
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


    }

}