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

package info.informationsea.tableio;

public interface TableCell {

    enum TableCellType {
        BLANK,
        BOOLEAN,
        STRING,
        NUMERIC,
        FORMULA
    }

    /**
     * Return a cell type.
     * @return a type of this cell
     */
    TableCellType getTableCellType();

    /**
     *
     * @return true if this cell type is blank
     */
    boolean isEmptyCell();

    /**
     * Boolean representation of a cell.
     * Return false if not parsable as boolean.
     * @return true if a value is true, otherwise return false
     */
    boolean toBoolean();

    /**
     * Numeric representation of a cell content
     * @throws NumberFormatException if the string does not contain a parsable double.
     * @return a numeric value
     */
    double toNumeric();

    /**
     * String representation of a cell content
     * @return string
     */
    @Override
    String toString();

    /**
     * Get a formula if this cell has a formula
     * @return a formula
     */
    String getFormula();

    /**
     * Suitable Object representation
     * @return One of Boolean, Double or String
     */
    Object toObject();

    /*
    default Object toObject() {
        switch (getTableCellType()) {
            case NUMERIC:
                return toNumeric();
            case BOOLEAN:
                return toBoolean();
            case STRING:
                return toString();
            case BLANK:
            default:
                return null;
        }
    }
    */
}
