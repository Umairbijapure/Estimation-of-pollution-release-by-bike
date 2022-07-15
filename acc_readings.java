package com.example.accelerometer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.jjoe64.graphview.GraphView;
//import com.jjoe64.graphview.GraphViewSeries;

//import com.jjoe64.graphview.LineGraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.*;
import com.opencsv.CSVWriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Graph2 extends AppCompatActivity implements SensorEventListener {


    SensorManager sensorManager;
    TextView tvX, tvY, tvZ;
    ArrayList<DataModel> dataList; 
    AndroidGravityUpdate androidGravityUpdate;
    TextView z_text;
    TextView value1,value2;
    Context context;

    GraphView graph;
    LineGraphSeries<DataPoint> seriesX, seriesY, seriesZ;
    long startTime;
    TextView dataTextView;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    String current_date;
    String current_time_str;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph2);
        SimpleDateFormat time_formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat time_formatter2 = new SimpleDateFormat("HH:mm:ss:SSS");

        tvX =  findViewById(R.id.textView1);    // color : blue
        tvY =  findViewById(R.id.textView2);    // color : green
        tvZ =  findViewById(R.id.textView3);    // color : magenta
        value1=findViewById(R.id.total_v1);
        value2=findViewById(R.id.total_v2);


        graph =  findViewById(R.id.graph);
        dataTextView = findViewById(R.id.data_text);
        dataList = new ArrayList<>();

        seriesX = new LineGraphSeries<>();
        seriesY = new LineGraphSeries<>();
        seriesZ = new LineGraphSeries<>();

        seriesX.setColor(Color.BLUE);
        seriesY.setColor(Color.GREEN);
        seriesZ.setColor(Color.MAGENTA);

//        graph.addSeries(seriesX);
//        graph.addSeries(seriesY);
        graph.addSeries(seriesZ);

        Viewport vp = graph.getViewport();
        vp.setXAxisBoundsManual(true);
        vp.setMinX(0);
        vp.setMaxX(1000);
//        vp.setMinY(0);
//        vp.setMaxY(15);
//
//
//        //vp.setYAxisBoundsManual(true);
//        vp.setXAxisBoundsManual(true);

        startTime = System.nanoTime() / 100000000;

