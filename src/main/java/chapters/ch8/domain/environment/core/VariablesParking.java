package chapters.ch8.domain.environment.core;

import java.util.Objects;


/**
 * Different nof steps shall give same equals and hashCode
 */
public record VariablesParking(
        int nOccupied,
        FeeEnum fee,
        int nSteps
) {


    public VariablesParking copy() {
        return new VariablesParking(nOccupied, fee, nSteps);
    }

    @Override
    public String toString() {
        return "VariablesParking[" +
                "nOccupied=" + nOccupied +
                ", fee=" + fee +
                ']';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VariablesParking that = (VariablesParking) o;
        return nOccupied == that.nOccupied &&  fee == that.fee;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nOccupied, fee, nSteps);
    }


}
