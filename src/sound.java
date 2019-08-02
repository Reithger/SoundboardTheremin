import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class sound {
	final static double change = Math.pow(2, 1.0/12.0);
	
	static double Hertz;
	static double Volume;
		
	public static void main(String[] args) throws Exception{
		Hertz = 110;
		Volume = 100;
		createTone();
	}
	
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
	    
	    boolean cond = true;
	 
	   	while(cond) {
	   		double hertzCopy = Hertz;
		    for(int i=0; i<rate; i++){
		    	  double angle1 = i/rate*hertzCopy*1.0*2.0*Math.PI;
		    	  double angle2 = i/rate*hertzCopy*3.0*2.0*Math.PI;
		    	  double angle3 = i/rate*hertzCopy*5.0*2.0*Math.PI;
		    	  double angle4 = i/rate*hertzCopy*7.0*2.0*Math.PI;
		    	 
		    	  buf[0]=(byte)(Math.sin(angle1)*Volume+Math.sin(angle2)*Volume/3+Math.sin(angle3)*Volume/5+
		    			  														Math.sin(angle4)*Volume/7);
		    	  sourceDL.write(buf,0,1);
		    	  sourceDL.write(buf,0,1);
		    	  sines[i]=(double)(Math.sin(angle1) * Volume + Math.sin(angle2)*Volume/3+Math.sin(angle3)*Volume/5 +
		    			  																Math.sin(angle4)*Volume/7);
		    	  hertzCopy = hertzFunctionOne(hertzCopy, i, rate);
		    	}
	   	}
	    sourceDL.drain();
	    sourceDL.stop();
	    sourceDL.close();
	 }	
	 
	public static double hertzFunctionOne(double hertzCopy, int count, float rate) {
	  	  if(count % (rate / 300) == 0)
	  		  hertzCopy *= change;
	  	  if(count % (rate / 15) == 0)
	  		  hertzCopy /= Math.pow(change, 15);
	  	  return hertzCopy;
	}

	
}
