package chapters.ch14.pong_animation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;

/***
 * Sending GraphicsDTO objects to the GraphicsViewer to enable animation
 * Sender thread continuously sends GraphicsDTO objects
 * So client class only need to bother about updating the field gfxDTO
 *
 */
@Log
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PongGraphicsServer {

    private long sleepTimeAnimationMs;
    private int socketPort;
    @Setter
    private PongGraphicsDTO gfxDTO;

    public static PongGraphicsServer of(long sleepTimeAnimationMs, int socketPort) {
        var server = new PongGraphicsServer(sleepTimeAnimationMs, socketPort, PongGraphicsDTO.empty());
        server.init();
        return server;
    }

    @SneakyThrows
    private void init() {
        var out = getOutputStream();
        var senderThread = getSenderThread(out);
        senderThread.setDaemon(true);
        senderThread.start();
    }

    private Thread getSenderThread(ObjectOutputStream out) {
        return new Thread(() -> {
            try {
                while (true) {
                    out.writeObject(gfxDTO);
                    out.flush();
                    Thread.sleep(sleepTimeAnimationMs);
                }
            } catch (IOException e) {
                log.info("TimeSender stopped: " + e.getMessage());
                throw new SenderThreadException(e);
            } catch (InterruptedException e) {
                log.info("Thread sleep interrupted: " + e.getMessage());
                throw new SenderThreadException(e);
            }
        });
    }

    private static class SenderThreadException extends RuntimeException {
        public SenderThreadException(Throwable cause) {
            super(cause);
        }
    }

    private ObjectOutputStream getOutputStream() throws IOException {
        log.info("Server waiting for client...");
        try (
                var serverSocket = new ServerSocket(socketPort);
        ) {
            var clientSocket = serverSocket.accept();
            log.info("Client connected.");
            return new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            log.info("Server failed starting: " + e.getMessage());
            throw new IOException(e);
        }
    }


}
