import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.TermCriteria;


public class CharDescBOW {

	public List<Float> getBOWFeature( List<MatOfFloat> harris, List<MatOfFloat> mser, List<MatOfFloat> dense, List<MatOfFloat> rand)
	{
		int vocabulary_size = 128;
		TermCriteria criteria = new TermCriteria(TermCriteria.MAX_ITER +TermCriteria.EPS, 100 , 0.1 );
		int attempts = 1;
		int flags = Core.KMEANS_PP_CENTERS;
		
		int hSize = harris.size();
		int mSize = mser.size();
		int dSize = dense.size();
		int rSize = rand.size();
		
		List<MatOfFloat> tempList = new ArrayList<MatOfFloat>();
		tempList.addAll(harris);
		tempList.addAll(mser);
		tempList.addAll(dense);
		tempList.addAll(rand);
		
		Mat Mat_Hog = getMat( tempList );
		
		
		
		Mat_Hog = Mat_Hog.reshape(1);
		Mat bestLabels = new Mat();
		Mat centers = new Mat();
		
		
		Core.kmeans(Mat_Hog , vocabulary_size, bestLabels, criteria, attempts, flags, centers);
		
		double[] harris_bow_array = getBOWFeature( harris, centers, bestLabels, 0, hSize-1 );
		double[] mser_bow_array = getBOWFeature( mser, centers, bestLabels, hSize , hSize + mSize - 1);
		double[] dense_bow_array = getBOWFeature( dense, centers, bestLabels, hSize+mSize, hSize + mSize + dSize - 1 );
		double[] rand_bow_array = getBOWFeature( rand, centers, bestLabels , hSize+mSize+dSize , hSize + mSize + dSize + rSize -1 );
		
		List<Float> harris_bow = getListFromArray(harris_bow_array);
		List<Float> mser_bow = getListFromArray(mser_bow_array);
		List<Float> dense_bow = getListFromArray(dense_bow_array);
		List<Float> rand_bow = getListFromArray(rand_bow_array);
		
		
		List<Float> bowFinalFeature = new ArrayList<Float>();
		bowFinalFeature.addAll(harris_bow);
		bowFinalFeature.addAll(mser_bow);
		bowFinalFeature.addAll(dense_bow);
		bowFinalFeature.addAll(rand_bow);
		
		return bowFinalFeature;
	}
	
	
	
	
	private List<Float> getListFromArray(double[] array)
	{
		List<Float> ld = new ArrayList<Float>();
		
		for(double d: array)
		{
			ld.add((float)d);
		}
		
		return ld;
	}
	
	
	
	private double[] getBOWFeature( List<MatOfFloat> hog , Mat centers , Mat labels, int low, int high )
	{
		double[] bowFeature = new double[ centers.rows() ];
		int centroid ;
		
		for(int i=low; i<=high; i++)
		{
			centroid = (int) labels.get(i, 0)[0];
			bowFeature[centroid] += 1;
		}
		
		return bowFeature;		
	}
	
	
	
	private Mat getMat(List<MatOfFloat> hogs)
	{
		Mat m = new Mat(0 , hogs.get(0).rows(), CvType.CV_32F);
		Mat temp;
		
		for(int row=0; row < hogs.size(); row++)
		{
			temp = hogs.get(row);
			temp = temp.t();
			m.push_back(temp);
			
		}
		return m;
	}

	
}
