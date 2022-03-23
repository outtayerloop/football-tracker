package fr.android.foottracker.model.entities.data;

import fr.android.foottracker.model.entities.data.common.AbstractIdentifiedData;

public class AttachmentData extends AbstractIdentifiedData {


    private String path;
    private long gameId;

    public AttachmentData(long attachmentId, String attachmentPath, long attachmentGameId){
        super(attachmentId);
        this.path = attachmentPath;
        this.gameId = attachmentGameId;
    }

    public String getPath() {
        return path;
    }

    public long getGameId() {
        return gameId;
    }


}
