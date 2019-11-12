package com.gameball.androidx.views.achievements;

import android.content.Context;

import com.gameball.androidx.local.LocalDataSource;
import com.gameball.androidx.local.SharedPreferencesUtils;
import com.gameball.androidx.model.response.BaseResponse;
import com.gameball.androidx.model.response.GetWithUnlocksWrapper;
import com.gameball.androidx.network.profileRemote.ProfileRemoteProfileDataSource;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class AchievementsPresenter implements AchievemetsContract.Presenter
{
    private Context context;
    private AchievemetsContract.View view;
    private LocalDataSource localDataSource;
    private ProfileRemoteProfileDataSource remoteDataSource;
    private SharedPreferencesUtils sharedPreferencesUtils;

    public AchievementsPresenter(Context context, AchievemetsContract.View view)
    {
        this.context = context;
        this.view = view;
        localDataSource = LocalDataSource.getInstance();
        remoteDataSource = ProfileRemoteProfileDataSource.getInstance();
        sharedPreferencesUtils = SharedPreferencesUtils.getInstance();
    }

    @Override
    public void getAchievements()
    {
        view.showLoadingIndicator();
        remoteDataSource.getWithUnlocks(sharedPreferencesUtils.getPlayerUniqueId())
                .subscribe(new SingleObserver<BaseResponse<GetWithUnlocksWrapper>>()
                {
                    @Override
                    public void onSubscribe(Disposable d)
                    {

                    }

                    @Override
                    public void onSuccess(BaseResponse<GetWithUnlocksWrapper> arrayListBaseResponse)
                    {
                        view.hideLoadingIndicator();
                        view.fillAchievements(arrayListBaseResponse.getResponse().getGames());
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        view.hideLoadingIndicator();
                    }
                });
    }
}
