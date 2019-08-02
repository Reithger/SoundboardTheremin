

import java.util.TimerTask;

/**
 * This class extends the TimerTask class to specify what behavior should occur periodically
 * for a provided object of the Intermediary type. (Very specific intent, but easily copied
 * for usage in having any object have periodic behavior.)
 * 
 * @author Mac Clevinger
 *
 */

public class TimerRepeat extends TimerTask{

//---  Instance Variables   -------------------------------------------------------------------

	/** Intermediary object designating the object that this TimerRepeat is associated to; will call clock() on this object*/
	private Theremin parent;
	
//---  Constructors   -------------------------------------------------------------------------
	
	/**
	 * Constructor for objects of the TimerRepeat type that assigns the provided
	 * Intermediary object as the object to perform the clock() method.
	 * 
	 * @param par - Intermediary object that this TimerRefresh object will tell to call clock().
	 */
	
	public TimerRepeat(Theremin par){
		super();
		parent = par;
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	@Override
	public void run(){
		//Whatever is here happens at whatever rate specified by a Timer object when this TimerRepeat is scheduled with it
		//parent.clock();
	}
	
}
