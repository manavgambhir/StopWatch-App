package com.example.stopwatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final String TAG="ASYNC";
    EditText etMin;
    EditText etSec;
    Button btnStart;
    Button btnPause;
    TextView tvDots;
    TextView tvMin;
    TextView tvSec;
    TextView tvTimeOut;
    private boolean isRunning = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etMin=findViewById(R.id.etMin);
        etSec=findViewById(R.id.etSec);
        btnStart=findViewById(R.id.btnStart);
        btnPause=findViewById(R.id.btnPause);
        tvMin=findViewById(R.id.tvMin);
        tvSec=findViewById(R.id.tvSec);
        tvDots=findViewById(R.id.tvDots);
        tvTimeOut=findViewById(R.id.tvTimeOut);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sec = etSec.getText().toString();
                String min = etMin.getText().toString();
                int n=0,p=0;
                if (!min.isEmpty()) {
                    n = Integer.parseInt(min);
                }
                if (!sec.isEmpty()) {
                    p = Integer.parseInt(sec);
                }
                countTask cTask=new countTask();
                cTask.execute(n,p);
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRunning){
                    isRunning=false;
                }
            }
        });
    }

    class countTask extends AsyncTask<Integer, Integer, Void>{
        @Override
        protected Void doInBackground(Integer... integers) {
            Log.d(TAG,"doInBackground: started");
            int i,j,a;
            int m=integers[0];
            int s=integers[1];

            for(i=m;i>=0;i--){
                if (i == m) {
                    a = s;
                } else {
                    a = 59;
                }
                for (j = a; j >= 0; j--) {
                    if(!isRunning) {
                        break;
                    }
                    else {
                        wait1Sec();
                        publishProgress(i, j);
                    }
                }
            }
            Log.d(TAG,"doInBackground: ended");
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            if(isRunning){
                tvMin.setText("");
                tvSec.setText("");
                tvDots.setText("");
                btnPause.setEnabled(false);
                tvTimeOut.setText("TIME OUT");
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            String formattedMin = String.format("%02d", values[0]);
            String formattedSec = String.format("%02d", values[1]);

            tvMin.setText(formattedMin);
            tvSec.setText(formattedSec);
//            tvMin.setText(String.valueOf(values[0]));
//            tvSec.setText(String.valueOf(values[1]));
        }
    }

    void wait1Sec(){
        long startTime = System.currentTimeMillis();
        while(System.currentTimeMillis()<startTime+1000);
    }
}