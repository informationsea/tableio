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


import info.informationsea.tableio.excel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

public class ExcelSheetWriterTest {

    public static final Object[][] data = new Object[][]{
            new Object[]{"A", "B", "C"},
            new Object[]{"D", "E", "F"},
            new Object[]{1.2, "G", false},
    };

    @Test
    public void testWriter() throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook();
        ExcelSheetWriter excelSheetWriter = new ExcelSheetWriter(workbook.createSheet());
        excelSheetWriter.printAll(Arrays.asList(data));

        ExcelSheetReader excelSheetReader = new ExcelSheetReader(workbook.getSheetAt(0));
        commonAssert(excelSheetReader);
    }

    @Test
    public void testWriter2() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XlsxWriter writer = new XlsxWriter(outputStream);
        writer.printAll(Arrays.asList(data));
        writer.close();

        XlsxReader reader = new XlsxReader(new ByteArrayInputStream(outputStream.toByteArray()));
        commonAssert(reader);
    }


    @Test
    public void testWriter3() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XlsWriter writer = new XlsWriter(outputStream);
        writer.printAll(Arrays.asList(data));
        writer.close();

        XlsReader reader = new XlsReader(new ByteArrayInputStream(outputStream.toByteArray()));
        commonAssert(reader);
    }

    @Test
    public void testWriter4() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XlsxWriter writer = new XlsxWriter(outputStream, "X1");
        writer.printAll(Arrays.asList(data));
        writer.close();

        XlsxReader reader = new XlsxReader(new ByteArrayInputStream(outputStream.toByteArray()), "X1");
        commonAssert(reader);
    }


    @Test
    public void testWriter5() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XlsWriter writer = new XlsWriter(outputStream, "Y1");
        writer.printAll(Arrays.asList(data));
        writer.close();

        XlsReader reader = new XlsReader(new ByteArrayInputStream(outputStream.toByteArray()), "Y1");
        commonAssert(reader);
    }


    public void commonAssert(ExcelSheetReader excelSheetReader) {
        List<Object[]> readData = excelSheetReader.readAll();

        for (int i = 0; i < data.length; i++) {
            Assert.assertArrayEquals(data[i], readData.get(i));
        }
    }

}
