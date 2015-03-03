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

package info.informationsea.tableio.csv.test;

import info.informationsea.tableio.ImageSheetWriter;
import info.informationsea.tableio.csv.ZipCSVImageSheetWriter;
import info.informationsea.tableio.csv.ZipCSVTableWorkbookWriter;
import org.apache.poi.util.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipCSVImageSheetWriterTest {
    @Test
    public void testSheets() throws Exception {
        Path tempFile = Files.createTempFile(null, ".zip");
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(tempFile.toFile()))) {
            ZipCSVImageSheetWriter imageSheetWriter = new ZipCSVImageSheetWriter(zipOutputStream, "test");
            imageSheetWriter.addImage(ImageSheetWriter.ImageType.TYPE_PNG, IOUtils.toByteArray(getClass().getResourceAsStream("ashinari-momiji.png")));
            imageSheetWriter.addImage(ImageSheetWriter.ImageType.TYPE_JPEG, IOUtils.toByteArray(getClass().getResourceAsStream("ashinari-osaka.jpg")));
        }

        ZipFile zipFile = new ZipFile(tempFile.toFile());
        Assert.assertArrayEquals(IOUtils.toByteArray(getClass().getResourceAsStream("ashinari-momiji.png")), IOUtils.toByteArray(zipFile.getInputStream(zipFile.getEntry("test/image-1.png"))));
        Assert.assertArrayEquals(IOUtils.toByteArray(getClass().getResourceAsStream("ashinari-osaka.jpg")), IOUtils.toByteArray(zipFile.getInputStream(zipFile.getEntry("test/image-2.jpg"))));
    }

    @Test
    public void testSheets2() throws Exception {
        Path tempFile = Files.createTempFile(null, ".zip");
        try (FileOutputStream outputStream = new FileOutputStream(tempFile.toFile())) {
            ZipCSVTableWorkbookWriter workbookWriter = new ZipCSVTableWorkbookWriter(outputStream);
            ImageSheetWriter imageSheetWriter = workbookWriter.createImageSheet("test");
            imageSheetWriter.addImage(ImageSheetWriter.ImageType.TYPE_PNG, IOUtils.toByteArray(getClass().getResourceAsStream("ashinari-momiji.png")));
            imageSheetWriter.addImage(ImageSheetWriter.ImageType.TYPE_JPEG, IOUtils.toByteArray(getClass().getResourceAsStream("ashinari-osaka.jpg")));
            workbookWriter.close();
        }

        ZipFile zipFile = new ZipFile(tempFile.toFile());
        Assert.assertArrayEquals(IOUtils.toByteArray(getClass().getResourceAsStream("ashinari-momiji.png")), IOUtils.toByteArray(zipFile.getInputStream(zipFile.getEntry("test/image-1.png"))));
        Assert.assertArrayEquals(IOUtils.toByteArray(getClass().getResourceAsStream("ashinari-osaka.jpg")), IOUtils.toByteArray(zipFile.getInputStream(zipFile.getEntry("test/image-2.jpg"))));
    }
}
