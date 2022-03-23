package fr.android.foottracker.controller.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import fr.android.foottracker.R;
import fr.android.foottracker.controller.gameform.GameFormActivity;
import fr.android.foottracker.controller.gamereview.GameReviewActivity;
import fr.android.foottracker.utils.LocaleUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setButtonClickListeners();
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

    private void setButtonClickListeners(){
        final Button gameCreationButton = findViewById(R.id.game_creation_btn);
        final Button previousGamesButton = findViewById(R.id.previous_games_btn);
        gameCreationButton.setOnClickListener(this::onButtonClick);
        previousGamesButton.setOnClickListener(this::onButtonClick);
    }

    private void onButtonClick(View view){
        final Intent intent = view.getId() == R.id.game_creation_btn
                ? new Intent(this, GameFormActivity.class)
                : new Intent(this, GameReviewActivity.class);
        startActivity(intent);
    }
}