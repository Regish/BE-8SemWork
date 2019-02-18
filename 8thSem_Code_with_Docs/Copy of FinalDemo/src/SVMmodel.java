import java.io.File;
import java.util.List;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;


public class SVMmodel {

	public void buildDataForSVM( int a , int b)
	{
		int cols = 4544;
		int noOfSamples = 1016;
		String DataSetFolder = "Datasets/Chars74K EnglishImg Dataset/Fnt-Computer Font";
		String folderName;
		String fileName;
		
		File folder = new File( DataSetFolder );
		File[] listOfFolder = folder.listFiles();
		
		File[] listOfImages;
		
		CharacterDescriptor cd;
		List<Float> ld;
		
		Mat finalSvmData = new Mat(); 
		Mat labels = new Mat();
		MatOfFloat mof;
		Mat m , l;
		
		
		for(int i=a ; ( i < b) ; i++) // listOfFolder.length  && listOfFolder[i].isDirectory()==true ) ; i++ )	
		{
			listOfImages = listOfFolder[i].listFiles();
			folderName = listOfFolder[i].getName();
			
			finalSvmData = new Mat(0, cols, CvType.CV_32FC1);
			labels = new Mat(0, 1 , CvType.CV_32FC1);
			//trainData_Mat = new Mat(0, cols, CvType.CV_32FC1);
			
			
			for( int j=0; j < noOfSamples  ; j++ )
			{
				fileName = DataSetFolder+"/"+folderName+"/"+ listOfImages[j].getName();
				System.out.println(fileName);
				cd = new CharacterDescriptor(  );
				Mat hello = Highgui.imread(fileName, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
				cd.buildDescriptor(hello);
				ld = cd.computeHOGAndGetFinalFeatureVector();
				
				mof = new MatOfFloat();
				mof.fromList( ld );
				m = mof.t();
				finalSvmData.push_back(m);
				
				l = new Mat(1,1,CvType.CV_32FC1);
				l.put(0, 0, i);
				labels.push_back(l);
				
			}
			
			Highgui.imwrite("SVM_Mats/trainingData"+i+".jpg", finalSvmData);
			Highgui.imwrite("SVM_Mats/label"+i+".jpg", labels);
			
		}
		
		System.out.println( finalSvmData.size() +"\n\n"+ labels.size() );
		//System.out.println( finalSvmData.dump() +"\n\n\n\n\n"+labels.dump());
	}
	
	
	
	
	public Mat getNegativeData(int i)
	{
		Mat Data = new Mat();
		Mat m;
		for(int k=0; k<36; k++)
		{
			if(k == i)
			{
				continue;
			}
			
			m = Highgui.imread("SVM_Mats/trainingData"+k+".jpg", Highgui.CV_LOAD_IMAGE_GRAYSCALE);
			//m = m.rowRange(0, 30);
		m=m.colRange(0, 512);
			Data.push_back(m);
		}
		
		return Data;
	}
	
	
	
	public Mat getNegativeLabel()
	{
		//Mat Label = new Mat(30*35, 1, CvType.CV_8UC1);
		
		Mat Label = new Mat(1016*35, 1, CvType.CV_8UC1);
		
		Label.setTo(new Scalar(777));
	/*	
		for(int k=0; k<36; k++)
		{
			if(k == i)
			{
				continue;
			}
// need to be modified			
			m = 
			m.
			m.put(0, 0, 777 );
			Label.push_back(m);
		}
		//System.out.println(Label.dump()+"\n\n\n\n");
		
	*/	
		return Label;
		
		
	}
}
