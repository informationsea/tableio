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
import info.informationsea.tableio.impl.AdaptiveTableCellImpl;
import info.informationsea.tableio.impl.TableCellHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.junit.Test;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertTrue;

@Slf4j
public class ExcelSheetWriterTest {

    public static final Object[][] data = new Object[][]{
            new Object[]{"A", "B", "C"},
            new Object[]{"D", "E", "F"},
            new Object[]{1.2, "G", false},
            new Object[]{1., 2., 3.},
            new Object[]{1., "B", 3.},
            new Object[]{1., "LONG-LONG-LONG-LONG-text", true}
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

    @Test
    public void testWriter6() throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook();
        try (ExcelSheetWriter sheetWriter = new ExcelSheetWriter(workbook.createSheet("Hello"))) {
            sheetWriter.printRecord(new AdaptiveTableCellImpl("Hello"), new AdaptiveTableCellImpl(1.0), new AdaptiveTableCellImpl(true), null, false);
        }
        try (ExcelSheetReader sheetReader = new ExcelSheetReader(workbook.getSheet("Hello"))) {
            Iterator<TableRecord> iterator = sheetReader.iterator();
            TableRecord record = iterator.next();
            Assert.assertArrayEquals(new Object[]{"Hello", 1.0, true, null, false}, TableCellHelper.convertFromTableCell(record.getContent()));
        }
    }

    @Test
    public void testWriter7() throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook();
        try (ExcelSheetWriter sheetWriter = new ExcelSheetWriter(workbook.createSheet())) {
            sheetWriter.printRecord(new AdaptiveTableCellImpl("=1+2"),
                    new AdaptiveTableCellImpl("=HYPERLINK(\"http://www.google.co.jp\")"));
        }

        File buildDir = new File("build");
        File testOutput = new File(buildDir.getAbsolutePath(), "test-data");
        if (!testOutput.isDirectory())
            assertTrue(testOutput.mkdirs());
        try (FileOutputStream outputStream = new FileOutputStream(new File(testOutput, "formula-writer.xlsx"))) {
            workbook.write(outputStream);
        }

    }

    @Test
    public void testPrettyWriter() throws Exception {
        File buildDir = new File(System.getProperty("user.dir"), "build");
        File testOutput = new File(buildDir, "test-data");
        testOutput.mkdirs();
        FileOutputStream outputStream = new FileOutputStream(new File(testOutput, "pretty.xlsx"));
        XlsxWriter writer = new XlsxWriter(outputStream);
        writer.setPrettyTable(true);
        writer.printAll(Arrays.asList(data));
        writer.close();
        log.warn("Please open excel files in {} to verify pretty table", testOutput.getAbsolutePath());
    }

    @Test
    public void testPrettyWriter2() throws Exception {
        File buildDir = new File(System.getProperty("user.dir"), "build");
        File testOutput = new File(buildDir, "test-data");
        testOutput.mkdirs();
        FileOutputStream outputStream = new FileOutputStream(new File(testOutput, "pretty.xls"));
        XlsWriter writer = new XlsWriter(outputStream);
        writer.setPrettyTable(true);
        writer.printAll(Arrays.asList(data));
        writer.close();
    }

    @Test
    public void testPrettyWriter3() throws Exception {
        File buildDir = new File(System.getProperty("user.dir"), "build");
        File testOutput = new File(buildDir, "test-data");
        testOutput.mkdirs();
        FileOutputStream outputStream = new FileOutputStream(new File(testOutput, "pretty2.xlsx"));
        try (XlsxWriter writer = new XlsxWriter(outputStream)) {
            writer.setPrettyTable(true);
            writer.setAlternativeBackground(false);

            CellStyle[] styles = new CellStyle[3];
            for (int i = 0; i < styles.length; i++) {
                styles[i] = writer.getWorkbook().createCellStyle();
                styles[i].setFillPattern(FillPatternType.SOLID_FOREGROUND);
            }
            ((XSSFCellStyle)styles[0]).setFillForegroundColor(new XSSFColor(new Color(241, 232, 255)));
            ((XSSFCellStyle)styles[1]).setFillForegroundColor(new XSSFColor(new Color(232, 255, 238)));
            ((XSSFCellStyle)styles[2]).setFillForegroundColor(new XSSFColor(new Color(255, 253, 232)));
            for (int i = 0; i < styles.length; i++) {
                writer.registerBaseCellStyle(i, styles[i]);
            }

            int index = 0;
            for (Object[] row : data) {
                writer.useBaseCellStyle(index % 3);
                writer.printRecord(row);
                index += 1;
            }
        }
    }

    @Test
    public void testCopyTable() throws Exception {
        File buildDir = new File(System.getProperty("user.dir"), "build");
        File testOutput = new File(buildDir, "test-data");
        testOutput.mkdirs();
        FileOutputStream outputStream = new FileOutputStream(new File(testOutput, "copied.xlsx"));
        try (XlsxWriter writer = new XlsxWriter(outputStream)) {
            try (XlsxReader reader = new XlsxReader(getClass().getResourceAsStream("cellltypes.xlsx"))) {
                for (TableRecord record : reader) {
                    writer.printRecord((Object[]) record.getContent());
                }
            }
        }

    }

    @Test
    public void testCopyTable2() throws Exception {
        File buildDir = new File(System.getProperty("user.dir"), "build");
        File testOutput = new File(buildDir, "test-data");
        testOutput.mkdirs();
        FileOutputStream outputStream = new FileOutputStream(new File(testOutput, "copied.xls"));
        try (XlsWriter writer = new XlsWriter(outputStream)) {
            try (XlsxReader reader = new XlsxReader(getClass().getResourceAsStream("cellltypes.xlsx"))) {
                for (TableRecord record : reader) {
                    writer.printRecord((Object[])record.getContent());
                }
            }
        }

    }

    public void commonAssert(ExcelSheetReader excelSheetReader) {
        List<TableCell[]> readData = excelSheetReader.readAll();

        for (int i = 0; i < data.length; i++) {
            Assert.assertArrayEquals(data[i], TableCellHelper.convertFromTableCell(readData.get(i)));
        }
    }

}
