package ch14;// TimeClient.java

import chapters.ch14.environments.pong_graphics.PongGraphicsViewer;
import chapters.ch14.factory.FactoryPongSettings;
import core.foundation.configOld.ProjectPropertiesReader;

public class RunnerPongViewer {
    public static void main(String[] args) throws Exception {
        var settings = FactoryPongSettings.create();
        var propertiesReader= ProjectPropertiesReader.create();
        PongGraphicsViewer.of(propertiesReader,settings);
    }
}
