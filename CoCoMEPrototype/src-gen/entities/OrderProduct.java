package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class OrderProduct implements Serializable {
	
	/* all primary attributes */
	private int Id;
	private LocalDate Time;
	private OrderStatus OrderStatus;
	private float Amount;
	
	/* all references */
	private Supplier Supplier; 
	private List<OrderEntry> ContainedEntries = new LinkedList<OrderEntry>(); 
	
	/* all get and set functions */
	public int getId() {
		return Id;
	}	
	
	public void setId(int id) {
		this.Id = id;
	}
	public LocalDate getTime() {
		return Time;
	}	
	
	public void setTime(LocalDate time) {
		this.Time = time;
	}
	public OrderStatus getOrderStatus() {
		return OrderStatus;
	}	
	
	public void setOrderStatus(OrderStatus orderstatus) {
		this.OrderStatus = orderstatus;
	}
	public float getAmount() {
		return Amount;
	}	
	
	public void setAmount(float amount) {
		this.Amount = amount;
	}
	
	/* all functions for reference*/
	public Supplier getSupplier() {
		return Supplier;
	}	
	
	public void setSupplier(Supplier supplier) {
		this.Supplier = supplier;
	}			
	public List<OrderEntry> getContainedEntries() {
		return ContainedEntries;
	}	
	
	public void addContainedEntries(OrderEntry orderentry) {
		this.ContainedEntries.add(orderentry);
	}
	
	public void deleteContainedEntries(OrderEntry orderentry) {
		this.ContainedEntries.remove(orderentry);
	}
	


}
