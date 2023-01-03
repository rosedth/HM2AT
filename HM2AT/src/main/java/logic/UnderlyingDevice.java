package logic;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class UnderlyingDevice {
	private String name;
	private Path path;
	private String paradigm;
	private String language;
	private String mainEntity;
	private List<Hook> hooks;
	private CommunicationMechanism communication;
	
	public UnderlyingDevice() {
		name="";
		path= Paths.get("");
		paradigm="";
		language="";
		mainEntity="";
		hooks=null;
		communication=null;
	}
	
	public CommunicationMechanism getCommunication() {
		return communication;
	}
	public void setCommunication(CommunicationMechanism communication) {
		this.communication = communication;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Path getPath() {
		return path;
	}
	public void setPath(Path path) {
		this.path = path;
	}
	public String getParadigm() {
		return paradigm;
	}
	public void setParadigm(String paradigm) {
		this.paradigm = paradigm;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getMainEntity() {
		return mainEntity;
	}
	public void setMainEntity(String mainEntity) {
		this.mainEntity = mainEntity;
	}
	public List<Hook> getHooks() {
		return hooks;
	}
	public void setHooks(List<Hook> hooks) {
		this.hooks = hooks;
	}


}
