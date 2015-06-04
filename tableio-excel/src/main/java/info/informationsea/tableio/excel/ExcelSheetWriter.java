/**
 *  tableio
 *  Copyright (C) 2015 Yasunobu OKAMURA
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package info.informationsea.tableio.excel;

import info.informationsea.tableio.TableCell;
import info.informationsea.tableio.impl.AbstractTableWriter;
import lombok.*;
import org.apache.poi.common.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

import java.awt.*;
import java.awt.Color;
import java.awt.image.IndexColorModel;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExcelSheetWriter extends AbstractTableWriter {
    @NonNull
    private Sheet sheet;
    private int currentRow = 0;
    private int maxColumn = 0;

    @Setter @Getter
    private boolean autoFilter = false;
    @Setter @Getter
    private boolean alternativeBackground = false;
    @Setter @Getter
    private boolean enableHeaderStyle = false;
    @Setter @Getter
    private boolean autoResizeColumn = false;

    private static final String DEFAULT_STYLE = "DEFAULT_STYLE";
    private enum CellStyleType {
        BASE, HEADER, ALTERNATIVE, LINK, LINK_ALTERNATIVE
    }
    private Object currentStyle = DEFAULT_STYLE;
    private Map<Object, Map<CellStyleType, CellStyle>> styles = new HashMap<>();



    public ExcelSheetWriter(Sheet sheet) {
        setSheet(sheet);
    }

    private void initialize() {
        registerBaseCellStyle(DEFAULT_STYLE, null);
        useBaseCellStyle(DEFAULT_STYLE);
    }

    protected void setSheet(Sheet sheet) {
        this.sheet = sheet;
        initialize();
    }

    public void useBaseCellStyle(Object index) {
        currentStyle = index;
    }

    public void registerBaseCellStyle(Object index, CellStyle style) {
        CellStyle baseCellStyles = style;

        if (baseCellStyles == null)
            baseCellStyles = sheet.getWorkbook().createCellStyle();

        // header style
        CellStyle headerCellStyles = sheet.getWorkbook().createCellStyle();
        headerCellStyles.cloneStyleFrom(baseCellStyles);
        Font headerFont = sheet.getWorkbook().createFont();
        headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        headerCellStyles.setFont(headerFont);
        headerCellStyles.setFillPattern(CellStyle.SOLID_FOREGROUND);
        headerCellStyles.setFillForegroundColor(IndexedColors.WHITE.getIndex());

        // alternative style
        CellStyle alternativeCellStyles = sheet.getWorkbook().createCellStyle();
        alternativeCellStyles.cloneStyleFrom(baseCellStyles);
        if (baseCellStyles instanceof XSSFCellStyle) {
            ((XSSFCellStyle) alternativeCellStyles).setFillForegroundColor(new XSSFColor(new Color(242, 242, 242)));
        } else {
            alternativeCellStyles.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        }
        alternativeCellStyles.setFillPattern(CellStyle.SOLID_FOREGROUND);

        // link style
        Font linkFont = sheet.getWorkbook().createFont();
        linkFont.setColor(IndexedColors.BLUE.getIndex());
        CellStyle linkStyle = sheet.getWorkbook().createCellStyle();
        linkStyle.cloneStyleFrom(baseCellStyles);
        linkStyle.setFont(linkFont);
        CellStyle alternativeLinkStyle = sheet.getWorkbook().createCellStyle();
        alternativeLinkStyle.cloneStyleFrom(alternativeCellStyles);
        alternativeLinkStyle.setFont(linkFont);

        Map<CellStyleType, CellStyle> styleMap = new HashMap<>();
        styleMap.put(CellStyleType.BASE, baseCellStyles);
        styleMap.put(CellStyleType.HEADER, headerCellStyles);
        styleMap.put(CellStyleType.ALTERNATIVE, alternativeCellStyles);
        styleMap.put(CellStyleType.BASE, style);
        styleMap.put(CellStyleType.LINK, linkStyle);
        styleMap.put(CellStyleType.LINK_ALTERNATIVE, alternativeLinkStyle);
        styles.put(index, styleMap);
    }

    @Override
    public void printRecord(Object... values) {
        Row row = sheet.createRow(currentRow);

        CellStyle style = styles.get(currentStyle).get(CellStyleType.BASE);
        if (currentRow % 2 == 1 && alternativeBackground)
            style = styles.get(currentStyle).get(CellStyleType.ALTERNATIVE);
        if (currentRow == 0 && enableHeaderStyle)
            style = styles.get(currentStyle).get(CellStyleType.HEADER);

        for (int i = 0; i < values.length; i++) {
            Cell cell;
            if (values[i] instanceof Boolean)
                cell = createCell(row, i, (Boolean) values[i]);
            else if (values[i] instanceof Number)
                cell = createCell(row, i, ((Number) values[i]).doubleValue());
            else if (values[i] instanceof Calendar)
                cell = createCell(row, i, (Calendar) values[i]);
            else if (values[i] instanceof Date)
                cell = createCell(row, i, (Date) values[i]);
            else if (values[i] instanceof TableCell)
                cell = createCell(row, i, (TableCell) values[i]);
            else if (values[i] == null)
                continue;
            else
                cell = createCell(row, i, values[i].toString());

            if (style != null)
                cell.setCellStyle(style);
        }

        maxColumn = Math.max(values.length, maxColumn);
        currentRow += 1;
    }

    public void setPrettyTable(boolean enable) {
        setAutoFilter(enable);
        setAlternativeBackground(enable);
        setEnableHeaderStyle(enable);
        setAutoResizeColumn(enable);
    }

    @Override
    public void close() throws Exception{
        if (autoFilter)
            sheet.setAutoFilter(new CellRangeAddress(0, currentRow-1, 0, maxColumn-1));
        if (enableHeaderStyle)
            sheet.createFreezePane(0, 1, 0, 1);
        if (autoResizeColumn) {
            for (int i = 0; i < maxColumn; i++) {
                sheet.autoSizeColumn(i);
            }
        }
    }

    private static Cell createCell(Row row, int column, boolean value) {
        Cell cell = row.createCell(column, Cell.CELL_TYPE_BOOLEAN);
        cell.setCellValue(value);
        return cell;
    }

    private static Cell createCell(Row row, int column, double value) {
        Cell cell = row.createCell(column, Cell.CELL_TYPE_NUMERIC);
        cell.setCellValue(value);
        return cell;
    }

    private static Cell createCell(Row row, int column, String value) {
        Cell cell = row.createCell(column, Cell.CELL_TYPE_STRING);
        cell.setCellValue(value);
        return cell;
    }

    private static Cell createCell(Row row, int column, Calendar value) {
        Cell cell = row.createCell(column, Cell.CELL_TYPE_STRING);
        cell.setCellValue(value);
        return cell;
    }

    private static Cell createCell(Row row, int column, Date value) {
        Cell cell = row.createCell(column, Cell.CELL_TYPE_STRING);
        cell.setCellValue(value);
        return cell;
    }

    private static Cell createCell(Row row, int column, TableCell tableCell) {
        Cell cell = row.createCell(column);

        if (tableCell instanceof ExcelCell) {
            Cell originalCell = ((ExcelCell)tableCell).getCell();
            CellStyle cellStyle = row.getSheet().getWorkbook().createCellStyle();
            copyCellStyle(cellStyle, originalCell.getCellStyle(), row.getSheet().getWorkbook(), originalCell.getSheet().getWorkbook());
            cell.setCellStyle(cellStyle);
            cell.setCellType(originalCell.getCellType());

            switch (originalCell.getCellType()) {
                case Cell.CELL_TYPE_FORMULA: {
                    switch (tableCell.getCellType()) {
                        case NUMERIC:
                            cell.setCellValue(tableCell.toNumeric());
                            break;
                        case BOOLEAN:
                            cell.setCellValue(tableCell.toBoolean());
                            break;
                        case STRING:
                            cell.setCellValue(tableCell.toString());
                            break;
                    }
                    cell.setCellFormula(originalCell.getCellFormula());
                    break;
                }
                case Cell.CELL_TYPE_BOOLEAN: {
                    cell.setCellValue(originalCell.getBooleanCellValue());
                    break;
                }
                case Cell.CELL_TYPE_NUMERIC: {
                    cell.setCellValue(originalCell.getNumericCellValue());
                    break;
                }
                case Cell.CELL_TYPE_STRING: {
                    if (cell.getCellStyle().getClass().equals(originalCell.getCellStyle().getClass())) {
                        cell.setCellValue(originalCell.getRichStringCellValue());
                    } else {
                        cell.setCellValue(originalCell.getStringCellValue());
                    }
                    break;
                }
            }

            Hyperlink originalLink = originalCell.getHyperlink();
            if (originalLink != null) {
                Hyperlink link = row.getSheet().getWorkbook().getCreationHelper().createHyperlink(originalLink.getType());
                link.setFirstColumn(originalLink.getFirstColumn());
                link.setFirstRow(originalLink.getFirstRow());
                link.setLastColumn(originalLink.getLastColumn());
                link.setLastRow(originalLink.getLastRow());
                link.setAddress(originalLink.getAddress());
                link.setLabel(originalLink.getLabel());
                cell.setHyperlink(link);
            }


        } else {
            switch (tableCell.getCellType()) {
                case BLANK:
                    cell.setCellType(Cell.CELL_TYPE_BLANK);
                    break;
                case STRING:
                default:
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    cell.setCellValue(tableCell.toString());
                    break;
                case NUMERIC:
                    cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                    cell.setCellValue(tableCell.toNumeric());
                    break;
                case BOOLEAN:
                    cell.setCellType(Cell.CELL_TYPE_BOOLEAN);
                    cell.setCellValue(tableCell.toBoolean());
                    break;
            }
        }

        return cell;
    }

    private static void copyCellStyle(CellStyle dest, CellStyle source, Workbook destWorkbook, Workbook sourceWorkbook) {
        if (dest.getClass().equals(source.getClass())) {
            dest.cloneStyleFrom(source);
            return;
        }

        dest.setBorderTop(source.getBorderTop());
        dest.setBorderBottom(source.getBorderBottom());
        dest.setBorderLeft(source.getBorderLeft());
        dest.setBorderRight(source.getBorderRight());
        dest.setDataFormat(source.getDataFormat());

        Font newFont = destWorkbook.createFont();
        Font originalFont = sourceWorkbook.getFontAt(source.getFontIndex());
        newFont.setBoldweight(originalFont.getBoldweight());
        newFont.setUnderline(originalFont.getUnderline());
        newFont.setItalic(originalFont.getItalic());
        newFont.setFontHeight(originalFont.getFontHeight());
        newFont.setFontHeightInPoints(originalFont.getFontHeightInPoints());
        newFont.setFontName(originalFont.getFontName());
        dest.setFont(newFont);
    }

}
