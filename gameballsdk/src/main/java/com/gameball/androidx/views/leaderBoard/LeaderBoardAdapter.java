package com.gameball.androidx.views.leaderBoard;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gameball.androidx.R;
import com.gameball.androidx.local.SharedPreferencesUtils;
import com.gameball.androidx.model.response.ClientBotSettings;
import com.gameball.androidx.model.response.PlayerAttributes;
import com.gameball.androidx.utils.ImageDownloader;

import java.util.ArrayList;
import java.util.Locale;

public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.ItemRowHolder> {
    private Context mContext;
    private ArrayList<PlayerAttributes> mData;
    private ClientBotSettings clientBotSettings;

    public LeaderBoardAdapter(Context context, ArrayList<PlayerAttributes> data) {
        this.mData = data;
        this.mContext = context;
        clientBotSettings = SharedPreferencesUtils.getInstance().getClientBotSettings();
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.gb_leaderboard_item_layout, parent, false);
        ItemRowHolder rowHolder = new ItemRowHolder(row);
        rowHolder.setIsRecyclable(false);
        return rowHolder;
    }

    @Override
    public void onBindViewHolder(ItemRowHolder holder, int position) {
        PlayerAttributes item = mData.get(position);


        holder.rank.setText(String.format(Locale.getDefault(),"%d", position + 1));
        holder.frubiesTitle.setText(clientBotSettings.getRankPointsName());

        if(position < 3)
            holder.rank.setBackgroundTintList(ColorStateList
                    .valueOf(Color.parseColor(clientBotSettings.getBotMainColor())));

        if(item.getDisplayName() != null)
            holder.playerName.setText(item.getDisplayName());
        holder.playerCurrentLevelName.setText(item.getLevel().getName());
        holder.frubiesValue.setText(String.format(Locale.getDefault(),
                "%d",item.getAccFrubies()));
        if(item.getLevel().getIcon() != null)
            ImageDownloader.downloadImage(mContext, holder.playerLevelLogo,
                    item.getLevel().getIcon().getFileName());
    }

    public void setmData(ArrayList<PlayerAttributes> mData)
    {
        this.mData = mData;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView playerLevelLogo;
        public TextView playerName;
        public TextView playerCurrentLevelName;
        public TextView frubiesValue;
        public TextView rank;
        public TextView frubiesTitle;



        public ItemRowHolder(View itemView) {
            super(itemView);
            playerLevelLogo = itemView.findViewById(R.id.player_level_logo);
            playerName = itemView.findViewById(R.id.player_name);
            playerCurrentLevelName = itemView.findViewById(R.id.player_current_level_name);
            frubiesValue = itemView.findViewById(R.id.frubies_for_next_level);
            rank = itemView.findViewById(R.id.leader_rank);
            frubiesTitle = itemView.findViewById(R.id.frubies_title);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            final int pos = getLayoutPosition();
            int pos1 = getAdapterPosition();
            if (pos == pos1) {

            }
        }
    }
}
