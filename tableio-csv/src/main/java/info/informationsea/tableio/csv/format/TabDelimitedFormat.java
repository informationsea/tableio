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

package info.informationsea.tableio.csv.format;

import au.com.bytecode.opencsv.CSVWriter;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor
public class TabDelimitedFormat implements TableCSVFormat {
    private char separator = '\t';
    private char quoteChar = CSVWriter.NO_QUOTE_CHARACTER;
    private char escape = CSVWriter.NO_ESCAPE_CHARACTER;
    private boolean strictQuotes = false;
}
