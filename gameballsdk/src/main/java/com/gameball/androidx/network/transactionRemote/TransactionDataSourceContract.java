package com.gameball.androidx.network.transactionRemote;

import com.gameball.androidx.model.request.GenerateOTPBody;
import com.gameball.androidx.model.request.GetPlayerBalanceBody;
import com.gameball.androidx.model.request.HoldPointBody;
import com.gameball.androidx.model.request.RedeemPointBody;
import com.gameball.androidx.model.request.ReferralBody;
import com.gameball.androidx.model.request.ReverseHeldPointsbody;
import com.gameball.androidx.model.request.RewardPointBody;
import com.gameball.androidx.model.response.BaseResponse;
import com.gameball.androidx.model.response.HoldPointsResponse;
import com.gameball.androidx.model.response.PlayerBalanceResponse;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface TransactionDataSourceContract
{
    Completable rewardPoints(RewardPointBody body);
    Single<BaseResponse<HoldPointsResponse>> holdPoints(HoldPointBody body);
    Completable redeemPoints(RedeemPointBody body);
    Completable generateOtp(GenerateOTPBody body);
    Completable reverseHeldPoints(ReverseHeldPointsbody body);
    Single<BaseResponse<PlayerBalanceResponse>> getPlayerBalance(GetPlayerBalanceBody body);
    Completable addReferral(ReferralBody body);
}
