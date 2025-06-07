package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class CashDesk implements Serializable {
	
	/* all primary attributes */
	private int Id;
	private String Name;
	private boolean IsOpened;
	
	/* all references */
	private List<Sale> ContainedSales = new LinkedList<Sale>(); 
	private Store BelongedStore; 
	
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
	public boolean getIsOpened() {
		return IsOpened;
	}	
	
	public void setIsOpened(boolean isopened) {
		this.IsOpened = isopened;
	}
	
	/* all functions for reference*/
	public List<Sale> getContainedSales() {
		return ContainedSales;
	}	
	
	public void addContainedSales(Sale sale) {
		this.ContainedSales.add(sale);
	}
	
	public void deleteContainedSales(Sale sale) {
		this.ContainedSales.remove(sale);
	}
	public Store getBelongedStore() {
		return BelongedStore;
	}	
	
	public void setBelongedStore(Store store) {
		this.BelongedStore = store;
	}			
	

	/* invarints checking*/
	public boolean CashDesk_UniqueCashDeskId() {
		
		if (StandardOPs.isUnique(((List<CashDesk>)EntityManager.getAllInstancesOf("CashDesk")), "Id")) {
			return true;
		} else {
			return false;
		}
	}
	
	//check all invariants
	public boolean checkAllInvairant() {
		
		if (CashDesk_UniqueCashDeskId()) {
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
	
	public final static ArrayList<String> allInvariantCheckingFunction = new ArrayList<String>(Arrays.asList("CashDesk_UniqueCashDeskId"));

}
