package core.plotting_core.base.shared;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.text.DecimalFormat;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FormattedAsString {


    public static String getFormattedAsString(Double value, String decimalFormat) {
        DecimalFormat df = new DecimalFormat(decimalFormat);
        return df.format(value);
    }

}
