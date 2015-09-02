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

package info.informationsea.tableio.excel;

import info.informationsea.tableio.TableCell;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.poi.ss.usermodel.Cell;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@EqualsAndHashCode
public class ExcelCell implements TableCell {

    @Getter
    private Cell cell;
    @Getter
    private CellType cellType = CellType.BLANK;

    public ExcelCell(Cell cell) {
        this.cell = cell;

        if (cell == null)
            return;

        try {
            cell.getStringCellValue();
            cellType = CellType.STRING;
            return;
        } catch (IllegalStateException e) {}

        try {
            cell.getNumericCellValue();
            cellType = CellType.NUMERIC;
            return;
        } catch (IllegalStateException e) {}

        try {
            cell.getBooleanCellValue();
            cellType = CellType.BOOLEAN;
            return;
        } catch (IllegalStateException e) {}
    }

    @Override
    public boolean isEmptyCell() {
        return getCellType() == CellType.BLANK;
    }

    @Override
    public boolean toBoolean() {
        switch (cellType) {
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case STRING:
                return Boolean.parseBoolean(cell.getStringCellValue());
            case BLANK:
            case NUMERIC:
            default:
                return false;
        }
    }

    @Override
    public double toNumeric() {
        switch (cellType) {
            case NUMERIC:
                return cell.getNumericCellValue();
            case STRING:
                return Double.parseDouble(cell.getStringCellValue());
            case BOOLEAN:
            case BLANK:
            default:
                throw new NumberFormatException();
        }
    }

    @Override
    public String toString() {
        switch (cellType) {
            case NUMERIC:
                return numericFormat(cell.getCellStyle().getDataFormatString(), cell.getNumericCellValue());
            case STRING:
                return cell.getStringCellValue();
            case BOOLEAN:
                return cell.getBooleanCellValue() ? "TRUE" : "FALSE";
            case BLANK:
            default:
                return "";
        }
    }

    @Override
    public String getFormula() {
        if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
            return cell.getCellFormula();
        }
        return null;
    }

    /// excel style numeric formatter

    public static Pattern FLOAT_PATTERN = Pattern.compile("^0\\.(0+)_ $");
    public static Pattern EXP_PATTERN = Pattern.compile("^0\\.(0+)E\\+(0+)$");

    public static String numericFormat(String dataFormat, double value) {
        Matcher matcher = FLOAT_PATTERN.matcher(dataFormat);
        if (matcher.matches()) {
            int precision = matcher.group(1).length();
            return String.format("%."+precision+"f", value);
        }

        matcher = EXP_PATTERN.matcher(dataFormat);
        if (matcher.matches()) {
            int precision = matcher.group(1).length();
            return String.format("%."+precision+"E", value);
        }

        // General
        String stringRepresentation = Double.toString(value);
        if (stringRepresentation.endsWith(".0"))
            return stringRepresentation.substring(0, stringRepresentation.length()-2);
        return stringRepresentation;
    }


    @Override
    public Object toObject() {
        switch (getCellType()) {
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
}
