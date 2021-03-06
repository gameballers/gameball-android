package com.gameball.androidx.network;

public interface Callback<T>
{
    void onSuccess(T t);
    void onError(Throwable e);
}
