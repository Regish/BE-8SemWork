import java.util.ArrayList; 

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;


public class ObservationPoints {

	private ArrayList<double[]> al;
	private double d;
	private double diff;
	private int g,h;
	
	//coonstructor sets default values
	public ObservationPoints()
	{
		al = new ArrayList<double[]>();
		diff = 0;
		d = 0;
		g = 0;
		h = 0;
	}
	
	
	//To find color pairs
	public ArrayList<double[]> getMaxColorDiff(int i, int j, Mat src)
	{
		ArrayList<double[]> res = new ArrayList<double[]>();
		
		try
		{
			
				if(i==0 || j==0 || i==src.rows()-1 || j==src.cols()-1)
				{
					//neglect edge pixels at image boundary
					res=null;
				}
				else
				{
					//considering 8 neighbor pixels
					al.add(src.get(i-1, j-1));
					al.add(src.get(i-1, j));
					al.add(src.get(i-1, j+1));
					al.add(src.get(i, j-1));
					al.add(src.get(i, j+1));
					al.add(src.get(i+1, j-1));
					al.add(src.get(i+1, j));
					al.add(src.get(i+1, j+1));
					
					
					for(int k=0; k< al.size()-1 ; k++)
					{
						for(int q=k+1; q< al.size() ; q++)
						{
							//calculate color difference
							d = Math.sqrt( Math.pow( Math.abs(al.get(k)[0] - al.get(q)[0]) ,2 ) 
											+ Math.pow( Math.abs(al.get(k)[1] - al.get(q)[1]) ,2 ) 
											+ Math.pow( Math.abs(al.get(k)[2] - al.get(q)[2]) ,2 ) );
							
							if(d > diff)
							{
								diff = d;
								g = k;
								h = q;		
							}
						}
					}
					
					
					//To find color intensity
					// ( (0.3 * R) + (0.59 * G) + (0.11 * B) )
					if( (0.11*al.get(g)[0] + 0.59*al.get(g)[1] + 0.3*al.get(g)[2]) < (0.11*al.get(h)[0] + 0.59*al.get(h)[1] + 0.3*al.get(h)[2]) )
					{
						res.add(al.get(g));
						res.add(al.get(h));
					}
					else
					{
						res.add(al.get(h));
						res.add(al.get(g));
					}
				
						
						
				}
		}
		catch(Exception e ){
			System.out.println(e);
		}

		//set default values to reuse the object instead of creating more new objects
		al.clear();
		diff=0;
		d=0;
		return res;
		
	}
	
	//Group the vectors into different clusters from k-means
	public Mat getCluster(Mat samples, Mat labels , double clusterNum)
	{
		Mat cluster = new Mat();
		
		for(int i=0; i < samples.rows(); i++)
		{
			if( labels.get(i , 0)[0] == clusterNum )
			{
				cluster.push_back(samples.row(i));
			} 	
		}
		
		return cluster;
	}
	
	
	//Group pixels into different layers after EM algorithm
	public Mat getLayer(Mat vectors, Mat emLabels, double layerNum )
	{
		Mat layer = new Mat(0, 12 , CvType.CV_32F);
		
		for(int i=0; i<vectors.rows() ; i++)
		{
			if(emLabels.get(i, 0)[0] == layerNum)
			{
				layer.push_back(vectors.row(i));
			}
		}
		
		return layer;
	}
	
	
	//write each Gaussian Distribution into different layer images
	public void createLayerImg(int rows, int cols, Mat layer, int layerNum)
	{
		Mat result = new Mat(rows , cols, CvType.CV_32F);
		
		
		for(int i=0; i<layer.rows(); i++)
		{
			//x axis => cols     y axis => rows
			result.put((int)layer.get(i, 9)[0], (int)layer.get(i, 6)[0], 255.0);
			
					
		}
		
		Highgui.imwrite("Layers/layer"+layerNum+".jpg", result);
		
	}
}
