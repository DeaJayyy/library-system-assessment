package library.returnBook;
import library.entities.Book;
import library.entities.Library;
import library.entities.Loan;

public class ReturnBookControl {

	private ReturnBookUI UI;
	private enum controlState { INITIALISED, READY, INSPECTING };
	private controlState state;
	
	private Library library;
	private Loan currentLoan;
	

	public ReturnBookControl() {
		this.library = library.getInstance();
		state = controlState.INITIALISED;
	}
	
	
	public void setUi(ReturnBookUI UI) {
		if (!state.equals(controlState.INITIALISED)) 
			throw new RuntimeException("ReturnBookControl: cannot call setUI except in INITIALISED state");
		
		this.UI = UI;
		UI.setState(ReturnBookUI.UI_state.READY);
		state = controlState.READY;		
	}


	public void bookScanned(int book_ID) {
		if (!state.equals(controlState.READY)) 
			throw new RuntimeException("ReturnBookControl: cannot call bookScanned except in READY state");
		
		Book currentBook = library.getBook(book_ID);
		
		if (currentBook == null) {
			UI.display("Invalid Book Id");
			return;
		}
		if (!currentBook.isOnLoan()) {
			UI.display("Book has not been borrowed");
			return;
		}		
		currentLoan = library.getLoanByBookId(book_ID);	
		double over_due_fine = 0.0;
		if (currentLoan.isOverDue()) 
			over_due_fine = library.calculateOverdueFine(currentLoan);
		
		UI.display("Inspecting");
		UI.display(currentBook.toString());
		UI.display(currentLoan.toString());
		
		if (currentLoan.isOverDue()) 
			UI.display(String.format("\nOverdue fine : $%.2f", over_due_fine));
		
		UI.setState(ReturnBookUI.UI_state.INSPECTING);
		state = controlState.INSPECTING;		
	}


	public void scanningComplete() {
		if (!state.equals(controlState.READY)) 
			throw new RuntimeException("ReturnBookControl: cannot call scanningComplete except in READY state");
			
		UI.setState(ReturnBookUI.UI_state.COMPLETED);		
	}


	public void dischargeLoan(boolean is_damaged) {
		if (!state.equals(controlState.INSPECTING)) 
			throw new RuntimeException("ReturnBookControl: cannot call dischargeLoan except in INSPECTING state");
		
		library.dischageLoan(currentLoan, is_damaged);
		currentLoan = null;
		UI.setState(ReturnBookUI.UI_state.READY);
		state = controlState.READY;				
	}


}
