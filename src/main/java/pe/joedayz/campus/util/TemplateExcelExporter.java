package pe.joedayz.campus.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by awusr on 10/05/2016.
 */
public class TemplateExcelExporter {

    public final static String FORMAT_TEXT="@";
    public final static String FORMAT_NUMERIC="###,###,##0";
    public final static String FORMAT_NUMERIC_INTEGER="###,###,##0";
    public final static String FORMAT_NUMERIC_DECIMAL="###,###,##0.00";

//    public interface FormulaExpr {
//        String eval(int rowStart, int rowEnd, int rowIdx, int colIdx, Object ref);
//    }

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    public final static int NO_STYLE = 0;

    public final static int SQUARE_STYLE = 1;
    public final static int GRAY_STYLE = 2;
    public final static int HIDDEN_STYLE = 3;
    public final static int RIGHT_STYLE = 4;
    public final static int LEFT_STYLE = 5;
    public final static int BOTTOM_STYLE = 6;
    public final static int UNDERLINE_STYLE = 7;
    public final static int TOP_RIGHT_STYLE = 8;
    public final static int CENTER_STYLE = 9;
    public final static int DOWN_RIGHT_STYLE = 10;

    private Row row;
    private Sheet sheet;
    private Workbook wb;
    private DataFormat format;

    private CellStyle cellStyle;

    private Font whiteFont;
    private Font boldFont;
    private Font underLineFont;

    CellStyle bottonLinetStyle;
    CellStyle squareCellStyle;


    public void exportH(String fullPath) throws IOException {
        LOG.info("Cargando template:" + fullPath);
        FileInputStream fileInputStream = new FileInputStream(fullPath);
        POIFSFileSystem fsFileSystem = new POIFSFileSystem(fileInputStream);
        wb = new HSSFWorkbook(fsFileSystem);
        init();
    }


    public void exportH(InputStream inputStream) throws IOException {
//        LOG.info("Cargando template:" + fullPath);
//        FileInputStream fileInputStream = new FileInputStream(fullPath);
//        POIFSFileSystem fsFileSystem = new POIFSFileSystem(inputStream);
        wb = new XSSFWorkbook(inputStream);// todo RAC descomentar
//        wb = new HSSFWorkbook(fsFileSystem);
        init();
    }

    public void exportX(String fullPath) throws IOException {
        LOG.info("Cargando template:" + fullPath);
        FileInputStream fileInputStream = new FileInputStream(fullPath);
        //POIFSFileSystem fsFileSystem = new POIFSFileSystem(fileInputStream);
        wb = new XSSFWorkbook(fileInputStream);

        init();
    }

    public void exportX() throws IOException {
        wb = new XSSFWorkbook();
        wb.createSheet();
        init();
    }

    public void exportH() throws IOException {
        wb = new HSSFWorkbook();
        wb.createSheet();
        init();
    }


    private void init() {
        format = wb.createDataFormat();
        sheet = wb.getSheetAt(0);

        underLineFont = wb.createFont();
        underLineFont.setUnderline(Font.U_SINGLE);

        whiteFont = wb.createFont();
        whiteFont.setColor(IndexedColors.WHITE.getIndex());

        boldFont = createFontBold();

        bottonLinetStyle = bottomCellStyle(false, null);
        squareCellStyle = squareCellStyle();
    }

    private Font createFontBold() {
        Font boldFont = wb.createFont();
        boldFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        return boldFont;
    }

//    public void cloneSheet(int index) {
//        sheet = wb.cloneSheet(index);
////        sheet = wb.setSheetName(1, "Hoja ");
//    }

    public void newSheet(String pageName) {
        sheet = wb.createSheet(pageName);
//        sheet = wb.setSheetName(1, "Hoja ");
    }

    public void groupRows(int fromRow, int toRow) {
        sheet.groupRow(fromRow, toRow);
        sheet.setRowGroupCollapsed(fromRow, true);
    }

    public void addRow(int rowIndex) {
        row = sheet.createRow(rowIndex);
    }

    public void setRow(int rowIndex) {
        row = sheet.getRow(rowIndex);
        if (row == null) row = sheet.createRow(rowIndex);
    }

    public void shiftRow(int rowIdx, int rows) {
        sheet.shiftRows(rowIdx, rowIdx + 1, rows, true, false);
    }

