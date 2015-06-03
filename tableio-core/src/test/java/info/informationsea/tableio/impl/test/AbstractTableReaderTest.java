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
import info.informationsea.tableio.impl.AbstractTableReader;
import info.informationsea.tableio.impl.AdaptiveTableCellImpl;
import info.informationsea.tableio.impl.TableCellHelper;
import info.informationsea.tableio.impl.TableRecordImpl;
import lombok.AllArgsConstructor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class AbstractTableReaderTest {

    TableRecord[] records = new TableRecord[]{
            new TableRecordImpl(TableCellHelper.convertToTableCell(1, 2)),
            new TableRecordImpl(TableCellHelper.convertToTableCell(3, 4))
    };

    TableReaderImpl tableReader;

    @Before
    public void before() {
        tableReader = new TableReaderImpl(Arrays.asList(records));
    }

    @Test
    public void testReadAll() {
        List<TableCell[]> all = tableReader.readAll();
        for (int i = 0; i < records.length; i++) {
            Assert.assertArrayEquals(records[i].getContent(), all.get(i));
        }
    }

    @AllArgsConstructor
    private class TableReaderImpl extends AbstractTableReader {
        List<TableRecord> list;

        @Override
        public String[] getHeader() {
            return new String[0];
        }

        @Override
        public Iterator<TableRecord> iterator() {
            return list.iterator();
        }

        @Override
        public void close() throws Exception {

        }
    }
}
