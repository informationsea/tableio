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

import info.informationsea.tableio.TableCell;
import info.informationsea.tableio.csv.TableCSVReader;
import info.informationsea.tableio.impl.TableCellHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStreamReader;
import java.util.List;

public class TableCSVReaderTest {
    protected TableCSVReader tableCSVReader;
    public static final String[] header = new String[]{"", "Sepal.Length", "Sepal.Width", "Petal.Length", "Petal.Width", "Species"};
    public static final Object[][] contentHead = new Object[][]{
            TableCellHelper.convertToTableCell("1", "5.1", "3.5", "1.4", "0.2", "setosa"),
            TableCellHelper.convertToTableCell("2", "4.9", "3", "1.4", "0.2", "setosa")
    };

    @Before
    public void before() {
        tableCSVReader = new TableCSVReader(new InputStreamReader(getClass().getResourceAsStream("iris.csv")));
    }

    @After
    public void after() throws Exception {
        tableCSVReader.close();
    }

    @Test
    public void testRead() {
        tableCSVReader.setUseHeader(true);
        Assert.assertArrayEquals(header, tableCSVReader.getHeader());
        List<TableCell[]> alldata = tableCSVReader.readAll();
        Assert.assertEquals(150, alldata.size());
        Assert.assertArrayEquals(contentHead[0], alldata.get(0));
        Assert.assertArrayEquals(contentHead[1], alldata.get(1));
        Assert.assertTrue(tableCSVReader.isUseHeader());
        Assert.assertFalse(tableCSVReader.iterator().hasNext());
    }
}
