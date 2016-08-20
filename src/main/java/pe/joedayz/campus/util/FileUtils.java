package pe.joedayz.campus.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by MATRIX-JAVA on 14/5/2016.
 */
public class FileUtils {

    public static void writeToFile(File file, byte[] bytes) throws Exception {
        if (bytes == null) return;
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bytes);
        fos.close();
    }


    public static File createTempFile(String suffix) throws IOException {
        return File.createTempFile("Celtic",suffix);
    }
}
