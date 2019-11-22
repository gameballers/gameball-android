package com.gameball.androidx.views.profile;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gameball.androidx.R;
import com.gameball.androidx.local.SharedPreferencesUtils;
import com.gameball.androidx.model.response.ClientBotSettings;
import com.gameball.androidx.model.response.Game;
import com.gameball.androidx.model.response.Mission;
import com.gameball.androidx.views.mainContainer.MainContainerContract;

import java.util.ArrayList;

public class ProfileFragment extends Fragment  implements ProfileContract.View
{
    View rootView;
    private TextView achievementTitle;
    private RecyclerView achievementsRecyclerView;
    private ProgressBar profileLoadingIndicator;
    private View profileLoadingIndicatorBg;
    private RelativeLayout noInternetConnectionLayout;
    private TextView missionsTitle;
    private RecyclerView missionRecyclerView;


    private ChallengesAdapter challengesAdapter;
    private MissionsAdapter missionsAdapter;
    private ProfileContract.Presenter presenter;
    private ClientBotSettings clientBotSettings;
    private Animation fadeIn;
    private Animation zoomInX;

    MainContainerContract.View mainContainerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initComponents();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.gb_fragment_profile, container, false);
        initView();
        setupBotSettings();
        prepView();
        presenter.getWithUnlocks();
        return rootView;
    }

    private void initComponents() {
        challengesAdapter = new ChallengesAdapter(getContext(), new ArrayList<Game>());
        missionsAdapter = new MissionsAdapter(getContext(), new ArrayList<Mission>());
        presenter = new ProfilePresenter(getContext(), this);
        clientBotSettings = SharedPreferencesUtils.getInstance().getClientBotSettings();

        fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        fadeIn.setDuration(500);
        zoomInX = AnimationUtils.loadAnimation(getContext(),R.anim.zoom_in_x_only);
        zoomInX.setDuration(400);
    }

    private void initView() {
        achievementTitle = rootView.findViewById(R.id.gb_achievements_title);
        achievementsRecyclerView = rootView.findViewById(R.id.gb_achievements_recyclerView);
        profileLoadingIndicator = rootView.findViewById(R.id.gb_profile_data_loading_indicator);
        profileLoadingIndicatorBg = rootView.findViewById(R.id.gb_profile_data_loading_indicator_bg);
        noInternetConnectionLayout = rootView.findViewById(R.id.gb_no_internet_layout);
        missionsTitle = rootView.findViewById(R.id.gb_missions_title);
        missionRecyclerView = rootView.findViewById(R.id.gb_missions_recyclerView);

    }

    private void setupBotSettings()
    {
        profileLoadingIndicator.getIndeterminateDrawable().setColorFilter(Color.parseColor(clientBotSettings.getBotMainColor()),
                PorterDuff.Mode.SRC_IN);
        achievementTitle.setTextColor(Color.parseColor(clientBotSettings.getBotMainColor()));
        missionsTitle.setTextColor(Color.parseColor(clientBotSettings.getBotMainColor()));
    }

    private void prepView() {
//        achievementsRecyclerView.setHasFixedSize(true);
        achievementsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        achievementsRecyclerView.setNestedScrollingEnabled(false);
        achievementsRecyclerView.setAdapter(challengesAdapter);

        missionRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        missionRecyclerView.setNestedScrollingEnabled(false);
        missionRecyclerView.setAdapter(missionsAdapter);
    }

    @Override
    public void onWithUnlocksLoaded(ArrayList<Game> games, ArrayList<Mission> missions)
    {
        if(games != null && games.size() > 0) {
            challengesAdapter.setmData(games);
            challengesAdapter.notifyDataSetChanged();
        } else {
            achievementTitle.setVisibility(View.GONE);
            achievementsRecyclerView.setVisibility(View.GONE);

        }

        if (missions != null && !missions.isEmpty()) {
            missionsTitle.setVisibility(View.VISIBLE);
            missionRecyclerView.setVisibility(View.VISIBLE);
            missionsAdapter.setmData(missions);
            missionsAdapter.notifyDataSetChanged();

        } else {
            missionsTitle.setVisibility(View.GONE);
            missionRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showLoadingIndicator()
    {
        profileLoadingIndicator.setVisibility(View.VISIBLE);
        profileLoadingIndicatorBg.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingIndicator()
    {
        profileLoadingIndicator.setVisibility(View.GONE);
        profileLoadingIndicatorBg.setVisibility(View.GONE);
        Animation fadeOut = AnimationUtils.loadAnimation(getContext(),
                android.R.anim.fade_out);
        fadeOut.setDuration(100);
        profileLoadingIndicatorBg.setAnimation(fadeOut);
    }

    @Override
    public void showNoInternetConnectionLayout() {
        noInternetConnectionLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStop()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH)
        {
            PowerManager pm = (PowerManager) (getContext()).getSystemService(Context.POWER_SERVICE);
            if((pm.isInteractive()))
            {
                presenter.unsubscribe();
            }
        }
        super.onStop();
    }
}
