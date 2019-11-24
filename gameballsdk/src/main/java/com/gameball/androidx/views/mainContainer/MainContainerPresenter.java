package com.gameball.androidx.views.mainContainer;

import com.gameball.androidx.local.LocalDataSource;
import com.gameball.androidx.local.SharedPreferencesUtils;
import com.gameball.androidx.model.response.BaseResponse;
import com.gameball.androidx.model.response.ClientBotSettings;
import com.gameball.androidx.model.response.PlayerInfoResponse;
import com.gameball.androidx.network.profileRemote.ProfileRemoteProfileDataSource;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class MainContainerPresenter implements MainContainerContract.Presenter
{
    private MainContainerContract.View view;
    private LocalDataSource localDataSource;
    private ProfileRemoteProfileDataSource profileRemoteDataSource;
    private SharedPreferencesUtils sharedPreferencesUtils;
    private CompositeDisposable disposable;

    public MainContainerPresenter(MainContainerContract.View view)
    {
        this.view = view;
        localDataSource = LocalDataSource.getInstance();
        profileRemoteDataSource = ProfileRemoteProfileDataSource.getInstance();
        sharedPreferencesUtils = SharedPreferencesUtils.getInstance();
        disposable = new CompositeDisposable();
    }

    @Override
    public void getPlayerInfo()
    {
        view.showLoadingIndicator();


        profileRemoteDataSource.getBotSettings()
                .subscribe(new SingleObserver<BaseResponse<ClientBotSettings>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(BaseResponse<ClientBotSettings> clientBotSettingsBaseResponse) {
                        sharedPreferencesUtils.putClientBotSettings(clientBotSettingsBaseResponse.getResponse());
                        view.updateBotSettings();

                        profileRemoteDataSource.getPlayerInfo(sharedPreferencesUtils.getPlayerUniqueId())
                                .subscribe(new SingleObserver<BaseResponse<PlayerInfoResponse>>()
                                {
                                    @Override
                                    public void onSubscribe(Disposable d)
                                    {
                                        disposable.add(d);
                                    }

                                    @Override
                                    public void onSuccess(BaseResponse<PlayerInfoResponse> playerInfoResponseBaseResponse)
                                    {
                                        sharedPreferencesUtils.putPlayerID(playerInfoResponseBaseResponse.getResponse().getPlayerAttributes().getPlayerId());
                                        localDataSource.playerAttributes = playerInfoResponseBaseResponse.getResponse().
                                                getPlayerAttributes();
                                        localDataSource.nextLevel = playerInfoResponseBaseResponse.getResponse().
                                                getNextLevel();
                                        view.hideLoadingIndicator();
                                        view.onProfileInfoLoaded(localDataSource.playerAttributes, localDataSource.nextLevel);
                                    }

                                    @Override
                                    public void onError(Throwable e)
                                    {
                                        e.printStackTrace();
                                        view.showNoInterNetConnection();
                                        view.hideLoadingIndicator();
                                    }
                                });
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showNoInterNetConnection();
                        view.hideLoadingIndicator();
                        e.printStackTrace();
                    }
                });
    }

    @Override
    public void unsubscribe()
    {
        disposable.clear();
    }
}
