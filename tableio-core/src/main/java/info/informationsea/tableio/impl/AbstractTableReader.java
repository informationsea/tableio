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
import info.informationsea.tableio.TableReader;
import info.informationsea.tableio.TableRecord;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTableReader implements TableReader {
    @Override
    public List<TableCell[]> readAll() {
        ArrayList<TableCell[]> list = new ArrayList<>();
        for (TableRecord record : this) {
            list.add(record.getContent());
        }
        return list;
    }
}
