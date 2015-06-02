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

import info.informationsea.tableio.TableRecord;
import info.informationsea.tableio.excel.ExcelSheetReader;
import info.informationsea.tableio.excel.XlsReader;
import info.informationsea.tableio.excel.XlsxReader;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

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

        Assert.assertArrayEquals(new String[]{"A1", "B1"}, iterator.next().getContent());
        Assert.assertArrayEquals(new String[]{}, iterator.next().getContent());
        Assert.assertArrayEquals(new String[]{"A3", "B3", null, "D3"}, iterator.next().getContent());
        Assert.assertArrayEquals(new String[]{}, iterator.next().getContent());
        Assert.assertArrayEquals(new String[]{}, iterator.next().getContent());
        Assert.assertArrayEquals(new String[]{null, null, null, "D6"}, iterator.next().getContent());
        Assert.assertFalse(iterator.hasNext());

    }

    private void testContents(ExcelSheetReader excelSheetReader) {
        excelSheetReader.setUseHeader(true);
        Assert.assertArrayEquals(header, excelSheetReader.getHeader());
        List<Object[]> alldata = excelSheetReader.readAll();
        Assert.assertEquals(150, alldata.size());
        Assert.assertArrayEquals(contentHead[0], alldata.get(0));
        Assert.assertArrayEquals(contentHead[1], alldata.get(1));
        Assert.assertTrue(excelSheetReader.isUseHeader());
        Assert.assertFalse(excelSheetReader.iterator().hasNext());
    }
}
