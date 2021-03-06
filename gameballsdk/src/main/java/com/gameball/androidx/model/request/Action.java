package com.gameball.androidx.model.request;

import android.se.omapi.Session;

import com.gameball.androidx.local.SharedPreferencesUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class Action
{
    @SerializedName("events")
    @Expose
    private HashMap<String, HashMap<String,Object>> events;
    @SerializedName("playerUniqueId")
    @Expose
    private String playerUniqueId;
    @SerializedName("isPositive")
    @Expose
    private boolean isPositive;
    @SerializedName("sessionInfo")
    @Expose
    private SessionInfo sessionInfo;

    public Action()
    {
        this.events = new HashMap<>();
        this.playerUniqueId = SharedPreferencesUtils.getInstance().getPlayerUniqueId();
        this.isPositive = true;
        sessionInfo = new SessionInfo();
    }

    public void addEvent(String eventName, HashMap<String, Object> metaData)
    {
        if(!this.events.containsKey(eventName))
        {
            this.events.put(eventName, new HashMap<String, Object>());
        }

        this.events.put(eventName, metaData);
    }


    private class SessionInfo {
        @SerializedName("platform")
        @Expose
        private int platform;
        protected SessionInfo() {
            platform = 4;
        }
    }
}
