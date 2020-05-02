package com.gameball.androidx.views.mainContainer;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
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

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.gameball.androidx.R;
import com.gameball.androidx.local.SharedPreferencesUtils;
import com.gameball.androidx.model.response.ClientBotSettings;
import com.gameball.androidx.model.response.Level;
import com.gameball.androidx.model.response.PlayerAttributes;
import com.gameball.androidx.utils.DisplayUtils;
import com.gameball.androidx.utils.ImageDownloader;
import com.gameball.androidx.utils.ProgressBarAnimation;
import com.gameball.androidx.views.GameBallMainActivity;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.Locale;

public class MainContainerFragment extends DialogFragment implements MainContainerContract.View, SwipeRefreshLayout.OnRefreshListener {

    private View rootView;
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
    private ImageButton noInternetConnectionCloseBtn;
    private SwipeRefreshLayout pullToRefresh;
    private AppBarLayout appBarLayout;
    private ConstraintLayout userDataLayout;
    private Toolbar toolbar;
    private ConstraintLayout walletPointsContainer;
    private ConstraintLayout rankPointsContainer;

    private Animation fadeIn;

    TabsAdapter tabsAdapter;
    MainContainerContract.Presenter presenter;
    ClientBotSettings clientBotSettings;
    private float playerProgress;
    private TextView currentFrubiesTitle;
    private TextView currentPointTitle;
    private TextView currentPointRedemptionValue;

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
        btnClose = rootView.findViewById(R.id.gb_btn_close);
        tabs = rootView.findViewById(R.id.gb_tabs);
        viewPager = rootView.findViewById(R.id.gb_view_pager);
        loadingIndicator = rootView.findViewById(R.id.gb_loading_indicator);
        levelLogo = rootView.findViewById(R.id.gb_level_logo);
        levelName = rootView.findViewById(R.id.gb_level_name);
        levelProgress = rootView.findViewById(R.id.gb_level_progress);
        nextLevelTitle = rootView.findViewById(R.id.gb_next_level_title);
        currentFrubiesValue = rootView.findViewById(R.id.gb_rank_points_value);
        currentPointsValue = rootView.findViewById(R.id.gb_wallet_points_value);
        currentFrubiesTitle = rootView.findViewById(R.id.gb_ranke_name);
        currentPointTitle = rootView.findViewById(R.id.gb_wallet_points_name);
        currentPointRedemptionValue = rootView.findViewById(R.id.gb_wallet_point_redemption_value);
        loadingIndicatorBg = rootView.findViewById(R.id.gb_loading_indicator_bg);
        noInternetConnectionLayout = rootView.findViewById(R.id.gb_no_internet_layout);
        noInternetConnectionCloseBtn = rootView.findViewById(R.id.gb_no_internet_btn_close);
        pullToRefresh = rootView.findViewById(R.id.pull_to_refresh);
        appBarLayout = rootView.findViewById(R.id.gb_appbar_layout);
        userDataLayout = rootView.findViewById(R.id.player_details_layout);
        toolbar = rootView.findViewById(R.id.gb_toolbar);
        walletPointsContainer = rootView.findViewById(R.id.gb_wallet_points_layout);
        rankPointsContainer = rootView.findViewById(R.id.gb_rank_points_layout);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset >= 0) {
                    pullToRefresh.setEnabled(true);
                } else {
                    pullToRefresh.setEnabled(false);
                }
            }
        });

        noInternetConnectionCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    private void setupBotSettings() {
        tabs.setSelectedTabIndicatorColor(Color.parseColor(clientBotSettings.getBotMainColor()));
        loadingIndicator.getIndeterminateDrawable().setColorFilter(Color.parseColor(clientBotSettings.getBotMainColor()),
                PorterDuff.Mode.SRC_IN);
        currentFrubiesTitle.setText(clientBotSettings.getRankPointsName());
        currentPointTitle.setText(clientBotSettings.getWalletPointsName());
        pullToRefresh.setColorSchemeColors(Color.parseColor(clientBotSettings.getBotMainColor()));
        userDataLayout.setBackgroundColor(Color.parseColor(clientBotSettings.getBotMainColor()));
        toolbar.setBackgroundColor(Color.parseColor(clientBotSettings.getBotMainColor()));

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
                    if(!clientBotSettings.isSingleTab())
                        tabs.getTabAt(0).setIcon(R.drawable.gb_ic_trophy);
                    else {
                        if (clientBotSettings.isReferralOn())
                            tabs.getTabAt(0).setIcon(R.drawable.gb_ic_referral);
                        else if (clientBotSettings.isEnableLeaderboard())
                            tabs.getTabAt(0).setIcon(R.drawable.gb_ic_leaderboard);
                    }
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
                    if (clientBotSettings.isReferralOn() && clientBotSettings.isEnableLeaderboard())
                        tabs.getTabAt(2).setIcon(R.drawable.gb_ic_leaderboard);
                    else if (clientBotSettings.isEnableNotifications())
                        tabs.getTabAt(2).setIcon(R.drawable.gb_ic_notification);
                    break;
                case 3:
                    tabs.getTabAt(3).setIcon(R.drawable.gb_ic_notification);
                    break;
            }
        }
    }

    @Override
    public void onProfileInfoLoaded(PlayerAttributes playerAttributes, Level nextLevel) {
        SharedPreferencesUtils.getInstance().putPlayerRefferalLink(playerAttributes.getDynamicLink());
        /**
         * the bellow check is made for a certain client which is fayvo.
         * it hides the player data section
         */
        if (!SharedPreferencesUtils.getInstance().getClientId().equals("d58a919179834f1583b66edd1c10f9bd")) {
            if (playerAttributes.getDisplayName() != null && !playerAttributes.getDisplayName().isEmpty())
                fillPlayerData(playerAttributes, nextLevel);
        } else {
            rootView.findViewById(R.id.player_details_layout).setVisibility(View.GONE);
        }
        prepView();
    }

    @Override
    public void showNoInterNetConnection() {
        noInternetConnectionLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void updateBotSettings() {
        clientBotSettings = SharedPreferencesUtils.getInstance().getClientBotSettings();
        ((GameBallMainActivity) getActivity()).updateStatusBarColor();
        setupBotSettings();
    }

    private void fillPlayerData(PlayerAttributes playerAttributes, Level nextLevel) {
        levelName.setText(playerAttributes.getLevel().getName());
        if (playerAttributes.getLevel().getIcon() != null)
            ImageDownloader.downloadImage(getContext(), levelLogo,
                    playerAttributes.getLevel().getIcon().getFileName());

        if (clientBotSettings.isWalletPointsVisible()) {
            walletPointsContainer.setVisibility(View.VISIBLE);
            currentPointsValue.setText(String.format(Locale.getDefault(),
                    "%d", playerAttributes.getAccPoints()));
            if (clientBotSettings.getGuest() != null) {
                Double playerPointActualValue =
                        (clientBotSettings.getGuest().getRedemptionFactor() * playerAttributes.getAccPoints());

                currentPointRedemptionValue.setText(getString(R.string.gb_points_actual_value_text,
                        playerPointActualValue.intValue()));
            } else {
                currentPointTitle.setVisibility(View.GONE);
            }

        } else {
            walletPointsContainer.setVisibility(View.GONE);
        }

        if (clientBotSettings.isRankPointsVisible()) {
            rankPointsContainer.setVisibility(View.VISIBLE);
            currentFrubiesValue.setText(String.format(Locale.getDefault(),
                    "%d", playerAttributes.getAccFrubies()));
        } else {
            rankPointsContainer.setVisibility(View.GONE);
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
