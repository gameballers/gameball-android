package com.gameball.androidx.views.notification;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.gameball.androidx.R;
import com.gameball.androidx.local.SharedPreferencesUtils;
import com.gameball.androidx.model.response.ClientBotSettings;
import com.gameball.androidx.model.response.Notification;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NotificationsHistoryAdapter extends RecyclerView.Adapter<NotificationsHistoryAdapter.ItemRowHolder> {

    Context context;
    private ArrayList<Notification> mdata;
    private ClientBotSettings clientBotSettingsl;
    SimpleDateFormat apiDateFormat;
    SimpleDateFormat viewDateFormat;

    public NotificationsHistoryAdapter(Context context, ArrayList<Notification> mdata)
    {
        this.mdata = mdata;
        this.context = context;
        this.clientBotSettingsl = SharedPreferencesUtils.getInstance().getClientBotSettings();

        apiDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        viewDateFormat = new SimpleDateFormat("d MMM", Locale.getDefault());
    }


    @NonNull
    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.gb_notification_history_item_layout, parent, false);
        ItemRowHolder rowHolder = new ItemRowHolder(row);
        rowHolder.setIsRecyclable(false);
        return rowHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemRowHolder holder, int position) {
        Notification item = this.mdata.get(position);

        if(item.getIconPath() != null && !item.getIconPath().isEmpty())
            Picasso.get()
            .load(item.getIconPath())
            .into(holder.icon);

        holder.title.setText(item.getTitleApp());
        holder.body.setText(item.getBodyApp());

        try {
            Date notiDate = apiDateFormat.parse(item.getDateTime());
            String viewDateStr = viewDateFormat.format( notiDate);
            holder.date.setText(viewDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void setMdata(ArrayList<Notification> mdata) {
        this.mdata = mdata;
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder{
        ImageView icon;
        TextView title, body, date;

        public ItemRowHolder(@NonNull View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.notification_icon);
            title = itemView.findViewById(R.id.notification_title);
            body = itemView.findViewById(R.id.notification_body);
            date = itemView.findViewById(R.id.notification_date);
        }
    }
}
