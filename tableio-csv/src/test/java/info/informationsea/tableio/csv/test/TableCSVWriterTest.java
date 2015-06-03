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

package info.informationsea.tableio.csv.test;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import info.informationsea.tableio.TableCell;
import info.informationsea.tableio.csv.TableCSVReader;
import info.informationsea.tableio.csv.TableCSVWriter;
import info.informationsea.tableio.csv.format.TabDelimitedFormat;
import info.informationsea.tableio.impl.AdaptiveTableCellImpl;
import info.informationsea.tableio.impl.TableCellHelper;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

public class TableCSVWriterTest {

    public static final Object[][] data = new Object[][]{
            TableCellHelper.convertToTableCell("A", "B", "C"),
            TableCellHelper.convertToTableCell("D", "E", "F"),
    };

    @Test
    public void testWriter() throws Exception {
        StringWriter stringWriter = new StringWriter();
        TableCSVWriter csvWriter = new TableCSVWriter(stringWriter);
        csvWriter.printAll(Arrays.asList(data));
        csvWriter.close();

        TableCSVReader csvReader = new TableCSVReader(new StringReader(stringWriter.toString()));
        List<TableCell[]> readData = csvReader.readAll();

        for (int i = 0; i < data.length; i++) {
            Assert.assertArrayEquals(data[i], readData.get(i));
        }
    }

    @Test
    public void testWriter2() throws Exception {
        StringWriter stringWriter = new StringWriter();
        TableCSVWriter csvWriter = new TableCSVWriter(stringWriter, new TabDelimitedFormat());
        csvWriter.printAll(Arrays.asList(data));
        csvWriter.close();

        TableCSVReader csvReader = new TableCSVReader(new StringReader(stringWriter.toString()), new TabDelimitedFormat());
        List<TableCell[]> readData = csvReader.readAll();

        for (int i = 0; i < data.length; i++) {
            Assert.assertArrayEquals(data[i], readData.get(i));
        }
    }


    @Test
    public void testWriter3() throws Exception {
        StringWriter stringWriter = new StringWriter();
        TableCSVWriter csvWriter = new TableCSVWriter(new CSVWriter(stringWriter));
        csvWriter.printAll(Arrays.asList(data));
        csvWriter.close();

        TableCSVReader csvReader = new TableCSVReader(new CSVReader(new StringReader(stringWriter.toString())));
        List<TableCell[]> readData = csvReader.readAll();

        for (int i = 0; i < data.length; i++) {
            Assert.assertArrayEquals(data[i], readData.get(i));
        }
    }
}
