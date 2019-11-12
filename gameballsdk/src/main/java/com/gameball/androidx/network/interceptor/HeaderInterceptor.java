package com.gameball.androidx.network.interceptor;

import com.gameball.androidx.local.SharedPreferencesUtils;
import com.gameball.androidx.utils.Constants;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor
{
    @Override
    public Response intercept(Chain chain) throws IOException
    {
        Request request = chain.request();

        Request.Builder builder = request.newBuilder();

        if (SharedPreferencesUtils.getInstance().getClientId() != null)
        {
            builder.addHeader(Constants.APIKey,
                    SharedPreferencesUtils.getInstance().getClientId());

        }

        builder.addHeader(Constants.LangKey, Locale.getDefault().getLanguage());

        request = builder.build();
        return chain.proceed(request);
    }
}
