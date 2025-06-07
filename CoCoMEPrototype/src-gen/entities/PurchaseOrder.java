package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class PurchaseOrder implements Serializable {
	
	/* all primary attributes */
	private int OrderId;
	private int SupplierId;
	private int StoreId;
	private LocalDate OrderTime;
	private LocalDate ExpectedDeliveryTime;
	private float TotalAmount;
	private boolean IsReceived;
	private boolean IsAccepted;
	
	/* all references */
	private Supplier Supplier; 
	private Store Store; 
	private OrderProduct OrderProduct; 
	
	/* all get and set functions */
	public int getOrderId() {
		return OrderId;
	}	
	
	public void setOrderId(int orderid) {
		this.OrderId = orderid;
	}
	public int getSupplierId() {
		return SupplierId;
	}	
	
	public void setSupplierId(int supplierid) {
		this.SupplierId = supplierid;
	}
	public int getStoreId() {
		return StoreId;
	}	
	
	public void setStoreId(int storeid) {
		this.StoreId = storeid;
	}
	public LocalDate getOrderTime() {
		return OrderTime;
	}	
	
	public void setOrderTime(LocalDate ordertime) {
		this.OrderTime = ordertime;
	}
	public LocalDate getExpectedDeliveryTime() {
		return ExpectedDeliveryTime;
	}	
	
	public void setExpectedDeliveryTime(LocalDate expecteddeliverytime) {
		this.ExpectedDeliveryTime = expecteddeliverytime;
	}
	public float getTotalAmount() {
		return TotalAmount;
	}	
	
	public void setTotalAmount(float totalamount) {
		this.TotalAmount = totalamount;
	}
	public boolean getIsReceived() {
		return IsReceived;
	}	
	
	public void setIsReceived(boolean isreceived) {
		this.IsReceived = isreceived;
	}
	public boolean getIsAccepted() {
		return IsAccepted;
	}	
	
	public void setIsAccepted(boolean isaccepted) {
		this.IsAccepted = isaccepted;
	}
	
	/* all functions for reference*/
	public Supplier getSupplier() {
		return Supplier;
	}	
	
	public void setSupplier(Supplier supplier) {
		this.Supplier = supplier;
	}			
	public Store getStore() {
		return Store;
	}	
	
	public void setStore(Store store) {
		this.Store = store;
	}			
	public OrderProduct getOrderProduct() {
		return OrderProduct;
	}	
	
	public void setOrderProduct(OrderProduct orderproduct) {
		this.OrderProduct = orderproduct;
	}			
	

	/* invarints checking*/
	public boolean PurchaseOrder_UniquePurchaseOrderId() {
		
		if (StandardOPs.isUnique(((List<PurchaseOrder>)EntityManager.getAllInstancesOf("PurchaseOrder")), "OrderId")) {
			return true;
		} else {
			return false;
		}
	}
	
	//check all invariants
	public boolean checkAllInvairant() {
		
		if (PurchaseOrder_UniquePurchaseOrderId()) {
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
	
	public final static ArrayList<String> allInvariantCheckingFunction = new ArrayList<String>(Arrays.asList("PurchaseOrder_UniquePurchaseOrderId"));

}
