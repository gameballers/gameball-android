package com.gameball.androidx.views.referral;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gameball.androidx.R;
import com.gameball.androidx.local.SharedPreferencesUtils;
import com.gameball.androidx.model.response.ClientBotSettings;
import com.gameball.androidx.model.response.Game;
import com.gameball.androidx.utils.DisplayUtils;
import com.gameball.androidx.utils.ImageDownloader;

import java.util.ArrayList;
import java.util.Locale;

public class ReferralFragment extends Fragment implements ReferralContract.View, View.OnClickListener {
    private View rootView;
    private ClientBotSettings clientBotSettings;

    private TextView shareLinkBtn;
    private TextView referralLink;
    private RecyclerView referralChallengeLs;
    private TextView referralHeadline;
    private TextView referralText;
    private TextView friendsReferralCountTv;
    private ImageView referralIcon;

    private ReferralChallengesAdapter referralChallengesAdapter;
    ReferralContract.Presenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.gb_fragment_referral, container, false);
        initComponents();
        initView();
        setupBotSettings();
        prepView();
        presenter.getReferralChallenges();
        referralLink.setText(SharedPreferencesUtils.getInstance().getPlayerReferralLink());
        return rootView;
    }

    private void initComponents() {
        presenter = new ReferralPresenter(this);
        clientBotSettings = SharedPreferencesUtils.getInstance().getClientBotSettings();
        referralChallengesAdapter = new ReferralChallengesAdapter(getContext(), getFragmentManager(), new ArrayList<Game>());
    }

    private void setupBotSettings() {
        referralHeadline.setTextColor(Color.parseColor(clientBotSettings.getBotMainColor()));
        shareLinkBtn.setBackgroundTintList(ColorStateList
                .valueOf(Color.parseColor(clientBotSettings.getBotMainColor())));

    }

    private void initView() {
        shareLinkBtn = rootView.findViewById(R.id.gb_share_link_btn);
        referralLink = rootView.findViewById(R.id.gb_referral_link);
        referralHeadline = rootView.findViewById(R.id.gb_referral_headline);
        referralText = rootView.findViewById(R.id.gb_referral_text);
        referralChallengeLs = rootView.findViewById(R.id.gb_referral_challenge_ls);
        referralIcon = rootView.findViewById(R.id.gb_referral_icon);
        friendsReferralCountTv = rootView.findViewById(R.id.gb_friends_referral_count_tv);

        referralChallengeLs.setLayoutManager(new LinearLayoutManager(getContext()));
        referralChallengeLs.setNestedScrollingEnabled(true);
        referralChallengeLs.setHasFixedSize(true);
        referralChallengeLs.setAdapter(referralChallengesAdapter);

        if (DisplayUtils.isRTL(Locale.getDefault())) {
            shareLinkBtn.setBackgroundResource(R.drawable.gb_bg_primary_left_corener_round);
            referralLink.setBackgroundResource(R.drawable.gb_bg_grey_right_corener_round);
        }

        shareLinkBtn.setOnClickListener(this);
    }

    private void prepView() {
        if (clientBotSettings.getReferralIcon() != null) {
            ImageDownloader.downloadImage(getContext(), referralIcon, clientBotSettings.getReferralIcon());
        }
        referralHeadline.setText(clientBotSettings.getReferralHeadLine());
        referralText.setText(clientBotSettings.getReferralText());
    }

    @Override
    public void onReferralChallengesFiltered(ArrayList<Game> games, int friendsReferral) {
        friendsReferralCountTv.setText(getString(R.string.friends_referral_count, friendsReferral));
        referralChallengesAdapter.setmData(games);
        referralChallengesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        if (referralLink.getText().toString().isEmpty())
            return;

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, referralLink.getText().toString());
        startActivity(Intent.createChooser(shareIntent, "Share link using"));


    }
}
