package utils;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import logic.Repository;

public class JSONManager {
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
}
