/*=============================================================================
 |   Assignment:  Program # 3
 |
 |       Author:  Ethan Holley (ethanholley@arizona.edu)
 |
 |       Course:  345 (Analysis of Discrete Structures), Fall 2024
 |   Instructor:  L. McCann
 |     Due Date:  10/31/24, at the beginning of class
 |
 |     Language:  Program Language: Java
 |     Packages:  java.io.File, java.io.FileNotFoundException;, java.util.Scanner, java.util.ArrayList
 |
 |  Compile/Run:  Compile: javac Prog3
 |				  Run:  java Prog3 filename
 |
 +-----------------------------------------------------------------------------
 |
 |  Description:  The ColumnSort program reads integers from a file, stores them into a 
 |				  2D grid, and performs an 8-step column-based sorting algorithm. The program
 |				  sorts the columns using the common sorting algorithm selection sort, in all
 |				  of the odd steps of the process. In steps 2 and 4 of the 8 step algorithm, 
 |				  the program rearranges the current grid through different transposition
 |				  techniques based on the instructions. For example, in step 2, the grid is
 |				  transposed from an r x s matrix into an r/s x s submatrix. In step 4, the
 |				  method performs the inverse permutation to the grid performed in step 2.
 |				  Lastly, steps 6 and 8 shift the values in the grid up or down as required 
 |				  by the instructions. Step 6 shifts all the values down r / 2 positions, wrapping
 |				  certain integers to the next column when needed. Step 8 shifts these values 
 |				  back up to their original positions, essentially undoing what was done in step
 |				  6, to create a sorted grid of integers by columns. The final sorted grid is 
 |				  output along with the time taken for sorting, providing a comprehensive view 
 |				  of the column-sorted data. 
 |				  
 |        Input:  The user is required to input one command line argument consisting of a
 |				file of integers, one per line. If the input consists of something other than
 |				integers, an error message will be printed. If file is not found, an error message
 |				will also be printed.
 |
 |       Output:  The output will produce the size of the file, the number of rows in each
 |				  column, the number of columns total for the algorithm, the time it took for
 |				  the algorithm to sort the numbers in order, and then print each of the numbers
 | 				  from the file in sorted order.
 |
 |   Techniques:  One standard algorithm implemented is in the odd steps of the algorithm
 |				  columnSort. In each of the odd steps, the algorithm calls a method selectionSort
 |				  which uses the standard algorithm to search for the next smallest integer in
 |				  the current column and swap items in order to sort the grid correctly. The even
 |				  steps follow the ColumnSort Algorithm instructions as provided.
 |
 |   Required Features Not Included: All required features are included.
 |
 |             
 |   Known Bugs:  None; the program operates correctly.
 |
 *===========================================================================*/
import java.io.File; // used for file reading

import java.io.FileNotFoundException; // used for file error checking
import java.util.ArrayList; // used for storing integers from file
import java.util.Scanner; // used to scan file

public class Prog3 {

