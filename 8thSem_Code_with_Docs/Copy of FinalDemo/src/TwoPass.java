import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.opencv.core.CvType;
import org.opencv.core.Mat;


public class TwoPass {

	public int[][] labeledMatrix;
    int NextLabel = 1 ; // 0;
    ArrayList<Mat> CntdComp;
    
    public TwoPass(int[][] matrix)
    {
    	ArrayList<ArrayList<Integer>> linked = new ArrayList<ArrayList<Integer>>();
        int[][] labels = new int[matrix.length][matrix[0].length];
        
        ArrayList<Integer> ai = new ArrayList<Integer>();
        ai.add(0);
        linked.add(0, ai );

        for(int i=0; i<matrix.length; i++)
        {
            for(int j=0; j<matrix[0].length; j++)
            {
                if(matrix[i][j] == 255)//!= 0)
//REG_TEST                if(matrix[i][j] == 0)
                {
                    //Labels of neighbors
                    ArrayList<Integer> neighbors = new ArrayList<Integer>();
                    
                    for(int ni=-1; ni<=1; ni++)
                    {
                        for(int nj=-1; nj<=1; nj++)
                        {
                            if(i+ni < 0 || j+nj < 0 || i+ni > labels.length-1 || j+nj > labels[0].length-1)
                            {
                                continue;
                            }
                            else if(i+ni == 0 && i+nj == 0)
                            {
                            	continue;
                            }
                            else if(ni == 0 && nj == 0)
                            {
                            	continue;
                            }
                            else if(labels[i+ni][j+nj] != 0) 
                            {
                            	neighbors.add(labels[i+ni][j+nj]);
                            }
                        }
                    }

                    if(neighbors.size() == 0) 
                    {
                        ArrayList<Integer> tempArrayList = new ArrayList<Integer>();
                        tempArrayList.add(NextLabel);
                        linked.add(NextLabel, tempArrayList);
                        labels[i][j] = NextLabel;
                        NextLabel++;
                    }
                    else 
                    {
                        labels[i][j]=1000*1000;
                        for(int neighbor : neighbors) {
                            if(neighbor < labels[i][j]) labels[i][j] = neighbor;
                        }

                        for(int neighbor : neighbors) {
                            linked.set(neighbor,union(linked.get(neighbor),neighbors));
                        }
                    }
                }

            }
        }

        	//System.out.println("Equi Label => \n"+ linked );
        
        
        //Second pass
        for(int i=0; i<matrix.length; i++) {
            for(int j=0; j<matrix[0].length; j++) {
              
            
              if(matrix[i][j] == 255)
              {
                ArrayList<Integer> EquivalentLabels = linked.get(labels[i][j]);
                labels[i][j]=1000*1000;
                for(int label : EquivalentLabels) {
                    if(label < labels[i][j]) labels[i][j]=label;
                } 
              }
              
            	
//Works            	
//            /*	
            	if(matrix[i][j] == 255) // != 0) // >50)
            	{
            		int minLab = findMinLabel(labels[i][j] , linked);
                	labels[i][j] = minLab;
            	}
//            */	
            	
            }
        }

        labeledMatrix = labels;
        
        
        
        //initialize CntdComp
        ArrayList<Mat> CC = new ArrayList<Mat>(NextLabel);
        for(int i=0; i<NextLabel; i++)
        {
        	CC.add(new Mat(0, 2, CvType.CV_32F));
        }
       
        CntdComp = CC;

    }


	private int findMinLabel(int label, ArrayList<ArrayList<Integer>> EquiLab)
	{
		
		int l = Collections.min( EquiLab.get(label) );
		if( l == label )
		{
			return l;
		}
		
		return findMinLabel( l , EquiLab );
	}
    
    
    //union: http://stackoverflow.com/questions/5283047/intersection-and-union-of-arraylists-in-java
    public <T> ArrayList<T> union(ArrayList<T> list1, ArrayList<T> list2)
    {
        Set<T> set = new HashSet<T>();

        set.addAll(list1);
        set.addAll(list2);

        return new ArrayList<T>(set);
    }
    
    
    public int[][] getLabeledMatrix()
    {
        return labeledMatrix;
    }
    
    public int findLabel()
    {
    	return NextLabel;
    }
    
    
    public ArrayList<Mat> getConnectedComponent()
    {
    	int labelVal=0;
        Mat m = new Mat(1,2,CvType.CV_32F);
                
        for(int row=1; row<labeledMatrix.length-1; row++) //row -> y value
        {
        	for(int col=1; col<labeledMatrix[0].length-1; col++) //col -> x value
        	{
        		if( (labelVal = labeledMatrix[row][col]) > 0)
        		{
        			m.put(0, 0, col);
        			m.put(0, 1, row);
        			CntdComp.get(labelVal).push_back(m);
        		}
        	}
        }
    	return CntdComp;
    }
    
    
}
