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
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class TableCellTest {
    @Test
    public void testEmptyCell() {
        AdaptiveTableCellImpl empty = new AdaptiveTableCellImpl();
        Assert.assertFalse(empty.toBoolean());
        Assert.assertEquals("", empty.toString());
        Assert.assertTrue(empty.isEmptyCell());
        Assert.assertEquals(TableCell.TableCellType.BLANK, empty.getTableCellType());
        Assert.assertNull(empty.toObject());
    }

    @Test(expected = NumberFormatException.class)
    public void testEmptyCell2() {
        AdaptiveTableCellImpl empty = new AdaptiveTableCellImpl();
        empty.toNumeric();
    }

    @Test
    public void testEmptyCell3() {
        AdaptiveTableCellImpl empty = new AdaptiveTableCellImpl(null);
        Assert.assertFalse(empty.toBoolean());
        Assert.assertEquals("", empty.toString());
        Assert.assertTrue(empty.isEmptyCell());
        Assert.assertEquals(TableCell.TableCellType.BLANK, empty.getTableCellType());
    }

    @Test(expected = NumberFormatException.class)
    public void testEmptyCell4() {
        AdaptiveTableCellImpl empty = new AdaptiveTableCellImpl(null);
        empty.toNumeric();
    }

    @Test
    public void testStringCell() {
        AdaptiveTableCellImpl str = new AdaptiveTableCellImpl("2.0");
        Assert.assertFalse(str.toBoolean());
        Assert.assertEquals("2.0", str.toString());
        Assert.assertFalse(str.isEmptyCell());
        Assert.assertEquals(TableCell.TableCellType.STRING, str.getTableCellType());
        Assert.assertEquals(2.0, str.toNumeric(), 0.00000001);
    }

    @Test
    public void testStringCell2() {
        AdaptiveTableCellImpl str = new AdaptiveTableCellImpl("Hello");
        Assert.assertFalse(str.toBoolean());
        Assert.assertEquals("Hello", str.toString());
        Assert.assertEquals("Hello", str.toObject());
        Assert.assertFalse(str.isEmptyCell());
        Assert.assertEquals(TableCell.TableCellType.STRING, str.getTableCellType());
    }

    @Test(expected = NumberFormatException.class)
    public void testStringCell3() {
        AdaptiveTableCellImpl str = new AdaptiveTableCellImpl("Hello");
        str.toNumeric();
    }

    @Test
    public void testStringCell4() {
        AdaptiveTableCellImpl str = new AdaptiveTableCellImpl("true");
        Assert.assertTrue(str.toBoolean());
        Assert.assertEquals("true", str.toString());
        Assert.assertFalse(str.isEmptyCell());
        Assert.assertEquals(TableCell.TableCellType.STRING, str.getTableCellType());
    }

    @Test
    public void testDoubleCell1() {
        AdaptiveTableCellImpl doubleTableCell = new AdaptiveTableCellImpl(3.0);
        Assert.assertFalse(doubleTableCell.toBoolean());
        Assert.assertEquals(Double.toString(3.0), doubleTableCell.toString());
        Assert.assertEquals(3.0, doubleTableCell.toNumeric(), 0.0000001);
        Assert.assertFalse(doubleTableCell.isEmptyCell());
    }

    @Test
    public void testBooleanCell1() {
        AdaptiveTableCellImpl str = new AdaptiveTableCellImpl(true);
        Assert.assertTrue(str.toBoolean());
        Assert.assertEquals("true", str.toString());
        Assert.assertFalse(str.isEmptyCell());
        Assert.assertEquals(TableCell.TableCellType.BOOLEAN, str.getTableCellType());
    }

    @Test
    public void testBooleanCell2() {
        AdaptiveTableCellImpl str = new AdaptiveTableCellImpl(false);
        Assert.assertFalse(str.toBoolean());
        Assert.assertEquals("false", str.toString());
        Assert.assertFalse(str.isEmptyCell());
        Assert.assertEquals(TableCell.TableCellType.BOOLEAN, str.getTableCellType());
    }

    @Test
    public void testCopyConstructor() {
        AdaptiveTableCellImpl adaptiveTableCell = new AdaptiveTableCellImpl("Hello");
        AdaptiveTableCellImpl copyConstructor = new AdaptiveTableCellImpl(adaptiveTableCell);

        Assert.assertEquals(TableCell.TableCellType.STRING, copyConstructor.getTableCellType());
        Assert.assertEquals("Hello", copyConstructor.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArgument() {
        new AdaptiveTableCellImpl(new Date());
    }

    @Test
    public void testFormula() {
        Assert.assertFalse(new AdaptiveTableCellImpl("Hello").getFormula() != null);
    }
}
