package com.gameball.androidx.model.request;

public class GenerateOTPBody extends PointTransactionParams
{
    public GenerateOTPBody(String transactionKey)
    {
        super(transactionKey,true);
    }
}
