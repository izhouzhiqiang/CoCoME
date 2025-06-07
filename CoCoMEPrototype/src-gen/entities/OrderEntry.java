package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class OrderEntry implements Serializable {
	
	/* all primary attributes */
	private int Quantity;
	private float SubAmount;
	
	/* all references */
	private Item Item; 
	
	/* all get and set functions */
	public int getQuantity() {
		return Quantity;
	}	
	
	public void setQuantity(int quantity) {
		this.Quantity = quantity;
	}
	public float getSubAmount() {
		return SubAmount;
	}	
	
	public void setSubAmount(float subamount) {
		this.SubAmount = subamount;
	}
	
	/* all functions for reference*/
	public Item getItem() {
		return Item;
	}	
	
	public void setItem(Item item) {
		this.Item = item;
	}			
	


}
