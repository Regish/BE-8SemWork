import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.TermCriteria;
import org.opencv.highgui.Highgui;
import org.opencv.ml.CvSVM;
import org.opencv.ml.CvSVMParams;


public class SceneSVMmain {
	
	public static void main(String[] args)
	{
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		
		@SuppressWarnings("unused")
		SceneSVMmodel secSvm = new SceneSVMmodel();
		
		//Building Training Data
	/*	
		int a=30 , b=36 ;
		//between 0 and 36
		secSvm.buildDataForSVM( a , b );
	*/
		
		
		
		Mat trainingData = new Mat();
		Mat trainingLabel = new Mat();
			
		Mat tempMatData , tempMatLabel;
		
		
		CvSVMParams param = new CvSVMParams();
		param.set_svm_type(CvSVM.C_SVC);
		param.set_kernel_type(CvSVM.LINEAR);
		
		TermCriteria criteria = new TermCriteria( TermCriteria.MAX_ITER,100, 1e-6);
		param.set_term_crit( criteria );

		
		//Binary Classifiers
/*		
		tempMatLabel = secSvm.getNegativeLabel();
		
		int a=25, b=36;
		
		for(int i=a; i<b; i++)//(int i=0; i<36; i++)
		{
			
			trainingData = Highgui.imread( "SVM_Mats/trainingData"+ i +".jpg", Highgui.CV_LOAD_IMAGE_ANYCOLOR );
			trainingLabel = Highgui.imread("SVM_Mats/label"+ i +".jpg", Highgui.CV_LOAD_IMAGE_ANYCOLOR);
			
			tempMatData = secSvm.getNegativeData(i);
			
			trainingData.push_back( tempMatData );
			trainingLabel.push_back( tempMatLabel );
			
			trainingData.convertTo(trainingData, CvType.CV_32FC1);
			trainingLabel.convertTo(trainingLabel, CvType.CV_32FC1);
			
	System.out.println(trainingData.size()+"\t"+trainingLabel.size());
	
			CvSVM svmObj = new CvSVM( trainingData, trainingLabel, new Mat(), new Mat(), param );
			svmObj.save("Saved_SVMs/svm"+i+".xml");
				
		}
*/
		
		
		// Multi-class classifier
		
		for(int i=0; i<36; i++)
		{
			tempMatData = Highgui.imread( "SVM_Mats/trainingData"+ i +".jpg", Highgui.CV_LOAD_IMAGE_ANYCOLOR );
			tempMatLabel = Highgui.imread("SVM_Mats/label"+ i +".jpg", Highgui.CV_LOAD_IMAGE_ANYCOLOR);
			
			trainingData.push_back( tempMatData );
			trainingLabel.push_back( tempMatLabel );
			
		}
		
		trainingData.convertTo(trainingData, CvType.CV_32FC1);
		trainingLabel.convertTo(trainingLabel, CvType.CV_32FC1);
		
		CvSVM svmObj = new CvSVM( trainingData, trainingLabel, new Mat(), new Mat(), param );
		svmObj.save("Saved_SVMs/multiClassSVM.xml");
		
	}

	
}
