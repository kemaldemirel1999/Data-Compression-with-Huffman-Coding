import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class HuffCode
{
	private static PQ Pq = new PQ();
	private static TreeNode Root;
	private static ArrayList<String> coding = new ArrayList<String>();
	private static int NUMBER_OF_CHARACTER;
	
	public static void main(String[] args)
	{
		if(args[0].equals("encode"))
		{
			encode(args[1]);
		}
		else if(args[0].equals("decode"))
		{
			decode(args[1]);
		}
		else
		{
			System.out.println("Hata!!! encode veya ecode oldugunu belirtiniz.");
			return;
		}
	}
	public static void encodeHuffman(TreeNode root, String str, Map<Character,String> huffmanCode)
	{
		if (root == null)
			return;
		if (root.getLeft() == null && root.getRight() == null) 
		{
			huffmanCode.put(root.getElement(), str);
		}
		encodeHuffman(root.getLeft(), str + '0', huffmanCode);
		encodeHuffman(root.getRight(), str + '1', huffmanCode);
	}
	public static String setExtraZeros(String string,int num)
	{
		switch(num)
		{
		case 0:
			string = "000" + string.substring(3);
		break;
		case 1:
			string = "001" + string.substring(3);
		break;
		case 2:
			string = "010" + string.substring(3);
		break;
		case 3:
			string = "011" + string.substring(3);
		break;
		case 4:
			string = "100" + string.substring(3);
		break;
		case 5:
			string = "101" + string.substring(3);
		break;
		case 6:
			string = "110" + string.substring(3);
		break;
		case 7:
			string = "111" + string.substring(3);
		break;
		}
		return string;
	}
	public static void writingTextFile(String decodingCode,Map<String,Character> informations,String newFileName)
	{
		String printingString = "";
		int i=0;
		String temp = "";
		
		for(char c : decodingCode.toCharArray())
		{
			temp = temp + c;
			if(informations.get(temp) != null)
			{
				char character = informations.get(temp);
				temp = "";
				printingString += character;
			}
		}
		PrintWriter outputStream = null;
		try
		{
			outputStream  = new PrintWriter(new FileOutputStream(newFileName));
			outputStream.println(printingString);
			outputStream.close();
			File deletingFile = new File(newFileName +".huff");
			if(deletingFile.delete())
			{
				System.out.println("Silindi");
			}
			else
			{
				System.out.println("Silinmedi");
			}
		}catch(Exception e)
		{
			e.printStackTrace();;
		}
	}
	public static void writingBinaryFile(String filename,ArrayList<String> rows)
	{
		FileOutputStream outputStream = null;
		String text = "";
		for(String s : rows)
		{
			text += s;
		}
		Map<Character,String> huffmanCode = new HashMap<Character,String>();
		encodeHuffman(Root,"",huffmanCode);
		String sb = "";
		for(char c : text.toCharArray())
		{
			if(huffmanCode.get(c) == null)	continue;
			sb += huffmanCode.get(c);
		}
		int numberOfExtraZeros = 0;
		sb = "000" + sb;
		while(sb.length() %8 != 0)
		{
			sb = sb + "0";
			numberOfExtraZeros ++;
		}
		sb = setExtraZeros(sb,numberOfExtraZeros);
		byte[] byte_array = new byte[(sb.length()/8)];
		String temp = sb;
		for(int i=0; i<sb.length() ;i=i+8)
		{
			String temp2 = temp.substring(i,i+8);
			byte temp_byte = (byte)Integer.parseInt(temp2,2);
			byte_array[i/8] = temp_byte;
		}
		System.out.println();
		byte[] coding_array = new byte[coding.size()*3];
		NUMBER_OF_CHARACTER = coding.size();
		String adding_number_of_character = Integer.toBinaryString(NUMBER_OF_CHARACTER);
		while(adding_number_of_character.length()<8)	adding_number_of_character = "0" + adding_number_of_character;
		for(int i=0; i<coding_array.length; i=i+3)
		{
			String k = coding.get(i/3);
			String asc2 = Integer.toBinaryString((int)k.charAt(0));
			String adding_code = k.substring(k.lastIndexOf(":")+1);
			int meanLength = adding_code.length();
			String meaningfulCodeBorder = Integer.toBinaryString(meanLength);
			while(asc2.length()<8)	asc2 = "0"+asc2;
			while(adding_code.length()<8)	adding_code = adding_code + "0";
			byte temp1 = (byte)Integer.parseInt(asc2,2);
			byte temp2 = (byte)Integer.parseInt(adding_code,2);
			byte temp3 = (byte)Integer.parseInt(meaningfulCodeBorder,2);
			coding_array[i] = temp1;
			coding_array[i+1] = temp2;
			coding_array[i+2] = temp3;
		}
		byte ch_byte = (byte)Integer.parseInt(adding_number_of_character,2);
		byte[] final_byte_array = new byte[coding_array.length + byte_array.length +  1];
		int sz = 0;
		final_byte_array[sz++] = ch_byte;
		for(int i=0; i<coding_array.length; i++)
		{
			final_byte_array[sz++] = coding_array[i];
		}
		for(int i=0; i<byte_array.length; i++)
		{
			final_byte_array[sz++] = byte_array[i];
		}
	
	//	System.out.println();
	//	System.out.println("\nEncoded string is : " + sb);
		try
		{
			outputStream = new FileOutputStream(filename + ".huff");
			outputStream.write(final_byte_array);
			outputStream.close();
			File file = new File(filename);
			if(file.delete())
			{
	//			System.out.println("Silme basarili");
			}
			else	
			{
		//		System.out.println("Silme basarisiz");
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	public static void encode(String filename)
	{
		ArrayList<String> rows = new ArrayList<String>();
		ArrayList<String> data = new ArrayList<String>();
		Scanner keyboard = null;
		try
		{
			keyboard = new Scanner(new FileInputStream(filename));
			while(keyboard.hasNextLine())
			{
				rows.add(keyboard.nextLine());
			}
			for(int co=0 ; co<=126; co++)
			{
				char i = (char)co;
				for(int j=0; j<rows.size(); j++)
				{
					String temp = rows.get(j);
					int counter = 0;
					for(int k=0; k <temp.length(); k++)
					{
						char c = temp.charAt(k);
						if( i == c)	
							counter ++;
					}
					if(counter != 0)
					{
						String s = "";
						s = s + i + "-" + counter;
						data.add(s);
						sort(data);
					}
				}
			}
			for(int l=0; l<data.size(); l++)
			{
				Pq.add(new TreeNode(data.get(l).charAt(0), Integer.parseInt(data.get(l).substring(data.get(l).lastIndexOf("-") +1))));
			}
			Root = buildHuffmanTree();
			Root.setElement('*');
			int[] arr = new int[2 * data.size() + 1];
			markTree(Root,arr,0);
			//preorderTraverse(Root);
			keyboard.close();
			writingBinaryFile(filename,rows);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public static int getASC2(String sk)
	{
		int number_of_character  = 0;
		for(int i=0,l=7; i<8 && l>=0; i++,l--)
		{
			if(sk.charAt(l) == '0') continue;
			number_of_character += Math.pow(2, i);
		}
		return number_of_character;
	}
	public static int getNumZero(String sk)
	{
		int number_of_character  = 0;
		for(int i=0,l=2; i<3 && l>=0; i++,l--)
		{
			if(sk.charAt(l) == '0') continue;
			number_of_character += Math.pow(2, i);
		}
		return number_of_character;
	}
	public static void decode(String decodingFilename)//ilk3 bit kac tane sona sifir eklendigini belirtir.
	{
		String newFileName = decodingFilename.substring(0, decodingFilename.length() -5);
		FileInputStream inputStream = null;
		try
		{
			inputStream  = new FileInputStream(decodingFilename);
			byte[] bytes = inputStream.readAllBytes();
			inputStream.close();
		//	System.out.println("decoding code");
			String my_bytes = "";
			/*for(int i=0; i<bytes.length; i++)
			{
				System.out.print(bytes[i] + "  ");
			}*/
		//	System.out.println();
			for(int i=0; i<bytes.length; i++)
			{
				String temp = Integer.toBinaryString(bytes[i] & 0xFF);
				while(temp.length()!=8)	temp = "0" + temp;
				my_bytes += temp;
				//System.out.print(temp+"   ");
			}
			Map<String,Character> extractingHuffmanCode = new HashMap<String,Character>();
			int number_of_character=0;
			String sk = my_bytes.substring(0,8);
			my_bytes = my_bytes.substring(8);
			for(int i=0,l=7; i<8 && l>=0; i++,l--)
			{
				if(sk.charAt(l) == '0') continue;
				number_of_character += Math.pow(2, i);
			}
		//	System.out.println("number_of_character = "+number_of_character);
			for(int i=1; i<=number_of_character; i++)
			{
				String char_string = my_bytes.substring(0,24);
				my_bytes = my_bytes.substring(24);
				int asc2_number = getASC2(char_string.substring(0,8));
				String code = char_string.substring(8,16);
				char new_char = getASC2Char(asc2_number);
				String mC = char_string.substring(16);
				int meanCodeNumber = getASC2(mC);
				String final_adding_string = "";
				for(int x=0; x<meanCodeNumber; x++)
				{
					final_adding_string += code.charAt(x);
				}
		//		System.out.print("char:"+asc2_number+":"+new_char+","+ meanCodeNumber+", "+final_adding_string+"|");
				extractingHuffmanCode.put(final_adding_string, new_char);
			}
			int extractingZero = getNumZero(my_bytes.substring(0,3));
		//	System.out.println("Extracting zero:"+extractingZero);
			my_bytes = my_bytes.substring(3,my_bytes.length() - extractingZero);
		//	System.out.println("DEcoding string length:"+my_bytes.length());
			inputStream.close();
			writingTextFile(my_bytes , extractingHuffmanCode,newFileName);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static char getASC2Char(int number)
	{
		for(int i=0; i<126; i++ )
		{
			char c = (char)i;
			if(i == number)	return c;
		}
		return '0';
	}
	public static void sort(ArrayList<String> unsortedList)
	{
		for(int i=0; i<unsortedList.size(); i++)
		{
			for(int j=0; j<unsortedList.size() -i -1; j++)
			{
				int first = Integer.parseInt(unsortedList.get(j).substring(unsortedList.get(j).lastIndexOf("-") +1));
				int second = Integer.parseInt(unsortedList.get(j+1).substring(unsortedList.get(j+1).lastIndexOf("-") +1));
				if(first > second)
				{
					String temp = unsortedList.get(j);
					unsortedList.set(j, unsortedList.get(j+1));
					unsortedList.set(j+1, temp);
				}
			}
		}
	}
	public static TreeNode buildHuffmanTree()
	{
		TreeNode left,right,top;
		while( Pq.size != 1)
		{
			left = Pq.removeMin(Pq);
			right = Pq.removeMin(Pq);
			top = new TreeNode(left.getFrequency() + right.getFrequency());
			top.setLeft(left);
			top.setRight(right);
			Pq.add(Pq,top);
		}
		return Pq.removeMin(Pq);
	}
	public static void markTree(TreeNode node,int[] array,int cur)
	{
		if(node.hasLeft())
		{
			array[cur] = 0;
			markTree(node.getLeft(),array,cur+1);
		}
		if(node.hasRight())
		{
			array[cur] = 1;
			markTree(node.getRight(),array,cur+1);
		}
		if(node.isExternal())
		{
			String s = "";
			for(int i=0; i<cur; i++)
			{
				s = s + array[i];
			}
			node.setMarkNumber(s);
			String addingString = node.getElement() + ":" + node.getMarkNumber();
			coding.add(addingString);
		}
	}
	public static void preorderTraverse(TreeNode node)
	{
		if(node == null)	return;
		System.out.println(node.getElement() + ":" + node.getFrequency()+"-"+node.getMarkNumber());
		preorderTraverse(node.getLeft());
		preorderTraverse(node.getRight());
	}
}
