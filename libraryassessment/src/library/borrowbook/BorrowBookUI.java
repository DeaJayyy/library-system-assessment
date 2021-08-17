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
		control.SeT_Ui(this);
	}

	
	private String iNpUT(String prompt) {
		System.out.print(prompt);
		return input.nextLine();
	}	
		
		
	private void OuTpUt(Object object) {
		System.out.println(object);
	}
	
			
	public void SeT_StAtE(uI_STaTe state) {
		this.state = state;
	}

	
	public void RuN() {
		OuTpUt("Borrow Book Use Case UI\n");
		
		while (true) {
			
			switch (state) {			
			
			case CANCELLED:
				OuTpUt("Borrowing Cancelled");
				return;

				
			case READY:
				String memStr = iNpUT("Swipe member card (press <enter> to cancel): ");
				if (memStr.length() == 0) {
					control.CaNcEl();
					break;
				}
				try {
					int memberId = Integer.valueOf(memStr).intValue();
					control.SwIpEd(memberId);
				}
				catch (NumberFormatException e) {
					OuTpUt("Invalid Member Id");
				}
				break;

				
			case RESTRICTED:
				iNpUT("Press <any key> to cancel");
				control.CaNcEl();
				break;
			
				
			case SCANNING:
				String bookStringInput = iNpUT("Scan Book (<enter> completes): ");
				if (bookStringInput.length() == 0) {
					control.CoMpLeTe();
					break;
				}
				try {
					int bId = Integer.valueOf(bookStringInput).intValue();
					control.ScAnNeD(bId);
					
				} catch (NumberFormatException e) {
					OuTpUt("Invalid Book Id");
				} 
				break;
					
				
			case FINALISING:
				String ans = iNpUT("Commit loans? (Y/N): ");
				if (ans.toUpperCase().equals("N")) {
					control.CaNcEl();
					
				} else {
					control.CoMmIt_LoAnS();
					iNpUT("Press <any key> to complete ");
				}
				break;
				
				
			case COMPLETED:
				OuTpUt("Borrowing Completed");
				return;
	
				
			default:
				OuTpUt("Unhandled state");
				throw new RuntimeException("BorrowBookUI : unhandled state :" + state);			
			}
		}		
	}


	public void DiSpLaY(Object object) {
		OuTpUt(object);		
	}


}
