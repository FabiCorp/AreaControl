package appwarp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class OutgoingWarpMessage {
	
	/*
	   Message Type:   0 - simple serialize/deserialize
	   Message Type: 1-9 - a packed byte array is transmitted that can be unpacked to a class 
	                       using a single buffer the value encodes which buffer
	                       this is for classes that are cannot be serialized into a single buffer.
	                       but packed
	                10-19  10 buffers for a multi-buffer 
	   */
	 
	ByteArrayOutputStream array;
	
	public OutgoingWarpMessage(byte messageType,ByteArrayOutputStream data) {
		this.array = new ByteArrayOutputStream();
		this.array.write(messageType); 
		try {
			this.array.write(data.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setUserName(byte userID) {
		this.array.write(userID);
	}

	public void broadcast(){
		WarpController.getInstance().sendGameUpdate(this);
	}

	public byte[] toByteArray() {
		return array.toByteArray();
	}
}
