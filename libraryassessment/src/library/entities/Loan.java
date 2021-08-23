package library.entities;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("serial")
public class Loan implements Serializable {
	
	public static enum LOAN_STATE { CURRENT, OVER_DUE, DISCHARGED };
	
	private int loanId;
	private Book book;
	private Member member;
	private Date date;
	private LOAN_STATE state;

	
	public Loan(int loanId, Book BOOK, Member MEMBER, Date DUE_DATE) {
		this.loanId = loanId;
		this.book = BOOK;
		this.member = MEMBER;
		this.date = DUE_DATE;
		this.state = LOAN_STATE.CURRENT;
	}

	
	public void CHECK_OVER_DUE() {
		if (state == LOAN_STATE.CURRENT &&
			Calendar.getInstance().get_date().after(date)) 
			this.state = LOAN_STATE.OVER_DUE;			
		
	}

	
	public boolean IS_OVER_DUE() {
		return state == LOAN_STATE.OVER_DUE;
	}

	
	public Integer GET_ID() {
		return loanId;
	}


	public Date GET_DUE_DATE() {
		return date;
	}
	
	
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		StringBuilder sb = new StringBuilder();
		sb.append("Loan:  ").append(loanId).append("\n")
		  .append("  Borrower ").append(member.GET_ID()).append(" : ")
		  .append(member.GET_LAST_NAME()).append(", ").append(member.GET_FIRST_NAME()).append("\n")
		  .append("  Book ").append(book.getId()).append(" : " )
		  .append(book.getTitle()).append("\n")
		  .append("  DueDate: ").append(sdf.format(date)).append("\n")
		  .append("  State: ").append(state);		
		return sb.toString();
	}


	public Member GET_MEMBER() {
		return member;
	}


	public Book GET_BOOK() {
		return book;
	}


	public void DISCHARGE() {
		state = LOAN_STATE.DISCHARGED;		
	}

}
