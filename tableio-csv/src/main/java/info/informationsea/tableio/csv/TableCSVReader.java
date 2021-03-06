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

package info.informationsea.tableio.csv;

import au.com.bytecode.opencsv.CSVReader;
import info.informationsea.tableio.TableCell;
import info.informationsea.tableio.csv.format.TableCSVFormat;
import info.informationsea.tableio.impl.AbstractTableWithHeaderReader;
import info.informationsea.tableio.impl.AdaptiveTableCellImpl;

import java.io.IOException;
import java.io.Reader;

public class TableCSVReader extends AbstractTableWithHeaderReader {

    private CSVReader csvReader;

    public TableCSVReader(CSVReader csvReader) {
        this.csvReader = csvReader;
    }

    public TableCSVReader(Reader reader) {
        csvReader = new CSVReader(reader);
    }

    public TableCSVReader(Reader reader, TableCSVFormat format) {
        csvReader = new CSVReader(reader, format.getSeparator(), format.getQuoteChar(), format.getEscape(), 0, format.isStrictQuotes());
    }

    @Override
    public void close() throws Exception {
        csvReader.close();
    }

    @Override
    protected TableCell[] readNextRow() {
        try {
            String[] row = csvReader.readNext();
            if (row == null)
                return null;
            TableCell[] tableCells = new TableCell[row.length];
            for (int i = 0; i < row.length; i++) {
                tableCells[i] = new AdaptiveTableCellImpl(row[i]);
            }
            return tableCells;
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
}
