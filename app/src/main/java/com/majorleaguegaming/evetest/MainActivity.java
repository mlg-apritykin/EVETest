package com.majorleaguegaming.evetest;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.majorleaguegaming.evetest.Model.Widget;
import com.majorleaguegaming.evetest.Remote.WidgetAPI;
import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.majorleaguegaming.evetest.R.id.imageView1;

public class MainActivity extends AppCompatActivity {

    //Team Card
    @BindView(R.id.linearLayout1) LinearLayout teamLinearLayout;
    @BindView(R.id.factTextView1) TextView text;
    @BindView(R.id.factTextView2) TextView text2;
    @BindView(imageView1) ImageView image;
    //Player Card
    @BindView(R.id.linearLayout2) LinearLayout playerLinearLayout;
    @BindView(R.id.playerText1) TextView playerText1;
    @BindView(R.id.playerText2) TextView playerText2;
    @BindView(R.id.playerName) TextView playerName;
    @BindView(R.id.playerImage) ImageView playerImage;
    //Debug Buttons
    @BindView(R.id.button) Button button;
    @BindView(R.id.button2) Button btnDebug;


    private static final String TAG = "MainActivity";
    private Timer timer;
    private TimerTask timertask;
    private Handler handler = new Handler();
    private int INTERVAL = 15000;
    public int id;
    public String name1 = ""
            ,nameType1 = ""
            ,name2 = ""
            ,nameType2 = ""
            ,image1URL = ""
            ,image2URL = ""
            ,dataType = ""
            ,data1 = ""
            ,data2 = ""
            ,widgetText = ""
            ,image1 = ""
            ,image2 = ""
            ,activeText = ""
            ,name_Display = ""
            ,team_Display = "";
    public boolean active = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        hideWidgetLayouts();
        refreshWidgetInterval();

    }

    private void hideWidgetLayouts() {
        teamLinearLayout.setVisibility(View.INVISIBLE);
        playerLinearLayout.setVisibility(View.INVISIBLE);
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
                        Log.d(TAG, "run: Checking Callback for updated JSON");
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


    @OnClick(R.id.button2) public void checkResponse(){
        WidgetAPI.Factory.getInstance().getWidget().enqueue(new Callback<Widget>() {
            @Override
            public void onResponse(Call<Widget> call, Response<Widget> response) {
                Log.e(TAG, "JSON INFO:\n "
                + id + "\n" + name1 + "\n" + nameType1 + "\n" + name2 + "\n" +
                nameType2 + "\n" + image1URL + "\n" + image2URL + "\n" + dataType + "\n" +
                data1 + "\n" + data2 + "\n" + widgetText + "\n" + active + "\n" + image1 + "\n" +
                activeText);
            }

            @Override
            public void onFailure(Call<Widget> call, Throwable t) {

            }
        });
    }


    // Method that calls WidgetAPI to fetch response from JSON and set View variables with data
    public void updateWidget(){

        WidgetAPI.Factory.getInstance().getWidget().enqueue(new Callback<Widget>() {
            @Override
            public void onResponse(Call<Widget> call, Response<Widget> response) {
//                text.setText(response.body().getText());
//                text2.setText(response.body().getData1());
//                Picasso.with(getApplicationContext()).load((String) response.body().getImage1()).into(image);
                id = response.body().getId();
                name1 = response.body().getName1();
                nameType1 = response.body().getName1Type();
                name2 = (String) response.body().getName2();
                nameType2 = (String) response.body().getName2Type();
                image1URL = (String) response.body().getImage1Url();
                image2URL = (String) response.body().getImage2Url();
                dataType = response.body().getDataType();
                data1 = response.body().getData1();
                data2 = (String) response.body().getData2();
                widgetText = response.body().getText();
                active = response.body().isActive();
                image1 = response.body().getImage1();
                image2 = response.body().getImage2();
                activeText = response.body().getActiveText();
                name_Display = response.body().getName1DisplayName();
                Log.d(TAG, "onResponse: Type - " + nameType1);


                if(nameType1.equals("player")){
                    Log.d(TAG, "onResponse: I AM INSIDE PLAYER LOOP!");
                    teamLinearLayout.setVisibility(View.INVISIBLE);
                    playerLinearLayout.setVisibility(View.VISIBLE);

                    playerName.setText(name_Display);
                    playerText2.setText(data1);
                    playerText1.setText(widgetText);
                    Picasso.with(getApplicationContext()).load((String) response.body().getImage1()).into(playerImage);

                }
                else{
                    playerLinearLayout.setVisibility(View.INVISIBLE);
                    teamLinearLayout.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onFailure(Call<Widget> call, Throwable t) {
                Log.e(TAG, "Error in onFailure: " + t.getMessage());
            }
        });
    }
}
