package util;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public class FileUtil {
    public static @NotNull String getFileExtension(@NotNull File file) {
        return file.getName().substring(file.getName().lastIndexOf("." + 1));
    }
}
