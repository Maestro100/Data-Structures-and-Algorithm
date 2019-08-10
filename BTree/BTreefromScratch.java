package col106.a3;

import java.util.List;
import java.util.*;

public class BTree<Key extends Comparable<Key>,Value> implements DuplicateBTree<Key,Value> {
    int t;
    public class element{
        public Key k;
        public Value v;
        public element(Key ke, Value va){
            k = ke;
            v = va;
        }
    }
    public class node{
        public Vector<element> key_pairs = new Vector<element>();
        public Vector<node> children = new Vector<node>();
        int numkeys = 0;
        public int searchInNode (Key k1){
            int i=0;
            while(i<numkeys){
                int x = k1.compareTo(key_pairs.get(i).k);
                if(x<=0) return i;
                else i++;
            }
            return i;
        }
        public boolean isleaf(){
            if(children.isEmpty()) return true;
            else return false;
        }
    }
    int size,height;
    node root;
    public BTree(int b) throws bNotEvenException { 
        if(b%2!=0) throw new bNotEvenException();
        root = new node();
        t=b/2;size=0;height=-1;
    }


    @Override
    public boolean isEmpty() {
        if(root==null) return true;
        else return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int height() {
        return height;
    }
    
    @Override
    public List<Value> search(Key key) throws IllegalKeyException {
        Vector<Value> imp = new Vector<Value>();
        node one = searchone(key,root);
        if(key==null) throw new IllegalKeyException();
        else if(one==null) return imp;
        return searchmore(key,one);
    }

    public Vector<Value> searchmore(Key key, node temp_root){
        int count =0;
        node one = searchone(key,temp_root);
        if(one==null) return null;
        Vector<Value> a = new Vector<Value>();
        int node_le_eq_idx = one.searchInNode(key);
        if(one.isleaf()){
            for(;node_le_eq_idx<one.numkeys;node_le_eq_idx++){
                if(key.compareTo(one.key_pairs.get(node_le_eq_idx).k)==0) a.add(0,one.key_pairs.get(node_le_eq_idx).v);
            }
        }
        else{
            for(;node_le_eq_idx<one.numkeys;node_le_eq_idx++){
                if(key.compareTo(one.key_pairs.get(node_le_eq_idx).k)==0){
                    if(searchmore(key,one.children.get(node_le_eq_idx))!=null) a.addAll(0,searchmore(key,one.children.get(node_le_eq_idx)));
                a.add(0,one.key_pairs.get(node_le_eq_idx).v);
                }
                else break;
            }
            if(searchmore(key,one.children.get(node_le_eq_idx))!=null) a.addAll(0,searchmore(key,one.children.get(node_le_eq_idx)));

        }
        return a;
    }

    public node searchone(Key key, node one){
        int i=0;
        while(!one.isleaf()){
            int node_le_eq_idx = one.searchInNode(key);
            if(node_le_eq_idx!=one.numkeys){
                if(key.compareTo(one.key_pairs.get(node_le_eq_idx).k)==0) return one;
            }
            one = one.children.get(node_le_eq_idx);
        }
        int node_le_eq_idx = one.searchInNode(key);
        if(node_le_eq_idx==one.numkeys) return null;
        if(key.compareTo(one.key_pairs.get(node_le_eq_idx).k)==0) return one;
        return null;
    }

    @Override
    public void insert(Key key, Value val) {
        element p = new element(key,val);
        node current = root;
        if(root.numkeys==(2*t-1)){
            node newroot = new node();
            newroot.key_pairs.add(0,root.key_pairs.get(t-1));
            newroot.numkeys++;
            node left_child = new node();
            node right_child = new node();
            for(int i=0;i<t-1;i++){
                left_child.key_pairs.add(i,root.key_pairs.get(i));
                left_child.numkeys++;
                right_child.key_pairs.add(i,root.key_pairs.get(i+t));
                right_child.numkeys++;
            }
            if(!root.isleaf()){
                for(int j=0;j<t;j++){
                    left_child.children.add(j,root.children.get(j));
                    right_child.children.add(j,root.children.get(j+t));
                }
            }
            newroot.children.add(0,left_child);
            newroot.children.add(1,right_child);
            root=newroot;
            height++;
            insert(key,val);
        }
        else{
            int node_le_eq_idx = current.searchInNode(key);
            while(!current.isleaf()){
                if(current.children.get(node_le_eq_idx).numkeys==(2*t-1)){
                    splitnode(current,node_le_eq_idx);
                    insert(key,val);
                    return;
                }
                current = current.children.get(node_le_eq_idx);
                node_le_eq_idx = current.searchInNode(key);
            }
            current.key_pairs.add(node_le_eq_idx,p);
            current.numkeys++;
            size++;
        }
    }

    public void splitnode(node current,int node_le_eq_idx){
        node cur_child = current.children.get(node_le_eq_idx);
        node newest = new node();
        current.key_pairs.add(node_le_eq_idx,cur_child.key_pairs.get(t-1));
        current.numkeys++;
        for(int i=0;i<(t-1);i++){
            newest.key_pairs.add(i,cur_child.key_pairs.get(i));
            newest.numkeys++;
        }
        if(!cur_child.isleaf()){
            for(int i=0;i<t;i++){
                newest.children.add(i,cur_child.children.get(0));
                cur_child.children.remove(0);
            }
        }
        for(int i=0;i<t;i++){
            cur_child.key_pairs.remove(0);
            cur_child.numkeys--;
        }
        current.children.add(node_le_eq_idx,newest);
    }
    
    @Override
    public void delete(Key key) throws IllegalKeyException {
        if(key==null||searchone(key,root)==null) throw new IllegalKeyException();
        Vector<element> vec = new Vector<element>();
        Queue<node> q = new LinkedList<node>();
        int i=0;
        for(i=0;i<root.numkeys;i++){
            if(key.compareTo(root.key_pairs.get(i).k)!=0){
                vec.add(root.key_pairs.get(i));                
            }
            if(!root.isleaf()) q.add(root.children.get(i));
        }
                 if(!root.isleaf()) q.add(root.children.get(i));   
             i=0;
        while(!q.isEmpty()){
            node temp = q.remove();
            for(i=0;i<temp.numkeys;i++){
                if(key.compareTo(temp.key_pairs.get(i).k)!=0){
                    vec.add(temp.key_pairs.get(i));                
                }
                if(!temp.isleaf()) q.add(temp.children.get(i));
            }
                    if(!temp.isleaf()) q.add(temp.children.get(i));   
        }
        node newest = new node();
        root= newest;
        height = -1;
        size=0;
        while(!vec.isEmpty()){
            Key wk = vec.get(0).k;
            Value wv = vec.get(0).v;
            insert(wk,wv);
            vec.remove(0);
        }        
    } 

    public String toString(){
        return string_node(root);
    }
    public String string_node(node temp_root){
        String s = "[";
        if(temp_root.isleaf()){
            s+= temp_root.key_pairs.get(0).k +"="+temp_root.key_pairs.get(0).v;
            for(int j=1;j<temp_root.numkeys;j++) s+= ", "+temp_root.key_pairs.get(j).k+"="+temp_root.key_pairs.get(j).v;
        }
        else{
            s+= string_node(temp_root.children.get(0));
            for(int j=0;j<temp_root.numkeys;j++){
                s+= ", "+temp_root.key_pairs.get(j).k+"="+temp_root.key_pairs.get(j).v+", "+string_node(temp_root.children.get(j+1));
            }
        }
        s+= "]";
        return s;
    }
}