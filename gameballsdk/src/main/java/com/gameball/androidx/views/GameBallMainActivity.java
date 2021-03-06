package com.gameball.androidx.views;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.gameball.androidx.R;
import com.gameball.androidx.local.LocalDataSource;
import com.gameball.androidx.local.SharedPreferencesUtils;
import com.gameball.androidx.model.response.ClientBotSettings;
import com.gameball.androidx.utils.DisplayUtils;
import com.gameball.androidx.views.mainContainer.MainContainerFragment;

public class GameBallMainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gb_activity_gameball_main);

        updateStatusBarColor();
        findViewById(R.id.gb_footer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.gameball.co/landing_mobile/?utm_source=Mobile%20apps&utm_medium=Mobile%20footer&utm_campaign=Mobile%20users"));
                startActivity(browserIntent);
            }
        });

        navigateToFragment(new MainContainerFragment());

    }

    public void navigateToFragment(Fragment fragment)
    {
        if (fragment != null /*&& !is(fragment.getClass().getSimpleName())*/)
        {
            String tag = fragment.getClass().getSimpleName();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                    android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);

            fragmentTransaction.replace(R.id.gb_main_activity_container, fragment, tag);

            fragmentTransaction.commit();
        }
    }

    public void updateStatusBarColor(){
        DisplayUtils.statusBarColorToSolid(this,SharedPreferencesUtils.getInstance().getClientBotSettings().getBotMainColor());
    }

    @Override
    protected void onDestroy()
    {
        LocalDataSource.getInstance().clear();
        super.onDestroy();
    }
}
