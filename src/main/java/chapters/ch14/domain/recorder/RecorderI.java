package chapters.ch14.domain.recorder;

import chapters.ch14.plotting.MeasuresCombLP;
import chapters.ch14.plotting.MeasuresCombLPEnum;

import java.util.List;

/**
 * Interface for recording measures. Used later for plotting.
 */
public interface RecorderI {
    /**
     * Adds a measure to the recorder.
     * @param measures The measure to add.
     */
    void add(MeasuresCombLP measures);

    /**
     * Gets the trajectory of a specific measure.
     * @param measure The measure to get the trajectory of.
     * @return A list of the measure values.
     */
    List<Double> trajectory(MeasuresCombLPEnum measure);

    /**
     * Checks if the recorder is empty.
     * @return True if the recorder is empty, false otherwise.
     */
    boolean isEmpty();
}
