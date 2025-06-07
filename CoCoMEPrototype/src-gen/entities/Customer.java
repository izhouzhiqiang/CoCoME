package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class Customer implements Serializable {
	
	/* all primary attributes */
	private int Id;
	private String Name;
	private String ContactInfo;
	private String MembershipLevel;
	
	/* all references */
	private List<Sale> Sales = new LinkedList<Sale>(); 
	private List<ReturnRequest> ReturnRequests = new LinkedList<ReturnRequest>(); 
	
	/* all get and set functions */
	public int getId() {
		return Id;
	}	
	
	public void setId(int id) {
		this.Id = id;
	}
	public String getName() {
		return Name;
	}	
	
	public void setName(String name) {
		this.Name = name;
	}
	public String getContactInfo() {
		return ContactInfo;
	}	
	
	public void setContactInfo(String contactinfo) {
		this.ContactInfo = contactinfo;
	}
	public String getMembershipLevel() {
		return MembershipLevel;
	}	
	
	public void setMembershipLevel(String membershiplevel) {
		this.MembershipLevel = membershiplevel;
	}
	
	/* all functions for reference*/
	public List<Sale> getSales() {
		return Sales;
	}	
	
	public void addSales(Sale sale) {
		this.Sales.add(sale);
	}
	
	public void deleteSales(Sale sale) {
		this.Sales.remove(sale);
	}
	public List<ReturnRequest> getReturnRequests() {
		return ReturnRequests;
	}	
	
	public void addReturnRequests(ReturnRequest returnrequest) {
		this.ReturnRequests.add(returnrequest);
	}
	
	public void deleteReturnRequests(ReturnRequest returnrequest) {
		this.ReturnRequests.remove(returnrequest);
	}
	

	/* invarints checking*/
	public boolean Customer_UniqueCustomerId() {
		
		if (StandardOPs.isUnique(((List<Customer>)EntityManager.getAllInstancesOf("Customer")), "Id")) {
			return true;
		} else {
			return false;
		}
	}
	
	//check all invariants
	public boolean checkAllInvairant() {
		
		if (Customer_UniqueCustomerId()) {
			return true;
		} else {
			return false;
		}
	}	
	
	//check invariant by inv name
	public boolean checkInvariant(String INVname) {
		
		try {
			Method m = this.getClass().getDeclaredMethod(INVname);
			return (boolean) m.invoke(this);
					
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	
	}	
	
	public final static ArrayList<String> allInvariantCheckingFunction = new ArrayList<String>(Arrays.asList("Customer_UniqueCustomerId"));

}
