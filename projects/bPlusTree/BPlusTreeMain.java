public class BPlusTreeMain {

  public static void main(String[] args){

    BPlusTree tree = new BPlusTree(32);

    for(int i = 0; i < 1000; i++){

      tree.add((int)(Math.random()*10000));
    }

    tree.print();
    System.out.println("\n");

    BPlusTree.processFile("morecommands.txt");
    System.out.println("\n");

    BPlusTree.processFile("commands.txt");
    
  }

}