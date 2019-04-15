package scripty.models;

import java.io.Serializable;

public class User implements Serializable {
	
	private static final long serialVersionUID = -3755719690034931012L;

	private String id;
	
	/* info */
	private String userName;
	private String emailId;
	private char[] password;
	
	/* metrics */
	private Long uploads;
	private Long views;
	private Long comments;
	
	
	@Override
	public boolean equals(Object otherUser) {
		return emailId.equals(((User)otherUser).getEmailId());
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public char[] getPassword() {
		return password;
	}
	public void setPassword(char[] password) {
		this.password = password;
	}
	public Long getUploads() {
		return uploads;
	}
	public void setUploads(Long uploads) {
		this.uploads = uploads;
	}
	public Long getViews() {
		return views;
	}
	public void setViews(Long views) {
		this.views = views;
	}
	public Long getComments() {
		return comments;
	}
	public void setComments(Long comments) {
		this.comments = comments;
	}
	
}
