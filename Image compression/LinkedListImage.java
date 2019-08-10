import java.util.*;
import java.io.*;
public class LinkedListImage implements CompressedImageInterface {
    public class node{
        public int index_start;
        public int index_end;
        public node next;
        public node(int s, int e, node n){
            index_start = s;
            index_end = e;
            next = n;
        }
    }
    public int row,column;
    //public node head;
    public node xr[] = new node[row +1];
    public LinkedListImage(String filename)
    { 
        node head =null;
        try{ 
        File file = new File(filename);
        Scanner s = new Scanner(file);
         row = s.nextInt();
         node ayush[] = new node[row+1];
        // xr = new node[row];
         //System.out.println(row);
         column = s.nextInt();
        int count =0;
        int left =0;
        int temp=1;
        int j;
        for(int i=0;i<row;i++){
            for(j=0;j<column;j++){
                temp = s.nextInt();
                if(temp==0){
                    count++;
                }
                else if(temp==1){
                    if(count!=0){
                        node newest = new node(j-count,j-1,null);
                        if(left==0){
                            head = newest;
                            ayush[i] = head;
                            left=1;
                        }
                        else{
                            head.next = newest;
                            head = newest;                     
                        }
                        count=0;
                    }
                }
                
            }
            if(temp==0&&j==column&& left==0){
                    node newest = new node(j-count,j-1,null);
                    head = newest;
                    ayush[i] = head;
                    count=0;
                }
            else if(temp==0&&j==column&& left!=0){
                    node p = new node(j-count,j-1,null);
                    head.next = p;
                    head = p;
                    count=0;
                }
            left=0;
        }
        xr = ayush;
    }
    catch (FileNotFoundException e){
        System.out.println("NO FILE");
        //you need to implement this
    }
    //  throw new java.lang.UnsupportedOperationException("Not implemented yet.");
    }

    public LinkedListImage(boolean[][] grid, int width, int height)
    {
        node head = null;
          column = width;
          row = height;
         node ayush[] = new node[row+1];
        int count =0;
        int left =0;
        int j;
        int c=0;

        //boolean tempr = true;
        boolean temp=false;
        for(int i=0;i<row;i++){
            for(j=0;j<column;j++){
                temp = grid[i][j];
                
                
                if(temp== false){
                    count++;
                 //   System.out.print(count+"t");
                }
                else if(temp== true){

                    if(count!=0){
                        node newest = new node(j-count,j-1,null);
                        if(left==0){
                            head = newest;
                            ayush[i] = head;
                            left=1;
                        }
                        else{
                            head.next = newest;
                            head = newest;                     
                        }
                        count=0;
                    }
                }
                
            }
            if(temp==false&&j==column&& left==0){
                 node newest = new node(j-count,j-1,null);
                    head = newest;
                    ayush[i] = head;
                    count=0;
                }
            else if(temp== false && j==column&& left!=0){
                    node p = new node(j-count,j-1,null);
                    head.next = p;
                    head = p;
                    count=0;
                }
               // System.out.print("linechange");
            left=0;
        }
        xr = ayush;

        //you need to implement this
        //throw new java.lang.UnsupportedOperationException("Not implemented yet.");
    }

    public boolean getPixelValue(int x, int y) throws PixelOutOfBoundException
    {
        if(x>row-1||y>column-1) throw new PixelOutOfBoundException("trt");
        if(xr[x]==null) return true;
        else{
        node p = xr[x];
        int c=1;
        
            while(p!= null){
                if(p.index_start<=y && y<=p.index_end){
                    c=0;
                    break;
                }
                p = p.next;
           }
        if(c==0) return false;
        else return true;
    }
        //you need to implement this
        //throw new java.lang.UnsupportedOperationException("Not implemented yet.");
    }

