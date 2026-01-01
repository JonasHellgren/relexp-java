package chapters.ch5.implem.walk;

public record VariablesWalk(int x) {

    public static VariablesWalk of(int x) {
        return new VariablesWalk(x);
    }

    @Override
    public String toString() {
        return String.valueOf(x);
    }

}
