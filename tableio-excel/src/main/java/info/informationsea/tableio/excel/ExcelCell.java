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
import lombok.AllArgsConstructor;
import lombok.Value;
import org.apache.poi.ss.usermodel.Cell;

@Value @AllArgsConstructor
public class ExcelCell implements TableCell {

    private Cell cell;

    @Override
    public CellType getCellType() {
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_BLANK:
                return CellType.BLANK;
            case Cell.CELL_TYPE_NUMERIC:
                return CellType.NUMERIC;
            case Cell.CELL_TYPE_BOOLEAN:
                return CellType.BOOLEAN;
            case Cell.CELL_TYPE_STRING:
                return CellType.STRING;
            case Cell.CELL_TYPE_FORMULA:
                return CellType.FORMULA;
            case Cell.CELL_TYPE_ERROR:
            default:
                return CellType.ERROR;
        }
    }

    @Override
    public boolean isEmptyCell() {
        return getCellType() == CellType.BLANK;
    }

    @Override
    public boolean toBoolean() {
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_BOOLEAN:
                return cell.getBooleanCellValue();
            case Cell.CELL_TYPE_STRING:
                return Boolean.parseBoolean(cell.getStringCellValue());
            case Cell.CELL_TYPE_BLANK:
            case Cell.CELL_TYPE_NUMERIC:
            case Cell.CELL_TYPE_FORMULA:
            case Cell.CELL_TYPE_ERROR:
            default:
                return false;
        }
    }

    @Override
    public double toNumeric() {
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                return cell.getNumericCellValue();
            case Cell.CELL_TYPE_STRING:
                return Double.parseDouble(cell.getStringCellValue());
            case Cell.CELL_TYPE_BLANK:
            case Cell.CELL_TYPE_FORMULA:
            case Cell.CELL_TYPE_ERROR:
            case Cell.CELL_TYPE_BOOLEAN:
            default:
                throw new NumberFormatException();
        }
    }

    @Override
    public String toString() {
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                return Double.toString(cell.getNumericCellValue());
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
            case Cell.CELL_TYPE_BOOLEAN:
                return Boolean.toString(cell.getBooleanCellValue());
            case Cell.CELL_TYPE_FORMULA:
                return cell.getStringCellValue();
            case Cell.CELL_TYPE_BLANK:
            case Cell.CELL_TYPE_ERROR:
            default:
                return "";
        }
    }
}
