package logic;

public class MethodTable {
	private String id;
	private String ownerClass;
	private String signature;
	private boolean isHook;
	private String hookType;
	
	
	public MethodTable() {
		super();
	}


	public MethodTable(String id,String ownerClass, String signature, boolean isHook, String hookType) {
		super();
		this.id=id;
		this.ownerClass = ownerClass;
		this.signature = signature;
		this.isHook = isHook;
		this.hookType = hookType;
	}
	
	
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getOwnerClass() {
		return ownerClass;
	}
	public void setOwnerClass(String ownerClass) {
		this.ownerClass = ownerClass;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public boolean isHook() {
		return isHook;
	}
	public void setHook(boolean isHook) {
		this.isHook = isHook;
	}
	public String getHookType() {
		return hookType;
	}
	public void setHookType(String hookType) {
		this.hookType = hookType;
	}

}
