/*
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

import info.informationsea.tableio.TableReader;
import info.informationsea.tableio.TableWriter;
import info.informationsea.tableio.excel.ExcelSheetReader;
import info.informationsea.tableio.excel.ExcelWorkbookWriter;
import info.informationsea.tableio.excel.XlsWorkbookWriter;
import info.informationsea.tableio.excel.XlsxWorkbookWriter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

public class ExcelWorkbookWriterTest {

    public static final Object[][][] data = new Object[][][]{
            new Object[][]{
                    new Object[]{"A", "B", "C"},
                    new Object[]{"D", "E", false},
            },
            new Object[][]{
                    new Object[]{1., "3", "5"},
                    new Object[]{"2", 4., true},
            }
    };


    @Test
    public void testXlsxWorkbookWriter() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (ExcelWorkbookWriter workbookWriter = new XlsxWorkbookWriter(outputStream)) {
            commonTestExcelWorkbookWriter(workbookWriter);
        }

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Workbook workbook = new XSSFWorkbook(inputStream);
        assertData(new ExcelSheetReader(workbook.getSheet("table1")), data[0]);
        assertData(new ExcelSheetReader(workbook.getSheet("table2")), data[1]);
    }

    @Test
    public void testXlsWorkbookWriter() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (ExcelWorkbookWriter workbookWriter = new XlsWorkbookWriter(outputStream)) {
            commonTestExcelWorkbookWriter(workbookWriter);
        }

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Workbook workbook = new HSSFWorkbook(inputStream);
        assertData(new ExcelSheetReader(workbook.getSheet("table1")), data[0]);
        assertData(new ExcelSheetReader(workbook.getSheet("table2")), data[1]);
    }

    public void commonTestExcelWorkbookWriter(ExcelWorkbookWriter workbookWriter) throws Exception {
        workbookWriter.setPrettyTable(true);
        try(TableWriter writer1 = workbookWriter.createTable("table1")) {
            writer1.printAll(Arrays.asList(data[0]));
        }

        try (TableWriter writer2 = workbookWriter.createTable("table2")) {
            writer2.printAll(Arrays.asList(data[1]));
        }
    }

    private void assertData(TableReader reader, Object[][] data) {
        List<Object[]> list = reader.readAll();
        Assert.assertEquals(data.length, list.size());

        for (int i = 0; i < data.length; i++) {
            Assert.assertArrayEquals(data[i], list.get(i));
        }
    }

}
