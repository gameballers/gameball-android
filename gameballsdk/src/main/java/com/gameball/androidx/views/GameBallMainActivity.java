package com.gameball.androidx.views;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

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
        ClientBotSettings botSettings = SharedPreferencesUtils.getInstance().getClientBotSettings();

        DisplayUtils.statusBarColorToSolid(this,botSettings.getBotMainColor());

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

    @Override
    protected void onDestroy()
    {
        LocalDataSource.getInstance().clear();
        super.onDestroy();
    }
}
