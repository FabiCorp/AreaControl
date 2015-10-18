package appwarp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.shephertz.app42.gaming.multiplayer.client.events.ChatEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LobbyData;
import com.shephertz.app42.gaming.multiplayer.client.events.MoveEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomData;
import com.shephertz.app42.gaming.multiplayer.client.events.UpdateEvent;
import com.shephertz.app42.gaming.multiplayer.client.listener.NotifyListener;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

public class NotificationListener implements NotifyListener{

	
	private WarpController callBack;
	
	public NotificationListener(WarpController callBack) {
		this.callBack = callBack;
	}
	
	public void onChatReceived(ChatEvent event) {
		
	}

	public void onRoomCreated(RoomData arg0) {
		
	}

	public void onRoomDestroyed(RoomData arg0) {
		
	}

	public void onUpdatePeersReceived(UpdateEvent event) {
		WarpMessage msg = null;
		try
		{
			ByteArrayInputStream bIn = new ByteArrayInputStream(event.getUpdate());
			// the last byte in the array is the player ID
			// the second-to-last byte is the messgage Type
			byte msgBuffer[] = new byte[1];
			int  flag = bIn.read(msgBuffer);
			if (flag<0){
				throw new ArrayIndexOutOfBoundsException("onUpdatePeersReceived no msgType in buffer");
			}
			Logger.getLogger("NotificationListener").log(Level.FINER,"Message received: " + msgBuffer[0]);
			System.out.println("Message received: " + msgBuffer[0]);
			
			if (msgBuffer[0]==0){
				// serializable WarpMessage
				ObjectInputStream in = new ObjectInputStream(bIn);
				msg = (WarpMessage) in.readObject();
				in.close();
			} 
			flag = bIn.read(msgBuffer);
			if (flag<0){
				throw new ArrayIndexOutOfBoundsException("onUpdatePeersReceived no userID in buffer");
			}
			msg.setUserID(msgBuffer[0]);
		}catch(IOException i)
		{
			i.printStackTrace();
			return;
		}catch(ClassNotFoundException c)
		{
			System.out.println("WarpMessage class not found");
			c.printStackTrace();
			return;
		}
		callBack.onGameUpdateReceived(msg); 
	}

	public void onUserJoinedLobby(LobbyData arg0, String arg1) {
		
	}

	public void onUserJoinedRoom(RoomData data, String username) {
		callBack.onUserJoinedRoom(data.getId(), username);
	}

	public void onUserLeftLobby(LobbyData arg0, String arg1) {
		
	}

	public void onUserLeftRoom(RoomData roomData, String userName) {
		System.out.println("User: " + userName + " left room!");
		callBack.onUserLeftRoom(roomData.getId(), userName);
	}

	@Override
	public void onGameStarted (String arg0, String arg1, String arg2) {
		
	}
	
	@Override
	public void onGameStopped (String arg0, String arg1) {
		
	}

	@Override
	public void onMoveCompleted (MoveEvent me) {
		
	}

	@Override
	public void onPrivateChatReceived (String arg0, String arg1) {
		
	}

	@Override
	public void onUserChangeRoomProperty (RoomData roomData, String userName, HashMap<String, Object> properties, HashMap<String, String> lockProperties) {
		int code = Integer.parseInt(properties.get("result").toString());
		callBack.onResultUpdateReceived(userName, code);
	}

	@Override
	public void onUserPaused (String arg0, boolean arg1, String arg2) {
		
	}

	@Override
	public void onUserResumed (String arg0, boolean arg1, String arg2) {
		
	}
	
}
