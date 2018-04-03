/*
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


package info.informationsea.tableio;

/**
 * A writer of table bundle
 */
public interface TableWorkbookWriter extends AutoCloseable {

    /**
     * Create a new table writer. Please close previous table writer before or image writer you create new table writer.
     * @param tableName a name of new table
     * @return a table writer
      @throws Exception failed to create table
     */
    TableWriter createTable(String tableName) throws Exception;

    /**
     * Create a new image sheet writer. Please close previous table writer or image writer before you create new table writer.
     * @param sheetName a name of new sheet
     * @return a image writer
     * @throws Exception failed to create table
     */
    ImageSheetWriter createImageSheet(String sheetName) throws Exception;
}
