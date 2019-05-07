package scripty.repository.api;

import java.util.List;

import scripty.models.Comment;
import scripty.models.ScriptEntity;

public interface ScriptRepository extends Repository<String, ScriptEntity> {
	
	/**
	 * Add a comment to repository for scriptId=sId
	 * @param sId : ID of script on which comment is added on
	 * @param comment : comment
	 * @throws Exception
	 */
	public void addComment(String sId, Comment comment) throws Exception;
	
	
	/**
	 * Gets a subset of comments between offset and (offset + pageSize)
	 * @param sId : comments of what script?
	 * @param offset : start from where
	 * @param pageSize : number of comments to return
	 * @return : list of comments
	 * @throws Exception
	 */
	public List<Comment> getComments(String sId, int offset, int pageSize) throws Exception;
	
	
	/**
	 * Remove a comment from the repository with scriptId=sId and commentId=cId.
	 * @param sId : script id
	 * @param cId : comment id
	 * @throws Exception
	 */
	public void removeComment(String sId, String cId) throws Exception;
	
	
	/**
	 * Returns last inserted ID.
	 * @return
	 */
	public String getLastId();
}
