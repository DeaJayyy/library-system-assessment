package library;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import library.borrowbook.BorrowBookUI;
import library.borrowbook.borrow_book_control;
import library.entities.Book;
import library.entities.Calendar;
import library.entities.Library;
import library.entities.Loan;
import library.entities.Member;
import library.fixbook.FixBookUI;
import library.fixbook.fix_book_control;
import library.payfine.PayFineUI;
import library.payfine.pay_fine_control;
import library.returnBook.ReturnBookUI;
import library.returnBook.ReturnBookControl
        ;


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
	
			for (Member m : LIB.list_members()) {
				output(m);
			}
			output(" ");
			for (Book b : LIB.LIST_BOOKS()) {
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
					list_members();
					break;
					
				case "B": 
					ADD_BOOK();
					break;
					
				case "LB": 
					LIST_BOOKS();
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
					list_current_loans();
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
				
				Library.save();
			}			
		} catch (RuntimeException e) {
			output(e);
		}		
		output("\nEnded\n");
	}	

	
	private static void PAY_FINES() {
		new PayFineUI(new pay_fine_control()).run();		
	}


	private static void list_current_loans() {
		output("");
		for (Loan loan : LIB.list_current_loans()) {
			output(loan + "\n");
		}		
	}



	private static void LIST_BOOKS() {
		output("");
		for (Book Book : LIB.LIST_BOOKS()) {
			output(Book + "\n");
		}		
	}



	private static void list_members() {
		output("");
		for (Member Member : LIB.list_members()) {
			output(Member + "\n");
		}		
	}



	private static void BORROW_BOOK() {
		new BorrowBookUI(new borrow_book_control()).run();		
	}


	private static void RETURN_BOOK() {
		new ReturnBookUI(new ReturnBookControl()).run();		
	}


	private static void FIX_BOOKS() {
		new FixBookUI(new fix_book_control()).run();		
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
