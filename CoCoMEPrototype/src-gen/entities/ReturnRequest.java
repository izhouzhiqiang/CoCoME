package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class ReturnRequest implements Serializable {
	
	/* all primary attributes */
	private int RequestId;
	private int OrderId;
	private int ItemId;
	private LocalDate ReturnTime;
	private String ReturnReason;
	private boolean IsApproved;
	private boolean IsReviewed;
	
	/* all references */
	private Customer Customer; 
	private Sale Sale; 
	private Item Item; 
	
	/* all get and set functions */
	public int getRequestId() {
		return RequestId;
	}	
	
	public void setRequestId(int requestid) {
		this.RequestId = requestid;
	}
	public int getOrderId() {
		return OrderId;
	}	
	
	public void setOrderId(int orderid) {
		this.OrderId = orderid;
	}
	public int getItemId() {
		return ItemId;
	}	
	
	public void setItemId(int itemid) {
		this.ItemId = itemid;
	}
	public LocalDate getReturnTime() {
		return ReturnTime;
	}	
	
	public void setReturnTime(LocalDate returntime) {
		this.ReturnTime = returntime;
	}
	public String getReturnReason() {
		return ReturnReason;
	}	
	
	public void setReturnReason(String returnreason) {
		this.ReturnReason = returnreason;
	}
	public boolean getIsApproved() {
		return IsApproved;
	}	
	
	public void setIsApproved(boolean isapproved) {
		this.IsApproved = isapproved;
	}
	public boolean getIsReviewed() {
		return IsReviewed;
	}	
	
	public void setIsReviewed(boolean isreviewed) {
		this.IsReviewed = isreviewed;
	}
	
	/* all functions for reference*/
	public Customer getCustomer() {
		return Customer;
	}	
	
	public void setCustomer(Customer customer) {
		this.Customer = customer;
	}			
	public Sale getSale() {
		return Sale;
	}	
	
	public void setSale(Sale sale) {
		this.Sale = sale;
	}			
	public Item getItem() {
		return Item;
	}	
	
	public void setItem(Item item) {
		this.Item = item;
	}			
	

	/* invarints checking*/
	public boolean ReturnRequest_UniqueReturnRequestId() {
		
		if (StandardOPs.isUnique(((List<ReturnRequest>)EntityManager.getAllInstancesOf("ReturnRequest")), "RequestId")) {
			return true;
		} else {
			return false;
		}
	}
	
	//check all invariants
	public boolean checkAllInvairant() {
		
		if (ReturnRequest_UniqueReturnRequestId()) {
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
	
	public final static ArrayList<String> allInvariantCheckingFunction = new ArrayList<String>(Arrays.asList("ReturnRequest_UniqueReturnRequestId"));

}