    public CellStyle getCellStyle(int rowIndex, int colIndex) {
        Row row = sheet.getRow(rowIndex);
        Cell cell = row.getCell(colIndex);
        if (cell == null) {
            return null;
        } else {
            return cell.getCellStyle();
        }
    }

    public void setCellValue(int rowIndex, int colIndex, String value) {
        Row row = sheet.getRow(rowIndex);
        if (row == null) row = sheet.createRow(rowIndex);
        Cell cell = row.getCell(colIndex);
        if (cell == null) {
            cell = row.createCell(colIndex);
        }
        if (value == null) {
            value = "";
        }

        cell.setCellValue(isXSS() ? new XSSFRichTextString(value) : new HSSFRichTextString(value));
    }

    public void setCellValue(int rowIndex, int colIndex, String value, CellStyle style) {
        Row row = sheet.getRow(rowIndex);

//        if(row==null){
//          row= sheet.createRow(0);
//        }

        Cell cell = row.getCell(colIndex);
        if (cell == null) {
            cell = row.createCell(colIndex);
        }

        cell.setCellStyle(style);
        if (value == null) {
            value = "";
        }

        cell.setCellValue(isXSS() ? new XSSFRichTextString(value) : new HSSFRichTextString(value));
    }


    public void setCellValue(int rowIndex, int colIndex, BigDecimal value, CellStyle style) {
        Row row = sheet.getRow(rowIndex);

        Cell cell = row.getCell(colIndex);
        if (cell == null) {
            cell = row.createCell(colIndex);
        }

        cell.setCellStyle(style);
        if (value != null) {
            cell.setCellValue(value.doubleValue());
        }
    }

    public void getCellValue(int rowIndex, int colIndex, String value) {
//        Row row = sheet.getRow(rowIndex);
//        Cell cell = row.getCell(colIndex);
//        if(cell==null){
//            cell=row.createCell(colIndex);
//        }
//        if(value==null){
//            value="";
//        }
//
//        cell.getCellFormula(new HSSFRichTextString(value));
    }

    public void setCellValue(int rowIndex, int colIndex, BigDecimal value) {
        //String aux=value==null?"":String.valueOf(value);
        //setCellValue(rowIndex,colIndex,value);
        Row row = sheet.getRow(rowIndex);
        Cell cell = row.getCell(colIndex);
        if (cell == null) {
            cell = row.createCell(colIndex);
        }

        if (value != null) {
            cell.setCellValue(value.doubleValue());
        }
        CellStyle styleCell = cell.getCellStyle();
        if (styleCell != null) {
            styleCell.setDataFormat(format.getFormat(FORMAT_NUMERIC));
            cell.setCellStyle(styleCell);
        }
    }

    public void addColumnValue(int index, String value) {
        addColumnValue(index, value, -1, false);
    }

    public void addCellFormula( int colIndex, String formula) {
        Cell cell = row.createCell(colIndex);

        if (cell != null) {
            cell.setCellType(Cell.CELL_TYPE_FORMULA);
            cell.setCellFormula(formula);
        }
    }

    public void addCellFormula(int rowIndex, int colIndex, String formula, CellStyle styleCell) {
        Row row = sheet.getRow(rowIndex);
        Cell cell = row.createCell(colIndex);

        if (styleCell != null) {
            cell.setCellStyle(styleCell);
        }

        if (cell != null) {
            cell.setCellType(Cell.CELL_TYPE_FORMULA);
            cell.setCellFormula(formula);
        }
    }

    public void addCellFormula(int index, String formula, int style, String pattern, boolean bold) {
        Cell cell = row.createCell(index);
        CellStyle styleCell = getStyle(style, null, null, bold);
        if (styleCell != null) {
            styleCell.setDataFormat(format.getFormat(pattern));
            cell.setCellStyle(styleCell);
        }
        cell.setCellType(Cell.CELL_TYPE_FORMULA);
        cell.setCellFormula(formula);
    }


    public void addCellFormula(int index, String formula,CellStyle styleCell  ) {
        Cell cell = row.createCell(index);
//        CellStyle styleCell = getStyle(style, null, null, bold);
        if (styleCell != null) {
            cell.setCellStyle(styleCell);
        }
        cell.setCellType(Cell.CELL_TYPE_FORMULA);
        cell.setCellFormula(formula);
    }

