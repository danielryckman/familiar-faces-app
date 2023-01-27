package com.example.proposal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.time.LocalTime;
import java.util.Locale;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EventEditActivity extends AppCompatActivity
{
    private EditText eventNameET;
    private EditText descriptionET;
    private TextView eventDateTV, eventTimeTV;
    Button timeButton;
    private ServerApi serverApi;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private int hour, minute;
    private String hour2, minute2;
    private LocalTime time;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        initWidgets();
        timeButton = findViewById(R.id.timeButton);
        time = LocalTime.now();
        eventDateTV.setText("Date: " + CalendarUtils.formattedDate(CalendarUtils.selectedDate));
    }
    private void initWidgets()
    {
        eventNameET = findViewById(R.id.eventNameET);
        eventDateTV = findViewById(R.id.eventDateTV);
        descriptionET = findViewById(R.id.descriptionET);
    }

    public void popTimePicker(View view){
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            }
        };
        int style = AlertDialog.THEME_HOLO_DARK;
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, hour, minute, true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    public void saveEventAction(View view)
    {
        String eventName = eventNameET.getText().toString();
        String description = descriptionET.getText().toString();
        //String dateTime = CalendarUtils.formatEpochDate(CalendarUtils.selectedDate);
        //long epochTime = CalendarUtils.epochTime(dateTime);
        hour2 = String.valueOf(hour);
        minute2 = String.valueOf(minute);
        Event newEvent = new Event(eventName, description, CalendarUtils.selectedDate,hour2+":"+minute2, 1);
        Event.eventsList.add(newEvent);
        TaskPOJO apipost = new TaskPOJO(eventName, description, (long)hour*60+minute*60, 1);
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.4.171:7860/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
            Post post = new Post(description, 20);
            jsonPlaceHolderApi.createPost(post);
            ServerApi serverApi = new ServerRequest();
            //retrofit.create(ServerApi.class);
            serverApi.createPostTask(apipost);
        } catch (Exception e) {
           e.printStackTrace();
        }
        finish();
    }
}