package com.moneam.gameball;

import android.app.Application;

import com.gameball.androidx.GameBallApp;

/**
 * Created by Ahmed Abdelmoneam Abdelfattah on 8/23/2018.
 */
public class GameBallDemoApplication extends Application {
    public static final String ExternalId = "00397ce7-216b-4ce1-a461-dd897c8231bb";

    @Override
    public void onCreate() {
        super.onCreate();

        GameBallApp.getInstance(this).init("8fdfd2dffd-9mnvhu25d6c3d" ,
                "assy2@gameball.co",
                3, R.mipmap.ic_launcher);

        // TODO: 8/23/2018
    }
}
