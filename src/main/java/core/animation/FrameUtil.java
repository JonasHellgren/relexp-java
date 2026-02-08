package core.animation;

import lombok.experimental.UtilityClass;

import javax.swing.*;

@UtilityClass
public class FrameUtil {

    public static void showFrame(JPanel rootPanel,
                                 int frameWidth,
                                 int frameHeight,
                                 int xLocation,
                                 int yLocation) {
        var frame = new JFrame();
        frame.add(rootPanel);
        frame.setSize(frameWidth, frameHeight);
        frame.setUndecorated(true);
        frame.setLocation(xLocation, yLocation);
        frame.setResizable(true);
        frame.setVisible(true);
    }

}
