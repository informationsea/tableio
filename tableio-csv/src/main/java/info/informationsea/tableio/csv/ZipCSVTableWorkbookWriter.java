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


package info.informationsea.tableio.csv;

import info.informationsea.tableio.TableWorkbookWriter;
import info.informationsea.tableio.TableWriter;
import info.informationsea.tableio.csv.format.DefaultFormat;
import info.informationsea.tableio.csv.format.TableCSVFormat;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipCSVTableWorkbookWriter implements TableWorkbookWriter {

    private ZipOutputStream zipOutputStream;
    private String suffix = ".csv";
    private TableCSVFormat format = new DefaultFormat();

    public ZipCSVTableWorkbookWriter(OutputStream outputStream) {
        zipOutputStream = new ZipOutputStream(outputStream);
    }

    public ZipCSVTableWorkbookWriter(OutputStream outputStream, TableCSVFormat format) {
        zipOutputStream = new ZipOutputStream(outputStream);
        this.format = format;
    }

    public ZipCSVTableWorkbookWriter(OutputStream outputStream, TableCSVFormat format, String suffix) {
        zipOutputStream = new ZipOutputStream(outputStream);
        this.format = format;
        this.suffix = suffix;
    }

    @Override
    public TableWriter createTable(String tableName) throws IOException {
        zipOutputStream.putNextEntry(new ZipEntry(tableName+suffix));
        return new TableCSVWriter(new OutputStreamWriter(new ZipEntryOutputStream(zipOutputStream)), format);
    }

    @Override
    public void close() throws Exception {
        zipOutputStream.close();
    }

    @RequiredArgsConstructor
    private static class ZipEntryOutputStream extends OutputStream {
        @NonNull
        private ZipOutputStream zipOutputStream;

        @Override
        public void write(int b) throws IOException {
            zipOutputStream.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            zipOutputStream.write(b, off, len);
        }

        @Override
        public void close() throws IOException {
            zipOutputStream.closeEntry();
        }

        @Override
        public void flush() throws IOException {
            zipOutputStream.flush();
        }
    }
}
