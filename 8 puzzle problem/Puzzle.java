import java.io.*;
import java.util.*;
public class Puzzle {
    public static class node{
        String s;
        long cost;
        String change;
        int d=0,u=0,l=0,r=0;
        long length;
        public node(String str){
            s =str;
        }
    }
    private static int get_index(String s){
        int ans=-1;
        for(int j=0;j<9;j++){
            String y = String.valueOf(s.charAt(j));
            if(y.equals("G")){
                ans = j;
                break;
            }
        }
        return ans;
    }
    private static node move_left(node n, int i, int[] value){
        String str = n.s;
        String r = Character.toString(n.s.charAt(i-1));
        str = str.replace("G", "*").replace(r, "G").replace("*", r);
        node temp =new node(str);
        temp.r=1;
        temp.change = n.change+r+"R"+" ";
        temp.length = n.length+1;
        temp.cost = n.cost+value[Character.getNumericValue(n.s.charAt(i-1))];
        return temp;
    }
    private static node move_right(node n, int i, int[] value){
        String str = n.s;
        String r = Character.toString(n.s.charAt(i+1));
        str = str.replace("G", "*").replace(r, "G").replace("*", r);
        node temp =new node(str);
        temp.l=1;
        temp.change = n.change+r+"L"+" ";
        temp.length = n.length+1;
        temp.cost = n.cost+value[Character.getNumericValue(n.s.charAt(i+1))];
        return temp;
    }
    private static node move_up(node n, int i, int[] value){
        String str = n.s;
        String r = Character.toString(n.s.charAt(i-3));
        str = str.replace("G", "*").replace(r, "G").replace("*", r);
        node temp =new node(str);
        temp.d=1;
        temp.change = n.change+r+"D"+" ";
        temp.length = n.length+1;
        temp.cost = n.cost+value[Character.getNumericValue(n.s.charAt(i-3))];
        return temp;
    }
    private static node move_down(node n, int i, int[] value){
        String str = n.s;
        String r = Character.toString(n.s.charAt(i+3));
        str = str.replace("G", "*").replace(r, "G").replace("*", r);
        node temp =new node(str);
        temp.u=1;
        temp.change = n.change+r+"U"+" ";
        temp.length = n.length+1;
        temp.cost = n.cost+value[Character.getNumericValue(n.s.charAt(i+3))];
        return temp;
    }
    private static boolean check(String s1, String s2){
        int g = get_index(s1);
        int inversions=0;
        StringBuilder sb = new StringBuilder(s1);
        sb.deleteCharAt(g);
        s1 = sb.toString();
        for(int i=0;i<s1.length()-1;i++){
            char a = s1.charAt(i);
            for(int j=i+1;j<s1.length();j++){
                char b = s1.charAt(j);
                if(Character.getNumericValue(a)>Character.getNumericValue(b)) inversions++;
            }
        }
        g=get_index(s2);
        sb =new StringBuilder(s2);
        sb.deleteCharAt(g);
        s2 = sb.toString();
        for(int i=0;i<s2.length()-1;i++){
            char a = s2.charAt(i);
            for(int j=i+1;j<s2.length();j++){
                char b = s2.charAt(j);
                if(Character.getNumericValue(a)>Character.getNumericValue(b)) inversions--;
            }
        }
        return inversions % 2 == 0;
    }
    public static void main(String args[]){
        PrintStream ps = null;
        Scanner sc =null;
        try {
            ps = new PrintStream(new File(args[1]));
            sc = new Scanner(new File(args[0]));
        } catch (FileNotFoundException e) {
            ps.println("input file not found");
        }
        int testcases = sc.nextInt();
        for(int kp=0;kp<testcases;kp++) {
            HashMap<String,Integer> hs = new HashMap<>();
            String source = sc.next();
            String target = sc.next();
            int[] value = new int[9];
            value[0] = -1;
            for (int j = 1; j < 9; j++) value[j] = sc.nextInt();
            if (check(source, target)) {
                long min_cost = 100000000;
                long length_ans = 0;
                String change_ans = "";
                Queue<node> q = new LinkedList<>();
                node root = new node(source);
                root.cost = 0;
                root.change = "";
                root.length = 0;
                q.add(root);
                hs.put(root.s,0);
                while (!q.isEmpty()) {
                    node temp = q.remove();
                    if (target.equals(temp.s)) {
                        if (min_cost == temp.cost) {
                            if (length_ans == 0) {
                                min_cost = temp.cost;
                                length_ans = temp.length;
                                change_ans = temp.change;
                            } else if (temp.length < length_ans) {
                                min_cost = temp.cost;
                                length_ans = temp.length;
                                change_ans = temp.change;
                            }
                        } else if (min_cost > temp.cost) {
                            min_cost = temp.cost;
                            length_ans = temp.length;
                            change_ans = temp.change;
                        }
                    }
                    else {
                        int in = get_index(temp.s);
                        if ((in != 0) && (in != 3) && (in != 6) && (temp.l == 0)) {
                            node left = move_left(temp, in, value);
                            if (left.cost < min_cost) {
                                if (!hs.containsKey(left.s) || (hs.containsKey(left.s) && (hs.get(left.s) > left.cost))) {
                                    q.add(left);
                                    hs.remove(left.s);
                                    hs.put(left.s, (int) left.cost);
                                }
                            }
                        }
                        if ((in != 2) && (in != 5) && (in != 8) && (temp.r == 0)) {
                            node right = move_right(temp, in, value);
                            if (right.cost < min_cost) {
                                if (!hs.containsKey(right.s) || (hs.containsKey(right.s) && (hs.get(right.s) > right.cost))) {
                                    q.add(right);
                                    hs.remove(right.s);
                                    hs.put(right.s, (int) right.cost);
                                }
                            }
                        }
                        if ((in != 0) && (in != 1) && (in != 2) && (temp.u == 0)) {
                            node up = move_up(temp, in, value);
                            if (up.cost < min_cost) {
                                if (!hs.containsKey(up.s) || (hs.containsKey(up.s) && (hs.get(up.s) > up.cost))) {
                                    q.add(up);
                                    hs.remove(up.s);
                                    hs.put(up.s, (int) up.cost);
                                }
                            }
                        }
                        if ((in != 6) && (in != 7) && (in != 8) && (temp.d == 0)) {
                            node down = move_down(temp, in, value);
                            if (down.cost < min_cost) {
                                if (!hs.containsKey(down.s) || (hs.containsKey(down.s) && (hs.get(down.s) > down.cost))) {
                                    q.add(down);
                                    hs.remove(down.s);
                                    hs.put(down.s, (int) down.cost);
                                }
                            }
                        }
                    }
                }
                ps.println(length_ans + " " + min_cost);
                if(change_ans.length()!=0)ps.println(change_ans.substring(0, change_ans.length() - 1));
                else ps.println("");
            }
            else {
                ps.println("-1 -1");
                ps.println("");
            }
        }
    }
}



