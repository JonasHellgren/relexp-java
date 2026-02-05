package chapters.ch14.domain.interfaces;


import chapters.ch14.plotting.Recorder;

/**
 * This interface represents an Executor in the l_comb_learn_plan domain.
 * It provides a way to validate the number of episodes and maximum steps per episode.
 * The ExecutorI interface is generic and can be used with any type of state (S), action (A), and state index (SI).
 */
public interface ExecutorI<SI, S, A>  {

     void validate(int nEpisodes, int maxStepsPerEpisode);
     Recorder getRecorder();

}
