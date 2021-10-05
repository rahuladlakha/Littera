package com.rahul.littera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    private ListView optionsListView ;
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
        userImageView = (ImageView) getActivity().findViewById(R.id.profileImageView);
        FirstActivity.getUserImage(userImageView);
        getActivity().findViewById(R.id.logoutOptionTextView).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if ( id == R.id.settingsOptionTextView){

        } else if (id == R.id.logoutOptionTextView){
            FirstActivity.signout();
        }
    }
}