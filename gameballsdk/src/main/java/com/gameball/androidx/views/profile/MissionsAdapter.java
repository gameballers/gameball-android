package com.gameball.androidx.views.profile;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gameball.androidx.R;
import com.gameball.androidx.local.SharedPreferencesUtils;
import com.gameball.androidx.model.response.ClientBotSettings;
import com.gameball.androidx.model.response.Mission;
import com.gameball.androidx.utils.ImageDownloader;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.Locale;

public class MissionsAdapter extends RecyclerView.Adapter<MissionsAdapter.ItemRowHolder> {
    private Context context;
    private ArrayList<Mission> mData;
    private ClientBotSettings clientBotSettings;

    public MissionsAdapter(Context context, ArrayList<Mission> mData) {
        this.context = context;
        this.mData = mData;
        clientBotSettings = SharedPreferencesUtils.getInstance().getClientBotSettings();
    }

    @NonNull
    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.gb_mission_item_layout, parent, false);
        ItemRowHolder rh = new ItemRowHolder(row);
        return rh;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemRowHolder holder, int position) {
        Mission item = mData.get(position);

        ImageDownloader.downloadImage(context, holder.missionIcon, item.getIcon());
        holder.missionName.setText(item.getQuestName());
        if (item.getRewardPoints() > 0 && item.getRewardFrubies() > 0) {
            holder.missionReward.setText(
                    context.getString(
                            R.string.reward_text,
                            item.getRewardFrubies(),
                            clientBotSettings.getRankPointsName(),
                            item.getRewardPoints(),
                            clientBotSettings.getWalletPointsName()
                    )
            );
        } else if (item.getRewardPoints() > 0) {
            holder.missionReward.setText(String.format(Locale.getDefault(),
                    "%d %s",
                    item.getRewardPoints(),
                    clientBotSettings.getWalletPointsName()));
        } else if(item.getRewardFrubies() > 0) {
            holder.missionReward.setText(String.format(Locale.getDefault(),
                    "%d %s",
                    item.getRewardFrubies(),
                    clientBotSettings.getRankPointsName()));
        }

        holder.challengesCount.setText(
                context.getString(R.string.count_challenges,
                        item.getQuestChallenges().size())
        );
        holder.missionProgress.setProgress(item.getCompletionPercentage().intValue());

        if (item.getCompletionPercentage() < 100) {
            holder.missionCompletionPercentage.setVisibility(View.VISIBLE);
            holder.greenCheckIcon.setVisibility(View.GONE);

            if (item.getCompletionPercentage() != null)
                holder.missionCompletionPercentage.setText(
                        context.getString(R.string.percentage,
                                item.getCompletionPercentage().intValue())
                );
        } else {
            holder.greenCheckIcon.setVisibility(View.VISIBLE);
            holder.missionCompletionPercentage.setVisibility(View.GONE);
        }

        MissionChallengesAdapter adapter = new MissionChallengesAdapter(context, item.getQuestChallenges(), item.isOrdered());
        holder.missionChallengesRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.missionChallengesRecyclerView.setNestedScrollingEnabled(false);
        holder.missionChallengesRecyclerView.setAdapter(adapter);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setmData(ArrayList<Mission> mData) {
        this.mData = mData;
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private View itemView;
        private ImageView missionIcon;
        private TextView missionCompletionPercentage;
        private ImageView greenCheckIcon;
        private TextView missionName;
        private TextView missionReward;
        private TextView challengesCount;
        private ExpandableLayout missionChallengesExpandableLayout;
        private RecyclerView missionChallengesRecyclerView;
        private ProgressBar missionProgress;


        public ItemRowHolder(@NonNull View itemView) {
            super(itemView);

            this.itemView = itemView;
            missionIcon = itemView.findViewById(R.id.gb_mission_icon);
            missionCompletionPercentage = itemView.findViewById(R.id.gb_mission_completion_percentage);
            greenCheckIcon = itemView.findViewById(R.id.gb_green_check_icon);
            missionName = itemView.findViewById(R.id.gb_mission_name);
            missionReward = itemView.findViewById(R.id.gb_mission_reward);
            challengesCount = itemView.findViewById(R.id.gb_challenges_count);
            missionChallengesExpandableLayout = itemView.findViewById(R.id.gb_missions_expandable_layout);
            missionChallengesRecyclerView = itemView.findViewById(R.id.gb_mission_challenges_recyclerView);
            missionProgress = itemView.findViewById(R.id.gb_mission_progress);

            itemView.setOnClickListener(this);

            LayerDrawable eventProgress = (LayerDrawable) missionProgress.getProgressDrawable();
            eventProgress.setColorFilter(Color.parseColor(clientBotSettings.getBotMainColor()), PorterDuff.Mode.SRC_IN);
            challengesCount.setTextColor(Color.parseColor(clientBotSettings.getBotMainColor()));
            missionChallengesExpandableLayout.setVisibility(View.GONE);
        }

        @Override
        public void onClick(View view) {
            missionChallengesExpandableLayout.toggle();
        }
    }
}
