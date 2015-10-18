package appwarp;



public abstract class WarpMessage  {

	int userID;
	
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public abstract void broadcast();	

	
}
