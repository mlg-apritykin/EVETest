package com.majorleaguegaming.evetest;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.majorleaguegaming.evetest.Model.Widget;
import com.majorleaguegaming.evetest.Remote.WidgetAPI;
import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.majorleaguegaming.evetest.R.id.imageView;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.textView) TextView text;
    @BindView(R.id.textView2) TextView text2;
    @BindView(imageView) ImageView image;
    @BindView(R.id.button) Button button;
    @BindView(R.id.button2) Button btnDebug;

    private static final String TAG = "MainActivity";
    private Timer timer;
    private TimerTask timertask;
    private Handler handler = new Handler();
    private int INTERVAL = 15000;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        refreshWidgetInterval();

    }


    // An interval to loop the refresh of widget data
    private void refreshWidgetInterval(){
        timer = new Timer();
        timertask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        updateWidget();
                    }
                });
            }
        };
        timer.schedule(timertask, 0, INTERVAL);
    }

//    Output the imageURL
//    @OnClick(R.id.button2) public void debugCallback(){
//        WidgetAPI.Factory.getInstance().getWidget().enqueue(new Callback<Widget>() {
//            @Override
//            public void onResponse(Call<Widget> call, Response<Widget> response) {
//
//
//                String imgurl = (String) response.body().getImage1();
//                Toast.makeText(MainActivity.this, imgurl, Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onFailure(Call<Widget> call, Throwable t) {
//
//            }
//        });
//    }


    // Method that calls WidgetAPI to fetch response from JSON and set View variables with data
    public void updateWidget(){

        WidgetAPI.Factory.getInstance().getWidget().enqueue(new Callback<Widget>() {
            @Override
            public void onResponse(Call<Widget> call, Response<Widget> response) {
                text.setText(response.body().getText());
                text2.setText(response.body().getData1());
                Picasso.with(getApplicationContext()).load((String) response.body().getImage1()).into(image);

            }

            @Override
            public void onFailure(Call<Widget> call, Throwable t) {
                Log.e(TAG, "Error in onFailure: " + t.getMessage());
            }
        });
    }
}
