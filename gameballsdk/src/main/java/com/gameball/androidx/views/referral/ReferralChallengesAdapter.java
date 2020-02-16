package com.gameball.androidx.views.referral;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gameball.androidx.R;
import com.gameball.androidx.local.SharedPreferencesUtils;
import com.gameball.androidx.model.response.ClientBotSettings;
import com.gameball.androidx.model.response.Game;
import com.gameball.androidx.utils.Constants;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Locale;

public class ReferralChallengesAdapter extends RecyclerView.Adapter<ReferralChallengesAdapter.ItemViewHolder> {
    private Context mContext;
    private ArrayList<Game> mData;
    private ClientBotSettings clientBotSettings;
    private FragmentManager fm;


    public ReferralChallengesAdapter(Context context, FragmentManager fm, ArrayList<Game> data) {
        this.mData = data;
        this.mContext = context;
        clientBotSettings = SharedPreferencesUtils.getInstance().getClientBotSettings();
        this.fm = fm;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.gb_referral_challenge_item_layout, parent, false);
        return new ItemViewHolder(row);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Game item = mData.get(position);

        String rewardStr = String.format(Locale.getDefault(), "%d %s | %d %s", item.getRewardFrubies(),
                clientBotSettings.getRankPointsName(), item.getRewardPoints(),
                clientBotSettings.getWalletPointsName());

        holder.challengeRewardTxt.setText(rewardStr);

        if (position == mData.size() - 1)
            holder.referralItemSeparator.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setmData(ArrayList<Game> mData) {
        this.mData = mData;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView challengeRewardTxt;
        View referralItemSeparator;
        TextView challengeDescriptionBtn;


        public ItemViewHolder(View itemView) {
            super(itemView);
            challengeRewardTxt = itemView.findViewById(R.id.gb_challenge_reward_txt);
            referralItemSeparator = itemView.findViewById(R.id.referral_item_separator);
            challengeDescriptionBtn = itemView.findViewById(R.id.gb_challenge_description_btn);

            challengeDescriptionBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            final int pos = getLayoutPosition();
            int pos1 = getAdapterPosition();
            if (pos == pos1) {
                BottomSheetDialogFragment fragment = new ReferralChallengeDescriptionBottomSheet();
                Bundle bundle = new Bundle() ;
                bundle.putString(Constants.GAME_OBJ_KEY, new Gson().toJson(mData.get(pos)));
                fragment.setArguments(bundle);
                fragment.show(fm,"referral_challenge_description");
            }
        }
    }
}
