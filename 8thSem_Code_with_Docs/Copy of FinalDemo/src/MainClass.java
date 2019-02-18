import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.opencv.core.Core;
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


public class MainClass {

	public static String main(String[] args)
	{
		
		
		
		
		File fcd = new File("CD");
		File facg = new File("ACG");
		File[] fcdFiles = fcd.listFiles();
		File[] acgFiles = facg.listFiles();
		
		for( File f : fcdFiles )
		{
			f.delete();
		}
		for( File f : acgFiles )
		{
			f.delete();
		}
		
		
		
		
		
		
		
		
		
		
		
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		
		String colorImgName = args[0]; //"K.jpg"; 
		Mat colImg = Highgui.imread(colorImgName, Highgui.CV_LOAD_IMAGE_COLOR);
		BoundaryCluster bc = new BoundaryCluster(colorImgName);
		bc.prepareVectors();
		bc.clusterToLayers();
		
		String FinalString = "";
		
		List<List<Object>> finalChars  = new ArrayList<List<Object>>();;
		
		for(int ln=0; ln<5; ln++)
		{
			
			
			String filename = "Layers/layer"+ ln +".jpg" ;
			
			Mat source = Highgui.imread(filename, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
		    int[][] matrix = new int[source.rows()][source.cols()];
		    
		  //System.out.println("\n\nArray\n\n");
		    
		    for(int i=0; i<source.rows(); i++)
	    	{
	    		for(int j=0; j<source.cols(); j++)
	    		{
	    			matrix[i][j]= ((int)source.get(i, j)[0] > 0 ) ? 255 : 0 ;
	    		}
	    	}
		    
		    
		    
		    for(int i=1; i<matrix.length-1; i++)
		    {
		    	for(int j=1; j<matrix[0].length-1; j++)
		    	{
		    		/*
		    		 * 
		    		 
		    		if( matrix[i-1][j] == 255 && matrix[i+1][j] == 255 )
		    		{
		    			matrix[i][j] = 255;
		    		}
		    		else if( matrix[i][j-1] == 255 && matrix[i][j+1] == 255 )
		    		{
		    			matrix[i][j] = 255;
		    		}
		    		else if( matrix[i-1][j-1] == 255 && matrix[i+1][j+1] == 255 )
		    		{
		    			matrix[i][j] = 255;
		    		}
		    		else if( matrix[i-1][j+1] == 255 && matrix[i+1][j-1] == 255 )
		    		{
		    			matrix[i][j] = 255;
		    		}
		    		
		    		*/
		    		
		    		
		    		if(matrix[i][j] > 0 )
		    		{
		    			matrix[i][j] = 255;
		    		}
		    		
		    		//System.out.print( matrix[i][j]+" , " );
		    	}
		    	
		    	//System.out.println(" ");
		    }
		    
		    
		    
		    		//System.out.println("Mat of layer \n"+source.dump());
		    		//System.exit(0);
		    
		    TwoPass matrixComponents = new TwoPass(matrix);
	        matrix=null;
	        
	        ArrayList<Mat> lcc = matrixComponents.getConnectedComponent();
	        
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
	        
	        
	        source = source.clone();
	        for(int z=0 ; z < ConnComp.size() ; z++)
	        {
	        	Rect rect = Imgproc.boundingRect(ConnComp.get(z));
				Core.rectangle(source , new Point(rect.x , rect.y) , new Point( rect.x+rect.width , rect.y+rect.height), new Scalar(255,255,255));
	        }
	        Highgui.imwrite("ACG/test"+ ln +".jpg", source);
	        
	        
	        AdjacentCharGroup acg = new AdjacentCharGroup(ConnComp , filename , colorImgName);
	        acg.getCharGroups();
	        
	        List<Set<Integer>> SibGrp = acg.getSiblingGroup();
	        List<List<Double>> CCProp = acg.getCcProperties();
	        
	        List<Double> PropForEachCC;
	        Mat eachComponent;
	        List<Float> feature;
	        
	        Set<Integer> finalCompSet = new HashSet<Integer>();
	        for(Set<Integer> si : SibGrp )
	        {
	        	finalCompSet.addAll(si);
	        }
	        SibGrp.clear();
	        SibGrp.add(finalCompSet);
	        
	        //List<List<Float>> llf;
	        int xPos, yPos;
	        
	        for( int w=0; w<SibGrp.size(); w++ )
	        {
	        	for( int index : SibGrp.get(w) )
	        	{
	        		PropForEachCC = CCProp.get(index);
	        		
	        		xPos = ( PropForEachCC.get(3).intValue()  - ( PropForEachCC.get(2).intValue() / 2 ) );
	        		yPos = ( PropForEachCC.get(4).intValue()  - ( PropForEachCC.get(1).intValue() / 2 ) );
	        		
	        		
	        		//eachComponent = colImg.submat( PropForEachCC.get(4).intValue() - 5 , (int) (PropForEachCC.get(4)+PropForEachCC.get(2) + 5)  , PropForEachCC.get(3).intValue() - 5 , (int)(PropForEachCC.get(3)+PropForEachCC.get(2) + 5 ) );
	        	//	try
	        	//	{
	        	//		eachComponent = colImg.submat(yPos-5, yPos + PropForEachCC.get(1).intValue() +5, xPos-5, xPos + PropForEachCC.get(2).intValue() +5) ;
	        	//	}
	        	//	catch(CvException excep)
	        	//	{
	        			eachComponent = colImg.submat(yPos, yPos + PropForEachCC.get(1).intValue() , xPos, xPos + PropForEachCC.get(2).intValue() ) ;
	        	//	}
	        		
	        		Imgproc.resize(eachComponent, eachComponent, new Size( 128 , 128 ) );
	        		
	        		CharacterDescriptor cd = new CharacterDescriptor();
	        		cd.buildDescriptor(eachComponent);
	        		
	        		feature = cd.computeHOGAndGetFinalFeatureVector();
	        		
	        	//System.out.println(feature);	

	        		MatOfFloat sample = new MatOfFloat();
	        		sample.fromList(feature.subList(0, 512));
	        		

	        		//do from here
	        		//System.out.println("\n"+feature+"\n");
	        	
	        		
	        		
	        		
	        		
	        		
	        	/*	
	        		PredictMyComponent pmc = new PredictMyComponent();
	        		@SuppressWarnings("unused")
					char ch = pmc.predict(sample);
	        	*/
	        		
	        		
	        		
	        		
	        		
	        		
	        		
	        		
	        		
	        		
	        		
	        	//System.out.println("Char is \t"+ch+"\t Cx=>"+PropForEachCC.get(3)+"\t Cy=>"+PropForEachCC.get(4)+"\t Layer num => "+ln+"\t Compindex is "+index);
	        		
	        		Mat clonedColImg = colImg.clone();
	        		Core.rectangle(clonedColImg, new Point((PropForEachCC.get(4).intValue() - 5), (PropForEachCC.get(4)+PropForEachCC.get(2) + 5 )), new Point( (PropForEachCC.get(3).intValue() - 5 ) , (PropForEachCC.get(3)+PropForEachCC.get(2) + 5 ) ), new Scalar(255,255,255));
	        		

	        		String joker = filename.substring( filename.lastIndexOf("/") +1 , filename.lastIndexOf(".") );
	        		
	        		Highgui.imwrite("CD/component"+joker+"_"+index+".jpg", eachComponent);
	        		
	        		
	        		//Initialize an instance of OcrEngine
	        		OcrEngine ocrEngine = new OcrEngine();
	        		
	        		String strPath = "CD/component"+joker+"_"+index+".jpg";
	        		//Set the Image property by loading the image from file path location
	        		ocrEngine.setImage(ImageStream.fromFile(strPath));
	        		
	        		//Process the image
	        		boolean try_catch_var=false;
	        		  try{ try_catch_var=ocrEngine.process(); }
	        		  catch(Exception expception){;}
	        		if ( try_catch_var )
	        		{
	        		    //Display the recognized text
	        			String tempstr = "";
	        			//System.out.println("Ocr says ===> \t"+ocrEngine.getText());
	        		    tempstr += ocrEngine.getText();
	        			
	        		    if(tempstr.length() == 1)
	        		    {
	        		    	List<Object> lo = new ArrayList<Object>();
		        		    lo.add(ocrEngine.getText());
		        		    lo.add(PropForEachCC.get(3).intValue());
		        		    finalChars.add( lo);
	        		    }
	        		    
	        		}
	        		//}
	        		//catch(Exception exception){}
	        		
	        	}
	        }
	        
	        
	       
		}
		
	/*	
		
	*/
		
		//System.out.println("\nbefore sort finalChars ==>  \t"+finalChars);
        for(int i=0; i<finalChars.size()-1; i++)
        {
        	for(int j=i+1; j<finalChars.size(); j++)
        	{
        		if((int)finalChars.get(j).get(1) < (int)finalChars.get(i).get(1) )
        		{
        			List<Object> templo = finalChars.get(j);
        			finalChars.remove(j);
        			finalChars.add(i, templo);
        		}
        	}
        	
        }
		//System.out.println("\n finalChars ==>  \t"+finalChars);
		
        for(List<Object> lob : finalChars)
        {
        	FinalString += lob.get(0);
        }
        
        System.out.println(FinalString);
        
        return FinalString;
	}
}
