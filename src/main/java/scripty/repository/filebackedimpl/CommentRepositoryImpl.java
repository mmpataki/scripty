package scripty.repository.filebackedimpl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import scripty.models.Comment;
import scripty.repository.api.CommentRepository;
import scripty.security.authorization.api.AuthUtil;
import scripty.security.authorization.api.AuthorizableAction;
import scripty.security.authorization.api.AuthorizationService;

public class CommentRepositoryImpl implements CommentRepository {

	String dataDir = "./scripts";
	
	@Autowired
	AuthorizationService AS;
	
	@Autowired
	AuthUtil AU;
	
	@PostConstruct
	public void init() throws Exception {
		AS.setAuthParam(new Integer(0744));
	}
	
	@Override
	public void put(Comment cmnt) throws Exception {
		CommentList cl = fread(cmnt.getsId());
		cmnt.setId(getCID(cmnt.getsId(), cl.size() + ""));
		cl.add(new WrappedObject<Comment>(AS.getToken(cmnt.getId(), AU.getAC()), cmnt));
		fwrite(cmnt.getId(), cl);
	}

	@Override
	public Comment get(String key) throws Exception {
		String[] ids = splitCID(key);
		String cId = ids[1];
		CommentList cl = fread(ids[0]);
		return cl.get(cId).get();
	}

	@Override
	public boolean has(String key) throws Exception {
		String[] ids = splitCID(key);
		String cId = ids[1];
		CommentList cl = fread(ids[0]);
		return cl.has(cId);
	}

	@Override
	public Comment remove(String key) throws Exception {
		String[] ids = splitCID(key);
		String cId = ids[1];
		CommentList cl = fread(ids[0]);
		WrappedObject<Comment> wc = cl.get(cId);
		if(!AS.isAuthorized(wc.tok, AU.getAC(), AuthorizableAction.WRITE))
			throw new Exception("Unauthorized");
		wc = cl.remove(cId);
		fwrite(ids[0], cl);
		return wc.get();
	}

	@Override
	public Set<Comment> search(List<String> searchTerms) throws Exception {
		throw new UnsupportedOperationException("search is unsupported for comments");
	}

	@Override
	public void applyUpdate(String key, UpdateFunction<Comment> uFunc) throws Exception {
		String[] ids = splitCID(key);
		String cId = ids[1];
		CommentList cl = fread(ids[0]);
		WrappedObject<Comment> wc = cl.get(cId);
		if(!AS.isAuthorized(wc.tok, AU.getAC(), AuthorizableAction.UPDATE))
			throw new Exception("Unauthorized");
	}
	
	private String getCID(String sId, String cId) {
		return sId + "." + cId;
	}

	private String[] splitCID(String cId) {
		return cId.split("\\.");
	}
	
	private void fwrite(String sId, CommentList list) throws Exception {
		FileOutputStream fis = new FileOutputStream(constructCommentPath(dataDir, sId).toFile());
		new ObjectOutputStream(fis).writeObject(list);
		fis.close();
	}

	private CommentList fread(String sId) throws Exception {
		FileInputStream fis = new FileInputStream(constructCommentPath(dataDir, sId).toFile());
		CommentList cl = (CommentList) new ObjectInputStream(fis).readObject();
		fis.close();
		return cl;
	}
	
	public static Path constructPath(String dataDir, String sId) {
		return Paths.get(dataDir, sId);
	}
	
	public static Path constructCommentPath(String dataDir, String sId) {
		return Paths.get(dataDir, sId + ".comments");
	}

	@Override
	public List<Comment> getComments(String sId, int start, int pageSize) throws Exception {
		CommentList cl = fread(sId);
		List<Comment> out = new ArrayList<>();
		for (WrappedObject<Comment> wc : cl.subList(start+"", ""+start+pageSize)) {
			out.add(wc.get());
		}
		return out;
	}
}

/* neither want to make a file, nor can make a innerclass as inner classes aren't well serialized */
class CommentList implements Serializable {

	private static final long serialVersionUID = -4485582174127307981L;
	TreeMap<String, WrappedObject<Comment>> map = new TreeMap<>();

	public void add(WrappedObject<Comment> c) {
		map.put(c.get().getId(), c);
	}
	public WrappedObject<Comment> remove(String cId) {
		return map.remove(cId);
	}
	public WrappedObject<Comment> get(String cId) {
		return map.get(cId);
	}
	public Collection<WrappedObject<Comment>> subList(String start, String end) {
		return map.subMap(start, end).values();
	}
	public int size() {
		return map.size();
	}
	public boolean has(String key) {
		return map.containsKey(key);
	}
}