	public static void main(String[] args) {
		String filename = args[0]; // accept input file command line argument
		try {
			File nums = new File(filename);
			Scanner read = new Scanner(nums);
			ArrayList<Integer> getNums = new ArrayList<>();
			while (read.hasNextLine()) { // read through all lines of file
				String curr = read.nextLine(); // variable to hold current line
				if (isNumeric(curr)) { // check to ensure line is a valid integer
					getNums.add(Integer.parseInt(curr)); // add int to list
				} else {
					// print error message if line is not an integer and exit program
					System.out.println("Error occurred, file doesn't contain only integers.");
					read.close();
					System.exit(1);
				}
			}
			read.close();
				
			final double BILLION = 1000000000.0; // a nanosec = billionth of a sec
			System.gc(); 

			int N = getNums.size(); // get size of file
			int[] getDimensions = pickDimensions(N); // call helper method to pick r and s
			int r = getDimensions[0]; // assign r value
			int s = getDimensions[1]; // assign s value
			
			Integer[][] grid = new Integer[r][s]; // initialize grid as Integer[][]
			
			// fill grid with elements from getNums list
            for (int i = 0; i < getNums.size(); i++) {
                int row = i / s;
                int col = i % s;
                grid[row][col] = getNums.get(i); // assign Integer object to grid
            }
            
            // check if s is only one column
            if (s == 1) {
            	// start timing program
            	long startTime = System.nanoTime();
            	selectionSort(grid, 0); // call selection sort on singular column
            	long elapsedTime = System.nanoTime() - startTime; // stop timing
                double seconds = elapsedTime / BILLION; // calculate seconds
            	printInts(grid, r, s, N, seconds); // call method to print output
            	System.exit(0); // exit program
            }
            
            // start timing program
            long startTime = System.nanoTime();
            for (int i = 1; i < 9; i++) { // iterate from 1-8 steps
            	int currentRows = grid.length; // get current grid's number of rows
                int currentCols = grid[0].length; // get current grid's number of columns
                
            	if (i % 2 == 1) { // check if current step is odd
            		
            		// if odd, call selection sort on each column in the grid
            		for (int col = 0; col < currentCols; col++) {
            			selectionSort(grid, col);
            		}
            		
            	} else if (i == 2) {
            		// if step 2, call method to transpose grid correctly for second step of algorithm
            		grid = step2Transpose(grid, currentRows, currentCols);
            	} else if(i == 4) {
            		// if step 4, call method to transpose grid correctly for fourth step of algorithm
            		grid = step4Transpose(grid, currentRows, currentCols);
            	} else if (i == 6) {
            		// if step 6, call method to transpose grid correctly for sixth step of algorithm
            		grid = step6ShiftDown(grid, currentRows, currentCols);
            	} else if (i == 8) {
            		// if step 8, call method to transpose grid correctly for eighth step of algorithm
            		grid = step8ShiftUp(grid, currentRows, currentCols);
            	}
            }
            
            long elapsedTime = System.nanoTime() - startTime; // stop time clock
            double seconds = elapsedTime / BILLION; // calculate time in seconds
            printInts(grid, r, s, N, seconds); // call method to print sorted output
            
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found"); // print message for file error
		}
	}
	
	/*---------------------------------------------------------------------
    |  Method isNumeric (String)
    |
    |  Purpose:  Checks if all lines from the input file are integers, by using
    |			 build in method Integer.parseInt. 
    |
    |  Pre-condition:  A valid String parameter
    |
    |  Post-condition: True if the string consists of only integers, False otherwise
    |
    |  Parameters:  String -- a line from the input file
    |
    |  Returns:  boolean value - True or False
    *-------------------------------------------------------------------*/
	public static boolean isNumeric(String str) {
		// check if string only contains integers
	    try {
	        Integer.parseInt(str);
	        return true;
	    } catch (NumberFormatException e) {
	        return false;
	    }
	}
	
	/*---------------------------------------------------------------------
    |  Method pickDimensions (int)
    |
    |  Purpose: Determines suitable values for the number of rows, r, and columns, s,
    |           that meet specific requirements for the ColumnSort algorithm. The method
    |           finds dimensions that show r is divisible by s and meets the 
    |           constraint r >= 2 * (s - 1)^2, ensuring the algorithm can operate 
    |           efficiently for the input file.
    |
    |  Pre-condition: the file being read is valid of only integers
    |
    |  Post-condition: Method chooses a correct/valid r and s values in order for
    |				   the algorithm ColumnSort to run smoothly
    |
    |  Parameters: arrayLength - the number of lines in the input file
    |
    |  Returns:  an array of the chosen r and s values
    *-------------------------------------------------------------------*/
	public static int[] pickDimensions(int arrayLength) {
		int findR = arrayLength; // default r to arrayLength
		int findS = 1; // default s to be 1 column
		
		for (int s = 1; s <= Math.sqrt(arrayLength); s++) {
			if (arrayLength % s == 0) {
				int r = arrayLength / s;
				
				// make sure s is a divisor of r and r meets equation requirement
				if (r >= 2 * Math.pow(s - 1, 2) && r % s == 0) {
					findR = r;
					findS = s;
				}
			}
		}
		return new int[]{findR, findS}; // return int[] of chosen r and s values
	}
	
