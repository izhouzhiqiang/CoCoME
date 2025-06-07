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

public class CoCoMEOrderProductsImpl implements CoCoMEOrderProducts, Serializable {
	
	
	public static Map<String, List<String>> opINVRelatedEntity = new HashMap<String, List<String>>();
	
	
	ThirdPartyServices services;
			
	public CoCoMEOrderProductsImpl() {
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
	public boolean makeNewOrder(int orderid) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get ordp
		OrderProduct ordp = null;
		//no nested iterator --  iterator: any previous:any
		for (OrderProduct opp : (List<OrderProduct>)EntityManager.getAllInstancesOf("OrderProduct"))
		{
			if (opp.getId() == orderid)
			{
				ordp = opp;
				break;
			}
				
			
		}
		/* previous state in post-condition*/
 
		/* check precondition */
		if (StandardOPs.oclIsundefined(ordp) == true) 
		{ 
			/* Logic here */
			OrderProduct op = null;
			op = (OrderProduct) EntityManager.createObject("OrderProduct");
			op.setOrderStatus(OrderStatus.NEW);
			op.setId(orderid);
			op.setTime(LocalDate.now());
			EntityManager.addObject("OrderProduct", op);
			this.setCurrentOrderProduct(op);
			
			
			refresh();
			// post-condition checking
			if (!(true && 
			op.getOrderStatus() == OrderStatus.NEW
			 && 
			op.getId() == orderid
			 && 
			op.getTime().isEqual(LocalDate.now())
			 && 
			StandardOPs.includes(((List<OrderProduct>)EntityManager.getAllInstancesOf("OrderProduct")), op)
			 && 
			this.getCurrentOrderProduct() == op
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
		//all relevant vars : op this
		//all relevant entities : OrderProduct 
	}  
	
	static {opINVRelatedEntity.put("makeNewOrder", Arrays.asList("OrderProduct",""));}
	 
	@SuppressWarnings("unchecked")
	public List<Item> listAllOutOfStoreProducts() throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* previous state in post-condition*/
 
		/* check precondition */
		if (true) 
		{ 
			/* Logic here */
			List<Item> tempsitem = new LinkedList<>();
			//no nested iterator --  iterator: select
			for (Item item : ((List<Item>)EntityManager.getAllInstancesOf("Item")))
			{
				if (item.getStockNumber() == 0)
				{
					tempsitem.add(item);
				} 
			}
			
			
			refresh();
			// post-condition checking
			if (!(true)) {
				throw new PostconditionException();
			}
			
			refresh(); return tempsitem;
		}
		else
		{
			throw new PreconditionException();
		}
	}  
	
	 
	@SuppressWarnings("unchecked")
	public boolean orderItem(int barcode, int quantity) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get item
		Item item = null;
		//no nested iterator --  iterator: any previous:any
		for (Item i : (List<Item>)EntityManager.getAllInstancesOf("Item"))
		{
			if (i.getBarcode() == barcode)
			{
				item = i;
				break;
			}
				
			
		}
		/* previous state in post-condition*/
 
		/* check precondition */
		if (StandardOPs.oclIsundefined(item) == false) 
		{ 
			/* Logic here */
			OrderEntry order = null;
			order = (OrderEntry) EntityManager.createObject("OrderEntry");
			order.setQuantity(quantity);
			order.setSubAmount(item.getOrderPrice()*quantity);
			order.setItem(item);
			EntityManager.addObject("OrderEntry", order);
			CurrentOrderProduct.addContainedEntries(order);
			
			
			refresh();
			// post-condition checking
			if (!(true && 
			order.getQuantity() == quantity
			 && 
			order.getSubAmount() == item.getOrderPrice()*quantity
			 && 
			order.getItem() == item
			 && 
			StandardOPs.includes(((List<OrderEntry>)EntityManager.getAllInstancesOf("OrderEntry")), order)
			 && 
			StandardOPs.includes(CurrentOrderProduct.getContainedEntries(), order)
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
		//all relevant vars : order
		//all relevant entities : OrderEntry
	}  
	
	static {opINVRelatedEntity.put("orderItem", Arrays.asList("OrderEntry"));}
	 
	@SuppressWarnings("unchecked")
	public boolean chooseSupplier(int supplierID) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get sup
		Supplier sup = null;
		//no nested iterator --  iterator: any previous:any
		for (Supplier s : (List<Supplier>)EntityManager.getAllInstancesOf("Supplier"))
		{
			if (s.getId() == supplierID)
			{
				sup = s;
				break;
			}
				
			
		}
		/* previous state in post-condition*/
 
		/* check precondition */
		if (StandardOPs.oclIsundefined(sup) == false && StandardOPs.oclIsundefined(CurrentOrderProduct) == false) 
		{ 
			/* Logic here */
			CurrentOrderProduct.setSupplier(sup);
			
			
			refresh();
			// post-condition checking
			if (!(CurrentOrderProduct.getSupplier() == sup
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
		//all relevant vars : CurrentOrderProduct
		//all relevant entities : 
	}  
	
	static {opINVRelatedEntity.put("chooseSupplier", Arrays.asList(""));}
	 
	@SuppressWarnings("unchecked")
	public boolean placeOrder() throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* previous state in post-condition*/
		/* service reference */
		/* service temp attribute */
		OrderProduct Pre_CurrentOrderProduct = SerializationUtils.clone(CurrentOrderProduct);
		/* objects in definition */  
 
		/* check precondition */
		if (StandardOPs.oclIsundefined(CurrentOrderProduct) == false) 
		{ 
			/* Logic here */
			CurrentOrderProduct.setOrderStatus(OrderStatus.REQUESTED);
			//no nested iterator --  iterator: forAll
			for (OrderEntry o : CurrentOrderProduct.getContainedEntries())
			{
				CurrentOrderProduct.setAmount(CurrentOrderProduct.getAmount()+o.getSubAmount());
			}
			
			
			refresh();
			// post-condition checking
			if (!(CurrentOrderProduct.getOrderStatus() == OrderStatus.REQUESTED
			 && 
			((Predicate<List>) (list) -> {	
				Iterator<OrderEntry> oIt =  list.iterator();
				Iterator<OrderEntry> Pre_oIt =  Pre_CurrentOrderProduct.getContainedEntries().iterator();
				OrderEntry o = null;
				OrderEntry Pre_o = null;
					while (oIt.hasNext() && Pre_oIt.hasNext()) {
					o = oIt.next();
					Pre_o = Pre_oIt.next();
					if (!(CurrentOrderProduct.getAmount() == Pre_CurrentOrderProduct.getAmount()+o.getSubAmount())) {
						return false;
					}
				}
				return true;
			}).test(CurrentOrderProduct.getContainedEntries())
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
		//all relevant vars : CurrentOrderProduct o
		//all relevant entities :  OrderEntry
	}  
	
	static {opINVRelatedEntity.put("placeOrder", Arrays.asList("","OrderEntry"));}
	 
	
	
	
	/* temp property for controller */
	private OrderProduct CurrentOrderProduct;
			
	/* all get and set functions for temp property*/
	public OrderProduct getCurrentOrderProduct() {
		return CurrentOrderProduct;
	}	
	
	public void setCurrentOrderProduct(OrderProduct currentorderproduct) {
		this.CurrentOrderProduct = currentorderproduct;
	}
	
	/* invarints checking*/
	public final static ArrayList<String> allInvariantCheckingFunction = new ArrayList<String>(Arrays.asList());
			
}
