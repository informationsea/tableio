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


package info.informationsea.tableio.csv.test;

import info.informationsea.tableio.TableCell;
import info.informationsea.tableio.TableReader;
import info.informationsea.tableio.TableWriter;
import info.informationsea.tableio.csv.TableCSVReader;
import info.informationsea.tableio.csv.ZipCSVTableWorkbookWriter;
import info.informationsea.tableio.csv.format.TabDelimitedFormat;
import info.informationsea.tableio.impl.AdaptiveTableCellImpl;
import info.informationsea.tableio.impl.TableCellHelper;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipFile;

public class ZipCSVTableWorkbookWriterTest {

    public static final Object[][][] data = new Object[][][]{
            new Object[][]{
                    TableCellHelper.convertToTableCell("A", "B", "C"),
                    TableCellHelper.convertToTableCell("D", "E", "F"),
            },
            new Object[][]{
                    TableCellHelper.convertToTableCell("1", "3", "5"),
                    TableCellHelper.convertToTableCell("2", "4", "6"),
            }
    };


    @Test
    public void testSheets() throws Exception {
        Path tempFile = Files.createTempFile(null, ".zip");
        FileOutputStream outputStream = new FileOutputStream(tempFile.toFile());

        try (ZipCSVTableWorkbookWriter workbookWriter = new ZipCSVTableWorkbookWriter(outputStream)) {
            try (TableWriter table1 = workbookWriter.createTable("table1")) {
                table1.printAll(Arrays.asList(data[0]));
            }
            try (TableWriter table1 = workbookWriter.createTable("table2")) {
                table1.printAll(Arrays.asList(data[1]));
            }
        }

        ZipFile zipFile = new ZipFile(tempFile.toFile());
        Assert.assertNotNull(zipFile.getEntry("table1.csv"));
        Assert.assertNotNull(zipFile.getEntry("table2.csv"));

        TableReader reader1 = new TableCSVReader(new InputStreamReader(zipFile.getInputStream(zipFile.getEntry("table1.csv"))));
        assertData(reader1, data[0]);

        TableReader reader2 = new TableCSVReader(new InputStreamReader(zipFile.getInputStream(zipFile.getEntry("table2.csv"))));
        assertData(reader2, data[1]);
    }

    @Test
    public void testSheets2() throws Exception {
        Path tempFile = Files.createTempFile(null, ".zip");
        FileOutputStream outputStream = new FileOutputStream(tempFile.toFile());

        try (ZipCSVTableWorkbookWriter workbookWriter = new ZipCSVTableWorkbookWriter(outputStream, new TabDelimitedFormat(), ".txt")) {
            try (TableWriter table1 = workbookWriter.createTable("table1")) {
                table1.printAll(Arrays.asList(data[0]));
            }
            try (TableWriter table1 = workbookWriter.createTable("table2")) {
                table1.printAll(Arrays.asList(data[1]));
            }
        }

        ZipFile zipFile = new ZipFile(tempFile.toFile());
        Assert.assertNotNull(zipFile.getEntry("table1.txt"));
        Assert.assertNotNull(zipFile.getEntry("table2.txt"));

        TableReader reader1 = new TableCSVReader(new InputStreamReader(zipFile.getInputStream(zipFile.getEntry("table1.txt"))), new TabDelimitedFormat());
        assertData(reader1, data[0]);

        TableReader reader2 = new TableCSVReader(new InputStreamReader(zipFile.getInputStream(zipFile.getEntry("table2.txt"))), new TabDelimitedFormat());
        assertData(reader2, data[1]);
    }

    @Test
    public void testSheets3() throws Exception {
        Path tempFile = Files.createTempFile(null, ".zip");
        FileOutputStream outputStream = new FileOutputStream(tempFile.toFile());

        try (ZipCSVTableWorkbookWriter workbookWriter = new ZipCSVTableWorkbookWriter(outputStream, new TabDelimitedFormat())) {
            try (TableWriter table1 = workbookWriter.createTable("table1")) {
                table1.printAll(Arrays.asList(data[0]));
            }
            try (TableWriter table1 = workbookWriter.createTable("table2")) {
                table1.printAll(Arrays.asList(data[1]));
            }
        }

        ZipFile zipFile = new ZipFile(tempFile.toFile());
        Assert.assertNotNull(zipFile.getEntry("table1.csv"));
        Assert.assertNotNull(zipFile.getEntry("table2.csv"));

        TableReader reader1 = new TableCSVReader(new InputStreamReader(zipFile.getInputStream(zipFile.getEntry("table1.csv"))), new TabDelimitedFormat());
        assertData(reader1, data[0]);

        TableReader reader2 = new TableCSVReader(new InputStreamReader(zipFile.getInputStream(zipFile.getEntry("table2.csv"))), new TabDelimitedFormat());
        assertData(reader2, data[1]);
    }

    private void assertData(TableReader reader, Object[][] data) {
        List<TableCell[]> list = reader.readAll();
        Assert.assertEquals(data.length, list.size());

        for (int i = 0; i < data.length; i++) {
            Assert.assertArrayEquals(data[i], list.get(i));
        }
    }
}
