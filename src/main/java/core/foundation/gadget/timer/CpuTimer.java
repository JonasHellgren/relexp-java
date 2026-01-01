package core.foundation.gadget.timer;

import lombok.Getter;


/**
 * A simple timer class to measure CPU time.
 *
 * This class provides methods to start, stop, and reset the timer, as well as to check if a time budget has been exceeded.
 *

 */

@Getter
public class CpuTimer {

    long startTimeMillis;  //starting time, long <=> minimum value of 0
    protected long timeBudgetMillis;

    long absoluteProgress;
    float relativeProgress;


    public CpuTimer() {
        this(Long.MAX_VALUE);
    }

    public static CpuTimer newWithTimeBudgetInMilliSec(long timeBudgetMillis) {
        return new CpuTimer(timeBudgetMillis);
    }

    public static CpuTimer empty() {
        return new CpuTimer();
    }

    public CpuTimer(long timeBudgetMillis) {
        this.timeBudgetMillis = timeBudgetMillis;
        reset();
    }

    public void reset() {
        startTimeMillis = System.currentTimeMillis();
    }

    public void stop() {
        absoluteProgress= absoluteProgressInMillis();
        relativeProgress=relativeProgress();
    }

    public boolean isTimeExceeded() {
        return System.currentTimeMillis() > startTimeMillis + timeBudgetMillis;
    }

    public float relativeProgress() {
        return absoluteProgressInMillis()/ (float) timeBudgetMillis;
    }

    public long absoluteProgressInMillis() {
        return  (System.currentTimeMillis() - startTimeMillis);
    }

    public String toString() {
        return "elapsed time in millis() = "+absoluteProgressInMillis()+", relativeProgress = "+relativeProgress();
    }

    public  String timeInMinutesAsString() {
        double timeInMin = absoluteProgressInMillis() * 1d / 1000 / 60 ;
        return "Time (minutes) = "  + timeInMin;
    }

    public  String timeInSecondsAsString() {
        double timeInSec = absoluteProgressInMillis() * 1d / 1000 ;
        return String.valueOf(timeInSec);
    }

    public void printInMs() {
        System.out.println("time used (ms) = " + absoluteProgressInMillis());
    }


}
