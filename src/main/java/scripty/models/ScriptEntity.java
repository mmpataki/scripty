package scripty.models;

import java.util.List;

import org.springframework.util.StringUtils;

public class ScriptEntity {

	String id;
	
	String script;
	String userId;
	String shellType;
	String description;
	String exampleUsage;
	
	Long views;
	Long upVotes;
	Long downVotes;
	
	List<Long> related;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getShellType() {
		return shellType;
	}

	public void setShellType(String shellType) {
		this.shellType = shellType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getExampleUsage() {
		return exampleUsage;
	}

	public void setExampleUsage(String exampleUsage) {
		this.exampleUsage = exampleUsage;
	}

	public Long getViews() {
		return views;
	}

	public void setViews(Long views) {
		this.views = views;
	}

	public Long getUpVotes() {
		return upVotes;
	}

	public void setUpVotes(Long upVotes) {
		this.upVotes = upVotes;
	}

	public Long getDownVotes() {
		return downVotes;
	}

	public void setDownVotes(Long downVotes) {
		this.downVotes = downVotes;
	}

	public List<Long> getRelated() {
		return related;
	}

	public void setRelated(List<Long> related) {
		this.related = related;
	}

	public void readNewValues(ScriptEntity script) {
		if(!StringUtils.isEmpty(script.description))
			description = script.description;
		if(!StringUtils.isEmpty(script.exampleUsage))
			exampleUsage = script.exampleUsage;
		if(!StringUtils.isEmpty(script.script))
			this.script = script.script;
		if(!StringUtils.isEmpty(script.shellType))
			shellType = script.shellType;
	}

	public void upVote() {
		upVotes++;
	}

	public void downVote() {
		downVotes++;
	}

	public void addComment(Comment comment) {
		throw new UnsupportedOperationException("Unsuported operation");
	}

	public void removeComment(String commentId) {
		throw new UnsupportedOperationException("Unsuported operation");
	}
	
}
