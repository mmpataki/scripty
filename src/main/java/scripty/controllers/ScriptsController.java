package scripty.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import scripty.models.ScriptEntity;
import scripty.services.api.ScriptManagementService;

@RestController
public class ScriptsController {

	@Autowired
	ScriptManagementService SMS;
	
	@RequestMapping(method = RequestMethod.GET, value = "/scripts/{id}")
	public ScriptEntity getScript(@RequestParam String id) throws Exception {
		return SMS.getScript(id);
	}
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/scripts")
	public List<ScriptEntity> getScripts(@RequestParam int from, @RequestParam int count) throws Exception {
		List<ScriptEntity> out = new ArrayList<ScriptEntity>();
		for (int i = from; i < from + count; i++) {
			try {
				out.add(SMS.getScript(i + ""));
			} catch(Exception ex) {
				break;
			}
		}
		return out;
	}
	
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/scripts/{id}")
	public void deleteScript(@RequestParam String id) throws Exception {
		SMS.deleteScript(id);
	}
	
	
	@RequestMapping(method = RequestMethod.PUT, value = "/scripts")
	public void updateScript(ScriptEntity script) throws Exception {
		SMS.updateScript(script);
	}
	
}
