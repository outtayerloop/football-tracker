package fr.android.foottracker.controller.gamereview.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import fr.android.foottracker.R;
import fr.android.foottracker.model.entities.data.CardData;
import fr.android.foottracker.model.entities.data.GoalData;
import fr.android.foottracker.model.entities.data.PlayerData;
import fr.android.foottracker.model.view.GameStatsViewModel;
import fr.android.foottracker.utils.DensityUtil;

public class GameStatsTimelineFragment extends Fragment {

    private GameStatsViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_stats_timeline, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setElevation(0f);
        viewModel = new ViewModelProvider(requireActivity()).get(GameStatsViewModel.class);
        initView(view);
    }

    private void initView(@NonNull View view){
        final LinearLayout layout = view.findViewById(R.id.game_stats_timeline_layout);
        final List<PlayerData> trackedTeamPlayers = viewModel.getGame().getTrackedTeam().getPlayers();
        initGoalRows(layout, trackedTeamPlayers);
        initCardRows(layout, trackedTeamPlayers);
    }

    private void initGoalRows(@NonNull LinearLayout layout, @NonNull List<PlayerData> trackedTeamPlayers){
        trackedTeamPlayers.forEach(player -> {
            List<GoalData> playerGoals = player.getGoals();
            if(playerGoals != null){
                playerGoals = playerGoals.stream().filter(goal -> goal.getGameId() == viewModel.getGame().getId()).collect(Collectors.toList());
                for(int i = 0; i < playerGoals.size(); ++i){
                    final String teamName = viewModel.getGame().getTrackedTeam().getName();
                    addRowToLayout(layout, player.getName(), teamName, playerGoals.get(i).getMoment(), (i % 2 != 0));
                }
            }
        });
        handleEmptyGoals(layout);
    }

    private void initCardRows(@NonNull LinearLayout layout, @NonNull List<PlayerData> trackedTeamPlayers){
        addCardTitleRow(layout);
        addCardColumnTitleRow(layout);
        trackedTeamPlayers.forEach(player -> {
            List<CardData> playerCards = player.getCards();
            if(playerCards != null){
                playerCards = playerCards.stream().filter(card -> card.getGameId() == viewModel.getGame().getId()).collect(Collectors.toList());
                for(int i = 0; i < playerCards.size(); ++i){
                    final String teamName = viewModel.getGame().getTrackedTeam().getName();
                    addRowToLayout(layout, player.getName(), teamName, playerCards.get(i).getMoment(), (i % 2 != 0));
                }
            }
        });
        handleEmptyCards(layout);
    }

    private void addCardTitleRow(@NonNull LinearLayout layout){
        final TextView titleRow = new TextView(requireContext());
        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        titleRow.setLayoutParams(layoutParams);
        titleRow.setBackground(ResourcesCompat.getDrawable(getResources(), R.color.design_default_color_primary, requireActivity().getTheme()));
        setCardTitleRowPadding(titleRow);
        setCardTitleRowText(titleRow);
        layout.addView(titleRow);
    }

    private void addCardColumnTitleRow(@NonNull LinearLayout layout){
        final LinearLayout row = new LinearLayout(requireContext());
        setRowLayoutParams(row);
        row.setOrientation(LinearLayout.HORIZONTAL);
        final TextView playerCell = getCell(getResources().getString(R.string.game_timeline_player), true);
        final TextView teamCell = getCell(getResources().getString(R.string.game_timeline_team), true);
        final TextView momentCell = getCell(getResources().getString(R.string.game_timeline_moment), true);
        setCardTitleCellsText(playerCell, teamCell, momentCell);
        addCellsToRow(row, playerCell, teamCell, momentCell);
        layout.addView(row);
    }

    private void setCardTitleCellsText(TextView playerCell, TextView teamCell, TextView momentCell) {
        playerCell.setTextColor(ResourcesCompat.getColor(getResources(), R.color.design_default_color_primary, requireActivity().getTheme()));
        teamCell.setTextColor(ResourcesCompat.getColor(getResources(), R.color.design_default_color_primary, requireActivity().getTheme()));
        momentCell.setTextColor(ResourcesCompat.getColor(getResources(), R.color.design_default_color_primary, requireActivity().getTheme()));
        playerCell.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        teamCell.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        momentCell.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    }

    private void setCardTitleRowPadding(TextView titleRow) {
        final int titlePadding = getDefaultPadding();
        titleRow.setPadding(0, titlePadding, 0, titlePadding);
    }

    private void setCardTitleRowText(TextView titleRow) {
        titleRow.setTextColor(ResourcesCompat.getColor(getResources(), R.color.white, requireActivity().getTheme()));
        titleRow.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        titleRow.setGravity(Gravity.CENTER);
        titleRow.setTextSize(18f);
        titleRow.setText(getResources().getString(R.string.card_timeline_title));
    }

    private void addRowToLayout(@NonNull LinearLayout layout, @NonNull String leftCellContent, @NonNull String midCellContent, @NonNull String rightCellContent, boolean isColouredLine){
        final LinearLayout row = new LinearLayout(requireContext());
        setRowLayoutParams(row);
        row.setOrientation(LinearLayout.HORIZONTAL);
        final TextView leftCell = getCell(leftCellContent, isColouredLine);
        final TextView midCell = getCell(midCellContent, isColouredLine);
        final TextView rightCell = getCell(rightCellContent, isColouredLine);
        addCellsToRow(row, leftCell, midCell, rightCell);
        layout.addView(row);
    }

    @NonNull
    private TextView getCell(@NonNull String leftCellContent, boolean isColouredLine) {
        final TextView leftCell = getTimelineRowCell(isColouredLine);
        leftCell.setText(leftCellContent);
        return leftCell;
    }

    private void setRowLayoutParams(@NonNull LinearLayout row) {
        row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        final float WEIGHT_SUM = 3f; // Car 3 cellules dans une ligne de stats de buts/cartons.
        row.setWeightSum(WEIGHT_SUM);
    }

    private TextView getTimelineRowCell(boolean isColouredLine){
        final TextView rowCell = new TextView(requireContext());
        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
        rowCell.setLayoutParams(layoutParams);
        layoutParams.weight = 1f;
        setRowCellBackground(isColouredLine, rowCell);
        rowCell.setGravity(Gravity.CENTER);
        setRowCellPadding(rowCell);
        rowCell.setTextSize(18f);
        rowCell.setTextColor(ResourcesCompat.getColor(getResources(), R.color.black, requireActivity().getTheme()));
        return rowCell;
    }

    private void setRowCellBackground(boolean isColouredLine, @NonNull TextView rowCell) {
        int backgroundColor = getColorToApplyToBackGround(isColouredLine);
        rowCell.setBackgroundColor(backgroundColor);
    }

    private int getColorToApplyToBackGround(boolean isColouredLine){
        return isColouredLine
                ? Color.parseColor("#F0F0F0")
                : Color.parseColor("#FFFFFF");
    }

    private void setRowCellPadding(@NonNull TextView rowCell) {
        final int pxPadding = getDefaultPadding();
        rowCell.setPadding(pxPadding, pxPadding, pxPadding, pxPadding);
    }

    private int getDefaultPadding(){
        final float DP_PADDING = 8f;
        return DensityUtil.fromDpToPx(DP_PADDING, getResources());
    }

    private void addCellsToRow(@NonNull LinearLayout row, @NonNull TextView leftCell, @NonNull TextView midCell, @NonNull TextView rightCell){
        row.addView(leftCell);
        row.addView(midCell);
        row.addView(rightCell);
    }

    private void handleEmptyGoals(@NonNull LinearLayout layout){
        if(viewModel.getGame().hasNoGoalsScoredByTrackedTeam()){
            final String emptyGoalsMessage = getResources().getString(R.string.empty_goals_message);
            addMessageRow(layout, emptyGoalsMessage);
        }
    }

    private void handleEmptyCards(@NonNull LinearLayout layout){
        if(viewModel.getGame().hasNoCardsFromTrackedTeam()){
            final String emptyGoalsMessage = getResources().getString(R.string.empty_cards_message);
            addMessageRow(layout, emptyGoalsMessage);
        }
    }

    private void addMessageRow(@NonNull LinearLayout layout, String message){
        final TextView messageRow = getCell(message, false);
        messageRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(messageRow);
    }
}
