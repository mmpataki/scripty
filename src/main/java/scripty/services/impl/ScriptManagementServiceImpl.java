package scripty.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import scripty.models.Comment;
import scripty.models.ScriptEntity;
import scripty.repository.api.ScriptRepository;
import scripty.services.api.ScriptManagementService;

@Service
public class ScriptManagementServiceImpl implements ScriptManagementService {

	@Autowired
	ScriptRepository SRS;

	@Override
	public void addScript(ScriptEntity script) throws Exception {
		Assert.notNull(script, "Script shouldn't be null");
		SRS.put(script);
	}

	@Override
	public void deleteScript(String sId) throws Exception {
		SRS.remove(sId);
	}

	@Override
	public void updateScript(final ScriptEntity script) throws Exception {
		SRS.applyUpdate(script.getId(), (ScriptEntity oldScript) -> oldScript.readNewValues(script));
	}

	@Override
	public ScriptEntity getScript(String sId) throws Exception {
		return SRS.get(sId);
	}

	@Override
	public void upVote(String sId) throws Exception {
		SRS.applyUpdate(sId, (ScriptEntity oldScript) -> oldScript.upVote());
	}

	@Override
	public void downVote(String sId) throws Exception {
		SRS.applyUpdate(sId, (ScriptEntity oldScript) -> oldScript.downVote());
	}

	@Override
	public void addComment(String sId, Comment comment) throws Exception {
		SRS.applyUpdate(sId, (ScriptEntity oldScript) -> oldScript.addComment(comment));
	}

	@Override
	public void removeComment(String sId, String commentId) throws Exception {
		SRS.applyUpdate(sId, (ScriptEntity oldScript) -> oldScript.removeComment(commentId));
	}

}
