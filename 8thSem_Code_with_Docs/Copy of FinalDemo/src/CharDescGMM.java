import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.TermCriteria;
import org.opencv.ml.EM;


public class CharDescGMM {

	public List<Float> findGmmFeature(List<MatOfFloat> hogs)
	{
		Mat bestLabels = new Mat();
		TermCriteria criteria = new TermCriteria(TermCriteria.EPS + TermCriteria.MAX_ITER,100,0.1);
		int attempts = 1;
		int flags = Core.KMEANS_PP_CENTERS;
		Mat centers = new Mat();
		
		Mat Mat_Hog = getMat(hogs);
		Mat_Hog = Mat_Hog.reshape(1);
		
		//8 centers
		Core.kmeans(Mat_Hog , 8, bestLabels, criteria, attempts, flags, centers);
		
		TermCriteria tc = new TermCriteria(TermCriteria.MAX_ITER , 100 , 0 );
		EM em = new EM( 8 , EM.COV_MAT_DIAGONAL , tc );
		
		Mat weights0;
		
		Mat logLikelihoods = new Mat();
		Mat labels = new Mat();
		Mat probs = new Mat();
			
		em.trainE(Mat_Hog, centers , new Mat(), new Mat(), logLikelihoods, labels, probs);
		weights0 = em.getMat("weights");
		
		List<List<Float>> likelihood = new ArrayList<List<Float>>();
		
		for(int i=0; i< Mat_Hog.rows(); i++)
		{
			List<Float> ld = new ArrayList<Float>();
			for(int j=0; j< weights0.cols(); j++)
			{
				float temp = (float) ( probs.get(i, j)[0] * weights0.get(0, j)[0] );
				ld.add(temp);
			}
			likelihood.add(ld);
		}
		
		List<Float> x, y;
		List<Byte> bv;
		List<List<Byte>> HistOfBinCom = new ArrayList<List<Byte>>();
		
		for(int i=0; i < likelihood.size()-1 ; i++)
		{
			x = likelihood.get(i);
			for(int j=i+1; j < likelihood.size() ; j++)
			{
				y = likelihood.get(j);
				bv = getBinaryVector(x , y);
				
				HistOfBinCom.add(bv);
			}
		}
		
		List<Float> GMMFeature = getGMMFeature( HistOfBinCom );
		
		return GMMFeature;
		
	}
	
	
	
	private List<Float> getGMMFeature( List<List<Byte>> ListOfBinVec )
	{
		double value;
		List<Float> ld = new ArrayList<Float>();
		List<Byte> temp;
		for(int i=0; i < ListOfBinVec.size() ; i++)
		{
			temp = ListOfBinVec.get(i);
			value = 0;
			for( int j=0; j< temp.size() ; j++)
			{
				if( temp.get(j) != 0 )
				{
					value += Math.pow(2, (temp.size()-1)-j );
				}
			}
			ld.add((float)value);
		}
		
		return ld;
	}
	
	
	
	private List<Byte> getBinaryVector( List<Float> x, List<Float> y )
	{
		List<Byte> lb = new ArrayList<Byte>();

		for(int index=0; index < x.size(); index++)
		{
			if( ( x.get(index) >= y.get(index) ) && ( Math.max(x.get(index), y.get(index)) != 0 ) )
			{
				lb.add((byte)1 );

			}
			else
			{
				lb.add((byte)0 );
			}
		}

		return lb;
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
