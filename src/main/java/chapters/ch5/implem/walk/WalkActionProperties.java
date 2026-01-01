package chapters.ch5.implem.walk;


public record WalkActionProperties(
        int deltaX,
        String arrow
) {

    public static WalkActionProperties of(int deltaX, String arrow) {
        return new WalkActionProperties(deltaX,arrow);
    }

}
