/**
 * A class to define a term of a polynomial by its power and coefficient
 * Also prints the term properly based on the contents of the term
 * 
 * @version v1.0
 * @author Joel Richart
 *
 */

public class Term {
  public double coeff;
  public int power;

  
  /* Constructors */
  
  public Term() {
    coeff = 0;
    power = 0;
  }

  public Term( double coeff, int power ) {
    this.coeff = coeff;
    this.power = power;
  }

  public Term( Term src ) {
    coeff = src.coeff;
    power = src.power;
  }
  
  /**
   * Returns a string representation of this term
   * @return string representation of this term
   */
  public String toString() {
	  if (coeff > 1 && power > 1) {
		  return "" + coeff + "x^" + power;
	  }
	  else if (coeff == 1 && power == 1) {
		  return "x";
	  }
	  else if (coeff == 1 && power > 1) {
		  return "x^" + power;
	  }
	  else if (coeff > 1 && power == 1) {
		  return "" + coeff + "x";
	  }
	  else if (coeff >= 1 && power == 0) {
		  return "" + coeff;
	  }
	  else if (coeff < -1 && power > 1) {
		  return "" + coeff + "x^" + power;
	  }
	  else if (coeff == -1 && power == 1) {
		  return "-x";
	  }
	  else if (coeff == -1 && power > 1) {
		  return "-x^" + power;
	  }
	  else if (coeff < -1 && power == 1) {
		  return "" + coeff + "x";
	  }
	  else if (coeff <= -1 && power == 0) {
		  return "" + coeff;
	  }
	  else {
		  return "";
	  }
  }

}