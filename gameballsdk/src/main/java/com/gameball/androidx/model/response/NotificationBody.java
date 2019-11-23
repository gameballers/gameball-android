package com.gameball.androidx.model.response;

import java.io.Serializable;

public class NotificationBody implements Serializable
{
    public final static String SMALL_TOAST = "Small Toast";
    public final static String LARGE_TOAST = "Large Toast";
    public final static String POPUP = "Popup";

    private String title;
    private String body;
    private String icon;
    private String type;

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getBody()
    {
        return body;
    }

    public void setBody(String body)
    {
        this.body = body;
    }

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
