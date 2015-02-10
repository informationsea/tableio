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

import info.informationsea.tableio.impl.AbstractTableWriter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Calendar;
import java.util.Date;

@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExcelSheetWriter extends AbstractTableWriter {
    @NonNull
    protected Sheet sheet;
    private int currentRow = 0;

    @Override
    public void printRecord(Object... values) {
        Row row = sheet.createRow(currentRow);
        for (int i = 0; i < values.length; i++) {
            if (values[i] instanceof Boolean)
                row.createCell(i, Cell.CELL_TYPE_BOOLEAN).setCellValue((Boolean) values[i]);
            else if (values[i] instanceof Number)
                row.createCell(i, Cell.CELL_TYPE_NUMERIC).setCellValue(((Number) values[i]).doubleValue());
            else if (values[i] instanceof Calendar)
                row.createCell(i, Cell.CELL_TYPE_STRING).setCellValue((Calendar) values[i]);
            else if (values[i] instanceof Date)
                row.createCell(i, Cell.CELL_TYPE_STRING).setCellValue((Date) values[i]);
            else
                row.createCell(i, Cell.CELL_TYPE_STRING).setCellValue(values[i].toString());
        }

        currentRow += 1;
    }
}
