package com.gameball.androidx.views.mainContainer;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.gameball.androidx.model.response.ClientBotSettings;
import com.gameball.androidx.views.leaderBoard.LeaderBoardFragment;
import com.gameball.androidx.views.notification.NotificationFragment;
import com.gameball.androidx.views.profile.ProfileFragment;
import com.gameball.androidx.views.referral.ReferralFragment;

public class TabsAdapter extends FragmentPagerAdapter {
    private ClientBotSettings clientBotSettings;

    public TabsAdapter(FragmentManager fm, ClientBotSettings clientBotSettings) {
        super(fm);
        this.clientBotSettings = clientBotSettings;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ProfileFragment();
            case 1:
                if (clientBotSettings.isReferralOn())
                    return new ReferralFragment();
                else if (clientBotSettings.isEnableLeaderboard())
                    return new LeaderBoardFragment();
                else return new NotificationFragment();
            case 2:
                if (clientBotSettings.isReferralOn() && clientBotSettings.isEnableLeaderboard())
                    return new LeaderBoardFragment();
                else
                    return new NotificationFragment();
            case 3:
                return new NotificationFragment();
        }

        return new Fragment();
    }

    @Override
    public int getCount() {
        int count = 1;
        if(clientBotSettings.isReferralOn())
            count ++;
        if(clientBotSettings.isEnableLeaderboard())
            count ++;
        if (clientBotSettings.isEnableNotifications())
            count++;

        return count;
    }
}
