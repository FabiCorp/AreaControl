package appwarp;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;

import com.areacontrol.game.Assets;
import com.badlogic.gdx.utils.ByteArray;
import com.badlogic.gdx.utils.Json;
import com.shephertz.app42.gaming.multiplayer.client.Constants;
import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.shephertz.app42.gaming.multiplayer.client.command.WarpResponseResultCode;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomData;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent;



public class WarpController {

	private static WarpController instance;
	static int      messageCount = 0;
	
	private final String apiKey = "14a611b4b3075972be364a7270d9b69a5d2b24898ac483e32d4dc72b2df039ef";
	private final String secretKey = "55216a9a165b08d93f9390435c9be4739888d971a17170591979e5837f618059";
	
	private WarpClient warpClient;
	
	private String localUser;
	private byte   localUserID;
	private String roomId;
	
	private boolean isConnected = false;
	boolean         isUDPEnabled = false;
	
	private WarpListener warpListener;
	
	private int STATE;
	

	// Game state constants
	public static final int WAITING = 1;
	public static final int STARTED = 2;
	public static final int COMPLETED = 3;
	public static final int FINISHED = 4;
	
	// Game completed constants
	public static final int GAME_WIN = 5;
	public static final int GAME_LOOSE = 6;
	public static final int ENEMY_LEFT = 7;
	
	private final Logger log;
	
	public WarpController() {
		log = Logger.getLogger("WarpController");
		log.setLevel(Level.FINEST);
		initAppwarp();
		warpClient.addConnectionRequestListener(new ConnectionListener(this));
		warpClient.addChatRequestListener(new ChatListener(this));
		warpClient.addZoneRequestListener(new ZoneListener(this));
		warpClient.addRoomRequestListener(new RoomListener(this));
		warpClient.addNotificationListener(new NotificationListener(this));
	}
	
	public byte getLocalUserID(){
		return localUserID;
	}
	
	public static WarpController getInstance(){
		if(instance == null){
			Logger.getLogger("WarpController").log(Level.INFO,"Creating WarpController");
			instance = new WarpController();
		}
		return instance;
	}
	
	public void startApp(String localUser){
		this.localUser = localUser;
		warpClient.connectWithUserName(localUser);
	}
	
	public void setListener(WarpListener listener){
		this.warpListener = listener;
	}
	
	public void stopApp(){
		if(isConnected){
			warpClient.unsubscribeRoom(roomId);
			warpClient.leaveRoom(roomId);
		}
		warpClient.disconnect();
	}
	
