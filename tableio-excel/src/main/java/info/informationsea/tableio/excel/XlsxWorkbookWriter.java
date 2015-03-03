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

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.OutputStream;

public class XlsxWorkbookWriter extends ExcelWorkbookWriter {

    private OutputStream outputStream;

    public XlsxWorkbookWriter(OutputStream outputStream) {
        super(new SXSSFWorkbook());
        this.outputStream = outputStream;
    }

    @Override
    public void close() throws Exception {
        super.close();
        workbook.write(outputStream);
        outputStream.close();
    }
}
