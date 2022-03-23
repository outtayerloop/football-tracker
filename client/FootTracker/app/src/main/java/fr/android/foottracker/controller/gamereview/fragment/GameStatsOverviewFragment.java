package fr.android.foottracker.controller.gamereview.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Objects;

import fr.android.foottracker.R;
import fr.android.foottracker.model.entities.data.GameData;
import fr.android.foottracker.model.view.GameStatsViewModel;

public class GameStatsOverviewFragment extends Fragment {

    private GameStatsViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_stats_overview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setElevation(0f);
        viewModel = new ViewModelProvider(requireActivity()).get(GameStatsViewModel.class);
        initView(view);
    }

    private void initView(@NonNull View view){
        final GameData game = viewModel.getGame();
        initTeams(game, view);
        initScores(game, view);
        initPossessionStats(game, view);
        initPlayStyleStats(game, view);
        initOutStats(game, view);
        initCardStats(game, view);
    }

    private void initTeams(@NonNull GameData game, @NonNull View view){
        final String upperCasedTrackedTeamName = game.getTrackedTeam().getName().toUpperCase();
        final String upperCasedOpponentTeamName = game.getOpponentTeam().getName().toUpperCase();
        final TextView teams = view.findViewById(R.id.game_stats_overview_teams);
        teams.setText(String.format("%s - %s", upperCasedTrackedTeamName, upperCasedOpponentTeamName));
        final TextView trackedTeam = view.findViewById(R.id.game_overview_tracked_team);
        trackedTeam.setText(upperCasedTrackedTeamName);
        final TextView opponentTeam = view.findViewById(R.id.game_overview_opponent_team);
        opponentTeam.setText(upperCasedOpponentTeamName);
    }

    private void initScores(@NonNull GameData game, @NonNull View view){
        final int trackedTeamScore = game.getTrackedTeamScore();
        final int opponentTeamScore = game.getOpponentScore();
        final TextView scores = view.findViewById(R.id.game_stats_overview_scores);
        scores.setText(String.format("%s - %s", trackedTeamScore, opponentTeamScore));
    }

    private void initPossessionStats(@NonNull GameData game, @NonNull View view){
        initTrackedTeamPossession(game, view);
        initOpponentTeamPossession(game, view);
    }

    @SuppressLint("SetTextI18n")
    private void initTrackedTeamPossession(@NonNull GameData game, @NonNull View view){
        final int trackedTeamPossessionPercentage = game.getTrackedTeamPossessionPercentage();
        final TextView trackedTeamPossession = view.findViewById(R.id.game_overview_tracked_team_possession);
        trackedTeamPossession.setText(trackedTeamPossessionPercentage + "%");
    }

    @SuppressLint("SetTextI18n")
    private void initOpponentTeamPossession(@NonNull GameData game, @NonNull View view){
        final int opponentTeamPossessionPercentage = game.getOpponentTeamPossessionPercentage();
        final TextView opponentTeamPossession = view.findViewById(R.id.game_overview_opponent_team_possession);
        opponentTeamPossession.setText(opponentTeamPossessionPercentage + "%");
    }

    private void initPlayStyleStats(@NonNull GameData game, @NonNull View view){
        initStopCount(game, view);
        initOccasionCount(game, view);
        initOffsideCount(game, view);
        initFaultCount(game, view);
        initPenaltyCount(game, view);
    }

    @SuppressLint("SetTextI18n")
    private void initStopCount(@NonNull GameData game, @NonNull View view) {
        final TextView stopCount =  view.findViewById(R.id.game_overview_stop_count);
        stopCount.setText(Integer.toString(game.getStopCount()));
    }

    @SuppressLint("SetTextI18n")
    private void initOccasionCount(@NonNull GameData game, @NonNull View view){
        final TextView occasionCount =  view.findViewById(R.id.game_overview_occasion_count);
        occasionCount.setText(Integer.toString(game.getOccasionCount()));
    }

    @SuppressLint("SetTextI18n")
    private void initOffsideCount(@NonNull GameData game, @NonNull View view){
        final TextView offsideCount =  view.findViewById(R.id.game_overview_offside_count);
        offsideCount.setText(Integer.toString(game.getOffsideCount()));
    }

    @SuppressLint("SetTextI18n")
    private void initFaultCount(@NonNull GameData game, @NonNull View view){
        final TextView faultCount =  view.findViewById(R.id.game_overview_fault_count);
        faultCount.setText(Integer.toString(game.getFaultCount()));
    }

    @SuppressLint("SetTextI18n")
    private void initPenaltyCount(@NonNull GameData game, @NonNull View view){
        final TextView penaltyCount =  view.findViewById(R.id.game_overview_penalty_count);
        penaltyCount.setText(Integer.toString(game.getPenaltyCount()));
    }

    private void initOutStats(@NonNull GameData game, @NonNull View view){
        initClassicOutCount(game, view);
        initFreeKickCount(game, view);
        initCornerCount(game, view);
    }

    @SuppressLint("SetTextI18n")
    private void initClassicOutCount(@NonNull GameData game, @NonNull View view){
        final TextView classicOutCount = view.findViewById(R.id.game_overview_classic_out_count);
        classicOutCount.setText(Integer.toString(game.getOutCount()));
    }

    @SuppressLint("SetTextI18n")
    private void initFreeKickCount(@NonNull GameData game, @NonNull View view){
        final TextView freeKickCount = view.findViewById(R.id.game_overview_freekick_count);
        freeKickCount.setText(Integer.toString(game.getFreeKickCount()));
    }

    @SuppressLint("SetTextI18n")
    private void initCornerCount(@NonNull GameData game, @NonNull View view){
        final TextView cornerCount = view.findViewById(R.id.game_overview_corner_count);
        cornerCount.setText(Integer.toString(game.getCornerCount()));
    }

    private void initCardStats(@NonNull GameData game, @NonNull View view){
        initYellowCard(game, view);
        initRedCard(game, view);
    }

    @SuppressLint("SetTextI18n")
    private void initYellowCard(@NonNull GameData game, @NonNull View view){
        final TextView yellowCard = view.findViewById(R.id.game_overview_yellow_card_count);
        yellowCard.setText(Integer.toString(game.getTrackedTeamGameYellowCardCount()));
    }

    @SuppressLint("SetTextI18n")
    private void initRedCard(@NonNull GameData game, @NonNull View view){
        final TextView redCard = view.findViewById(R.id.game_overview_red_card_count);
        redCard.setText(Integer.toString(game.getTrackedTeamGameRedCardCount()));
    }
}