    public void setPixelValue(int x, int y, boolean val) throws PixelOutOfBoundException
    {
        if(x>row-1||y>column-1) throw new PixelOutOfBoundException("trt");
     
    try{
    
    if(getPixelValue(x,y)==val)return;
        else{
                  // System.out.println("daan");
            if(val==false){
                if(xr[x]==null){
                   // System.out.println("daan");
                    node newest = new node(y,y,null);
                    xr[x]=newest;
                    return; 
                    }
                else{
                    node p = xr[x];
                    if(y==0){
                        if(getPixelValue(x,y+1)==true){
                            node newest = new node(y,y,p);
                            xr[x] = newest;
                            return;
                        }
                        else{
                            p.index_start=0;
                            return;
                        }
                    }
                    else if(y==column-1){
                        while(p.next!=null)p=p.next;
                         if(getPixelValue(x,y-1)==true){
                            node newest = new node(y,y,null);
                            p.next = newest;
                            return;
                        }
                        else{
                            p.index_end=column-1;
                            return;
                        }
                    }
                    else{
                        node head = null;
                        while(p!=null){
                            if(y<p.index_start) break;
                            head =p;
                            p=p.next;}
                        if(getPixelValue(x,y-1)==true&&getPixelValue(x,y+1)==true){
                            if(head!=null){
                            node newest = new node(y,y,p);
                            head.next = newest;
                            return;
                        }
                            else{
                                node newest = new node(y,y,p);
                                xr[x] = newest;
                                return;
                            }
                        }
                        else if(getPixelValue(x,y-1)==true&&getPixelValue(x,y+1)==false){
                            
                            p.index_start=y;
                            return;
                        }
                        else if(getPixelValue(x,y-1)==false&&getPixelValue(x,y+1)==true){
                            head.index_end=y;
                            return;
                        }
                        else if(getPixelValue(x,y-1)==false&&getPixelValue(x,y+1)==false){
                            head.next = p.next;
                            head.index_end = p.index_end;
                            return;
                        }
                    }
                }
        }
        else{
            node head = null;
            node p =xr[x];
            while(p!=null){
                if(y>=p.index_start&&y<=p.index_end)break;
                head = p;
                p=p.next;
            }
            if(y==p.index_start){
                if(p.index_start==p.index_end){
                    if(head==null){
                        p=p.next;
                        xr[x] = p;
                        return;
                    }
                    else{
                        head.next = p.next;
                        return;
                    }
                }
                else{
                    p.index_start=y+1;
                    return;
                }
            }
            else if(y==p.index_end){
                if(p.index_start!=p.index_end){
                p.index_end = y-1;
                return;}
                else{
                    return;
                }
            }
            else{
                node newest = new node(y+1,p.index_end,p.next);
                p.next = newest;
                p.index_end = y-1;
                return;
            }
        }
    }
}
    catch(PixelOutOfBoundException e){
        System.out.println("Error");
    }
        //if(getPixelValue(x,y)!=val){

        //}
        //you need to implement this
        //throw new java.lang.UnsupportedOperationException("Not implemented yet.");
    }
    public int[] numberOfBlackPixels()
    {
        int q=0;
        int[] output = new int[row];
        for(int i=0;i<row;i++){
            if(xr[i]!=null){
                node p = xr[i];
                while(p!=null){
                    q=q+ p.index_end- p.index_start+1;
                p=p.next;}
                
            }
            output[i] = q;
            q=0;
        }
        return output;
        //you need to implement this
        //throw new java.lang.UnsupportedOperationException("Not implemented yet.");
    }
    
    public void invert()
    {
        try{
        for(int i=0;i<row;i++){
            for(int j=0;j<column;j++){
                if(getPixelValue(i,j)==true) setPixelValue(i,j,false);
                else setPixelValue(i,j,true);
            }
        }
    }
    catch(PixelOutOfBoundException e){
        System.out.println("Error");
    }
       
    }
    
    public void performAnd(CompressedImageInterface img)
    {
        try{
        LinkedListImage k = (LinkedListImage)img;
        for(int i=0;i<row;i++){
            for(int j=0;j<column;j++){
                if(k.getPixelValue(i,j)==false && getPixelValue(i,j)==true) setPixelValue(i,j,false);
                else if(k.getPixelValue(i,j)==false && getPixelValue(i,j)==false) setPixelValue(i,j,false);
                else if(k.getPixelValue(i,j)==true && getPixelValue(i,j)==true) setPixelValue(i,j,true);
                else if(k.getPixelValue(i,j)==true && getPixelValue(i,j)==false) setPixelValue(i,j,false);
            }
        }
    }
    catch(PixelOutOfBoundException e){
        System.out.println("error");
    }


        //you need to implement this
        //throw new java.lang.UnsupportedOperationException("Not implemented yet.");
    }
    
