package scripty.repository.filebackedimpl;

import java.io.Serializable;

import scripty.security.authorization.api.AuthToken;

public class WrappedObject<WrappedType> implements Serializable {

	private static final long serialVersionUID = -5487097825696818214L;
	AuthToken tok;
	WrappedType obj;
	
	public WrappedObject(AuthToken t, WrappedType o) {
		obj = o;
		tok = t;
	}
	
	public WrappedType get() {
		return obj;
	}
	
	public AuthToken getTok() {
		return tok;
	}
	
	public void set(WrappedType o) {
		this.obj = o;
	}
	
	public void setTok(AuthToken t) {
		tok = t;
	}
}
