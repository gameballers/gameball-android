package com.gameball.androidx.views.achievements;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gameball.androidx.R;
import com.gameball.androidx.local.SharedPreferencesUtils;
import com.gameball.androidx.model.response.ClientBotSettings;
import com.gameball.androidx.model.response.Game;
import com.gameball.androidx.views.profile.ChallengesAdapter;

import java.util.ArrayList;

public class AchievementsFragment extends Fragment implements AchievemetsContract.View
{
    View rootView;
    private RecyclerView achievementsRecyclerView;
    private ProgressBar loadingIndicator;
    private TextView achievementTitle;

    ChallengesAdapter challengesAdapter;
    AchievemetsContract.Presenter presenter;
    ClientBotSettings clientBotSettings;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        challengesAdapter = new ChallengesAdapter(getContext(), new ArrayList<Game>());
        presenter = new AchievementsPresenter(getContext(), this);
        clientBotSettings = SharedPreferencesUtils.getInstance().getClientBotSettings();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.gb_fragment_achievements, container, false);
        initView();
        setupBotSettings();
        presenter.getAchievements();
        return rootView;
    }

    private void initView() {
        achievementTitle = rootView.findViewById(R.id.gb_achievements_title);
        achievementsRecyclerView = rootView.findViewById(R.id.gb_achievements_recyclerView);
        loadingIndicator = rootView.findViewById(R.id.gb_loading_indicator);
        achievementsRecyclerView.setHasFixedSize(true);
        achievementsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        achievementsRecyclerView.setAdapter(challengesAdapter);
    }

    private void setupBotSettings()
    {
        achievementTitle.setTextColor(Color.parseColor(clientBotSettings.getBotMainColor()));
    }

    @Override
    public void fillAchievements(ArrayList<Game> games)
    {
        challengesAdapter.setmData(games);
        challengesAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLoadingIndicator()
    {
        loadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingIndicator()
    {
        loadingIndicator.setVisibility(View.GONE);
    }
}