	/*---------------------------------------------------------------------
    |  Method selectionSort (int[][], int)
    |
    |  Purpose: Sorts a each column within the 2D grid in ascending order using the 
    |           selection sort algorithm. The method treats each row as an element and 
    |           orders them based on the values within the specified column. It finds
    |			the minimum value in the current row and swaps it with the to the correct
    |			index it belongs.
    |
    |  Pre-condition: The 2D array grid contains integers, and cols is a valid index 
    |                 of the columns in grid
    |
    |  Post-condition: All the columns in the grid will be sorted in 
    |                  ascending order, by rearranging the ints in the current row
    |
    |  Parameters: 
    	   grid - a 2D integer array to be sorted by a specific column
    |      cols - the current column index to sort in the grid
    |
    |  Returns: N/A
    *-------------------------------------------------------------------*/
	public static void selectionSort(Integer[][] grid, int cols) {
	    int rows = grid.length; // get number of rows in grid
	    
	    // iterate through rows
	    for (int i = 0; i < rows - 1; i++) {
	        int currMin = i; // make current min value i
	        
	        // iterate from i + 1 through rest of rows
	        for (int j = i + 1; j < rows; j++) {
	            // find next minimum value in column
	            if (grid[j][cols].compareTo(grid[currMin][cols]) < 0) {
	                currMin = j;
	            }
	        }
	        
	        if (currMin != i) { // check if min is different from current i
	            
	            // switch min value in grid with position i
	            Integer temp = grid[i][cols]; 
	            grid[i][cols] = grid[currMin][cols];
	            grid[currMin][cols] = temp;
	        }
	    }
	}
	
	/*---------------------------------------------------------------------
    |  Method step2Transpose (int[][], int, int)
    |
    |  Purpose: Transposes the 2D integer array grid, rearranging its rows and columns 
    |           to produce a new grid where the r x s matrix grid is transformed into an
    |			r / s x s submatrix grid.
    |
    |  Pre-condition: The dimensions row and col match the dimensions of the current grid, 
    |                 and grid is a valid, non-null 2D integer array.
    |
    |  Post-condition: Returns a new 2D array with rows and columns swapped, effectively 
    |                  creating a transposed version of grid.
    |
    |  Parameters: 
    |	   grid - the current 2D integer array to transpose
    |      row - the number of rows in grid
    |      col - the number of columns in grid
    |
    |  Returns: a new 2D array that correctly transposed the current grid of integers
    *-------------------------------------------------------------------*/
	public static Integer[][] step2Transpose(Integer[][] grid, int row, int col) {
	    Integer[][] newGrid = new Integer[row][col]; // create new grid to be returned
	    
	    int currRow = 0; // variable to track current row for transpose to new grid
	    
	    for (int c = 0; c < col; c++) { // iterate over columns in grid
	        
	        int currCol = 0; // variable to keep track of current column for transpose to new grid
	        
	        for (int r = 0; r < row; r++) { // iterate rows in grid
	            
	            if (currCol < col) { // transpose grid until currCol equals col number
	                
	                newGrid[currRow][currCol] = grid[r][c]; // transpose current spot on grid to new grid
	                currCol += 1; // increment currCol number
	                
	            } else { 
	                currCol = 0; // reset currCol number
	                currRow += 1; // move down one row in newGrid
	                
	                newGrid[currRow][currCol] = grid[r][c]; // transpose current spot on grid to new grid
	                currCol += 1; // increment currCol number
	            }
	        }
	        currRow += 1; // after inner for loop iterates through entire column, increment row number
	    }
	    return newGrid; // return transposed grid
	}
	
