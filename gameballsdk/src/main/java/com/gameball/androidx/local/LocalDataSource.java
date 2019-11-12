package com.gameball.androidx.local;

import com.gameball.androidx.model.response.Game;
import com.gameball.androidx.model.response.Level;
import com.gameball.androidx.model.response.Mission;
import com.gameball.androidx.model.response.PlayerAttributes;

import java.util.ArrayList;

public class LocalDataSource
{
    public static LocalDataSource instance;

    public PlayerAttributes playerAttributes;
    public Level nextLevel;
    public ArrayList<Game> games;
    public ArrayList<Mission> missions;

    private LocalDataSource()
    {
    }

    public static LocalDataSource getInstance()
    {
        if(instance == null)
            instance = new LocalDataSource();

        return instance;
    }

    public void clear()
    {
        instance = new LocalDataSource();
    }
}
