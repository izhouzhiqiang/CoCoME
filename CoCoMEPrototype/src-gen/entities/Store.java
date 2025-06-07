package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class Store implements Serializable {
	
	/* all primary attributes */
	private int Id;
	private String Name;
	private String Address;
	private boolean IsOpened;
	
	/* all references */
	private List<CashDesk> AssociationCashdeskes = new LinkedList<CashDesk>(); 
	private List<ProductCatalog> Productcatalogs = new LinkedList<ProductCatalog>(); 
	private List<Item> Items = new LinkedList<Item>(); 
	private List<Cashier> Cashiers = new LinkedList<Cashier>(); 
	private List<Sale> Sales = new LinkedList<Sale>(); 
	
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
	public String getAddress() {
		return Address;
	}	
	
	public void setAddress(String address) {
		this.Address = address;
	}
	public boolean getIsOpened() {
		return IsOpened;
	}	
	
	public void setIsOpened(boolean isopened) {
		this.IsOpened = isopened;
	}
	
	/* all functions for reference*/
	public List<CashDesk> getAssociationCashdeskes() {
		return AssociationCashdeskes;
	}	
	
	public void addAssociationCashdeskes(CashDesk cashdesk) {
		this.AssociationCashdeskes.add(cashdesk);
	}
	
	public void deleteAssociationCashdeskes(CashDesk cashdesk) {
		this.AssociationCashdeskes.remove(cashdesk);
	}
	public List<ProductCatalog> getProductcatalogs() {
		return Productcatalogs;
	}	
	
	public void addProductcatalogs(ProductCatalog productcatalog) {
		this.Productcatalogs.add(productcatalog);
	}
	
	public void deleteProductcatalogs(ProductCatalog productcatalog) {
		this.Productcatalogs.remove(productcatalog);
	}
	public List<Item> getItems() {
		return Items;
	}	
	
	public void addItems(Item item) {
		this.Items.add(item);
	}
	
	public void deleteItems(Item item) {
		this.Items.remove(item);
	}
	public List<Cashier> getCashiers() {
		return Cashiers;
	}	
	
	public void addCashiers(Cashier cashier) {
		this.Cashiers.add(cashier);
	}
	
	public void deleteCashiers(Cashier cashier) {
		this.Cashiers.remove(cashier);
	}
	public List<Sale> getSales() {
		return Sales;
	}	
	
	public void addSales(Sale sale) {
		this.Sales.add(sale);
	}
	
	public void deleteSales(Sale sale) {
		this.Sales.remove(sale);
	}
	

	/* invarints checking*/
	public boolean Store_UniqueStoreId() {
		
		if (StandardOPs.isUnique(((List<Store>)EntityManager.getAllInstancesOf("Store")), "Id")) {
			return true;
		} else {
			return false;
		}
	}
	
	//check all invariants
	public boolean checkAllInvairant() {
		
		if (Store_UniqueStoreId()) {
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
	
	public final static ArrayList<String> allInvariantCheckingFunction = new ArrayList<String>(Arrays.asList("Store_UniqueStoreId"));

}
