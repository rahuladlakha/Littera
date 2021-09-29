package com.rahul.littera;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                getUserInfo(account);
                // Signed in successfully, show authenticated UI.

            } catch (ApiException e) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                Toast.makeText(this, e.toString(),Toast.LENGTH_LONG).show();
                e.printStackTrace();

            }
        }
    }

    private void getUserInfo(GoogleSignInAccount acct){
        Toast.makeText(this, "Inside getuserinfo method",Toast.LENGTH_LONG).show();
        String personName = acct.getDisplayName();
        String personEmail = acct.getEmail();
        String personId = acct.getId();
        Uri personPhoto = acct.getPhotoUrl();
        Toast.makeText(this, personId +" "+ personName + " " + personEmail, Toast.LENGTH_LONG).show();
    }

    public void googleSignIn(View view){
        Toast.makeText(this, "Button tapped", Toast.LENGTH_LONG).show();
        Log.i("Button", "Tapped");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("819501200298-iluiitkjeg3b9ti4j8o1q1d4p67aa2cr.apps.googleusercontent.com")
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(MainActivity.this, gso);

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,1);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

             SignInButton signInButton = (SignInButton) findViewById(R.id.signInButton);
             signInButton.setSize(SignInButton.SIZE_STANDARD);

            findViewById(R.id.signInButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              googleSignIn(view);         // Due to some unknownn issue the onclick method for buttonn googleSignIn() was never called and thus am having to
                // redirect the same method from this screen
            }
        });

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if ( account != null ){
           getUserInfo(account);
        }
    }
}