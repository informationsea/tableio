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

package info.informationsea.tableio.impl;

import info.informationsea.tableio.TableCell;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TableCellHelper {

    public static TableCell[] convertToTableCell(Object... objs) {
        TableCell[] result = new TableCell[objs.length];
        for (int i = 0; i < objs.length; i++) {
            result[i] = new AdaptiveTableCellImpl(objs[i]);
        }
        return result;
    }

    public static List<TableCell> convertToTableCell(List<Object> objs) {
        ArrayList<TableCell> result = new ArrayList<>();
        for (Object one : objs) {
            result.add(new AdaptiveTableCellImpl(one));
        }
        return result;
    }

    public static Object[] convertFromTableCell(TableCell ...cells) {
        Object[] result = new Object[cells.length];
        for (int i = 0; i < cells.length; i++) {
            result[i] = cells[i].toObject();
        }
        return result;
    }

    public static List<Object> convertFromTableCell(List<TableCell> cells) {
        ArrayList<Object> result = new ArrayList<>();
        for (TableCell one : cells) {
            result.add(one.toObject());
        }
        return result;
    }
}
