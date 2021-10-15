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
    String termsString = "By downloading or using the app, these terms will automatically apply to you – you should make sure therefore that you read them carefully before using the app. You’re not allowed to copy, or modify the app, any part of the app, or our trademarks in any way. You’re not allowed to attempt to extract the source code of the app, and you also shouldn’t try to translate the app into other languages, or make derivative versions. The app itself, and all the trade marks, copyright, database rights and other intellectual property rights related to it, still belong to Rahul Adlakha.\n" +
            "\n" +
            "Rahul Adlakha is committed to ensuring that the app is as useful and efficient as possible. For that reason, we reserve the right to make changes to the app or to charge for its services, at any time and for any reason. We will never charge you for the app or its services without making it very clear to you exactly what you’re paying for.\n" +
            "\n" +
            "The Littera app stores and processes personal data that you have provided to us, in order to provide my Service. It’s your responsibility to keep your phone and access to the app secure. We therefore recommend that you do not jailbreak or root your phone, which is the process of removing software restrictions and limitations imposed by the official operating system of your device. It could make your phone vulnerable to malware/viruses/malicious programs, compromise your phone’s security features and it could mean that the Littera app won’t work properly or at all.\n" +
            "\n" +
            "The app does use third party services that declare their own Terms and Conditions.\n" +
            "\n" +
            "Link to Terms and Conditions of third party service providers used by the app\n" +
            "\n" +
            "Google Play Services\n" +
            "Google Analytics for Firebase\n" +
            "Firebase Crashlytics\n" +
            "You should be aware that there are certain things that Rahul Adlakha will not take responsibility for. Certain functions of the app will require the app to have an active internet connection. The connection can be Wi-Fi, or provided by your mobile network provider, but Rahul Adlakha cannot take responsibility for the app not working at full functionality if you don’t have access to Wi-Fi, and you don’t have any of your data allowance left.\n" +
            "\n" +
            "If you’re using the app outside of an area with Wi-Fi, you should remember that your terms of the agreement with your mobile network provider will still apply. As a result, you may be charged by your mobile provider for the cost of data for the duration of the connection while accessing the app, or other third party charges. In using the app, you’re accepting responsibility for any such charges, including roaming data charges if you use the app outside of your home territory (i.e. region or country) without turning off data roaming. If you are not the bill payer for the device on which you’re using the app, please be aware that we assume that you have received permission from the bill payer for using the app.\n" +
            "\n" +
            "Along the same lines, Rahul Adlakha cannot always take responsibility for the way you use the app i.e. You need to make sure that your device stays charged – if it runs out of battery and you can’t turn it on to avail the Service, Rahul Adlakha cannot accept responsibility.\n" +
            "\n" +
            "With respect to Rahul Adlakha’s responsibility for your use of the app, when you’re using the app, it’s important to bear in mind that although we endeavour to ensure that it is updated and correct at all times, we do rely on third parties to provide information to us so that we can make it available to you. Rahul Adlakha accepts no liability for any loss, direct or indirect, you experience as a result of relying wholly on this functionality of the app.\n" +
            "\n" +
            "At some point, we may wish to update the app. The app is currently available on Android – the requirements for system(and for any additional systems we decide to extend the availability of the app to) may change, and you’ll need to download the updates if you want to keep using the app. Rahul Adlakha does not promise that it will always update the app so that it is relevant to you and/or works with the Android version that you have installed on your device. However, you promise to always accept updates to the application when offered to you, We may also wish to stop providing the app, and may terminate use of it at any time without giving notice of termination to you. Unless we tell you otherwise, upon any termination, (a) the rights and licenses granted to you in these terms will end; (b) you must stop using the app, and (if needed) delete it from your device.\n" +
            "\n" +
            "Changes to This Terms and Conditions\n" +
            "\n" +
            "I may update our Terms and Conditions from time to time. Thus, you are advised to review this page periodically for any changes. I will notify you of any changes by posting the new Terms and Conditions on this page.\n" +
            "\n" +
            "These terms and conditions are effective as of 2021-10-15\n" +
            "\n" +
            "Contact Us\n" +
            "\n" +
            "If you have any questions or suggestions about my Terms and Conditions, do not hesitate to contact me at rahuladlakha26@gmail.com.\n" +
            "\n";
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
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
                googleSignIn(view);         // Due to some unknownn issue the onclick method for buttonn googleSignIn() was never called and thus am having to
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
               TextView terms = new TextView(this);
               terms.setText(termsString);
               terms.setPadding(40,20,40,0);
               terms.setMaxLines(30);
               terms.setTextSize(14);
               terms.setMovementMethod(new ScrollingMovementMethod());
               terms.setScroller(new Scroller(this));
               terms.setVerticalScrollBarEnabled(true);

               AlertDialog.Builder termsDialog = new AlertDialog.Builder(this);
                       termsDialog.setIcon(R.drawable.ic_info_twotone)
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

    public void googleSignIn(View view){
       // Toast.makeText(this, "Button tapped", Toast.LENGTH_LONG).show();
        Log.i("Button", "SignIN button tapped");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("819501200298-iluiitkjeg3b9ti4j8o1q1d4p67aa2cr.apps.googleusercontent.com")
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(SignUpActivity.this, gso);

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,1);

    }

}