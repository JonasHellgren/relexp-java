package chapters.ch5.implem.dice;

public record VariablesDice(int sum, int count) {

    public static VariablesDice of(int sum, int count) {
        return new VariablesDice(sum, count);
    }

    public static VariablesDice start() {
        return VariablesDice.of(0, 0);
    }

    @Override
    public String toString() {
        return "VariablesDice{" +
                "sum=" + sum +
                ", count=" + count +
                '}';
    }

}
