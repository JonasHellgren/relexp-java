package core.plotting_core.chart_plotting;

import com.google.common.base.Preconditions;
import core.foundation.configOld.ProjectPropertiesReader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Enum representing different project folders.
 */
public enum ProjectFoldersKeyInterpreter {
    CONCEPT,
    TD,
    MC,
    MS,
    SAFE,
    AC,
    GL,
    RBF,
    ADVC_NEURAL,
    POLGRAD,
    DEEP_RL
    ;


    /**
     * Returns the path for the given folder key.
     *
     * @param folderKey the folder key
     * @return the path for the folder
     * @throws IOException if an inertia/O error occurs
     */
    public static String getPath(ProjectFoldersKeyInterpreter folderKey) throws IOException {
        var typePathMap = getTypePathMap(ProjectPropertiesReader.create());
        var path = typePathMap.get(folderKey);
        validate(folderKey, typePathMap, path);
        return path;
    }


    /**
     * Returns a map of type to path based on the project properties.
     *
     * @param props the project properties reader
     * @return a map of type to path
     */
    private static Map<ProjectFoldersKeyInterpreter, String> getTypePathMap(ProjectPropertiesReader props) {
        Map<ProjectFoldersKeyInterpreter, String> map = new HashMap<>();
        map.put(ProjectFoldersKeyInterpreter.CONCEPT, props.pathConceptsPics());
        map.put(ProjectFoldersKeyInterpreter.TD, props.pathTempDiff());
        map.put(ProjectFoldersKeyInterpreter.MC, props.pathMonteCarlo());
        map.put(ProjectFoldersKeyInterpreter.MS, props.pathMultiStep());
        map.put(ProjectFoldersKeyInterpreter.SAFE, props.pathSafe());
        map.put(ProjectFoldersKeyInterpreter.AC, props.pathActorCriticPics());
        map.put(ProjectFoldersKeyInterpreter.GL, props.pathGradientLearning());
        map.put(ProjectFoldersKeyInterpreter.RBF, props.pathRbf());
        map.put(ProjectFoldersKeyInterpreter.ADVC_NEURAL, props.pathAdvConceptsNeural());
        map.put(ProjectFoldersKeyInterpreter.POLGRAD, props.pathPolGrad());
        map.put(ProjectFoldersKeyInterpreter.DEEP_RL, props.pathDeepRl());
        return map;
    }

    private static void validate(ProjectFoldersKeyInterpreter folder,
                                 Map<ProjectFoldersKeyInterpreter, String> typePathMap,
                                 String path) {
        Preconditions.checkArgument(typePathMap.containsKey(folder), "Unknown folder: " + folder);
        Preconditions.checkArgument(path != null, "No path for folder: " + folder);
    }

}

