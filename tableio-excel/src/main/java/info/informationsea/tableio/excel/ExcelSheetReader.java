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

import info.informationsea.tableio.impl.AbstractTableWithHeaderReader;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

@RequiredArgsConstructor
public class ExcelSheetReader extends AbstractTableWithHeaderReader {

    @NonNull
    private Sheet sheet;
    private int currentRow = 0;

    @Override
    protected Object[] readNextRow() {
        if (sheet.getLastRowNum() < currentRow)
            return null;

        Row row = sheet.getRow(currentRow);
        Object[] rowObjects = new Object[row.getLastCellNum()];
        for (Cell cell : row) {
            Object value;
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    value = cell.getBooleanCellValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    value = cell.getNumericCellValue();
                    break;
                case Cell.CELL_TYPE_STRING:
                default:
                    value = cell.getStringCellValue();
                    break;
            }

            rowObjects[cell.getColumnIndex()] = value;
        }
        currentRow += 1;
        return rowObjects;
    }
}
