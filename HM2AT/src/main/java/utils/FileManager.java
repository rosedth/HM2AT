package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileManager {

    public static void copyFileNIO(String from, String to) throws IOException {

        Path fromFile = Paths.get(from);
        Path toFile = Paths.get(to);

        // if fromFile doesn't exist, Files.copy throws NoSuchFileException
        if (Files.notExists(fromFile)) {
            System.out.println("File doesn't exist? " + fromFile);
            return;
        }

        // if toFile folder doesn't exist, Files.copy throws NoSuchFileException
        // if toFile parent folder doesn't exist, create it.
        Path parent = toFile.getParent();
        if(parent!=null){
            if(Files.notExists(parent)){
                Files.createDirectories(parent);
            }
        }

        // default - if toFile exist, throws FileAlreadyExistsException
        Files.copy(fromFile, toFile);

        // if toFile exist, replace it.
        // Files.copy(fromFile, toFile, StandardCopyOption.REPLACE_EXISTING);

        // multiple StandardCopyOption
        /*CopyOption[] options = { StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.COPY_ATTRIBUTES,
                LinkOption.NOFOLLOW_LINKS };

        Files.copy(fromFile, toFile, options);*/

    }
    
    public static List<Path> findByFileName(Path path, String fileName)
            throws IOException {

        List<Path> result;
        try (Stream<Path> pathStream = Files.find(path,
                1,
                (p, basicFileAttributes) ->
                        p.getFileName().toString().equalsIgnoreCase(fileName))
        ) {
            result = pathStream.collect(Collectors.toList());
        }
        return result;
    }
    
    
	public static List<String> findFiles(Path path, String fileExtension) throws IOException {

		if (!Files.isDirectory(path)) {
			throw new IllegalArgumentException("Path must be a directory!");
		}

		List<String> result;
		
		// this is a path, not string,
		// this only test if path end with a certain path
		// .filter(p -> p.endsWith(fileExtension))
		// convert path to string first
		try (Stream<Path> walk = Files.walk(path, 1)) {
			result = walk.filter(p -> !Files.isDirectory(p))
					.map(p -> p.toString().toLowerCase()).filter(f -> f.endsWith(fileExtension))
					.collect(Collectors.toList());
		}

		return result;
	}

}