    public void addColumnValue(int index, String value, CellStyle style) {
        Cell cell = row.createCell(index);
        if (value == null) {
            value = "";
        }

        if (style != null) {
            cell.setCellStyle(style);
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }

        cell.setCellValue(isXSS() ? new XSSFRichTextString(value) : new HSSFRichTextString(value));
    }

    public void addColumn2Value(int index, String value, CellStyle style) {
        Cell cell = row.createCell(index);
         if (style != null) {
            cell.setCellStyle(style);
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }
         cell.setCellValue(isXSS() ? new XSSFRichTextString(value) : new HSSFRichTextString(value));
    }


    public void addColumnValue(int index, CellStyle style) {
        Cell cell = row.createCell(index);
       if (style != null) {
            cell.setCellStyle(style);
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }
       // String nullVal=null;
      //  cell.setCellValue(null);
    }




    public void addColumnValue(int index, Date value, CellStyle style) {
        Cell cell = row.createCell(index);

        if (style != null) {
            cell.setCellStyle(style);
        }

        if(value!=null){
            cell.setCellValue(value);
        }

    }

    private boolean isXSS() {
        return wb instanceof XSSFWorkbook;
    }

    public void addColumnValue(int index, String value, int style, boolean bold) {
        addColumnValue(index, value, style, null, null, bold);
    }

    public void addColumnValue(int index, String value, int style, HSSFColor fgColor, HSSFColor bgColor, boolean bold) {
        Cell cell = row.createCell(index);
        if (value == null) {
            value = "";
        }
        CellStyle styleCell = getStyle(style, fgColor, bgColor, bold);
        if (styleCell != null) {
            styleCell.setDataFormat(format.getFormat(FORMAT_TEXT));
            cell.setCellStyle(styleCell);
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }
        cell.setCellValue(isXSS() ? new XSSFRichTextString(value) : new HSSFRichTextString(value));

    }

    public void addColumnValue(int index, BigDecimal value) {
        addColumnValue(index, value, -1, false);
    }

    public void addColumnValue(int index, BigDecimal value, int style, boolean bold) {
        addColumnValue(index, value, style, FORMAT_NUMERIC, bold);
    }

    public void addColumnValue(int colIdxStart, int style, HSSFColor fgColor, HSSFColor bgColor, boolean bold, String... vals) {
        for (int i = 0; i < vals.length; i++) {
            String val = vals[i];
            if (val != null)
                addColumnValue(colIdxStart + i, val, style, fgColor, bgColor, bold);
        }
    }

    public void addColumnValue(int index, BigDecimal value, CellStyle cellStyle) {
        Cell cell = row.createCell(index);

        if (cellStyle != null) {
            cell.setCellStyle(cellStyle);
        }

        if (value != null) {
            cell.setCellValue(value.doubleValue());
        }
    }

    public void addColumnValue(int index, Integer value, CellStyle cellStyle) {
        Cell cell = row.createCell(index);

        if (cellStyle != null) {
            cell.setCellStyle(cellStyle);
        }

        if (value != null) {
            cell.setCellValue(value.doubleValue());
        }
    }

    public void addColumnValue(int index, BigDecimal value, int style, String pattern, boolean bold) {
        addColumnValue(index, value, style, pattern, bold, null, null);
    }

    public void addColumnValue(int index, BigDecimal value, int style, String pattern, boolean bold, HSSFColor fondoColor, HSSFColor fontColor) {
        Cell cell = getColumnCell(index, style, pattern, bold, fondoColor, fontColor);

        if (value != null) {
            cell.setCellValue(value.doubleValue());
        }
    }

    public Cell getColumnCell(int index, int style, String pattern, boolean bold, HSSFColor fondoColor, HSSFColor fontColor) {
        Cell cell = row.createCell(index);
        CellStyle styleCell = getStyle(style, fondoColor, fontColor, bold);

        if (styleCell != null && pattern != null) {
            styleCell.setDataFormat(format.getFormat(pattern));
        }
        if (styleCell != null) {
            cell.setCellStyle(styleCell);
        }
        return cell;
    }

