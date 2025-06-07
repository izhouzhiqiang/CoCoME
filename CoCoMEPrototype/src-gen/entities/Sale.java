package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class Sale implements Serializable {
	
	/* all primary attributes */
	private LocalDate Time;
	private boolean IsComplete;
	private float Amount;
	private boolean IsReadytoPay;
	private String PaymentMethod;
	private boolean IsPaid;
	private String PaymentDetails;
	private int OrderId;
	
	/* all references */
	private Store Belongedstore; 
	private CashDesk BelongedCashDesk; 
	private List<SalesLineItem> ContainedSalesLine = new LinkedList<SalesLineItem>(); 
	private Payment AssoicatedPayment; 
	
	/* all get and set functions */
	public LocalDate getTime() {
		return Time;
	}	
	
	public void setTime(LocalDate time) {
		this.Time = time;
	}
	public boolean getIsComplete() {
		return IsComplete;
	}	
	
	public void setIsComplete(boolean iscomplete) {
		this.IsComplete = iscomplete;
	}
	public float getAmount() {
		return Amount;
	}	
	
	public void setAmount(float amount) {
		this.Amount = amount;
	}
	public boolean getIsReadytoPay() {
		return IsReadytoPay;
	}	
	
	public void setIsReadytoPay(boolean isreadytopay) {
		this.IsReadytoPay = isreadytopay;
	}
	public String getPaymentMethod() {
		return PaymentMethod;
	}	
	
	public void setPaymentMethod(String paymentmethod) {
		this.PaymentMethod = paymentmethod;
	}
	public boolean getIsPaid() {
		return IsPaid;
	}	
	
	public void setIsPaid(boolean ispaid) {
		this.IsPaid = ispaid;
	}
	public String getPaymentDetails() {
		return PaymentDetails;
	}	
	
	public void setPaymentDetails(String paymentdetails) {
		this.PaymentDetails = paymentdetails;
	}
	public int getOrderId() {
		return OrderId;
	}	
	
	public void setOrderId(int orderid) {
		this.OrderId = orderid;
	}
	
	/* all functions for reference*/
	public Store getBelongedstore() {
		return Belongedstore;
	}	
	
	public void setBelongedstore(Store store) {
		this.Belongedstore = store;
	}			
	public CashDesk getBelongedCashDesk() {
		return BelongedCashDesk;
	}	
	
	public void setBelongedCashDesk(CashDesk cashdesk) {
		this.BelongedCashDesk = cashdesk;
	}			
	public List<SalesLineItem> getContainedSalesLine() {
		return ContainedSalesLine;
	}	
	
	public void addContainedSalesLine(SalesLineItem saleslineitem) {
		this.ContainedSalesLine.add(saleslineitem);
	}
	
	public void deleteContainedSalesLine(SalesLineItem saleslineitem) {
		this.ContainedSalesLine.remove(saleslineitem);
	}
	public Payment getAssoicatedPayment() {
		return AssoicatedPayment;
	}	
	
	public void setAssoicatedPayment(Payment payment) {
		this.AssoicatedPayment = payment;
	}			
	

	/* invarints checking*/
	public boolean Sale_AmountGreatAndEqualZero() {
		
		if (Amount >= 0) {
			return true;
		} else {
			return false;
		}
	}
	
	//check all invariants
	public boolean checkAllInvairant() {
		
		if (Sale_AmountGreatAndEqualZero()) {
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
	
	public final static ArrayList<String> allInvariantCheckingFunction = new ArrayList<String>(Arrays.asList("Sale_AmountGreatAndEqualZero"));

}
