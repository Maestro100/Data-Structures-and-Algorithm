import java.util.*;
import java.io.*;
public class Anagram {

    private static boolean contained_in(String s_1, String s2){
        //s2 contained in s1
        int count=0;
        String s1 = s_1;
        for(int i=0;i<s2.length();i++){
            char t2 = s2.charAt(i);
            for(int j=0;j<s1.length();j++){
                char t1 = s1.charAt(j);
                if(t1==t2){
                    String s3 = s1.substring(0,j);
                    String s4 =  s1.substring(j+1,s1.length());
                    s1 = s3+s4;
                    count++;
                    break;
                }
            }
        }
        return count==s2.length();
    }

    private static String update(String s1, String s2){
        //s2 contained in s1
        String s3,s4 = null;
        char t1,t2;
        for(int i=0;i<s2.length();i++){
            t2 = s2.charAt(i);
            for(int j=0;j<s1.length();j++){
                t1 = s1.charAt(j);
                if(t1==t2){
                    s3 = s1.substring(0,j);
                    s4 =  s1.substring(j+1,s1.length());
                    s1 = s3+s4;
                    break;
                }
            }
        }
        return s1;
    }
    public static void main(String[] args){
        //final long startTime = System.currentTimeMillis();
        //long ac=0;
        Scanner sc = null;
        try {
            String w = args[0];
            sc = new Scanner(new File(w));
        } catch (FileNotFoundException e) {
            System.out.println("no file");
        }
        int y=0;
        int num = sc.nextInt();
        ArrayList<String> voc = new ArrayList<String>(40000);
        for(int i=0;i<num;i++){
            String inp = sc.next();
            if(inp.length()<=12) {
                voc.add(y, inp);y++;
            }
        }
        //Collections.sort(voc);
        int id=0;
        int sz = voc.size();
        int first=0,second=0,third=0;
        Scanner ip = null;
        try {
            String v = args[1];
            ip = new Scanner(new File(v));
        } catch (FileNotFoundException e) {
            System.out.println("no file");
        }
        num = ip.nextInt();
        for(int c=0;c<num;c++){
            ArrayList<String> output = new ArrayList<String>(1000000);
            String inp_1 = ip.next();
            for (int i = 0; i < sz; i++) {
                if(contained_in(inp_1,voc.get(i))) {
                    first = i;
                    if (voc.get(first).length() == inp_1.length()) {
                        output.add(id,voc.get(first));id++;
                    }
                    else {
                        String inp_2 = update(inp_1,voc.get(first));
                        for (int j = i+1; j < sz; j++){
                            if(contained_in(inp_2,voc.get(j))){
                                second = j;
                                if(voc.get(second).length()+voc.get(first).length()==inp_1.length()){
                                    String out = voc.get(first)+" "+voc.get(second);
                                    output.add(id,out);id++;
                                    out = voc.get(second)+" "+voc.get(first);
                                    output.add(id,out);id++;
                                }
                                else {
                                    String inp_3 = update(inp_2,voc.get(j));
                                    for(int k=j+1;k<sz;k++){
                                        if(contained_in(inp_3,voc.get(k))&&inp_3.length()==voc.get(k).length()){
                                            third = k;
                                            String out = voc.get(first)+" "+voc.get(second)+" "+voc.get(third);
                                            output.add(id,out);id++;
                                            out = voc.get(first)+" "+voc.get(third)+" "+voc.get(second);
                                            output.add(id,out);id++;
                                            out = voc.get(second)+" "+voc.get(first)+" "+voc.get(third);
                                            output.add(id,out);id++;
                                            out = voc.get(second)+" "+voc.get(third)+" "+voc.get(first);
                                            output.add(id,out);id++;
                                            out = voc.get(third)+" "+voc.get(first)+" "+voc.get(second);
                                            output.add(id,out);id++;
                                            out = voc.get(third)+" "+voc.get(second)+" "+voc.get(first);
                                            output.add(id,out);id++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Collections.sort(output);
            for(int p=0;p<output.size();p++){
                System.out.println(output.get(p));
            }
            System.out.println("-1");
            //ac+=output.size();
            first=0;second=0;third=0;id=0;
        }
        //System.out.println(ac);
    }
}