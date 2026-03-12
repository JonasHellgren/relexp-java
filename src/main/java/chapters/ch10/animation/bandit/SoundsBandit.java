package chapters.ch10.animation.bandit;

import lombok.SneakyThrows;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public record SoundsBandit(Clip coinSound) {

    @SneakyThrows
    public static SoundsBandit create() {
        File file = new File("src/main/java/chapters/ch10/animation/bandit/coin.wav");
        Clip clip = AudioSystem.getClip();
        clip.open(AudioSystem.getAudioInputStream(file));
        return new SoundsBandit(clip);
    }

    public void playCoin() {
        coinSound().start();
        coinSound().setFramePosition(0);
    }
}
