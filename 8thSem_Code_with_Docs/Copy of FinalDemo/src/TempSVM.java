import org.opencv.core.Core; 
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.TermCriteria;
import org.opencv.highgui.Highgui;
import org.opencv.ml.CvSVM;
import org.opencv.ml.CvSVMParams;


public class TempSVM {

	public static void main(String[] args)
	{
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		Mat trainingData = new Mat();
		Mat trainingLabel = new Mat();
			
		CvSVMParams param = new CvSVMParams();
		param.set_svm_type(CvSVM.C_SVC);
		param.set_kernel_type(CvSVM.LINEAR);
			
		TermCriteria criteria = new TermCriteria( TermCriteria.MAX_ITER,100, 1e-6);
		param.set_term_crit( criteria );

/*		
		int a=20 , b=36;	
			
		for(int i=a; i<b; i++)
		{
			trainingData = Highgui.imread( "SVM_Mats/trainingData"+ i +".jpg", Highgui.CV_LOAD_IMAGE_ANYCOLOR );
			trainingLabel = Highgui.imread("SVM_Mats/label"+ i +".jpg", Highgui.CV_LOAD_IMAGE_ANYCOLOR);
			
			trainingData = trainingData.rowRange(0, 36);
			trainingLabel = trainingLabel.rowRange(0, 36);
			
			Mat negData = new Mat();
			for(int j=0; j<36; j++)
			{
				Mat m;
				if(j == i)
				{
					continue;
				}
				else
				{
					m = Highgui.imread("SVM_Mats/trainingData"+j+".jpg", Highgui.CV_LOAD_IMAGE_GRAYSCALE);
					//m = m.rowRange(0, 30);
				m = m.row(0);
					negData.push_back(m);
							
				}
				
			}
			
			trainingData.push_back(negData);
			
			Mat Label = new Mat(1*35, 1, CvType.CV_8UC1);
			
			Label.setTo(new Scalar(777));
			
			trainingLabel.push_back(Label);
			
			
			
			trainingData.convertTo(trainingData, CvType.CV_32FC1);
			trainingLabel.convertTo(trainingLabel, CvType.CV_32FC1);
			
			CvSVM svmObj = new CvSVM( trainingData, trainingLabel, new Mat(), new Mat(), param );
			svmObj.save("Saved_SVMs/svm"+i+".xml");
			
		}
*/
		
		
		int a=0 , b=36;
		Mat data=new Mat();
		Mat lab=new Mat();
		
		for(int i=a; i<b; i++)
		{
			trainingData = Highgui.imread( "SVM_Mats/trainingData"+ i +".jpg", Highgui.CV_LOAD_IMAGE_ANYCOLOR );
			trainingLabel = Highgui.imread("SVM_Mats/label"+ i +".jpg", Highgui.CV_LOAD_IMAGE_ANYCOLOR);
			
			trainingData = trainingData.rowRange(0, 36);
			trainingLabel = trainingLabel.rowRange(0, 36);
			
			data.push_back(trainingData);
			lab.push_back(trainingLabel);
						
		}
		
		data.convertTo(data, CvType.CV_32FC1);
		lab.convertTo(lab, CvType.CV_32FC1);
		
		CvSVM svmObj = new CvSVM( data, lab, new Mat(), new Mat(), param );
		svmObj.save("Saved_SVMs/multiClassSVM.xml");

	}
}
