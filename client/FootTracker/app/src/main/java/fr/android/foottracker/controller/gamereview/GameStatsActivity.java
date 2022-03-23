package fr.android.foottracker.controller.gamereview;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import java.util.Objects;

import fr.android.foottracker.R;
import fr.android.foottracker.controller.home.MainActivity;
import fr.android.foottracker.model.entities.data.GameData;
import fr.android.foottracker.model.view.GameStatsViewModel;
import fr.android.foottracker.utils.ActionBarUtil;
import fr.android.foottracker.utils.JsonUtil;
import fr.android.foottracker.utils.LocaleUtil;

public class GameStatsActivity extends AppCompatActivity {

    private GameStatsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_stats);
        viewModel = new ViewModelProvider(this).get(GameStatsViewModel.class);
        setViewModel();
        ActionBarUtil.setToolBar(getSupportActionBar());
        setBottomNavigationView();
        LocaleUtil.initLocale(this);
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        final Intent intent = new Intent(this, GameReviewActivity.class);
        startActivity(intent);
        return true;
    }

    private void setBottomNavigationView(){
        final BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        final NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.game_stats_nav_host_fragment);
        final NavController navController = Objects.requireNonNull(navHostFragment).getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        setBottomNavigationViewSelectionListener(bottomNavigationView, navController);
    }

    private void setBottomNavigationViewSelectionListener(@NonNull BottomNavigationView bottomNavigationView, @NonNull NavController navController){
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if(item.getItemId() == R.id.navigation_game_stats_overview)
                navController.navigate(R.id.action_to_overview);
            else
                navController.navigate(R.id.action_to_timeline);
            return false;
        });
    }

    private void setViewModel(){
        final String jsonGame = getIntent().getStringExtra(GameReviewActivity.GAME_EXTRA_KEY);
        final GameData game = JsonUtil.fromJson(jsonGame, GameData.class);
        viewModel.setGame(game);
    }
}