package scripty.rest.server.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import scripty.models.Comment;
import scripty.models.ScriptEntity;
import scripty.services.api.ScriptManagementService;

import static scripty.rest.server.controllers.RestTryExecutor.build;

@RestController
@CrossOrigin
public class ScriptsController {

	@Autowired
	ScriptManagementService SMS;

	@RequestMapping(method = RequestMethod.POST, value = SCRIPT_BASE)
	public RESTResponse<ScriptEntity> addScript(@RequestBody ScriptEntity script) throws Exception {
		return build(() -> SMS.addScript(script));
	}

	@RequestMapping(method = RequestMethod.GET, value = SCRIPT_BASE + "/{id}")
	public RESTResponse<ScriptEntity> getScript(@PathVariable String id) throws Exception {
		return build(() -> SMS.getScript(id));
	}

	@RequestMapping(method = RequestMethod.GET, value = SCRIPT_BASE)
	public RESTResponse<List<ScriptEntity>> getScripts(@RequestParam(defaultValue = "-1") long offset, @RequestParam(defaultValue = "10") int pageSize) throws Exception {
		
		return build(
			() -> {
				List<ScriptEntity> out = new ArrayList<ScriptEntity>();
				long start = offset;
				
				if(start == -1) {
					start = Long.parseLong(SMS.getLastId());
				}
				
				for(long i = start, cnt = 0; i > 0 && cnt < pageSize; i--) {
					try {
						out.add(SMS.getScript(i + ""));
					} catch(Exception ex) {
						
					}
				}
				return out;
			}
		);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = SCRIPT_BASE + "/{sId}")
	public RESTResponse<NullObject> deleteScript(@PathVariable String sId) throws Exception {
		return build(() -> SMS.deleteScript(sId));
	}

	@RequestMapping(method = RequestMethod.PATCH, value = SCRIPT_BASE)
	public RESTResponse<NullObject> updateScript(@RequestBody ScriptEntity script) throws Exception {
		return build(() -> SMS.updateScript(script));
	}

	@RequestMapping(method = RequestMethod.PATCH, value = SCRIPT_BASE + "/{sId}/upvote")
	public RESTResponse<NullObject> upVote(@PathVariable String sId) throws Exception {
		return build(() -> SMS.upVote(sId));
	}

	@RequestMapping(method = RequestMethod.PATCH, value = SCRIPT_BASE + "/{sId}/downvote")
	public RESTResponse<NullObject> downVote(@PathVariable String sId) throws Exception {
		return build(() -> SMS.downVote(sId));
	}

	@RequestMapping(method = RequestMethod.PATCH, value = SCRIPT_BASE + "/{sId}/comment")
	public RESTResponse<NullObject> addComment(String sId, Comment comment) throws Exception {
		return build(() -> SMS.addComment(sId, comment));
	}

	public static final String SCRIPT_BASE = "/scripts";

}
