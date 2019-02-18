import java.util.Random;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;


public class CreateRandomPoints {

	public static void main(String[] args)
	{
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		
		Random r = new Random();
		int low_limitX = 1;
		int high_limitX = 127;
		int low_limitY = 1;
		int high_limitY = 127;
		float ranX;
		float ranY;

		Mat randomKeypoint = new Mat(0, 2, CvType.CV_32F) , m;
		
		for(int i=0; i<64; i++)
		{
			ranX = r.nextInt(high_limitX - low_limitX) + low_limitX;
			ranY = r.nextInt(high_limitY - low_limitY) + low_limitY;
			
			m = new Mat(1, 2, CvType.CV_32F);
			m.put(0, 0, ranX);
			m.put(0, 1, ranY);
			
			randomKeypoint.push_back(m);
			
		}
		
		Highgui.imwrite("Random/RandomPoints.jpg", randomKeypoint);
	}
}
