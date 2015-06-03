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

package info.informationsea.tableio.excel.test;

import info.informationsea.tableio.excel.ExcelCell;
import org.junit.Assert;
import org.junit.Test;

public class ExcelCellTest {
    @Test
    public void testNumericGeneral() {
        Assert.assertEquals("1", ExcelCell.numericFormat("General", 1.0));
        Assert.assertEquals("1.2", ExcelCell.numericFormat("General", 1.2));
        Assert.assertEquals("0.52", ExcelCell.numericFormat("General", 0.52));
    }

    @Test
    public void testNumericFloat() {
        Assert.assertEquals("1.00", ExcelCell.numericFormat("0.00_ ", 1.0));
        Assert.assertEquals("1.20", ExcelCell.numericFormat("0.00_ ", 1.2));
        Assert.assertEquals("0.5200", ExcelCell.numericFormat("0.0000_ ", 0.52));
    }

    @Test
    public void testNumericExp() {
        Assert.assertEquals("1.00E+00", ExcelCell.numericFormat("0.00E+00", 1.0));
        Assert.assertEquals("1.20E+00", ExcelCell.numericFormat("0.00E+00", 1.2));
        Assert.assertEquals("5.20E-03", ExcelCell.numericFormat("0.00E+00", 0.0052));
    }

}
