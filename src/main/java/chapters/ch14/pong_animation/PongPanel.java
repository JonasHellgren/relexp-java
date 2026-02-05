package chapters.ch14.pong_animation;

import lombok.Getter;
import javax.swing.*;
import java.awt.*;

/***
 * Panel for pong game, with a ball and a paddle.
 */

@Getter
public class PongPanel extends JPanel {

    public static final int DUMMY_SIZE = 10;
    public static final int DUMMY_POS = 10;
    public static final Color COLOR_BALL = Color.WHITE;
    public static final Color COLOR_PADDLE = Color.WHITE;

    JLabel labelBall;
    JPanel paddlePanel;
    PanelProperties panelProperties;

    public static PongPanel of(PanelProperties panelProperties) {
        return new PongPanel(panelProperties);
    }

    public PongPanel(PanelProperties panelProperties) {
        this.panelProperties = panelProperties;
        this.setBackground(panelProperties.color());
        this.setPreferredSize(new Dimension(panelProperties.widthPanel(), panelProperties.heightPanel()));
        labelBall = new JLabel("o");
        labelBall.setBounds(DUMMY_POS, DUMMY_POS, DUMMY_SIZE, DUMMY_SIZE);
        labelBall.setForeground(COLOR_BALL); // Set the ball color
        labelBall.setVisible(true);
        this.add(labelBall);

        paddlePanel=new JPanel();
        paddlePanel.setBounds(DUMMY_POS, DUMMY_POS,DUMMY_SIZE, DUMMY_SIZE);
        paddlePanel.setBackground(COLOR_PADDLE); // Set the paddle color
        paddlePanel.setVisible(true);
        this.add(paddlePanel);
    }


}
