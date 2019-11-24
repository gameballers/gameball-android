package com.gameball.androidx.network.profileRemote;

import com.gameball.androidx.model.request.Action;
import com.gameball.androidx.model.request.PlayerInfoBody;
import com.gameball.androidx.model.response.BaseResponse;
import com.gameball.androidx.model.response.ClientBotSettings;
import com.gameball.androidx.model.response.GetWithUnlocksWrapper;
import com.gameball.androidx.model.response.LeaderBoardResponse;
import com.gameball.androidx.model.response.Notification;
import com.gameball.androidx.model.response.PlayerInfoResponse;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.Single;


public interface ProfileDataSourceContract
{
    Single<BaseResponse<PlayerInfoResponse>> getPlayerInfo(String playerUniqueId);

    Single<BaseResponse<GetWithUnlocksWrapper>> getWithUnlocks(String playerUniqueId);

    Single<BaseResponse<LeaderBoardResponse>> getLeaderBoard(int playerId, int limit);
    Single<BaseResponse<ClientBotSettings>> getBotSettings();
    Completable AddNewAction(Action actionBody);
    Completable initializePlayer(PlayerInfoBody body);

    Single<BaseResponse<ArrayList<Notification>>> getNotificationHistory(String playerUniqueId);
}
