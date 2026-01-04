package chapters.ch14.environments.pong_graphics;

import chapters.ch14.environments.pong.PongSettings;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import javax.swing.*;
import java.awt.*;


/***
 * Creates the graphical view of the pong game
 */

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PongFrameCreator {

    static final int WEIGHT_PANEL = 300;
    static final int HEIGHT_PANEL = 200;
    static final String FRAME_TITLE = "Pong";

    final PongPanel panel;

    public static PongFrameCreator of(PongSettings settings) {
        return new PongFrameCreator(createPanel(settings));
    }

    public JFrame createFrame() {
        return createFrame(panel);
    }

    private static PongPanel createPanel(PongSettings settings) {
        return PongPanel.of(getPanelPropertiesInfo());
    }

    private static PanelProperties getPanelPropertiesInfo() {
        return new PanelProperties(Color.DARK_GRAY, WEIGHT_PANEL, HEIGHT_PANEL);
    }

    JFrame createFrame(PongPanel panel) {
        JFrame frame = new JFrame(FRAME_TITLE);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        return frame;
    }



}
