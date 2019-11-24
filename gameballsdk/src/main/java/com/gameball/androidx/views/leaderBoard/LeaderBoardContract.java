package com.gameball.androidx.views.leaderBoard;

import com.gameball.androidx.model.response.LeaderBoardResponse;
import com.gameball.androidx.utils.BasePresenter;

public interface LeaderBoardContract
{
    interface View
    {
        void fillLeaderBoard(LeaderBoardResponse leaderBoardResponse);

        void showLoadingIndicator();

        void hideLoadingIndicator();

        void showNoInternetLayout();
    }

    interface Presenter extends BasePresenter
    {
        void getLeaderBoard(int limit);
    }
}