	/*---------------------------------------------------------------------
    |  Method step4Transpose (int[][], int, int)
    |
    |  Purpose:  Transposes the current 2D integer array grid, rearranging its 
    |            rows and columns to produce the inverse permutation on the grid performed
    |			 in step 2.
    |
    |  Pre-condition: The dimensions row and col match the dimensions of the current grid, 
    |                 and grid is a valid, non-null 2D integer array.
    |
    |  Post-condition: Returns a new 2D array with rows and columns swapped, effectively 
    |                  creating the inverse permutation performed in step 2.
    |
    |  Parameters: 
    |	   grid - the current 2D integer array to transpose
    |      row - the number of rows in grid
    |      col - the number of columns in grid
    |
    |  Returns: a new 2D array that correctly transposed the current grid of integers
    *-------------------------------------------------------------------*/
	public static Integer[][] step4Transpose(Integer[][] grid, int row, int col) {
	    Integer[][] newGrid = new Integer[row][col]; // create new grid to be returned
	    
	    int currCol = 0; // variable to keep track of current column for transpose to new grid
	    int currRow = 0; // variable to track current row for transpose to new grid

	    for (int c = 0; c < col; c++) { // iterate over columns
	        for (int r = 0; r < row; r++) { // iterate over rows
	            
	            // transpose current spot on grid to new grid
	            newGrid[r][c] = grid[currRow][currCol];
	            currCol += 1; // increment column number
	            
	            if (currCol == col) { // check if current column is equal to number of columns
	                currCol = 0; // reset column number
	                currRow += 1; // move down to next row
	            }
	        }
	    }
	    return newGrid; // return transposed grid
	}
	
	/*---------------------------------------------------------------------
    |  Method step6ShiftDown (int[][], int, int)
    |
    |  Purpose: Shifts elements within the 2D array grid either downwards by half 
    |           the row count or upward by a similar amount, depending on their 
    |           position, and adds one extra column to the right to accommodate shifts.
    |           Rows in the upper half are shifted down by row / 2, variable (shift), rows, 
    |			while rows in the lower half are shifted up by row / 2 rows, moving one 
    |			column over. Adds Integer.MIN_VALUE to the first row / 2 cells in the 
    |			first column and Integer.MAX_VALUE to the last row / 2 cells in the last 
    |			column.
    |
    |  Pre-condition: grid is a valid 2D integer array, and row and col match the
    |                 dimensions of grid.
    |
    |  Post-condition: Returns a new grid with shifted values according to the described 
    |                  pattern, with one additional column added for shifted and
    |				   infinity values.
    |
    |  Parameters: 
    |	   grid - the current 2D integer array to transpose
    |      row - the number of rows in grid
    |      col - the number of columns in grid
    |
    |  Returns: a new 2D array that correctly shifted down/over the integers in the current grid
    *-------------------------------------------------------------------*/
	public static Integer[][] step6ShiftDown(Integer[][] grid, int row, int col) {
	    Integer numCols = col + 1; // number of columns needed for shifting
	    int shift = row / 2;        // variable representing how far down needed to shift

	    // create newGrid to be returned with the same number of rows and one extra column
	    Integer[][] newGrid = new Integer[row][numCols];

	    // store negative infinity values into the first half of the first column of newGrid
	    for (int i = 0; i < shift; i++) {
	        newGrid[i][0] = Integer.MIN_VALUE;
	    }

	    // store infinity values into the last half of the last column of newGrid
	    for (int i = shift; i < row; i++) {
	        newGrid[i][numCols - 1] = Integer.MAX_VALUE;
	    }

	    // check if the number of rows in the grid is odd
	    if (row % 2 == 1) {
	        for (int c = 0; c < col; c++) { // iterate columns
	            for (int r = 0; r < row; r++) { // iterate rows
	                if (r <= shift) { // check if current row is less than or equal to rows / 2
	                    newGrid[r + shift][c] = grid[r][c]; // shift first half of rows down
	                } else { // if in the second half of the current row
	                    // shift positions up by (rows / 2 - 1) and move over one column to the right
	                    newGrid[r - shift - 1][c + 1] = grid[r][c];
	                }
	            }
	        }
	    } else { // if the number of rows is even
	        for (int c = 0; c < col; c++) { // iterate columns
	            for (int r = 0; r < row; r++) { // iterate rows
	                if (r < shift) { // check if current row is less than rows / 2
	                    newGrid[r + shift][c] = grid[r][c]; // shift first half of rows down
	                } else { // if in the second half of the current row
	                    newGrid[r - shift][c + 1] = grid[r][c]; // shift positions up and move over one column to the right
	                }
	            }
	        }
	    }
	    return newGrid; // return the correctly shifted newGrid
	}
	
