package scripty.repository.filebackedimpl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import scripty.models.ScriptEntity;
import scripty.repository.api.ScriptRepository;

@Service
public class ScriptRepositoryImpl implements ScriptRepository {

	String dataDir = "./scripts";

	public static Path constructPath(String dataDir, String sId) {
		return Paths.get(dataDir, sId);
	}
	
	@Override
	public void put(ScriptEntity script) throws Exception {
		Path scriptPath = constructPath(dataDir, script.getId());
		FileOutputStream fos = new FileOutputStream(scriptPath.toFile());
		new ObjectOutputStream(fos).writeObject(script);
		fos.close();
	}

	@Override
	public ScriptEntity get(String sId) throws Exception {
		FileInputStream fis = new FileInputStream(constructPath(dataDir, sId).toFile());
		ScriptEntity se = (ScriptEntity) (new ObjectInputStream(fis)).readObject();
		fis.close();
		return se;
	}

	@Override
	public boolean has(String sId) throws Exception {
		return constructPath(dataDir, sId).toFile().exists();
	}

	@Override
	public ScriptEntity remove(String sId) throws Exception {
		Path scriptPath = constructPath(dataDir, sId);
		Files.delete(scriptPath);
		return null;
	}

	@Override
	public Set<ScriptEntity> search(List<String> searchTerms) throws Exception {
		throw new UnsupportedOperationException("not supported yet");
	}


	@Override
	public void applyUpdate(String sId, UpdateFunction<ScriptEntity> uFunc) throws Exception {
		ScriptEntity script = get(sId);
		Exception thrown = null;
		/* lock */
		try {
			uFunc.update(script);
		} catch(Exception e) {
			thrown = e;
		}
		/* unlock */
		if(thrown != null)
			throw thrown;
	}

}
