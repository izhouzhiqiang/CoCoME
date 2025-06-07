package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class Item implements Serializable {
	
	/* all primary attributes */
	private int Barcode;
	private String Name;
	private float Price;
	private int StockNumber;
	private float OrderPrice;
	private int ItemID;
	
	/* all references */
	private ProductCatalog BelongedCatalog; 
	
	/* all get and set functions */
	public int getBarcode() {
		return Barcode;
	}	
	
	public void setBarcode(int barcode) {
		this.Barcode = barcode;
	}
	public String getName() {
		return Name;
	}	
	
	public void setName(String name) {
		this.Name = name;
	}
	public float getPrice() {
		return Price;
	}	
	
	public void setPrice(float price) {
		this.Price = price;
	}
	public int getStockNumber() {
		return StockNumber;
	}	
	
	public void setStockNumber(int stocknumber) {
		this.StockNumber = stocknumber;
	}
	public float getOrderPrice() {
		return OrderPrice;
	}	
	
	public void setOrderPrice(float orderprice) {
		this.OrderPrice = orderprice;
	}
	public int getItemID() {
		return ItemID;
	}	
	
	public void setItemID(int itemid) {
		this.ItemID = itemid;
	}
	
	/* all functions for reference*/
	public ProductCatalog getBelongedCatalog() {
		return BelongedCatalog;
	}	
	
	public void setBelongedCatalog(ProductCatalog productcatalog) {
		this.BelongedCatalog = productcatalog;
	}			
	

	/* invarints checking*/
	public boolean Item_UniqueBarcode() {
		
		if (StandardOPs.isUnique(((List<Item>)EntityManager.getAllInstancesOf("Item")), "Barcode")) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean Item_PriceGreatThanEqualZero() {
		
		if (Price >= 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean Item_StockNumberGreatThanEqualZero() {
		
		if (StockNumber >= 0) {
			return true;
		} else {
			return false;
		}
	}
	
	//check all invariants
	public boolean checkAllInvairant() {
		
		if (Item_UniqueBarcode() && Item_PriceGreatThanEqualZero() && Item_StockNumberGreatThanEqualZero()) {
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
	
	public final static ArrayList<String> allInvariantCheckingFunction = new ArrayList<String>(Arrays.asList("Item_UniqueBarcode","Item_PriceGreatThanEqualZero","Item_StockNumberGreatThanEqualZero"));

}
