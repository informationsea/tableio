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

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;

public class XlsReader extends ExcelSheetReader {

    public XlsReader(InputStream is) throws IOException {
        super(new HSSFWorkbook(is).getSheetAt(0));
    }

    public XlsReader(InputStream is, int sheetIndex) throws IOException {
        super(new HSSFWorkbook(is).getSheetAt(sheetIndex));
    }

    public XlsReader(InputStream is, String sheetName) throws IOException {
        super(new HSSFWorkbook(is).getSheet(sheetName));
    }
}
