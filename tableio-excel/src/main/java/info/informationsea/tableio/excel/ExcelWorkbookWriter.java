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

import info.informationsea.tableio.TableWorkbookWriter;
import info.informationsea.tableio.TableWriter;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Workbook;

@RequiredArgsConstructor
public class ExcelWorkbookWriter implements TableWorkbookWriter {
    @NonNull
    protected Workbook workbook;

    @Setter @Getter
    private boolean prettyTable;

    @Override
    public TableWriter createTable(String tableName) throws Exception {
        ExcelSheetWriter writer = new ExcelSheetWriter(workbook.createSheet(tableName));
        writer.setPrettyTable(prettyTable);
        return writer;
    }

    @Override
    public void close() throws Exception {

    }
}
