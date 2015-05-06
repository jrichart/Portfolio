import java.util.Scanner;


public class PolynomialDriver {
	
	public static void main(String[] args) {
	Scanner keyboard = new Scanner(System.in);
	
	Polynomial p = new Polynomial("x^3 + 6x^3 + x^2 - 4x^2 + 1 + 7 + 3x + 5x^4 - 2xs");
	
	System.out.println(p.toString());
	}
}
