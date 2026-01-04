package chapters.ch14.environments.pong_graphics;

import chapters.ch14.environments.pong.PongSettings;
import core.foundation.config.ProjectPropertiesReader;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * This class represents a viewer for a Pong game. It establishes a socket connection with the game server,
 * reads graphics data from the server, and updates the game view accordingly.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Log
public class PongGraphicsViewer {

    private int port;
    private PongPanelsContentSetter panelsContentSetter;
    private JFrame frame;

    @SneakyThrows
    public static PongGraphicsViewer of(ProjectPropertiesReader reader, PongSettings settings) {
        var viewer = new PongGraphicsViewer(
                reader.socketPort(),
                null, null);
        viewer.initFrame(settings);
        viewer.startThread(settings);
        return viewer;
    }

    private void initFrame(PongSettings settings) {
        var creator = PongFrameCreator.of(settings);
        frame = creator.createFrame();
        panelsContentSetter = PongPanelsContentSetter.of(creator, settings);
    }

    private void startThread(PongSettings settings) throws IOException, ClassNotFoundException {
        log.info("Starting viewer thread");
        try (
                var socket = new Socket("localhost", port);
        ) {
            var in = new ObjectInputStream(socket.getInputStream());
            while (true) {
                var gfxDTO = (PongGraphicsDTO) in.readObject();
                SwingUtilities.invokeLater(() -> {
                            panelsContentSetter.setPanelContent(gfxDTO, settings);
                            frame.setTitle(gfxDTO.title());
                        }
                );
            }
        }
    }

}
