package Game;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

class NumberFormater {

    // Format big numbers into more readable ones
    static String formatNumber(int number) {
        Locale locale  = new Locale("en", "US");
        String pattern = "###,###.###";
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(locale);
        decimalFormat.applyPattern(pattern);
        return (decimalFormat.format(number));
    }
}
