import java.util.Timer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;

/**
 * Tarsos reference for audio: https://github.com/JorenSix/TarsosDSP
 * 
 * GPIO reference: https://pi4j.com/1.2/usage.html
 * For Pi3 GPIO Board: https://pi4j.com/1.2/pins/model-3b-rev1.html
 * 
 * On the PI, need to include the WiringPi Native Library and install Pi4J.
 * https://pi4j.com/1.2/install.html
 * 
 * @author Reithger
 *
 */

public class Theremin {

//	final static GpioController GPIO = GpioFactory.getInstance();
	final static double change = Math.pow(2, 1.0/12.0);
	
	static double Hertz;
	static double Volume;
	private Timer timer;

	/*
    GpioPinDigitalOutput trigger = GPIO.provisionDigitalOutputPin(RaspiPin.GPIO_22);
    GpioPinDigitalInput echo = GPIO.provisionDigitalInputPin(RaspiPin.GPIO_23);
    
    GpioPinDigitalOutput trigger2 = GPIO.provisionDigitalOutputPin(RaspiPin.GPIO_00);
    GpioPinDigitalInput echo2 = GPIO.provisionDigitalInputPin(RaspiPin.GPIO_02);
	
	/*
	 * Can access GPIO via RaspiPin.GPIO_00 RaspiPin.GPIO_01 ... RaspiPin.GPIO_31
	 * For mapping GPIO_## to the physical board: http://wiringpi.com/pins/
	 * 
	 */
		
	public void run() throws Exception{
		Hertz = 110;
		Volume = 100;
		timer = new Timer();
		//timer.schedule(new TimerRepeat(this), 0, 10);
        createTone();
	}

	public static double getUltrasonicDistance(GpioPinDigitalOutput trigger, GpioPinDigitalInput echo) {
		trigger.high();
		try {
			Thread.sleep((long).01);
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("Ultrasonic Distance Reception Error");
		}
    	trigger.low();
    	long start = System.currentTimeMillis();
    	long end = System.currentTimeMillis();
    	while(echo.isLow()) { }
    		start = System.nanoTime();
    	while(echo.isHigh()) { }
    		end = System.nanoTime();
    	long elap = end - start;
    	double out = ((elap / 1000.0)/2)/29.1;
    	return out;
	}
	/*
	public void clock() {
		try {
        	double distOne = getUltrasonicDistance(trigger, echo);
        	double distTwo = getUltrasonicDistance(trigger2, echo2);
        	distOne = distOne > 100 ? 100 : distOne;
        	distTwo = distTwo > 100 ? 100 : distTwo;
        	System.out.println("D: " + distOne);
        	Hertz = 110 + distOne / 100.0 * 330;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	*/
	public static void createTone() throws LineUnavailableException {
	    float rate = 44100;
	    byte[] buf;
	    buf = new byte[1];
	    double[] sines = new double[(int)rate];
	 
	    AudioFormat audioF;
	    audioF = new AudioFormat(rate,8,1,true,false);
	 
	    SourceDataLine sourceDL = AudioSystem.getSourceDataLine(audioF);
	    sourceDL = AudioSystem.getSourceDataLine(audioF);
	    sourceDL.open(audioF);
	    sourceDL.start();
	    
		while(true) {
		  double hertzCopy = Hertz;		//so derive echo from initial sound and factor it in a few beats later
	      for(int i = 0; i < rate; i++){
		    double angle1 = i / rate * hertzCopy * 1.0 * 2.0 * Math.PI;	// * (x+2)/x; x = 1, x+= 2
		    double angle2 = i / rate * hertzCopy * 3.0 * 2.0 * Math.PI;
		    double angle3 = i / rate * hertzCopy * 5.0 * 2.0 * Math.PI;
		    double angle4 = i / rate * hertzCopy * 7.0 * 2.0 * Math.PI;
	
		    sines[i]=(double)(Math.sin(angle1) * Volume + Math.sin(angle2)*Volume/3+Math.sin(angle3)*Volume/5 + Math.sin(angle4)*Volume/7);
		    //buf[0]=(byte)(Math.sin(angle1)*Volume+Math.sin(angle2)*Volume/3+Math.sin(angle3)*Volume/5+Math.sin(angle4)*Volume/7);
		    buf[0] = (byte) sines[i];
		    sourceDL.write(buf,0,1);
		    sourceDL.write(buf,0,1);
		    //hertzCopy = hertzFunctionOne(hertzCopy, i, rate);
	    	}
		  }
	    //sourceDL.drain();
	    //sourceDL.stop();
	    //sourceDL.close();
	 }	
	 
	public static double hertzFunctionOne(double hertzCopy, int count, float rate) {
	  	  if(count % (rate / 300) == 0)
	  		  hertzCopy *= change;
	  	  if(count % (rate / 15) == 0)
	  		  hertzCopy /= Math.pow(change, 15);
	  	  return hertzCopy;
	}
	

}
