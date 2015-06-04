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

import info.informationsea.tableio.TableCell;
import info.informationsea.tableio.TableRecord;
import info.informationsea.tableio.impl.AdaptiveTableCellImpl;
import info.informationsea.tableio.impl.TableCellHelper;
import info.informationsea.tableio.impl.TableRecordImpl;
import javafx.scene.control.Tab;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

public class TableRecordTest {
    TableRecord[] record;
    TableRecord[] withHeader;

    @Before
    public void before() {
        record = new TableRecord[]{
                new TableRecordImpl(new String[]{"A", "B", "C"}, TableCellHelper.convertToTableCell(1, 2., "X")),
                new TableRecordImpl(TableRecordImpl.createHeaderMap(new String[]{"A", "B", "C"}), TableCellHelper.convertToTableCell(1, 2., "X")),
                new TableRecordImpl(TableCellHelper.convertToTableCell(1, 2., "X"))
        };
        withHeader = new TableRecord[]{record[0], record[1]};
    }

    @Test
    public void testGet() {
        for (TableRecord one : record) {
            Assert.assertEquals(new AdaptiveTableCellImpl(1), one.get(0));
            Assert.assertEquals(new AdaptiveTableCellImpl(2.), one.get(1));
            Assert.assertEquals(new AdaptiveTableCellImpl("X"), one.get(2));
        }
    }

    @Test
    public void testKeyGet() {
        for (TableRecord one : withHeader) {
            Assert.assertEquals(new AdaptiveTableCellImpl(1), one.get("A"));
            Assert.assertEquals(new AdaptiveTableCellImpl(2.), one.get("B"));
            Assert.assertEquals(new AdaptiveTableCellImpl("X"), one.get("C"));
        }
    }

    @Test
    public void testGetContent() {
        for (TableRecord one : record) {
            Assert.assertArrayEquals(TableCellHelper.convertToTableCell(1, 2., "X"), one.getContent());
        }
    }

    @Test
    public void testGetHeader() {
        for (TableRecord one : withHeader) {
            Assert.assertArrayEquals(new String[]{"A", "B", "C"}, one.getHeader());
        }
    }

    @Test
    public void testSize() {
        for (TableRecord one : record) {
            Assert.assertEquals(3, one.size());
        }
    }

    @Test
    public void testIterator() {
        TableCell[] expected = TableCellHelper.convertToTableCell(1, 2., "X");

        for (TableRecord one : record) {
            Assert.assertEquals(expected.length, one.size());
            Iterator<TableCell> it = one.iterator();
            for (TableCell anExpected : expected) {
                Assert.assertEquals(anExpected, it.next());
            }
        }
    }

    @Test
    public void testEqual() {
        Assert.assertEquals(
                new TableRecordImpl(new String[]{"A", "B", "C"}, TableCellHelper.convertToTableCell(1, 2., "X")),
                record[0]);
    }

    @Test
    public void testEqual2() {
        Assert.assertNotEquals(
                new TableRecordImpl(new String[]{"A", "B", "C"}, TableCellHelper.convertToTableCell(1, 2., "X")),
                record[2]);
    }

    @Test
    public void testSmallRowSize() {
        TableRecord record = new TableRecordImpl(new String[]{"A", "B", "C"}, TableCellHelper.convertToTableCell(1, 2));
        Assert.assertEquals(3, record.size());
    }
}
