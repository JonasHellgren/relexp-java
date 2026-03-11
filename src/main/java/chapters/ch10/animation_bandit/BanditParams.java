package chapters.ch10.animation_bandit;

import lombok.Builder;

import java.awt.*;

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
                5, 45, 60, 1, 12, 38, 47, 30, 20, 5,20);
    }

    public int armLeftXPos() {
        return left() - armWidth;
    }

    public int armRightXPos() {
        return right + armWidth;
    }

    public int topArm() {
        return windowTop;
    }

    public int coinX() {
        return left+(right-left)/2;
    }

    public int panelHigh() {
        return coinTop+5;
    }



    public int panelLow() {
        return coinTop-5;
    }

    public double dispY() {
        return (coinTop - bottom) *0.35;
    }

    public double dispYtop() {
        return (coinTop - bottom) *0.35+5;
    }

    public double bottomArm() {
        return topArm()-armLenght;
    }

    public double dispLeft() {
        return mid() -5;
    }

    public double dispRight() {
        return mid() +5;
    }

    public double coinY() {
        return (coinTop-bottom)/2+1;
    }

    public Color colorKnob() {
        return new Color(0, 100, 0);  //r
    }

    private int mid() {
        return left + (right - left)/2;
    }

    public int buttonY() {
        return panelLow()+ (panelHigh()- panelLow())/2;
    }


    public int xRel(double v) {
        return (int) (v * (right - left) + left);
    }


    public Color colorButtons() {
        return Color.RED;
    }
}