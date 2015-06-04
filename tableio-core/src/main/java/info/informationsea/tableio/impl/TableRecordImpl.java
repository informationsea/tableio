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
import lombok.EqualsAndHashCode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@EqualsAndHashCode
public class TableRecordImpl implements TableRecord {

    private Map<String, Integer> mHeader;
    private TableCell[] mContent;

    public TableRecordImpl(String[] header, TableCell[] content) {
        this(createHeaderMap(header), content);
    }

    public TableRecordImpl(Map<String, Integer> header, TableCell[] content) {
        mHeader = header;
        if (content.length >= header.size()) {
            mContent = content;
        } else {
            mContent = new TableCell[header.size()];
            System.arraycopy(content, 0, mContent, 0, content.length);
            for (int i = content.length; i < header.size(); i++) {
                mContent[i] = new AdaptiveTableCellImpl();
            }
        }
    }

    public TableRecordImpl(TableCell[] content) {
        mContent = content;
    }

    public static Map<String, Integer> createHeaderMap(String[] header) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (int i = 0; i < header.length; i++) {
            map.put(header[i], i);
        }
        return map;
    }

    @Override
    public TableCell get(int i) {
        return mContent[i];
    }

    @Override
    public TableCell get(String key) {
        return mContent[mHeader.get(key)];
    }

    @Override
    public TableCell[] getContent() {
        return mContent;
    }

    @Override
    public String[] getHeader() {
        String[] header = new String[mHeader.size()];
        for (Map.Entry<String, Integer> one : mHeader.entrySet()) {
            header[one.getValue()] = one.getKey();
        }
        return header;
    }

    @Override
    public int size() {
        return mContent.length;
    }

    @Override
    public Iterator<TableCell> iterator() {
        return Arrays.asList(mContent).iterator();
    }
}
