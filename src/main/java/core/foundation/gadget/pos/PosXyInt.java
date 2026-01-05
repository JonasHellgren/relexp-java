package core.foundation.gadget.pos;

public record PosXyInt(int x, int y) {
    public static PosXyInt of(int x, int y) {
        return new PosXyInt(x, y);
    }
}
