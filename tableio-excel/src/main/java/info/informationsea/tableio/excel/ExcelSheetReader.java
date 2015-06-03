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
import info.informationsea.tableio.impl.AbstractTableWithHeaderReader;
import info.informationsea.tableio.impl.AdaptiveTableCellImpl;
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
    protected TableCell[] readNextRow() {
        if (sheet.getLastRowNum() < currentRow)
            return null;

        Row row = sheet.getRow(currentRow);
        if (row == null) {
            currentRow += 1;
            return new TableCell[0];
        }

        TableCell[] rowObjects = new TableCell[row.getLastCellNum()];
        for (Cell cell : row) {
            rowObjects[cell.getColumnIndex()] = new ExcelCell(cell);
        }

        for (int i = 0; i < rowObjects.length; i++) {
            if (rowObjects[i] == null)
                rowObjects[i] = new AdaptiveTableCellImpl();
        }

        currentRow += 1;
        return rowObjects;
    }

    @Override
    public void close() throws Exception {
        // do nothing
    }
}
