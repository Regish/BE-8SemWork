import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import com.aspose.ocr.ImageStream;
import com.aspose.ocr.OcrEngine;


@SuppressWarnings("unused")
public class TempTester {

	public static void main(String[] args)
	{
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		
		
		
		//String filename = "Layers/layer"+ 1 +".jpg" ;
/*		
		Mat source = Highgui.imread(filename, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
	    int[][] matrix = new int[source.rows()][source.cols()];
	    
	    for(int i=0; i<source.rows(); i++)
    	{
    		for(int j=0; j<source.cols(); j++)
    		{
    			matrix[i][j]=(int)source.get(i, j)[0];
    		}
    	}
		
		TwoPass tp = new TwoPass(matrix);
		ArrayList<Mat> lcc = tp.getConnectedComponent();
		
        
        ArrayList<MatOfPoint> ConnComp = new ArrayList<MatOfPoint>();
        Mat ref;
        int x, y;
        
        List<Point> lp;
        Point p;
        MatOfPoint m;
        
        for(int i=0; i < lcc.size() ; i++ )
        {
        	if(lcc.get(i).rows() >= 20)//8)
        	{
        		lp = new ArrayList<Point>();
        		for(int k = 0; k < lcc.get(i).rows() ; k++)
        		{
        			ref = lcc.get(i);
        			x = (int)ref.get(k, 0)[0];
        			y = (int)ref.get(k, 1)[0];
        			p = new Point(x,y);
        			lp.add(k, p);
        		}
        		m = new MatOfPoint();
        		m.fromList(lp);
        		ConnComp.add(m);
        	}
        }
		
		
		int col,row, count=0;
		for( MatOfPoint compMat: ConnComp )
		{
			Mat tempMat = new Mat(source.size(), CvType.CV_8U);
			count++;
			for(int ak=0; ak<compMat.rows(); ak++)
			{
				col =(int) compMat.toList().get(ak).x;
				row =(int) compMat.toList().get(ak).y;//.get(ak, 1)[0];
				tempMat.put(row, col, 255);
			}
			
			Highgui.imwrite("2Pass/"+count+"comp.jpg", tempMat);
		}
		
		
		
		 for(int z=0 ; z < ConnComp.size() ; z++)
	        {
	        	Rect rect = Imgproc.boundingRect(ConnComp.get(z));
				Core.rectangle(source , new Point(rect.x , rect.y) , new Point( rect.x+rect.width , rect.y+rect.height), new Scalar(255,255,255));
	        }
	        Highgui.imwrite("ACG/test"+ 1237 +".jpg", source);
	        
	        
*/
	/*
		String fname = "num.jpg";
		Mat source = Highgui.imread(fname, Highgui.CV_LOAD_IMAGE_COLOR);
		Mat sharpen = new Mat(source.rows(),source.cols(),source.type());
		Imgproc.GaussianBlur(source, sharpen,new Size(0,0), 30);
		Core.addWeighted(source, 1.5, sharpen, -0.5, 0, sharpen);
        Highgui.imwrite("BC/Sharpen.jpg", sharpen);
		source = sharpen;
		
		int numClus = 5;
	
		//Canny Edge Detection
		Mat destination = new Mat(source.rows(),source.cols(),source.type());
		Imgproc.Canny(source, destination, 200, 300, 3, true);
		Highgui.imwrite("BC/canny.jpg", destination);
	*/
		
/*		
		String fname = "SomeDataSetImgs/2.png";
		Mat source = Highgui.imread(fname, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
		
		CharacterDescriptor cd = new CharacterDescriptor();
		cd.buildDescriptor(source);
		
		List<Float> feature = cd.computeHOGAndGetFinalFeatureVector();
		
		MatOfFloat sample = new MatOfFloat();
		sample.fromList(feature);
		
		PredictMyComponent pmc = new PredictMyComponent();
		char ch = pmc.predict(sample);
		System.out.println("Char is "+ch);
*/
		

 
		//Initialize an instance of OcrEngine
		OcrEngine ocrEngine = new OcrEngine();
		
		String strPath = "SomeDataSetImgs/0.png";
		//Set the Image property by loading the image from file path location
		ocrEngine.setImage(ImageStream.fromFile(strPath));
		
		//Process the image
		if (ocrEngine.process())
		{
		    //Display the recognized text
		    System.out.println("Ocr says ===> \n"+ocrEngine.getText());
		}
		
	}
		
}
