
public class Fraction {
	//-*- Inner Exception Classes -*-\\
	public class ZeroDenomException extends Exception {
		public ZeroDenomException(String msg) {
			super(msg);
		}
		
		public ZeroDenomException() {
			super("A denominator of zero was created");
		}
	}
	
	public class DivisionByZeroException extends ArithmeticException {
		public DivisionByZeroException(String msg) {
			super(msg);
		}
		
		public DivisionByZeroException() {
			super("Division by zero was attempted");
		}
	}
	
	//Class variables for the numerator and denominator
	private int num = 0;
	private int denom = 1;
	
	//Constructors
	
	//Default constructor
	public Fraction() {
		num = 0;
		denom = 1;
	}
	//Constructs class from another class
	public Fraction(Fraction other) throws ZeroDenomException{
		this(other.num, other.denom);
	}
	
	public Fraction(int n) {
		num = n;
		denom = 1;
	}
	//Takes two integers
	public Fraction(int n, int d) throws ZeroDenomException {
		int commonDenom = Math.abs(GCD(n, d));
		if (d > 0) {
			num = n / commonDenom;
			denom = d / commonDenom;
		}
		else if(d < 0) {
			d *= -1;
			num = (n / commonDenom) * -1;
			denom = d / commonDenom;
		}
		else if(d == 0){
			throw new ZeroDenomException("A denominator of zero was submitted.");
		}
	}
	//Parses a string
	public Fraction(String src) throws ZeroDenomException, ArithmeticException {
		int stringNum;
		int stringDenom = 1;
		if(src.contains("/")){
			String numerator = src.split("/")[0];
			String denominator = src.split("/")[1];
		
			stringNum = Integer.parseInt(numerator);
			stringDenom = Integer.parseInt(denominator);
		}
		else {
			stringNum = Integer.parseInt(src);
		}
		
		int commonDenom = Math.abs(GCD(stringNum, stringDenom));
		if (stringDenom > 0) {
			num = stringNum / commonDenom;
			denom = stringDenom / commonDenom;
		}
		else if(stringDenom < 0) {
			stringDenom *= -1;
			num = (stringNum / commonDenom) * -1;
			denom = stringDenom / commonDenom;
		}
		else if(stringDenom == 0){
			throw new ZeroDenomException("A denominator of zero was submitted.");
		}
		
	}
	
	//Accessors
	
	//Returns the numerator
	public int getNumerator() {
		return num;
	}
	//Returns the denominator
	public int getDenominator() {
		return denom;
	}
	//Returns the sign of the fraction
	public int getSign() {
		int sign = 2; //if something goes wrong the initial value of two will be returned
		if (num < 0) {
			sign = -1;
		}
		else if (num == 0) {
			sign = 0;
		}
		else if (num > 0) {
			sign = 1;
		}
		
		return sign;
	}
	//Returns the decimal value of the fraction
	public double value() {
		double value = (double)num / denom;
		
		return value;
	}
	
	//Mutators
	
	//Changes the numerator
	public void setNumerator ( int newNumerator ) {
		num = newNumerator;
		normalize();
	}
	//Changes the denominator
	public void setDenominator( int newDenominator ) {
		denom = newDenominator;
		normalize();
	}
	
	//Operations
	
	//These operations return a new instance
	
	//Adds two fractions
	public Fraction sum(Fraction that) throws ZeroDenomException{
		Fraction fractionSum = new Fraction((this.num*that.denom) + (that.num*this.denom), (that.denom*this.denom));
		
		return fractionSum;
	}
	//subtracts two fractions
	public Fraction difference(Fraction that) throws ZeroDenomException{
		Fraction fractionDifference = new Fraction((this.num*that.denom) - (that.num*this.denom), (that.denom*this.denom));
		
		return fractionDifference;
	}
	//multiplies two fractions
	public Fraction product(Fraction that) throws ZeroDenomException{
		Fraction fractionProduct = new Fraction(this.num*that.num, this.denom*that.denom);
		
		return fractionProduct;
	}
	//divides two fractions
	public Fraction quotient(Fraction that) throws ZeroDenomException, DivisionByZeroException{
		Fraction fractionQuotient = new Fraction(this.num*that.denom, this.denom*that.num);
		if (that.num == 0) {
			throw new DivisionByZeroException();
		}
		return fractionQuotient;
	}
	//Inverts the fraction
	public Fraction inverse() throws ZeroDenomException, DivisionByZeroException{
		Fraction fractionInverse = new Fraction(this.denom, this.num);
		if (fractionInverse.num == 0) {
			throw new DivisionByZeroException();
		}
		return fractionInverse;
	}
	//Changes the sign of the fraction
	public Fraction negation() throws ZeroDenomException{
		Fraction fractionNegative = new Fraction(this.num * -1, this.denom);
		
		return fractionNegative;
	}
	
