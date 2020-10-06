import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.ListIterator;
import java.util.LinkedList;

public class BPlusTree{

/**

Nathan Hickey
CISC 450
Prof. Sawin
December 12th, 2019

This class implements a BPlusTree with nodes of given size n.
	
Methods:
  private int getDegree()
    Private method to return the degree of the tree.

  private void setRoot()
    Private method to set the root.

  public void find(int value)
    Searches for an int in the tree and prints the result of the search to the console.

  private Node privFind(int value)
    Private method to search the tree for a value. If found returns null. If not found, returns the node it should go into.

  public boolean add(int value)
    Adds a integer to the tree. Automatically handles all splitting and placement.

  public static BPlusTree processFile(String fName)
    Process a file and creates output to the terminal based on the commands in the file. Returns the tree created by the file.

  public void print()
    Prints the tree to the console. 

Modification History:
  December 12th, 2019
    Original version.

  December 13th, 2019
    Cleaned up code formatting. Added comments.

*/

  int degree;
  Node root;
  // Instance variables.

  public BPlusTree(final int degree){
    // Constructor takes given degree as an int.

    if(degree < 3 || degree > 100){
      // Error checking degree.
      
      throw new IllegalArgumentException(getClass().getName() + ".constructor: Degree of tree must be between 3 and 100 inclusive.");
    }

    this.degree = degree;
    setRoot(null);
    // Set instance variables.
  }

  private int getDegree(){return this.degree;}
  // Access to degree.

  private void setRoot(final Node newRoot){this.root = newRoot;}
  // Set root.

  public void find(final int value){
    // Searches for an int in the tree and prints the result to the console.

    if(privFind(value) == null){System.out.println(value + " FOUND");}
    else{System.out.println(value + " NOT FOUND");}
  }

  private Node privFind(final int value){
    // Private search. Returns null if it finds the value in the tree.
    // If value is not found, returns the node the value would go into.

    Node cNode = root;
    ListIterator<NodeElem> ite = cNode.items.listIterator();
    NodeElem hold = cNode.items.get(0);
    NodeElem last = cNode.items.get(1);
    boolean first = true;
    boolean found = false;
    // Local variables.


    while(cNode.isLeaf() == false){
      // While current node is not a leaf.

      found = false;
      // Boolean to break out of inner loop.

      while(ite.hasNext() && found == false){
        // While the iterator has a next value and we have not found the right place.
        
        hold = ite.next();
        // Get the next item.

        if(hold.isValue() && value > last.value() && value <= hold.value()){
          // If place is found between two values in the list.
          // Follow pointer to next node down the tree.

          ite.previous();
          cNode = ite.previous().next();
          ite = cNode.items.listIterator();
          found = true;
        }
        else if(hold.isValue() && first == true && value <= hold.value()){
          // Edge case for less than the first item.
          // Only needs to be less that the first item.

          ite.previous();
          cNode = ite.previous().next();
          ite = cNode.items.listIterator();
          found = true;
        }
        else if(hold.isValue()){
          // After first pass, first is false.

          first = false;
          last = hold;
        }
        
      }

      if(found == false){
        // Hit the end of the list.
        // Grab node off last pointer, reset booleans.

        cNode = hold.next();
        ite = cNode.items.listIterator();
        found = false;
        first = true;
      }

    }

    while(ite.hasNext()){
      // We are now on a leaf. See if the value is in the leaf.
      // Yes, return null. No, return the node it would go into.

      hold = ite.next();
      if(hold.isValue() && hold.value() == value){
        return null;
      }
    }

    return cNode;
  }

  public boolean add(final int value){
    // Add a value to the tree.

    if(root == null){
      // If root is null, create and add the value.

      root = new Node();
      root.isLeaf = true;
      root.items.add(new Pointer(null));
      root.items.add(new Value(value));
      root.items.add(new Pointer(null));
      return true;
    }

    final Node place = privFind(value);
    // Get the node where the value would go.

    if(place == null){return false;}
    // If node returned is null, the value is already in the tree, so we ignore and return false.

    place.add(value, null, false);
    // Otherwise, add into the given node and return true.

    return true;
  }

