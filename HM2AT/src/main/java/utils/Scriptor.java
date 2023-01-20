package utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

//import org.jsonschema2pojo.DefaultGenerationConfig;
//import org.jsonschema2pojo.GenerationConfig;
//import org.jsonschema2pojo.Jackson2Annotator;
//import org.jsonschema2pojo.SchemaGenerator;
//import org.jsonschema2pojo.SchemaMapper;
//import org.jsonschema2pojo.SchemaStore;
//import org.jsonschema2pojo.SourceType;
//import org.jsonschema2pojo.rules.RuleFactory;
//
//import com.sun.codemodel.JCodeModel;

public class Scriptor {
private static final String packageName="HM2AT";
private static final String PREAMBLE_CLASS="public class ";
private static final String OPEN_SCOPE="{";
private static final String CLOSE_SCOPE="}";
private static final String BRACKETS_EMPTY=" {\r\n"
		+ "\r\n"
		+ "}"; 
private static final String IMPLEMENTS_INTERFACE=" implements ";
private static final String EXTENDS_CLASS=" extends ";
private static final String TAB="    ";

public static List<String> createClass(String className, List<String> parentClasses, List<String> interfaces) {
	int indentLevel=0;
	String codeLine="";
	List<String> newClass= new ArrayList<String>();
	codeLine+=PREAMBLE_CLASS+className;
	if(parentClasses!=null) {
		codeLine+=EXTENDS_CLASS;
		// add the inheritance clause for each element in the list
		codeLine+=String.join(",", parentClasses);
	}
	if(interfaces!=null) {
		codeLine+=IMPLEMENTS_INTERFACE;
		// add the implementation  clause for each element in the list
		codeLine+=String.join(",", interfaces);
	}
	codeLine+=OPEN_SCOPE;
	indentLevel+=1;
	newClass.add(codeLine);
	
	newClass.addAll(createMethod("public void sense()", indentLevel));
	newClass.add(CLOSE_SCOPE);
	return newClass;
}	

public static String printCode(List<String> code) {
	String codeInLine="";
	codeInLine=String.join("\r\n", code);
	return codeInLine;
}

public static List<String> printCodeFromFile(String filename){
	List<String> allLines= new ArrayList<String>();
	Path path = Paths.get(filename);
	try {
		allLines = Files.readAllLines(path, StandardCharsets.UTF_8);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return allLines;
}

public static String createPackagePreamble() {
	String preamble=packageName;
	return "package "+preamble+";";
}

public static List<String> createMethod(String signature, int indentLevel) {
	List<String> method=new ArrayList<String>();
	method.add(signature+OPEN_SCOPE);
	
	return applyIndentation(method,indentLevel);
}

public static List<String> applyIndentation(List<String> code,int indentationLevel) {
	String indent="";
	for (int i = 0; i < indentationLevel; i++) {
	    indent+=TAB;
	}
	for (String codeLine : code) {
		codeLine=indent+codeLine;
	}
	return code;
}

public static void main( String[] args ) throws MalformedURLException, IOException {
	Scriptor.createPackagePreamble();
	List<String> parents= new ArrayList<String>();
	parents.add("Monitor");
	parents.add("Controller");

	List<String> inter= new ArrayList<String>();
	inter.add("Data");
	Scriptor.createClass("MonitorFSM",parents,inter);
}

//public static void convertJsonToJavaClass(URL inputJsonUrl, File outputJavaClassDirectory, String packageName, String javaClassName) 
//		  throws IOException {
//		    JCodeModel jcodeModel = new JCodeModel();
//
//		    GenerationConfig config = new DefaultGenerationConfig() {
//		        @Override
//		        public boolean isGenerateBuilders() {
//		            return true;
//		        }
//
//		        @Override
//		        public SourceType getSourceType() {
//		            return SourceType.JSON;
//		        }
//		    };
//
//		    SchemaMapper mapper = new SchemaMapper(new RuleFactory(config, new Jackson2Annotator(config), new SchemaStore()), new SchemaGenerator());
//		    mapper.generate(jcodeModel, javaClassName, packageName, inputJsonUrl);
//
//		    jcodeModel.build(outputJavaClassDirectory);
//		}
//
//public static void whenProvideInputJSON_thenGenerateJavaClass() throws MalformedURLException, IOException {
//
//    String packageName = "com.hm2at.json2java";
//
//    // load input JSON file
//    String jsonPath = "schema/";
//    File inputJson = new File(jsonPath + "monitor.json");
//
//    // create the local directory for generating the Java Class file
//    String outputPath = "src/test/resources/";
//    File outputJavaClassDirectory = new File(outputPath);
//
//    String javaClassName = "Monitor";
//
//    Scriptor.convertJsonToJavaClass(inputJson.toURI()
//            .toURL(), outputJavaClassDirectory, packageName, javaClassName);
//
//    File outputJavaClassPath = new File(outputPath + packageName.replace(".", "/"));
//}

}
