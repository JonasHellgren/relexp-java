package core.plotting.progress_plotting;

import lombok.Getter;

@Getter
public enum ProgressMeasureEnum {
    RETURN("Return"),
    N_STEPS("Number of steps"),
    TD_ERROR("TD error"),
    TD_ERROR_CLIPPED("TD error clipped"),
    GRADIENT_ACTOR_MEAN("Gradient actor mean"),
    GRADIENT_ACTOR_MEAN_CLIPPED("Gradient actor mean clipped"),
    STD_ACTOR("Standard dev. actor"),
    TD_BEST_ACTION("TD error"),
    SIZE_MEMORY("Memory size");


    private final String description;

    ProgressMeasureEnum(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }

}

