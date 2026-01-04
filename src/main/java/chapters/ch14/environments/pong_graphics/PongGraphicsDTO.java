package chapters.ch14.environments.pong_graphics;

import chapters.ch14.environments.pong.StatePong;

import java.io.Serializable;

/**
 * This class represents the graphical state of the Pong game.
 * It stores the position of the paddle and the ball, as well as some additional information.
 * It also provides methods to create instances of this class from a game state.
 */
public record PongGraphicsDTO(
        double xPaddle,
        double yPaddle,
        double xBall,
        double yBall,
        boolean failState,
        boolean failSearch,
        boolean isStepExceed,
        String title

) implements Serializable {

    public  static  PongGraphicsDTO of(StatePong state) {
        return of(state,false,false,false,"");
    }
    public static  PongGraphicsDTO of(StatePong state,
                                     boolean isFail,
                                     boolean isFailSearch,
                                     boolean isStepExceed,
                                     String title) {
        return new PongGraphicsDTO(state.posPaddle().x(),
                state.posPaddle().y(),
                state.posBall().x(),
                state.posBall().y(),
                isFail,
                isFailSearch,
                isStepExceed,
                title);
    }

    public static PongGraphicsDTO empty() {
        return of(StatePong.empty(),false,false,false,"");
    }
}
