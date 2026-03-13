
package chapters.ch11.animation;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public record SoundsLunar(Clip niceLand, Clip crash) {

    public static final String PATH = "src/main/java/chapters/ch11/animation/";

    @SneakyThrows
    public static SoundsLunar create() {
        return new SoundsLunar(
                getClip("coin.wav"),
                getClip("crash.wav"));
    }

    @NotNull
    private static Clip getClip(String s) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        File file = new File(PATH + s);
        Clip fire = AudioSystem.getClip();
        fire.open(AudioSystem.getAudioInputStream(file));
        return fire;
    }

    public void playNiceLanding() {
        play(niceLand());
    }

    public void playCrash() {
        play(crash());
    }

    private void play(Clip clip) {
        clip.setFramePosition(0);
        clip.start();
    }

}
