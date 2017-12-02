import java.awt.Component;
import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

public class WordPuzzle {
	
	int num_rows;
	int num_cols;
	
	char [][] grid;
	
	int row, col, orientation, characters;
	
	MyHashTable<String> k;	
	
	ArrayList<String> words;
	
	public WordPuzzle(int r, int c, MyHashTable<String> a) {
		
		num_rows = r;
		num_cols = c;		
		grid = new char[num_rows][num_cols];
		
		row = col = orientation = characters = 0;		
		
		k = a;
		words = new ArrayList<String>();
	}
	
	private void clearWords() {
		words.clear();
	}
	public void prefixedLoadDictionary() {
		try {
			JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory()) {
				
					   @Override
					   protected JDialog createDialog(Component parent) throws HeadlessException {
					       JDialog dialog = super.createDialog(parent);
					       dialog.setModal(true);  // set modality (or setModalityType)
					       dialog.setAlwaysOnTop(true);
					       return dialog;
					   }
					};
			

			int returnValue = jfc.showOpenDialog(null);
			// int returnValue = jfc.showSaveDialog(null);
			File selectedFile = null;
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				selectedFile = jfc.getSelectedFile();
				System.out.println(selectedFile.getAbsolutePath());
			}
			FileReader fileReader = new FileReader(selectedFile.getAbsolutePath());
			
			
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;

			while ((line = bufferedReader.readLine()) != null) {
				
				for (int i = 1; i < line.length()+1; i++) {
					
					if ( i == line.length() ) { //Inserting as a word
						if ( k.contains(line) ) {
							
							if ( k.getType(line) == 0)
								continue;
							
							else if ( k.getType(line) == 1){
								k.remove(line);
								k.insert(line, 2);
							}
							
							else
								k.insert(line, 0);
							
						}
						else
							k.insert(line, 0);
					}
					
					else {	//Inserting as a prefix
						
						if( k.contains(line.substring(0, i)) ) {
							
							if ( k.getType(line.substring(0, i)) == 0) {
								k.remove(line.substring(0, i));
								k.insert(line.substring(0, i), 2);
							}
							
							else if( k.getType(line.substring(0, i)) == -1)
								k.insert(line.substring(0, i), 1);
							
							else 
								continue;
						}
						else
							k.insert(line.substring(0, i), 1);
					}
				}
			}
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void printWords() {
		Iterator <String> itr = words.iterator();		
		System.out.println("Words found : " + words.size());		
		while(itr.hasNext()){  
		   System.out.print(itr.next() + " ");  
		  }
		System.out.println();
	}

