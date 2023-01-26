package com.example.proposal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecordActivity extends AppCompatActivity implements OnGetRecordsListener, AdapterView.OnItemSelectedListener{
    TextView textView;
    EditText etText;
    ImageView ivMic;

    GraphView graphView;

    private RecordPOJO recordPOJO;

    private GetRecords getRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        GraphView graph = (GraphView) findViewById(R.id.graph);

        // initialize views
        textView = findViewById(R.id.recordtextView);
        etText = findViewById(R.id.recordetSpeech);

        //create a new Test
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.4.214:8080/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            getRecord = retrofit.create(GetRecords.class);
            LocalDate dateObj = LocalDate.now();
            DateTimeFormatter formatter =DateTimeFormatter.ofPattern("MM-dd-YYYY");
            TestPOJO test = new TestPOJO("ShirleyF-" + dateObj.format(formatter) );
            GetRecordsRequest getRecordRequest = new GetRecordsRequest();
            long id= 1;
            long begin =0;
            long end=0;  // end==0 will return all records
            getRecordRequest.getRecords(id, begin, end);
            getRecordRequest.setOnGetRecordsListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // activity result launcher to start intent
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // if result is not empty
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData()!=null) {
                        // get data and append it to editText
                        ArrayList<String> d=result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        etText.setText(etText.getText()+" "+d.get(0));
                    }
                }
            });


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // setting lcode corresponding
        // to the language selected
        //lcode = lCodes[i];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // automatically generated method
        // for implementing onItemSelectedListener
    }

    @Override
    public void onGetRecords(List<RecordPOJO> records) {
        Collections.sort(records, new Comparator<RecordPOJO>() {
            @Override
            public int compare(RecordPOJO r1, RecordPOJO r2){
                if(r2.getRdate()>r1.getRdate()){
                    return -1;
                }else{
                    return 1;
                }
            }
        });
        GraphView graph = (GraphView) findViewById(R.id.graph);
        DataPoint[] appTimeDataPoints = new DataPoint[records.size()];
        DataPoint[] photoTimeDataPoints = new DataPoint[records.size()];
        DataPoint[] testTimeDataPoints = new DataPoint[records.size()];
        DataPoint[] testNumberDataPoints = new DataPoint[records.size()];
        DataPoint[] commentNumberDataPoints = new DataPoint[records.size()];
        DataPoint[] testScoreDataPoints = new DataPoint[records.size()];

        for(int i=0; i<records.size(); i++) {
            RecordPOJO record = records.get(i);
            Date recordDate = new Date(records.get(i).getRdate());
            appTimeDataPoints[i] = new DataPoint(recordDate, record.getApptime());
            photoTimeDataPoints[i] = new DataPoint(recordDate, record.getPhototime());
            testTimeDataPoints[i] = new DataPoint(recordDate, record.getTesttime());
            testNumberDataPoints[i] = new DataPoint(recordDate, record.getTestnumber());
            commentNumberDataPoints[i] = new DataPoint(recordDate, record.getCommentnumber());
            testScoreDataPoints[i] = new DataPoint(recordDate, record.getAveragescore());
        }

        LineGraphSeries<DataPoint> appTimeSeries = new LineGraphSeries<>(appTimeDataPoints);
        LineGraphSeries<DataPoint> photoTimeSeries = new LineGraphSeries<>(photoTimeDataPoints);
        LineGraphSeries<DataPoint> testTimeSeries = new LineGraphSeries<>(testTimeDataPoints);
        LineGraphSeries<DataPoint> testNumberSeries = new LineGraphSeries<>(testNumberDataPoints);
        LineGraphSeries<DataPoint> commentNumberSeries = new LineGraphSeries<>(commentNumberDataPoints);
        LineGraphSeries<DataPoint> testScoreSeries = new LineGraphSeries<>(testScoreDataPoints);
        graph.addSeries(appTimeSeries);
        graph.addSeries(photoTimeSeries);
        graph.addSeries(testTimeSeries);
        graph.addSeries(testNumberSeries);
        graph.addSeries(commentNumberSeries);
        graph.addSeries(testScoreSeries);
        appTimeSeries.setColor(Color.GREEN);
        photoTimeSeries.setColor(Color.YELLOW);
        testTimeSeries.setColor(Color.CYAN);
        testNumberSeries.setColor(Color.MAGENTA);
        commentNumberSeries.setColor(Color.BLUE);
        testScoreSeries.setColor(Color.GREEN);

        appTimeSeries.setDrawDataPoints(true);
        photoTimeSeries.setDrawDataPoints(true);
        testTimeSeries.setDrawDataPoints(true);
        testNumberSeries.setDrawDataPoints(true);
        commentNumberSeries.setDrawDataPoints(true);
        testScoreSeries.setDrawDataPoints(true);

        // set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
        graph.getGridLabelRenderer().setNumHorizontalLabels(records.size()); // only 4 because of the space
        // set manual x bounds to have nice steps
        graph.getViewport().setMinX(records.get(0).getRdate());
        graph.getViewport().setMaxX(records.get(records.size()-1).getRdate());
        //graph.getViewport().setMinX(0);
        //graph.getViewport().setMaxX(10);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Date");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Hours");
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graph.getGridLabelRenderer().
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(10);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getGridLabelRenderer().setHumanRounding(false, true);
    }
}
