package ch14;// TimeClient.java

import chapters.ch14.pong_animation.PongGraphicsViewer;
import chapters.ch14.factory.FactoryPongSettings;
import core.foundation.config.ConfigFactory;
import core.foundation.configOld.ProjectPropertiesReader;

public class RunnerPongViewer {
    public static void main(String[] args) throws Exception {
        var settings = FactoryPongSettings.create();
        var animationConfig= ConfigFactory.getAnimationConfig();
        PongGraphicsViewer.of(animationConfig,settings);
    }
}
