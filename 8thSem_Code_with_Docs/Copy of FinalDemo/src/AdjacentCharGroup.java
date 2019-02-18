import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;


public class AdjacentCharGroup 
{

	List<List<Double>> cc = new ArrayList<List<Double>>() , tempcc;
	double T1 ,T2 ,T3 ,T4, T5 ,Ts;
	List<List<Set<Integer>>> compSiblings = new ArrayList<List<Set<Integer>>>();
	List<Set<Integer>> SiblingGroup = new ArrayList<Set<Integer>>() , tempSibGrp;
	
	List<MatOfPoint> componentPoints;
	String filename , colorImgName;
	
	
	public AdjacentCharGroup(ArrayList<MatOfPoint> conComList , String fname ,String C_I_Name)
	{
		componentPoints = conComList;
		filename = fname;
		colorImgName = C_I_Name;
		
/*		T1=T4=1.25;//1.25 //2;
		T2=1.1;//1.1; //3;
		T3=0.2;//0.2; //0.5;
		T5=100;
		Ts=20;//40; //20;	*/
		
//Test
		T1=T4=2;
		T2=3;
		T3=0.5;
		T5=100;//100;
		Ts=20;//40;
		
		MatOfPoint temp;
		Rect rect;
		List<Double> l;
		double area;
		double height;
		double width;
		double cx;
		double cy;
		
		for(int i=0; i<conComList.size(); i++)
		{
			temp = conComList.get(i);
			area = Imgproc.contourArea(temp);
			rect = Imgproc.boundingRect(temp);
			height = rect.height;
			width = rect.width;
			cx = rect.x + width/2 ;
			cy = rect.y + height/2 ;
			
			l = new ArrayList<Double>();
			l.add(area);
			l.add(height);
			l.add(width);
			l.add(cx);
			l.add(cy);
			
			cc.add(l);
		}

	}
	
	
	@SuppressWarnings("unused")
	public List<Rect> getCharGroups()
	{
		
		List<Rect> finalListOfRect = new ArrayList<Rect>();
		
		//remove components with small area
		tempcc = new ArrayList<List<Double>>(cc);
		for(int i=0; i<tempcc.size() ; i++)
		{
			if(tempcc.get(i).get(0) < Ts)
			{
				tempcc.remove(i);
			}
		}
		
		//Initialize sibling sets
		List<Set<Integer>> list = new ArrayList<Set<Integer>>(2);
		for(int i=0; i < tempcc.size(); i++)
		{
			list.add(0, new HashSet<Integer>());
			list.add(1, new HashSet<Integer>());
			
			compSiblings.add(i, list);
			
		}
		
		List<Double> c1 , c2;
		List<Set<Integer>> cs1 ,cs2;
		
		for(int i=0; i< compSiblings.size()-1 ; i++)
		{
			for(int j=i+1; j<compSiblings.size(); j++)
			{
				c1 = tempcc.get(i);
				c2 = tempcc.get(j);
				
				if( (1/T1 < c1.get(1) / c2.get(1)) && (c1.get(1) / c2.get(1) < T1 ) 
					&& ( d(c1.get(3) , c2.get(3)) <= T2 * Math.max(c1.get(2), c2.get(2) ) )
				    && ( d(c1.get(4) , c2.get(4)) <= T3 * Math.max(c1.get(1), c2.get(1) ) )
					&& ( ( 1/T4 < c1.get(0)/c2.get(1) ) && ( c1.get(0)/c2.get(1) < T4 ) )
					&& ( meanRgbDiff( i,j,tempcc,cc,componentPoints, colorImgName)  < T5 )
				  )
				{
					cs1 = compSiblings.get(i);
					cs2 = compSiblings.get(j);
					if(c1.get(3) <= c2.get(3))
					{
						cs1.get(1).add( tempcc.indexOf(c2) );
						cs2.get(0).add( tempcc.indexOf(c1) );
					}
					else
					{
						cs1.get(0).add( tempcc.indexOf(c2) );
						cs2.get(1).add( tempcc.indexOf(c1) );
					}
				}
			}
		}
		
		
		//Sibling Grouping 
		List<Set<Integer>> eachComp;
		Set<Integer> eachGroup;
		for(int i=0; i<compSiblings.size(); i++)
		{
			eachComp = compSiblings.get(i);
			
			if( (eachComp.get(0).size() != 0) 
				&& ( eachComp.get(1).size() != 0 ) 
				&& ( Math.abs( eachComp.get(0).size() - eachComp.get(1).size() ) <= 3 )
				
				//|| ( Math.abs( eachComp.get(0).size() - eachComp.get(1).size() ) <= 3 )
			  )
			{
				eachGroup = new HashSet<Integer>();
				eachGroup.addAll(eachComp.get(0));
				eachGroup.addAll(eachComp.get(1));
				eachGroup.add(i);
				SiblingGroup.add(eachGroup);//.add(i, eachGroup);
			}
		}
		
		//merge sibling groups
		Set<Integer> sibSet1 , sibSet2 , tempSet;
		for(int i=0; i<SiblingGroup.size()-1 ; i++)
		{
			sibSet1 = SiblingGroup.get(i);
			tempSet = new HashSet<Integer>();
			tempSet.addAll(sibSet1);
			for(int j=0; j<SiblingGroup.size() ; j++)
			{
				sibSet2 = SiblingGroup.get(j);
				tempSet.retainAll(sibSet2);
				if( tempSet.size() >= 2 )//1 ) //2 )
// Test				
//				if( tempSet.size() >= 1)
				{
					sibSet1.addAll(sibSet2);
					SiblingGroup.get(j).clear();
					i=0;
					break;
				}
			}
		}

		
		Set<Integer> myset;
		List<Point> Lmop, totalPoints = new ArrayList<Point>();
		MatOfPoint sibGrpMop;
		List<MatOfPoint> listOfMOPForEachSet = new ArrayList<MatOfPoint>();
		
		tempSibGrp = new ArrayList<Set<Integer>>(SiblingGroup);
		
		for(int i=0; i<tempSibGrp.size(); i++)
		{
			totalPoints.clear();
			
			if( ! tempSibGrp.get(i).isEmpty() )
			{
				myset = tempSibGrp.get(i);
				int k=0;
				for(int v : myset)
				{
					k = cc.indexOf(tempcc.get(v));
					Lmop = componentPoints.get(k).toList();
					totalPoints.addAll(Lmop);
				}
				sibGrpMop = new MatOfPoint();
				sibGrpMop.fromList(totalPoints);
				
				listOfMOPForEachSet.add(i, sibGrpMop);
				
			}
			else
			{
				tempSibGrp.remove(i);
				i--;
			}
		}
		
		//filter adj groups
		MatOfPoint mopBuffer1, mopBuffer2;
		Rect rec1, rec2;

/*		
		//removing subgroups
		for(int i=0; i<tempSibGrp.size()-1; i++)
		{
			mopBuffer1 = listOfMOPForEachSet.get(i);
			rec1 = Imgproc.boundingRect(mopBuffer1);
			for(int j=i+1; j<tempSibGrp.size(); j++)
			{
				mopBuffer2 = listOfMOPForEachSet.get(j);
				rec2 = Imgproc.boundingRect(mopBuffer2);
								
				if((rec2.x >= rec1.x) && (rec2.y >= rec1.y))
				{
					if( (rec2.height >= rec1.height) && (rec2.width >= rec1.width) )
					{
						if( (rec2.width * rec2.height) >= (rec1.width * rec1.height) )
						{
							listOfMOPForEachSet.remove(i);
							tempSibGrp.remove(i);
							i=0;
							break;
						}
						else
						{
							listOfMOPForEachSet.remove(j);
							tempSibGrp.remove(j);
							i=0;
							break;
						}						
					}
					else
					{
						listOfMOPForEachSet.remove(j);
						tempSibGrp.remove(j);
						i=0;
						break;
					}
				}
				else if( (rec1.x >= rec2.x) && (rec1.y >= rec2.y) )
				{
					if( (rec1.height >= rec2.height) && (rec1.width >= rec2.width) )
					{
						if( (rec1.width * rec1.height) >= (rec2.width * rec2.height) )
						{
							listOfMOPForEachSet.remove(j);
							tempSibGrp.remove(j);
							i=0;
							break;
						}
						else
						{
							listOfMOPForEachSet.remove(i);
							tempSibGrp.remove(i);
							i=0;
							break;
						}
					}
					else
					{
						listOfMOPForEachSet.remove(i);
						tempSibGrp.remove(i);
						i=0;
						break;
					}
				}
				else if( (rec2.x >= rec1.x) && (rec2.y <= rec1.y) )
				{
					if( ((rec2.x+rec2.width) <= (rec1.x+rec1.width)) && ((rec2.y+rec2.height) <= (rec1.y+rec1.height)) )
					{
						listOfMOPForEachSet.remove(j);
						tempSibGrp.remove(j);
						i=0;
						break;
					}
					else if( ((rec2.x+rec2.width) >= (rec1.x+rec1.width)) && ((rec2.y+rec2.height) >= (rec1.y+rec1.height)) )
					{
						listOfMOPForEachSet.remove(i);
						tempSibGrp.remove(i);
						i=0;
						break;
					}
					else
					{
						if( (rec2.width*rec2.height) >= (rec1.width*rec1.height) )
						{
							listOfMOPForEachSet.remove(i);
							tempSibGrp.remove(i);
							i=0;
							break;
						}
						else
						{
							listOfMOPForEachSet.remove(j);
							tempSibGrp.remove(j);
							i=0;
							break;
						}
					}
				}
				else if( (rec1.x >= rec2.x) && (rec1.y <= rec2.y) )
				{
					if( ((rec1.x+rec1.width) <= (rec2.x+rec2.width)) && ((rec1.y+rec1.height) <= (rec2.y+rec2.height)) )
					{
						listOfMOPForEachSet.remove(i);
						tempSibGrp.remove(i);
						i=0;
						break;
					}
					else if( ((rec1.x+rec1.width) >= (rec2.x+rec2.width)) && ((rec1.y+rec1.height) >= (rec2.y+rec2.height)) )
					{
						listOfMOPForEachSet.remove(j);
						tempSibGrp.remove(j);
						i=0;
						break;
					}
					else
					{
						if( (rec1.width*rec1.height) >= (rec2.width*rec2.height) )
						{
							listOfMOPForEachSet.remove(j);
							tempSibGrp.remove(j);
							i=0;
							break;
						}
						else
						{
							listOfMOPForEachSet.remove(i);
							tempSibGrp.remove(i);
							i=0;
							break;
						}
					}
				}
			  
			}
		}

*/	
		
		//filter height
		Rect rec3, rec4;
		Set<Integer> setOfIndex;
		int z, max_height=0;
		MatOfPoint mop_bm;
		for(int i=0; i<tempSibGrp.size(); i++)
		{
			setOfIndex = tempSibGrp.get(i);
			
			for(int v : setOfIndex)
			{
				z = cc.indexOf(tempcc.get(v));
				mop_bm = componentPoints.get(z);
				rec3 = Imgproc.boundingRect(mop_bm);
				if(rec3.height > max_height)
				{
					max_height = rec3.height;
				}
			}
			
			rec4 = Imgproc.boundingRect( listOfMOPForEachSet.get(i) );
			
			if(!( ((1/T1) < (rec4.height/max_height)) && ((rec4.height/max_height) < (T1)) ))
			{
				tempSibGrp.remove(i);
				listOfMOPForEachSet.remove(i);
			}
		}


	
		
		//final groups
		Mat image = Highgui.imread(filename , Highgui.CV_LOAD_IMAGE_GRAYSCALE);
		Mat tempImg;
		
		int whySoSerious = filename.lastIndexOf( "/" );
		String tempstr = filename.substring( whySoSerious + 1 , filename.length() - 1 );
		
		for(int i=0; i<tempSibGrp.size() ; i++)
		{
			Rect rect_bm = new Rect();
			rect_bm = Imgproc.boundingRect(listOfMOPForEachSet.get(i));
			finalListOfRect.add(rect_bm);
			tempImg = image.clone();
			Core.rectangle(tempImg, new Point(rect_bm.x , rect_bm.y ), new Point(rect_bm.x+rect_bm.width , rect_bm.y+rect_bm.height), new Scalar(255,255,255) );
			
			Highgui.imwrite("ACG/"+tempstr+"AdjCharGrp"+i+".jpg", tempImg);

		}
		
		return finalListOfRect;
	}
	
	
	
	