  public static BPlusTree processFile(final String fName){
    // Reads a file from a given string and performs the operations prescribed within.
    // Degree must be the first line of text in the file.
    // All commands must be a character followed by an integer.
    // Commands are p for print, i for add integer, and s for search in tree.

    BPlusTree readTree = null;
    final File source = new File(fName);
    final ArrayList<String> contents = new ArrayList<String>();
    String record;
    // Local variables.

    try{
      // Try to read file.

      final BufferedReader in = new BufferedReader(new FileReader(source));
    
      record = in.readLine();

      while(record != null){
        contents.add(record);
        record = in.readLine();
      }

      in.close();

    }
    catch(final IOException ioe){
      // Catch exception and inform  the user.

      System.out.println("Cannot read given file name: " + source);
      return null;
    }

    if(contents.size() == 0){
      // If file contains no commands, inform the user.

      System.out.println("Given file is empty.");
      return null;
    }

    String order = "";
    int value = 0;
    String command = contents.get(value);
    // Local variables.

    while(command.trim().length() < 1){
      // Remove whitespace from top of file.

      value++;
      command = contents.get(value);
    }

    try{
      // Try to parse the first line into an int and create the tree.

      readTree = new BPlusTree(Integer.parseInt(command)); 
    }
    catch(final NumberFormatException nfe){
      // If fails inform the user.

      throw new RuntimeException("Degree of tree must be first command in file.");
    }

    for(int i = value + 1; i < contents.size(); i++){
      // For each line in the file, parse the command and execute it.
      // For invalid commands, inform the user.

      command = contents.get(i);

      command.trim();
      order = command.substring(0,1);
      try{
      value = Integer.parseInt(command.substring(1).trim());
      }catch(final Exception e){}

      if(order.equals("p")){readTree.print();}
      else if(order.equals("i")){readTree.add(value);}
      else if(order.equals("s")){readTree.find(value);}
      else{System.out.println("INVALID COMMAND FORMAT");}
    }

    return readTree;
    // Return the tree created by the file.
  }

  public void print(){
    // Prints the tree.

    System.out.println("PRINTING TREE");

    Queue<Node> cQueue = new LinkedList<Node>();
    Queue<Node> nQueue = new LinkedList<Node>();
    StringBuilder output = new StringBuilder();
    // Local variables.

    if(root == null){
      // Special case for null root.

      System.out.println("#");
      return;
    }

    cQueue.offer(root);
    boolean onLeaf = root.isLeaf();
    Node hold = root;
    NodeElem elem = root.items.get(0);
    ListIterator<NodeElem> ite = hold.items.listIterator();
    // Local variables.

    while(onLeaf == false){
      // While not currently on a leaf.
  
      while(cQueue.isEmpty() == false){
        // While node Queue is not empty.

        hold = cQueue.remove();
        ite = hold.items.listIterator();
        // Remove node and create iterator.


        while(ite.hasNext()){
          // While not at the end.
          
          elem = ite.next();
          // Grab the next element.

          if(elem.isValue()){output.append(elem.value() + ", ");}
          if(elem.isValue() == false){nQueue.offer(elem.next());}
          // If Value, add it to the StringBuilder.
          // If Pointer, add the child to a new Queue.
        }

        output.deleteCharAt(output.length() - 2);
        output.append("# ");
        // End of node. Remove trailing comma and add a pound symbol.

      }
      
      output.delete(output.length() - 2, output.length());
      System.out.println(output.toString());
      // End of queue. Remove the trailing pound symbol.
      // Print the StringBuilder.
  
      output = new StringBuilder();
      cQueue = nQueue;
      nQueue = new LinkedList<Node>();
      // Reset for next layer of tree.

      if(cQueue.peek().isLeaf()){onLeaf = true;}
      // If we are at the leaves, set onLeaf to true;
    }

    hold = cQueue.remove();
    // Grab first leaf.

    ite = hold.items.listIterator();
    boolean finished = false;
    // Grab first item and set our finished boolean.

    while(finished == false){
      // While not done with the leaves, get the next item, print if a value.

      elem = ite.next();

      if(elem.isValue()){output.append(elem.value() + ", ");}
      
      if(ite.hasNext() == false){
        // At the end of the iterator.

        if(elem.next() != null){
          // If not done with all the leaves.
          // Get the next leaf node, remove the comma, add a pound symbol.
      
          ite = elem.next().items.listIterator();
          output.deleteCharAt(output.length() - 2);
          output.append("# ");
        }
        else{finished = true;}
        // Set finished if on the last leaf.

      }
    }

    output.delete(output.length() - 2, output.length());
    System.out.println(output.toString());
    // Remove trailing comma and print.
  }

  private class Node{

    /*

    This class implements a node object of size n to be used in a BPlusTree object.

    Methods:
      boolean isLeaf()
        Returns true if the node is a leaf.

      boolean add(int value, Node pointer, boolean valLeft)
        Adds an int to the Node. Uses valLeft to handle a first item exception for splitting. Handles splitting and placement of new value and pointer.

      boolean split()
        Splits the node and handles all the necessary parts of passing it up the tree.

    */
    // Node class.

    ArrayList<NodeElem> items;
    Node parent;
    int capacity;
    boolean isLeaf;
    // Instance variables.

