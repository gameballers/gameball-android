package com.gameball.gameball.network.api;

import com.gameball.gameball.model.request.Action;
import com.gameball.gameball.model.request.GenerateOTPBody;
import com.gameball.gameball.model.request.GetPlayerBalanceBody;
import com.gameball.gameball.model.request.HoldPointBody;
import com.gameball.gameball.model.request.PlayerInfoBody;
import com.gameball.gameball.model.request.PlayerRegisterRequest;
import com.gameball.gameball.model.request.RedeemPointBody;
import com.gameball.gameball.model.request.ReferralBody;
import com.gameball.gameball.model.request.ReverseHeldPointsbody;
import com.gameball.gameball.model.request.RewardPointBody;
import com.gameball.gameball.model.response.BaseResponse;
import com.gameball.gameball.model.response.ClientBotSettings;
import com.gameball.gameball.model.response.GetWithUnlocksWrapper;
import com.gameball.gameball.model.response.HoldPointsResponse;
import com.gameball.gameball.model.response.Notification;
import com.gameball.gameball.model.response.PlayerAttributes;
import com.gameball.gameball.model.response.PlayerBalanceResponse;
import com.gameball.gameball.model.response.PlayerInfoResponse;
import com.gameball.gameball.model.response.PlayerRegisterResponse;
import com.gameball.gameball.network.Config;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Ahmed Abdelmoneam Abdelfattah on 8/23/2018.
 */
public interface GameBallApi {
    @POST(Config.InitializePlayer)
    Single<BaseResponse<PlayerRegisterResponse>> registrationPlayer(
            @Body PlayerRegisterRequest playerRegisterRequest);

    @POST(Config.Push)
    Single<Response<Void>> push(@Header("token") String token);

    @GET(Config.PlayerInfo)
    Single<BaseResponse<PlayerInfoResponse>> getPlayerDetails(@Query("PlayerUniqueId") String playerUniqueId);

    @GET(Config.GetWithUnlocks)
    Single<BaseResponse<GetWithUnlocksWrapper>> getWithUnlocks(@Query("PlayerUniqueId") String playerUniqueId);

    @GET(Config.GetLeaderBoard)
    Single<BaseResponse<ArrayList<PlayerAttributes>>> getLeaderBoard(@Query("PlayerUniqueId") String playerUniqueId);

    @GET(Config.GetBotSettings)
    Single<BaseResponse<ClientBotSettings>> getBotSettings();

    @POST(Config.AddNewAction)
    Completable addNewAtion(@Body Action actionBody);

    @POST(Config.RewardPoints)
    Completable rewardPoints(@Body RewardPointBody rewardPointsBody);

    @POST(Config.HoldPoints)
    Single<BaseResponse<HoldPointsResponse>> holdPoints(@Body HoldPointBody body);

    @POST(Config.RedeemPoints)
    Completable redeemPoints(@Body RedeemPointBody body);

    @POST(Config.GenerateOTP)
    Completable generateOTP(@Body GenerateOTPBody body);

    @POST(Config.ReverseHeld)
    Completable reverseHeldPoints(@Body ReverseHeldPointsbody body);

    @POST(Config.referral)
    Completable addReferral(@Body ReferralBody body);

    @POST(Config.GetPlayerBalance)
    Single<BaseResponse<PlayerBalanceResponse>> getPlayerBalance(@Body GetPlayerBalanceBody body);

    @POST(Config.InitializePlayer)
    Completable initializePlayer(@Body PlayerInfoBody body);

    @GET(Config.notifications)
    Single<BaseResponse<ArrayList<Notification>>> getPlayerNotificationHistory(@Query("PlayerUniqueId") String playerUniqueId);
    
    

}
