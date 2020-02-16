package com.gameball.androidx.views.referral;

import com.gameball.androidx.model.response.Game;

import java.util.ArrayList;

public interface ReferralContract
{
    interface View
    {
        void onReferralChallengesFiltered(ArrayList<Game> games, int friendsReferral);
    }

    interface Presenter
    {
        void getReferralChallenges();
    }
}
