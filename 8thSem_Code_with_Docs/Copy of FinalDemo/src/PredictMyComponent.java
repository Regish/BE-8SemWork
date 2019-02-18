//import java.util.List; 

import org.opencv.core.Mat;
//import org.opencv.core.MatOfFloat;
//import org.opencv.highgui.Highgui;
import org.opencv.ml.CvSVM;


public class PredictMyComponent {

	char arr[] = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};;
	public PredictMyComponent()
	{
	
		//arr = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	}
	
	
	
	public char predict( Mat  sample)
	{
	   //sample = sample.colRange(0, 512);
		
		for(int i=0; i<36; i++)
		{
			CvSVM svmObj = new CvSVM();			
			svmObj.load("Saved_SVMs/svm"+i+".xml");
			
			if( svmObj.predict(sample,false) > 37)
			{
				continue;
			}
			else
			{
				//System.out.println(" The Character is \t"+ arr[i]);
				return arr[i];
				//break;
			}
		}
		
		return '?' ;
	}
	
	
	
		
	public char Predict( Mat sample )
	{
	  //sample = sample.colRange(0, 512);
		
		CvSVM svmObj = new CvSVM();
		
		svmObj.load("Saved_SVMs/multiClassSVM.xml");//.load();
		int num = (int) svmObj.predict(sample);
		return arr[num];
	
	}	
		
		//check if trained
	
	
	/*	
		CharacterDescriptor cd = new CharacterDescriptor();
		Mat testimage =  Highgui.imread( "SomeDataSetImgs/"+i+".png" , Highgui.CV_LOAD_IMAGE_GRAYSCALE);
		cd.buildDescriptor(testimage);
		List<Float> lf = cd.computeHOGAndGetFinalFeatureVector();
		MatOfFloat sample = new MatOfFloat();
		sample.fromList(lf);
		System.out.println("answer "+i+" ==> "+svmObj.predict(sample, false));
	*/	
	


	
	
	
}
