package library.returnBook;
import library.entities.Book;
import library.entities.library;
import library.entities.Loan;

public class ReturnBookControl {

	private ReturnBookUI UI;
	private enum control_state { INITIALISED, READY, INSPECTING };
	private control_state state;
	
	private library library;
	private Loan current_loan;
	

	public ReturnBookControl() {
		this.library = library.getInstance();
		state = control_state.INITIALISED;
	}
	
	
	public void sEt_UI(ReturnBookUI UI) {
		if (!state.equals(control_state.INITIALISED)) 
			throw new RuntimeException("ReturnBookControl: cannot call setUI except in INITIALISED state");
		
		this.UI = UI;
		UI.set_state(ReturnBookUI.UI_state.READY);
		state = control_state.READY;		
	}


	public void book_scanned(int book_ID) {
		if (!state.equals(control_state.READY)) 
			throw new RuntimeException("ReturnBookControl: cannot call bookScanned except in READY state");
		
		Book current_book = library.get_book(book_ID);
		
		if (current_book == null) {
			UI.display("Invalid Book Id");
			return;
		}
		if (!current_book.is_on_loan()) {
			UI.display("Book has not been borrowed");
			return;
		}		
		current_loan = library.get_loan_by_bookID(book_ID);	
		double over_due_fine = 0.0;
		if (current_loan.is_over_due()) 
			over_due_fine = library.calculate_over_due_fine(current_loan);
		
		UI.display("Inspecting");
		UI.display(current_book.toString());
		UI.display(current_loan.toString());
		
		if (current_loan.is_over_due()) 
			UI.display(String.format("\nOverdue fine : $%.2f", over_due_fine));
		
		UI.set_state(ReturnBookUI.UI_state.INSPECTING);
		state = control_state.INSPECTING;		
	}


	public void scanning_complete() {
		if (!state.equals(control_state.READY)) 
			throw new RuntimeException("ReturnBookControl: cannot call scanningComplete except in READY state");
			
		UI.set_state(ReturnBookUI.UI_state.COMPLETED);		
	}


	public void discharge_loan(boolean is_damaged) {
		if (!state.equals(control_state.INSPECTING)) 
			throw new RuntimeException("ReturnBookControl: cannot call dischargeLoan except in INSPECTING state");
		
		library.discharge_loan(current_loan, is_damaged);
		current_loan = null;
		UI.set_state(ReturnBookUI.UI_state.READY);
		state = control_state.READY;				
	}


}
