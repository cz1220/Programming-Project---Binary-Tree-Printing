package printTree;


import net.datastructures.*;
import java.util.Comparator;


public class MyAVLTreeMap<K,V> extends TreeMap<K,V> {
	
  /** Constructs an empty map using the natural ordering of keys. */
  public MyAVLTreeMap() { super(); }

  /**
   * Constructs an empty map using the given comparator to order keys.
   * @param comp comparator defining the order of keys in the map
   */
  public MyAVLTreeMap(Comparator<K> comp) { super(comp); }

  /** Returns the height of the given tree position. */
  protected int height(Position<Entry<K,V>> p) {
    return tree.getAux(p);
  }

  /** Recomputes the height of the given position based on its children's heights. */
  protected void recomputeHeight(Position<Entry<K,V>> p) {
    tree.setAux(p, 1 + Math.max(height(left(p)), height(right(p))));
  }

  /** Returns whether a position has balance factor between -1 and 1 inclusive. */
  protected boolean isBalanced(Position<Entry<K,V>> p) {
    return Math.abs(height(left(p)) - height(right(p))) <= 1;
  }

  /** Returns a child of p with height no smaller than that of the other child. */
  protected Position<Entry<K,V>> tallerChild(Position<Entry<K,V>> p) {
    if (height(left(p)) > height(right(p))) return left(p);     // clear winner
    if (height(left(p)) < height(right(p))) return right(p);    // clear winner
    // equal height children; break tie while matching parent's orientation
    if (isRoot(p)) return left(p);                 // choice is irrelevant
    if (p == left(parent(p))) return left(p);      // return aligned child
    else return right(p);
  }

  /**
   * Utility used to rebalance after an insert or removal operation. This traverses the
   * path upward from p, performing a trinode restructuring when imbalance is found,
   * continuing until balance is restored.
   */
  protected void rebalance(Position<Entry<K,V>> p) {
    int oldHeight, newHeight;
    do {
      oldHeight = height(p);                       // not yet recalculated if internal
      if (!isBalanced(p)) {                        // imbalance detected
        // perform trinode restructuring, setting p to resulting root,
        // and recompute new local heights after the restructuring
        p = restructure(tallerChild(tallerChild(p)));
        recomputeHeight(left(p));
        recomputeHeight(right(p));
      }
      recomputeHeight(p);
      newHeight = height(p);
      p = parent(p);
    } while (oldHeight != newHeight && p != null);
  }

  /** Overrides the TreeMap rebalancing hook that is called after an insertion. */
  @Override
  protected void rebalanceInsert(Position<Entry<K,V>> p) {
    rebalance(p);
  }

  /** Overrides the TreeMap rebalancing hook that is called after a deletion. */
  @Override
  protected void rebalanceDelete(Position<Entry<K,V>> p) {
    if (!isRoot(p))
      rebalance(parent(p));
  }

  /** Ensure that current tree structure is valid AVL (for debug use only). */
  @SuppressWarnings("unused")
private boolean sanityCheck() {
    for (Position<Entry<K,V>> p : tree.positions()) {
      if (isInternal(p)) {
        if (p.getElement() == null)
          System.out.println("VIOLATION: Internal node has null entry");
        else if (height(p) != 1 + Math.max(height(left(p)), height(right(p)))) {
          System.out.println("VIOLATION: AVL unbalanced node with key " + p.getElement().getKey());
          dump();
          return false;
        }
      }
    }
    return true;
  }
  
  public void printTree() {
	  String [][]location = new String[100][100]; 
	  Position<Entry<K,V>> root = this.root();
	  K rootKey = root.getElement().getKey(); 
	  //2 to the 5th power. To make sure that we get whole number when we divide by 2 every single time
	  location[0][32] = (String) rootKey; 
	  int brenchSize = 16; 
	  int row = 0; 
	  int column = 32; 
	  printTree1(location, row ,column,  brenchSize,root); 
	  
	  
	  for (int i = 0;  i<location.length; i ++ ) {
		  for (int j = 0; j <location[i].length; j++) {
			  if (location[i][j] != null) {
			  System.out.print(location[i][j]);
			  }
			  
			  else {
				  System.out.print(" "); 
			  }
			  
			  
		  }
		  System.out.println("");
	  }
	
	  
  }
  public void printTree1(String[][]location, int row, int column, int brenchSize,  Position<Entry<K,V>> parent) {
	  Position<Entry<K,V>> p = parent; 
	  //if p has no child, reach the leave, return. 
	  if (left(p).getElement() == null && right(p).getElement()== null) {
		  return; 
	  }
		  
	
	  if(left(p).getElement() != null) {
		  Position<Entry<K,V>> leftchild = left(p);
		  K leftchildkey = left(p).getElement().getKey(); 
		  location[row+2][column - brenchSize/2] = "/"; 
		  location[row+4][column - brenchSize] =  leftchildkey.toString(); 
		  printTree1(location, row+4, column - brenchSize , brenchSize/2, leftchild); 
	  }
	
	  if(right(p).getElement() != null) {
		  Position<Entry<K,V>> rightchild = right(p); 
		  K rightchildkey = rightchild.getElement().getKey(); 
		  location[row+2][column + brenchSize/2] = "\\"; 
		  location[row+4][column + brenchSize] =  rightchildkey.toString();

		  printTree1(location, row+4, column +brenchSize, brenchSize/2, rightchild);
	  }

  }
}