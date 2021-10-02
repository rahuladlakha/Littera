package com.rahul.littera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}