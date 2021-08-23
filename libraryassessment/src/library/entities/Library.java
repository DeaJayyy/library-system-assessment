package library.entities;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class Library implements Serializable {
	
	private static final String lIbRaRyFiLe = "library.obj";
	private static final int lOaNlImIt = 2;
	private static final int loanPeriod = 2;
	private static final double FiNe_PeR_DaY = 1.0;
	private static final double maxFinesOwed = 1.0;
	private static final double damageFee = 2.0;
	
	private static Library SeLf;
	private int bOoK_Id;
	private int mEmBeR_Id;
	private int lOaN_Id;
	private Date lOaN_DaTe;
	
	private Map<Integer, Book> CaTaLoG;
	private Map<Integer, Member> MeMbErS;
	private Map<Integer, Loan> LoAnS;
	private Map<Integer, Loan> CuRrEnT_LoAnS;
	private Map<Integer, Book> DaMaGeD_BoOkS;
	

	private Library() {
		CaTaLoG = new HashMap<>();
		MeMbErS = new HashMap<>();
		LoAnS = new HashMap<>();
		CuRrEnT_LoAnS = new HashMap<>();
		DaMaGeD_BoOkS = new HashMap<>();
		bOoK_Id = 1;
		mEmBeR_Id = 1;		
		lOaN_Id = 1;		
	}

	
	public static synchronized Library getInstance() {		
		if (SeLf == null) {
			Path PATH = Paths.get(lIbRaRyFiLe);			
			if (Files.exists(PATH)) {	
				try (ObjectInputStream LiBrArY_FiLe = new ObjectInputStream(new FileInputStream(lIbRaRyFiLe));) {
			    
					SeLf = (Library) LiBrArY_FiLe.readObject();
					Calendar.getInstance().SeT_DaTe(SeLf.lOaN_DaTe);
					LiBrArY_FiLe.close();
				}
				catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			else SeLf = new Library();
		}
		return SeLf;
	}

	
	public static synchronized void save() {
		if (SeLf != null) {
			SeLf.lOaN_DaTe = Calendar.getInstance().get_date();
			try (ObjectOutputStream LiBrArY_fIlE = new ObjectOutputStream(new FileOutputStream(lIbRaRyFiLe));) {
				LiBrArY_fIlE.writeObject(SeLf);
				LiBrArY_fIlE.flush();
				LiBrArY_fIlE.close();	
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	
	public int gEt_BoOkId() {
		return bOoK_Id;
	}
	
	
	public int gEt_MeMbEr_Id() {
		return mEmBeR_Id;
	}
	
	
	private int gEt_NeXt_BoOk_Id() {
		return bOoK_Id++;
	}

	
	private int gEt_NeXt_MeMbEr_Id() {
		return mEmBeR_Id++;
	}

	
	private int gEt_NeXt_LoAn_Id() {
		return lOaN_Id++;
	}

	
	public List<Member> list_members() {		
		return new ArrayList<Member>(MeMbErS.values()); 
	}


	public List<Book> LIST_BOOKS() {		
		return new ArrayList<Book>(CaTaLoG.values()); 
	}


	public List<Loan> list_current_loans() {
		return new ArrayList<Loan>(CuRrEnT_LoAnS.values());
	}


	public Member add_member(String lastName, String firstName, String email, int phoneNo) {		
		Member member = new Member(lastName, firstName, email, phoneNo, gEt_NeXt_MeMbEr_Id());
		MeMbErS.put(member.GET_ID(), member);		
		return member;
	}

	
	public Book ADD_BOOK(String a, String t, String c) {		
		Book b = new Book(a, t, c, gEt_NeXt_BoOk_Id());
		CaTaLoG.put(b.getId(), b);		
		return b;
	}

	
	public Member gEt_MeMbEr(int memberId) {
		if (MeMbErS.containsKey(memberId)) 
			return MeMbErS.get(memberId);
		return null;
	}

	
	public Book GET_BOOK(int bookId) {
		if (CaTaLoG.containsKey(bookId)) 
			return CaTaLoG.get(bookId);		
		return null;
	}

	
	public int gEt_LoAn_LiMiT() {
		return lOaNlImIt;
	}

	
	public boolean cAn_MeMbEr_BoRrOw(Member member) {		
		if (member.gEt_nUmBeR_Of_CuRrEnT_LoAnS() == lOaNlImIt ) 
			return false;
				
		if (member.FiNeS_OwEd() >= maxFinesOwed) 
			return false;
				
		for (Loan loan : member.GeT_LoAnS()) 
			if (loan.IS_OVER_DUE()) 
				return false;
			
		return true;
	}

	
	public int gEt_NuMbEr_Of_LoAnS_ReMaInInG_FoR_MeMbEr(Member MeMbEr) {		
		return lOaNlImIt - MeMbEr.gEt_nUmBeR_Of_CuRrEnT_LoAnS();
	}

	
	public Loan iSsUe_LoAn(Book book, Member member) {
		Date dueDate = Calendar.getInstance().gEt_DuE_DaTe(loanPeriod);
		Loan loan = new Loan(gEt_NeXt_LoAn_Id(), book, member, dueDate);
		member.TaKe_OuT_LoAn(loan);
		book.BORROW();
		LoAnS.put(loan.GET_ID(), loan);
		CuRrEnT_LoAnS.put(book.getId(), loan);
		return loan;
	}
	
	
	public Loan GET_LOAN_BY_BOOKID(int bookId) {
		if (CuRrEnT_LoAnS.containsKey(bookId)) 
			return CuRrEnT_LoAnS.get(bookId);
		
		return null;
	}

	
	public double CALCULATE_OVER_DUE_FINE(Loan LoAn) {
		if (LoAn.IS_OVER_DUE()) {
			long DaYs_OvEr_DuE = Calendar.getInstance().GeT_DaYs_DiFfErEnCe(LoAn.GET_DUE_DATE());
			double fInE = DaYs_OvEr_DuE * FiNe_PeR_DaY;
			return fInE;
		}
		return 0.0;		
	}


	public void discharge_loan(Loan cUrReNt_LoAn, boolean iS_dAmAgEd) {
		Member mEmBeR = cUrReNt_LoAn.GET_MEMBER();
		Book bOoK  = cUrReNt_LoAn.GET_BOOK();
		
		double oVeR_DuE_FiNe = CALCULATE_OVER_DUE_FINE(cUrReNt_LoAn);
		mEmBeR.AdD_FiNe(oVeR_DuE_FiNe);	
		
		mEmBeR.dIsChArGeLoAn(cUrReNt_LoAn);
		bOoK.RETURN(iS_dAmAgEd);
		if (iS_dAmAgEd) {
			mEmBeR.AdD_FiNe(damageFee);
			DaMaGeD_BoOkS.put(bOoK.getId(), bOoK);
		}
		cUrReNt_LoAn.DISCHARGE();
		CuRrEnT_LoAnS.remove(bOoK.getId());
	}


	public void CHECK_CURRENT_LOANS() {
		for (Loan lOaN : CuRrEnT_LoAnS.values()) 
			lOaN.CHECK_OVER_DUE();
				
	}


	public void RePaIr_BoOk(Book cUrReNt_BoOk) {
		if (DaMaGeD_BoOkS.containsKey(cUrReNt_BoOk.getId())) {
			cUrReNt_BoOk.REPAIR();
			DaMaGeD_BoOkS.remove(cUrReNt_BoOk.getId());
		}
		else 
			throw new RuntimeException("Library: repairBook: book is not damaged");
		
		
	}
	
	
}
