package scripty.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

public class ScriptEntity implements Serializable{

	private static final long serialVersionUID = 5765617985954411664L;

	String id;
	
	String title;
	String script;
	String userId;
	String shellType;
	String description;
	String exampleUsage;
	
	Long views;
	Long upVotes;
	Long downVotes;
	
	List<String> related;
	
	public ScriptEntity() {
		// TODO Auto-generated constructor stub
	}
	
	public ScriptEntity(String title, String script, String shellType, String description, String exampleUsage) {
		this.title = title;
		this.script = script;
		this.shellType = shellType;
		this.description = description;
		this.exampleUsage = exampleUsage;
		
		views = 0L;
		upVotes = 0L;
		downVotes = 0L;
		
		related = new ArrayList<>();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

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

	public List<String> getRelated() {
		return related;
	}

	public void setRelated(List<String> related) {
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

	@Override
	public boolean equals(Object obj) {
		ScriptEntity that = (ScriptEntity)obj;
		return (this.id.equals(that.id) || this.title.equals(that.title) || this.script.equals(that.script));
	}
}