	public List<Set<Integer>> getSiblingGroup()
	{
		return tempSibGrp;
	}
	
	
	public List<List<Double>> getCcProperties()
	{
		return tempcc;
	}
	
	
	
	private double  d(double a, double b)
	{
		return Math.abs(a-b);
	}
	
	
	
	private double meanRgbDiff(int i, int j, List<List<Double>> setAfterRemoval, List<List<Double>> setBeforeRemoval, List<MatOfPoint> Comp_Points, String colImName )
	{
		double mean_I[] = new double[3] ;
		double mean_J[] = new double[3] ;
		double[] rgb;
		Mat img = Highgui.imread(colImName, Highgui.CV_LOAD_IMAGE_COLOR);
		int m = setBeforeRemoval.indexOf(setAfterRemoval.get(i));
		int n = setBeforeRemoval.indexOf(setAfterRemoval.get(j));
		
		List<Point> a = Comp_Points.get(m).toList();
		List<Point> b = Comp_Points.get(n).toList();
		
		
		int col, row;
		for(int z=0; z<a.size() ; z++)
		{
			col = (int)a.get(z).x;
			row = (int)a.get(z).y;
			
			rgb = img.get(row, col);
						
			mean_I[0] += rgb[0];
			mean_I[1] += rgb[1];
			mean_I[2] += rgb[2];
			
		}
		mean_I[0] /= a.size();
		mean_I[1] /= a.size();
		mean_I[2] /= a.size();
		
		
		for(int z=0; z<b.size(); z++)
		{
			col = (int)b.get(z).x;
			row = (int)b.get(z).y;
			
			rgb = img.get(row, col);
			
			mean_J[0] += rgb[0];
			mean_J[1] += rgb[1];
			mean_J[2] += rgb[2];
			
		}
		mean_J[0] /= a.size();
		mean_J[1] /= a.size();
		mean_J[2] /= a.size();
		
		double val = Math.sqrt( Math.pow( ( mean_I[0] - mean_J[0] ) , 2 ) + Math.pow( ( mean_I[1] - mean_J[1] ) , 2 ) + Math.pow( ( mean_I[2] - mean_J[2] ) , 2 ) );
		
		return val;
	}
}
