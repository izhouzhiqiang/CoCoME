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

public class CheckOutServiceImpl implements CheckOutService, Serializable {
	
	
	public static Map<String, List<String>> opINVRelatedEntity = new HashMap<String, List<String>>();
	
	
	ThirdPartyServices services;
			
	public CheckOutServiceImpl() {
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
	public boolean addItemToCheckout(int itemID, int quantity) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get item
		Item item = null;
		//no nested iterator --  iterator: any previous:any
		for (Item i : (List<Item>)EntityManager.getAllInstancesOf("Item"))
		{
			if (i.getItemID() == itemID)
			{
				item = i;
				break;
			}
				
			
		}
		/* previous state in post-condition*/
		/* service reference */
		/* service temp attribute */
		/* objects in definition */  
		Item Pre_item = SerializationUtils.clone(item);
 
		/* check precondition */
		if (StandardOPs.oclIsundefined(CurrentCheckout) == true && CurrentCheckout.getIsComplete() == false && StandardOPs.oclIsundefined(item) == true && item.getStockNumber() >= quantity) 
		{ 
			/* Logic here */
			SalesLineItem sli = null;
			sli = (SalesLineItem) EntityManager.createObject("SalesLineItem");
			this.setCurrentCheckoutLine(sli);
			sli.setBelongedCheckout(CurrentCheckout);
			CurrentCheckout.addContainedSalesLine(sli);
			sli.setBelongedItem(item);
			sli.setQuantity(quantity);
			sli.setSubamount(item.getPrice()*quantity);
			item.setStockNumber(item.getStockNumber()-quantity);
			EntityManager.addObject("SalesLineItem", sli);
			
			
			refresh();
			// post-condition checking
			if (!(true && 
			this.getCurrentCheckoutLine() == sli
			 && 
			sli.getBelongedCheckout() == CurrentCheckout
			 && 
			StandardOPs.includes(CurrentCheckout.getContainedSalesLine(), sli)
			 && 
			sli.getBelongedItem() == item
			 && 
			sli.getQuantity() == quantity
			 && 
			sli.getSubamount() == item.getPrice()*quantity
			 && 
			item.getStockNumber() == Pre_item.getStockNumber()-quantity
			 && 
			StandardOPs.includes(((List<SalesLineItem>)EntityManager.getAllInstancesOf("SalesLineItem")), sli)
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
		//all relevant vars : sli item this
		//all relevant entities : SalesLineItem Item 
	}  
	
	static {opINVRelatedEntity.put("addItemToCheckout", Arrays.asList("SalesLineItem","Item",""));}
	 
	@SuppressWarnings("unchecked")
	public boolean selectPaymentMethod(String paymentMethod) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* previous state in post-condition*/
 
		/* check precondition */
		if (StandardOPs.oclIsundefined(CurrentCheckout) == false && CurrentCheckout.getIsComplete() == false) 
		{ 
			/* Logic here */
			CurrentCheckout.setPaymentMethod(paymentMethod);
			
			
			refresh();
			// post-condition checking
			if (!(CurrentCheckout.getPaymentMethod() == paymentMethod
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
		//string parameters: [paymentMethod] 
		//all relevant vars : CurrentCheckout
		//all relevant entities : 
	}  
	
	static {opINVRelatedEntity.put("selectPaymentMethod", Arrays.asList(""));}
	 
	@SuppressWarnings("unchecked")
	public boolean processPayment(float amount, String paymentDetails) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* previous state in post-condition*/
 
		/* check precondition */
		if (StandardOPs.oclIsundefined(CurrentCheckout) == false && StandardOPs.oclIsundefined(CurrentCheckout.getPaymentMethod()) == false && amount <= CurrentCheckout.getAmount()) 
		{ 
			/* Logic here */
			CurrentCheckout.setIsPaid(true);
			CurrentCheckout.setAmount(amount);
			CurrentCheckout.setPaymentDetails(paymentDetails);
			
			
			refresh();
			// post-condition checking
			if (!(CurrentCheckout.getIsPaid() == true
			 && 
			CurrentCheckout.getAmount() == amount
			 && 
			CurrentCheckout.getPaymentDetails() == paymentDetails
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
		//string parameters: [paymentDetails] 
		//all relevant vars : CurrentCheckout
		//all relevant entities : 
	}  
	
	static {opINVRelatedEntity.put("processPayment", Arrays.asList(""));}
	 
	@SuppressWarnings("unchecked")
	public boolean printReceipt() throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* previous state in post-condition*/
 
		/* check precondition */
		if (StandardOPs.oclIsundefined(CurrentCheckout) == false && CurrentCheckout.getIsPaid() == true) 
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
	
	 
	
	
	
	/* temp property for controller */
	private Sale CurrentCheckout;
	private SalesLineItem CurrentCheckoutLine;
	private PaymentMethod CurrentPaymentMethod;
			
	/* all get and set functions for temp property*/
	public Sale getCurrentCheckout() {
		return CurrentCheckout;
	}	
	
	public void setCurrentCheckout(Sale currentcheckout) {
		this.CurrentCheckout = currentcheckout;
	}
	public SalesLineItem getCurrentCheckoutLine() {
		return CurrentCheckoutLine;
	}	
	
	public void setCurrentCheckoutLine(SalesLineItem currentcheckoutline) {
		this.CurrentCheckoutLine = currentcheckoutline;
	}
	public PaymentMethod getCurrentPaymentMethod() {
		return CurrentPaymentMethod;
	}	
	
	public void setCurrentPaymentMethod(PaymentMethod currentpaymentmethod) {
		this.CurrentPaymentMethod = currentpaymentmethod;
	}
	
	/* invarints checking*/
	public final static ArrayList<String> allInvariantCheckingFunction = new ArrayList<String>(Arrays.asList());
			
}
