package scripty.repository.api;

import java.util.List;
import java.util.Set;


public interface Repository<KeyType, ValueType> {
	
	public interface UpdateFunction<ValueType> {
		public void update(ValueType oldValue) throws Exception;
	}
	
	public void put(ValueType object) throws Exception;
	
	public ValueType get(KeyType key) throws Exception;

	public boolean has(KeyType key) throws Exception;
	
	public ValueType remove(KeyType key) throws Exception;
	
	public Set<ValueType> search(List<String> searchTerms) throws Exception;
	
	public void applyUpdate(KeyType key, UpdateFunction<ValueType> uFunc) throws Exception;
}
