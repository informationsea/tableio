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

@EqualsAndHashCode
public class AdaptiveTableCellImpl implements TableCell {

    boolean booleanValue = false;
    double doubleValue = Double.NaN;
    String stringValue = null;
    String formulaValue = null;

    @Getter
    TableCellType tableCellType = TableCellType.BLANK;

    public AdaptiveTableCellImpl() {}

    public AdaptiveTableCellImpl(Object data) {
        if (data == null) {
            tableCellType = TableCellType.BLANK;
        } else if (data instanceof Number) {
            doubleValue = ((Number) data).doubleValue();
            tableCellType = TableCellType.NUMERIC;
        } else if (data instanceof Boolean) {
            booleanValue = (Boolean)data;
            tableCellType = TableCellType.BOOLEAN;
        } else if (data instanceof String) {
            if (((String) data).startsWith("=")) {
                formulaValue = ((String) data).substring(1);
                tableCellType = TableCellType.FORMULA;
            } else {
                stringValue = (String) data;
                tableCellType = TableCellType.STRING;
            }
        } else if (data instanceof AdaptiveTableCellImpl) {
            booleanValue = ((AdaptiveTableCellImpl) data).booleanValue;
            stringValue = ((AdaptiveTableCellImpl) data).stringValue;
            doubleValue = ((AdaptiveTableCellImpl) data).doubleValue;
            tableCellType = ((AdaptiveTableCellImpl) data).tableCellType;
        } else {
            throw new IllegalArgumentException(String.format("%s is not supported", data.getClass().toString()));
        }
    }

    @Override
    public boolean isEmptyCell() {
        return tableCellType == TableCellType.BLANK;
    }

    @Override
    public boolean toBoolean() {
        switch (tableCellType) {
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
        switch (tableCellType) {
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
        switch (tableCellType) {
            case STRING:
                return stringValue;
            case FORMULA:
                return formulaValue;
            case NUMERIC:
                return Double.toString(doubleValue);
            case BOOLEAN:
                return Boolean.toString(booleanValue);
            case BLANK:
            default:
                return "";
        }
    }

    @Override
    public String getFormula() {
        return formulaValue;
    }

    @Override
    public Object toObject() {
        switch (this.getTableCellType()) {
            case NUMERIC:
                return toNumeric();
            case BOOLEAN:
                return toBoolean();
            case STRING:
                return toString();
            case FORMULA:
                return getFormula();
            case BLANK:
            default:
                return null;
        }
    }
}
