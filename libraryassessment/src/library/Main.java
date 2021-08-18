package library;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import library.borrowBook.BorrowBookUI;
import library.borrowBook.BORROW_BOOK_CONTROL;
import library.entities.Book;
import library.entities.Calendar;
import library.entities.Library;
import library.entities.Loan;
import library.entities.Member;
import library.fixBook.FixBookUI;
import library.fixBook.FIX_Book_CONTROL;
import library.payfine.PayFineUI;
import library.payfine.pay_fine_control;
import library.returnBook.ReturnBookUI;
import library.returnBook.ReturnBookControl;


public class Main {
	
	private static Scanner IN;
	private static Library LIB;
	private static String MENU;
	private static Calendar CAL;
	private static SimpleDateFormat SDF;
	
	
	private static String Get_menu() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("\nLibrary Main Menu\n\n")
		  .append("  M  : add Member\n")
		  .append("  LM : list Members\n")
		  .append("\n")
		  .append("  B  : add Book\n")
		  .append("  LB : list Books\n")
		  .append("  FB : fix Books\n")
		  .append("\n")
		  .append("  L  : take out a loan\n")
		  .append("  R  : return a loan\n")
		  .append("  LL : list loans\n")
		  .append("\n")
		  .append("  P  : pay fine\n")
		  .append("\n")
		  .append("  T  : increment date\n")
		  .append("  Q  : quit\n")
		  .append("\n")
		  .append("Choice : ");
		  
		return sb.toString();
	}


	public static void main(String[] args) {		
		try {			
			IN = new Scanner(System.in);
			LIB = Library.getInstance();
			CAL = Calendar.getInstance();
			SDF = new SimpleDateFormat("dd/MM/yyyy");
	
			for (Member m : LIB.LIST_MemberS()) {
				output(m);
			}
			output(" ");
			for (Book b : LIB.LIST_BookS()) {
				output(b);
			}
						
			MENU = Get_menu();
			
			boolean e = false;
			
			while (!e) {
				
				output("\n" + SDF.format(CAL.get_date()));
				String c = input(MENU);
				
				switch (c.toUpperCase()) {
				
				case "M": 
					ADD_MEMBER();
					break;
					
				case "LM": 
					LIST_MemberS();
					break;
					
				case "B": 
					ADD_BOOK();
					break;
					
				case "LB": 
					LIST_BookS();
					break;
					
				case "FB": 
					FIX_BOOKS();
					break;
					
				case "L": 
					BORROW_BOOK();
					break;
					
				case "R": 
					RETURN_BOOK();
					break;
					
				case "LL": 
					LIST_CURRENT_LOANS();
					break;
					
				case "P": 
					PAY_FINES();
					break;
					
				case "T": 
					INCREMENT_DATE();
					break;
					
				case "Q": 
					e = true;
					break;
					
				default: 
					output("\nInvalid option\n");
					break;
				}
				
				Library.Save();
			}			
		} catch (RuntimeException e) {
			output(e);
		}		
		output("\nEnded\n");
	}	

	
	private static void PAY_FINES() {
		new PayFineUI(new pay_fine_control()).RUN();		
	}


	private static void LIST_CURRENT_LOANS() {
		output("");
		for (Loan loan : LIB.LIST_CURRENT_LOANS()) {
			output(loan + "\n");
		}		
	}



	private static void LIST_BookS() {
		output("");
		for (Book Book : LIB.LIST_BookS()) {
			output(Book + "\n");
		}		
	}



	private static void LIST_MemberS() {
		output("");
		for (Member Member : LIB.LIST_MemberS()) {
			output(Member + "\n");
		}		
	}



	private static void BORROW_BOOK() {
		new BorrowBookUI(new BORROW_BOOK_CONTROL()).RUN();		
	}


	private static void RETURN_BOOK() {
		new ReturnBookUI(new ReturnBookControlL()).RUN();		
	}


	private static void FIX_BOOKS() {
		new FixBookUI(new FIX_Book_CONTROL()).RUN();		
	}


	private static void INCREMENT_DATE() {
		try {
			int days = Integer.valueOf(input("Enter number of days: ")).intValue();
			CAL.incrementDate(days);
			LIB.CHECK_CURRENT_LOANS();
			output(SDF.format(CAL.get_date()));
			
		} catch (NumberFormatException e) {
			 output("\nInvalid number of days\n");
		}
	}


	private static void ADD_BOOK() {
		
		String Author = input("Enter Author: ");
		String Title  = input("Enter Title: ");
		String call_number = input("Enter call number: ");
		Book Book = LIB.ADD_BOOK(Author, Title, call_number);
		output("\n" + Book + "\n");
		
	}

	
	private static void ADD_MEMBER() {
		try {
			String LAST_NAME = input("Enter last name: ");
			String FIRST_NAME  = input("Enter first name: ");
			String EMAIL_ADDRESS = input("Enter email address: ");
			int PHONE_NUMBER = Integer.valueOf(input("Enter phone number: ")).intValue();
			Member Member = LIB.ADD_MEMBER(LAST_NAME, FIRST_NAME, EMAIL_ADDRESS, PHONE_NUMBER);
			output("\n" + Member + "\n");
			
		} catch (NumberFormatException e) {
			 output("\nInvalid phone number\n");
		}
		
	}


	private static String input(String prompt) {
		System.out.print(prompt);
		return IN.nextLine();
	}
	
	
	
	private static void output(Object object) {
		System.out.println(object);
	}

	
}
