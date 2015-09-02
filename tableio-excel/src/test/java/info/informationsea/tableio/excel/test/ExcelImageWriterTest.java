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


package info.informationsea.tableio.excel.test;

import info.informationsea.tableio.ImageSheetWriter;
import info.informationsea.tableio.TableWorkbookWriter;
import info.informationsea.tableio.excel.ExcelImageSheetWriter;
import info.informationsea.tableio.excel.ExcelWorkbookWriter;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class ExcelImageWriterTest {
    @Test
    public void testWriteImage() throws Exception {
        File buildDir = new File(System.getProperty("user.dir"), "build");
        File testOutput = new File(buildDir, "test-data");
        testOutput.mkdirs();

        SXSSFWorkbook workbook = new SXSSFWorkbook();
        TableWorkbookWriter workbookWriter = new ExcelWorkbookWriter(workbook);
        ImageSheetWriter imageSheetWriter = workbookWriter.createImageSheet("testsheet");
        imageSheetWriter.addImage(ExcelImageSheetWriter.ImageType.TYPE_JPEG, IOUtils.toByteArray(getClass().getResourceAsStream("ashinari-osaka.jpg")));
        imageSheetWriter.addImage(ExcelImageSheetWriter.ImageType.TYPE_PNG, IOUtils.toByteArray(getClass().getResourceAsStream("ashinari-momiji.png")));

        List<? extends PictureData> list = workbook.getAllPictures();
        for (PictureData one : list) {
            if (one.suggestFileExtension().endsWith("jpeg")) {
                Assert.assertArrayEquals(IOUtils.toByteArray(getClass().getResourceAsStream("ashinari-osaka.jpg")), one.getData());
            } else if (one.suggestFileExtension().endsWith("png")) {
                Assert.assertArrayEquals(IOUtils.toByteArray(getClass().getResourceAsStream("ashinari-momiji.png")), one.getData());
            } else {
                Assert.fail();
            }
        }

        try(FileOutputStream outputStream = new FileOutputStream(new File(testOutput, "image.xlsx"))) {
            workbook.write(outputStream);
        }
    }
}