    public void addColumnValue(int index, Long value) {
        addColumnValue(index, value, -1);
    }

    public void addColumnValue(int index, Integer value) {
        addColumnValue(index, value, -1);
    }

    public void addColumnValue(int index, Long value, int style) {
        //String aux=value==null?"":String.valueOf(value);
        addColumnValue(index, BigDecimal.valueOf(value), style, FORMAT_NUMERIC_INTEGER, false);
    }

    public void addColumnValue(int index, Integer value, int style) {
        //String aux=value==null?"":String.valueOf(value);
        addColumnValue(index, BigDecimal.valueOf(value), style, FORMAT_NUMERIC_INTEGER, false);
    }

    public void writeExcelToResponse(HttpServletResponse response, String filename) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Expires", "0");
        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
        response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");
        wb.write(response.getOutputStream());
    }

    public File writeExcelToFile(File file) throws IOException {
        wb.write(new FileOutputStream(file));
        return file;
    }

    public byte[] getExcelAsByteArray() throws IOException{
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        wb.write(byteArrayOutputStream);
        //byteArrayOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    public CellStyle getStyle(int type, String pattern,HSSFColor fondoColor, HSSFColor fontColor, boolean bold) {

        CellStyle style=getStyle(type,fondoColor,fontColor,bold);

        if (style != null && pattern != null) {
            style.setDataFormat(format.getFormat(pattern));
        }

        return style;
    }



    public CellStyle getStyle(int type, HSSFColor fondoColor, HSSFColor fontColor, boolean bold) {
        CellStyle style = null;


        switch (type) {
            case TemplateExcelExporter.NO_STYLE:
                style = getCellNoStyle(bold, fontColor);
                break;
            case TemplateExcelExporter.SQUARE_STYLE:
                style = defaultCellStyle();
                break;
            case TemplateExcelExporter.GRAY_STYLE:
                style = getCellGrayStyle(bold, fontColor);
                break;
            case TemplateExcelExporter.RIGHT_STYLE:
                style = rightCellStyle(bold, fontColor);
                break;
            case TemplateExcelExporter.LEFT_STYLE:
                style = leftCellStyle(bold, fontColor);
                break;
            case TemplateExcelExporter.BOTTOM_STYLE:
                style = bottomCellStyle(bold, fontColor);
                break;
            case TemplateExcelExporter.TOP_RIGHT_STYLE:
                style = topRightCellStyle(bold, fontColor);
            case TemplateExcelExporter.DOWN_RIGHT_STYLE:
                style = downRightCellStyle(bold, fontColor);
                break;
            case TemplateExcelExporter.UNDERLINE_STYLE:
                style = underLineCellStyle(bold, fontColor);
                break;
            case TemplateExcelExporter.HIDDEN_STYLE:
                style = getCellHiddenStyle();
                break;
            case TemplateExcelExporter.CENTER_STYLE:
                style = centerCellStyle(bold, fontColor);
                //style.setFillForegroundColor(new HSSFColor.RED().getIndex());
                break;
            default:
//                throw new IllegalArgumentException();
        }

        if (style!=null && fondoColor != null) {
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            style.setFillForegroundColor(fondoColor.getIndex());
        }

        return style;
    }

    private CellStyle centerCellStyle(boolean bold, HSSFColor color) {
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        addFontIfApply(style, bold, color);
        return style;
    }

    private CellStyle defaultCellStyle() {
        CellStyle style = wb.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
//        Font font=style.getFont(wb);
//        font.setColor(IndexedColors.BLACK.getIndex());
//        style.setFont(font);
        return style;
    }

    private CellStyle rightCellStyle(boolean bold, HSSFColor color) {
        CellStyle style = wb.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_NONE);
        style.setBorderTop(HSSFCellStyle.BORDER_NONE);
        style.setBorderLeft(HSSFCellStyle.BORDER_NONE);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);

//        Font font=style.getFont(wb);
//        font.setColor(IndexedColors.BLACK.getIndex());
//        style.setFont(font);

        addFontIfApply(style, bold, color);

        return style;
    }

    private CellStyle leftCellStyle(boolean bold, HSSFColor color) {
        CellStyle style = wb.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_NONE);
        style.setBorderTop(HSSFCellStyle.BORDER_NONE);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_NONE);