/*import javafx.util.Pair;

import java.io.*;
import java.util.*;
import java.lang.*;
public class puzzles {
    public static class pair{
        String s;
        int cost=0;
        public pair(String str,int num){
            s = str;
            cost = num;
        }
}
    public static boolean search(String s,LinkedList<pair> ls){
        for(int i=0;i<ls.size();i++){
            if(s.equals(ls.get(i).s)) return true;
        }
        return false;
    }
    public static void main(String args[]){
        long st = System.currentTimeMillis();
        Scanner sc =null;
        try {
            sc = new Scanner(new File("C:\\Users\\AYUSH\\Desktop\\A5\\inp.txt"));
        } catch (FileNotFoundException e) {
            ps.println("input file not found");
        }
        int numberofcases = sc.nextInt();
        char g = 'G';
        for(int i=0;i<numberofcases;i++){
            String source = sc.next();
            String destination = sc.next();
            int[] value = new int[9];
            value[0] = -1;
            for(int j=1;j<9;j++) value[j] = sc.nextInt();
            Vector<LinkedList<pair>> vertices = new Vector<>();
            Vector<String> check_v = new Vector<>();
            Queue<String> q = new LinkedList<>();
            HashSet<String> hash = new HashSet<>();
            q.add(source);
            //int qw=0;
            while(!q.isEmpty()){/*!q.isEmpty()
                //qw+
                ps.println(q.size());
                String temp = q.remove();
                int gap_index=0;
                //String re = new String(temp);
                if(!hash.contains(temp)){
                    //Vector<pair> s = new Vector<>();
                    check_v.add(temp);
                    hash.add(temp);
                    vertices.add(new LinkedList<>());
                    //vertices.add(s);
                    //temp = "123G4578";
                    for(int j=0;j<temp.length();j++){
                        String y = String.valueOf(temp.charAt(j));
                        if(y.equals("G")){
                            gap_index = j;
                            break;
                        }
                    }
                    //ps.println(gap_index);
                    int index = check_v.indexOf(temp);
                    if(gap_index==0) {
                        String n = temp;
                        int c;
                        n = temp;
                        String r = Character.toString(temp.charAt(1));
                        //ps.println(temp);
                        n = n.replace("G", "*").replace(r, "G").replace("*", r);
                        //ps.println(temp.charAt(1));
                        //ps.println(gap_index);
                        c = value[Character.getNumericValue(temp.charAt(1))];
                        q.add(n);
                        if (!search(n,vertices.get(index))) {
                            pair p = new pair(n,c);
                            vertices.get(index).addLast(p);
                        }
                        n = temp;
                        r = Character.toString(temp.charAt(3));
                        n = n.replace("G", "*").replace(r, "G").replace("*", r);
                        c = value[Character.getNumericValue(temp.charAt(3))];
                        q.add(n);
                        if (/*!vertices.get(index).contains(n)!search(n,vertices.get(index))) {
                            pair p = new pair(n,c);
                            vertices.get(index).addLast(p);
                        }
                    }
                    else if (gap_index==1) {
                        String n = temp;
                        int c;
                        n = temp;
                        String r = Character.toString(temp.charAt(0));
                        n = n.replace("G", "*").replace(r, "G").replace("*", r);
                        c = value[Character.getNumericValue(temp.charAt(0))];
                        q.add(n);
                        if (!search(n,vertices.get(index))) {
                            pair p = new pair(n,c);
                            vertices.get(index).addLast(p);
                        }
                        n = temp;
                        r = Character.toString(temp.charAt(2));
                        n = n.replace("G", "*").replace(r, "G").replace("*", r);
                        c = value[Character.getNumericValue(temp.charAt(2))];
                        q.add(n);
                        if (!search(n,vertices.get(index))) {
                            pair p = new pair(n,c);
                            vertices.get(index).addLast(p);
                        }
                        n = temp;
                        r = Character.toString(temp.charAt(4));
                        n = n.replace("G", "*").replace(r, "G").replace("*", r);
                        c = value[Character.getNumericValue(temp.charAt(4))];
                        q.add(n);
                        if (!search(n,vertices.get(index))) {
                            pair p = new pair(n,c);
                            vertices.get(index).addLast(p);
                        }
                    }
                    else if (gap_index==2){
                        String n=temp;
                        int c;
                        n=temp;
                        String r = Character.toString(temp.charAt(1));
                        n = n.replace("G","*").replace(r,"G").replace("*",r);
                        c = value[Character.getNumericValue(temp.charAt(1))];
                        q.add(n);
                        if (!search(n,vertices.get(index))){
                            pair p = new pair(n,c);
                            vertices.get(index).addLast(p);
                        }
                        n=temp;
                        r = Character.toString(temp.charAt(5));
                        n = n.replace("G","*").replace(r,"G").replace("*",r);
                        c = value[Character.getNumericValue(temp.charAt(5))];
                        q.add(n);
                        if (!search(n,vertices.get(index))){
                            pair p = new pair(n,c);
                            vertices.get(index).addLast(p);
                        }
                    }
                    else if (gap_index==3){
                        String n=temp;
                        int c;
                        n=temp;
                        String r = Character.toString(temp.charAt(0));
                        n = n.replace("G","*").replace(r,"G").replace("*",r);
                        c = value[Character.getNumericValue(temp.charAt(0))];
                        q.add(n);
                        if (!search(n,vertices.get(index))){
                            pair p = new pair(n,c);
                            vertices.get(index).addLast(p);
                        }
                        n=temp;
                        r = Character.toString(temp.charAt(4));
                        n = n.replace("G","*").replace(r,"G").replace("*",r);
                        c = value[Character.getNumericValue(temp.charAt(4))];
                        q.add(n);
                        if (!search(n,vertices.get(index))){
                            pair p = new pair(n,c);
                            vertices.get(index).addLast(p);
                        }
                        n=temp;
                        r = Character.toString(temp.charAt(6));
                        n = n.replace("G","*").replace(r,"G").replace("*",r);
                        c = value[Character.getNumericValue(temp.charAt(6))];
                        q.add(n);
                        if (!search(n,vertices.get(index))){
                            pair p = new pair(n,c);
                            vertices.get(index).addLast(p);
                        }
                    }
                    else if (gap_index==4){
                        String n=temp;
                        int c;
                        n=temp;
                        String r = Character.toString(temp.charAt(1));
                        n = n.replace("G","*").replace(r,"G").replace("*",r);
                        c = value[Character.getNumericValue(temp.charAt(1))];
                        q.add(n);
                        if (!search(n,vertices.get(index))){
                            pair p = new pair(n,c);
                            vertices.get(index).addLast(p);
                        }
                        n=temp;
                        r = Character.toString(temp.charAt(3));
                        n = n.replace("G","*").replace(r,"G").replace("*",r);
                        c = value[Character.getNumericValue(temp.charAt(3))];
                        q.add(n);
                        if (!search(n,vertices.get(index))){
                            pair p = new pair(n,c);
                            vertices.get(index).addLast(p);
                        }
                        n=temp;
                        r = Character.toString(temp.charAt(7));
                        n = n.replace("G","*").replace(r,"G").replace("*",r);
                        c = value[Character.getNumericValue(temp.charAt(7))];
                        q.add(n);
                        if (!search(n,vertices.get(index))){
                            pair p = new pair(n,c);
                            vertices.get(index).addLast(p);
                        }
                        n=temp;
                        r = Character.toString(temp.charAt(5));
                        n = n.replace("G","*").replace(r,"G").replace("*",r);
                        c = value[Character.getNumericValue(temp.charAt(5))];
                        q.add(n);
                        if (!search(n,vertices.get(index))){
                            pair p = new pair(n,c);
                            vertices.get(index).addLast(p);
                        }
                    }
                    else if (gap_index==5){
                        String n=temp;
                        int c;
                        n=temp;
                        String r = Character.toString(temp.charAt(2));
                        n = n.replace("G","*").replace(r,"G").replace("*",r);
                        c = value[Character.getNumericValue(temp.charAt(2))];
                        q.add(n);
                        if (!search(n,vertices.get(index))){
                            pair p = new pair(n,c);
                            vertices.get(index).addLast(p);
                        }
                        n=temp;
                        r = Character.toString(temp.charAt(4));
                        n = n.replace("G","*").replace(r,"G").replace("*",r);
                        c = value[Character.getNumericValue(temp.charAt(4))];
                        q.add(n);
                        if (!search(n,vertices.get(index))){
                            pair p = new pair(n,c);
                            vertices.get(index).addLast(p);
                        }
                        n=temp;
                        r = Character.toString(temp.charAt(8));
                        n = n.replace("G","*").replace(r,"G").replace("*",r);
                        c = value[Character.getNumericValue(temp.charAt(8))];
                        q.add(n);
                        if (!search(n,vertices.get(index))){
                            pair p = new pair(n,c);
                            vertices.get(index).addLast(p);
                        }
                    }
                    else if (gap_index==6){
                        String n=temp;
                        int c;
                        n=temp;
                        String r = Character.toString(temp.charAt(3));
                        n = n.replace("G","*").replace(r,"G").replace("*",r);
                        c = value[Character.getNumericValue(temp.charAt(3))];
                        q.add(n);
                        if (!search(n,vertices.get(index))){
                            pair p = new pair(n,c);
                            vertices.get(index).addLast(p);
                        }
                        n=temp;
                        r = Character.toString(temp.charAt(7));
                        n = n.replace("G","*").replace(r,"G").replace("*",r);
                        c = value[Character.getNumericValue(temp.charAt(7))];
                        q.add(n);
                        if (!search(n,vertices.get(index))){
                            pair p = new pair(n,c);
                            vertices.get(index).addLast(p);
                        }
                    }
                    else if (gap_index==7){
                        String n=temp;
                        int c;
                        n=temp;
                        String r = Character.toString(temp.charAt(6));
                        n = n.replace("G","*").replace(r,"G").replace("*",r);
                        c = value[Character.getNumericValue(temp.charAt(6))];
                        q.add(n);
                        if (!search(n,vertices.get(index))){
                            pair p = new pair(n,c);
                            vertices.get(index).addLast(p);
                        }
                        n=temp;
                        r = Character.toString(temp.charAt(4));
                        n = n.replace("G","*").replace(r,"G").replace("*",r);
                        c = value[Character.getNumericValue(temp.charAt(4))];
                        q.add(n);
                        if (!search(n,vertices.get(index))){
                            pair p = new pair(n,c);
                            vertices.get(index).addLast(p);
                        }
                        n=temp;
                        r = Character.toString(temp.charAt(8));
                        n = n.replace("G","*").replace(r,"G").replace("*",r);
                        c = value[Character.getNumericValue(temp.charAt(8))];
                        q.add(n);
                        if (!search(n,vertices.get(index))){
                            pair p = new pair(n,c);
                            vertices.get(index).addLast(p);
                        }
                    }
                    else if (gap_index==8){
                        String n=temp;
                        int c;
                        n=temp;
                        String r = Character.toString(temp.charAt(7));
                        n = n.replace("G","*").replace(r,"G").replace("*",r);
                        c = value[Character.getNumericValue(temp.charAt(7))];
                        q.add(n);
                        if (!search(n,vertices.get(index))){
                            pair p = new pair(n,c);
                            vertices.get(index).addLast(p);
                        }
                        n=temp;
                        r = Character.toString(temp.charAt(5));
                        n = n.replace("G","*").replace(r,"G").replace("*",r);
                        c = value[Character.getNumericValue(temp.charAt(5))];
                        q.add(n);
                        if (!search(n,vertices.get(index))){
                            pair p = new pair(n,c);
                            vertices.get(index).addLast(p);
                        }
                    }
                    else ps.println("invalid string input");
                }
            }
            /*int max=0;
            for(int h=0;h<vertices.size();h++){
                if(max<vertices.get(h).size()) max = vertices.get(h).size();
            }*/
