package fr.android.foottracker.model.view;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

/**
 * Classe qui stocke toutes les performances à suivre pour
 * la mise à jour pendant le déroulement du match
 */
public class GamePerformanceViewModel extends ViewModel {

    final private MutableLiveData<Integer> outCount;

    final private MutableLiveData<Integer> offsideCount;

    final private MutableLiveData<Integer> faultCount;

    final private MutableLiveData<Integer> redcardCount;

    final private MutableLiveData<Integer> yellowcardCount;

    final private MutableLiveData<Integer> pelnaltyCount;

    final private MutableLiveData<Integer> freekickCount;

    final private MutableLiveData<Integer> cornerCount;

    final private MutableLiveData<Integer> possessionCount;

    final private MutableLiveData<Integer> occasionCount;

    final private MutableLiveData<Integer> stopCount;

    final private MutableLiveData<Integer> goalCount;

    final private MutableLiveData<Integer> scoreCount;

    public MutableLiveData<Integer> getDefaultValue() {
        return defaultValue;
    }

    private MutableLiveData<Integer> defaultValue;


    private MutableLiveData<String> overtime;

    public MutableLiveData<Integer> getCornerCount() {
        return cornerCount;
    }

    public GamePerformanceViewModel() {


        this.outCount = new MutableLiveData<>();
        this.outCount.setValue(0);

        this.offsideCount = new MutableLiveData<>();
        this.offsideCount.setValue(0);

        this.cornerCount = new MutableLiveData<>();
        this.cornerCount.setValue(0);

        this.faultCount = new MutableLiveData<>();
        this.faultCount.setValue(0);

        this.redcardCount = new MutableLiveData<>();
        this.redcardCount.setValue(0);

        this.yellowcardCount = new MutableLiveData<>();
        this.yellowcardCount.setValue(0);

        this.pelnaltyCount = new MutableLiveData<>();
        this.pelnaltyCount.setValue(0);

        this.freekickCount = new MutableLiveData<>();
        this.freekickCount.setValue(0);

        this.possessionCount = new MutableLiveData<>();
        this.possessionCount.setValue(0);

        this.occasionCount = new MutableLiveData<>();
        this.occasionCount.setValue(0);

        this.goalCount = new MutableLiveData<>();
        this.goalCount.setValue(0);

        this.stopCount = new MutableLiveData<>();
        this.stopCount.setValue(0);

        this.scoreCount = new MutableLiveData<>();
        this.scoreCount.setValue(0);

        this.defaultValue = new MutableLiveData<>();
        this.defaultValue.setValue(0);

        this.overtime = new MutableLiveData<>();
        this.overtime.setValue("00:00:00");

    }

    public MutableLiveData<Integer> getOutCount() {
        return outCount;
    }

    public MutableLiveData<Integer> getOffsideCount() {
        return this.offsideCount;
    }

    public MutableLiveData<Integer> getFaultCount() {
        return faultCount;
    }

    public MutableLiveData<Integer> getRedcardCount() {
        return redcardCount;
    }

    public MutableLiveData<Integer> getYellowcardCount() {
        return yellowcardCount;
    }

    public MutableLiveData<Integer> getFreekickCount() {
        return freekickCount;
    }

    public MutableLiveData<Integer> getPelnaltyCount() {
        return pelnaltyCount;
    }

    public MutableLiveData<Integer> getPossessionCount() {
        return possessionCount;
    }

    public MutableLiveData<Integer> getOccasionCount() {
        return occasionCount;
    }

    public MutableLiveData<Integer> getStopCount() {
        return stopCount;
    }

    public MutableLiveData<Integer> getGoalCount() {
        return goalCount;
    }

    public MutableLiveData<Integer> getScoreCount() {
        return scoreCount;
    }

    public MutableLiveData<String> getOvertime() {
        return overtime;
    }

}