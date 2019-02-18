import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.TermCriteria;
import org.opencv.highgui.Highgui;
import org.opencv.ml.CvSVM;
import org.opencv.ml.CvSVMParams;


public class SVMmain 
{

	public static void main(String[] args)
	{
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		SVMmodel svmM = new SVMmodel();
		
		//int a=8 , b=10;
//		int a=20 , b=25;
		//a starts from 0;   b is max to 61; only caps==>36
		
//svmM.buildDataForSVM(a , b);

		
		//SVMDataCreator trd = new SVMDataCreator(6,7);
		
		//SVMDataCreator th = new SVMDataCreator(7,8);
		
		//SVMDataCreator th3 = new SVMDataCreator(8, 9);
		
		//SVMDataCreator t3 = new SVMDataCreator(8, 9);
	/*	
		try {
			trd.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			th.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			t3.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	*/	

		
		
		
	  
	    Mat trainingData = new Mat();
		Mat trainingLabel = new Mat();
		
		Mat tempMatData , tempMatLabel;
		
		CvSVMParams param = new CvSVMParams();
		param.set_svm_type(CvSVM.C_SVC);
		param.set_kernel_type(CvSVM.LINEAR);
		
		TermCriteria criteria = new TermCriteria( TermCriteria.MAX_ITER,100, 1e-6);
		param.set_term_crit( criteria );

		
		
		tempMatLabel = svmM.getNegativeLabel();
/*		
		int a=30, b=36;
		
		for(int i=a; i<b; i++)//(int i=0; i<36; i++)
		{
			
			trainingData = Highgui.imread( "SVM_Mats/trainingData"+ i +".jpg", Highgui.CV_LOAD_IMAGE_ANYCOLOR );
			trainingLabel = Highgui.imread("SVM_Mats/label"+ i +".jpg", Highgui.CV_LOAD_IMAGE_ANYCOLOR);
			
		 trainingData=trainingData.submat(0, trainingData.rows(), 0, 512); //.colRange(0, 512);
			
			tempMatData = svmM.getNegativeData(i);
		//System.out.println("trData size => "+trainingData.size()+"\t tmpData size => "+tempMatData.size());	
			trainingData.push_back( tempMatData );
			trainingLabel.push_back( tempMatLabel );
			
			trainingData.convertTo(trainingData, CvType.CV_32FC1);
			trainingLabel.convertTo(trainingLabel, CvType.CV_32FC1);
			
	System.out.println(trainingData.size()+"\t"+trainingLabel.size());
	
			CvSVM svmObj = new CvSVM( trainingData, trainingLabel, new Mat(), new Mat(), param );
			svmObj.save("Saved_SVMs/svm"+i+".xml");
			
			
		}

*/

		
		for(int i=0; i<36; i++)
		{
			tempMatData = Highgui.imread( "SVM_Mats/trainingData"+ i +".jpg", Highgui.CV_LOAD_IMAGE_ANYCOLOR );
			tempMatLabel = Highgui.imread("SVM_Mats/label"+ i +".jpg", Highgui.CV_LOAD_IMAGE_ANYCOLOR);
	tempMatData=tempMatData.colRange(0, 512);		
			trainingData.push_back( tempMatData );
			trainingLabel.push_back( tempMatLabel );
			
		}
		
		trainingData.convertTo(trainingData, CvType.CV_32FC1);
		trainingLabel.convertTo(trainingLabel, CvType.CV_32FC1);
		
		CvSVM svmObj = new CvSVM( trainingData, trainingLabel, new Mat(), new Mat(), param );
		svmObj.save("Saved_SVMs/multiClassSVM.xml");
		

		
	}
}
