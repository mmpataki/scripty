package scripty.services.api;

import java.util.Iterator;
import java.util.List;

import scripty.models.ScriptEntity;

public interface SearchService {

	public Iterator<ScriptEntity> search(List<String> searchTerms);
	
}