    Node(){
      // Constructor gets degree from outer class, initializes values.

      this.items = new ArrayList<NodeElem>();
      this.parent = null;
      this.capacity = (getDegree()*2) + 1;
      this.isLeaf = false;
    }

    Node(final ArrayList<NodeElem> items){
      // Second constructor if given a NodeElem array.
      this();
      this.items = items;
    }

    boolean isLeaf(){return this.isLeaf;}
    // Returns if the Node is a leaf.

    boolean add(final int value, final Node pointer, final boolean valLeft){
      // Adds a value to the Node. Calls split if this fills the node.
      // Boolean valLeft allows the method to handle a splitting exception.

      boolean added = false;
      final Value newVal = new Value(value);
      final Pointer newPoint = new Pointer(pointer);
      NodeElem hold = items.get(0);
      // Local variables.

      int i = 1;

      while(added == false && i < items.size()){
        // If not done and there are still items in the ArrayList.

        hold = items.get(i);
        // Get the next item.

        if(hold.isValue() && hold.value() > value){
          // If item is greater than the value we're adding, add before it.

          if(valLeft){
            // If doing the splitting exception, add between the held value and it's pointer.

            items.add(i, newPoint);
            items.add(i, newVal);
          }
          else{
            // Else add before the pointer.

            items.add(i-1, newVal);
            items.add(i-1, newPoint);
          }
          added = true;
        }

        i++;
      }

      if(added == false){
        // If we hit the end and have not added, add it at the end.
        
        items.add(items.size(), newVal);
        items.add(items.size(), newPoint);
        added = true;
      }

      if(items.size() == capacity){added = split();}
      // Check our new size. If we are at capacity, call split.

      return added;
    }

    boolean split(){
      // Splits a node and handles all the necessary operations involved in splitting.

      final ArrayList<NodeElem> left = new ArrayList<NodeElem>();
      final ArrayList<NodeElem> right = new ArrayList<NodeElem>();
      int pad = 0;
      if(getDegree()%2 == 0){pad = -1;}
      final int splitIndex = items.size()/2 + pad;
      final int splitValue = items.get(splitIndex).value();
      // Local variables.

      for(int i = splitIndex + 1; i < items.size(); i++){right.add(items.get(i));}
      // Copy right side items into new ArrayList.

      final Node rNode = new Node(right);
      // Create new right node.

      if(isLeaf()){
        // If we are splitting a leaf.

        rNode.isLeaf = true;
        // Set the leaf boolean on the new node.

        for(int i = 0; i <= splitIndex; i++){left.add(items.get(i));}
        left.add(new Pointer(rNode));
        // Copy the center split value as well into the left ArrayList, and add a pointer at the end that points to the new right node.
      }
      else{
        // Not a leaf.

        for(int i = 0; i < splitIndex; i++){left.add(items.get(i));}
        // Copy the left items into a new ArrayList without the center value.

        Node child = null;
        // Local variable.

        for(int i = 0; i < rNode.items.size(); i+=2){
          // Set children for new parent node.

          child = rNode.items.get(i).next();
          if(child != null){child.parent = rNode;}
        }

      }

      this.items = left;
      // Set new item ArrayList on the original node.

      if(parent == null){
        // We are splitting root. Create a new root and set it's values accordingly.

        final ArrayList<NodeElem> rList = new ArrayList<NodeElem>();
        rList.add(new Pointer(this));
        rList.add(new Value(splitValue));
        rList.add(new Pointer(rNode));
        final Node newRoot = new Node(rList);
        this.parent = newRoot;
        rNode.parent = newRoot;
        setRoot(newRoot);
      }
      else{
        // Set the parent for the new node, add the new node to the parent using the valLeft flag.

        rNode.parent = this.parent;
        parent.add(splitValue, rNode, true);
      }

      return true;
    }

  } // class Node

  private class Value implements NodeElem{
    // Wrapper class for int values. Uses the NodeElem interface for all methods.

    int value;

    Value(final int value){
      this.value = value;
    }

    public boolean isValue() {return true;}

    public Node next(){
      // Throw exceptions if treated as a Pointer.
      throw new RuntimeException("Next called on a value: " + value());
    }

    public int value(){return this.value;}

  } // class Value

  private class Pointer implements NodeElem{
    // Wrapper class for Node pointers. Uses the NodeElem interface for all methods.

    Node next;

    Pointer(final Node point){
      this.next = point;
    }

    public boolean isValue(){return false;}

    public Node next(){return this.next;}

    public int value(){
      // Throw exceptions if treated as a Value.
      throw new RuntimeException("Value called on a pointer: " + next().toString());
    }

  } // class Pointer

  private interface NodeElem{
    // Interface that allows for Value objects and Pointer objects to live in the same array.

    boolean isValue();

    Node next();

    int value();

  } // interface NodeElem

} // class BPlusTree