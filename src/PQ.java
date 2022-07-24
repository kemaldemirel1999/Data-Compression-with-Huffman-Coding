public class PQ 
{
	private static final int CAPACITY = 1000000;
	public int size = 0;
	public TreeNode[] data;
	
	public PQ() {	data = new TreeNode[CAPACITY]; }
	public int getSize() {	return size;}
	public boolean isEmpty() {	return size == 0;}
	public boolean isFull() {	return data.length == size;}
	public int getLeftChildIndex(int parentIndex) {	return parentIndex*2 + 1;}
	public int getRightChildIndex(int parentIndex) {	return parentIndex*2 + 2;}
	public int getParentIndex(int childIndex) {	return (childIndex-1)/2;}
	private boolean hasLeftChild(int index) {	return getLeftChildIndex(index) < size ;}
	private boolean hasRightChild(int index) {	return getRightChildIndex(index) < size ;}
	private boolean hasParent(int index) {	return getParentIndex(index) >= 0	;}
	private TreeNode leftChild(int index) {	return data[getLeftChildIndex(index)];}
	private TreeNode parent(int index) {	return data[getParentIndex(index)];}
	private TreeNode rightChild(int index) {	return data[getRightChildIndex(index)];}
	public boolean isRightChild(int index)
	{
		int parentIndex = getParentIndex(index);
		if(getRightChildIndex(parentIndex)>0 && getRightChildIndex(parentIndex) == index)
			return true;
		return false;
	}
	public boolean isLeftChild(int index)
	{
		int parentIndex = getParentIndex(index);
		if(getLeftChildIndex(parentIndex)>0 && getLeftChildIndex(parentIndex) == index)
			return true;
		return false;
	}
	public boolean isInternal(int index)
	{
		if(hasLeftChild(index) || hasRightChild(index))
			return true;
		return false;
	}
	public boolean isExternal(int index)
	{
		if(!hasLeftChild(index) && !hasRightChild(index))
			return true;
		return false;
	}
	public void swap(int index1, int index2)
	{
		TreeNode temp = data[index2];
		data[index2] = data[index1];
		data[index1] = temp;
	}
	public TreeNode min()
	{
		if(isEmpty())	return null;
		return data[0];
	}
	public TreeNode removeMin(PQ minHeap)
	{
		if(isEmpty())	return null;
		TreeNode answer = data[0];
		data[0] = data[size -1];
		size --;
		minHeapify(minHeap,0);
		return answer;
	}
	public void minHeapify(PQ minHeap,int idx)
	{
		int smallest = 	idx;
		int left = 2*idx +1;
		int right = 2*idx +2;
		if(left < minHeap.size && (minHeap.data[left].getFrequency() < minHeap.data[smallest].getFrequency() || minHeap.data[left].getFrequency() == minHeap.data[smallest].getFrequency()))
		{
			if( minHeap.data[left].getFrequency() == minHeap.data[smallest].getFrequency() )
			{
				if((int)minHeap.data[left].getElement() < (int)minHeap.data[smallest].getElement())
				{
					smallest = left;
				}
			}
			else 
			{
				smallest = left;
			}
		}
		if(right < minHeap.size && (minHeap.data[right].getFrequency() < minHeap.data[smallest].getFrequency() || minHeap.data[right].getFrequency() == minHeap.data[smallest].getFrequency()))
		{
			if( minHeap.data[right].getFrequency() == minHeap.data[smallest].getFrequency() )
			{
				if((int)minHeap.data[right].getElement() < (int)minHeap.data[smallest].getElement())
				{
					smallest = right;
				}
			}
			else 
			{
				smallest = right;
			}
		}
		if(smallest != idx)
		{
			swap(smallest,idx);
			minHeapify(minHeap,smallest);
		}
	}
	public void add(TreeNode e)	throws IllegalStateException
	{
		if(isFull())	throw new IllegalStateException();
		size ++ ;
		int i = size -1;
		while( i!=0 && e.getFrequency() < data[(i-1)/2].getFrequency())
		{
			data[i] = data[(i-1)/2];
			i = (i-1)/2;
		}
		data[i] = e;
	}
	public void add(PQ P,TreeNode e)
	{
		P.size ++ ;
		int i = P.size -1;
		while( i!=0 && (e.getFrequency() < P.data[(i-1)/2].getFrequency() || e.getFrequency() == P.data[(i-1)/2].getFrequency()))
		{
			if( e.getFrequency() == P.data[(i-1)/2].getFrequency() )
			{
				if(e.getElement() < P.data[(i-1)/2].getElement())
				{
					P.data[i] = P.data[(i-1)/2];
					i = (i-1)/2;
					continue;
				}
			}
			P.data[i] = P.data[(i-1)/2];
			i = (i-1)/2;
		}
		P.data[i] = e;
	}
}
