import java.util.ArrayList;
import java.util.List;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.KeyPoint;
import org.opencv.highgui.Highgui;
import org.opencv.objdetect.HOGDescriptor;


public class CharacterDescriptor {

	String C_I_Name;
	List<Rect> Li_Re;
	MatOfKeyPoint harris_MOKP, mser_MOKP, dense_MOKP, rand_MOKP;
	
	List<MatOfFloat> harris_hogs , mser_hogs, dense_hogs, rand_hogs;
	
	Mat img ;
	
	public void buildDescriptor( Mat image)
	{
		Mat sub_mat = image;
		
		FeatureDetector fd = FeatureDetector.create(FeatureDetector.HARRIS);
		harris_MOKP = new MatOfKeyPoint();
		fd.detect(sub_mat, harris_MOKP);
		
		fd = FeatureDetector.create(FeatureDetector.MSER);
		mser_MOKP = new MatOfKeyPoint();
		fd.detect(sub_mat, mser_MOKP);
		
		int xInc = sub_mat.cols() / 9; 
		int yInc = sub_mat.rows() / 9;
		int xVal = 0;
		int yVal = 0;
		KeyPoint kp;
		List<KeyPoint> LOKP = new ArrayList<KeyPoint>();
		float diameter = 1;
		for(int i=0; i<8; i++)
		{
			yVal += yInc;
			for(int j=0; j<8; j++)
			{
				xVal += xInc;
				kp = new KeyPoint(xVal, yVal, diameter);
				LOKP.add(kp);
			}
			xVal = 0;
		}
		dense_MOKP = new MatOfKeyPoint();
		dense_MOKP.fromList(LOKP);
		
		LOKP.clear();
		diameter = 1;
		Mat random = Highgui.imread( "Random/RandomPoints.jpg", Highgui.CV_LOAD_IMAGE_ANYCOLOR);
		for(int i=0; i<64; i++)
		{
			kp = new KeyPoint((int)random.get(i, 0)[0] , (int)random.get(i, 1)[0] , diameter);
			LOKP.add(kp);
		}
		rand_MOKP = new MatOfKeyPoint();
		rand_MOKP.fromList(LOKP);
		
		Mat dest = new Mat(sub_mat.rows(),sub_mat.cols(), CvType.CV_32FC1);
		
		Features2d.drawKeypoints(sub_mat, harris_MOKP, dest);
		Highgui.imwrite("CD/harris.jpg", dest);
		
		Features2d.drawKeypoints(sub_mat, mser_MOKP, dest);
		Highgui.imwrite("CD/mser.jpg", dest);
		
		Features2d.drawKeypoints(sub_mat, dense_MOKP, dest);
		Highgui.imwrite("CD/dense.jpg", dest);
		
		Features2d.drawKeypoints(sub_mat, rand_MOKP, dest);
		Highgui.imwrite("CD/rand.jpg", dest);
		
		img = sub_mat;
		
	}
	
	
	public List<Float> computeHOGAndGetFinalFeatureVector()
	{
		harris_hogs = findHogFromKeyPoints(harris_MOKP, img);
		mser_hogs = findHogFromKeyPoints(mser_MOKP, img);
		dense_hogs = findHogFromKeyPoints(dense_MOKP, img);
		rand_hogs = findHogFromKeyPoints(rand_MOKP, img);
		
		CharDescGMM cdg = new CharDescGMM();
		List<Float> dense_GMM = cdg.findGmmFeature(dense_hogs);
		List<Float> rand_GMM = cdg.findGmmFeature(rand_hogs);
		
		List<Float> finalGMM = new ArrayList<Float>();
		finalGMM.addAll(dense_GMM);
		finalGMM.addAll(rand_GMM);
		
		
		CharDescBOW cdb = new CharDescBOW();
		List<Float> finalBOW = cdb.getBOWFeature(harris_hogs , mser_hogs, dense_hogs, rand_hogs);
		
		List<Float> finalFeature = finalBOW;
		finalFeature.addAll(finalGMM);
		
		return finalFeature;
		
	}
	
	
	
	private List<MatOfFloat> findHogFromKeyPoints( MatOfKeyPoint mokp , Mat image)
	{
		Size winSize = new Size(64, 128);
		Size blockSize = new Size(16,16);
		Size blockStride = new Size(16,16);
		Size cellSize = new Size(16,16);
		int nbins = 1;
		
		HOGDescriptor hog = new HOGDescriptor(winSize, blockSize, blockStride, cellSize, nbins);
		List<KeyPoint> lokp = mokp.toList();
		MatOfKeyPoint mkp;
		
		Mat temp_img;
		MatOfFloat descriptors;
		
		List<MatOfFloat> listOfMOF = new ArrayList<MatOfFloat>();
		
		for(int i=0; i<lokp.size(); i++)
		{
			mkp = new MatOfKeyPoint();
			mkp.fromList( lokp.subList(i, i+1) );
			
			temp_img = Mat.zeros(image.rows(), image.cols(), image.type());	

			Features2d.drawKeypoints(temp_img, mkp, temp_img, new Scalar(255,255,255), Features2d.DRAW_RICH_KEYPOINTS);
			Features2d.drawKeypoints(temp_img, mkp, temp_img, new Scalar(255,255,255), Features2d.DRAW_OVER_OUTIMG);
			
			temp_img = keepOnlyKeyPoints( temp_img , image );
			Highgui.imwrite("CDout/out.jpg", temp_img);	
			descriptors = new MatOfFloat();
			
			hog.compute( temp_img, descriptors, new Size(img.width()/2, img.height()/2), new Size(0,0), new MatOfPoint() );
			
			listOfMOF.add(descriptors);
		}
		
		return listOfMOF;
	}
	
	
	private Mat keepOnlyKeyPoints(Mat keyPointImg, Mat grayImg)
	{
		for(int row=0; row<keyPointImg.rows(); row++)
		{
			for(int col=0; col<keyPointImg.cols(); col++)
			{
				if(keyPointImg.get(row, col)[0] > 0)
				{
					keyPointImg.put(row, col, new byte[]{ (byte)grayImg.get(row, col)[0] , (byte)grayImg.get(row, col)[0] , (byte)grayImg.get(row, col)[0] });
				}
			}
		}
		
		return keyPointImg;
	}
	
	
	
	public List<MatOfFloat>  getHarrisHog()
	{
		return harris_hogs;
	}
	
	public List<MatOfFloat> getMSERHog()
	{
		return mser_hogs;
	}
	
	public List<MatOfFloat> getDenseHog()
	{
		return dense_hogs;
	}
	
	public List<MatOfFloat> getRandHog()
	{
		return rand_hogs;
	}
}