    public void performOr(CompressedImageInterface img)
    {
        try{
        LinkedListImage k = (LinkedListImage)img;
        for(int i=0;i<row;i++){
            for(int j=0;j<column;j++){
                if(k.getPixelValue(i,j)==true && getPixelValue(i,j)==false) setPixelValue(i,j,true);
                else if(k.getPixelValue(i,j)==true && getPixelValue(i,j)==true) setPixelValue(i,j,true);
                else if(k.getPixelValue(i,j)==false && getPixelValue(i,j)==false) setPixelValue(i,j,false);
                else if(k.getPixelValue(i,j)==false && getPixelValue(i,j)==true) setPixelValue(i,j,true);
            }
        }  
        }
        catch(PixelOutOfBoundException e){
        System.out.println("error");
    }      
        //you need to implement this
    //  throw new java.lang.UnsupportedOperationException("Not implemented yet.");
    }
    
    public void performXor(CompressedImageInterface img)
    {
        try
        {
        LinkedListImage k = (LinkedListImage)img;
        for(int i=0;i<row;i++){
            for(int j=0;j<column;j++){
                if(k.getPixelValue(i,j)==true){
                if(getPixelValue(i,j)==true) setPixelValue(i,j,false);
                else setPixelValue(i,j,true);   
                                              }
                else{
                if(getPixelValue(i,j)==true) setPixelValue(i,j,true);
                else setPixelValue(i,j,false);   
                    }
                                     }
                              }
        }
    catch(PixelOutOfBoundException e){
        System.out.println("ERRor ");
    }
        //you need to implement this
    //  throw new java.lang.UnsupportedOperationException("Not implemented yet.");
    }
    
    public String toStringUnCompressed()
    {
        String s = "";
        try
        {
        
        s+=row+" "+column;
        if(row!=0){
         s+=",";   
        }
       
        for(int i=0;i<row;i++){
            for(int j=0;j<column;j++){
                if(getPixelValue(i,j)==true)s+=" 1";
                else s+=" 0";
            }
            if(i<row-1)s+=",";
            
        }
       
    }
    catch(PixelOutOfBoundException e){
  System.out.println("error");
    }
     return s;    
        //you need to implement this
        //throw new java.lang.UnsupportedOperationException("Not implemented yet.");
    }
    
    public String toStringCompressed()
    {
        String sb ="";
        sb+=row+" "+column;
        if(row!=0){
         sb+=",";   
        }
        for(int i=0;i<row;i++){
            if(xr[i]==null && i<row-1) sb+=" -1,";
            else if(xr[i]==null && i==row-1) sb+=" -1";
            else{
                node p = xr[i];
                while(p!=null){
                    sb+=" ";
                    sb+=p.index_start;
                    sb+=" ";
                    sb+=p.index_end;
                    p= p.next;
                }
                if(i<row-1)
                sb+=" -1,";
                if(i==row-1)
                    sb+=" -1";
            }
        }
        //System.out.println(sb);
        return sb;
        //you need to implement this
        //throw new java.lang.UnsupportedOperationException("Not implemented yet.");
    }
    
