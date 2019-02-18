import org.opencv.core.Mat;
import org.opencv.ml.CvSVM;


public class PredictToday {

	char arr[] = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};;
	
	public char Predict( Mat sample )
	{
		CvSVM svmObj = new CvSVM();
		
		svmObj.load("ClassSVM.xml");//.load();
		int num = (int) svmObj.predict(sample);
		return arr[num];
	
	}	
}
