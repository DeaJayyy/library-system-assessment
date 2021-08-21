package library.returnBook;
import library.entities.Book;
import library.entities.Library;
import library.entities.Loan;

public class ReturnBookControl {

	private ReturnBookUI UI;
	private enum control_state { INITIALISED, READY, INSPECTING };
	private control_state state;
	
	private Library library;
	private Loan current_loan;
	

	public ReturnBookControl() {
		this.library = library.getInstance();
		state = control_state.INITIALISED;
	}
	
	
	public void set_UI(ReturnBookUI UI) {
		if (!state.equals(control_state.INITIALISED)) 
			throw new RuntimeException("ReturnBookControl: cannot call setUI except in INITIALISED state");
		
		this.UI = UI;
		UI.set_state(ReturnBookUI.UI_state.READY);
		state = control_state.READY;		
	}


	public void book_scanned(int book_ID) {
		if (!state.equals(control_state.READY)) 
			throw new RuntimeException("ReturnBookControl: cannot call bookScanned except in READY state");
		
		Book current_book = library.GET_BOOK(book_ID);
		
		if (current_book == null) {
			UI.display("Invalid Book Id");
			return;
		}
		if (!current_book.IS_ON_LOAN()) {
			UI.display("Book has not been borrowed");
			return;
		}		
		current_loan = library.GET_LOAN_BY_BOOKID(book_ID);	
		double over_due_fine = 0.0;
		if (current_loan.IS_OVER_DUE()) 
			over_due_fine = library.CALCULATE_OVER_DUE_FINE(current_loan);
		
		UI.display("Inspecting");
		UI.display(current_book.toString());
		UI.display(current_loan.toString());
		
		if (current_loan.IS_OVER_DUE()) 
			UI.display(String.format("\nOverdue fine : $%.2f", over_due_fine));
		
		UI.set_state(ReturnBookUI.UI_state.INSPECTING);
		state = control_state.INSPECTING;		
	}


	public void scanning_complete() {
		if (!state.equals(control_state.READY)) 
			throw new RuntimeException("ReturnBookControl: cannot call scanningComplete except in READY state");
			
		UI.set_state(ReturnBookUI.UI_state.COMPLETED);		
	}


	public void DISCHARGE_LOAN(boolean is_damaged) {
		if (!state.equals(control_state.INSPECTING)) 
			throw new RuntimeException("ReturnBookControl: cannot call dischargeLoan except in INSPECTING state");
		
		library.DISCHARGE_LOAN(current_loan, is_damaged);
		current_loan = null;
		UI.set_state(ReturnBookUI.UI_state.READY);
		state = control_state.READY;				
	}


}
