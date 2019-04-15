package scripty.services.api;

import scripty.models.ScriptEntity;

public interface ScriptManagementService {
	
	public void addScript(ScriptEntity script) throws Exception;
	
	public void deleteScript(String id) throws Exception;
	
	public void updateScript(ScriptEntity script) throws Exception;
	
	public ScriptEntity getScript(String id) throws Exception;
}
