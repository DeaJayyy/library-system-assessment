package library.borrowbook;
import java.util.Scanner;


public class BorrowBookUI {
	
	public static enum uI_STaTe { INITIALISED, READY, RESTRICTED, SCANNING, IDENTIFIED, FINALISING, COMPLETED, CANCELLED };

	private BorrowBookControl control;
	private Scanner input;
	private uI_STaTe state;

	
	public BorrowBookUI(BorrowBookControl control) {
		this.control = control;
		input = new Scanner(System.in);
		state = uI_STaTe.INITIALISED;
		control.setUi(this);
	}

	
	private String input(String prompt) {
		System.out.print(prompt);
		return input.nextLine();
	}	
		
		
	private void output(Object object) {
		System.out.println(object);
	}
	
			
	public void setState(uI_STaTe state) {
		this.state = state;
	}

	
	public void run() {
		output("Borrow Book Use Case UI\n");
		
		while (true) {
			
			switch (state) {			
			
			case CANCELLED:
				output("Borrowing Cancelled");
				return;

				
			case READY:
				String memStr = input("Swipe member card (press <enter> to cancel): ");
				if (memStr.length() == 0) {
					control.cancel();
					break;
				}
				try {
					int memberId = Integer.valueOf(memStr).intValue();
					control.swiped(memberId);
				}
				catch (NumberFormatException e) {
					output("Invalid Member Id");
				}
				break;

				
			case RESTRICTED:
				input("Press <any key> to cancel");
				control.cancel();
				break;
			
				
			case SCANNING:
				String bookStringInput = input("Scan Book (<enter> completes): ");
				if (bookStringInput.length() == 0) {
					control.complete();
					break;
				}
				try {
					int bId = Integer.valueOf(bookStringInput).intValue();
					control.scanned(bId);
					
				} catch (NumberFormatException e) {
					output("Invalid Book Id");
				} 
				break;
					
				
			case FINALISING:
				String ans = input("Commit loans? (Y/N): ");
				if (ans.toUpperCase().equals("N")) {
					control.cancel();
					
				} else {
					control.commitLoans();
					input("Press <any key> to complete ");
				}
				break;
				
				
			case COMPLETED:
				output("Borrowing Completed");
				return;
	
				
			default:
				output("Unhandled state");
				throw new RuntimeException("BorrowBookUI : unhandled state :" + state);			
			}
		}		
	}


	public void display(Object object) {
		output(object);		
	}


}
