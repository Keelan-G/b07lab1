import java.io.IOException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

class Polynomial{
	//Precondition: The length of coefficients and exponents must be equal
	double[] coefficients;
	int[] exponents;
	
	public Polynomial(){
	//Empty constructor for polynomial class, accepts no arguments and returns zero polynomial
		this.coefficients = new double[]{0};
		this.exponents = new int[]{0};
	}
	
	public Polynomial(double[] coeff, int[] expon){
	//Constructor for polynomial class, accepts arrays of doubles for each coefficient and ints for each exponent
		this.coefficients = coeff;
		this.exponents = expon;
	}
	
	public Polynomial(File inputfile){
	//Constructor for polynomial class, accepts a file with a text input
		try{
			BufferedReader b = new BufferedReader(new FileReader(inputfile));
			String input = b.readLine();
			String[] vals = input.split("(\\+)|(?=-)"); //this splits the string along all "+" or "-" without removal of "-"'s
			this.coefficients = new double[vals.length];
			this.exponents = new int[vals.length];
			for (int i = 0; i < vals.length; i++){
				String[] term = vals[i].split("x");
				if (term.length < 2) { //if no exponent
					this.coefficients[i] = Double.parseDouble(term[1]);
					this.exponents[i] = 0;
				} else {
					this.coefficients[i] = Double.parseDouble(term[1]);
					this.exponents[i] = Integer.parseInt(term[2]);
				}
			}
		} catch (IOException e) {
			System.out.println("An error occurred. Check file permissions.");
		}
		
	}

	private int maxExp(int[] exp){
	//Retrieves the maximum number from the integer array exp
		int max = exp[0];
		for (int i = 1; i < exp.length; i++){
			if (exp[i] > max){
				max = exp[i];
			}
		}
		return max;
	}

	private int nonZero(double[] list){
	//Returns the total count of nonzero integers in the integer array list
		int count = 0;
		for (int i = 0; i < list.length; i++){
			if (list[i] != 0){
				count++;
			}
		}
		return count;
	}

	public Polynomial add(Polynomial targetPoly){
	//Adds targetPoly to the calling object polynomial linearly
		//Convert the Polynomial objects into more serviceable form
		double[] poly1 = new double[maxExp(this.exponents)];
		for (int i = 0; i < this.exponents.length; i++){
			poly1[this.exponents[i]] = this.coefficients[i];
		}
		double[] poly2 = new double[maxExp(targetPoly.exponents)];
		for (int i = 0; i < targetPoly.exponents.length; i++){
			poly2[targetPoly.exponents[i]] = targetPoly.coefficients[i];
		}
		int length1 = Math.min(poly1.length, poly2.length);
		int length2 = Math.max(poly1.length, poly2.length);
		double[] sum = new double[length2];
		for(int i = 0; i < length1; i++){
			sum[i] = poly1[i] + poly2[i];
		}
		if (poly1.length < poly2.length){
			for(int i = length1; i < length2; i++){
				sum[i] = poly2[i];
			}
		}else{
			for(int i = length1; i < length2; i++){
				sum[i] = poly1[i];
			}
		}
		//Revert the Polynomial output into the desired format
		double[] outCoeff = new double[nonZero(sum)];
		int[] outExp = new int[nonZero(sum)];
		{ //This bracket is literally just to keep j in scope
			int j = 0;
			for (int i = 0; i < sum.length; i++){
				if (sum[i] != 0){
					outCoeff[j] = sum[i];
					outExp[j] = i;
					j++;
				}
			}
		}
		Polynomial output = new Polynomial(outCoeff, outExp);
		return output;
	}
	
	public double evaluate(double value){
	//Evaluates the calling Polynomial at x = value
		double result = 0;
		for(int i = 0; i < this.coefficients.length; i++){
			result += (this.coefficients[i])*(Math.pow(value, this.exponents[i]));
		}
		return result;
	}
	
	public boolean hasRoot(double root){
	//Returns true only if the calling Polynomial has "root" as a root, return false otherwise
		return (this.evaluate(root) == 0);
	}
	
	public Polynomial multiply(Polynomial quotient){
	//Returns the product of the calling Polynomial object with the Polynomial quotient
		int highestExp = maxExp(this.exponents)+maxExp(quotient.exponents);
		double[] multCoeff = new double[highestExp+1]; //because an array to store up to exponent n requires n+1 slots (0 exponent)
		for (int i = 0; i < this.coefficients.length; i++){
			for (int j = 0; j < quotient.coefficients.length; j++){
				//multiply coefficients, multiply exponents, add to the coefficients array at index of exponents
				double product = this.coefficients[i]*quotient.coefficients[j];
				int power = this.exponents[i]+quotient.exponents[j];
				multCoeff[power] += product;
			}
		}
		int outputLength = nonZero(multCoeff);
		double[] productCoeff = new double[outputLength];
		int[] productExp = new int[outputLength];
		{
			int j = 0;
			for (int i = 0; j < outputLength; i++){ //I have absolute confidence this will not blow up
				if (multCoeff[i] != 0){
					productCoeff[j] = multCoeff[i];
					productExp[j] = i;
					j++;
				}
			}
		}
		Polynomial product = new Polynomial(productCoeff, productExp);
		return product;
	}
	
	public void saveToFile(String filename){
	//Saves the calling Polynomial object as a text file named filename
		try {
			File target = new File(filename);
			String poly = "";
			for (int i = 0; i < this.coefficients.length; i++){
				if (this.exponents[i] == 0){
					poly = poly + this.coefficients[i];
				} else {
					if (this.coefficients[i] > 0){
						poly = poly + "+";
					}
					poly = poly + this.coefficients[i] + "x" + this.exponents[i];
				}
			}
			if(!target.isFile()){
				target.createNewFile();
			}
			FileWriter writer = new FileWriter(target, false);
			writer.write(poly);
			writer.close();
		} catch (IOException e) {
			System.out.println("Failed to write to file. Check file permissions.");
		}
	}
}
