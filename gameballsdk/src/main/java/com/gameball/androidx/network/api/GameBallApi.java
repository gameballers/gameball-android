package com.gameball.androidx.network.api;

import com.gameball.androidx.model.request.Action;
import com.gameball.androidx.model.request.GenerateOTPBody;
import com.gameball.androidx.model.request.GetPlayerBalanceBody;
import com.gameball.androidx.model.request.HoldPointBody;
import com.gameball.androidx.model.request.PlayerInfoBody;
import com.gameball.androidx.model.request.PlayerRegisterRequest;
import com.gameball.androidx.model.request.RedeemPointBody;
import com.gameball.androidx.model.request.ReferralBody;
import com.gameball.androidx.model.request.ReverseHeldPointsbody;
import com.gameball.androidx.model.request.RewardPointBody;
import com.gameball.androidx.model.response.BaseResponse;
import com.gameball.androidx.model.response.ClientBotSettings;
import com.gameball.androidx.model.response.GetWithUnlocksWrapper;
import com.gameball.androidx.model.response.HoldPointsResponse;
import com.gameball.androidx.model.response.LeaderBoardResponse;
import com.gameball.androidx.model.response.Notification;
import com.gameball.androidx.model.response.PlayerBalanceResponse;
import com.gameball.androidx.model.response.PlayerInfoResponse;
import com.gameball.androidx.model.response.PlayerRegisterResponse;
import com.gameball.androidx.network.Config;

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
    Single<BaseResponse<LeaderBoardResponse>> getLeaderBoard(@Query("playerId") int playerUniqueId,
                                                                        @Query("limit") int limit);

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
