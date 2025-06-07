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

public class ManageReturnServiceImpl implements ManageReturnService, Serializable {
	
	
	public static Map<String, List<String>> opINVRelatedEntity = new HashMap<String, List<String>>();
	
	
	ThirdPartyServices services;
			
	public ManageReturnServiceImpl() {
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
	public boolean reviewReturnRequest(int requestID) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get returnRequest
		ReturnRequest returnRequest = null;
		//no nested iterator --  iterator: any previous:any
		for (ReturnRequest rr : (List<ReturnRequest>)EntityManager.getAllInstancesOf("ReturnRequest"))
		{
			if (rr.getRequestId() == requestID)
			{
				returnRequest = rr;
				break;
			}
				
			
		}
		/* previous state in post-condition*/
 
		/* check precondition */
		if (StandardOPs.oclIsundefined(returnRequest) == false) 
		{ 
			/* Logic here */
			returnRequest.setIsReviewed(true);
			
			
			refresh();
			// post-condition checking
			if (!(returnRequest.getIsReviewed() == true
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
	
	static {opINVRelatedEntity.put("reviewReturnRequest", Arrays.asList("ReturnRequest"));}
	 
	@SuppressWarnings("unchecked")
	public boolean approveOrRejectReturn(boolean isApproved) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* previous state in post-condition*/
 
		/* check precondition */
		if (StandardOPs.oclIsundefined(CurrentReturnRequest) == false) 
		{ 
			/* Logic here */
			CurrentReturnRequest.setIsApproved(isApproved);
			
			
			refresh();
			// post-condition checking
			if (!(CurrentReturnRequest.getIsApproved() == isApproved
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
		//all relevant vars : CurrentReturnRequest
		//all relevant entities : 
	}  
	
	static {opINVRelatedEntity.put("approveOrRejectReturn", Arrays.asList(""));}
	 
	@SuppressWarnings("unchecked")
	public boolean processRefund(float amount, String paymentMethod) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* previous state in post-condition*/
 
		/* check precondition */
		if (StandardOPs.oclIsundefined(CurrentReturnRequest) == false && CurrentReturnRequest.getIsApproved() == true) 
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
		//string parameters: [paymentMethod] 
	}  
	
	 
	
	
	
	/* temp property for controller */
	private ReturnRequest CurrentReturnRequest;
	private Item CurrentItem;
	private Sale CurrentSale;
			
	/* all get and set functions for temp property*/
	public ReturnRequest getCurrentReturnRequest() {
		return CurrentReturnRequest;
	}	
	
	public void setCurrentReturnRequest(ReturnRequest currentreturnrequest) {
		this.CurrentReturnRequest = currentreturnrequest;
	}
	public Item getCurrentItem() {
		return CurrentItem;
	}	
	
	public void setCurrentItem(Item currentitem) {
		this.CurrentItem = currentitem;
	}
	public Sale getCurrentSale() {
		return CurrentSale;
	}	
	
	public void setCurrentSale(Sale currentsale) {
		this.CurrentSale = currentsale;
	}
	
	/* invarints checking*/
	public final static ArrayList<String> allInvariantCheckingFunction = new ArrayList<String>(Arrays.asList());
			
}
