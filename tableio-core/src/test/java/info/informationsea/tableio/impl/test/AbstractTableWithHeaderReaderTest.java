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

package info.informationsea.tableio.impl.test;

import info.informationsea.tableio.TableRecord;
import info.informationsea.tableio.impl.AbstractTableWithHeaderReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class AbstractTableWithHeaderReaderTest {
    private AbstractTableWithHeaderReader tableCSVReader;
    public static final Object[][] contents = new Object[][]{
            new Object[]{"", "Sepal.Length", "Sepal.Width", "Petal.Length", "Petal.Width", "Species"},
            new Object[]{"1", "5.1", "3.5", "1.4", "0.2", "setosa"},
            new Object[]{"2", "4.9", "3", "1.4", "0.2", "setosa"}
    };

    @Before
    public void before() {
        tableCSVReader = new AbstractTableWithHeaderReader() {
            private int row = 0;

            @Override
            protected Object[] readNextRow() {
                if (row >= contents.length)
                    return null;
                row += 1;
                return contents[row - 1];
            }
        };
    }

    @Test
    public void testHeader() throws IOException {
        tableCSVReader.setUseHeader(true);
        Assert.assertArrayEquals(contents[0], tableCSVReader.getHeader());
        // call twice to make 100% coverage
        Assert.assertArrayEquals(contents[0], tableCSVReader.getHeader());
    }

    @Test
    public void testRead() {
        tableCSVReader.setUseHeader(true);
        Iterator<TableRecord> recordIterator = tableCSVReader.iterator();

        TableRecord record = recordIterator.next();
        Assert.assertArrayEquals(contents[1], record.getContent());
        Assert.assertArrayEquals(contents[0], record.getHeader());

        Assert.assertTrue(recordIterator.hasNext());
        record = recordIterator.next();
        Assert.assertArrayEquals(contents[2], record.getContent());
        Assert.assertArrayEquals(contents[0], record.getHeader());
    }

    @Test
    public void testRead2() {
        List<Object[]> alldata = tableCSVReader.readAll();
        Assert.assertEquals(3, alldata.size());
        Assert.assertArrayEquals(contents[0], alldata.get(0));
        Assert.assertArrayEquals(contents[1], alldata.get(1));
        Assert.assertArrayEquals(contents[2], alldata.get(2));
        Assert.assertFalse(tableCSVReader.isUseHeader());
    }

    @Test
    public void testRead3() {
        tableCSVReader.setUseHeader(true);
        Assert.assertArrayEquals(contents[0], tableCSVReader.getHeader());
        List<Object[]> alldata = tableCSVReader.readAll();
        Assert.assertEquals(2, alldata.size());
        Assert.assertArrayEquals(contents[1], alldata.get(0));
        Assert.assertArrayEquals(contents[2], alldata.get(1));
        Assert.assertTrue(tableCSVReader.isUseHeader());
        Assert.assertFalse(tableCSVReader.iterator().hasNext());
    }


    @Test
    public void testRead4() {
        Iterator<TableRecord> recordIterator = tableCSVReader.iterator();
        Assert.assertTrue(recordIterator.hasNext());
        for (int i = 0; i < 3; i++) {
            recordIterator.next();
        }
        Assert.assertFalse(recordIterator.hasNext());

        boolean exceptionCatched = false;
        try {
            recordIterator.next();
        } catch (NoSuchElementException nsee) {
            exceptionCatched = true;
        }
        Assert.assertTrue(exceptionCatched);
    }

    @Test
    public void testSetUseHeader() {
        tableCSVReader.iterator().next();
        boolean exceptionCatched = false;

        try {
            tableCSVReader.setUseHeader(true);
        } catch (RuntimeException re) {
            exceptionCatched = true;
        }
        Assert.assertTrue(exceptionCatched);
    }
}
