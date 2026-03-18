
package chapters.ch12.animation;

import core.foundation.util.math.MathUtil;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public record SoundsPendulum(Clip niceLand) {

    public static final String PATH = "src/main/java/chapters/ch12/animation/";

    @SneakyThrows
    public static SoundsPendulum create() {
        return new SoundsPendulum(getClip("metal-pipe.wav"));
    }

    @NotNull
    private static Clip getClip(String s) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        File file = new File(PATH + s);
        Clip fire = AudioSystem.getClip();
        fire.open(AudioSystem.getAudioInputStream(file));
        return fire;
    }

    public void playFail() {
        play(niceLand());
    }


    private void play(Clip clip) {
        clip.setMicrosecondPosition(0);
        clip.start();
    }

}