	private String createString(int steps, int direction, int prefix) {
		
		StringBuffer temp = new StringBuffer();
		int temp_row = row;
		int temp_col = col;
		
		String a = "";
		
			if (steps == 0) {
				return "";
			}
			
			if (direction == 1) { //forward

					for (int k = 1; k <= steps; k++) {
						temp = temp.append(grid[temp_row][temp_col]);							
						temp_col = temp_col + 1;
						a = temp.toString();
					}
					
					if (prefix == 1) {
						if ( this.k.getType(a) == -1 )
							return " ";						
						else if ( this.k.getType(a) == 0 || this.k.getType(a) == 2 ) 
						{							
							if ( a.length() > 1)
								words.add(a);							
						}
						else if ( this.k.getType(a) == 1 )
							return " ";						
					}
					else {
						if ( this.k.getType(a) == 0 || this.k.getType(a) == 2 )
						{							
							if ( a.length() > 1)
								words.add(a);							
						}
					}
											
				
			}
			
			else if (direction == -1) { //backward

					for (int k = 1; k <= steps; k++) {
						temp = temp.append(grid[temp_row][temp_col]);
						temp_col--;
						a = temp.toString();
					}
						
						if (prefix == 1) {
							if ( this.k.getType(a) == -1 )
								return " ";						
							else if ( this.k.getType(a) == 0 || this.k.getType(a) == 2 )
							{							
								if ( a.length() > 1)
									words.add(a);							
							}
							else if ( this.k.getType(a) == 1 )
								return " ";						
						}
						else {
							if ( this.k.getType(a) == 0 || this.k.getType(a) == 2 )
							{							
								if ( a.length() > 1)
									words.add(a);							
							}
						}					
				
			}
			
			else if (direction == 2) { //upward

					for (int k = 1; k <= steps; k++) {
						temp = temp.append(grid[temp_row][temp_col]);						
						temp_row--;
						a = temp.toString();
					}
						
						if (prefix == 1) {
							if ( this.k.getType(a) == -1 )
								return " ";						
							else if ( this.k.getType(a) == 0 || this.k.getType(a) == 2 )
							{
								if ( a.length()>1 )
									words.add(temp.toString());
							}								
							else if ( this.k.getType(a) == 1 )
								return " ";						
						}
						else {
							if ( this.k.getType(a) == 0 || this.k.getType(a) == 2 )
							{
								if ( a.length()>1 )
									words.add(a);
							}								
						}
			}
			
			else if (direction == -2) { //downward
					for (int k = 1; k <= steps; k++) {
						temp = temp.append(grid[temp_row][temp_col]);						
						temp_row++;
						a = temp.toString();
					}
						
						if (prefix == 1) {
							if ( this.k.getType(a) == -1 )
								return " ";						
							else if ( this.k.getType(a) == 0 || this.k.getType(a) == 2 )
							{							
								if ( a.length() > 1)
									words.add(a);							
							}
							else if ( this.k.getType(a) == 1 )
								return " ";						
						}
						else {
							if ( this.k.getType(a) == 0 || this.k.getType(a) == 2 )
							{							
								if ( a.length() > 1)
									words.add(a);							
							}
						}					
			}
			
			else if (direction == 3) { //left diagonal upward
					for (int k = 1; k <= steps; k++) {
						temp = temp.append(grid[temp_row][temp_col]);
						temp_col--;				
						temp_row--;				
						a = temp.toString();
					}
						
						if (prefix == 1) {
							if ( this.k.getType(a) == -1 )
								return " ";						
							else if ( this.k.getType(a) == 0 || this.k.getType(a) == 2 )
							{							
								if ( a.length() > 1)
									words.add(a);							
							}
							else if ( this.k.getType(a) == 1 )
								return " ";						
						}
						else {
							if ( this.k.getType(a) == 0 || this.k.getType(a) == 2 )
							{							
								if ( a.length() > 1)
									words.add(a);							
							}
						}						
			}
			
			else if (direction == -3) { //left diagonal downward
					for (int k = 1; k <= steps; k++) {
						temp = temp.append(grid[temp_row][temp_col]);
						temp_col++;
						temp_row++;			
						a = temp.toString();
					}
						
						if (prefix == 1) {
							if ( this.k.getType(a) == -1 )
								return " ";						
							else if ( this.k.getType(a) == 0 || this.k.getType(a) == 2 )
							{							
								if ( a.length() > 1)
									words.add(a);							
							}
							else if ( this.k.getType(a) == 1 )
								return " ";						
						}
						else {
							if ( this.k.getType(a) == 0 || this.k.getType(a) == 2 )
							{							
								if ( a.length() > 1)
									words.add(a);							
							}
						}									
			}
			
			else if (direction == 4) { //right diagonal upward
					for (int k = 1; k <= steps; k++) {
						temp = temp.append(grid[temp_row][temp_col]);
						temp_col++;
						temp_row--;
						a = temp.toString();
					}
						
						if (prefix == 1) {
							if ( this.k.getType(a) == -1 )
								return " ";						
							else if ( this.k.getType(a) == 0 || this.k.getType(a) == 2 )
							{							
								if ( a.length() > 1)
									words.add(a);							
							}
							else if ( this.k.getType(a) == 1 )
								return " ";						
						}
						else {
							if ( this.k.getType(a) == 0 || this.k.getType(a) == 2 )
							{							
								if ( a.length() > 1)
									words.add(a);							
							}
						}								
			}
			
			else if (direction == -4) { //right diagonal downward
					for (int k = 1; k <= steps; k++) {
						temp = temp.append(grid[temp_row][temp_col]);
						temp_col--;
						temp_row++;
						a = temp.toString();
					}
						
						if (prefix == 1) {
							if ( this.k.getType(a) == -1 )
								return " ";		
							else if ( this.k.getType(a) == 0 || this.k.getType(a) == 2 )
							{							
								if ( a.length() > 1)
									words.add(a);							
							}
							else if ( this.k.getType(a) == 1 )
								return " ";						
						}
						else {
							if ( this.k.getType(a) == 0 || this.k.getType(a) == 2 )
							{							
								if ( a.length() > 1)
									words.add(a);							
							}
						}				
			}
				
		return temp.toString();
	}
				
