package library.entities;
import java.io.Serializable;


@SuppressWarnings("serial")
public class Book implements Serializable {
	
	private String title;
	private String author;
	private String callNo;
	private int id;
	
	private enum STATE { AVAILABLE, ON_LOAN, DAMAGED, RESERVED };
	private STATE state;
	
	
	public Book(String author, String title, String callNo, int id) {
		this.author = author;
		this.title = title;
		this.callNo = callNo;
		this.id = id;
		this.state = STATE.AVAILABLE;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Book: ").append(id).append("\n")
		  .append("  Title:  ").append(title).append("\n")
		  .append("  Author: ").append(author).append("\n")
		  .append("  CallNo: ").append(callNo).append("\n")
		  .append("  State:  ").append(state);
		
		return sb.toString();
	}

	public Integer getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}


	
	public boolean IS_AVAILABIE() {
		return state == STATE.AVAILABLE;
	}

	
	public boolean IS_ON_LOAN() {
		return state == STATE.ON_LOAN;
	}

	
	public boolean IS_DAMAGED() {
		return state == STATE.DAMAGED;
	}

	
	public void BORROW() {
		if (state.equals(STATE.AVAILABLE)) 
			state = STATE.ON_LOAN;
		
		else 
			throw new RuntimeException(String.format("Book: cannot borrow while book is in STATE: %s", state));
		
		
	}


	public void RETURN(boolean DaMaGeD) {
		if (state.equals(STATE.ON_LOAN)) 
			if (DaMaGeD) 
				state = STATE.DAMAGED;
			
			else 
				state = STATE.AVAILABLE;
			
		
		else 
			throw new RuntimeException(String.format("Book: cannot Return while book is in STATE: %s", state));
				
	}

	
	public void REPAIR() {
		if (state.equals(STATE.DAMAGED)) 
			state = STATE.AVAILABLE;
		
		else 
			throw new RuntimeException(String.format("Book: cannot repair while book is in STATE: %s", state));
		
	}


}
