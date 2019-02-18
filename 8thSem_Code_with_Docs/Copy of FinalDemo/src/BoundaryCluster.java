import java.util.ArrayList;
import java.util.Vector;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.core.TermCriteria;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.ml.EM;


public class BoundaryCluster {

	String fname;
	Mat source;
	Mat kmeanVec;
	ObservationPoints obp;
	int numClus;
	
	// Constructor preprocesses the image - Sharpening
	public BoundaryCluster(String imagePathName)
	{
		fname = imagePathName;
		
		source = Highgui.imread(fname, Highgui.CV_LOAD_IMAGE_COLOR);
		Mat sharpen = new Mat(source.rows(),source.cols(),source.type());
		Imgproc.GaussianBlur(source, sharpen,new Size(0,0), 30);
		Core.addWeighted(source, 1.5, sharpen, -0.5, 0, sharpen);
        Highgui.imwrite("BC/Sharpen.jpg", sharpen);
		source = sharpen;
		
		numClus = 5;
	}
	
	
	//Prepare the vectors for all the edge pixels
	public void prepareVectors()
	{
		//Canny Edge Detection
		Mat destination = new Mat(source.rows(),source.cols(),source.type());
		Imgproc.Canny(source, destination, 200, 300, 3, true);
		Highgui.imwrite("BC/output.jpg", destination);
		
		//Create an object to help building vectors, group them and create layer images
		obp = new ObservationPoints();
		
		//to contain color pairs
		ArrayList<double[]> cpairs;
		
		//for each vector
		Mat vec = new Mat(1,12, CvType.CV_32F);
		
		//for all the vectors
		kmeanVec = new Mat(0, 12 , CvType.CV_32F);
		
		for(int i=0; i<destination.rows(); i++)
		{
			for(int j=0; j<destination.cols(); j++)
			{
				if(destination.get(i, j)[0] == 255.0)
				{
					//gets Color pairs
					cpairs = obp.getMaxColorDiff(i,j,source);
					if(cpairs != null)
					{
						//Build a vector of format
						// { B1 , G1, R1, B2, G2, R2, x, x, x, y, y, y }
						
						vec.put(0, 0, cpairs.get(0)[0]);
						vec.put(0, 1, cpairs.get(0)[1]);
						vec.put(0, 2, cpairs.get(0)[2]);
						vec.put(0, 3, cpairs.get(1)[0]);
						vec.put(0, 4, cpairs.get(1)[1]);
						vec.put(0, 5, cpairs.get(1)[2]);

						//col value i.e, x-axis
						vec.put(0, 6, j);
						vec.put(0, 7, j);
						vec.put(0, 8, j);
					  	
						//row value i.e, y-axis
						vec.put(0, 9, i);
						vec.put(0, 10, i);
						vec.put(0, 11, i);
						
						//Add each vector
						kmeanVec.push_back(vec);
					}
				}
			}
		}
		
	}
	
	
	//Does k-means clustering followed by EM algorithm, then group to layers
	public void clusterToLayers()
	{
		//k-means
		Mat bestLabels = new Mat();
		TermCriteria criteria = new TermCriteria(TermCriteria.EPS + TermCriteria.MAX_ITER,100,0.1);
		int attempts = 1;
		int flags = Core.KMEANS_PP_CENTERS;
		Mat centers = new Mat();
		
		Core.kmeans(kmeanVec , numClus, bestLabels, criteria, attempts, flags, centers);
		
		//k-means clusters
		Mat cluster0 = obp.getCluster(kmeanVec, bestLabels, 0);
		Mat cluster1 = obp.getCluster(kmeanVec, bestLabels, 1);
		Mat cluster2 = obp.getCluster(kmeanVec, bestLabels, 2);
		Mat cluster3 = obp.getCluster(kmeanVec, bestLabels, 3);
		Mat cluster4 = obp.getCluster(kmeanVec, bestLabels, 4);
		//Mat cluster5 = obp.getCluster(kmeanVec, bestLabels, 5);
		//Mat cluster6 = obp.getCluster(kmeanVec, bestLabels, 6);
		//Mat cluster7 = obp.getCluster(kmeanVec, bestLabels, 7);
		
		Mat covar0 = new Mat();
		Mat covar1 = new Mat();
		Mat covar2 = new Mat();
		Mat covar3 = new Mat();
		Mat covar4 = new Mat();
		
		//covariance matrix from means/centers
		Core.calcCovarMatrix(cluster0, covar0, centers.row(0), Core.COVAR_USE_AVG + Core.COVAR_NORMAL + Core.COVAR_ROWS, centers.type());
		Core.calcCovarMatrix(cluster1, covar1, centers.row(1), Core.COVAR_USE_AVG + Core.COVAR_NORMAL + Core.COVAR_ROWS, centers.type());
		Core.calcCovarMatrix(cluster2, covar2, centers.row(2), Core.COVAR_USE_AVG + Core.COVAR_NORMAL + Core.COVAR_ROWS, centers.type());
		Core.calcCovarMatrix(cluster3, covar3, centers.row(3), Core.COVAR_USE_AVG + Core.COVAR_NORMAL + Core.COVAR_ROWS, centers.type());
		Core.calcCovarMatrix(cluster4, covar4, centers.row(4), Core.COVAR_USE_AVG + Core.COVAR_NORMAL + Core.COVAR_ROWS, centers.type());
		
		//EM algorithm
		TermCriteria tc = new TermCriteria(TermCriteria.MAX_ITER , 100 , 0 );
		EM em = new EM( numClus , EM.COV_MAT_DIAGONAL , tc );
		Vector<Mat> covs0 = new Vector<Mat>();
		
		covs0.add(covar0);
		covs0.add(covar1);
		covs0.add(covar2);
		covs0.add(covar3);
		covs0.add(covar4);
		
		Mat weights0 = Mat.ones(1, numClus, CvType.CV_32F);
		Mat logLikelihoods = new Mat();
		Mat emLabels = new Mat();
		Mat probs = new Mat();
		
		Boolean flag = em.trainE(kmeanVec, centers, new Mat(), weights0, logLikelihoods, emLabels, probs);
		
		if(flag)
		{
			cluster0 = obp.getLayer(kmeanVec, emLabels, 0.0);
			cluster1 = obp.getLayer(kmeanVec, emLabels, 1.0);
			cluster2 = obp.getLayer(kmeanVec, emLabels, 2.0);
			cluster3 = obp.getLayer(kmeanVec, emLabels, 3.0);
			cluster4 = obp.getLayer(kmeanVec, emLabels, 4.0);
			//cluster5 = obp.getLayer(kmeanVec, emLabels, 5.0);
			//cluster6 = obp.getLayer(kmeanVec, emLabels, 6.0);
			//cluster7 = obp.getLayer(kmeanVec, emLabels, 7.0); 
		 
			obp.createLayerImg(source.rows(), source.cols(), cluster0, 0);
			obp.createLayerImg(source.rows(), source.cols(), cluster1, 1);
			obp.createLayerImg(source.rows(), source.cols(), cluster2, 2);
			obp.createLayerImg(source.rows(), source.cols(), cluster3, 3);
			obp.createLayerImg(source.rows(), source.cols(), cluster4, 4);
			//obp.createLayerImg(source.rows(), source.cols(), cluster5, 5);
			//obp.createLayerImg(source.rows(), source.cols(), cluster6, 6);
			//obp.createLayerImg(source.rows(), source.cols(), cluster7, 7);
		}
		
	}
	
}
