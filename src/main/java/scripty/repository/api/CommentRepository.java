package scripty.repository.api;

import java.util.List;

import scripty.models.Comment;

public interface CommentRepository extends Repository<String, Comment> {

	public List<Comment> getComments(String sId, int start, int pageSize) throws Exception;
	
}