	private void iterateGrid(int prefix) {
				
		boolean flag = true;
		
		for ( int i = 0; i < num_rows; i++) {			
			row = i;
			
			for ( int j = 0; j < num_cols; j++) {										
				col = j;
			
				orientation =  1; //forward
				for (int k =  1; k <= num_cols; k++) {
					if (j+k <= num_cols) {
						if (prefix == 1)
							createString(k, orientation, 1);
						else
							createString(k, orientation, 0);						
					}						
				}
				
				orientation = -1; //backward
				for (int k =  1; k <= num_cols; k++) {
					if (j-k >= -1) {
						if (prefix == 1)
							createString(k, orientation, 1);
						else
							createString(k, orientation, 0);
					}
				}
				
				orientation =  2; //upward
				for (int k =  1; k <= num_rows; k++) {
					if (i-k >= -1) {
						if (prefix == 1)
							createString(k, orientation, 1);
						else
							createString(k, orientation, 0);						
					}
					else
						break;
				}
				
				orientation = -2; //downward
				for (int k =  1; k <= num_rows; k++) {
					if (i+k <= num_rows) {
						if (prefix == 1)
							createString(k, orientation, 1);
						else
							createString(k, orientation, 0);						
					}						
					else 
						break;
				}
				
				orientation =  3; //left diagonal upward
				flag = true;
				int step_count = 0;
				while(flag) {
					if ( j-step_count >= -1 && i-step_count >= -1 ) {
						if (prefix == 1)
							createString(step_count, orientation, 1);
						else
							createString(step_count, orientation, 0);
						step_count++;
					}
					else {
						flag = false;
					}
				}
				
				orientation = -3; //left diagonal downward
				flag = true;
				step_count = 0;
				while(flag) {
					if ( j+step_count <= num_cols && i+step_count <= num_rows ) {
						if (prefix == 1)
							createString(step_count, orientation, 1);
						else
							createString(step_count, orientation, 0);
						step_count++;
					}
					else {
						flag = false;
					}
				}
				
				orientation =  4; //right diagonal upward
				flag = true;
				step_count = 0;
				while(flag) {
					if ( j+step_count <= num_cols && i-step_count >= -1 ) {
						if (prefix == 1)
							createString(step_count, orientation, 1);
						else
							createString(step_count, orientation, 0);
						step_count++;
					}
					else {
						flag = false;
					}
				}
				
				orientation = -4; //right diagonal downward
				flag = true;
				step_count = 0;
				while(flag) {
					if ( j-step_count >= -1 && i+step_count <= num_rows ) {
						if (prefix == 1)
							createString(step_count, orientation, 1);
						else
							createString(step_count, orientation, 0);
						step_count++;
					}
					else {
						flag = false;
					}
				}				 
			}			
		}
	}
		
	private void printGrid() {

		for ( int i = 0; i < num_rows; i++) {
			for ( int j = 0; j < num_cols; j++) {
				System.out.print(grid[i][j] + "\t"); 
			}
			System.out.println();
		}		
	}
	
	private void fillGrid(int fillRandom) {

		if ( fillRandom == 0) {

			char input;
			Scanner sc = new Scanner(System.in);
			
			for ( int i = 0; i < num_rows; i++) {
				for ( int j = 0; j < num_cols; j++) {				
					input = sc.next().charAt(0);			
					grid[i][j] = input;
				}		
			}			
			sc.close();				
		}
		else {
			for ( int i = 0; i < num_rows; i++) {
				for ( int j = 0; j < num_cols; j++) {
					char diff = 'z' - 'a';
					grid[i][j] = (char)('a' + Math.random() * diff);			
				}
			}			
		}		
	}
	
	public static void main(String args[]) {

        Scanner sc = new Scanner(System.in);
        System.out.print("Rows: ");
        int num_rows = sc.nextInt();
        System.out.print("Columns: ");
        int num_cols = sc.nextInt();

        MyHashTable<String> H = new MyHashTable<>( );      
        
		WordPuzzle w = new WordPuzzle(num_rows, num_cols, H);
		        
        w.prefixedLoadDictionary();
 
//        System.out.println(H.getType(" ")); //Un-comment and pass a string to get its type
//        0 -> Word, 1 -> Prefix, 2 -> Word&Prefix, -1 -> doesn't exist in the dictionary
        
        w.fillGrid(1); // 0 - Grid filled based on user input, 1 - Grid filled randomly
        w.printGrid(); 
        
        long startTime = System.nanoTime();
        w.iterateGrid(0); // 0 -> Un-prefixed, 1 -> Prefixed
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);

        w.printWords();
        w.clearWords();

        System.out.println("Time for un-prefixed word puzzle : " + (duration/100));        

        startTime = System.nanoTime();
        w.iterateGrid(1); 
        endTime = System.nanoTime();
        duration = (endTime - startTime);
        
        w.printWords();        

        System.out.println("Time for prefixed word puzzle : " + (duration/100));                                

        sc.close();
                                   
	}	
}