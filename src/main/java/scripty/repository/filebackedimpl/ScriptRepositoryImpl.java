package scripty.repository.filebackedimpl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import scripty.models.Comment;
import scripty.models.ScriptEntity;
import scripty.repository.api.ScriptRepository;
import scripty.security.authorization.api.AuthContext;
import scripty.security.authorization.api.AuthToken;
import scripty.security.authorization.api.AuthUtil;
import scripty.security.authorization.api.AuthorizableAction;
import scripty.security.authorization.api.AuthorizationService;
import scripty.util.luid.LUIDService;
import scripty.util.luid.Token;


@Service
public class ScriptRepositoryImpl implements ScriptRepository {

	String dataDir = "./scripts";
	Set<String> titleCache = new HashSet<>();
	
	@Autowired
	AuthorizationService AS;
	
	@Autowired
	LUIDService luidService;
	Token LUG;
	
	@Autowired
	AuthUtil AU;
	
	@PostConstruct
	public void init() throws Exception {
		LUG = luidService.register("scriptmanager", 1024);
		AS.setAuthParam(new Integer(0705));
	}
	
	@Override
	public void put(ScriptEntity script) throws Exception {
		if(titleCache.contains(script.getTitle()))
			throw new Exception("script with title = `" + script.getTitle() + "` exists");
		script.setId(LUG.next());
		fwrite(new WrappedObject<ScriptEntity>(AS.getToken(script.getId(), AU.getAC()), script));
	}

	@SuppressWarnings("unchecked")
	private WrappedObject<ScriptEntity> fread(String sId) throws Exception {
		FileInputStream fis = new FileInputStream(constructPath(dataDir, sId).toFile());
		WrappedObject<ScriptEntity> ws = (WrappedObject<ScriptEntity>) (new ObjectInputStream(fis)).readObject();
		fis.close();
		return ws;
	}
	
	private void fwrite(WrappedObject<ScriptEntity> ws) throws FileNotFoundException, IOException {
		Path scriptPath = constructPath(dataDir, ws.get().getId());
		FileOutputStream fos = new FileOutputStream(scriptPath.toFile());
		new ObjectOutputStream(fos).writeObject(ws);
		fos.close();
		titleCache.add(ws.get().getTitle());
	}

	@Override
	public ScriptEntity get(String sId) throws Exception {
		return fread(sId).get();
	}

	@Override
	public boolean has(String sId) throws Exception {
		return constructPath(dataDir, sId).toFile().exists();
	}

	@Override
	public ScriptEntity remove(String sId) throws Exception {
		WrappedObject<ScriptEntity> ws = fread(sId);
		authorize(ws.tok, AU.getAC());
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
		WrappedObject<ScriptEntity> ws = fread(sId);
		authorize(ws.tok, AU.getAC());
		Exception thrown = null;
		/* lock */
		try {
			uFunc.update(ws.get());
		} catch(Exception e) {
			thrown = e;
		}
		fwrite(ws);
		/* unlock */
		if(thrown != null)
			throw thrown;
	}

	
	@Override
	public void addComment(String sId, Comment comment) throws Exception {
		if(!has(sId)) {
			throw new Exception("Script " + sId + " doesn't exist");
		}
		List<Comment> list = readList(sId);
		list.add(comment);
		writeList(sId, list);
	}

	@Override
	public void removeComment(String sId, String cId) throws Exception {
		List<Comment> list = readList(sId);
		list.remove(Integer.parseInt(cId));
		writeList(sId, list);
	}

	@Override
	public List<Comment> getComments(String sId, int offset, int pageSize) throws Exception {
		List<Comment> list = readList(sId);
		return list.subList(offset, offset + pageSize);
	}
	
	private void writeList(String sId, List<Comment> list) throws Exception {
		FileOutputStream fis = new FileOutputStream(constructCommentPath(dataDir, sId).toFile());
		new ObjectOutputStream(fis).writeObject(list);
		fis.close();
	}

	private List<Comment> readList(String sId) throws Exception {
		FileInputStream fis = new FileInputStream(constructCommentPath(dataDir, sId).toFile());
		@SuppressWarnings("unchecked")
		List<Comment> list = (List<Comment>) new ObjectInputStream(fis).readObject();
		fis.close();
		return list;
	}
	
	public static Path constructPath(String dataDir, String sId) {
		return Paths.get(dataDir, sId);
	}
	
	public static Path constructCommentPath(String dataDir, String sId) {
		return Paths.get(dataDir, sId + ".comments");
	}
	
	private void authorize(AuthToken tok, AuthContext ac) throws Exception {
		if(!AS.isAuthorized(tok, ac, AuthorizableAction.WRITE))
			throw new Exception("403. Unauthorized");
	}

	@Override
	public String getLastId() {
		return LUG.last();
	}
}
