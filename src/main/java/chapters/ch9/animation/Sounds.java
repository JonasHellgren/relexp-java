package chapters.ch9.animation;

import lombok.SneakyThrows;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public record Sounds(Clip coinSound) {

    @SneakyThrows
    public static Sounds of() {
        File file = new File("src/main/java/chapters/ch9/animation/coin.wav");
        Clip clip = AudioSystem.getClip();
        clip.open(AudioSystem.getAudioInputStream(file));
        return new Sounds(clip);
    }

    public void playCoin() {
        coinSound().start();
        coinSound().setFramePosition(0);
    }
}
