package appwarp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SerializableWarpMessage  extends WarpMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public void broadcast() {
		
		try
		{
			ByteArrayOutputStream bOut = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bOut);
			out.writeObject(this);
			out.close();
			Logger.getLogger("WarpMessage").log(Level.FINEST,"Sending Message: Size: " + bOut.size());
			if (bOut.size()>10000)
				throw new IllegalArgumentException("Appwarp Buffer Too Long");
		
			OutgoingWarpMessage msg = new OutgoingWarpMessage((byte) 0, bOut); 
			msg.broadcast();
			
			/* use this code for multi-buffer msg   
			ByteArrayOutputStream bOut2 = new ByteArrayOutputStream();
			bOut2.write(bOut.size());
			bOut2.write(bOut.toByteArray());
			System.out.println("Warpmessage sendign " + bOut2.size()/sliceSize+1 + " slices");
			int end;
			for(int slice=0; slice < bOut2.size()/sliceSize+1; slice++){
				end = (slice+1)*sliceSize;
				if (bOut.size()<end)
					end = bOut.size();
				
				}	
			}
			*/
		}catch(IOException i)
		{
			i.printStackTrace();
		}
	
		
	}
}
