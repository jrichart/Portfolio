
import java.util.regex.Pattern;
import java.util.regex.Matcher;
/**
 * Polynomial Class, which takes an array of the term class and organizes as well as performs basic functions.
 * 
 * @version v1.0
 * @author Joel Richart
 * 
 */
public class Polynomial {
  // The polynomial is represented by an array of 'Term' instances.
  // The 'terms' array may contain extraneous cells: the relevant cells
  // are those from 0 through 'nTerms - 1'

  private Term   terms[];  // array of terms
  private int    nTerms;   // number of meaningful terms
  
  // default constructor: sets the instance to the zero polynomial
  public Polynomial() {
    nTerms = 0;
    terms = new Term[1];
  }

  private Polynomial( int maxTerms ) {
    nTerms = 0;
    terms = new Term[maxTerms];
  }

  public Polynomial( Polynomial p ) {
    nTerms = p.nTerms;
    terms = new Term[nTerms];
    for (int k = 0; k < nTerms; k++)
      terms[k] = new Term(p.terms[k]);
  }


  public Polynomial( String src ) {
    // Initialize this polynomial to zero
    nTerms = 0;
    terms = new Term[1]; 

    // Setup the basic regular expressions for matching terms
    String xPatString     = "x(\\^([0-9]+))?";
    Pattern termPattern = 
      Pattern.compile("\\s*(" + xPatString + ")" + "|" 
		      + "\\s*([-+]?[0-9]+(\\.[0-9]*)?)[*\\s]?(" + xPatString + ")?");
    Pattern signPattern = Pattern.compile("\\s*([-+])\\s*");

    // Match a sequence of signs and terms
    Matcher matcher;  // reassigned as needed for matching the patterns
    int sign = 1;     // incoming sign from the operator
    while (true) {
      Term term = new Term();

      // The next thing in 'src' should be a term
      matcher = termPattern.matcher(src);
      boolean result = matcher.lookingAt();
      if (!result) {
	// syntax error: stop with what he have
	break;
      }

      /* // Uncomment this to show the capture groups
	for (int k = 0; k < 9; k++) 
	  System.out.println("group " + k + ": " + matcher.group(k));
	System.out.println("");
      */

      // extract the coefficient and power from the captured groups
      if (matcher.group(1) != null) {
	// group 1 contains the first alternation, which matches
	// just "x^<power>" without a coefficient
	term.coeff = sign;
	if (matcher.group(2) == null) {
	  // the power is 1
	  term.power = 1;
	}
	else {
	  // the power is in group 3
	  term.power = Integer.valueOf(matcher.group(3));
	}
      }
      else {

	// otherwise, the second alternation matches, which has an
	// explicit coefficient but an optional "x" term
	term.coeff = sign*Double.valueOf(matcher.group(4));
	// get the exponent
	if (matcher.group(6) == null) {
	  // the "x" term is omitted; take it as x^0
	  term.power = 0;	    
	}
	else if (matcher.group(7) == null) {
	  // the exponent is omitted, take it as 1
	  term.power = 1;	    
	}
	else {
	  term.power = Integer.valueOf(matcher.group(8));
	}
      }
      
      // update the position in the input string
      src = src.substring(matcher.end());
      
      // add the term
      if (nTerms >= terms.length) {
	Term newTerms[] = new Term[2*nTerms];
	for (int k = 0; k < terms.length; k++)
	  newTerms[k] = terms[k];
	terms = newTerms;	  
      }	  
      terms[nTerms++] = term;
    
      // If there is a "+" or "-", take it as a binary operator
      // (which indicates there is another term coming)
      matcher = signPattern.matcher(src);
      if (matcher.lookingAt()) {
	sign = (matcher.group(1).equals("-") ? -1 : 1);
	src = src.substring(matcher.end());
      }
      else {
	break;
      }
    }
    
    normalize();
  }


  /* Simple accessors */

  /**
   * Returns the degree of this polynomial, which is the largest power
   * of all the terms.
   * @return the degree
   */
  public int degree() {
    return terms[nTerms - 1].power;
  }

  /**
   * Returns true if, and only if, the polynomial is zero
   * @return true if zero
   */
  public boolean isZero() {
    boolean test = true;
    for(int i = 0; i < nTerms; i++) {
    	if(terms[i].coeff != 0) {
    		test = false;
    	}
    }
    return test;
  }


  /* Polynomial operations. */

  /**
   * Computes the polynomial sum of this polynomial and 'addend'.
   * Neither this polynomial nor 'addend' is modified.
   *
   * @param addend polynomial to be added
   * @return a newly created Polynomial instance containing the sum
   */
  public Polynomial add( Polynomial addend ) {
    //! It should go something like this

    // Create a new polynomial for the sum
    Polynomial sum = new Polynomial(nTerms + addend.nTerms);

    // Copy the terms of this polynomial to the new 'sum' polynomial
    int i = 0; // index of the term copied to the sum
    for (int k = 0; k < nTerms; k++) {
      sum.terms[i++] = new Term(terms[k]);
    }

    // Append all the terms of 'addend' to 'sum' after the terms of 'this'
    for (int k = 0; k < addend.nTerms; k++) {
      sum.terms[i++] = new Term(addend.terms[k]);
    }

    // Set the total number of terms
    sum.nTerms = i;

    // Normalize
    sum.normalize();
    return sum;

  }

