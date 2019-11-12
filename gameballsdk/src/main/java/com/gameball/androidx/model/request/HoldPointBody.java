package com.gameball.androidx.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HoldPointBody extends PointTransactionParams
{
    @SerializedName("OTP")
    @Expose
    private String otp;

    public HoldPointBody(double amount, String otp, String transactionKey)
    {
        super(amount,transactionKey,true);
        this.otp = otp;
        setAmount(amount);
    }
}
