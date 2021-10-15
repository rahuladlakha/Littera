package com.rahul.littera;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;

public class DataManager {
    public static DataManager instance;
    public static DataManager getInstance(){
        if ( instance == null) instance = new DataManager();
        return instance;
    }
   private DataManager(){
        //so that new instance cant be created outside the class
   }
    public boolean save(){
        try {
            SaveData task = new SaveData();
            return task.execute(FirstActivity.sharedPreferences).get();
        } catch (Exception e){
            e.printStackTrace();
            Log.i("save state", "Unsuccessful");
            return  false;
        }
    }

    public boolean retrieveSaved(){
        try {
            RetrieveData task = new RetrieveData();
            return task.execute(FirstActivity.sharedPreferences).get();
        } catch (Exception e){
            e.printStackTrace();
            Log.i("Retrieval state", "Unsuccessful");
            return  false;
        }
    }


    static class RetrieveData extends AsyncTask<SharedPreferences,Void ,Boolean> {

        @Override
        protected Boolean doInBackground(SharedPreferences... sharedPreferences) {
            try { SharedPreferences sp = sharedPreferences[0];
                String s = sp.getString("data", null);
                Log.i("retrieve","String: "+ s);
                ByteArrayInputStream bis = new ByteArrayInputStream(Base64.getDecoder().decode(s.getBytes()));
                ObjectInputStream in = new ObjectInputStream(bis);
                Data.instance = (Data) in.readObject();
                Log.i("retrieval","successful - local");
                return true;
            } catch (Exception e ){
                e.printStackTrace();
                FirstActivity.storageReference.child(FirstActivity.instance.userId+".db")
                        .getBytes(1024*1024*100) // set a maximum download size of 100 mb
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        try {
                            ByteArrayInputStream bis = new ByteArrayInputStream(Base64.getDecoder().decode(bytes));
                            ObjectInputStream in = new ObjectInputStream(bis);
                            Data.instance = (Data) in.readObject();
                            Log.i("got string",new String(Base64.getDecoder().decode(bytes)));
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                      Log.i("Retrieval sync","Unsuccessful");
                      e.printStackTrace();
                    }
                });
                return false;
            }
        }


    }
    static class SaveData extends AsyncTask<SharedPreferences,Void ,Boolean> {

        @Override
        protected Boolean doInBackground(SharedPreferences... sharedPreferences) {
            try { SharedPreferences sp = sharedPreferences[0];
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(bos);
                out.writeObject(Data.getInstance());
                String s = new String(Base64.getEncoder().encode(bos.toByteArray()));
                sp.edit().putString("data",s).apply();


                    FirstActivity.storageReference.child(FirstActivity.instance.userId + ".db").putBytes(Base64.getEncoder().encode(bos.toByteArray()))
                            .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    Log.i("save state", "successful");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });


                return true;

            } catch (Exception e){
                e.printStackTrace();
            }
            return false;

        }


    }
}
