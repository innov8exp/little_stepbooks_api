package co.botechservices.novlnovl.infrastructure.util;

import com.poiji.bind.Poiji;
import com.poiji.exception.PoijiExcelType;
import com.poiji.option.PoijiOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
public class ExcelUtil {

    public static <T> List<T> parseExcel(MultipartFile file, Class<T> clazz, int fromRowIndex) {
        try (InputStream inputStream = file.getInputStream()) {
            if (fromRowIndex <= 0) {
                PoijiOptions poijiOptions = PoijiOptions.PoijiOptionsBuilder.settings().build();
                return Poiji.fromExcel(inputStream, PoijiExcelType.XLSX, clazz, poijiOptions);
            }
            PoijiOptions poijiOptions = PoijiOptions.PoijiOptionsBuilder.settings(fromRowIndex).build();
            return Poiji.fromExcel(inputStream, PoijiExcelType.XLSX, clazz, poijiOptions);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
