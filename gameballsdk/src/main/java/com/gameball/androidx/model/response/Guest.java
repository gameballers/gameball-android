package com.gameball.androidx.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Guest {
    @SerializedName("currency")
    @Expose
    private String currancy;
    @SerializedName("redemptionFactor")
    @Expose
    private double redemptionFactor;

    public String getCurrancy() {
        return currancy;
    }

    public double getRedemptionFactor() {
        return redemptionFactor;
    }
}
