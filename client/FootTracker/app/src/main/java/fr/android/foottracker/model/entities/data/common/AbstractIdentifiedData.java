package fr.android.foottracker.model.entities.data.common;

public abstract class AbstractIdentifiedData {

    private long id;

    public AbstractIdentifiedData(){}

    public AbstractIdentifiedData(long id){
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
