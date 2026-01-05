package core.foundation.configOld;

import lombok.NonNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {

    /**
     * Retrieves properties from a file specified by the given path and name.
     *
     * @param path the directory path to the properties file
     * @param name the name of the properties file
     * @return a Properties object containing the key-value pairs from the file
     * @throws IOException if an I/O error occurs while reading the file
     */
    public static  Properties getProperties(@NonNull String path, @NonNull String name) throws IOException {

        Properties prop=new Properties();
        FileInputStream ip= new FileInputStream(path+name);
        prop.load(ip);
        return prop;
    }

}
