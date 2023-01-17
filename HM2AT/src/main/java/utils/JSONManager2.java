package utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.rossedth.hm2aTool.MainHM2AT;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;


import logic.AdaptivityModel;
import logic.AdaptivityModelImplementation;
import logic.ImplementationDependency;
import logic.ImplementationExample;
import logic.Repository;

public class JSONManager2 {
	public static Repository loadRepositoryConfig(String repoPath) {
		ObjectMapper mapper = new ObjectMapper();
		Repository repo = new Repository();

		try {
			repo = mapper.readValue(new File(repoPath), Repository.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return repo;
	}

	public static void saveRepositoryConfig(String path, Repository repo) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
		try {
			writer.writeValue(new File(path), repo);
			// mapper.writeValue(new File(path), repo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static List<AdaptivityModel> readModels() {
		List<AdaptivityModel> modelList = new ArrayList<AdaptivityModel>();
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
		File models_File = new File(MainHM2AT.repository + "\\models\\models.json");
		if (models_File.exists()) {
			try {
				modelList = objectMapper.readValue(models_File, new TypeReference<List<AdaptivityModel>>() {
				});
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return modelList;
	}

	public static List<AdaptivityModelImplementation> readImplementations() {
		List<AdaptivityModelImplementation> implementationList = new ArrayList<AdaptivityModelImplementation>();
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
		File implementations_File = new File(MainHM2AT.repository + "\\implementations\\implementations.json");
		if (implementations_File.exists()) {
			try {
				implementationList = objectMapper.readValue(implementations_File, new TypeReference<List<AdaptivityModelImplementation>>() {
				});
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return implementationList;
	}

	public static List<ImplementationDependency> readDependencies() {
		List<ImplementationDependency> dependencyList = new ArrayList<ImplementationDependency>();
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
		File dependencies_File = new File(MainHM2AT.repository + "\\dependencies\\dependencies.json");
		try {
			dependencyList = objectMapper.readValue(dependencies_File, new TypeReference<List<ImplementationDependency>>() {
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dependencyList;
	}
	
	public static List<ImplementationExample> readExamples() {
		List<ImplementationExample> dependencyList = new ArrayList<ImplementationExample>();
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
		File dependencies_File = new File(MainHM2AT.repository + "\\examples\\examples.json");
		try {
			dependencyList = objectMapper.readValue(dependencies_File, new TypeReference<List<ImplementationExample>>() {
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dependencyList;
	}
	

}
