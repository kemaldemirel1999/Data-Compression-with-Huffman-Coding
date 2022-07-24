public class TreeNode
{
	private char element;
	private int frequency;
	private String markNumber;
	private TreeNode left,right;
	
	public TreeNode(char element,int frequency) 
	{	
		this.element = element;	
		this.frequency = frequency;
		right = null;
		left = null;
		markNumber = "";
	}
	public TreeNode(int frequency) 
	{	
		this.element = '$';	
		this.frequency = frequency;
		right = null;
		left = null;
		markNumber = "";
	}
	public boolean isExternal()
	{
		if(left == null && right== null)	return true;
		return false;
	}
	public boolean hasLeft() {return left!=null;}
	public boolean hasRight() {return right!=null;}
	public TreeNode getLeft() {return left;}
	public TreeNode getRight() {return right;}
	public void setLeft(TreeNode l) {left= l;}
	public void setRight(TreeNode r) {right = r;}
	public char getElement() {	return element;}
	public void setElement(char element) {this.element = element;}
	public void setFrequency(int frequency) {	this.frequency = frequency;}
	public int getFrequency() {	return frequency;}
	public String getMarkNumber() {	return markNumber;}
	public void setMarkNumber(String markNumber) {	this.markNumber = markNumber;}
}
