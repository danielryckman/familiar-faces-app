package com.example.proposal;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalTime;
import java.util.Locale;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewEventEditActivity extends AppCompatActivity implements OnNewTaskListener
{
    private EditText eventNameET;
    private EditText descriptionET;
    private TextView eventDateTV, eventTimeTV;
    private ImageView imageView;
    private Button timeButton, okButton, createImageButton, cancelButton;
    private NewTaskApi newTaskApi;
    private int hour, minute;
    private String hour2, minute2;
    private LocalTime time;
    private TaskPOJO lastTask;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event_edit);
        initWidgets();
        timeButton = findViewById(R.id.timeButton);
        createImageButton = findViewById(R.id.createImageButton);
        okButton = findViewById(R.id.okButton);
        cancelButton = findViewById(R.id.cancelButton);
        imageView = findViewById(R.id.imageView);
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

    public void createEventAction(View view){
        String eventName = eventNameET.getText().toString();
        String description = descriptionET.getText().toString();
        //String dateTime = CalendarUtils.formatEpochDate(CalendarUtils.selectedDate);
        //long epochTime = CalendarUtils.epochTime(dateTime);
        hour2 = String.valueOf(hour);
        minute2 = String.valueOf(minute);
        Event newEvent = new Event(eventName, description, CalendarUtils.selectedDate,hour2+":"+minute2, 1);
        Event.eventsList.add(newEvent);
        TaskPOJO newTask = new TaskPOJO(eventName, description, (long)hour*60+minute*60, 1);
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MainActivity.WS_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            newTaskApi = retrofit.create(NewTaskApi.class);
            NewTaskRequest newTaskRequest = new NewTaskRequest();
            newTaskRequest.setOnNewTaskListener(this);
            newTaskRequest.newTask(newTask, MainActivity.currentFamilyMember.getUserid());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //finish();
    }

    public void okEventAction(View view){
        lastTask = null;
        finish();
    }
    public void cancelEventAction(View view)
    {
        //delete the last task if there was any then quit
        if(lastTask != null){
            try {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(MainActivity.WS_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                newTaskApi = retrofit.create(NewTaskApi.class);
                NewTaskRequest newTaskRequest = new NewTaskRequest();
                newTaskRequest.setOnNewTaskListener(this);
                newTaskRequest.deleteTask(lastTask.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        lastTask = null;
        finish();
    }

    @Override
    public void onNewTask(TaskPOJO task) {
        //delete the last task if there was any
        if(lastTask != null){
            try {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(MainActivity.WS_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                newTaskApi = retrofit.create(NewTaskApi.class);
                NewTaskRequest newTaskRequest = new NewTaskRequest();
                newTaskRequest.setOnNewTaskListener(this);
                newTaskRequest.deleteTask(task.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        lastTask = task;
        String image = "";
        image += task.getImage();
        byte[] decodedBytes = android.util.Base64.decode(image, Base64.DEFAULT);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        Bitmap bmp = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length, options);
        imageView.setImageBitmap(bmp);
        //ImageGenerationActivity.viewImage(decodedBytes);
    }
}