//        Font font=style.getFont(wb);
//        font.setColor(IndexedColors.BLACK.getIndex());
//        style.setFont(font);


        addFontIfApply(style, bold, color);


        return style;
    }

    private CellStyle bottomCellStyle(boolean bold, HSSFColor color) {
        CellStyle style = wb.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_NONE);
        style.setBorderLeft(HSSFCellStyle.BORDER_NONE);
        style.setBorderRight(HSSFCellStyle.BORDER_NONE);
//        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        addFontIfApply(style, bold, color);


        return style;
    }

    public CellStyle squareCellStyle(){
        CellStyle style = wb.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);

        return style;
    }
    public CellStyle topCellStyle(String pattern, boolean bold, HSSFColor color) {
        CellStyle style = wb.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_NONE);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_NONE);
        style.setBorderRight(HSSFCellStyle.BORDER_NONE);

        if (style != null && pattern != null) {
            style.setDataFormat(format.getFormat(pattern));
        }


        addFontIfApply(style, bold, color);


        return style;
    }

    private CellStyle topRightCellStyle(boolean bold, HSSFColor color) {
        CellStyle style = wb.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_NONE);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_NONE);
        style.setBorderRight(HSSFCellStyle.BORDER_NONE);

        style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);


        addFontIfApply(style, bold, color);


        return style;
    }

    private CellStyle downRightCellStyle(boolean bold, HSSFColor color) {
        CellStyle style = wb.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_NONE);
        style.setBorderLeft(HSSFCellStyle.BORDER_NONE);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);

        style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);


        addFontIfApply(style, bold, color);


        return style;
    }

    private CellStyle underLineCellStyle(boolean bold, HSSFColor color) {
        CellStyle style = wb.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_NONE);
        style.setBorderTop(HSSFCellStyle.BORDER_NONE);
        style.setBorderLeft(HSSFCellStyle.BORDER_NONE);
        style.setBorderRight(HSSFCellStyle.BORDER_NONE);
//
//        Font font=style.getFont(wb);
//        font.setColor(IndexedColors.BLACK.getIndex());
        style.setFont(underLineFont);


        addFontIfApply(style, bold, color);
//            Font underLineFont=style.getFont(wb);
//            underLineFont.setUnderline(Font.U_SINGLE);


        return style;
    }

    private CellStyle getCellNoStyle(boolean bold, HSSFColor fontColor) {
        CellStyle style = wb.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_NONE);
        style.setBorderTop(HSSFCellStyle.BORDER_NONE);
        style.setBorderLeft(HSSFCellStyle.BORDER_NONE);
        style.setBorderRight(HSSFCellStyle.BORDER_NONE);
//        Font font=style.getFont(wb);
//        font.setColor(IndexedColors.BLACK.getIndex());
//        style.setFont(font);

        addFontIfApply(style, bold, fontColor);

        return style;
    }

    public CellStyle getCellNoStyle(String pattern, boolean bold, HSSFColor color) {
        CellStyle style = wb.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_NONE);
        style.setBorderTop(HSSFCellStyle.BORDER_NONE);
        style.setBorderLeft(HSSFCellStyle.BORDER_NONE);
        style.setBorderRight(HSSFCellStyle.BORDER_NONE);


        if (style != null && pattern != null) {
            style.setDataFormat(format.getFormat(pattern));
        }


        addFontIfApply(style, bold, color);

        return style;
    }

    private CellStyle getCellGrayStyle(boolean bold, HSSFColor color) {
        CellStyle style = wb.createCellStyle();
        if (cellStyle != null) {
            style.cloneStyleFrom(cellStyle);
        }
//        Font font=style.getFont(wb);
//        font.setColor(IndexedColors.BLACK.getIndex());
//        style.setFont(font);

        addFontIfApply(style, bold, color);


        return style;
    }

    private void addFontIfApply(CellStyle style, boolean bold, HSSFColor color) {
        //Font font=style.getFont(wb);
        //underLineFont.setUnderline(Font.U_SINGLE);
        if (bold) {
            Font boldFontLocal = boldFont;
            if (color != null) {
                boldFontLocal = createFontBold();
                boldFontLocal.setColor(color.getIndex());
            }
            style.setFont(boldFontLocal);
        } else {
            if (color != null) {
                Font boldFontLocal = wb.createFont();
                boldFontLocal.setColor(color.getIndex());
                style.setFont(boldFontLocal);
            }
        }
    }

    private CellStyle getCellHiddenStyle() {
        CellStyle style = wb.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_NONE);
        style.setBorderTop(HSSFCellStyle.BORDER_NONE);
        style.setBorderLeft(HSSFCellStyle.BORDER_NONE);
        style.setBorderRight(HSSFCellStyle.BORDER_NONE);
