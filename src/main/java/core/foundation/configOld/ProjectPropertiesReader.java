package core.foundation.configOld;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.io.IOException;
import java.util.Properties;


/***
 * 2Col - two pics side by side in doc, 1Col - standalone pic
 */

@AllArgsConstructor
@Getter
public class ProjectPropertiesReader {

    static final String PROP_PATH = "src/main/resources/";
    static final String PROP_NAME = "config.properties";

     Properties properties;

    public static ProjectPropertiesReader create() throws IOException {
        return new ProjectPropertiesReader(createProperties());
    }

    public static Properties createProperties() throws IOException {
        return PropertiesReader.getProperties(PROP_PATH, PROP_NAME);
    }

    public String getStringProperty(String key) {
        return properties.getProperty(key);
    }

    public Integer xyChartWidth2Col() {
        return Integer.parseInt(properties.getProperty("xyChartWidth2Col"));
    }

    public Integer xyChartWidth3Col() {
        return Integer.parseInt(properties.getProperty("xyChartWidth3Col"));
    }

    public Integer xyChartWidth1Col() {
        return Integer.parseInt(properties.getProperty("xyChartWidth1Col"));
    }

    public Integer xyChartHeight() {
        return Integer.parseInt(properties.getProperty("XYChartHeight"));
    }

    public int xyChartWidthSameAsHeight() {
        return xyChartHeight();
    }

    public Integer frameWidth2Col() {
        return Integer.parseInt(properties.getProperty("frameWidth2Col"));
    }

    public Integer frameWidthSameAsHeight() {
        return frameHeight();
    }

    public Integer frameWidth1Col() {
        return Integer.parseInt(properties.getProperty("frameWidth1Col"));
    }

    public Integer frameHeight() {
        return Integer.parseInt(properties.getProperty("FrameHeight"));
    }

    public String pathConceptsPics() {
        return properties.getProperty("ch1_pics");
    }

    public String pathTempDiff() {
        return properties.getProperty("ch4_pics");
    }

    public String pathActorCriticPics() {
        return properties.getProperty("ch11_pics");
    }

    public String pathGradientLearning() {
        return properties.getProperty("ch9_pics");
    }

    public String pathRbf() {
        return properties.getProperty("ch9_pics");
    }

    public String pathMonteCarlo() {
        return properties.getProperty("ch5_pics");
    }

    public String pathMultiStep() {
        return properties.getProperty("ch6_pics");
    }

    public String pathSafe() {
        return properties.getProperty("ch7_pics");
    }

    public String pathPolGrad() {
        return properties.getProperty("ch10_pics");
    }

    public String pathAdvConcepts() {
        return properties.getProperty("ch9_pics");
    }

    public String pathAdvConceptsNeural() {
        return pathAdvConcepts();
    }

    public String pathDeepRl() {
        return properties.getProperty("ch12_pics");
    }

    public String pathNonEpisodic() {
        return properties.getProperty("ch8_pics");
    }

    public String pathMcts() {
        return properties.getProperty("ch13_pics");
    }

    public String pathCombLP() {
        return properties.getProperty("ch14_pics");
    }


    public long sleepTimeAnimationMs() {
        return Long.parseLong(properties.getProperty("sleepTimeAnimationMs"));
    }

    public int socketPort() {
        return Integer.parseInt(properties.getProperty("socketPort"));
    }



    /***
     * gradient_learning_pics= pictures/f_advanced_concepts/gradient_learning/
     * rbf_pics= pictures/f_advanced_concepts/rbf/
     * actor_critic_pics= pictures/h_actor_critic/
     * td_pics= pictures/b_temp_diff/
     * concepts_pics= pictures/a_concepts/
     */

}
