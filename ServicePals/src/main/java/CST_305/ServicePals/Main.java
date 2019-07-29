package CST_305.ServicePals;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
	
	static Scanner scan = new Scanner(System.in);

	public static void main(String[] args) throws FileNotFoundException {
		
		Prompts prompt = new Prompts();
		prompt.createCommunities();
		prompt.mainMenu();

	}
 
}
