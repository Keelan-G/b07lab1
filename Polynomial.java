class Polynomial{
	double[] coefficients;
	
	public Polynomial(){
	//Empty constructor for polynomial class, accepts no arguments and returns zero polynomial
		this.coefficients = new double[]{0};
	}
	
	public Polynomial(double[] coeff){
	//Constructor for polynomial class, accepts an array of doubles for each coefficient
		this.coefficients = coeff;
	}
	
	public Polynomial add(Polynomial targetPoly){
	//Adds targetPoly to the calling object polynomial linearly
		int length1 = Math.min(this.coefficients.length, targetPoly.coefficients.length);
		int length2 = Math.max(this.coefficients.length, targetPoly.coefficients.length);
		double[] sum = new double[length2];
		for(int i = 0; i < length1; i++){
			sum[i] = this.coefficients[i] + targetPoly.coefficients[i];
		}
		if (this.coefficients.length < targetPoly.coefficients.length){
			for(int i = length1; i < length2; i++){
				sum[i] = targetPoly.coefficients[i];
			}
		}else{
			for(int i = length1; i < length2; i++){
				sum[i] = this.coefficients[i];
			}
		}
		Polynomial output = new Polynomial(sum);
		return output;
	}
	
	public double evaluate(double value){
	//Evaluates the calling Polynomial at x = value
		double result = 0;
		for(int i = 0; i < this.coefficients.length; i++){
			result += (this.coefficients[i])*(Math.pow(value, i));
		}
		return result;
	}
	
	public boolean hasRoot(double root){
	//Returns true only if the calling Polynomial has "root" as a root, return false otherwise
		return (this.evaluate(root) == 0);
	}
}