package com.gameball.androidx.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LeaderBoardResponse {
    public final static int TODAY = 1;
    public final static int YESTERDAY = 2;
    public final static int THIS_WEEK = 3;
    public final static int LAST_WEEK = 4;
    public final static int THIS_MONTH = 5;
    public final static int LAST_MONTH = 6;
    public final static int THIS_YEAR = 7;
    public final static int ALL = 8;


    @SerializedName("playerBot")
    @Expose
    private ArrayList<PlayerAttributes> playerBot;
    @SerializedName("playerRank")
    @Expose
    private PlayerRank playerRank;

    public ArrayList<PlayerAttributes> getPlayerBot() {
        return playerBot;
    }

    public PlayerRank getPlayerRank() {
        return playerRank;
    }
}
