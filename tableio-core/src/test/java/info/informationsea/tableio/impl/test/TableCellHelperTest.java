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
import info.informationsea.tableio.impl.AdaptiveTableCellImpl;
import info.informationsea.tableio.impl.TableCellHelper;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class TableCellHelperTest {

    @Test
    public void testConvertToTableCell() throws Exception {
        Assert.assertArrayEquals(
                new TableCell[]{new AdaptiveTableCellImpl(true), new AdaptiveTableCellImpl(1.0)},
                TableCellHelper.convertToTableCell(true, 1.0));
    }

    @Test
    public void testConvertToTableCell1() throws Exception {
        Assert.assertEquals(
                Arrays.asList(new AdaptiveTableCellImpl(true), new AdaptiveTableCellImpl(1.0)),
                TableCellHelper.convertToTableCell(Arrays.asList(true, 1.0)));
    }

    @Test
    public void testConvertFromTableCell() throws Exception {
        Assert.assertArrayEquals(new Object[]{1.0, false},
                TableCellHelper.convertFromTableCell(new AdaptiveTableCellImpl(1.0), new AdaptiveTableCellImpl(false)));
    }

    @Test
    public void testConvertFromTableCell1() throws Exception {
        Assert.assertEquals(Arrays.asList(1.0, false),
                TableCellHelper.convertFromTableCell(Arrays.asList(new AdaptiveTableCellImpl(1.0), new AdaptiveTableCellImpl(false))));
    }
}