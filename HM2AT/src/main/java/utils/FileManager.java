package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import frames.Main;

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


        Files.copy(fromFile, toFile, StandardCopyOption.REPLACE_EXISTING);

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
	
    public static List<Path> findByFilePartialName(Path path, String partialName)
            throws IOException {

        if (!Files.isDirectory(path)) {
            throw new IllegalArgumentException("Path must be a directory!");
        }

        List<Path> result;
        try (Stream<Path> walk = Files.walk(path)) {
            result = walk
                    .filter(Files::isRegularFile)   // is a file
                    .filter(p -> p.getFileName().toString().toLowerCase().contains(partialName))
                    .collect(Collectors.toList());
        }
        return result;

    }

	   public static Map<String,Integer> readIndexes(String from){
	    	Map<String,Integer> indexes=new HashMap<>();
	    	Path path = Paths.get(from);
	    	List<String> allLines;
			try {
				allLines = Files.readAllLines(path);
				for (String line : allLines) {
					String [] part=line.split(":");
					indexes.put(part[0].trim(),Integer.valueOf(part[1].trim()));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	return indexes;
	    }
	    
	    public static void updateIndexes() {
	        File file = new File(Main.repository+"\\indexes.txt");
	        FileWriter fr = null;
	        int line=1;
	        try {
	            fr = new FileWriter(file);
	            for (String key : Main.indexes.keySet()) {
	                
	            	Integer value = Main.indexes.get(key);
	                fr.write(key+":"+value);
	            	if(line!=4) {
	            		fr.write(System.lineSeparator());          		
	            	}
	                line+=1;
	         }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }finally{
	            //close resources
	            try {
	                fr.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	    
	    public static String loadMetadata() {
	    	String metadata="";
	    	Path path = Paths.get(System.getProperty("user.dir")+"\\.metadata");
	    	
	    	if (Files.exists(path)) {
	    		try {
	    			metadata = Files.readAllLines(path).get(0);
	     		} catch (IOException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	        } 	

	    	return metadata;
	    }
}
