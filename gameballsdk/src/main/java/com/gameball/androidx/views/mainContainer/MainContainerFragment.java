package com.gameball.androidx.views.mainContainer;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;

import androidx.annotation.Nullable;

import com.google.android.material.tabs.TabLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gameball.androidx.R;
import com.gameball.androidx.local.SharedPreferencesUtils;
import com.gameball.androidx.model.response.ClientBotSettings;
import com.gameball.androidx.model.response.Level;
import com.gameball.androidx.model.response.PlayerAttributes;
import com.gameball.androidx.utils.DisplayUtils;
import com.gameball.androidx.utils.ImageDownloader;
import com.gameball.androidx.utils.ProgressBarAnimation;

import java.util.Locale;

public class MainContainerFragment extends DialogFragment implements MainContainerContract.View, SwipeRefreshLayout.OnRefreshListener {

    private View rootView;
    private TextView txtPlayerName;
    private ImageButton btnClose;
    private TabLayout tabs;
    private ViewPager viewPager;
    ProgressBar loadingIndicator;
    private ImageView levelLogo;
    private TextView levelName;
    private ProgressBar levelProgress;
    private TextView nextLevelTitle;
    private TextView currentFrubiesValue;
    private TextView currentPointsValue;
    private View loadingIndicatorBg;
    private RelativeLayout noInternetConnectionLayout;
    private SwipeRefreshLayout pullToRefresh;
    private TextView singlePoints;

    private ConstraintLayout walletRankPointsContainer;

    private Animation fadeIn;

