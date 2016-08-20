package pe.joedayz.campus.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * Created by MATRIX-JAVA on 14/5/2016.
 */
public class ExcelUtils {

    static Logger LOG = LoggerFactory.getLogger(ExcelUtils.class);

    public static String getDataStringCell(Cell cell) {
        return getDataStringCell(cell, null);
    }

    public static String getDataStringCell(Cell cell, String formato) {
        return getDataStringCell(cell, formato, null);
    }

    public static String getDataStringCell(Cell cell, String formato, Integer size) {
        String dataCell = null;
        if (cell == null) return null;
        if (cell.getCellType() == Cell.CELL_TYPE_BLANK) return null;

        if (cell.getCellType() == (Cell.CELL_TYPE_STRING)) {
            if (!StringUtils.hasText(cell.getStringCellValue())) return null;

            if (StringUtils.hasText(formato)) {
                DecimalFormat destinoPattern = new DecimalFormat(formato);
                try {
                    Number fecha = destinoPattern.parse(cell.getStringCellValue().trim());
                    dataCell = destinoPattern.format(fecha);
                } catch (ParseException e) {
                    throw new RuntimeException("format incorrect " + cell.getStringCellValue() + " format:" + formato);
                }
            } else {
                dataCell = cell.getStringCellValue().trim();
            }

        } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {

            if (StringUtils.hasText(formato)) {
                DecimalFormat pattern = new DecimalFormat(formato);
                dataCell = pattern.format(cell.getNumericCellValue());
            } else {
                dataCell = String.valueOf((cell.getNumericCellValue()));
            }

        }

        if (size != null && StringUtils.hasText(dataCell)) {
            if (dataCell.length() > size) throw new RuntimeException("Error el length");
        }
        return dataCell;
    }

    public static String getDataIntegerCell(Cell cell, String formato, Integer size) {
        String dataCell = null;
        if (cell == null) return null;
        if (cell.getCellType() == Cell.CELL_TYPE_BLANK) return null;

        if (cell.getCellType() == (Cell.CELL_TYPE_STRING)) {
            if (!StringUtils.hasText(cell.getStringCellValue())) return null;

            if (NumberUtil.isInteger(new BigDecimal(cell.getStringCellValue().trim()))) {
                dataCell= cell.getStringCellValue().trim();
            }else{
                throw new RuntimeException("format incorrect " + cell.getStringCellValue() + " ,only digits");
            }

        } else {
           if (NumberUtil.isInteger(new BigDecimal(cell.getNumericCellValue()))) {
                dataCell= new DecimalFormat("#####").format(cell.getNumericCellValue());
            }else{
               throw new RuntimeException("format incorrect " + cell.getStringCellValue() + " ,only digits");
           }
        }

        if(StringUtils.hasText(dataCell)){
            if (size != null ) {
                if (dataCell.length() != size) throw new RuntimeException("Error el length");
            }

        }

        return dataCell;
    }

    public static Date getDateCell(Cell cell, String formato) {
        Date dataCell = null;
        if (cell == null) return null;
        if (cell.getCellType() == Cell.CELL_TYPE_BLANK) return null;
        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            throw new RuntimeException("format incorrect  by date" + cell.getNumericCellValue() + " format is:" + formato);
        }


        if (cell.getCellType() == (Cell.CELL_TYPE_STRING)) {
            if (!StringUtils.hasText(cell.getStringCellValue())) return null;

            if (StringUtils.hasText(formato)) {
                SimpleDateFormat sdf = new SimpleDateFormat(formato);
                try {
                    dataCell = sdf.parse(cell.getStringCellValue().trim());
                } catch (ParseException e) {
                    throw new RuntimeException("format incorrect  by date" + cell.getStringCellValue() + " format is:" + formato);
                }
            }
        }else {
            dataCell = cell.getDateCellValue();
        }
        return dataCell;
    }

    public static BigDecimal getBigDecimalCell(Cell cell) {
        BigDecimal dataCell = null;
        if (cell == null) return dataCell;
        if (cell.getCellType() == Cell.CELL_TYPE_BLANK) return dataCell;


        if (cell.getCellType() == (Cell.CELL_TYPE_STRING)) {
            if (!StringUtils.hasText(cell.getStringCellValue())) return null;
            try {
                dataCell = new BigDecimal(cell.getStringCellValue());
            } catch (Exception e) {
                LOG.error("Error in parse number " , e);
                throw new RuntimeException("Error in parse number");
            }
        } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            dataCell = new BigDecimal(cell.getNumericCellValue());

        }
        return dataCell;
    }

 
}
