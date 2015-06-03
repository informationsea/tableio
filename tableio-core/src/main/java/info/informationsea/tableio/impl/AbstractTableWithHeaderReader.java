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

package info.informationsea.tableio.impl;

import info.informationsea.tableio.TableCell;
import info.informationsea.tableio.TableRecord;
import lombok.Getter;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public abstract class AbstractTableWithHeaderReader extends AbstractTableReader {

    @Getter
    private boolean useHeader = false;
    private boolean firstRead = true;
    private String[] header = null;
    private Map<String, Integer> headerMap = null;
    private RowIterator rowIterator = null;

    protected abstract TableCell[] readNextRow();

    private void processFirstRead() {
        if (useHeader) {
            TableCell[] row = readNextRow();
            header = new String[row.length];
            for (int i = 0; i < row.length; i++) {
                header[i] = row[i].toString();
            }
            headerMap = TableRecordImpl.createHeaderMap(header);
        }
        firstRead = false;
    }

    public void setUseHeader(boolean useHeader) {
        if (!firstRead)
            throw new RuntimeException("Cannot set useHeader after starting reading");
        this.useHeader = useHeader;
    }

    @Override
    public String[] getHeader() {
        if (firstRead) processFirstRead();
        return header;
    }

    @Override
    public Iterator<TableRecord> iterator() {
        if (rowIterator != null)
            return rowIterator;

        if (firstRead) processFirstRead();

        rowIterator = new RowIterator();
        return rowIterator;
    }

    private class RowIterator implements Iterator<TableRecord> {

        public RowIterator() {
            readNext();
        }

        TableRecord nextObject = null;

        private void readNext() {
            TableCell[] row = readNextRow();
            if (row == null) {
                nextObject = null;
                return;
            }

            if (headerMap != null)
                nextObject = new TableRecordImpl(headerMap, row);
            else
                nextObject = new TableRecordImpl(row);
        }

        @Override
        public boolean hasNext() {
            return nextObject != null;
        }

        @Override
        public TableRecord next() {
            if (nextObject == null)
                throw new NoSuchElementException();
            TableRecord returnObj = nextObject;
            readNext();

            return returnObj;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }


}
