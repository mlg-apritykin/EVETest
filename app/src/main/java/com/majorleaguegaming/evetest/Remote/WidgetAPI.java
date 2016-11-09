package com.majorleaguegaming.evetest.Remote;

import com.majorleaguegaming.evetest.Model.Widget;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface WidgetAPI {

    String BASE_URL = "http://overwatch.majorleaguegaming.com/";

    @GET("widget")
    Call<Widget> getWidget();


    class Factory{

        private static WidgetAPI service;

        public static WidgetAPI getInstance(){
            Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .build();

            service = retrofit.create(WidgetAPI.class);
            return service;
        }
    }


}
