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

public class AcceptPurchaseOrderServiceImpl implements AcceptPurchaseOrderService, Serializable {
	
	
	public static Map<String, List<String>> opINVRelatedEntity = new HashMap<String, List<String>>();
	
	
	ThirdPartyServices services;
			
	public AcceptPurchaseOrderServiceImpl() {
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
	public boolean receivePurchaseOrder(int orderID) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get purchaseOrder
		PurchaseOrder purchaseOrder = null;
		//no nested iterator --  iterator: any previous:any
		for (PurchaseOrder po : (List<PurchaseOrder>)EntityManager.getAllInstancesOf("PurchaseOrder"))
		{
			if (po.getOrderId() == orderID)
			{
				purchaseOrder = po;
				break;
			}
				
			
		}
		/* previous state in post-condition*/
 
		/* check precondition */
		if (StandardOPs.oclIsundefined(purchaseOrder) == false) 
		{ 
			/* Logic here */
			purchaseOrder.setIsReceived(true);
			
			
			refresh();
			// post-condition checking
			if (!(purchaseOrder.getIsReceived() == true
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
		//all relevant vars : purchaseOrder
		//all relevant entities : PurchaseOrder
	}  
	
	static {opINVRelatedEntity.put("receivePurchaseOrder", Arrays.asList("PurchaseOrder"));}
	 
	@SuppressWarnings("unchecked")
	public PurchaseOrder reviewOrderDetails(int orderID) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get currentOrder
		PurchaseOrder currentOrder = null;
		//no nested iterator --  iterator: any previous:any
		for (PurchaseOrder po : (List<PurchaseOrder>)EntityManager.getAllInstancesOf("PurchaseOrder"))
		{
			if (po.getOrderId() == orderID)
			{
				currentOrder = po;
				break;
			}
				
			
		}
		/* previous state in post-condition*/
 
		/* check precondition */
		if (StandardOPs.oclIsundefined(CurrentReceivedOrder) == false) 
		{ 
			/* Logic here */
			
			
			refresh();
			// post-condition checking
			if (!(true)) {
				throw new PostconditionException();
			}
			
			refresh(); return currentOrder;
		}
		else
		{
			throw new PreconditionException();
		}
	}  
	
	 
	@SuppressWarnings("unchecked")
	public boolean confirmOrderAcceptance(boolean isAccepted) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* previous state in post-condition*/
 
		/* check precondition */
		if (StandardOPs.oclIsundefined(CurrentReceivedOrder) == false) 
		{ 
			/* Logic here */
			CurrentReceivedOrder.setIsAccepted(isAccepted);
			
			
			refresh();
			// post-condition checking
			if (!(CurrentReceivedOrder.getIsAccepted() == isAccepted
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
		//all relevant vars : CurrentReceivedOrder
		//all relevant entities : 
	}  
	
	static {opINVRelatedEntity.put("confirmOrderAcceptance", Arrays.asList(""));}
	 
		
	
	
	/* temp property for controller */
	private PurchaseOrder CurrentReceivedOrder;
	private List<Item> CurrentProductList;
			
	/* all get and set functions for temp property*/
	public PurchaseOrder getCurrentReceivedOrder() {
		return CurrentReceivedOrder;
	}	
	
	public void setCurrentReceivedOrder(PurchaseOrder currentreceivedorder) {
		this.CurrentReceivedOrder = currentreceivedorder;
	}
	public List<Item> getCurrentProductList() {
		return CurrentProductList;
	}	
	
	public void setCurrentProductList(List<Item> currentproductlist) {
		this.CurrentProductList = currentproductlist;
	}
	
	/* invarints checking*/
	public final static ArrayList<String> allInvariantCheckingFunction = new ArrayList<String>(Arrays.asList());
			
}
