package fr.android.foottracker.utils;

import fr.android.foottracker.R;
import fr.android.foottracker.model.entities.PlayerPosition;

public final class PlayerUtil {

    private PlayerUtil() {
    }

    public static String getPlayerPositionBySlotId(int slotId) {
        if (slotId == R.id.striker)
            return PlayerPosition.STRIKER;
        else if (slotId == R.id.left_midfielder)
            return PlayerPosition.LEFT_MIDFIELDER;
        else if (slotId == R.id.right_midfielder)
            return PlayerPosition.RIGHT_MIDFIELDER;
        else if (slotId == R.id.attacking_midfielder)
            return PlayerPosition.ATTACKING_MIDFIELDER;
        else if (slotId == R.id.central_midfielder)
            return PlayerPosition.CENTRAL_MIDFIELDER;
        else if (slotId == R.id.defending_midfielder)
            return PlayerPosition.DEFENDING_MIDFIELDER;
        else if (slotId == R.id.left_fullback)
            return PlayerPosition.LEFT_FULLBACK;
        else if (slotId == R.id.right_fullback)
            return PlayerPosition.RIGHT_FULLBACK;
        else if (slotId == R.id.left_center_back)
            return PlayerPosition.LEFT_CENTER_BACK;
        else if (slotId == R.id.right_center_back)
            return PlayerPosition.RIGHT_CENTER_BACK;
        else if (slotId == R.id.goalkeeper)
            return PlayerPosition.GOALKEEPER;
        else
            throw new IllegalArgumentException("Provided ID does not belong to team form fragment slot list.");
    }
}
