package fr.android.foottracker.controller.gameform.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import fr.android.foottracker.R;
import fr.android.foottracker.controller.gameform.fragment.map.MapFragment;

// Collection des fragments dans GameFormActivity. Les cree et les agence dans le ViewPager.
public class GameFormCollectionFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_form_collection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final GameFormCollectionAdapter adapter = new GameFormCollectionAdapter(this);
        final ViewPager2 viewPager = view.findViewById(R.id.game_form_pager);
        viewPager.setAdapter(adapter);
        // On cree les tabs du layout à partir des fragments crees avec l'adapter.
        final TabLayout tabLayout = view.findViewById(R.id.game_form_tab_layout);
        new TabLayoutMediator(tabLayout, viewPager, this::setTabAppearanceFromPosition).attach();
    }

    // Donne un icône et un texte à chaque tab selon sa position
    private void setTabAppearanceFromPosition(@NonNull TabLayout.Tab tab, int position) {
        setTabIconFromPosition(tab, position);
        setTabTextFromPosition(tab, position);
    }

    private void setTabIconFromPosition(@NonNull TabLayout.Tab tab, int position) {
        switch (position) {
            case 0:
                tab.setIcon(R.drawable.baseline_sports_soccer_white_24); // Icône de GameFormFragment
                break;
            case 1:
                tab.setIcon(R.drawable.baseline_groups_white_24); // Icône de TeamFormFragment
                break;
            default:
                tab.setIcon(R.drawable.baseline_place_white_24); // Icône de MapFormFragment
                break;
        }
    }

    // Donne un texte au tab selon sa position.
    private void setTabTextFromPosition(@NonNull TabLayout.Tab tab, int position) {
        switch (position) {
            case 0:
                tab.setText(R.string.game_form_tab_title); // 1er tab a le titre de GameFormFragment
                break;
            case 1:
                tab.setText(R.string.game_form_team_tab_title); // 2eme tab a le titre de TeamFormFragment
                break;
            default:
                tab.setText(R.string.game_form_map_tab_title); // 3eme tab a le titre de MapFormFragment
                break;
        }
    }

    // Cree les fragments de GameFormActivity
    private static class GameFormCollectionAdapter extends FragmentStateAdapter {

        public static final int ITEM_COUNT = 3;

        public GameFormCollectionAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new GameFormFragment(); // GameFormFragment est le 1er tab.
                case 1:
                    return new TeamFormFragment(); // GameFormFragment est le 2eme tab.
                default:
                    return new MapFragment(); // GameFormFragment est le 3eme tab.
            }
        }

        // Nombre de tabs ( = nombre de fragments).
        @Override
        public int getItemCount() {
            return ITEM_COUNT;
        }
    }

}
