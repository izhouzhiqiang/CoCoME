package services.impl;

import services.*;
import entities.*;
import java.util.List;
import java.util.LinkedList;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.Arrays;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;
import org.apache.commons.lang3.SerializationUtils;
import java.util.Iterator;

public class ManageItemCRUDServiceImpl implements ManageItemCRUDService, Serializable {
	
	
	public static Map<String, List<String>> opINVRelatedEntity = new HashMap<String, List<String>>();
	
	
	ThirdPartyServices services;
			
	public ManageItemCRUDServiceImpl() {
		services = new ThirdPartyServicesImpl();
	}

	
	//Shared variable from system services
	
	/* Shared variable from system services and get()/set() methods */
	private CashDesk CurrentCashDesk;
	private Store CurrentStore;
			
	/* all get and set functions for temp property*/
	public CashDesk getCurrentCashDesk() {
		return CurrentCashDesk;
	}	
	
	public void setCurrentCashDesk(CashDesk currentcashdesk) {
		this.CurrentCashDesk = currentcashdesk;
	}
	public Store getCurrentStore() {
		return CurrentStore;
	}	
	
	public void setCurrentStore(Store currentstore) {
		this.CurrentStore = currentstore;
	}
				
	
	
	/* Generate inject for sharing temp variables between use cases in system service */
	public void refresh() {
		CoCoMESystem cocomesystem_service = (CoCoMESystem) ServiceManager.getAllInstancesOf("CoCoMESystem").get(0);
		cocomesystem_service.setCurrentCashDesk(CurrentCashDesk);
		cocomesystem_service.setCurrentStore(CurrentStore);
	}
	
	/* Generate buiness logic according to functional requirement */
	@SuppressWarnings("unchecked")
	public boolean createItem(int barcode, String name, float price, int stocknumber, float orderprice) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get item
		Item item = null;
		//no nested iterator --  iterator: any previous:any
		for (Item ite : (List<Item>)EntityManager.getAllInstancesOf("Item"))
		{
			if (ite.getBarcode() == barcode)
			{
				item = ite;
				break;
			}
				
			
		}
		/* previous state in post-condition*/
 
		/* check precondition */
		if (StandardOPs.oclIsundefined(item) == true) 
		{ 
			/* Logic here */
			Item ite = null;
			ite = (Item) EntityManager.createObject("Item");
			ite.setBarcode(barcode);
			ite.setName(name);
			ite.setPrice(price);
			ite.setStockNumber(stocknumber);
			ite.setOrderPrice(orderprice);
			EntityManager.addObject("Item", ite);
			
			
			refresh();
			// post-condition checking
			if (!(true && 
			ite.getBarcode() == barcode
			 && 
			ite.getName() == name
			 && 
			ite.getPrice() == price
			 && 
			ite.getStockNumber() == stocknumber
			 && 
			ite.getOrderPrice() == orderprice
			 && 
			StandardOPs.includes(((List<Item>)EntityManager.getAllInstancesOf("Item")), ite)
			 && 
			true)) {
				throw new PostconditionException();
			}
			
		
			//return primitive type
			refresh();				
			return true;
		}
		else
		{
			throw new PreconditionException();
		}
		//string parameters: [name] 
		//all relevant vars : ite
		//all relevant entities : Item
	}  
	
	static {opINVRelatedEntity.put("createItem", Arrays.asList("Item"));}
	 
	@SuppressWarnings("unchecked")
	public Item queryItem(int barcode) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get item
		Item item = null;
		//no nested iterator --  iterator: any previous:any
		for (Item ite : (List<Item>)EntityManager.getAllInstancesOf("Item"))
		{
			if (ite.getBarcode() == barcode)
			{
				item = ite;
				break;
			}
				
			
		}
		/* previous state in post-condition*/
 
		/* check precondition */
		if (StandardOPs.oclIsundefined(item) == false) 
		{ 
			/* Logic here */
			
			
			refresh();
			// post-condition checking
			if (!(true)) {
				throw new PostconditionException();
			}
			
			refresh(); return item;
		}
		else
		{
			throw new PreconditionException();
		}
	}  
	
	 
	@SuppressWarnings("unchecked")
	public boolean modifyItem(int barcode, String name, float price, int stocknumber, float orderprice) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get item
		Item item = null;
		//no nested iterator --  iterator: any previous:any
		for (Item ite : (List<Item>)EntityManager.getAllInstancesOf("Item"))
		{
			if (ite.getBarcode() == barcode)
			{
				item = ite;
				break;
			}
				
			
		}
		/* previous state in post-condition*/
 
		/* check precondition */
		if (StandardOPs.oclIsundefined(item) == false) 
		{ 
			/* Logic here */
			item.setBarcode(barcode);
			item.setName(name);
			item.setPrice(price);
			item.setStockNumber(stocknumber);
			item.setOrderPrice(orderprice);
			
			
			refresh();
			// post-condition checking
			if (!(item.getBarcode() == barcode
			 && 
			item.getName() == name
			 && 
			item.getPrice() == price
			 && 
			item.getStockNumber() == stocknumber
			 && 
			item.getOrderPrice() == orderprice
			 && 
			true)) {
				throw new PostconditionException();
			}
			
		
			//return primitive type
			refresh();				
			return true;
		}
		else
		{
			throw new PreconditionException();
		}
		//string parameters: [name] 
		//all relevant vars : item
		//all relevant entities : Item
	}  
	
	static {opINVRelatedEntity.put("modifyItem", Arrays.asList("Item"));}
	 
	@SuppressWarnings("unchecked")
	public boolean deleteItem(int barcode) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get item
		Item item = null;
		//no nested iterator --  iterator: any previous:any
		for (Item ite : (List<Item>)EntityManager.getAllInstancesOf("Item"))
		{
			if (ite.getBarcode() == barcode)
			{
				item = ite;
				break;
			}
				
			
		}
		/* previous state in post-condition*/
 
		/* check precondition */
		if (StandardOPs.oclIsundefined(item) == false && StandardOPs.includes(((List<Item>)EntityManager.getAllInstancesOf("Item")), item)) 
		{ 
			/* Logic here */
			EntityManager.deleteObject("Item", item);
			
			
			refresh();
			// post-condition checking
			if (!(StandardOPs.excludes(((List<Item>)EntityManager.getAllInstancesOf("Item")), item)
			 && 
			true)) {
				throw new PostconditionException();
			}
			
		
			//return primitive type
			refresh();				
			return true;
		}
		else
		{
			throw new PreconditionException();
		}
		//all relevant vars : item
		//all relevant entities : Item
	}  
	
	static {opINVRelatedEntity.put("deleteItem", Arrays.asList("Item"));}
	 
	
	
	
	/* temp property for controller */
			
	/* all get and set functions for temp property*/
	
	/* invarints checking*/
	public final static ArrayList<String> allInvariantCheckingFunction = new ArrayList<String>(Arrays.asList());
			
}
