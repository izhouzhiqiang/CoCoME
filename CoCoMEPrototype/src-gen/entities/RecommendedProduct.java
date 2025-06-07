package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class RecommendedProduct implements Serializable {
	
	/* all primary attributes */
	private int RecommendationId;
	private int ProductId;
	private int SupplierId;
	private LocalDate RecommendationTime;
	private String RecommendationReason;
	
	/* all references */
	private Supplier Supplier; 
	private Item Item; 
	private Store Store; 
	
	/* all get and set functions */
	public int getRecommendationId() {
		return RecommendationId;
	}	
	
	public void setRecommendationId(int recommendationid) {
		this.RecommendationId = recommendationid;
	}
	public int getProductId() {
		return ProductId;
	}	
	
	public void setProductId(int productid) {
		this.ProductId = productid;
	}
	public int getSupplierId() {
		return SupplierId;
	}	
	
	public void setSupplierId(int supplierid) {
		this.SupplierId = supplierid;
	}
	public LocalDate getRecommendationTime() {
		return RecommendationTime;
	}	
	
	public void setRecommendationTime(LocalDate recommendationtime) {
		this.RecommendationTime = recommendationtime;
	}
	public String getRecommendationReason() {
		return RecommendationReason;
	}	
	
	public void setRecommendationReason(String recommendationreason) {
		this.RecommendationReason = recommendationreason;
	}
	
	/* all functions for reference*/
	public Supplier getSupplier() {
		return Supplier;
	}	
	
	public void setSupplier(Supplier supplier) {
		this.Supplier = supplier;
	}			
	public Item getItem() {
		return Item;
	}	
	
	public void setItem(Item item) {
		this.Item = item;
	}			
	public Store getStore() {
		return Store;
	}	
	
	public void setStore(Store store) {
		this.Store = store;
	}			
	

	/* invarints checking*/
	public boolean RecommendedProduct_UniqueRecommendationId() {
		
		if (StandardOPs.isUnique(((List<RecommendedProduct>)EntityManager.getAllInstancesOf("RecommendedProduct")), "RecommendationId")) {
			return true;
		} else {
			return false;
		}
	}
	
	//check all invariants
	public boolean checkAllInvairant() {
		
		if (RecommendedProduct_UniqueRecommendationId()) {
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
	
	public final static ArrayList<String> allInvariantCheckingFunction = new ArrayList<String>(Arrays.asList("RecommendedProduct_UniqueRecommendationId"));

}
