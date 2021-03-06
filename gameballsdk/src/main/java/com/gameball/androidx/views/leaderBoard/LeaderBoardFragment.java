package com.gameball.androidx.views.leaderBoard;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gameball.androidx.R;
import com.gameball.androidx.local.SharedPreferencesUtils;
import com.gameball.androidx.model.response.ClientBotSettings;
import com.gameball.androidx.model.response.LeaderBoardResponse;
import com.gameball.androidx.model.response.PlayerAttributes;
import com.gameball.androidx.model.response.PlayerRank;

import java.util.ArrayList;
import java.util.Locale;

public class LeaderBoardFragment extends Fragment implements LeaderBoardContract.View {
    View rootView;
    private TextView filerBtn;
    private TextView leaderTitle;
    private TextView playerRankTv;
    private RecyclerView leaderboardRecyclerview;
    private ProgressBar loadingIndicator;
    private RelativeLayout noInternetLayout;
    private ImageView leaderBoardEmptyIv;
    private TextView leaderBoardEmptyTv;
    private TextView leaderBoardFilterBtn;

    private PopupMenu filterMenu;

    LeaderBoardAdapter leaderBoardAdapter;
    LeaderBoardContract.Presenter presenter;
    ClientBotSettings clientBotSettings;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.gb_fragment_leader_board, container, false);
        initComponents();
        initView();
        setupBotSettings();
        prepView();
        presenter.getLeaderBoard(LeaderBoardResponse.TODAY);
        return rootView;
    }

    private void initComponents() {
        presenter = new LeaderBoardPresenter(getContext(), this);
        leaderBoardAdapter = new LeaderBoardAdapter(getContext(), new ArrayList<PlayerAttributes>());
        clientBotSettings = SharedPreferencesUtils.getInstance().getClientBotSettings();
    }

    private void initView() {
        filerBtn = rootView.findViewById(R.id.gb_leaderboard_filter_btn);
        leaderboardRecyclerview = rootView.findViewById(R.id.gb_leaderboard_recyclerview);
        loadingIndicator = rootView.findViewById(R.id.gb_loading_indicator);
        leaderTitle = rootView.findViewById(R.id.gb_leaderboard_title);
        playerRankTv = rootView.findViewById(R.id.player_rank_value);
        noInternetLayout = rootView.findViewById(R.id.gb_no_internet_layout);
        leaderBoardEmptyIv = rootView.findViewById(R.id.leaderboard_empty_state_iv);
        leaderBoardEmptyTv = rootView.findViewById(R.id.leaderboard_empty_state_tv);
        leaderBoardFilterBtn = rootView.findViewById(R.id.gb_leaderboard_filter_btn);

        filterMenu = new PopupMenu(getContext(), leaderBoardFilterBtn);
        MenuInflater menuInflater = filterMenu.getMenuInflater();
        menuInflater.inflate(R.menu.leaderboard_filter_menu, filterMenu.getMenu());

        filterMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                leaderBoardAdapter.setmData(new ArrayList<PlayerAttributes>());
                leaderBoardAdapter.notifyDataSetChanged();
                int itemId = menuItem.getItemId();
                if (itemId == R.id.today_filter) {
                    presenter.getLeaderBoard(LeaderBoardResponse.TODAY);
                } else if (itemId == R.id.yesterday_filter) {
                    presenter.getLeaderBoard(LeaderBoardResponse.YESTERDAY);
                } else if (itemId == R.id.this_week_filter) {
                    presenter.getLeaderBoard(LeaderBoardResponse.THIS_WEEK);
                } else if (itemId == R.id.last_week_filter) {
                    presenter.getLeaderBoard(LeaderBoardResponse.LAST_WEEK);
                } else if (itemId == R.id.this_month_filter) {
                    presenter.getLeaderBoard(LeaderBoardResponse.THIS_MONTH);
                } else if (itemId == R.id.last_month_filter) {
                    presenter.getLeaderBoard(LeaderBoardResponse.LAST_MONTH);
                } else if (itemId == R.id.this_year_filter) {
                    presenter.getLeaderBoard(LeaderBoardResponse.THIS_YEAR);
                } else if (itemId == R.id.all_filter) {
                    presenter.getLeaderBoard(LeaderBoardResponse.ALL);
                }

                leaderBoardFilterBtn.setText(menuItem.getTitle());
                return true;
            }
        });
    }

    private void setupBotSettings() {
        leaderTitle.setTextColor(Color.parseColor(clientBotSettings.getBotMainColor()));
        loadingIndicator.getIndeterminateDrawable().setColorFilter(Color.parseColor(clientBotSettings.getBotMainColor()),
                PorterDuff.Mode.SRC_IN);
    }

    private void prepView() {
        leaderboardRecyclerview.setHasFixedSize(true);
        leaderboardRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        leaderboardRecyclerview.setAdapter(leaderBoardAdapter);
        leaderBoardFilterBtn.setText("Today");
        leaderBoardFilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterMenu.show();
            }
        });
    }

    @Override
    public void fillLeaderBoard(LeaderBoardResponse leaderBoardResponse) {
        ArrayList<PlayerAttributes> leaderBoard = leaderBoardResponse.getPlayerBot();
        PlayerRank playerRank = leaderBoardResponse.getPlayerRank();
        if (leaderBoard.size() > 0) {
            leaderBoardAdapter.setmData(leaderBoard);
            leaderBoardAdapter.notifyDataSetChanged();

            leaderBoardEmptyTv.setVisibility(View.GONE);
            leaderBoardEmptyIv.setVisibility(View.GONE);
        } else {
            leaderBoardAdapter.setmData(leaderBoard);
            leaderBoardAdapter.notifyDataSetChanged();

            leaderBoardEmptyTv.setVisibility(View.VISIBLE);
            leaderBoardEmptyIv.setVisibility(View.VISIBLE);
        }

        playerRankTv.setText(String.format(Locale.getDefault(),
                "%d/%d", playerRank.getRowOrder(), playerRank.getPlayersCount()));
    }

    @Override
    public void showLoadingIndicator() {
        loadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingIndicator() {
        loadingIndicator.setVisibility(View.GONE);
    }

    @Override
    public void showNoInternetLayout() {
        noInternetLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStop() {
        PowerManager pm = (PowerManager) (getContext()).getSystemService(Context.POWER_SERVICE);
        if ((pm.isInteractive())) {
            presenter.unsubscribe();
        }

        super.onStop();
    }
}