	//These operations change the instance
	
	//Adds the fractions
	public void add(Fraction that) {
		this.num = (this.num*that.denom) + (that.num*this.denom);
		this.denom = that.denom*this.denom;
		normalize();
	}
	//Subtracts the fractions
	public void subtract(Fraction that) {
		this.num = (this.num*that.denom) - (that.num*this.denom);
		this.denom = that.denom*this.denom;
		normalize();
	}
	//Multiplies the fractions
	public void multiply(Fraction that) {
		this.num = (this.num*that.num);
		this.denom = that.denom*this.denom;
		normalize();
	}
	//Divides the fractions
	public void divide(Fraction that) throws DivisionByZeroException{
		if (that.num == 0) {
			throw new DivisionByZeroException();
		}
		this.num = (this.num*that.denom);
		this.denom = that.num*this.denom;
		normalize();
	}
	//Inverts the fraction
	public void invert() throws DivisionByZeroException{
		if(this.num == 0){
			throw new DivisionByZeroException();
		}
		int tmp = this.num;
		this.num = this.denom;
		this.denom = tmp;
		normalize();
	}
	//Changes the sign of the fraction
	public void negate() {
		this.num *= -1;
		normalize();
	}
	//Returns the instance as a string in its reduced terms
	public String toString() {
		if(isUnity() || isZero()) {
			return "" + num;
		}
		else {
		int commonDenom = GCD(num, denom);
		num /= commonDenom;
		denom /= commonDenom;
		return num + "/" + denom;
		}
	}
	//Checks if the numerator is zero
	public boolean isZero() {
		if (num == 0) {
			return true;
		}
		
		return false;
	}
	//Checks if the numerator and denominator are equal
	public boolean isUnity() {
		if (num == denom) {
			return true;
		}
		else
			return false;
	}
	//Checks if two fractions are equal
	public boolean equals(Object o) {
		if(o==null || !(o instanceof Fraction)) {
			return false;
		}
		Fraction that = (Fraction) o;
		if ((this.num ==that.num) && (this.denom == that.denom)) {
			return true;
		}
		return false;
	}
	
	private void normalize() {
		int n = this.num;
		int d = this.denom;
		int commonDenom = Math.abs(GCD(n, d));
		if (d > 0) {
			num = n / commonDenom;
			denom = d / commonDenom;
		}
		else if(d < 0) {
			d *= -1;
			num = (n / commonDenom) * -1;
			denom = d / commonDenom;
		}
		else if(d == 0){
			d = 1;
			num = n / commonDenom;
			denom = d / commonDenom;
		}
	}
	//Finds the greatest common denominator of a fraction.
	//There are multiple methods here commented out for my own edification
	public static int GCD (int a, int b) {
		/*
		while (b != a) {
			int r = b;
			b = a % b;
			a = r;
			}
			return a;
			// Use the absolute values of 'a' and 'b' (two ways to do it)
			//a = ((a < 0) ? -a : a);
			//b = Math.abs(b);
			
			
			if (a < b) {
				//exchange a and b
				int tmp = a;
				a = b;
				b = tmp;
				}
			*/
				//recursive call
				//if (a < b) {
					//return GCD(b, a);
					//}
				// GCD(a, b) = GCD(b, a mod b) This is also a more readable version of the method
				if (b == 0) { //This is a step to make sure there isn't infinite recurssion
					return a;
				}
				else {
					return GCD(b, a % b);
				}
		}
}
