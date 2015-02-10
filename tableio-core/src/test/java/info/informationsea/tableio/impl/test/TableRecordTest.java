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
import info.informationsea.tableio.impl.TableRecordImpl;
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
                new TableRecordImpl(new String[]{"A", "B", "C"}, new Object[]{1, 2., "X"}),
                new TableRecordImpl(TableRecordImpl.createHeaderMap(new String[]{"A", "B", "C"}), new Object[]{1, 2., "X"}),
                new TableRecordImpl(new Object[]{1, 2., "X"})
        };
        withHeader = new TableRecord[]{record[0], record[1]};
    }

    @Test
    public void testGet() {
        for (TableRecord one : record) {
            Assert.assertEquals(1, one.get(0));
            Assert.assertEquals(2., one.get(1));
            Assert.assertEquals("X", one.get(2));
        }
    }

    @Test
    public void testKeyGet() {
        for (TableRecord one : withHeader) {
            Assert.assertEquals(1, one.get("A"));
            Assert.assertEquals(2., one.get("B"));
            Assert.assertEquals("X", one.get("C"));
        }
    }

    @Test
    public void testGetContent() {
        for (TableRecord one : record) {
            Assert.assertArrayEquals(new Object[]{1, 2., "X"}, one.getContent());
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
        Object[] expected = new Object[]{1, 2., "X"};

        for (TableRecord one : record) {
            Assert.assertEquals(expected.length, one.size());
            Iterator<Object> it = one.iterator();
            for (int i = 0; i < expected.length; i++) {
                Assert.assertEquals(expected[i], it.next());
            }
        }
    }
}
