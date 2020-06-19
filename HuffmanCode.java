// Shivangi Khanna

// Implements HuffmanCode class. Uses Huffman algorithm to compress text files. 
// Takes frequencies of different characters or integers from files and creates 
// nodes. 
// Writes Huffman codes to files. 
// Also reads huffman code to decompress files. 
 
import java.util.*;
import java.io.*;
 
public class HuffmanCode {
 
   private HuffmanNode root;
 
   // Initializes HuffmanCode object.
   // Takes ascii values of characters and other integers. 
   // Tree is ordered by these frequencies.
   public HuffmanCode(int[] frequencies) {
      Queue<HuffmanNode> codes = new PriorityQueue<HuffmanNode>();
      for(int i = 0; i < frequencies.length; i++) {
         if(frequencies[i] > 0) {
            HuffmanNode codeNode = new HuffmanNode(i, frequencies[i]);
            codes.add(codeNode);
         }
      }
      while(codes.size() > 1) {
         HuffmanNode first = codes.remove();
         HuffmanNode second = codes.remove();
         int sum = first.frequency + second.frequency;
         HuffmanNode sumNode = new HuffmanNode(sum, first, second);
         codes.add(sumNode);
      }
      root = codes.remove();
   }
 
   // Constructs tree from the inputted file which has ascii values and codes in the
   // standard format.
   public HuffmanCode(Scanner input) {
      while(input.hasNextLine()) {
         int value = Integer.parseInt(input.nextLine());
         String code = input.nextLine();
         root = readHelp(root, value, code, 0);
      }
   }
 
   // Builds tree from file which has ascii values and codes in the standard format.
   private HuffmanNode readHelp(HuffmanNode subRoot, int value, String code, int index) {
      if(index < code.length()) {
         if(subRoot == null) {
            subRoot = new HuffmanNode(-1, -1);
         }
         if(code.charAt(index) == '0') {
            subRoot.left = readHelp(subRoot.left, value, code, index + 1);
         } else {
            subRoot.right = readHelp(subRoot.right, value, code, index + 1);
         }
      } else {
         subRoot = new HuffmanNode(value, -1);
      }
      return subRoot;
   }
 
   // Writes the tree to the inputted file.
   // It contains ascii values and tree traversal codes.
   public void save(PrintStream output) {
      write(output, root, "");
   }
 
   // Traverses the tree.
   // Writes the tree to the output file.
   private void write(PrintStream output, HuffmanNode subRoot, String code) {
      if(subRoot != null) {
         if(subRoot.left != null && subRoot.right != null) {
            write(output, subRoot.left, code + "0");
            write(output, subRoot.right, code + "1");
         } else {
            output.println(subRoot.asciiValue);
            output.println(code);
         }
      }
   }
 
   // Reads bits from the input and writes their ascii codes to the output 
   // file.
   public void translate(BitInputStream input, PrintStream output) {
      HuffmanNode rootTemp = root;
      while(input.hasNextBit()) {
         int bit = input.nextBit();
         if(bit == 0) {
            rootTemp = rootTemp.left;
         } else {
            rootTemp = rootTemp.right;
         }
         if(rootTemp.left == null && rootTemp.right == null) {
            output.write(rootTemp.asciiValue);
            rootTemp = root;
         }
      }
   }
 
   // Creates a HuffmanNode used by the HuffmanCode class. 
   // Can compare the frequencies of 2 nodes. 
   private static class HuffmanNode implements Comparable<HuffmanNode> {
      public int asciiValue;
      public HuffmanNode left;
      public HuffmanNode right;
      public int frequency;
 
      // Constructs HuffmanNode with ascii values of characters and frequency.
      public HuffmanNode(int value, int frequency) {
         this.asciiValue = value;
         this.frequency = frequency;
         this.left = null;
         this.right = null;
      }
 
      // Constructs HuffmanNode object from frequency and 2 nodes.
      public HuffmanNode(int frequency, HuffmanNode left, HuffmanNode right) {
         this.frequency = frequency;
         this.left = left;
         this.right = right;
         int asciiValue = -1;
      }
 
      // Compares the frequency of this HuffmanNode with the frequency of the 
      // passed HuffmanNode. 
      // Returns the difference between the frequencies. 
      // Positive if this node's frequency is larger than the other node.
      public int compareTo(HuffmanNode other) {
         return this.frequency - other.frequency;
      }
   }
}