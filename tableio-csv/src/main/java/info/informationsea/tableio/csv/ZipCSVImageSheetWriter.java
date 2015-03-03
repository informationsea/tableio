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

import info.informationsea.tableio.ImageSheetWriter;

import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipCSVImageSheetWriter implements ImageSheetWriter {

    private ZipOutputStream zipOutputStream;
    private String name;
    private int imageCount = 1;

    public ZipCSVImageSheetWriter(ZipOutputStream zipOutputStream, String name) throws IOException {
        this.zipOutputStream = zipOutputStream;
        this.name = name;
        zipOutputStream.putNextEntry(new ZipEntry(name+"/"));
        zipOutputStream.closeEntry();
    }

    @Override
    public void close() throws Exception {
        // do nothing
    }

    @Override
    public void addImage(ImageType type, byte[] data) throws IOException {
        String fileName = name + "/image-" + imageCount;
        switch (type) {
            case TYPE_JPEG:
                fileName += ".jpg";
                break;
            case TYPE_PNG:
                fileName += ".png";
                break;
        }
        zipOutputStream.putNextEntry(new ZipEntry(fileName));
        zipOutputStream.write(data);
        zipOutputStream.closeEntry();

        imageCount += 1;
    }
}
