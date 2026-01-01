package core.foundation.util.formatting;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class NumberFormatterUtil {

    private NumberFormatterUtil() {
    }

    public static final DecimalFormatSymbols SYMBOLS = new DecimalFormatSymbols(Locale.US); //US <=> only dots
    public static final DecimalFormat formatterOneDigit = new DecimalFormat("#.#", SYMBOLS);
    public static final DecimalFormat formatterTwoDigits = new DecimalFormat("#.##", SYMBOLS);
    public static final DecimalFormat formatterThreeDigits = new DecimalFormat("#.###", SYMBOLS);

    public static String getRoundedNumberAsString(Double value, int nofDigits)  {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);  //ENGLISH => point
        nf.setMaximumFractionDigits(nofDigits);
        return nf.format(value);
    }


    public static double roundTo2Decimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public static double roundTo1Decimals(double value) {
        return Math.round(value * 10.0) / 10.0;
    }

    public static double roundToNDecimals(double value, int n) {
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(n, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
