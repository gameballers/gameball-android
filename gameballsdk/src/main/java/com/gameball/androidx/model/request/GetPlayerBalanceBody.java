package com.gameball.androidx.model.request;

public class GetPlayerBalanceBody extends PointTransactionParams
{
    public GetPlayerBalanceBody(String transactionKey)
    {
        super(transactionKey, false);
    }
}
