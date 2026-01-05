package core.foundation.config;


import com.google.common.base.Preconditions;
import lombok.extern.java.Log;

import java.util.Properties;

@Log
public record PathPicsConfig(
        String ch2,
        String ch3,
        String ch4,
        String ch5,
        String ch6,
        String ch7,
        String ch8,
        String ch9,
        String ch10,
        String ch11,
        String ch12,
        String ch13,
        String ch14
) {

    public static PathPicsConfig extract(Properties props) {
        Preconditions.checkArgument(props != null, "properties is null");
        return  PathPicsConfig.extract(new ConfigReader(props));
    }

    public static PathPicsConfig extract(ConfigReader r) {
        return new PathPicsConfig(
                r.requireStr("pathpic.ch2"),
                r.requireStr("pathpic.ch3"),
                r.requireStr("pathpic.ch4"),
                r.requireStr("pathpic.ch5"),
                r.requireStr("pathpic.ch6"),
                r.requireStr("pathpic.ch7"),
                r.requireStr("pathpic.ch8"),
                r.requireStr("pathpic.ch9"),
                r.requireStr("pathpic.ch10"),
                r.requireStr("pathpic.ch11"),
                r.requireStr("pathpic.ch12"),
                r.requireStr("pathpic.ch13"),
                r.requireStr("pathpic.ch14")
        );
    }

}
