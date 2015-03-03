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

package info.informationsea.tableio.excel;

import info.informationsea.tableio.ImageSheetWriter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;

@RequiredArgsConstructor
public class ExcelImageSheetWriter implements ImageSheetWriter {
    @NonNull
    private Sheet sheet;

    private int nextRow = 1;

    public void addImage(ImageType type, byte[] data) {
        int pictureType;
        switch (type) {
            case TYPE_JPEG:
                pictureType = Workbook.PICTURE_TYPE_JPEG;
                break;
            case TYPE_PNG:
                pictureType = Workbook.PICTURE_TYPE_PNG;
                break;
            default:
                throw new IllegalArgumentException("Image type should be jpeg or png");
        }

        int pictureIndex = sheet.getWorkbook().addPicture(data, pictureType);

        CreationHelper creationHelper = sheet.getWorkbook().getCreationHelper();
        Drawing drawing = sheet.createDrawingPatriarch();
        ClientAnchor anchor = creationHelper.createClientAnchor();
        anchor.setCol1(1);
        anchor.setRow1(nextRow);
        Picture picture = drawing.createPicture(anchor, pictureIndex);
        picture.resize();
        nextRow = picture.getPreferredSize().getRow2()+1;
    }

    @Override
    public void close() throws Exception {
        // do nothing
    }

}
