package com.rahul.littera;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class SignUpActivity extends AppCompatActivity {
    View signInButton = null;

    private static FirebaseAuth mAuth;
    private static GoogleSignInClient mGoogleSignInClient;

    public static void signout(){
        mAuth.signOut();
        mGoogleSignInClient.signOut();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        googleSignIn();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if ( firebaseUser != null ){
            Intent intent = new Intent(this, FirstActivity.class);
            startActivity(intent);
            finish();
        }

        if ( getSupportActionBar() != null ) getSupportActionBar().hide();


       signInButton = findViewById(R.id.signInButton);
       signInButton.setEnabled(false);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignIn();         // Due to some unknownn issue the onclick method for buttonn googleSignIn() was never called and thus am having to
                // redirect the same method from this screen
            }
        });


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
               Intent intent = new Intent(Intent.ACTION_VIEW);
               intent.setData(Uri.parse("https://termslitteraapp.blogspot.com/2021/10/terms-and-conditions-littera-mobile-app.html"));
               startActivity(intent);
           } catch (Exception e){
             // Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
               e.printStackTrace();
          }
//               TextView terms = new TextView(this);
//               terms.setText(termsString);
//               terms.setPadding(40,20,40,0);
//               terms.setMaxLines(30);
//               terms.setTextSize(14);
//               terms.setMovementMethod(new ScrollingMovementMethod());
//               terms.setScroller(new Scroller(this));
//               terms.setVerticalScrollBarEnabled(true);
//
//               AlertDialog.Builder termsDialog = new AlertDialog.Builder(this);
//                       termsDialog.setIcon(R.drawable.ic_info_twotone)
//                       .setTitle("Terms and Conditions")
//                       .setView(terms)
//                       .setPositiveButton("Agree", new DialogInterface.OnClickListener() {
//                           @Override
//                           public void onClick(DialogInterface dialogInterface, int i) {
//                           }
//                       }).show();
//           } catch (Exception e){
//              // Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
//               e.printStackTrace();
//           }
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
                Log.i("idtoken",account.getIdToken());
                firebaseAuthWithGoogle(account.getIdToken());
                // Signed in successfully, show authenticated UI.

            } catch (ApiException e) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
               // Toast.makeText(this, e.toString(),Toast.LENGTH_LONG).show();
                e.printStackTrace();

            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SignIN", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent = new Intent(SignUpActivity.this,FirstActivity.class);
                                startActivity(intent);
                                SignUpActivity.this.finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("SignIn", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }
  /*
    private void getUserInfo(GoogleSignInAccount acct){
       // Toast.makeText(this, "Inside getuserinfo method",Toast.LENGTH_LONG).show();
        String personName = acct.getDisplayName();
        String personEmail = acct.getEmail();
        String personId = acct.getId();
        Uri personPhoto = acct.getPhotoUrl();

        Intent intent = new Intent(this, FirstActivity.class);
        intent.putExtra("person name", personName);
        intent.putExtra("user email", personEmail);
        intent.putExtra("user Id", personId);
        intent.putExtra("photo url", personPhoto);
        startActivity(intent);
        finish();
    }
    instead of using this method here Ill get signin info directly in firstActivity
    */

    public void googleSignIn(){
       // Toast.makeText(this, "Button tapped", Toast.LENGTH_LONG).show();
        Log.i("Button", "SignIN button tapped");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("819501200298-iluiitkjeg3b9ti4j8o1q1d4p67aa2cr.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(SignUpActivity.this, gso);

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,1);

    }

}