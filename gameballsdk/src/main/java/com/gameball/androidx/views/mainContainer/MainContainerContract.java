package com.gameball.androidx.views.mainContainer;

import com.gameball.androidx.model.response.Level;
import com.gameball.androidx.model.response.PlayerAttributes;
import com.gameball.androidx.utils.BasePresenter;
import com.gameball.androidx.utils.BaseView;

public interface MainContainerContract {

    interface View extends BaseView
    {
        void onProfileInfoLoaded(PlayerAttributes playerAttributes, Level nextLevel);
        void showNoInterNetConnection();
        void updateBotSettings();
    }

    interface Presenter extends BasePresenter
    {
        void getPlayerInfo();
    }
}
