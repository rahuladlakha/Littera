package com.rahul.littera;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {
    View signInButton = null;
    String termsString = " k\njs\nhf j\nsj\nh j\nkhj\nkhd\nk fj\njk\nhjk a\nhh\naj\nkh k\n akjh\njk jk\nhahj j\nkh k\n ka\nl H\nere \n are\n the\n ter\nms\n \nand \ncond\nti\nons\n for\n Littera\n app\n\n shk \n s; g\n sgi\nuiu \n hsgu \n kjl g\n hsjhg \n klsh g\n lshg j\n lshg ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();


       signInButton = findViewById(R.id.signInButton);
       signInButton.setEnabled(false);

        signInButton.setOnClickListener(new View.OnClickListener() {
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

    public void checkClicked(View view){
        if ( view.getId() == R.id.termsCheckbox){
            if ( ((CheckBox)view).isChecked()){
                signInButton.setEnabled(true);
            } else {
                signInButton.setEnabled(false);
            }
        } else if ( view.getId() == R.id.termsTextView){
           try {
               TextView terms = new TextView(this);
               terms.setText(termsString);
               terms.setMaxLines(20);
               terms.setTextSize(20);
               terms.setMovementMethod(new ScrollingMovementMethod());
               terms.setScroller(new Scroller(this));
               terms.setVerticalScrollBarEnabled(true);

               AlertDialog.Builder termsDialog = new AlertDialog.Builder(this);
                       termsDialog.setIcon(android.R.drawable.ic_dialog_info)
                       .setTitle("Terms and Conditions")
                       .setView(terms)
                       .setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialogInterface, int i) {
                           }
                       }).show();
           } catch (Exception e){
              // Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
               e.printStackTrace();
           }
        }
    }

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
               // Toast.makeText(this, e.toString(),Toast.LENGTH_LONG).show();
                e.printStackTrace();

            }
        }
    }

    private void getUserInfo(GoogleSignInAccount acct){
       // Toast.makeText(this, "Inside getuserinfo method",Toast.LENGTH_LONG).show();
        String personName = acct.getDisplayName();
        String personEmail = acct.getEmail();
        String personId = acct.getId();
        Uri personPhoto = acct.getPhotoUrl();
      //  Toast.makeText(this, personId +" "+ personName + " " + personEmail, Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, FirstActivity.class);
        intent.putExtra("person name", personName);
        intent.putExtra("user email", personEmail);
        intent.putExtra("user Id", personId);
        intent.putExtra("photo url", personPhoto);
        startActivity(intent);

    }

    public void googleSignIn(View view){
       // Toast.makeText(this, "Button tapped", Toast.LENGTH_LONG).show();
        Log.i("Button", "SignIN button tapped");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("819501200298-iluiitkjeg3b9ti4j8o1q1d4p67aa2cr.apps.googleusercontent.com")
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(MainActivity.this, gso);

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,1);

    }

}