//ps.println(max);
//String temp = "12345678G";
//ps.println(vertices.get(0).get(2).s);
            /*boolean b = vertices.contains("12345678G");
            ps.println(vertices.get(0));
            int id=vertices.indexOf("12345678G");
            ps.println(id);
        }
        long ed = System.currentTimeMillis();
        ps.println(ed-st);
    }
}
import java.io.*;
import java.util.*;
public class Puzzle {
    public static class pair{
        node n;
        int cost;
        public pair(node x,int c){
            n=x;
            cost=c;
        }
    }
    public static class node{
        pair parent = null;
        String change = "";
        Vector<pair> neighbours = new Vector<>();
        int[][] arr = new int[3][3];
        int row,column;
        int step;
        public node(String s){
            arr = str_arr(s);
            if(parent==null) step=0;
            else{
                step = parent.n.step+1;
            }
            for(int i=0;i<3;i++){
                for (int j=0;j<3;j++){
                    if(arr[i][j]==0){
                        row=i;
                        column=j;
                    }
                }
            }
        }
        public node(int[][] a){
            arr=a;
            if(parent==null) step=0;
            else{
                step = parent.n.step+1;
            }
            //step=n;
            for(int i=0;i<3;i++){
                for (int j=0;j<3;j++){
                    if(arr[i][j]==0){
                        row=i;
                        column=j;
                    }
                }
            }
        }
    }
    public static int[][] str_arr(String s){
        int[][] arr = new int[3][3];
        int l=0;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    char c = s.charAt(l);
                    l++;
                    if(c=='G') arr[i][j] = 0;
                    else arr[i][j] = Character.getNumericValue(c);
                }
            }
        return arr;
    }
    public static String arr_str(int[][] a){
        String s = "";
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(a[i][j]==0) s+="G";
                else s += (char)a[i][j];
            }
        }
        return s;
    }
    public static int[][] swap(int[][] temp,int i,int j, int i1, int j1){
        int[][] t = new int[3][3];
        for(int g=0;g<3;g++){
            for(int h=0;h<3;h++){
                t[g][h] = temp[g][h];
            }
        }
        //ps.println(j);
        int x = temp[i][j];
        int y = temp[i1][j1];
        t[i][j] = y;
        t[i1][j1] = x;
        return t;
    }
    public static void main(String[] args){
        long str = System.currentTimeMillis();
        //HashSet<int[][]> h = new HashSet<>();
        Scanner sc =null;
        try {
            sc = new Scanner(new File("C:\\Users\\AYUSH\\Desktop\\A5\\inp.txt"));
        } catch (FileNotFoundException e) {
            ps.println("input file not found");
        }
        int t = sc.nextInt();
        for(int i=0;i<t;i++){
            HashSet<String> h = new HashSet<>();
            String source = sc.next();
            String target = sc.next();
            int[] value = new int[9];
            value[0] = -1;
            for(int j=1;j<9;j++) value[j] = sc.nextInt();
            node root = new node(source);
            h.add(source);
            int step=1;
            Queue<node> q = new LinkedList<node>();
            q.add(root);
            int m,n;
            //to start making the graph here
            while(!q.isEmpty()) {
                //ps.println(q.size());
                m = 1;
                n = 10;
                int op = 0;
                node temp = q.remove();
                    if (temp.row == 0) {
                        if (temp.column == 0) {
                            node x = new node(swap(temp.arr, 0, 0, 0, 1));
                            //ps.println(temp.arr[0][1]);
                            pair p = new pair(x, value[temp.arr[0][1]]);
                            p.n.parent = new pair(temp, value[temp.arr[0][1]]);
                            p.n.step = p.n.parent.n.step + 1;
                            if (!h.contains(arr_str(x.arr))) {
                                temp.neighbours.add(p);
                                temp.neighbours.lastElement().n.change = Integer.toString(temp.arr[0][1]) + "L";
                                q.add(temp.neighbours.get(temp.neighbours.size()-1).n);
                                h.add(arr_str(x.arr));
                            }
                            else op++;
                            x = new node(swap(temp.arr, 0, 0, 1, 0));
                            p = new pair(x, value[temp.arr[1][0]]);
                            p.n.parent = new pair(temp, value[temp.arr[1][0]]);
                            p.n.step = p.n.parent.n.step + 1;
                            if (!h.contains(arr_str(x.arr))) {
                                temp.neighbours.add(p);
                                h.add(arr_str(x.arr));
                                temp.neighbours.lastElement().n.change = Integer.toString(temp.arr[1][0]) + "U";
                                q.add(temp.neighbours.get(temp.neighbours.size()-1).n);
                            }
                            else op++;
                            if(op==2) m=n;
                        } else if (temp.column == 1) {
                            node x = new node(swap(temp.arr, 0, 0, 0, 1));
                            pair p = new pair(x, value[temp.arr[0][0]]);
                            p.n.parent = new pair(temp, value[temp.arr[0][0]]);
                            p.n.step = p.n.parent.n.step + 1;
                            if (!h.contains(arr_str(x.arr))) {
                                temp.neighbours.add(p);
                                h.add(arr_str(x.arr));
                                temp.neighbours.lastElement().n.change = Integer.toString(temp.arr[0][0]) + "R";
                                q.add(temp.neighbours.get(temp.neighbours.size()-1).n);
                            }
                            else op++;
                            x = new node(swap(temp.arr, 0, 2, 0, 1));
                            p = new pair(x, value[temp.arr[0][2]]);
                            p.n.parent = new pair(temp, value[temp.arr[0][2]]);
                            p.n.step = p.n.parent.n.step + 1;
                            if (!h.contains(arr_str(x.arr))) {
                                temp.neighbours.add(p);
                                h.add(arr_str(x.arr));
                                temp.neighbours.lastElement().n.change = Integer.toString(temp.arr[0][2]) + "L";
                                q.add(temp.neighbours.get(temp.neighbours.size()-1).n);
                            }
                            else op++;
                            x = new node(swap(temp.arr, 0, 1, 1, 1));
                            p = new pair(x, value[temp.arr[1][1]]);
                            p.n.parent = new pair(temp, value[temp.arr[1][1]]);
                            p.n.step = p.n.parent.n.step + 1;
                            if (!h.contains(arr_str(x.arr))) {
                                temp.neighbours.add(p);
                                h.add(arr_str(x.arr));
                                temp.neighbours.lastElement().n.change = Integer.toString(temp.arr[1][1]) + "U";
                                q.add(temp.neighbours.get(temp.neighbours.size()-1).n);
                            }
                            else op++;
                            if(op==3) m=n;
                        } else if (temp.column == 2) {
                            node x = new node(swap(temp.arr, 0, 2, 0, 1));
                            pair p = new pair(x, value[temp.arr[0][1]]);
                            p.n.parent = new pair(temp, value[temp.arr[0][1]]);
                            p.n.step = p.n.parent.n.step + 1;
                            if (!h.contains(arr_str(x.arr))) {
                                temp.neighbours.add(p);
                                h.add(arr_str(x.arr));
                                temp.neighbours.lastElement().n.change = Integer.toString(temp.arr[0][1]) + "R";
                                q.add(temp.neighbours.get(temp.neighbours.size()-1).n);
                            }
                            else op++;
                            x = new node(swap(temp.arr, 0, 2, 1, 2));
                            p = new pair(x, value[temp.arr[1][2]]);
                            p.n.parent = new pair(temp, value[temp.arr[1][2]]);
                            p.n.step = p.n.parent.n.step + 1;
                            if (!h.contains(arr_str(x.arr))) {
                                temp.neighbours.add(p);
                                h.add(arr_str(x.arr));
                                temp.neighbours.lastElement().n.change = Integer.toString(temp.arr[1][2]) + "U";
                                q.add(temp.neighbours.get(temp.neighbours.size()-1).n);
                            }
                            else op++;
                            if(op==2) m=n;
                        }
                    } else if (temp.row == 1) {
                        if (temp.column == 0) {
                            node x = new node(swap(temp.arr, 1, 0, 0, 0));
                            pair p = new pair(x, value[temp.arr[0][0]]);
                            p.n.parent = new pair(temp, value[temp.arr[0][0]]);
                            p.n.step = p.n.parent.n.step + 1;
                            if (!h.contains(arr_str(x.arr))) {
                                temp.neighbours.add(p);
                                h.add(arr_str(x.arr));
                                temp.neighbours.lastElement().n.change = Integer.toString(temp.arr[0][0]) + "D";
                                q.add(temp.neighbours.get(temp.neighbours.size()-1).n);
                            }
                            else op++;
                            x = new node(swap(temp.arr, 1, 1, 1, 0));
                            p = new pair(x, value[temp.arr[1][1]]);
                            p.n.parent = new pair(temp, value[temp.arr[1][1]]);
                            p.n.step = p.n.parent.n.step + 1;
                            if (!h.contains(arr_str(x.arr))) {
                                temp.neighbours.add(p);
                                h.add(arr_str(x.arr));
                                temp.neighbours.lastElement().n.change = Integer.toString(temp.arr[1][1]) + "L";
                                q.add(temp.neighbours.get(temp.neighbours.size()-1).n);
                            }
                            else op++;
                            x = new node(swap(temp.arr, 2, 0, 1, 0));
                            p = new pair(x, value[temp.arr[2][0]]);
                            p.n.parent = new pair(temp, value[temp.arr[2][0]]);
                            p.n.step = p.n.parent.n.step + 1;
                            if (!h.contains(arr_str(x.arr))) {
                                temp.neighbours.add(p);
                                h.add(arr_str(x.arr));
                                temp.neighbours.lastElement().n.change = Integer.toString(temp.arr[0][1]) + "U";
                                q.add(temp.neighbours.get(temp.neighbours.size()-1).n);
                            }
                            else op++;
                            if (op==3) m=n;
                        } else if (temp.column == 1) {
                            node x = new node(swap(temp.arr, 0, 1, 1, 1));
                            pair p = new pair(x, value[temp.arr[0][1]]);
                            p.n.parent = new pair(temp, value[temp.arr[0][1]]);
                            p.n.step = p.n.parent.n.step + 1;
                            if (!h.contains(arr_str(x.arr))) {
                                temp.neighbours.add(p);
                                h.add(arr_str(x.arr));
                                temp.neighbours.lastElement().n.change = Integer.toString(temp.arr[0][1]) + "D";
                                q.add(temp.neighbours.get(temp.neighbours.size()-1).n);
                            }
                            else op++;
                            x = new node(swap(temp.arr, 1, 0, 1, 1));
                            p = new pair(x, value[temp.arr[1][0]]);
                            p.n.parent = new pair(temp, value[temp.arr[1][0]]);
                            p.n.step = p.n.parent.n.step + 1;
                            if (!h.contains(arr_str(x.arr))) {
                                temp.neighbours.add(p);
                                h.add(arr_str(x.arr));
                                temp.neighbours.lastElement().n.change = Integer.toString(temp.arr[1][0]) + "R";
                                q.add(temp.neighbours.get(temp.neighbours.size()-1).n);
                            }
                            else op++;
                            x = new node(swap(temp.arr, 1, 2, 1, 1));
                            p = new pair(x, value[temp.arr[1][2]]);
                            p.n.parent = new pair(temp, value[temp.arr[1][2]]);
                            p.n.step = p.n.parent.n.step + 1;
                            if (!h.contains(arr_str(x.arr))) {
                                temp.neighbours.add(p);
                                h.add(arr_str(x.arr));
                                temp.neighbours.lastElement().n.change = Integer.toString(temp.arr[1][2]) + "L";
                                q.add(temp.neighbours.get(temp.neighbours.size()-1).n);
                            }
                            else op++;
                            x = new node(swap(temp.arr, 2, 1, 1, 1));
                            p = new pair(x, value[temp.arr[2][1]]);
                            p.n.parent = new pair(temp, value[temp.arr[2][1]]);
                            p.n.step = p.n.parent.n.step + 1;
                            if (!h.contains(arr_str(x.arr))) {
                                temp.neighbours.add(p);
                                h.add(arr_str(x.arr));
                                temp.neighbours.lastElement().n.change = Integer.toString(temp.arr[2][1]) + "U";
                                q.add(temp.neighbours.get(temp.neighbours.size()-1).n);
                            }
                            else op++;
                            if (op==4) m=n;
                        } else if (temp.column == 2) {
                            node x = new node(swap(temp.arr, 0, 2, 1, 2));
                            pair p = new pair(x, value[temp.arr[0][2]]);
                            p.n.parent = new pair(temp, value[temp.arr[0][2]]);
                            p.n.step = p.n.parent.n.step + 1;
                            if (!h.contains(arr_str(x.arr))) {
                                ps.println("k");
                                temp.neighbours.add(p);
                                h.add(arr_str(x.arr));
                                temp.neighbours.lastElement().n.change = Integer.toString(temp.arr[0][2]) + "D";
                                q.add(temp.neighbours.get(temp.neighbours.size()-1).n);
                            }
                            else op++;
                            x = new node(swap(temp.arr, 1, 1, 1, 2));
                            p = new pair(x, value[temp.arr[1][1]]);
                            p.n.parent = new pair(temp, value[temp.arr[1][1]]);
                            p.n.step = p.n.parent.n.step + 1;
                            if (!h.contains(arr_str(x.arr))) {
                                ps.println("k1");
                                temp.neighbours.add(p);
                                h.add(arr_str(x.arr));
                                temp.neighbours.lastElement().n.change = Integer.toString(temp.arr[1][1]) + "R";
                                q.add(temp.neighbours.get(temp.neighbours.size()-1).n);
                            }
                            else op++;
                            x = new node(swap(temp.arr, 2, 2, 1, 2));
                            p = new pair(x, value[temp.arr[2][2]]);
                            p.n.parent = new pair(temp, value[temp.arr[2][2]]);
                            p.n.step = p.n.parent.n.step + 1;
                            if (!h.contains(arr_str(x.arr))) {
                                ps.println("k2");
                                temp.neighbours.add(p);
                                h.add(arr_str(x.arr));
                                temp.neighbours.lastElement().n.change = Integer.toString(temp.arr[2][2]) + "U";
                                q.add(temp.neighbours.get(temp.neighbours.size()-1).n);
                            }
                            else op++;
                            if (op==3) m=n;
                        }
                    } else if (temp.row == 2) {
                        if (temp.column == 0) {
                            node x = new node(swap(temp.arr, 2, 0, 1, 0));
                            pair p = new pair(x, value[temp.arr[1][0]]);
                            p.n.parent = new pair(temp, value[temp.arr[1][0]]);
                            p.n.step = p.n.parent.n.step + 1;
                            if (!h.contains(arr_str(x.arr))) {
                                temp.neighbours.add(p);
                                h.add(arr_str(x.arr));
                                temp.neighbours.lastElement().n.change = Integer.toString(temp.arr[1][0]) + "D";
                                q.add(temp.neighbours.get(temp.neighbours.size()-1).n);
                            }
                            else op++;
                            x = new node(swap(temp.arr, 2, 0, 2, 1));
                            p = new pair(x, value[temp.arr[2][1]]);
                            p.n.parent = new pair(temp, value[temp.arr[2][1]]);
                            p.n.step = p.n.parent.n.step + 1;
                            if (!h.contains(arr_str(x.arr))) {
                                temp.neighbours.add(p);
                                h.add(arr_str(x.arr));
                                temp.neighbours.lastElement().n.change = Integer.toString(temp.arr[2][1]) + "L";
                                q.add(temp.neighbours.get(temp.neighbours.size()-1).n);
                            }
                            else op++;
                            if (op==2) m=n;
                        } else if (temp.column == 1) {
                            node x = new node(swap(temp.arr, 2, 1, 2, 0));
                            //ps.println(temp.arr[2][0]);
                            pair p = new pair(x, value[temp.arr[2][0]]);
                            p.n.parent = new pair(temp, value[temp.arr[2][0]]);
                            p.n.step = p.n.parent.n.step + 1;
                            if (!h.contains(arr_str(x.arr))) {
                                temp.neighbours.add(p);
                                h.add(arr_str(x.arr));
                                temp.neighbours.lastElement().n.change = Integer.toString(temp.arr[2][0]) + "R";
                                q.add(temp.neighbours.get(temp.neighbours.size()-1).n);
                            }
                            else op++;
                            x = new node(swap(temp.arr, 2, 1, 1, 1));
                            p = new pair(x, value[temp.arr[1][1]]);
                            p.n.parent = new pair(temp, value[temp.arr[1][1]]);
                            p.n.step = p.n.parent.n.step + 1;
                            if (!h.contains(arr_str(x.arr))) {
                                temp.neighbours.add(p);
                                h.add(arr_str(x.arr));
                                temp.neighbours.lastElement().n.change = Integer.toString(temp.arr[1][1]) + "D";
                                q.add(temp.neighbours.get(temp.neighbours.size()-1).n);
                            }
                            else op++;
                            x = new node(swap(temp.arr, 2, 1, 2, 2));
                            p = new pair(x, value[temp.arr[2][2]]);
                            p.n.parent = new pair(temp, value[temp.arr[2][2]]);
                            p.n.step = p.n.parent.n.step + 1;
                            if (!h.contains(arr_str(x.arr))) {
                                temp.neighbours.add(p);
                                h.add(arr_str(x.arr));
                                temp.neighbours.lastElement().n.change = Integer.toString(temp.arr[2][2]) + "L";
                                q.add(temp.neighbours.get(temp.neighbours.size()-1).n);
                            }
                            else op++;
                            if (op==3) m=n;
                        } else if (temp.column == 2) {
                            node x = new node(swap(temp.arr, 2, 2, 1, 2));
                            pair p = new pair(x, value[temp.arr[1][2]]);
                            p.n.parent = new pair(temp, value[temp.arr[1][2]]);
                            p.n.step = p.n.parent.n.step + 1;
                            if (!h.contains(arr_str(x.arr))) {
                                temp.neighbours.add(p);
                                h.add(arr_str(x.arr));
                                temp.neighbours.lastElement().n.change = Integer.toString(temp.arr[1][2]) + "D";
                                q.add(temp.neighbours.get(temp.neighbours.size()-1).n);
                            }
                            else op++;
                            x = new node(swap(temp.arr, 2, 2, 2, 1));
                            p = new pair(x, value[temp.arr[2][1]]);
                            p.n.parent = new pair(temp, value[temp.arr[2][1]]);
                            p.n.step = p.n.parent.n.step + 1;
                            if (!h.contains(arr_str(x.arr))) {
                                temp.neighbours.add(p);
                                h.add(arr_str(x.arr));
                                temp.neighbours.lastElement().n.change = Integer.toString(temp.arr[2][1]) + "R";
                                q.add(temp.neighbours.get(temp.neighbours.size()-1).n);
                            }
                            else op++;
                            if (op==2) m=n;
                        }
                    }
                    /*for (int k = 0; k < temp.neighbours.size(); k++) {
                        if(m!=n) q.add(temp.neighbours.get(k).n);
                    }

            }
                    int w=0;
                    int mm=2;
                    //ps.println(" "+root.neighbours.get(0).n.parent.n.column);
                    ps.println(root.neighbours.get(w).n.neighbours.get(mm).n.row+" "+root.neighbours.get(w).n.neighbours.get(mm).n.column+" "+root.neighbours.get(w).n.neighbours.get(mm).n.step);
                    int[][] tr = new int[3][3];
                    tr[0][0] = 1;
                    tr[0][1] = 2;
                    tr[0][2] = 3;
                    tr[1][0] = 4;
                    tr[1][1] = 5;
                    tr[1][2] = 6;
                    tr[2][0] = 7;
                    tr[2][1] = 8;
                    tr[2][2] = 0;
                    ps.println(arr_str(tr));
                    }
                    long ed = System.currentTimeMillis();
                    //ps.println(ed-str);
                    }
                    }
                    */