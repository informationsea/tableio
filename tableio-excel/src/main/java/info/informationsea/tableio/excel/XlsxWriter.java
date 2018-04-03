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

import lombok.Getter;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.OutputStream;


public class XlsxWriter extends ExcelSheetWriter implements AutoCloseable {

    @Getter
    protected Workbook workbook;
    private OutputStream outputStream;

    public XlsxWriter(OutputStream os) {
        workbook = new SXSSFWorkbook();

        setSheet(workbook.createSheet("Sheet1"));
        outputStream = os;
    }

    public XlsxWriter(OutputStream os, String sheetName) {
        workbook = new SXSSFWorkbook();
        setSheet(workbook.createSheet(sheetName));
        outputStream = os;
    }

    @Override
    public void close() throws Exception {
        super.close();
        workbook.write(outputStream);
        outputStream.close();
    }
}
