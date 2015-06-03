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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TableCellHelper {

    public static TableCell[] convertToTableCell(Object... objs) {
        return Stream.of(objs).map(AdaptiveTableCellImpl::new).toArray(TableCell[]::new);
    }

    public static List<TableCell> convertToTableCell(List<Object> objs) {
        return objs.stream().map(AdaptiveTableCellImpl::new).collect(Collectors.toList());
    }

    public static Object[] convertFromTableCell(TableCell ...cells) {
        return Stream.of(cells).map(TableCell::toObject).toArray(Object[]::new);
    }

    public static List<Object> convertFromTableCell(List<TableCell> cells) {
        return cells.stream().map(TableCell::toObject).collect(Collectors.toList());
    }
}