//        Font font=style.getFont(wb);
//        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(whiteFont);
        return style;
    }

    public CellStyle getCellStyle() {
        return cellStyle;
    }

    public void setCellStyle(CellStyle cellStyle) {
        this.cellStyle = cellStyle;
    }

    public void addShape(int rowIndex, int colIndex, short conditionValue) {

        switch (conditionValue) {
            case HSSFColor.RED.index:
                createShape(rowIndex, colIndex, HSSFColor.RED.triplet);
                break;
            case HSSFColor.GREEN.index:
                createShape(rowIndex, colIndex, HSSFColor.GREEN.triplet);
                break;
            case HSSFColor.YELLOW.index:
                createShape(rowIndex, colIndex, HSSFColor.YELLOW.triplet);
                break;

        }
    }

    private void createShape(int rowIndex, int colIndex, short[] color) {
        short[] lineColor = HSSFColor.GREY_25_PERCENT.triplet;
        HSSFPatriarch patriarch = (HSSFPatriarch) sheet.createDrawingPatriarch();
        HSSFClientAnchor a = new HSSFClientAnchor(0, 0, 255, 230, (short) colIndex, rowIndex, (short) colIndex, rowIndex);
        HSSFSimpleShape shape = patriarch.createSimpleShape(a);
        shape.setShapeType(HSSFSimpleShape.OBJECT_TYPE_OVAL);
        shape.setLineStyleColor(lineColor[0], lineColor[1], lineColor[2]);
        shape.setFillColor(color[0], color[1], color[2]);
        shape.setLineWidth(HSSFShape.LINEWIDTH_DEFAULT);
        shape.setLineStyle(HSSFShape.LINESTYLE_SOLID);
    }

    public boolean isEmpty() {
        return row == null;
    }

    public void drawBottonLine(int rowIdx, int colStartIdx, int colEndIdx, boolean bold) {
        setRow(rowIdx);
        //CellStyle defaultStyle = bottomCellStyle(bold, null);
        for (int colIndex = colStartIdx; colIndex <= colEndIdx; colIndex++) {
            Cell cell = row.getCell(colIndex);
            if (cell == null) {
                cell = row.createCell(colIndex);
            }
            //CellStyle styleCell = cell.getCellStyle();
            //if (styleCell != null) styleCell.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            //else styleCell = defaultStyle;
            cell.setCellStyle(bottonLinetStyle);
        }
    }

    public void drawAllLines(int rowIdx, int colStartIdx, int colEndIdx, boolean bold) {
        setRow(rowIdx);
        for (int colIndex = colStartIdx; colIndex <= colEndIdx; colIndex++) {
            Cell cell = row.getCell(colIndex);
            if (cell == null) {
                cell = row.createCell(colIndex);
            }
            cell.setCellStyle(squareCellStyle);
        }
    }


    public void merge(int currRow, int colStart, int colEnd) {
        sheet.addMergedRegion(new CellRangeAddress(currRow, currRow, colStart, colEnd));
    }

    public void forceFormulaCalculation() {
        if (!isXSS()) {
            HSSFSheet sheet2 = (HSSFSheet) sheet;
            sheet2.setForceFormulaRecalculation(true);
        }
    }



    public static String convertIntToLiteral(int col) {
        int col1 = (col / ('Z' - 'A' + 1));
        int col2 = (col % ('Z' - 'A' + 1));
        String str = Character.valueOf((char) ('A' + col2)).toString();
        if (col1 > 0) str = Character.valueOf((char) ('A' + col1 - 1)).toString() + str;
        return str;
    }

    public void autoSizeColumn(int i) {
        CellStyle cs = wb.createCellStyle();
        cs.setWrapText(true);
        Cell cell = row.getCell(i);
        cell.setCellStyle(cs);
    }
}
