# ColumnSort
This is a Java Program that implements Leighton's column sorting algorithm column sort, to sort a list of numbers.

It is as follows:
Step 1 : Sort Each column individually
- Use any sorting technique, I used selection sort.

Step 2 : Transpose
- Use a different variant of transpose of matrix, in which we read the elements in a Column Major Form and then write back to the matrix in a Row Major Form

Step 3 : Sort Each column individually
- Repeat the step 1 on the new matrix

Step 4 : Untranspose
- This is the reverse of the step 2. In this, we read the elements of the matrix in Row Major Form and write back to the Column Major Form.

Step 5 : Sort Each Column individually

Step 6 : Shift
- Add (r/2) (lower integer limit) elements on one side of this 1-D matrix and (r/2) (upper integer limit) on one side. Then, we make the elements added to the front side (from 0 index 0,1,2....) as -infinity and make the elements added to the other side as +infinity
Transform the 1-D vector to a 2-D matrix again

Step 7 : Sort Each column individually

Step 8 : Unshift
- Here, we reverse the steps performed in the step 6, by again making the 2-D Matrix to a 1-D vector, and remove the infinities and retransform the 1-D vector to a 2-D matrix of dimensions (r,c) again.


