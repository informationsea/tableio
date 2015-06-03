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
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
public class AdaptiveTableCellImpl implements TableCell {

    boolean booleanValue = false;
    double doubleValue = Double.NaN;
    String stringValue = null;

    @Getter
    CellType cellType = CellType.BLANK;

    public AdaptiveTableCellImpl() {}

    public AdaptiveTableCellImpl(Object data) {
        if (data == null) {
            cellType = CellType.BLANK;
        } else if (data instanceof Number) {
            doubleValue = ((Number) data).doubleValue();
            cellType = CellType.NUMERIC;
        } else if (data instanceof Boolean) {
            booleanValue = (Boolean)data;
            cellType = CellType.BOOLEAN;
        } else if (data instanceof String) {
            stringValue = (String)data;
            cellType = CellType.STRING;
        } else if (data instanceof AdaptiveTableCellImpl) {
            booleanValue = ((AdaptiveTableCellImpl) data).booleanValue;
            stringValue = ((AdaptiveTableCellImpl) data).stringValue;
            doubleValue = ((AdaptiveTableCellImpl) data).doubleValue;
            cellType = ((AdaptiveTableCellImpl) data).cellType;
        } else {
            throw new IllegalArgumentException(String.format("%s is not supported", data.getClass().toString()));
        }
    }

    @Override
    public boolean isEmptyCell() {
        return cellType == CellType.BLANK;
    }

    @Override
    public boolean toBoolean() {
        switch (cellType) {
            case STRING:
                return Boolean.parseBoolean(stringValue);
            case BOOLEAN:
                return booleanValue;
            case NUMERIC:
            case BLANK:
            default:
                return false;
        }
    }

    @Override
    public double toNumeric() {
        switch (cellType) {
            case STRING:
                return Double.parseDouble(stringValue);
            case NUMERIC:
                return doubleValue;
            case BOOLEAN:
            case BLANK:
            default:
                throw new NumberFormatException();
        }
    }

    @Override
    public String toString() {
        switch (cellType) {
            case STRING:
                return stringValue;
            case NUMERIC:
                return Double.toString(doubleValue);
            case BOOLEAN:
                return Boolean.toString(booleanValue);
            case BLANK:
            default:
                return "";
        }
    }
}
