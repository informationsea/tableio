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

import info.informationsea.tableio.impl.AbstractTableWriter;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class AbstractTableWriterTest {
    public static final Object[][] data = new Object[][]{
            new Object[]{"A", "B", "C"},
            new Object[]{"D", "E", "F"},
    };

    @Test
    public void testPrintAll() {
        new AbstractTableWriter() {
            private int i = 0;

            @Override
            public void printRecord(Object... values) {
                Assert.assertArrayEquals(data[i], values);
                i += 1;
            }
        }.printAll(Arrays.asList(data));

    }
}