    public static void main(String[] args) {
    	// testing all methods here :
    	boolean success = true;

    	// check constructor from file
    	CompressedImageInterface img1 = new LinkedListImage("sampleInputFile.txt");

    	// check toStringCompressed
    	String img1_compressed = img1.toStringCompressed();
    	String img_ans = "16 16, -1, 5 7 -1, 3 7 -1, 2 7 -1, 2 2 6 7 -1, 6 7 -1, 6 7 -1, 4 6 -1, 2 4 -1, 2 3 14 15 -1, 2 2 13 15 -1, 11 13 -1, 11 12 -1, 10 11 -1, 9 10 -1, 7 9 -1";
    	success = success && (img_ans.equals(img1_compressed));

    	if (!success)
    	{
    		System.out.println("Constructor (file) or toStringCompressed ERROR");
    		return;
    	}

    	// check getPixelValue
    	boolean[][] grid = new boolean[16][16];
    	for (int i = 0; i < 16; i++){
    		for (int j = 0; j < 16; j++)
    		{
                try
                {
        			grid[i][j] = img1.getPixelValue(i, j);                
                }
                catch (PixelOutOfBoundException e)
                {
                    System.out.println("Errorrrrrrrr");
                }
    		}
        }
    	// check constructor from grid
    	CompressedImageInterface img2 = new LinkedListImage(grid, 16, 16);
    	String img2_compressed = img2.toStringCompressed();
    	success = success && (img2_compressed.equals(img_ans));

    	if (!success)
    	{
    		System.out.println("Constructor (array) or toStringCompressed ERROR");
    		return;
    	}
        
        

    	// check Xor
        try
        {
        	img1.performXor(img2); 
            //System.out.println(img1.toStringCompressed());      
        }
        catch (BoundsMismatchException e)
        {
            System.out.println("Errorrrrrrrr");
        }
    	for (int i = 0; i < 16; i++)
    		for (int j = 0; j < 16; j++)
    		{
                try
                {
        			success = success && (!img1.getPixelValue(i,j));                
                }
                catch (PixelOutOfBoundException e)
                {
                    System.out.println("Errorrrrrrrr");
                }
    		}

    	if (!success)
    	{
    		System.out.println("performXor or getPixelValue ERROR");
    		return;
    	}

    	// check setPixelValue
    	for (int i = 0; i < 16; i++)
        {
            try
            {
    	    	img1.setPixelValue(i, 0, true);            
            }
            catch (PixelOutOfBoundException e)
            {
                System.out.println("Errorrrrrrrr");
            }
        }
        
    	// check numberOfblackPixels
    	int[] img1_black = img1.numberOfBlackPixels();
    	success = success && (img1_black.length == 16);
    	for (int i = 0; i < 16 && success; i++)
    		success = success && (img1_black[i] == 15);
    	if (!success)
    	{
    		System.out.println("setPixelValue or numberOfblackPixels ERROR");
    		return;
    	}

   	// check invert
        img1.invert();
        for (int i = 0; i < 16; i++)
        {
            try
            {
                success = success && !(img1.getPixelValue(i, 0));            
            }
            catch (PixelOutOfBoundException e)
            {
                System.out.println("Errorrrrrrrr");
            }
        }
        if (!success)
        {
            System.out.println("invert or getPixelValue ERROR");
            return;
        }

    	// check Or
        try
        {
            img1.performOr(img2);        
        }
        catch (BoundsMismatchException e)
        {
            System.out.println("Errorrrrrrrr");
        }
        for (int i = 0; i < 16; i++)
            for (int j = 0; j < 16; j++)
            {
                try
                {
                    success = success && img1.getPixelValue(i,j);
                }
                catch (PixelOutOfBoundException e)
                {
                    System.out.println("Errorrrrrrrr");
                }
            }
        if (!success)
        {
            System.out.println("performOr or getPixelValue ERROR");
            return;
        }

        // check And
        try
        {
            img1.performAnd(img2);    
        }
        catch (BoundsMismatchException e)
        {
            System.out.println("Errorrrrrrrr");
        }
        for (int i = 0; i < 16; i++)
            for (int j = 0; j < 16; j++)
            {
                try
                {
                    success = success && (img1.getPixelValue(i,j) == img2.getPixelValue(i,j));             
                }
                catch (PixelOutOfBoundException e)
                {
                    System.out.println("Errorrrrrrrr");
                }
            }
        if (!success)
        {
            System.out.println("performAnd or getPixelValue ERROR");
            return;
        }
       // System.out.println(img1.toStringUnCompressed());
    	// check toStringUnCompressed
        String img_ans_uncomp = "16 16, 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1, 1 1 1 1 1 0 0 0 1 1 1 1 1 1 1 1, 1 1 1 0 0 0 0 0 1 1 1 1 1 1 1 1, 1 1 0 0 0 0 0 0 1 1 1 1 1 1 1 1, 1 1 0 1 1 1 0 0 1 1 1 1 1 1 1 1, 1 1 1 1 1 1 0 0 1 1 1 1 1 1 1 1, 1 1 1 1 1 1 0 0 1 1 1 1 1 1 1 1, 1 1 1 1 0 0 0 1 1 1 1 1 1 1 1 1, 1 1 0 0 0 1 1 1 1 1 1 1 1 1 1 1, 1 1 0 0 1 1 1 1 1 1 1 1 1 1 0 0, 1 1 0 1 1 1 1 1 1 1 1 1 1 0 0 0, 1 1 1 1 1 1 1 1 1 1 1 0 0 0 1 1, 1 1 1 1 1 1 1 1 1 1 1 0 0 1 1 1, 1 1 1 1 1 1 1 1 1 1 0 0 1 1 1 1, 1 1 1 1 1 1 1 1 1 0 0 1 1 1 1 1, 1 1 1 1 1 1 1 0 0 0 1 1 1 1 1 1";
        success = success && (img1.toStringUnCompressed().equals(img_ans_uncomp)) && (img2.toStringUnCompressed().equals(img_ans_uncomp));

        if (!success)
        {
            System.out.println("toStringUnCompressed ERROR");
            return;
        }
        else
            System.out.println("ALL TESTS SUCCESSFUL! YAYY!");
    }
}