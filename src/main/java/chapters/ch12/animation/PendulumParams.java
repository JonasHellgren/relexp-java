package chapters.ch12.animation;

import chapters.ch12.domain.inv_pendulum.environment.core.EnvironmentPendulum;
import chapters.ch12.domain.inv_pendulum.trainer.core.ExperiencePendulum;
import core.foundation.gadget.pos.PosXyDouble;
import lombok.Builder;
import java.awt.*;
import java.util.Optional;

@Builder
record PendulumParams(
        ExperiencePendulum exp,
        EnvironmentPendulum env,
        Color colorBackground,
        double midx,
        double bottomy,
        int xmax,  //cm
        int ymax,  //cm
        int width,
        int height,
        double armLenght,
        double armThikness,
        Color armColor,
        double jointWidth,
        double jointheight,
        double jointThickness,
        Color jointColor,
        Color torqueColor,
        double torqueThikness
) {


    public static PendulumParams create(ExperiencePendulum exp, EnvironmentPendulum env) {
        Optional<EnvironmentPendulum> optionalEnv = Optional.ofNullable(env);
        return PendulumParams.builder()
                .exp(exp)  //.dx(dx)
                .colorBackground(Color.BLACK)
                .midx(0).bottomy(0)
                .xmax(50).ymax(110)
                .armLenght((optionalEnv.map(e -> e.getParameters().length() * 100)
                        .orElse(0.0)))
                .armThikness(2).armColor(Color.WHITE)
                .jointWidth(5).jointheight(10.0)
                .jointThickness(15).jointColor(Color.darkGray)
                .torqueColor(Color.RED).torqueThikness(2)
                .build();
    }

    public static PendulumParams empty() {
        return create(null,null);
    }


    public PosXyDouble bottomArmXy() {
        return PosXyDouble.of(midx,bottomy+jointheight/2);
    }

    public PosXyDouble topArmXy() {
        double angle = exp.state().angle();
        double x=armLenght*Math.sin(angle);
        double y=armLenght*Math.cos(angle);
        return PosXyDouble.of(x,y);
    }

    public PosXyDouble jointTop() {
        return PosXyDouble.of(midx, jointheight);
    }

    public PosXyDouble jointLeft() {
        return PosXyDouble.of(-jointWidth, bottomy);
    }

    public PosXyDouble jointRight() {
        return PosXyDouble.of(jointWidth, bottomy);
    }

}
