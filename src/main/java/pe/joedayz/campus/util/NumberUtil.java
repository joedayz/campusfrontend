package pe.joedayz.campus.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Created by awusr on 12/05/2016.
 */
public class NumberUtil {

    private static DecimalFormat moneyFormat = new DecimalFormat("$#,##0.00;$-#,##0.00");
    private static DecimalFormat percentageFormat = new DecimalFormat("#0.00%");


    public static String formatMoney( BigDecimal value ) {
        if(value==null){
            return "";
        }
        moneyFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));

        String output = moneyFormat.format(value);
        return output;
    }

    public static String formatPercentage( BigDecimal value ) {
        if(value==null){
            return "";
        }
        percentageFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
        String output = percentageFormat.format(value);
        return output;
    }

    public static boolean isInteger(BigDecimal value ) {
        if(value==null){
            return true;
        }

        return Math.round(value.doubleValue())==value.doubleValue();
    }

    public static BigDecimal nvl(BigDecimal value ) {
        if(value==null){
            return BigDecimal.ZERO;
        }

        return value;
    }

}
