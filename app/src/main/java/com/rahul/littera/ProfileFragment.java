package com.rahul.littera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/*
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {
    private static ProfileFragment instance;
    public static ImageView userImageView;
    private static TextView nameTextView, emailTextView;
    public static ProfileFragment getInstance(){
        if (instance == null) instance = new ProfileFragment();
        return instance;
    }
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().findViewById(R.id.settingsOptionTextView).setVisibility(View.GONE);

        nameTextView = (TextView) getActivity().findViewById(R.id.nameTextView);
        emailTextView = (TextView) getActivity().findViewById(R.id.emailTextView);
        String[] info = FirstActivity.getUserInfo();
        nameTextView.setText("  "+ info[0]);
        emailTextView.setText("  " + info[1]);
        userImageView = (ImageView) getActivity().findViewById(R.id.profileImageView);
        if ( Data.getInstance().userImage == null) {
            GetImageTask task = new GetImageTask();
            task.execute(userImageView);
        } else {
            byte[] byteArray = Data.getInstance().userImage;
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            userImageView.setImageBitmap(bitmap); //this way user won't have to download same image again and again from web
        }

        getActivity().findViewById(R.id.settingsOptionTextView).setOnClickListener(this);
        getActivity().findViewById(R.id.termsTextView).setOnClickListener(this);
        getActivity().findViewById(R.id.privacyPolicyTextView).setOnClickListener(this);
        getActivity().findViewById(R.id.logoutOptionTextView).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if ( id == R.id.settingsOptionTextView){

        } else if (id == R.id.logoutOptionTextView){
            FirstActivity.signout();
        } else if (id == R.id.termsTextView){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://termslitteraapp.blogspot.com/2021/10/terms-and-conditions-littera-mobile-app.html"));
            startActivity(intent);
        } else if ( id == R.id.privacyPolicyTextView){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://termslitteraapp.blogspot.com/2021/10/privacy-policy-littera-mobile.html"));
            startActivity(intent);
        }
    }

    class GetImageTask extends AsyncTask<ImageView,Void,Bitmap>{
        @Override
        protected Bitmap doInBackground(ImageView... imageViews) {
            return FirstActivity.getUserImage();
        }

        @Override
        protected void onPostExecute(Bitmap image){
            if (image != null) {
                userImageView.setImageBitmap(image);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                Data.getInstance().userImage = bos.toByteArray();// so that next time user wont need to download the same image
                DataManager.getInstance().save();
                super.onPostExecute(image);
            }
        }
    }
}