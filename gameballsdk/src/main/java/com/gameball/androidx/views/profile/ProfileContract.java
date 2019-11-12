package com.gameball.androidx.views.profile;

import com.gameball.androidx.model.response.Game;
import com.gameball.androidx.model.response.Mission;
import com.gameball.androidx.utils.BasePresenter;

import java.util.ArrayList;

public interface ProfileContract
{
    interface View
    {
        void onWithUnlocksLoaded(ArrayList<Game> games, ArrayList<Mission> missions);

        void showLoadingIndicator();

        void hideLoadingIndicator();
        void showNoInternetConnectionLayout();
    }

    interface Presenter extends BasePresenter
    {
        void getWithUnlocks();
    }
}
