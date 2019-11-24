package com.gameball.androidx.views.leaderBoard;

import android.content.Context;

import com.gameball.androidx.local.LocalDataSource;
import com.gameball.androidx.local.SharedPreferencesUtils;
import com.gameball.androidx.model.response.BaseResponse;
import com.gameball.androidx.model.response.LeaderBoardResponse;
import com.gameball.androidx.network.profileRemote.ProfileRemoteProfileDataSource;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class LeaderBoardPresenter implements LeaderBoardContract.Presenter
{
    private Context context;
    private LeaderBoardContract.View view;
    private LocalDataSource localDataSource;
    private ProfileRemoteProfileDataSource profileRemoteDataSource;
    private SharedPreferencesUtils sharedPreferencesUtils;
    private CompositeDisposable disposable;

    public LeaderBoardPresenter(Context context, LeaderBoardContract.View view)
    {
        this.context = context;
        this.view = view;
        localDataSource = LocalDataSource.getInstance();
        profileRemoteDataSource = ProfileRemoteProfileDataSource.getInstance();
        sharedPreferencesUtils = SharedPreferencesUtils.getInstance();
        disposable = new CompositeDisposable();
    }

    @Override
    public void getLeaderBoard(int limit)
    {
        view.showLoadingIndicator();
        profileRemoteDataSource.getLeaderBoard(sharedPreferencesUtils.getPlayerID(), limit)
                .subscribe(new SingleObserver<BaseResponse<LeaderBoardResponse>>()
                {
                    @Override
                    public void onSubscribe(Disposable d)
                    {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(BaseResponse<LeaderBoardResponse> leaderBoardResponseBaseResponse)
                    {
                        view.fillLeaderBoard(leaderBoardResponseBaseResponse.getResponse());
                        view.hideLoadingIndicator();
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        view.hideLoadingIndicator();
                        view.showNoInternetLayout();
                    }
                });
    }

    @Override
    public void unsubscribe()
    {
        disposable.clear();
    }
}