	/*---------------------------------------------------------------------
    |  Method step8ShiftUp (int[][], int, int)
    |
    |  Purpose:  Shifts elements in the grid upwards to remove added columns from step6 
    |           and revert to the original layout, excluding infinity values.
    |           Restores the original column count by reducing col by one, 
    |           creating a new 2D array where only valid integer values are retained 
    |           in the shifted positions.
    |
    |  Pre-condition: grid is a valid 2D integer array, and row and col match the
    |                 dimensions of grid.
    |
    |  Post-condition: Returns a new grid with shifted values according to the described 
    |                  pattern, with one column less for shifted values back to original
    |				   form, replacing infinity values.
    |
    |  Parameters: 
    |	   grid - the current 2D integer array to transpose
    |      row - the number of rows in grid
    |      col - the number of columns in grid
    |
    |  Returns: a new 2D array that correctly shifted up/backward over the integers in the current grid
    *-------------------------------------------------------------------*/
	public static Integer[][] step8ShiftUp(Integer[][] grid, int row, int col) {
	    int numCols = col - 1; // number of columns for newGrid
	    Integer[][] newGrid = new Integer[row][numCols]; // initialize a newGrid with correct dimensions

	    int currRow = 0; // variable to keep track of currRow in newGrid
	    int currCol = 0; // variable to keep track of currCol in newGrid

	    for (int c = 0; c < col; c++) { // iterate cols
	        for (int r = 0; r < row; r++) { // iterate rows
	            Integer pos = grid[r][c]; // get current Integer in grid

	            // check to make sure Integer is not negative or positive infinity
	            if (pos != Integer.MIN_VALUE && pos != Integer.MAX_VALUE) {
	                newGrid[currRow][currCol] = pos; // add current Integer from grid into newGrid at correct position
	                currRow += 1; // move down a row

	                if (currRow == row) { // check if the row is at the max number of rows in newGrid
	                    currRow = 0; // reset currRow
	                    currCol += 1; // move one col to the right
	                }
	            }
	        }
	    }
	    return newGrid; // return correctly transposed newGrid
	}
	
	/*---------------------------------------------------------------------
    |  Method printInts (int[][], int, int, int, double)
    |
    |  Purpose: Prints the details of the sorting algorithm, including grid dimensions, 
    |           file size, and time it took for sorting, in seconds. Then, iterates through 
    |           each element in grid to print the sorted integers, one per line.
    |
    |  Pre-condition: The grid array is populated with integers and matches the specified
    |                 row and col dimensions. size is a valid representation of the total
    |                 count of integers in the input file. seconds the amount of time taken
    |				  for algorithm to complete.
    |
    |  Post-condition: Prints size, row, col, and seconds information, followed by each
    |                  integer in grid, printed one per line.
    |
    |  Parameters: 
    |	   grid - the current 2D integer array to transpose
    |      row - the number of rows in grid
    |      col - the number of columns in grid
    |	   size - size of the file
    |	   seconds - number of seconds algorithm took to sort the file
    |
    |  Returns: N/A
    *-------------------------------------------------------------------*/
	public static void printInts(Integer[][] grid, int row, int col, int size, double seconds) {
	    System.out.println("n = " + size); // print N (size of file)
	    System.out.println("r = " + row); // print row count
	    System.out.println("s = " + col); // print column count

	    // print time taken for sorting algorithm, in seconds rounded to 3 decimals
	    System.out.println("Elapsed time = " + String.format("%.3f", seconds) + " seconds.");

	    for (int c = 0; c < col; c++) { // iterate cols
	        for (int r = 0; r < row; r++) { // iterate rows
	                    System.out.println(grid[r][c]); // print current Integer with new line
	           }
	        }
		}
}
