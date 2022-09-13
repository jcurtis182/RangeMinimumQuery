package bst;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
//import java.util.Random;

class Node {
    int key, data, minData;
    Node left, right;
            
    public Node(int Key, int Data) {
        key = Key;
        data = Data;
    }
}

public class BST {
    static void SetMinData(Node root) {
        if (root != null) root.minData = root.data;
        if (root.left != null) root.minData = Math.min(root.minData, root.left.minData);
        if (root.right != null) root.minData = Math.min(root.minData, root.right.minData);
    }
    
    static Node insertKey(Node root, int key, int data) {
        long start = System.currentTimeMillis();
        long elapsed = 0;
        if (root == null) {                     
            root = new Node(key, data);
            root.minData = data;
            return root;
        }   
        if (key < root.key) {
            root.left = insertKey(root.left, key, data);
            elapsed = System.currentTimeMillis() - start;
        }
        else if (key > root.key) {
            root.right = insertKey(root.right, key, data);
            elapsed = System.currentTimeMillis() - start;
        }    
        SetMinData(root);
        //System.out.print(elapsed + ",\n");
        return root;        
    }
    static int min = 10000;
    static Node root = null;
    
// Method 1
    public static void rmqNaive(int k1, int k2, Node root) {
        if (root == null) return;
        if (root.key >= k1) rmqNaive(k1, k2, root.left);
        if (root.key <= k2) rmqNaive(k1, k2, root.right);
        if ((root.key >=k1) && (root.key <= k2)) min = Math.min(min, root.data);
        
    }
    
// Method 2    
    static int RangeMin(int k1, int k2, Node root) {
        while (!(k1 <= root.key && root.key <= k2)) 
            if (k1 < root.key && k2 < root.key)
                if (root.left != null) root = root.left;
                else if (root.right != null) root = root.right;
                
                int min = RMRight(k1, root.left);
                int min2 = RMLeft(k2, root.right);
                return Math.min(min, min2);
    }

    static int RMRight(int k1, Node root) {
        int min = root.data;
        if (k1 < root.key && root.right != null) {
            min = Math.min(min, root.right.minData);
            RMRight(k1, root.left);
        }
        else if (k1 > root.key && root.right != null) RMRight(k1, root.right);
        else if (root.right != null) min = Math.min(min, root.right.minData);
        return min;
    }

    static int RMLeft(int k2, Node root) {
        int min = root.data;
        if (k2 < root.key && root.right != null) {
            min = Math.min(min, root.left.minData);
            RMRight(k2, root.right);
        }
        else if (k2 > root.key && root.right != null) RMRight(k2, root.left);
        else if (root.right != null) min = Math.min(min, root.left.minData);
        return min;
    }
    
    
    public static void main(String[] args) throws FileNotFoundException {
        File in = new File("inputFile.txt");
        Scanner scan = new Scanner(in);
        
//    // Random #s for Comparison
//        Random rand = new Random();
//        int maxSize = 32767;
//        int randKey = rand.nextInt(maxSize);
//        int randData = rand.nextInt(maxSize);
//        int total = 30000;
//        long start = System.currentTimeMillis();
//        root = insertKey(null, randKey, randData);
//        for (int i = 0; i < total; i++) {
//            insertKey(root, randKey, randData);
//            BST.rmqNaive(randKey, randData, root);
//        }
//        long elapsed = System.currentTimeMillis() - start;
//        System.out.println("Naive time = " + elapsed);
//        


        int numInstructions = 0;
        if (scan.hasNextLine()) numInstructions = Integer.parseInt(scan.nextLine());
        String firstInstruction = scan.nextLine();
        String[] firstSplit = firstInstruction.split("\\s");
        root = insertKey(null, Integer.parseInt(firstSplit[1]), Integer.parseInt(firstSplit[2]));
        while (numInstructions >= 1 && scan.hasNextLine()) {
            String instructions = scan.nextLine();
            String[] split = instructions.split(" ");
            if (split[0].equals("IN")) BST.insertKey(BST.root, Integer.parseInt(split[1]), Integer.parseInt(split[2]));
            else if (split[0].equals("RMQ")) min = BST.RangeMin(Integer.parseInt(split[1]), Integer.parseInt(split[2]), BST.root);
            numInstructions--;
        }
       System.out.print(min + "\n"); 
    }
}