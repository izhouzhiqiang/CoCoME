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

public class ReturnProductsServiceImpl implements ReturnProductsService, Serializable {
	
	
	public static Map<String, List<String>> opINVRelatedEntity = new HashMap<String, List<String>>();
	
	
	ThirdPartyServices services;
			
	public ReturnProductsServiceImpl() {
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
	public boolean initiateReturn(int orderID, int itemID) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get order
		Sale order = null;
		//no nested iterator --  iterator: any previous:any
		for (Sale od : (List<Sale>)EntityManager.getAllInstancesOf("Sale"))
		{
			if (od.getOrderId() == orderID)
			{
				order = od;
				break;
			}
				
			
		}
		//Get item
		Item item = null;
		//no nested iterator --  iterator: any previous:any
		for (Item i : (List<Item>)EntityManager.getAllInstancesOf("Item"))
		{
			if (i.getBarcode() == itemID)
			{
				item = i;
				break;
			}
				
			
		}
		/* previous state in post-condition*/
 
		/* check precondition */
		if (StandardOPs.oclIsundefined(order) == false && StandardOPs.oclIsundefined(item) == false) 
		{ 
			/* Logic here */
			ReturnRequest returnRequest = null;
			returnRequest = (ReturnRequest) EntityManager.createObject("ReturnRequest");
			returnRequest.setOrderId(order.getOrderId());
			returnRequest.setItemId(item.getItemID());
			EntityManager.addObject("ReturnRequest", returnRequest);
			
			
			refresh();
			// post-condition checking
			if (!(true && 
			returnRequest.getOrderId() == order.getOrderId()
			 && 
			returnRequest.getItemId() == item.getItemID()
			 && 
			StandardOPs.includes(((List<ReturnRequest>)EntityManager.getAllInstancesOf("ReturnRequest")), returnRequest)
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
		//all relevant vars : returnRequest
		//all relevant entities : ReturnRequest
	}  
	
	static {opINVRelatedEntity.put("initiateReturn", Arrays.asList("ReturnRequest"));}
	 
	@SuppressWarnings("unchecked")
	public boolean checkReturnEligibility(int requestID) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get currentRequest
		ReturnRequest currentRequest = null;
		//no nested iterator --  iterator: any previous:any
		for (ReturnRequest rr : (List<ReturnRequest>)EntityManager.getAllInstancesOf("ReturnRequest"))
		{
			if (rr.getRequestId() == requestID)
			{
				currentRequest = rr;
				break;
			}
				
			
		}
		/* previous state in post-condition*/
 
		/* check precondition */
		if (StandardOPs.oclIsundefined(CurrentReturnRequest) == false) 
		{ 
			/* Logic here */
			
			
			refresh();
			// post-condition checking
			if (!(true)) {
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
	}  
	
	 
	@SuppressWarnings("unchecked")
	public boolean provideReturnReason(String reason) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* previous state in post-condition*/
 
		/* check precondition */
		if (StandardOPs.oclIsundefined(CurrentReturnRequest) == false) 
		{ 
			/* Logic here */
			CurrentReturnRequest.setReturnReason(reason);
			
			
			refresh();
			// post-condition checking
			if (!(CurrentReturnRequest.getReturnReason() == reason
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
		//string parameters: [reason] 
		//all relevant vars : CurrentReturnRequest
		//all relevant entities : 
	}  
	
	static {opINVRelatedEntity.put("provideReturnReason", Arrays.asList(""));}
	 
	
	
	
	/* temp property for controller */
	private ReturnRequest CurrentReturnRequest;
	private OrderProduct CurrentOrder;
	private Item CurrentItem;
			
	/* all get and set functions for temp property*/
	public ReturnRequest getCurrentReturnRequest() {
		return CurrentReturnRequest;
	}	
	
	public void setCurrentReturnRequest(ReturnRequest currentreturnrequest) {
		this.CurrentReturnRequest = currentreturnrequest;
	}
	public OrderProduct getCurrentOrder() {
		return CurrentOrder;
	}	
	
	public void setCurrentOrder(OrderProduct currentorder) {
		this.CurrentOrder = currentorder;
	}
	public Item getCurrentItem() {
		return CurrentItem;
	}	
	
	public void setCurrentItem(Item currentitem) {
		this.CurrentItem = currentitem;
	}
	
	/* invarints checking*/
	public final static ArrayList<String> allInvariantCheckingFunction = new ArrayList<String>(Arrays.asList());
			
}
