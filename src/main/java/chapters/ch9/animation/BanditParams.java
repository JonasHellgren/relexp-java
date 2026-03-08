package chapters.ch9.animation;

import lombok.Builder;

@Builder
record BanditParams(
        int left,
        int right,
        int top,
        int bottom,
        int windowLeft,
        int windowRight,
        int windowTop,
        int windowBottom,
        int coinTop,
        int armWidth,
        int armLenght
) {


    public static BanditParams create() {
        return new BanditParams(
                5, 45, 60, 5, 12, 38, 47, 30, 20, 5,20);
    }

    public int armLeftXPos() {
        return left() - armWidth / 2;
    }

    public int armRightXPos() {
        return right + armWidth / 2;
    }

    public int topArm() {
        return top-armWidth/2;
    }

    public int coinX() {
        return left+(right-left)/2;
    }

    public int coinY() {
        return (coinTop - bottom)/2 + bottom;
    }

    public double dispY() {
        return (coinTop - bottom) / 2.0;
    }
}