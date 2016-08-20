package pe.joedayz.campus.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by awusr on 14/05/2016.
 */
public class DateUtil {
    private static DateFormat formatFullDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    private static DateFormat formatShortDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    public static String formatShorDate(Date date){

        if(date==null){
            return "";
        }

        return formatShortDate.format(date);
    }

}
