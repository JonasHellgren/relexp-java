package chapters.ch14.factory;

import chapters.ch14.environments.pong.PongSettings;
import chapters.ch14.environments.pong.PosXy;
import chapters.ch14.environments.pong.StatePong;
import core.foundation.util.rand.RandUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FactoryStatePong {

    public static final double T_STILL = 0d;
    PongSettings pongSetting;

    public static FactoryStatePong of(PongSettings pongSetting) {
        return new FactoryStatePong(pongSetting);
    }

    public StatePong random() {
        boolean isHeadNorth=Math.random()>0.5;
        double headingAngle = RandUtils.getRandomDouble(
                pongSetting.minHeadingAngleRad(),
                pongSetting.maxHeadingAngleRad());
        double xBall = RandUtils.getRandomDouble(pongSetting.radiusBall(), pongSetting.xMaxMinusHalfRadiusBall());
        double yBall = RandUtils.getRandomDouble(pongSetting.radiusBall(), pongSetting.yMaxMinusHalfRadiusBall());
        double xPaddle = RandUtils.getRandomDouble(0, pongSetting.xMax());
        return StatePong.of(
                PosXy.of(xBall, yBall),
                xPaddle,
                isHeadNorth ? headingAngle : -headingAngle,
                T_STILL);
    }

    public StatePong willFail() {
        return StatePong.of(
                PosXy.of(pongSetting.radiusBall(), pongSetting.yMid()),
                pongSetting.xMaxMinusHalfPaddle(),
                -Math.PI/2,
                T_STILL);
    }

    public StatePong midMidSouth() {
        return StatePong.of(
                PosXy.of(pongSetting.xMid(), pongSetting.yMid()),
                pongSetting.xMid(),
                -Math.PI/2,
                T_STILL);
    }

    public StatePong midMidNorth() {
        return StatePong.of(
                PosXy.of(pongSetting.xMid(), pongSetting.yMid()),
                pongSetting.xMid(),
                Math.PI/2,
                T_STILL);
    }


    public  StatePong doomed() {
        return StatePong.of(
                PosXy.of(pongSetting.xMaxMinusHalfRadiusBall(), pongSetting.yMid()/2),
                pongSetting.xMinPlusHalfPaddle(),
                -Math.PI/2,
                T_STILL);
    }

    public StatePong stateBallXMaxPaddleMid_HeadSouth(double y) {
        return StatePong.of(
                PosXy.of(pongSetting.xMaxMinusHalfRadiusBall(), y),
                pongSetting.xMid(),
                -Math.PI/2,
                T_STILL);
    }


}
