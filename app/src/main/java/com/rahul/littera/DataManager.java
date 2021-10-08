package com.rahul.littera;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    public boolean save(SharedPreferences sp){
       try {
           ByteArrayOutputStream bos = new ByteArrayOutputStream();
           ObjectOutputStream out = new ObjectOutputStream(bos);
           out.writeObject(Data.getInstance());
           String s = new String(Base64.getEncoder().encode(bos.toByteArray()));
           sp.edit().putString("data",s).apply();
           Log.i("save state","successful");
           return true;

       } catch (Exception e){
           e.printStackTrace();
       }
       return false;
    }
    public void sync(){

    }
    public boolean retrieveSaved(SharedPreferences sp){
      try {
          String s = sp.getString("data", null);
          Log.i("retrieve","String: "+ s);
          ByteArrayInputStream bis = new ByteArrayInputStream(Base64.getDecoder().decode(s.getBytes()));
          ObjectInputStream in = new ObjectInputStream(bis);
          Data.instance = (Data) in.readObject();
          Log.i("retrieval","successful");
          return true;
      } catch (Exception e ){
          e.printStackTrace();
          return false;
      }
    }
    public void retrieveSynced(){

    }
}
