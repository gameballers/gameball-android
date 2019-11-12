package com.gameball.androidx.views.profile;

import android.content.Context;

import com.gameball.androidx.local.LocalDataSource;
import com.gameball.androidx.local.SharedPreferencesUtils;
import com.gameball.androidx.model.response.BaseResponse;
import com.gameball.androidx.model.response.GetWithUnlocksWrapper;
import com.gameball.androidx.network.profileRemote.ProfileRemoteProfileDataSource;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class ProfilePresenter implements ProfileContract.Presenter
{
    private Context context;
    private ProfileContract.View view;
    private LocalDataSource localDataSource;
    private ProfileRemoteProfileDataSource profileRemoteDataSource;
    private SharedPreferencesUtils sharedPreferencesUtils;
    private CompositeDisposable disposable;

    public ProfilePresenter(Context context, ProfileContract.View view)
    {
        this.context = context;
        this.view = view;
        profileRemoteDataSource = ProfileRemoteProfileDataSource.getInstance();
        localDataSource = LocalDataSource.getInstance();
        sharedPreferencesUtils = SharedPreferencesUtils.getInstance();
        disposable = new CompositeDisposable();
    }

    @Override
    public void getWithUnlocks()
    {
        if(localDataSource.games != null)
        {
            view.onWithUnlocksLoaded(localDataSource.games, localDataSource.missions);
            return;
        }
        view.showLoadingIndicator();
        profileRemoteDataSource.getWithUnlocks(sharedPreferencesUtils.getPlayerUniqueId())
                .subscribe(new SingleObserver<BaseResponse<GetWithUnlocksWrapper>>()
                {
                    @Override
                    public void onSubscribe(Disposable d)
                    {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(BaseResponse<GetWithUnlocksWrapper> arrayListBaseResponse)
                    {
                        localDataSource.games = arrayListBaseResponse.getResponse().getGames();
                        localDataSource.missions = arrayListBaseResponse.getResponse().getMissions();

                        view.onWithUnlocksLoaded(localDataSource.games, localDataSource.missions);
                        view.hideLoadingIndicator();
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        e.printStackTrace();
                        view.showNoInternetConnectionLayout();
                        view.hideLoadingIndicator();
                    }
                });
    }

    @Override
    public void unsubscribe()
    {
        disposable.clear();
    }
}
