package fr.android.foottracker.controller.gamereview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import fr.android.foottracker.R;
import fr.android.foottracker.app.FootTrackerAppContainer;
import fr.android.foottracker.app.FootTrackerApplication;
import fr.android.foottracker.controller.home.MainActivity;
import fr.android.foottracker.model.database.DbHelper;
import fr.android.foottracker.model.entities.data.GameData;
import fr.android.foottracker.model.repositories.common.GenericLocalDataSource;
import fr.android.foottracker.model.repositories.gamereview.GameReviewRemoteDataSource;
import fr.android.foottracker.model.view.GameReviewViewModel;
import fr.android.foottracker.utils.ActionBarUtil;
import fr.android.foottracker.utils.DataSourceUtil;
import fr.android.foottracker.utils.DateTimeUtil;
import fr.android.foottracker.utils.DensityUtil;
import fr.android.foottracker.utils.DeviceKeyboardUtil;
import fr.android.foottracker.utils.JsonUtil;
import fr.android.foottracker.utils.LocaleUtil;

public class GameReviewActivity extends AppCompatActivity {

    public static final String GAME_EXTRA_KEY = "GAME_EXTRA";

    private FootTrackerAppContainer appContainer;
    private GameReviewViewModel viewModel;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBarUtil.setToolBar(getSupportActionBar());
        appContainer = ((FootTrackerApplication) getApplication()).getAppContainer();
        viewModel = new ViewModelProvider(this).get(GameReviewViewModel.class);
        LocaleUtil.initLocale(this);
    }

    @Override
    public void onDestroy() {
        db.close();
        super.onDestroy();
    }

    /**
     * Déclenché lorsque le menu est créé. On remplace celui par défaut par le nôtre (R.menu.menu_game_form).
     * @param menu Menu qu'on veut modifier.
     * @return true pour terminer la modification.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_game_form, menu); // Remplace le menu actuel par celui indiqué dans l'inflate.
        menu.getItem(3).setVisible(false); // On cache l'icône de validation de match.
        LocaleUtil.setLocaleSwitch(menu, this);
        setContentView(R.layout.activity_game_review);
        initView();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        final Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        return true;
    }

    private void initView(){
        appContainer.getExecutorService().execute(() -> {
            final SQLiteOpenHelper dbHelper = new DbHelper(this);
            db = dbHelper.getWritableDatabase();
            final List<GameData> games = DataSourceUtil.getLocalDataSource(appContainer, GenericLocalDataSource.class).getAll(db, GameData.class);
            appContainer.getHandler().post(() -> handleRetrievedPreviousGames(games));
        });
    }

    private void handleRetrievedPreviousGames(@NonNull List<GameData> games){
        viewModel.setAllGames(games);
        if(games.isEmpty())
            showMissingGamesMessage();
        else
            displayGameCards(games, true, true);
    }

    private void showMissingGamesMessage(){
        final ViewGroup layout = findViewById(R.id.game_review);
        final TextView missingGamesMessage = getBuiltMissingGamesMessage();
        layout.removeAllViews();
        layout.addView(missingGamesMessage);
    }

    private TextView getBuiltMissingGamesMessage(){
        final TextView missingGamesMessage = new TextView(this);
        missingGamesMessage.setId(R.id.missing_games_message);
        missingGamesMessage.setGravity(Gravity.CENTER);
        setMissingGamesMaxWidth(missingGamesMessage);
        setMissingGamesMessageLayoutParams(missingGamesMessage);
        setMissingGamesMessagePadding(missingGamesMessage);
        setMissingGamesMessageElevation(missingGamesMessage);
        setMissingGamesMessageText(missingGamesMessage);
        SetMissingGamesMessageBackground(missingGamesMessage);
        return missingGamesMessage;
    }

    private void setMissingGamesMaxWidth(@NonNull TextView missingGamesMessage) {
        final float DP_MAX_WIDTH = 300f;
        missingGamesMessage.setMaxWidth(DensityUtil.fromDpToPx(DP_MAX_WIDTH, getResources()));
    }

    private void setMissingGamesMessagePadding(@NonNull TextView missingGamesMessage) {
        final float DP_PADDING = 20f;
        final int padding = DensityUtil.fromDpToPx(DP_PADDING, getResources());
        missingGamesMessage.setPadding(padding, padding, padding, padding);
    }

    private void SetMissingGamesMessageBackground(@NonNull TextView missingGamesMessage) {
        final Drawable background = ResourcesCompat.getDrawable(getResources(), R.drawable.missing_games_message_border, getTheme());
        missingGamesMessage.setBackground(background);
    }

    private void setMissingGamesMessageText(@NonNull TextView missingGamesMessage) {
        final String message = getResources().getString(R.string.no_games_found);
        missingGamesMessage.setText(message);
        final int textColor = ResourcesCompat.getColor(getResources(), R.color.white, getTheme());
        missingGamesMessage.setTextColor(textColor);
        missingGamesMessage.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    }

    private void setMissingGamesMessageElevation(@NonNull TextView missingGamesMessage) {
        float dpDefaultElevation = getResources().getDimension(R.dimen.cardview_default_elevation);
        int pxDefaultElevation = DensityUtil.fromDpToPx(dpDefaultElevation, getResources());
        missingGamesMessage.setElevation(pxDefaultElevation);
    }

    private void setMissingGamesMessageLayoutParams(@NonNull TextView missingGamesMessage) {
        final float DP_TOP_MARGIN = 30f;
        final int pxTopMargin = DensityUtil.fromDpToPx(DP_TOP_MARGIN, getResources());
        final LinearLayout.LayoutParams layoutParams = getLayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0, pxTopMargin, 0, 0, true);
        missingGamesMessage.setLayoutParams(layoutParams);
    }

    private void displayGameCards(@NonNull List<GameData> games, boolean mustRemoveAllViews, boolean mustAddGameLoadButton){
        final ViewGroup layout = findViewById(R.id.game_review);
        if(mustRemoveAllViews)
            layout.removeAllViews();
        games.forEach(game -> addGameCardToLayout(layout, game));
        if(mustAddGameLoadButton)
            addGameLoadButtonToLayout(layout);
    }

    private void addGameCardToLayout(ViewGroup layout, GameData game) {
        final MaterialCardView gameCard = getGameCardFromData(game);
        layout.addView(gameCard);
    }

    private void addGameLoadButtonToLayout(ViewGroup layout) {
        final MaterialButton gameLoadButton = getGameLoadButton();
        layout.addView(gameLoadButton);
    }

    private MaterialCardView getGameCardFromData(@NonNull GameData game){
        final MaterialCardView gameCard = new MaterialCardView(this);
        setGameCardLayoutParams(gameCard);
        final LinearLayout cardContainer = getGameCardContainer(game);
        gameCard.addView(cardContainer);
        return gameCard;
    }

    private LinearLayout getGameCardContainer(@NonNull GameData game) {
        final LinearLayout cardContainer = getGameCardLinearLayout();
        addImageToGameCardContainer(cardContainer);
        addBodyToGameCardContainer(game, cardContainer);
        return cardContainer;
    }

    private void addBodyToGameCardContainer(@NonNull GameData game, @NonNull LinearLayout cardContainer) {
        final LinearLayout cardBody = getGameCardBody(game);
        cardContainer.addView(cardBody);
    }

    private void addImageToGameCardContainer(@NonNull LinearLayout cardContainer) {
        final ImageView image = getGameCardImage();
        cardContainer.addView(image);
    }

    @NonNull
    private LinearLayout getGameCardBody(@NonNull GameData game) {
        final LinearLayout cardBody = getGameCardLinearLayout();
        setGameCardBodyPadding(cardBody);
        addTeamsToGameCardBody(game, cardBody);
        addScoresToGameCardBody(game, cardBody);
        addGameCardSecondaryComponents(game, cardBody);
        MaterialButton statsButton = getStatsButtonFromData(game);
        cardBody.addView(statsButton);
        return cardBody;
    }

    private void addScoresToGameCardBody(@NonNull GameData game, LinearLayout cardBody) {
        TextView scores = getGameCardScoresFromData(game);
        cardBody.addView(scores);
    }

    private void addTeamsToGameCardBody(@NonNull GameData game, LinearLayout cardBody) {
        TextView teams = getGameCardTeams(game);
        cardBody.addView(teams);
    }

    private void setGameCardBodyPadding(@NonNull LinearLayout cardBody) {
        final float DP_CARD_BODY_PADDING = 16f;
        final int pxCardBodyPadding = DensityUtil.fromDpToPx(DP_CARD_BODY_PADDING, getResources());
        cardBody.setPadding(pxCardBodyPadding, pxCardBodyPadding, pxCardBodyPadding, pxCardBodyPadding);
    }

    private void addGameCardSecondaryComponents(@NonNull GameData game, @NonNull LinearLayout cardBody) {
        final int GAME_CARD_SECONDARY_COMPONENTS_COUNT = 6;
        for(int i = 0; i < GAME_CARD_SECONDARY_COMPONENTS_COUNT; ++i){
            final TextView secondaryComp = new TextView(this);
            setGameCardComponentAndProgressLayoutParams(secondaryComp, false);
            setGameCardSecondaryComponentText(game, i, secondaryComp);
            setGameCardSecondaryComponentDrawable(i, secondaryComp);
            cardBody.addView(secondaryComp);
        }
    }

    private void setGameCardSecondaryComponentDrawable(int i, @NonNull TextView secondaryComp) {
        secondaryComp.setCompoundDrawablesRelativeWithIntrinsicBounds(getGameCardSecondaryComponentDrawable(i), null, null, null);
        secondaryComp.setCompoundDrawablePadding(getGameCardSecondaryComponentDrawablePadding());
    }

    private void setGameCardSecondaryComponentText(@NonNull GameData game, int i, @NonNull TextView secondaryComp) {
        secondaryComp.setTextAppearance(R.style.TextAppearance_MaterialComponents_Body2);
        secondaryComp.setText(getGameCardSecondaryComponentText(i, game));
    }

    private void setGameCardLayoutParams(@NonNull MaterialCardView gameCard) {
        final int defaultPxMargin = getDefaultPxMargin();
        final LinearLayout.LayoutParams layoutParams = getLayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, defaultPxMargin, defaultPxMargin, defaultPxMargin, defaultPxMargin, false);
        gameCard.setLayoutParams(layoutParams);
    }

    private ImageView getGameCardImage(){
        final ImageView image = new ImageView(this);
        setGameCardImageLayoutParams(image);
        setGameCardImageSource(image);
        setGameCardImageDescription(image);
        return image;
    }

    private void setGameCardImageDescription(@NonNull ImageView image) {
        final String imageDescription = getResources().getString(R.string.game_card_image_description);
        image.setContentDescription(imageDescription);
    }

    private void setGameCardImageSource(@NonNull ImageView image) {
        final Drawable imageDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.soccer_game_background, getTheme());
        image.setImageDrawable(imageDrawable);
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    private void setGameCardImageLayoutParams(@NonNull ImageView image) {
        final float DP_IMAGE_HEIGHT = 100f;
        final int pxImageHeight = DensityUtil.fromDpToPx(DP_IMAGE_HEIGHT, getResources());
        image.setLayoutParams(getLayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, pxImageHeight, 0, 0, 0, 0, false));
    }

    private LinearLayout getGameCardLinearLayout(){
        final LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(getLayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0, 0, 0, 0, false));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        return linearLayout;
    }

    private TextView getGameCardTeams(@NonNull GameData game){
        final TextView teams = new TextView(this);
        teams.setLayoutParams(getLayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0, 0, 0, 0, false));
        setGameCardTeamsText(game, teams);
        return teams;
    }

    private void setGameCardTeamsText(@NonNull GameData game, @NonNull TextView teams) {
        final String trackedTeamName = game.getTrackedTeam().getName();
        final String opponentTeamName = game.getOpponentTeam().getName();
        final String trackedTeamDiscriminator = getResources().getString(R.string.tracked_team_descriminator);
        teams.setText(String.format("%s (%s) - %s", trackedTeamName, trackedTeamDiscriminator, opponentTeamName));
        teams.setTextAppearance(R.style.TextAppearance_MaterialComponents_Headline6);
    }

    private TextView getGameCardScoresFromData(@NonNull GameData game){
        final TextView scores = new TextView(this);
        setGameCardComponentAndProgressLayoutParams(scores, true);
        setGameCardScoresText(game, scores);
        return scores;
    }

    @SuppressLint("DefaultLocale")
    private void setGameCardScoresText(@NonNull GameData game, @NonNull TextView scores) {
        scores.setText(String.format("%d - %d", game.getTrackedTeamScore(), game.getOpponentScore()));
        scores.setTextAppearance(R.style.TextAppearance_MaterialComponents_Headline5);
        scores.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    }

    // Margin utilisee par toutes les TextViews des panels de match sauf celle des equipes (titre).
    private int getDefaultPxMargin(){
        final float DP_MARGIN = 8f;
        return DensityUtil.fromDpToPx(DP_MARGIN, getResources());
    }

    private Drawable getGameCardSecondaryComponentDrawable(int secondaryComponentIndex){
        final Drawable drawable;
        switch(secondaryComponentIndex){
            case 0:
                drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.baseline_emoji_events_24, getTheme());
                break;
            case 1:
                drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.baseline_sports_24, getTheme());
                break;
            case 2:
                drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.baseline_today_24, getTheme());
                break;
            case 3:
                drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.baseline_schedule_24, getTheme());
                break;
            case 4:
                drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.baseline_place_24, getTheme());
                break;
            default:
                drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.baseline_timelapse_24, getTheme());
                break;
        }
        Objects.requireNonNull(drawable).setTint(ResourcesCompat.getColor(getResources(), R.color.design_default_color_primary, getTheme()));
        return drawable;
    }

    private int getGameCardSecondaryComponentDrawablePadding(){
        final float DP_DRAWABLE_PADDING = 15f;
        return DensityUtil.fromDpToPx(DP_DRAWABLE_PADDING, getResources());
    }

    private String getGameCardSecondaryComponentText(int secondaryComponentIndex, @NonNull GameData game){
        switch(secondaryComponentIndex){
            case 0:
                return game.getChampionship().getName();
            case 1:
                return game.getReferee().getName();
            case 2:
                return game.getDateTime().split(" ")[0]; // Date (avec datetime au format jj/MM/AAAA HH:mm)
            case 3:
                return game.getDateTime().split(" ")[1]; // Time (avec datetime au format jj/MM/AAAA HH:mm)
            case 4:
                return game.getAddress();
            default:
                return DateTimeUtil.getTimeLapseDetail(game.getOverTime(), getResources());
        }
    }

    private MaterialButton getStatsButtonFromData(@NonNull GameData game){
        final MaterialButton statsButton = new MaterialButton(this);
        statsButton.setTag(game);
        setGameCardComponentAndProgressLayoutParams(statsButton, true);
        statsButton.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.design_default_color_primary, getTheme()));
        statsButton.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.outline_trending_up_24, getTheme()));
        setStatsButtonText(statsButton);
        statsButton.setOnClickListener(this::onStatsButtonClick);
        return statsButton;
    }

    private void onStatsButtonClick(View view){
        final Intent intent = new Intent(this, GameStatsActivity.class);
        intent.putExtra(GAME_EXTRA_KEY, JsonUtil.toJson((GameData)view.getTag()));
        startActivity(intent);
    }

    private void setStatsButtonText(@NonNull MaterialButton statsButton) {
        statsButton.setText(getResources().getString(R.string.stats_button_label));
        statsButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.white, getTheme()));
    }

    private void setGameCardComponentAndProgressLayoutParams(@NonNull View view, boolean mustCenterLayoutGravity){
        final int pxMargin = getDefaultPxMargin();
        view.setLayoutParams(getLayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, pxMargin, pxMargin, pxMargin, pxMargin, mustCenterLayoutGravity));
    }

    private LinearLayout.LayoutParams getLayoutParams(int width, int height, int pxLeftMargin, int pxTopMargin, int pxRightMargin, int pxBottomMargin, boolean mustSetCenteredGravity){
        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        layoutParams.leftMargin = pxLeftMargin;
        layoutParams.topMargin = pxTopMargin;
        layoutParams.rightMargin = pxRightMargin;
        layoutParams.bottomMargin = pxBottomMargin;
        if(mustSetCenteredGravity)
            layoutParams.gravity = Gravity.CENTER;
        return layoutParams;
    }

    private MaterialButton getGameLoadButton(){
        final MaterialButton gameLoadButton = new MaterialButton(this);
        setGameCardComponentAndProgressLayoutParams(gameLoadButton, false);
        gameLoadButton.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.field_grey, getTheme()));
        setGameLoadButtonText(gameLoadButton);
        setGameLoadCornerRadius(gameLoadButton);
        gameLoadButton.setOnClickListener(this::loadMoreGame);
        return gameLoadButton;
    }

    private void setGameLoadCornerRadius(@NonNull MaterialButton gameLoadButton) {
        final float DP_GAME_LOAD_BUTTON_CORNER_RADIUS = 40f;
        final int pxGameLoadButtonCornerRadius = DensityUtil.fromDpToPx(DP_GAME_LOAD_BUTTON_CORNER_RADIUS, getResources());
        gameLoadButton.setCornerRadius(pxGameLoadButtonCornerRadius);
    }

    private void setGameLoadButtonText(@NonNull MaterialButton gameLoadButton) {
        gameLoadButton.setText(getResources().getString(R.string.game_load_button_label));
        gameLoadButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.white, getTheme()));
    }

    private void loadMoreGame(@NonNull View view){
        final ViewGroup layout = findViewById(R.id.game_review);
        layout.removeView(view); // On enleve le bouton de chargement de matchs supplementaires.
        addLoadProgressToLayout(layout);
        appContainer.getExecutorService().execute(() -> {
            final List<GameData> remoteGames = ((GameReviewRemoteDataSource)DataSourceUtil.getRemoteDataSource(appContainer, GameReviewRemoteDataSource.class)).getAllGames();
            appContainer.getHandler().post(() -> handleRetrievedRemoteGames(remoteGames));
        });
    }

    private void handleRetrievedRemoteGames(@NonNull List<GameData> remoteGames) {
        final List<GameData> remoteGamesToDisplay = getRemoteGamesToDisplay(remoteGames);
        viewModel.getAllGames().addAll(remoteGamesToDisplay);
        displayRemoteGames(remoteGamesToDisplay);
    }

    @NonNull
    private List<GameData> getRemoteGamesToDisplay(@NonNull List<GameData> remoteGames) {
        final List<Long> gameIdList = viewModel.getAllGamesIdList();
        return remoteGames.stream()
                .filter(remoteGame -> !Objects.requireNonNull(gameIdList).contains(remoteGame.getId()))
                .collect(Collectors.toList());
    }

    private void addLoadProgressToLayout(@NonNull ViewGroup layout){
        final ProgressBar loadProgress = getLoadProgress();
        layout.addView(loadProgress);
    }

    private ProgressBar getLoadProgress(){
        final ProgressBar loadProgress = new ProgressBar(this);
        loadProgress.setId(R.id.remote_game_load_progress);
        setGameCardComponentAndProgressLayoutParams(loadProgress, false);
        return loadProgress;
    }

    private void displayRemoteGames(@NonNull List<GameData> remoteGamesToDisplay){
        final ViewGroup layout = findViewById(R.id.game_review);
        layout.removeView(findViewById(R.id.remote_game_load_progress));
        if(!remoteGamesToDisplay.isEmpty())
            displayGameCards(remoteGamesToDisplay, false, false);
    }
}