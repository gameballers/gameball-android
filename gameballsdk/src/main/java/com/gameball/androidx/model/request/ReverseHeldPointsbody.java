package com.gameball.androidx.model.request;

public class ReverseHeldPointsbody extends PointTransactionParams
{
    public ReverseHeldPointsbody(String transactionKey)
    {
        super(transactionKey, true);
    }
}
