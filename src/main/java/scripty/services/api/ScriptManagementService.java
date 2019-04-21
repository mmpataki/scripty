package scripty.services.api;

import scripty.models.Comment;
import scripty.models.ScriptEntity;

public interface ScriptManagementService {
	
	public void addScript(ScriptEntity script) throws Exception;
	
	public void deleteScript(String sId) throws Exception;
	
	public void updateScript(ScriptEntity script) throws Exception;
	
	public ScriptEntity getScript(String sId) throws Exception;
	
	public void upVote(String sId) throws Exception;
	
	public void downVote(String sId) throws Exception;
	
	public void addComment(String sId, Comment comment) throws Exception;
	
	public void removeComment(String sId, String commentId) throws Exception;
	
}
