package com.gameball.androidx.views.referral;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gameball.androidx.R;
import com.gameball.androidx.local.SharedPreferencesUtils;
import com.gameball.androidx.model.response.ClientBotSettings;
import com.gameball.androidx.model.response.Game;
import com.gameball.androidx.utils.Constants;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;

public class ReferralChallengeDescriptionBottomSheet extends BottomSheetDialogFragment {

    private View rootView;
    private TextView howToEarnTv;
    private TextView referralChallengeDescriptionTv;

    private Game game;
    private ClientBotSettings botSettings;

    @Override
    public int getTheme() {
        return super.getTheme();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String gameStr = getArguments().getString(Constants.GAME_OBJ_KEY);
        game = new Gson().fromJson(gameStr, Game.class);
        botSettings = SharedPreferencesUtils.getInstance().getClientBotSettings();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.gb_referral_challenge_description_bottom_sheet,container,false);
        if(game == null)
            dismiss();
        else {
            howToEarnTv = rootView.findViewById(R.id.gb_how_to_earn_text);
            howToEarnTv.setTextColor(Color.parseColor(botSettings.getBotMainColor()));

            referralChallengeDescriptionTv = rootView.findViewById(R.id.gb_referral_challenge_description);
            referralChallengeDescriptionTv.setText(game.getDescription());
        }
        return rootView;
    }
}
