package library.returnBook;
import java.util.Scanner;


public class ReturnBookUI {

	public static enum UI_state { INITIALISED, READY, INSPECTING, COMPLETED };

	private ReturnBookControl control;
	private Scanner input;
	private UI_state state;

	
	public ReturnBookUI(ReturnBookControl control) {
		this.control = control;
		input = new Scanner(System.in);
		state = UI_state.INITIALISED;
		control.set_ui(this);
	}


	public void run() {		
		output("Return Book Use Case UI\n");
		
		while (true) {
			
			switch (state) {
			
			case INITIALISED:
				break;
				
			case READY:
				String book_input_string = input("Scan Book (<enter> completes): ");
				if (book_input_string.length() == 0) 
					control.scanning_complete();
				
				else {
					try {
						int book_ID = Integer.valueOf(book_input_string).intValue();
						control.book_scanned(book_ID);
					}
					catch (NumberFormatException e) {
						output("Invalid bookId");
					}					
				}
				break;				
				
			case INSPECTING:
				String ans = input("Is book damaged? (Y/N): ");
				boolean is_damaged = false;
				if (ans.toUpperCase().equals("Y")) 					
					is_damaged = true;
				
				control.discharge_loan(is_damaged);
			
			case COMPLETED:
				output("Return processing complete");
				return;
			
			default:
				output("Unhandled state");
				throw new RuntimeException("ReturnBookUI : unhandled state :" + state);			
			}
		}
	}

	
	private String input(String prompt) {
		System.out.print(prompt);
		return input.nextLine();
	}	
		
		
	private void output(Object object) {
		System.out.println(object);
	}
	
			
	public void display(Object object) {
		output(object);
	}
	
	public void set_state(UI_state state) {
		this.state = state;
	}

	
}
