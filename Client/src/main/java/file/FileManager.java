package file;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

public class FileManager {
    public static FileManager getInstance() {
        return LazyHolder.INSTANCE;
    }

    public List<ClientFile> getListByPath(String path) {
        System.out.println("getListByPath : " + path);
        final List<ClientFile> result = new ArrayList<>();
        if (path.equals("root")) {
            for (Path p : FileSystems.getDefault().getRootDirectories()) {
                result.add(new ClientFile(p.toString(), true, 0, FileMapper.FOLDER_TYPE, 0));
            }
            return result;
        }
        StringBuilder builder = new StringBuilder();
        Path p = Paths.get(path);
        try {
            DirectoryStream<Path> stream;
            BasicFileAttributes attr;
            stream = Files.newDirectoryStream(p);
            for (Path entry : stream) {
                attr = Files.readAttributes(entry, BasicFileAttributes.class);
                result.add(new ClientFile(entry.getFileName().toString(), attr));
            }
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean changeFileName(String fromPath, String name) {
        File sourceFile = new File(fromPath);

        File destFile = new File(sourceFile.getParent() + "/" + name);
        System.out.println("changeFileName : " + sourceFile + " " + destFile);

        if (sourceFile.renameTo(destFile)) {
            System.out.println("File renamed successfully");
            return true;
        } else {
            System.out.println("Failed to rename file");
            return false;
        }
    }

    public boolean deleteFile(String payload) {
        Path path = Paths.get(payload);
        try (Stream<Path> walk = Files.walk(path)) {
            walk.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .peek(System.out::println)
                    .forEach(File::delete);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean copyFile(String fromPath, String toPath) {
        int idx = fromPath.lastIndexOf("/");
        String filename = fromPath.substring(idx + 1);
        Path sourcePath = Paths.get(fromPath);
        Path destinationPath = Paths.get(toPath + "/" + filename);
        if (sourcePath.toString().equals(destinationPath.toString())) return false;
        if (Files.isDirectory(sourcePath)) {
            try {
                copyFolder(sourcePath, destinationPath);
            } catch (Exception e) {
                return false;
            }
        } else {
            try {
                Files.copy(sourcePath, destinationPath);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public boolean copyFile2(String fromPath, String toPath) {
        int idx = fromPath.lastIndexOf("/");
        String filename = fromPath.substring(idx + 1);
        Path sourcePath = Paths.get(fromPath);
        Path destinationPath = Paths.get(toPath + "/" + filename);
        AtomicBoolean isSuccess = new AtomicBoolean(true);
        try {
            Files.walk(sourcePath)
                    .forEach(source -> {
                        try {
                            copy(source, destinationPath.resolve(sourcePath.relativize(source)));
                        } catch (IOException e) {
                            isSuccess.set(false);
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return isSuccess.get();
    }

    private void copyFolder(Path sourcePath, Path destinationPath) throws IOException {
        Files.walk(sourcePath)
                .forEach(source -> {
                    try {
                        copy(source, destinationPath.resolve(sourcePath.relativize(source)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    private void copy(Path source, Path dest) throws IOException {
        Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
    }

    public boolean moveFile(String fromPath, String toPath) {
        int idx = fromPath.lastIndexOf("/");
        String filename = fromPath.substring(idx + 1);
        Path sourcePath = Paths.get(fromPath);
        Path destinationPath = Paths.get(toPath + "/" + filename);
        if (sourcePath.toString().equals(destinationPath.toString())) return false;
        try {
            Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (FileAlreadyExistsException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static class LazyHolder {
        private static final FileManager INSTANCE = new FileManager();
    }

    private FileManager() {
    }
}
