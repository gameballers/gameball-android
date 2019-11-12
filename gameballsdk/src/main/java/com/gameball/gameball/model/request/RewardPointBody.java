package com.gameball.gameball.model.request;

public class RewardPointBody extends PointTransactionParams
{
    public RewardPointBody(double amount, String transactionOnClientSystemId, String transactionKey)
    {
        super(amount,transactionKey,true);
        setAmount(amount);
        setTransactionOnClientSystemId(transactionOnClientSystemId);

    }
}
