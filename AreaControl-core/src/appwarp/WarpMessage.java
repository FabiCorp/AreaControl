package appwarp;

import java.io.Serializable;

public abstract class WarpMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String userName;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public void broadcast(){
		WarpController.getInstance().sendGameUpdate(this);
	}
}