	private void initAppwarp(){
		try {
			WarpClient.initialize(apiKey, secretKey);
			warpClient = WarpClient.getInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void updateResult(int code, String msg){
		if(isConnected){
			STATE = COMPLETED;
			HashMap<String, Object> properties = new HashMap<String, Object>();
			properties.put("result", code);
			warpClient.lockProperties(properties);
		}
	}
	
	public void onConnectDone(boolean status){
		log.log(Level.INFO, "onConnectDone: "+status);
		if(status){
			warpClient.initUDP();
			warpClient.joinRoomInRange(1, 1, false);
		}else{
			isConnected = false;
			handleError();
		}
	}
	
	public void onDisconnectDone(boolean status){
		
	}
	
	public void onRoomCreated(String roomId){
		if(roomId!=null){
			warpClient.joinRoom(roomId);
		}else{
			handleError();
		}
	}
	
	public void onJoinRoomDone(RoomEvent event){
		log.log(Level.INFO, "onJoinRoomDone: "+event.getResult());
		if(event.getResult()==WarpResponseResultCode.SUCCESS){// success case
			this.roomId = event.getData().getId();
			warpClient.subscribeRoom(roomId);
		}else if(event.getResult()==WarpResponseResultCode.RESOURCE_NOT_FOUND){// no such room found
			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("result", "");
			// warpClient.createRoom("superjumper", "shephertz", 2, data);
			warpClient.createRoom("AreaControl", "FabiCorp", 2, data);
		}else{
			warpClient.disconnect();
			handleError();
		}
	}
	
	public void onRoomSubscribed(String roomId){
		log.log(Level.INFO,"onSubscribeRoomDone: "+roomId);
		if(roomId!=null){
			isConnected = true;
			warpClient.getLiveRoomInfo(roomId);
		}else{
			warpClient.disconnect();
			handleError();
		}
	}
	
	public void onGetLiveRoomInfo(String[] liveUsers){
		
		if(liveUsers!=null){
			log.log(Level.INFO,"onGetLiveRoomInfo: "+liveUsers.length);
			localUserID = (byte) liveUsers.length;
			Assets.gameInfo.setPlayerID(liveUsers.length);
			if(liveUsers.length==2){
				startGame();	
			}else{
				waitForOtherUser();
			}
		}
		else{
			warpClient.disconnect();
			handleError();
		}
	}
	
	public void onUserJoinedRoom(String roomId, String userName){
		/*
		 * if room id is same and username is different then start the game
		 */
		if(localUser.equals(userName)==false){
			startGame();
		}
	}

	public void onSendChatDone(boolean status){
		log.log(Level.INFO, "onSendChatDone: "+status);
	}
	
	public void onGameUpdateReceived(WarpMessage message){
		System.out.println("onGameUpdateReceived: message from: " + message.getUserID() + " at: " + localUserID);
		if(localUserID != message.getUserID()){
			warpListener.onGameUpdateReceived(message);
		}
	}
	
	public void sendGameUpdate(OutgoingWarpMessage msg){
		msg.setUserName(localUserID);
		if(isConnected){
			if(isUDPEnabled){
				warpClient.sendUDPUpdatePeers(msg.toByteArray());
			}else{
				warpClient.sendUDPUpdatePeers(msg.toByteArray());
			}
		}	
		
	}
	
	public void onResultUpdateReceived(String userName, int code){
		log.log(Level.INFO, "ResultsUpdateReceived");
		if(localUser.equals(userName)==false){
			STATE = FINISHED;
			warpListener.onGameFinished(code, true);
		}else{
			warpListener.onGameFinished(code, false);
		}
	}
	
	public void onUserLeftRoom(String roomId, String userName){
		log.log(Level.INFO, "onUserLeftRoom "+userName+" in room "+roomId);
		if(STATE==STARTED && !localUser.equals(userName)){// Game Started and other user left the room
			warpListener.onGameFinished(ENEMY_LEFT, true);
		}
	}
	
	public int getState(){
		return this.STATE;
	}
	
	private void startGame(){
		STATE = STARTED;
		log.log(Level.INFO, "Game started");
		warpListener.onGameStarted("Start the Game");
	}
	
	private void waitForOtherUser(){
		STATE = WAITING;
		log.log(Level.INFO, "Waiting for other user");
		warpListener.onWaitingStarted("Waiting for other user");
	}
	
	private void handleError(){
		if(roomId!=null && roomId.length()>0){
			warpClient.deleteRoom(roomId);
		}
		disconnect();
	}
	
	public void handleLeave(){
		log.log(Level.INFO, "Leaving Room");
		if(isConnected){
			warpClient.unsubscribeRoom(roomId);
			warpClient.leaveRoom(roomId);
			if(STATE!=STARTED){
				warpClient.deleteRoom(roomId);
			}
			warpClient.disconnect();
		}
	}
	
	private void disconnect(){
		log.log(Level.INFO, "Disconnecting");
		warpClient.removeConnectionRequestListener(new ConnectionListener(this));
		warpClient.removeChatRequestListener(new ChatListener(this));
		warpClient.removeZoneRequestListener(new ZoneListener(this));
		warpClient.removeRoomRequestListener(new RoomListener(this));
		warpClient.removeNotificationListener(new NotificationListener(this));
		warpClient.disconnect();
	}
	
	public String getLocalUser() {
		return localUser;
	}

}
