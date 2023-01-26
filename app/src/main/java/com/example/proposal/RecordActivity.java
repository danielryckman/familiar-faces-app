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

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.Format;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecordActivity extends AppCompatActivity implements OnGetRecordsListener, AdapterView.OnItemSelectedListener{
    TextView textView;
    TextView textView2;
    TextView nameView;
    EditText etText;
    ImageView ivMic;

    GraphView graphView;
    GraphView graphView2;


    private RecordPOJO recordPOJO;

    private GetRecords getRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        GraphView graph = (GraphView) findViewById(R.id.graph);
        GraphView graph2 = (GraphView) findViewById(R.id.graph2);

        // initialize views
        nameView = findViewById(R.id.nametextView);
        nameView.setText("Shirley Ford");
        textView = findViewById(R.id.recordtextView);
        textView.setText("Recent Activities");

        textView2= findViewById(R.id.recordtextView2);
        textView2.setText("Recent Tests");

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
        GraphView graph2 = (GraphView) findViewById(R.id.graph2);
        DataPoint[] appTimeDataPoints = new DataPoint[records.size()];
        DataPoint[] photoTimeDataPoints = new DataPoint[records.size()];
        DataPoint[] testTimeDataPoints = new DataPoint[records.size()];
        DataPoint[] testNumberDataPoints = new DataPoint[records.size()];
        DataPoint[] commentNumberDataPoints = new DataPoint[records.size()];
        DataPoint[] testScoreDataPoints = new DataPoint[records.size()];

        List<Date> dateList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Date d1 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d2 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d3 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d4 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d5 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d6 = calendar.getTime();
        dateList.add(d1);
        dateList.add(d2);
        dateList.add(d3);
        dateList.add(d4);
        dateList.add(d5);
        dateList.add(d6);

       for(int i=0; i<records.size(); i++) {
            RecordPOJO record = records.get(i);
            //Date recordDate = new Date(records.get(i).getRdate());
            Date recordDate = dateList.get(i);
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
        graph.addSeries(commentNumberSeries);

        graph2.addSeries(testTimeSeries);
        graph2.addSeries(testNumberSeries);
        graph2.addSeries(testScoreSeries);

        appTimeSeries.setTitle("time using the App");
        photoTimeSeries.setTitle("time looking at photo");
        testTimeSeries.setTitle("time taking tests");
        testNumberSeries.setTitle("number of test taken");
        commentNumberSeries.setTitle("number of comments left for photo");
        testScoreSeries.setTitle("average test score of the day");

        appTimeSeries.setColor(Color.GREEN);
        photoTimeSeries.setColor(Color.YELLOW);
        testTimeSeries.setColor(Color.CYAN);
        testNumberSeries.setColor(Color.MAGENTA);
        commentNumberSeries.setColor(Color.BLUE);
        testScoreSeries.setColor(Color.RED);

        appTimeSeries.setDrawDataPoints(true);
        photoTimeSeries.setDrawDataPoints(true);
        testTimeSeries.setDrawDataPoints(true);
        testNumberSeries.setDrawDataPoints(true);
        commentNumberSeries.setDrawDataPoints(true);
        testScoreSeries.setDrawDataPoints(true);

        // set date label formatter
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("MM-dd", Locale.US);
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(graph.getContext(), format));
        graph.getGridLabelRenderer().setNumHorizontalLabels(records.size()); // only 4 because of the space
        graph.getGridLabelRenderer().setLabelHorizontalHeight(10);
        graph2.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(graph2.getContext(), format));
        graph2.getGridLabelRenderer().setNumHorizontalLabels(records.size()); // only 4 because of the space
        graph2.getGridLabelRenderer().setLabelHorizontalHeight(10);

        // set manual x bounds to have nice steps
        graph.getViewport().setMinX(d1.getTime());
        graph.getViewport().setMaxX(d6.getTime());
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(true);
        graph2.getGridLabelRenderer().setHorizontalLabelsVisible(true);
        graph2.getViewport().setMinX(d1.getTime());
        graph2.getViewport().setMaxX(d6.getTime());
        //graph.getViewport().setScalable(true);
        //graph.getViewport().setScrollable(true);

        graph.getGridLabelRenderer().setHorizontalAxisTitle("Date");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Hours");
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.BOTH);
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph.getLegendRenderer().setBackgroundColor(Color.WHITE);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(9);
        graph.getViewport().setXAxisBoundsManual(false);
        graph.getViewport().setYAxisBoundsManual(true);
        //graph.getGridLabelRenderer().setHumanRounding(false, true);

        graph2.getGridLabelRenderer().setHorizontalAxisTitle("Date");
        graph2.getGridLabelRenderer().setVerticalAxisTitle("Hours");
        graph2.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.BOTH);
        graph2.getLegendRenderer().setVisible(true);
        graph2.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph2.getLegendRenderer().setBackgroundColor(Color.WHITE);
        graph2.getViewport().setMinY(0);
        graph2.getViewport().setMaxY(9);
        graph2.getViewport().setXAxisBoundsManual(false);
        graph2.getViewport().setYAxisBoundsManual(true);
        //graph2.getGridLabelRenderer().setHumanRounding(false, true);
    }
}
