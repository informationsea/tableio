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

import au.com.bytecode.opencsv.CSVWriter;
import info.informationsea.tableio.csv.format.TableCSVFormat;
import info.informationsea.tableio.impl.AbstractTableWriter;

import java.io.Writer;

public class TableCSVWriter extends AbstractTableWriter implements AutoCloseable {

    CSVWriter csvWriter;

    public TableCSVWriter(CSVWriter csvWriter) {
        this.csvWriter = csvWriter;
    }

    public TableCSVWriter(Writer writer) {
        csvWriter = new CSVWriter(writer);
    }

    public TableCSVWriter(Writer writer, TableCSVFormat format) {
        csvWriter = new CSVWriter(writer, format.getSeparator(), format.getQuoteChar(), format.getEscape());
    }

    @Override
    public void printRecord(Object... values) {
        String[] stringValues = new String[values.length];
        for (int i = 0; i < stringValues.length; i++) {
            stringValues[i] = values[i].toString();
        }

        csvWriter.writeNext(stringValues);
    }

    @Override
    public void close() throws Exception {
        csvWriter.close();
    }
}
