package com.gameball.gameball.network.transactionRemote;

import com.gameball.gameball.model.request.GenerateOTPBody;
import com.gameball.gameball.model.request.GetPlayerBalanceBody;
import com.gameball.gameball.model.request.HoldPointBody;
import com.gameball.gameball.model.request.RedeemPointBody;
import com.gameball.gameball.model.request.ReferralBody;
import com.gameball.gameball.model.request.ReverseHeldPointsbody;
import com.gameball.gameball.model.request.RewardPointBody;
import com.gameball.gameball.model.response.BaseResponse;
import com.gameball.gameball.model.response.HoldPointsResponse;
import com.gameball.gameball.model.response.PlayerBalanceResponse;
import com.gameball.gameball.network.Network;
import com.gameball.gameball.network.api.GameBallApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TransactionRemoteDataSource implements TransactionDataSourceContract
{
    private static TransactionRemoteDataSource instance;
    private GameBallApi gameBallApi;
    private Gson jsonFactory;

    private TransactionRemoteDataSource()
    {
        jsonFactory = new GsonBuilder()
                .setPrettyPrinting()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        gameBallApi = Network.getInstance().getGameBallApi();
    }

    public static TransactionRemoteDataSource getInstance()
    {
        if (instance == null)
            instance = new TransactionRemoteDataSource();

        return instance;
    }

    @Override
    public Completable rewardPoints(RewardPointBody body)
    {
        return gameBallApi.rewardPoints(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<BaseResponse<HoldPointsResponse>> holdPoints(HoldPointBody body)
    {
        return gameBallApi.holdPoints(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Completable redeemPoints(RedeemPointBody body)
    {
        return gameBallApi.redeemPoints(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Completable generateOtp(GenerateOTPBody body)
    {
        return gameBallApi.generateOTP(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Completable reverseHeldPoints(ReverseHeldPointsbody body)
    {
        return gameBallApi.reverseHeldPoints(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<BaseResponse<PlayerBalanceResponse>> getPlayerBalance(GetPlayerBalanceBody body)
    {
        return gameBallApi.getPlayerBalance(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Completable addReferral(ReferralBody body)
    {
        return gameBallApi.addReferral(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
