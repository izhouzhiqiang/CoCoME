package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class SalesLineItem implements Serializable {
	
	/* all primary attributes */
	private int Quantity;
	private float Subamount;
	private Sale BelongedCheckout;
	
	/* all references */
	private Sale BelongedSale; 
	private Item BelongedItem; 
	
	/* all get and set functions */
	public int getQuantity() {
		return Quantity;
	}	
	
	public void setQuantity(int quantity) {
		this.Quantity = quantity;
	}
	public float getSubamount() {
		return Subamount;
	}	
	
	public void setSubamount(float subamount) {
		this.Subamount = subamount;
	}
	public Sale getBelongedCheckout() {
		return BelongedCheckout;
	}	
	
	public void setBelongedCheckout(Sale belongedcheckout) {
		this.BelongedCheckout = belongedcheckout;
	}
	
	/* all functions for reference*/
	public Sale getBelongedSale() {
		return BelongedSale;
	}	
	
	public void setBelongedSale(Sale sale) {
		this.BelongedSale = sale;
	}			
	public Item getBelongedItem() {
		return BelongedItem;
	}	
	
	public void setBelongedItem(Item item) {
		this.BelongedItem = item;
	}			
	


}
