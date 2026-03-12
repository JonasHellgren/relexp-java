
package chapters.ch10.animation.cannon;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public record SoundsCannon(Clip fire, Clip hit, Clip splat) {

    public static final String PATH = "src/main/java/chapters/ch10/animation/cannon/";

    @SneakyThrows
    public static SoundsCannon create() {
        return new SoundsCannon(
                getClip("cannon-shot.wav"),
                getClip("hitHouse.wav"),
                getClip("splat.wav"));
    }

    @NotNull
    private static Clip getClip(String s) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        File file = new File(PATH + s);
        Clip fire = AudioSystem.getClip();
        fire.open(AudioSystem.getAudioInputStream(file));
        return fire;
    }

    public void playFire() {
        play(fire());
    }

    public void playHit() {
        play(hit());
    }

    public void playSplat() {
        play(splat());
    }

    private void play(Clip clip) {
        clip.setFramePosition(0);
        clip.start();
    }

}
