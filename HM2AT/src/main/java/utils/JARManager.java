package utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JARManager {
	
	public static Set<String> getClassNamesFromJarFile(File givenFile) throws IOException {
	    Set<String> classNames = new HashSet<>();
	    try (JarFile jarFile = new JarFile(givenFile)) {
	        Enumeration<JarEntry> e = jarFile.entries();
	        while (e.hasMoreElements()) {
	            JarEntry jarEntry = e.nextElement();
	            if (jarEntry.getName().endsWith(".class")) {
	                String className = jarEntry.getName()
	                  .replace("/", ".")
	                  .replace(".class", "");
	                classNames.add(className);
	            }
	        }
	        return classNames;
	    }
	}
	
	public static Set<Class> getClassesFromJarFile(File jarFile) throws IOException, ClassNotFoundException {
	    Set<String> classNames = getClassNamesFromJarFile(jarFile);
	    Set<Class> classes = new HashSet<>(classNames.size());
	    try (URLClassLoader cl = URLClassLoader.newInstance(
	           new URL[] { new URL("jar:file:" + jarFile + "!/") })) {
	        for (String name : classNames) {
	            Class clazz = cl.loadClass(name); // Load the class by its name
	            classes.add(clazz);
	        }
	    }
	    return classes;
	}
	
//	@Test
//	public void givenJarFilePath_whenLoadClass_thenGetClassObjects()
//	  throws IOException, ClassNotFoundException, URISyntaxException {
//	    File jarFile
//	      = new File(Objects.requireNonNull(getClass().getClassLoader().getResource(JAR_PATH)).toURI());
//	    Set<Class> classes = GetClassNamesFromJar.getClassesFromJarFile(jarFile);
//	    Set<String> names = classes.stream().map(Class::getName).collect(Collectors.toSet());
//	    Assert.assertEquals(EXPECTED_CLASS_NAMES, names);
//	}
	
}
