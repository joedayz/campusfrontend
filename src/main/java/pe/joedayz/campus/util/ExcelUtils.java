package pe.joedayz.campus.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

                dataCell = cell.getStringCellValue().trim();

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
                dataCell = cell.getStringCellValue().trim();
            } else {
                throw new RuntimeException("format incorrect " + cell.getStringCellValue() + " ,only digits");
            }

        } else {
           if (NumberUtil.isInteger(new BigDecimal(cell.getNumericCellValue()))) {
                dataCell = new DecimalFormat("#####").format(cell.getNumericCellValue());
            } else {
               throw new RuntimeException("format incorrect " + cell.getStringCellValue() + " ,only digits");
           }
        }

        if (StringUtils.hasText(dataCell)) {
            if (size != null) {
                if (dataCell.length() != size) throw new RuntimeException("Error el length");
            }

        }

        return dataCell;
    }

    public static Long getDataLongCell(Cell cell) {
        BigDecimal bigDecimal = getDataBigDecimalCell(cell);
        return bigDecimal == null ? null : bigDecimal.longValue();
    }

    public static BigDecimal getDataBigDecimalCell(Cell cell) {
        return getDataBigDecimalCellIntern(cell, true);
    }

    public static BigDecimal getDataBigDecimalAllowDecimalCell(Cell cell) {
        return getDataBigDecimalCellIntern(cell, false);
    }

    private static BigDecimal getDataBigDecimalCellIntern(Cell cell, boolean validateInteger) {
        BigDecimal dataCell = null;
        if (cell == null) return null;
        if (cell.getCellType() == Cell.CELL_TYPE_BLANK) return null;

        if (cell.getCellType() == (Cell.CELL_TYPE_STRING)) {
            if (!StringUtils.hasText(cell.getStringCellValue())) return null;

            if (NumberUtil.isInteger(new BigDecimal(cell.getStringCellValue().trim()))) {
                dataCell = new BigDecimal(cell.getStringCellValue().trim());
            } else {
                throw new RuntimeException("format incorrect " + cell.getStringCellValue() + ", please use only digits");
            }

        } else {
            BigDecimal value = new BigDecimal(cell.getNumericCellValue());
            if (validateInteger && !NumberUtil.isInteger(value)) {
                throw new RuntimeException("format incorrect " + getData(cell) + ", please use only digits");
            } else {
                dataCell = value;
            }
        }
        return dataCell;
    }

    public static Date getDateCell(Cell cell, String formato) {
        Date dataCell = null;
        try {
            dataCell = cell.getDateCellValue();
        } catch (Throwable e2) {
        if (cell == null) return null;
        if (cell.getCellType() == Cell.CELL_TYPE_BLANK) return null;
        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                throw new RuntimeException("format incorrect by date " + cell.getNumericCellValue() + ", correct format is:" + formato);
        }

        if (cell.getCellType() == (Cell.CELL_TYPE_STRING)) {
            if (!StringUtils.hasText(cell.getStringCellValue())) return null;

            if (StringUtils.hasText(formato)) {
                SimpleDateFormat sdf = new SimpleDateFormat(formato);
                try {
                    dataCell = sdf.parse(cell.getStringCellValue().trim());
                } catch (ParseException e) {
                        throw new RuntimeException("format incorrect  by date " + getData(cell) + ", correct format is:" + formato);
                }
            }
            } else {
                //dataCell = cell.getDateCellValue();
                throw new RuntimeException("format incorrect  by date " + getData(cell) + ", correct format is:" + formato);

            }

        }

        return dataCell;
    }

    public static BigDecimal getBigDecimalCell(Cell cell, int integer, int decimal) {
        BigDecimal data = getBigDecimalCell(cell);

        if (data == null) return null;

        if (String.valueOf(data.intValue()).length() > integer) {
            throw new RuntimeException("Number exceed the " + integer + " integer digits");
        }
        if (!NumberUtil.isDouble(data, decimal)) {
            throw new RuntimeException("Number exceed the " + decimal + " decimal digits");
        }
        return data;
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
                LOG.error("Error in parse number ", e);
                throw new RuntimeException("Error in parse number");
            }
        } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            dataCell = new BigDecimal(cell.getNumericCellValue());

        }
        return dataCell;
    }


    public static BigDecimal getBigDecimalCellDouble(Cell cell) {
        BigDecimal dataCell = null;
        if (cell == null) return dataCell;
        if (cell.getCellType() == Cell.CELL_TYPE_BLANK) return dataCell;


        if (cell.getCellType() == (Cell.CELL_TYPE_STRING)) {
            if (!StringUtils.hasText(cell.getStringCellValue())) return null;
            try {
                dataCell = new BigDecimal(cell.getStringCellValue());
            } catch (Exception e) {
                LOG.error("Error in parse number ", e);
                throw new RuntimeException("Error in parse number");
            }
        } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            dataCell = new BigDecimal("" + cell.getNumericCellValue());

        }
        return dataCell;
    }

    public static BigDecimal getPorcentaje(Cell cell, int digitDecimal) {
        BigDecimal dataCell = null;
        if (cell == null) return dataCell;
        if (cell.getCellType() == Cell.CELL_TYPE_BLANK) return dataCell;


        if (cell.getCellType() == (Cell.CELL_TYPE_STRING)) {
            if (!StringUtils.hasText(cell.getStringCellValue())) return null;
            try {
                if (NumberUtil.isPercentage(new BigDecimal(cell.getStringCellValue()))) {
                    dataCell = new BigDecimal(cell.getStringCellValue());
                } else {
                    throw new RuntimeException("format incorrect " + cell.getStringCellValue() + " ,only percentage");
                }
            } catch (Exception e) {
                LOG.error("Error in parse number ", e);
                throw new RuntimeException("Error in parse number");
            }
        } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            if (NumberUtil.isPercentage(new BigDecimal(cell.getNumericCellValue()))) {
                dataCell = new BigDecimal(cell.getNumericCellValue());
            } else {
                throw new RuntimeException("format incorrect " + cell.getNumericCellValue() + " ,only percentage");
            }
        }
        if (digitDecimal != -1) {
            if (!NumberUtil.isDouble(dataCell, digitDecimal)) {
                throw new RuntimeException("format incorrect " + dataCell + " ,only " + digitDecimal + " digits");
            }
        }
        return dataCell;
    }


    public static boolean IsBlank(Cell cell) {

        if (cell == null) return true;
        if (cell.getCellType() == Cell.CELL_TYPE_BLANK) return true;

        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            if (cell == null) {
                return true;
            } else {
                return false;
            }
        }
        if (StringUtils.isEmpty(cell.getStringCellValue().trim())) {
            return true;
        }
        return false;
    }


    public static String getData(Cell cell) {
        if (cell==null) return null;
        int cellType = cell.getCellType();

        switch (cellType) {
            case Cell.CELL_TYPE_BLANK:
                return "";
            case Cell.CELL_TYPE_BOOLEAN:
                return cell.getBooleanCellValue() ? "TRUE" : "FALSE";
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
            case Cell.CELL_TYPE_NUMERIC:
                return Double.toString(cell.getNumericCellValue());
            case Cell.CELL_TYPE_ERROR:
                byte errVal = cell.getErrorCellValue();
                return FormulaError.forInt(errVal).getString();
            case Cell.CELL_TYPE_FORMULA:
                return "";
            default:
                throw new IllegalStateException("Unexpected cell type (" + cellType + ")");
        }
    }
}
