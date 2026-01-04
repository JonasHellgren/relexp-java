package chapters.ch14.environments.pong_graphics;

import chapters.ch14.environments.pong.PongSettings;
import core.foundation.gadget.math.ScalerLinear;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import java.awt.*;

/***
 * Subordinate class to GraphicsViewer. Pastes content of GraphicsDTO into panels.
 */

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Log
public class PongPanelsContentSetter {

    public static final int MIN_BALL_RADIUS = 10;
    PongPanel panel;
    ScalerLinear scalerX, scalerY;


    public static PongPanelsContentSetter of(PongFrameCreator creator, PongSettings settings) {
        var panel = creator.panel;
        var props = panel.getPanelProperties();
        return new PongPanelsContentSetter(panel,
                ScalerLinear.of(0,settings.xMax(),0, props.widthPanel()),
                ScalerLinear.of(0,settings.yMax(), props.heightPanel(),0));  //panel is upside down, y=0 is top
    }


    public void setPanelContent(PongGraphicsDTO gfxDTO, PongSettings settings) {
        setBallPos(gfxDTO, settings);
        setPaddlePos(gfxDTO, settings);
        var color=gfxDTO.failState() || gfxDTO.failSearch() ? Color.RED : Color.BLACK;
        color = gfxDTO.isStepExceed() ? Color.GREEN : color;
        panel.setBackground(color);
        panel.repaint();
    }

    private void setPaddlePos(PongGraphicsDTO gfxDTO, PongSettings settings) {
        int baryPos=(int) scalerY.calcOutDouble(0.01);
        int barX1=(int) scalerX.calcOutDouble(gfxDTO.xPaddle()- settings.halfPaddleLength());
        int barX2=(int) scalerX.calcOutDouble(gfxDTO.xPaddle()+ settings.halfPaddleLength());
        panel.paddlePanel.setBounds(barX1,baryPos, barX2-barX1, MIN_BALL_RADIUS);
    }

    private void setBallPos(PongGraphicsDTO gfxDTO, PongSettings settings) {
        int panelX= (int) scalerX.calcOutDouble(gfxDTO.xBall());
        int panelY= (int) scalerY.calcOutDouble(gfxDTO.yBall());
        int panelBallRadius= Math.max(MIN_BALL_RADIUS,(int) scalerX.calcOutDouble(settings.radiusBall()));
        panel.labelBall.setBounds(panelX, panelY,panelBallRadius,panelBallRadius);
    }


}