//        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.Dela);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        androidGravityUpdate=new AndroidGravityUpdate(mSensorManager ,this);
        z_text=findViewById(R.id.z_text);
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                dataList.add(new DataModel(tvX.getText().toString(),tvY.getText().toString(),tvZ.getText().toString()));
//            }
//        },0,200);

        final Handler handler=new Handler();
        final Runnable runnable=new Runnable() {
            @Override
            public void run() {
                current_date = time_formatter.format(System.currentTimeMillis());
                current_time_str = time_formatter2.format(System.currentTimeMillis());
                dataList.add(new DataModel(tvX.getText().toString(),tvY.getText().toString(),tvZ.getText().toString(),current_time_str,current_date));
                handler.postDelayed(this,200);
            }
        };

        ((Button)findViewById(R.id.nextButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(Graph2.this,DisplayActivity.class);
//                intent.putExtra("data", dataList);
//                startActivity(intent);
//                DataModel data = new DataModel();
                ArrayList<DataModel> datalist2=dataList;
                String a="";
                for(DataModel data:datalist2){
                    a+= data.getX()+" "+data.getY()+" "+data.getZ()+"\n";
                }
                dataTextView.setText(a);

            }
        });
        context=this;
        String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        ((Button)findViewById(R.id.exportButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                try (PrintWriter writer = new PrintWriter("test.csv")) {
//
//                    StringBuilder sb = new StringBuilder();
//                    sb.append("x");
//                    sb.append(",");
//                    sb.append("y");
//                    sb.append(",");
//                    sb.append("z");
//                    sb.append(",");
//                    sb.append("T");
//                    sb.append('\n');
//
//                    ArrayList<DataModel> datalist3=dataList;
//                    for(DataModel data:datalist3){
//                        double x=Double.parseDouble(data.getX());
//                        double y=Double.parseDouble(data.getY());
//                        double z=Double.parseDouble(data.getZ());
//                        sb.append(data.getX());
//                        sb.append(",");
//                        sb.append(data.getY());
//                        sb.append(",");
//                        sb.append(data.getZ());
//                        sb.append(",");
//                        sb.append(Math.sqrt(x*x+y*y+z*z));
//                        sb.append('\n');
//
//                    }
//
//                    writer.write(sb.toString());
//
//
//                    Toast.makeText(Graph2.this, "Done!", Toast.LENGTH_SHORT).show();
//                   // PrintWriter pw = new PrintWriter(new FileOutputStream(
//                           //new File(getFilesDir(),".csv"),
//                          //  true /* append = true */));
//
//                } catch (Exception e) {
//
//                    Toast.makeText(Graph2.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                }


                String fileName = "AnalysisData.csv";
//                ContextWrapper cw = new ContextWrapper(context);
//                String fullPath =cw.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
                String filePath = baseDir + "/" + fileName;
                //File f = new File(Environment.getExternalStorageDirectory(),fileName);
//                File f = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),fileName);
                CSVWriter writer;
//                FileWriter mFileWriter;


                // File exist
                try{
//                if(f.exists()&&!f.isDirectory())
//                {
//                    Toast.makeText(context, "ifblock", Toast.LENGTH_SHORT).show();
//                    mFileWriter = new FileWriter(filePath, true);
//                    writer = new CSVWriter(mFileWriter);
//                    Toast.makeText(context, "endifblock", Toast.LENGTH_SHORT).show();
//                }
//                else
//                {
//                    Toast.makeText(context, "elseblock", Toast.LENGTH_SHORT).show();
                    writer = new CSVWriter(new FileWriter(filePath));
//                }

                //String[] data = {"Ship Name", "Scientist Name", "...", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").formatter.format(date)});

                    StringBuilder sb = new StringBuilder();
                    sb.append("x");
                    sb.append(",");
                    sb.append("y");
                    sb.append(",");
                    sb.append("z");
                    sb.append(",");
                    sb.append("Total");
                    sb.append(",");
                    sb.append("Date");
                    sb.append(",");
                    sb.append("Time");

//                    sb.append("\n");
                    writer.writeNext(sb.toString().split(","));

                    ArrayList<DataModel> datalist3=dataList;

                    for(DataModel data:datalist3){
                        if(data.getX()==""||data.getX()==null){
                            continue;
                        }
                        double x=Double.parseDouble(data.getX());
                        double y=Double.parseDouble(data.getY());
                        double z=Double.parseDouble(data.getZ());
                        sb=new StringBuilder();
                        sb.append(data.getX());
                        sb.append(",");
                        sb.append(data.getY());
                        sb.append(",");
                        sb.append(data.getZ());
                        sb.append(",");
                        sb.append(Math.sqrt(x*x+y*y+z*z));
                        sb.append(",");
                        sb.append(data.getDate());
                        sb.append(",");
                        sb.append(data.getTime());

                        writer.writeNext(sb.toString().split(","));

//                        sb.append("\n");

                    }

                    Toast.makeText(context,"newtoast", Toast.LENGTH_SHORT).show();


                    Toast.makeText(Graph2.this, "Done!", Toast.LENGTH_SHORT).show();
                writer.flush();
                writer.close();

                }

                catch(Exception e){
                    Toast.makeText(Graph2.this,"error :"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

          }
        });
        handler.post(runnable);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        tvX.setText(String.valueOf(event.values[0]));
        tvY.setText(String.valueOf(event.values[1]));
        tvZ.setText(String.valueOf(event.values[2]));
        double t_v1= Math.sqrt(event.values[0]*event.values[0]+event.values[1]*event.values[1]+event.values[2]*event.values[2]);
        value1.setText("value1: "+String.valueOf(t_v1));
        z_text.setText(""+androidGravityUpdate.getZ()+"\n"+androidGravityUpdate.getX()+"\n"+androidGravityUpdate.getY());
        double t_v2=Math.sqrt(androidGravityUpdate.getZ()*androidGravityUpdate.getZ()+androidGravityUpdate.getX()*androidGravityUpdate.getX()+androidGravityUpdate.getY()*androidGravityUpdate.getY());
        value2.setText("value2: "+String.valueOf(t_v2));

        updateGraph((event.timestamp / 100000000) - startTime,
                event.values[0], event.values[1], event.values[2]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    void updateGraph(final long timestamp, final float x, final float y, final float z) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //seriesX.appendData(new DataPoint(timestamp, x), true, 40);
                //seriesY.appendData(new DataPoint(timestamp, y), true, 40);
                seriesZ.appendData(new DataPoint(timestamp, z), true, 40);
            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);

    }
}
