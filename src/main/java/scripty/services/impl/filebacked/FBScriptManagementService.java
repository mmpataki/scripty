package scripty.services.impl.filebacked;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import scripty.models.ScriptEntity;
import scripty.services.api.ScriptManagementService;

@Service
public class FBScriptManagementService implements ScriptManagementService {

	String dataDir = "./scripts";

	public static Path constructPath(String dataDir, String sId) {
		return Paths.get(dataDir, sId);
	}

	@Override
	public void addScript(ScriptEntity script) throws Exception {
		Assert.notNull(script, "Script shouldn't be null");
		Path scriptPath = constructPath(dataDir, script.getId());
		FileOutputStream fos = new FileOutputStream(scriptPath.toFile());
		new ObjectOutputStream(fos).writeObject(script);
		fos.close();
	}

	@Override
	public void deleteScript(String sId) throws Exception {
		Path scriptPath = constructPath(dataDir, sId);
		Files.delete(scriptPath);
	}

	@Override
	public void updateScript(ScriptEntity script) throws Exception {
		addScript(script);
	}

	@Override
	public ScriptEntity getScript(String sId) throws Exception {
		FileInputStream fis = new FileInputStream(constructPath(dataDir, sId).toFile());
		ScriptEntity se = (ScriptEntity) (new ObjectInputStream(fis)).readObject();
		fis.close();
		return se;
	}

}
