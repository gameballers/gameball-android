package com.gameball.androidx.views.achievements;

import com.gameball.androidx.model.response.Game;

import java.util.ArrayList;

public interface AchievemetsContract
{
    interface View
    {
        void fillAchievements(ArrayList<Game> games);

        void showLoadingIndicator();

        void hideLoadingIndicator();

    }

    interface Presenter
    {
        void getAchievements();
    }
}
