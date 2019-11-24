package com.gameball.androidx.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlayerRank {
    @SerializedName("rowOrder")
    @Expose
    private Integer rowOrder;
    @SerializedName("frubies")
    @Expose
    private Integer frubies;
    @SerializedName("playerId")
    @Expose
    private Integer playerId;
    @SerializedName("playersCount")
    @Expose
    private Integer playersCount;

    public Integer getRowOrder() {
        return rowOrder;
    }

    public Integer getFrubies() {
        return frubies;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public Integer getPlayersCount() {
        return playersCount;
    }
}
