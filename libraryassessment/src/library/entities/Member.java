package library.entities;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class Member implements Serializable {

    private String lastName;
    private String firstName;
    private String emailAddress;
    private int phoneNumber;
    private int memberId;
    private double finesOwing;
    
    private Map<Integer, Loan> currentLoans;

    
    public Member(String lastName, String firstName, String emailAddress, int phoneNumber, int memberId) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.memberId = memberId;
        
        this.currentLoans = new HashMap<>();
    }

    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Member:  ").append(memberId).append("\n")
          .append("  Name:  ").append(lastName).append(", ").append(firstName).append("\n")
          .append("  Email: ").append(emailAddress).append("\n")
          .append("  Phone: ").append(phoneNumber)
          .append("\n")
          .append(String.format("  Fines Owed :  $%.2f", finesOwing))
          .append("\n");
        
        for (Loan loan : currentLoans.values()) {
            sb.append(loan).append("\n");
        }          
        return sb.toString();
    }

    
    public int getId() {
        return memberId;
    }

    
    public List<Loan> getLoans() {
        return new ArrayList<Loan>(currentLoans.values());
    }

    
    public int getNumberOfCurrentLoans() {
        return currentLoans.size();
    }

    
    public double finesOwned() {
        return finesOwing;
    }

    
    public void takeOutLoan(Loan loan) {
        if (!currentLoans.containsKey(loan.getId()))
            currentLoans.put(loan.getId(), loan);
        
        else
            throw new RuntimeException("Duplicate loan added to member");
                
    }

    
    public String getLastName() {
        return lastName;
    }

    
    public String getFirstName() {
        return firstName;
    }


    public void addFine(double fine) {
        finesOwing += fine;
    }
    
    public double payFine(double amount) {
        if (amount < 0)
            throw new RuntimeException("Member.payFine: amount must be positive");
        
        double change = 0;
        if (amount > finesOwing) {
            change = amount - finesOwing;
            finesOwing = 0;
        }
        else
            finesOwing -= amount;
        
        return change;
    }


    public void dischargeLoan(Loan loan) {
        if (currentLoans.containsKey(loan.getId()))
            currentLoans.remove(loan.getId());
        
        else
            throw new RuntimeException("No such loan held by member");
                
    }

}
