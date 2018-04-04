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

package info.informationsea.tableio.excel.test;

import info.informationsea.tableio.TableCell;
import info.informationsea.tableio.TableRecord;
import info.informationsea.tableio.excel.*;
import info.informationsea.tableio.impl.TableCellHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class ExcelSheetReaderTest {
    public static final String[] header = new String[]{"Index", "Sepal.Length", "Sepal.Width", "Petal.Length", "Petal.Width", "Species"};
    public static final Object[][] contentHead = new Object[][]{
            new Object[]{1.0, 5.1, 3.5, 1.4, 0.2, "setosa"},
            new Object[]{2.0, 4.9, 3.0, 1.4, 0.2, "setosa"}
    };

    @Test
    public void testRead() throws IOException {
        Workbook workbook = new XSSFWorkbook(getClass().getResourceAsStream("iris.xlsx"));
        ExcelSheetReader excelSheetReader = new ExcelSheetReader(workbook.getSheetAt(0));
        testContents(excelSheetReader);
    }

    @Test
    public void testRead2() throws IOException {
        Workbook workbook = new HSSFWorkbook(getClass().getResourceAsStream("iris.xls"));
        ExcelSheetReader excelSheetReader = new ExcelSheetReader(workbook.getSheetAt(0));
        testContents(excelSheetReader);
    }

    @Test
    public void testRead3() throws IOException {
        testContents(new XlsxReader(getClass().getResourceAsStream("iris.xlsx")));
        testContents(new XlsxReader(getClass().getResourceAsStream("iris.xlsx"), 0));
        testContents(new XlsxReader(getClass().getResourceAsStream("iris.xlsx"), "iris.csv"));
    }

    @Test
    public void testRead4() throws IOException {
        testContents(new XlsReader(getClass().getResourceAsStream("iris.xls")));
        testContents(new XlsReader(getClass().getResourceAsStream("iris.xls"), 0));
        testContents(new XlsReader(getClass().getResourceAsStream("iris.xls"), "iris.csv"));
    }

    @Test
    public void testNullLine() throws Exception {
        XlsxReader reader = new XlsxReader(getClass().getResourceAsStream("nullline.xlsx"));
        Iterator<TableRecord> iterator = reader.iterator();

        Assert.assertArrayEquals(new String[]{"A1", "B1"}, TableCellHelper.convertFromTableCell(iterator.next().getContent()));
        Assert.assertArrayEquals(new String[]{}, TableCellHelper.convertFromTableCell(iterator.next().getContent()));
        Assert.assertArrayEquals(new String[]{"A3", "B3", null, "D3"}, TableCellHelper.convertFromTableCell(iterator.next().getContent()));
        Assert.assertArrayEquals(new String[]{}, TableCellHelper.convertFromTableCell(iterator.next().getContent()));
        Assert.assertArrayEquals(new String[]{}, TableCellHelper.convertFromTableCell(iterator.next().getContent()));
        Assert.assertArrayEquals(new String[]{null, null, null, "D6"}, TableCellHelper.convertFromTableCell(iterator.next().getContent()));
        Assert.assertFalse(iterator.hasNext());

    }

    @Test
    public void cellTypes() throws Exception {
        File buildDir = new File(System.getProperty("user.dir"), "build");
        File testOutput = new File(buildDir, "test-data");
        testOutput.mkdirs();
        try (FileOutputStream outputStream = new FileOutputStream(new File(testOutput, "celltypes-result.xlsx"))) {
            try (XlsxWriter writer = new XlsxWriter(outputStream)) {
                writer.setAutoResizeColumn(true);
                XlsxReader reader = new XlsxReader(getClass().getResourceAsStream("cellltypes.xlsx"));
                reader.setUseHeader(true);
                writer.printRecord((Object[]) reader.getHeader());

                for (TableRecord record : reader) {
                    ArrayList<String> cellInfo = new ArrayList<>();

                    for (TableCell cell : record) {
                        ExcelCell excelCell = (ExcelCell) cell;
                        cellInfo.add(String.format(
                                "%s  %s  %d  %s",
                                excelCell.getCell().toString(),
                                excelCell.getCell().getCellStyle().getDataFormatString(),
                                excelCell.getCell().getCellStyle().getDataFormat(),
                                excelCell.getTableCellType().toString()));
                    }
                    writer.printRecord(cellInfo.toArray());
                }
            }
        }
    }

    @Test
    public void testCells() throws Exception {
        XlsxReader reader = new XlsxReader(getClass().getResourceAsStream("cellltypes.xlsx"));
        reader.setUseHeader(true);
        Iterator<TableRecord> iterator = reader.iterator();

        { // Verify first line
            TableRecord record = iterator.next();
            ExcelCell cell = (ExcelCell) record.get("String");
            Assert.assertEquals("Hello", cell.toString());
            Assert.assertEquals(TableCell.TableCellType.STRING, cell.getTableCellType());

            cell = (ExcelCell) record.get("Numeric");
            Assert.assertEquals(2.0, cell.toNumeric(), 0.00000001);
            Assert.assertEquals(TableCell.TableCellType.NUMERIC, cell.getTableCellType());
            Assert.assertEquals("2", cell.toString());

            cell = (ExcelCell) record.get("Boolean");
            Assert.assertTrue(cell.toBoolean());
            Assert.assertEquals(TableCell.TableCellType.BOOLEAN, cell.getTableCellType());
            Assert.assertEquals("TRUE", cell.toString());

            cell = (ExcelCell) record.get("Formula");
            Assert.assertEquals(5.4, cell.toNumeric(), 0.00000001);
            Assert.assertEquals(TableCell.TableCellType.NUMERIC, cell.getTableCellType());
            Assert.assertEquals("5.4", cell.toString());
        }

        { // Verify second line
            TableRecord record = iterator.next();
            ExcelCell cell = (ExcelCell) record.get("String");
            Assert.assertEquals("1.0", cell.toString());
            Assert.assertEquals(TableCell.TableCellType.STRING, cell.getTableCellType());
            Assert.assertNull(cell.getFormula());

            cell = (ExcelCell) record.get("Numeric");
            Assert.assertEquals(3.4, cell.toNumeric(), 0.00000001);
            Assert.assertEquals(TableCell.TableCellType.NUMERIC, cell.getTableCellType());
            Assert.assertEquals("3.4", cell.toString());
            Assert.assertNull(cell.getFormula());

            cell = (ExcelCell) record.get("Boolean");
            Assert.assertFalse(cell.toBoolean());
            Assert.assertEquals(TableCell.TableCellType.BOOLEAN, cell.getTableCellType());
            Assert.assertEquals("FALSE", cell.toString());
            Assert.assertNull(cell.getFormula());

            cell = (ExcelCell) record.get("Formula");
            Assert.assertEquals(TableCell.TableCellType.STRING, cell.getTableCellType());
            Assert.assertEquals("Hello1.0", cell.toString());
            Assert.assertNotNull(cell.getFormula());
            Assert.assertEquals("A2&A3", cell.getFormula());
        }

        { // Verify third line
            TableRecord record = iterator.next();
            ExcelCell cell = (ExcelCell) record.get("String");
            Assert.assertEquals("2", cell.toString());
            Assert.assertEquals(TableCell.TableCellType.STRING, cell.getTableCellType());

            cell = (ExcelCell) record.get("Numeric");
            Assert.assertEquals(1.0, cell.toNumeric(), 0.00000001);
            Assert.assertEquals(TableCell.TableCellType.NUMERIC, cell.getTableCellType());
            Assert.assertEquals("1.00", cell.toString());

            cell = (ExcelCell) record.get("Boolean");
            Assert.assertFalse(cell.toBoolean());
            Assert.assertEquals(TableCell.TableCellType.BOOLEAN, cell.getTableCellType());
            Assert.assertEquals("FALSE", cell.toString());
        }

        { // Verify fourth line
            TableRecord record = iterator.next();
            ExcelCell cell = (ExcelCell) record.get("String");
            Assert.assertEquals("3", cell.toString());
            Assert.assertEquals(TableCell.TableCellType.STRING, cell.getTableCellType());

            cell = (ExcelCell) record.get("Numeric");
            Assert.assertEquals(2.0, cell.toNumeric(), 0.00000001);
            Assert.assertEquals(TableCell.TableCellType.NUMERIC, cell.getTableCellType());
            Assert.assertEquals("2.00E+00", cell.toString());

            cell = (ExcelCell) record.get("Boolean");
            Assert.assertTrue(cell.toBoolean());
            Assert.assertEquals(TableCell.TableCellType.BOOLEAN, cell.getTableCellType());
            Assert.assertEquals("TRUE", cell.toString());

            cell = (ExcelCell) record.get("Formula");
            Assert.assertEquals(3.0, cell.toNumeric(), 0.00000001);
            Assert.assertEquals(TableCell.TableCellType.NUMERIC, cell.getTableCellType());
            Assert.assertEquals("3.00", cell.toString());
        }

        { // Verify fifth line
            TableRecord record = iterator.next();
            ExcelCell cell = (ExcelCell) record.get("String");
            Assert.assertEquals("TRUE", cell.toString());
            Assert.assertEquals(TableCell.TableCellType.STRING, cell.getTableCellType());

            cell = (ExcelCell) record.get("Numeric");
            Assert.assertEquals(0.5, cell.toNumeric(), 0.00000001);
            Assert.assertEquals(TableCell.TableCellType.NUMERIC, cell.getTableCellType());
            Assert.assertEquals("1/2", cell.toString().trim());

            cell = (ExcelCell) record.get("Boolean");
            Assert.assertFalse(cell.toBoolean());
            Assert.assertEquals(TableCell.TableCellType.BOOLEAN, cell.getTableCellType());
            Assert.assertEquals("FALSE", cell.toString());

            cell = (ExcelCell) record.get("Formula");
            Assert.assertEquals(3.0, cell.toNumeric(), 0.00000001);
            Assert.assertEquals(TableCell.TableCellType.NUMERIC, cell.getTableCellType());
            Assert.assertEquals("3.000", cell.toString().trim());
        }
    }

    private void testContents(ExcelSheetReader excelSheetReader) {
        excelSheetReader.setUseHeader(true);
        Assert.assertArrayEquals(header, excelSheetReader.getHeader());
        List<TableCell[]> alldata = excelSheetReader.readAll();
        Assert.assertEquals(150, alldata.size());
        Assert.assertArrayEquals(contentHead[0], TableCellHelper.convertFromTableCell(alldata.get(0)));
        Assert.assertArrayEquals(contentHead[1], TableCellHelper.convertFromTableCell(alldata.get(1)));
        Assert.assertTrue(excelSheetReader.isUseHeader());
        Assert.assertFalse(excelSheetReader.iterator().hasNext());
    }
}
