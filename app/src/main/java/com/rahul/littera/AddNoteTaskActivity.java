package com.rahul.littera;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Calendar;
import java.util.Locale;

public class AddNoteTaskActivity extends AppCompatActivity {
    EditText titleEditText ;
    EditText desEditText;
    TextView dateTextView = null, timeTextView = null;
    Calendar alarmTime = Calendar.getInstance();
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note_task);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        titleEditText = (EditText) findViewById(R.id.titleEditText);
        desEditText = (EditText) findViewById(R.id.descriptionEditText);
        dateTextView = (TextView) findViewById(R.id.dateTextView);
        timeTextView = (TextView) findViewById(R.id.timeTextView);

        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });
        timeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimeDialog();
            }
        });
        if (getIntent().getStringExtra("initiated from").equals("tasksFragment")){
            int i = getIntent().getIntExtra("task index", -1);
            if (i != -1){
                StringPair curr = Data.getInstance().tasks.get(i);
                titleEditText.setText(curr.s1);
                desEditText.setText(curr.s2);
            }
        } else if ((getIntent().getStringExtra("initiated from").equals("notesFragment"))) {
            findViewById(R.id.alarmLinearLayout).setVisibility(View.GONE);
            //dateTextView.setVisibility(View.GONE); timeTextView.setVisibility(View.GONE);
            int i = getIntent().getIntExtra("note index", -1);
            if (i != -1) {
                StringPair curr = Data.getInstance().notes.get(i);
                titleEditText.setText(curr.s1);
                desEditText.setText(curr.s2);
            }
        }
    }

    private void cancelAlarm(){
        Intent intent = new Intent(this, AlarmBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0, intent, 0);
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.cancel(pendingIntent);
        Toast.makeText(this, "Alarm cancelled successfully", Toast.LENGTH_LONG).show();
    }
    private void setAlarm(StringPair sp){
        alarmTime.set(Calendar.SECOND, 0); //without setting it zero the alarm will not hit as soon as it hits the clock
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmBroadcastReceiver.class);
        intent.putExtra("task ID", alarmTime.getTimeInMillis());
        intent.putExtra("title", sp.s1);
        intent.putExtra("description", sp.s2);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0, intent, 0);
        am.setAlarmClock(new AlarmManager.AlarmClockInfo(alarmTime.getTimeInMillis(), pendingIntent), pendingIntent);
       // am.setExact(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(),  pendingIntent);
        Toast.makeText(this, "Alarm set successfully", Toast.LENGTH_LONG).show();
    }

    private void showDateDialog(){
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                alarmTime.set(Calendar.YEAR, i);
                alarmTime.set(Calendar.MONTH, i1);
                alarmTime.set(Calendar.DAY_OF_MONTH, i2);
                SimpleDateFormat sd = new SimpleDateFormat("dd MMMM, yyyy");
                dateTextView.setText(" " + sd.format(alarmTime.getTime()));
                showTimeDialog();
            }
        }, alarmTime.get(Calendar.YEAR),alarmTime.get(Calendar.MONTH), alarmTime.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimeDialog(){
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                alarmTime.set(Calendar.HOUR_OF_DAY, i);
                alarmTime.set(Calendar.MINUTE, i1);
                SimpleDateFormat sp = new SimpleDateFormat("hh:mm a", Locale.US);
                timeTextView.setText(" " + sp.format(alarmTime.getTime()));
            }
        }, alarmTime.get(Calendar.HOUR_OF_DAY), alarmTime.get(Calendar.MINUTE), false).show();
    }

    public void saveTaskNote(View view){
        String s1 = titleEditText.getText().toString(), s2 =  desEditText.getText().toString();
        if ((s1 == null || s1.trim().isEmpty()) && (s2 == null || s2.trim().isEmpty())){ super.onBackPressed(); return; }
        if ( s1 == null || s1.trim().isEmpty()) s1 = "<Untitled>";

        if (getIntent().getStringExtra("initiated from").equals("tasksFragment")){
            int i = getIntent().getIntExtra("task index", -1);
            if (i != -1) {
                Data.getInstance().tasks.remove(i);
            }
            StringPair sp = new StringPair(s1,s2, alarmTime);
            TasksFragment.newTask(sp);
            setAlarm(sp);
        } else if ((getIntent().getStringExtra("initiated from").equals("notesFragment"))){
            int i = getIntent().getIntExtra("note index", -1);
            if (i != -1) {
                Data.getInstance().notes.remove(i);
            }
            NotesFragment.newNote(new StringPair(s1, s2, null));
        }
        super.onBackPressed();

    }
    @Override
    public void onBackPressed() {
        String s1 = titleEditText.getText().toString(), s2 =  desEditText.getText().toString();
        if ((s1 == null || s1.trim().isEmpty()) && (s2 == null || s2.trim().isEmpty())){ super.onBackPressed(); return; }
        new AlertDialog.Builder(this).setTitle("Select an action")
                .setIcon(R.drawable.ic_info_twotone)
                .setMessage("Do you want to save your changes !")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        saveTaskNote(null);
                    }
                })
                .setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AddNoteTaskActivity.super.onBackPressed();
                    }
                }).show();

    }
}