  /**
   * Computes the polynomial difference of this polynomial and 'subtrahend'.
   * Neither this polynomial nor 'subtrahend' is modified.
   *
   * @param subtrahend polynomial to be subtracted
   * @return a newly created Polynomial instance containing the difference
   */  
  public Polynomial sub( Polynomial subtrahend ) {
	// Create a new polynomial for the sum
	Polynomial sub = new Polynomial(nTerms + subtrahend.nTerms);

	// Copy the terms of this polynomial to the new 'sub' polynomial
	int i = 0; // index of the term copied to the sub
	for (int k = 0; k < nTerms; k++) {
	  sub.terms[i++] = new Term(terms[k]);
	}
	
	//inverse all of the coefficients of 'subtrahend'
	for(int k = 0; k < subtrahend.nTerms; k++) {
		subtrahend.terms[k].coeff = - subtrahend.terms[k].coeff;
	}

	// Append all the terms of 'subtrahend' to 'sub' after the terms of 'this'
	for (int k = 0; k < subtrahend.nTerms; k++) {
	  sub.terms[i++] = new Term(subtrahend.terms[k]);
	}

	// Set the total number of terms
	sub.nTerms = i;

	// Normalize
	sub.normalize();
	return sub;
  }

  /**
   * Computes the polynomial product of this polynomial and 'multiplier'.
   * Neither this polynomial nor 'multiplier' is modified.
   *
   * @param multiplier polynomial to multiply by
   * @return a newly created Polynomial instance containing the product
   */  
  public Polynomial mul( Polynomial multiplier ) {
	// Create a new polynomial for the sum
	Polynomial mul = new Polynomial(this.nTerms + multiplier.nTerms);
	
	for(int i = 0; i < this.nTerms; i++) {
		for(int j = 0; j < multiplier.nTerms; j++){
		mul.terms[i+j] = new Term(this.terms[i].coeff * multiplier.terms[j].coeff, this.terms[i].power * multiplier.terms[j].power);
		}
	}
		
	mul.normalize();
	return mul;
  }

  /**
   * Computes this polynomial raised to the power 'power', which is the
   * polynomial product of 'power' copies of this.
   * @param power power to which this polynomial is raised (assumed to be
   *              nonnegative)
   * @return a newly created polynomial instance containing the power
   */
  public Polynomial pow( int power ) {
	// Create a new polynomial for the sum
	Polynomial pow = new Polynomial(nTerms);
	
	// Copy the terms of this polynomial to the new 'pow' polynomial
	int i = 0; // index of the term copied to the pow
	for (int k = 0; k < nTerms; k++) {
	  pow.terms[i++] = new Term(terms[k]);
	}
	
	//adds 'power' to the degree of each term
	for (int k = 0; k < nTerms; k++) {
		pow.terms[k].power = pow.terms[k].power + power;
	}
	//no need to normalize, each term was increased by the same degree,
	//so they should still be in the correct order
	return pow;
  }

  /**
   * Compares this polynomial and 'poly'.  The return is true if they
   * are equal as polynomials, and false otherwise
   * @param poly polynomial instance to which this is compared
   * @return true if the polynomials are equal, and false otherwise.
   */
  public boolean equals( Polynomial poly ) {
    if(this.degree() != poly.degree())
    	return false;
    for(int i = 0; i < this.nTerms; i++) {
    	if (this.terms[i].coeff != poly.terms[i].coeff){
    		return false;
    	}
    }
    return true;
  }

  /**
   * Creates a normalized polynomial by combining like terms
   * and sorting the array from lowest degree to highest
   */
  private void normalize() {
	  //combines like terms and sets the second term's coefficient to zero
	  for(int i = 0; i < nTerms -1; i++) {
		  for(int j = i + 1; j < nTerms; j++) {
			  if(terms[i].power == terms[j].power) {
				  terms[i].coeff += terms[j].coeff;
				  terms[j].coeff = 0;
			  }
		  }
	  }
	  //Moves all of the non zero coefficient terms to the beginning to the array
	  Term temp = new Term();
	  for(int i = 0; i < nTerms; i++) {
		  for(int j = i + 1; j < nTerms; j++) {
			  if(terms[i].coeff == 0) {
				  if(terms[j].coeff != 0) {
					  terms[i] = temp;
					  terms[i] = terms[j];
					  terms[j] = temp;
				  }
			  }
		  }
	  }
	  //resets the count of nTerms to only the non zero coefficient terms
	  int count = 0;
	  for(int i = 0; i < nTerms; i++) {
		  if (terms[i].coeff != 0)
			  count += 1;
	  }
	  
	  nTerms = count;
	  
	  //uses a selection sort to organize the terms from lowest degree to highest
	  Term temp2 = new Term();
	  for(int i = 0; i < nTerms - 1; i++) {
		  int check = i;
		  for(int j = nTerms - 1; j > i; j--) {
			  if(terms[check].power > terms[j].power) {
				  check = j;
			  }
		  }
		  temp2 = terms[i];
		  terms[i] = terms[check];
		  terms[check] = temp2;
	  }
  }

  /**
   * Returns a string representation of this polynomial
   * @return string representation of this polynomial
   */
  public String toString() {
    //! Implement
	  String poly = "";
	  for(int i = 0; i < nTerms; i++) {
		  poly += terms[i].toString() + ", ";
	  }
	  return poly;
  }
  
}