    TabsAdapter tabsAdapter;
    MainContainerContract.Presenter presenter;
    ClientBotSettings clientBotSettings;
    private float playerProgress;
    private TextView currentFrubiesTitle;
    private TextView currentPointTitle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initComponents();
    }

    private void initComponents() {
        clientBotSettings = SharedPreferencesUtils.getInstance().getClientBotSettings();
        tabsAdapter = new TabsAdapter(getChildFragmentManager(), clientBotSettings);
        presenter = new MainContainerPresenter(this);

        fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        fadeIn.setDuration(700);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.gb_fragment_main_container, container, false);
        initView();
        setupBotSettings();
        presenter.getPlayerInfo();
        return rootView;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {

        // the content
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        // creating the fullscreen dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }

        return dialog;
    }

    /*@Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            Window window = dialog.getWindow();
            if (window != null) window.setLayout(width, height);
        }
    }*/

    private void initView() {
        txtPlayerName = rootView.findViewById(R.id.txt_player_name);
        btnClose = rootView.findViewById(R.id.btn_close);
        tabs = rootView.findViewById(R.id.tabs);
        viewPager = rootView.findViewById(R.id.view_pager);
        loadingIndicator = rootView.findViewById(R.id.loading_indicator);
        levelLogo = rootView.findViewById(R.id.level_logo);
        levelName = rootView.findViewById(R.id.level_name);
        levelProgress = rootView.findViewById(R.id.level_progress);
        nextLevelTitle = rootView.findViewById(R.id.next_level_title);
        currentFrubiesValue = rootView.findViewById(R.id.current_frubies_value);
        currentPointsValue = rootView.findViewById(R.id.current_points_value);
        currentFrubiesTitle = rootView.findViewById(R.id.frubies_title);
        currentPointTitle = rootView.findViewById(R.id.points_title);
        loadingIndicatorBg = rootView.findViewById(R.id.loading_indicator_bg);
        noInternetConnectionLayout = rootView.findViewById(R.id.no_internet_layout);
        pullToRefresh = rootView.findViewById(R.id.pull_to_refresh);
        singlePoints = rootView.findViewById(R.id.single_points);
        walletRankPointsContainer = rootView.findViewById(R.id.frubies_and_points_container);
    }

    private void setupBotSettings() {
        tabs.setSelectedTabIndicatorColor(Color.parseColor(clientBotSettings.getBotMainColor()));
        loadingIndicator.getIndeterminateDrawable().setColorFilter(Color.parseColor(clientBotSettings.getBotMainColor()),
                PorterDuff.Mode.SRC_IN);
        LayerDrawable progressDrawable = (LayerDrawable) levelProgress.getProgressDrawable();
        progressDrawable.setColorFilter(Color.parseColor(clientBotSettings.getBotMainColor()),
                PorterDuff.Mode.SRC_IN);
        currentFrubiesTitle.setText(clientBotSettings.getRankPointsName());
        currentPointTitle.setText(clientBotSettings.getWalletPointsName());
        pullToRefresh.setColorSchemeColors(Color.parseColor(clientBotSettings.getBotMainColor()));
        singlePoints.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(clientBotSettings.getBotMainColor())));
    }

    private void prepView() {
        pullToRefresh.setOnRefreshListener(this);
        viewPager.setAdapter(tabsAdapter);
        tabs.setupWithViewPager(viewPager);

        tabs.setSelectedTabIndicatorHeight((int) DisplayUtils.convertDpToPixel(2));
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        setupTabsIcons();
        tabs.getTabAt(tabs.getSelectedTabPosition()).getIcon().
                setColorFilter(Color.parseColor(clientBotSettings.getBotMainColor()), PorterDuff.Mode.SRC_IN);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor(clientBotSettings.getBotMainColor()), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#adadad"), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            tabs.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
    }

    private void setupTabsIcons() {
        for (int i = 0; i < tabs.getTabCount(); i++) {
            switch (i) {
                case 0:
                    tabs.getTabAt(0).setIcon(R.drawable.gb_ic_trophy);
                    break;
                case 1:
                    if (clientBotSettings.isReferralOn())
                        tabs.getTabAt(1).setIcon(R.drawable.gb_ic_referral);
                    else if (clientBotSettings.isEnableLeaderboard())
                        tabs.getTabAt(1).setIcon(R.drawable.gb_ic_leaderboard);
                    else if (clientBotSettings.isEnableNotifications())
                        tabs.getTabAt(1).setIcon(R.drawable.gb_ic_notification);
                    break;
                case 2:
                    if (clientBotSettings.isEnableLeaderboard())
                        tabs.getTabAt(2).setIcon(R.drawable.gb_ic_leaderboard);
                    else if (clientBotSettings.isEnableNotifications())
                        tabs.getTabAt(2).setIcon(R.drawable.gb_ic_notification);
                case 3:
                    tabs.getTabAt(3).setIcon(R.drawable.gb_ic_notification);
            }
        }
    }

    @Override
    public void onProfileInfoLoaded(PlayerAttributes playerAttributes, Level nextLevel) {
        SharedPreferencesUtils.getInstance().putPlayerRefferalLink(playerAttributes.getDynamicLink());

        if (playerAttributes.getDisplayName() != null && !playerAttributes.getDisplayName().isEmpty())
            txtPlayerName.setText(playerAttributes.getDisplayName());

        fillPlayerData(playerAttributes, nextLevel);
        prepView();
    }

    @Override
    public void showNoInterNetConnection() {
        noInternetConnectionLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void updateBotSettings() {
        clientBotSettings = SharedPreferencesUtils.getInstance().getClientBotSettings();
    }

    private void fillPlayerData(PlayerAttributes playerAttributes, Level nextLevel) {
        levelName.setText(playerAttributes.getLevel().getName());
        if (playerAttributes.getLevel().getIcon() != null)
            ImageDownloader.downloadImage(getContext(), levelLogo,
                    playerAttributes.getLevel().getIcon().getFileName());

        if (clientBotSettings.isWalletPointsVisible() && clientBotSettings.isRankPointsVisible()) {

            walletRankPointsContainer.setVisibility(View.VISIBLE);
            singlePoints.setVisibility(View.GONE);
            currentPointsValue.setText(String.format(Locale.getDefault(),
                    "%d", playerAttributes.getAccPoints()));
            currentFrubiesValue.setText(String.format(Locale.getDefault(),
                    "%d", playerAttributes.getAccFrubies()));
        } else {
            walletRankPointsContainer.setVisibility(View.GONE);

            if (clientBotSettings.isWalletPointsVisible()) {
                singlePoints.setVisibility(View.VISIBLE);
                singlePoints.setText(String.format(Locale.getDefault(),"%d",playerAttributes.getAccPoints()));
                singlePoints.setCompoundDrawablesRelativeWithIntrinsicBounds( R.drawable.gb_ic_points, 0,0,0);
            } else if (clientBotSettings.isRankPointsVisible()) {
                singlePoints.setVisibility(View.VISIBLE);
                singlePoints.setText(String.format(Locale.getDefault(), "%d",playerAttributes.getAccFrubies()));
                singlePoints.setCompoundDrawablesRelativeWithIntrinsicBounds( R.drawable.gb_ic_diamon_outline, 0,0,0);
            } else {
                singlePoints.setVisibility(View.GONE);
            }
        }

        if (nextLevel != null) {
            nextLevelTitle.setText(String.format(Locale.getDefault(),
                    "%s %d", getString(R.string.next_level_at), nextLevel.getLevelFrubies()));
            playerProgress = (playerAttributes.getAccFrubies() * 100) / nextLevel.getLevelFrubies();
        } else {
            nextLevelTitle.setVisibility(View.GONE);
            levelProgress.setVisibility(View.GONE);
            nextLevelTitle.setVisibility(View.GONE);

        }

        applyAnimation();
    }

    private void applyAnimation() {
        ProgressBarAnimation progressBarAnimation = new ProgressBarAnimation(levelProgress, 0,
                playerProgress);
        progressBarAnimation.setDuration(700);
        progressBarAnimation.setFillAfter(true);
        if (nextLevelTitle.getVisibility() == View.GONE)
            levelProgress.setVisibility(View.GONE);
        else
            levelProgress.startAnimation(progressBarAnimation);

        levelName.startAnimation(fadeIn);
        currentFrubiesValue.startAnimation(fadeIn);
        currentPointsValue.startAnimation(fadeIn);
        currentFrubiesTitle.startAnimation(fadeIn);
        currentPointTitle.startAnimation(fadeIn);
    }

    @Override
    public void showLoadingIndicator() {
        noInternetConnectionLayout.setVisibility(View.GONE);
        loadingIndicatorBg.setVisibility(View.VISIBLE);
        loadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingIndicator() {
        loadingIndicatorBg.setVisibility(View.GONE);
        loadingIndicator.setVisibility(View.GONE);
        pullToRefresh.setRefreshing(false);
    }

    @Override
    public void onStop() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            PowerManager pm = (PowerManager) (getContext()).getSystemService(Context.POWER_SERVICE);
            if ((pm.isInteractive())) {
                presenter.unsubscribe();
            }
        }
        super.onStop();
    }

    @Override
    public void onRefresh() {
        clearTabItems();
        presenter.getPlayerInfo();
    }

    private void clearTabItems() {
        tabs.clearOnTabSelectedListeners();
        tabs.removeAllTabs();
        viewPager.removeAllViews();
    }
}
