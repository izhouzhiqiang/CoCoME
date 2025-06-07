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

public class ProcessSaleServiceImpl implements ProcessSaleService, Serializable {
	
	
	public static Map<String, List<String>> opINVRelatedEntity = new HashMap<String, List<String>>();
	
	
	ThirdPartyServices services;
			
	public ProcessSaleServiceImpl() {
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
	public boolean makeNewSale() throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* previous state in post-condition*/
 
		/* check precondition */
		if (StandardOPs.oclIsundefined(CurrentCashDesk) == false && CurrentCashDesk.getIsOpened() == true && (StandardOPs.oclIsundefined(CurrentSale) == true || (StandardOPs.oclIsundefined(CurrentSale) == false && CurrentSale.getIsComplete() == true))) 
		{ 
			/* Logic here */
			Sale s = null;
			s = (Sale) EntityManager.createObject("Sale");
			s.setBelongedCashDesk(CurrentCashDesk);
			CurrentCashDesk.addContainedSales(s);
			s.setIsComplete(false);
			s.setIsReadytoPay(false);
			EntityManager.addObject("Sale", s);
			this.setCurrentSale(s);
			
			
			refresh();
			// post-condition checking
			if (!(true && 
			s.getBelongedCashDesk() == CurrentCashDesk
			 && 
			StandardOPs.includes(CurrentCashDesk.getContainedSales(), s)
			 && 
			s.getIsComplete() == false
			 && 
			s.getIsReadytoPay() == false
			 && 
			StandardOPs.includes(((List<Sale>)EntityManager.getAllInstancesOf("Sale")), s)
			 && 
			this.getCurrentSale() == s
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
		//all relevant vars : s this
		//all relevant entities : Sale 
	}  
	
	static {opINVRelatedEntity.put("makeNewSale", Arrays.asList("Sale",""));}
	 
	@SuppressWarnings("unchecked")
	public boolean enterItem(int barcode, int quantity) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
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
		/* service reference */
		/* service temp attribute */
		/* objects in definition */  
		Item Pre_item = SerializationUtils.clone(item);
 
		/* check precondition */
		if (StandardOPs.oclIsundefined(CurrentSale) == false && CurrentSale.getIsComplete() == false && StandardOPs.oclIsundefined(item) == false && item.getStockNumber() > 0) 
		{ 
			/* Logic here */
			SalesLineItem sli = null;
			sli = (SalesLineItem) EntityManager.createObject("SalesLineItem");
			this.setCurrentSaleLine(sli);
			sli.setBelongedSale(CurrentSale);
			CurrentSale.addContainedSalesLine(sli);
			sli.setQuantity(quantity);
			sli.setBelongedItem(item);
			item.setStockNumber(item.getStockNumber()-quantity);
			sli.setSubamount(item.getPrice()*quantity);
			EntityManager.addObject("SalesLineItem", sli);
			
			
			refresh();
			// post-condition checking
			if (!(true && 
			this.getCurrentSaleLine() == sli
			 && 
			sli.getBelongedSale() == CurrentSale
			 && 
			StandardOPs.includes(CurrentSale.getContainedSalesLine(), sli)
			 && 
			sli.getQuantity() == quantity
			 && 
			sli.getBelongedItem() == item
			 && 
			item.getStockNumber() == Pre_item.getStockNumber()-quantity
			 && 
			sli.getSubamount() == item.getPrice()*quantity
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
	
	static {opINVRelatedEntity.put("enterItem", Arrays.asList("SalesLineItem","Item",""));}
	 
	@SuppressWarnings("unchecked")
	public float endSale() throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get sls
		List<SalesLineItem> sls = new LinkedList<>();
		sls = CurrentSale.getContainedSalesLine();
		//Get sub
		List<Float> sub = new LinkedList<>();
		//no nested iterator --  iterator: collect previous:collect
		for (SalesLineItem s : sls)
		{
			sub.add(s.getSubamount());
		}
		/* previous state in post-condition*/
 
		/* check precondition */
		if (StandardOPs.oclIsundefined(CurrentSale) == false && CurrentSale.getIsComplete() == false && CurrentSale.getIsReadytoPay() == false) 
		{ 
			/* Logic here */
			CurrentSale.setAmount(StandardOPs.sum(sub));
			CurrentSale.setIsReadytoPay(true);
			
			
			refresh();
			// post-condition checking
			if (!(CurrentSale.getAmount() == StandardOPs.sum(sub)
			 && 
			CurrentSale.getIsReadytoPay() == true
			 && 
			true)) {
				throw new PostconditionException();
			}
			
		
			//return primitive type
			refresh();				
			return CurrentSale.getAmount();
		}
		else
		{
			throw new PreconditionException();
		}
		//all relevant vars : CurrentSale
		//all relevant entities : 
	}  
	
	static {opINVRelatedEntity.put("endSale", Arrays.asList(""));}
	 
	@SuppressWarnings("unchecked")
	public boolean makeCashPayment(float amount) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* previous state in post-condition*/
 
		/* check precondition */
		if (StandardOPs.oclIsundefined(CurrentSale) == false && CurrentSale.getIsComplete() == false && CurrentSale.getIsReadytoPay() == true && amount >= CurrentSale.getAmount()) 
		{ 
			/* Logic here */
			CashPayment cp = null;
			cp = (CashPayment) EntityManager.createObject("CashPayment");
			cp.setAmountTendered(amount);
			cp.setBelongedSale(CurrentSale);
			CurrentSale.setAssoicatedPayment(cp);
			CurrentSale.setBelongedstore(CurrentStore);
			CurrentStore.addSales(CurrentSale);
			CurrentSale.setIsComplete(true);
			CurrentSale.setTime(LocalDate.now());
			cp.setBalance(amount-CurrentSale.getAmount());
			EntityManager.addObject("CashPayment", cp);
			
			
			refresh();
			// post-condition checking
			if (!(true && 
			cp.getAmountTendered() == amount
			 && 
			cp.getBelongedSale() == CurrentSale
			 && 
			CurrentSale.getAssoicatedPayment() == cp
			 && 
			CurrentSale.getBelongedstore() == CurrentStore
			 && 
			StandardOPs.includes(CurrentStore.getSales(), CurrentSale)
			 && 
			CurrentSale.getIsComplete() == true
			 && 
			CurrentSale.getTime().isEqual(LocalDate.now())
			 && 
			cp.getBalance() == amount-CurrentSale.getAmount()
			 && 
			StandardOPs.includes(((List<CashPayment>)EntityManager.getAllInstancesOf("CashPayment")), cp)
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
		//all relevant vars : cp CurrentSale
		//all relevant entities : CashPayment 
	}  
	
	static {opINVRelatedEntity.put("makeCashPayment", Arrays.asList("CashPayment",""));}
	 
	@SuppressWarnings("unchecked")
	public boolean makeCardPayment(String cardAccountNumber, LocalDate expiryDate, float fee) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* previous state in post-condition*/
 
		/* check precondition */
		if (StandardOPs.oclIsundefined(CurrentSale) == false && CurrentSale.getIsComplete() == false && CurrentSale.getIsReadytoPay() == true && services.thirdPartyCardPaymentService(cardAccountNumber, expiryDate, fee)) 
		{ 
			/* Logic here */
			CardPayment cdp = null;
			cdp = (CardPayment) EntityManager.createObject("CardPayment");
			cdp.setAmountTendered(fee);
			cdp.setBelongedSale(CurrentSale);
			CurrentSale.setAssoicatedPayment(cdp);
			cdp.setCardAccountNumber(cardAccountNumber);
			cdp.setExpiryDate(expiryDate);
			EntityManager.addObject("CardPayment", cdp);
			CurrentSale.setBelongedstore(CurrentStore);
			CurrentStore.addSales(CurrentSale);
			CurrentSale.setIsComplete(true);
			CurrentSale.setTime(LocalDate.now());
			
			
			refresh();
			// post-condition checking
			if (!(true && 
			cdp.getAmountTendered() == fee
			 && 
			cdp.getBelongedSale() == CurrentSale
			 && 
			CurrentSale.getAssoicatedPayment() == cdp
			 && 
			cdp.getCardAccountNumber() == cardAccountNumber
			 && 
			cdp.getExpiryDate() == expiryDate
			 && 
			StandardOPs.includes(((List<CardPayment>)EntityManager.getAllInstancesOf("CardPayment")), cdp)
			 && 
			CurrentSale.getBelongedstore() == CurrentStore
			 && 
			StandardOPs.includes(CurrentStore.getSales(), CurrentSale)
			 && 
			CurrentSale.getIsComplete() == true
			 && 
			CurrentSale.getTime().isEqual(LocalDate.now())
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
		//string parameters: [cardAccountNumber] 
		//all relevant vars : cdp CurrentSale
		//all relevant entities : CardPayment 
	}  
	
	static {opINVRelatedEntity.put("makeCardPayment", Arrays.asList("CardPayment",""));}
	 
	@SuppressWarnings("unchecked")
	public boolean makeWechatPayment(float amount, String openID) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* previous state in post-condition*/
 
		/* check precondition */
		if (StandardOPs.oclIsundefined(CurrentSale) == false && CurrentSale.getIsComplete() == false && CurrentSale.getIsReadytoPay() == true && services.thirdPartyWechatPaymentService(amount, openID)) 
		{ 
			/* Logic here */
			WechatPayment wcp = null;
			wcp = (WechatPayment) EntityManager.createObject("WechatPayment");
			wcp.setAmountTendered(amount);
			wcp.setBelongedSale(CurrentSale);
			CurrentSale.setAssoicatedPayment(wcp);
			wcp.setOpenID(openID);
			EntityManager.addObject("WechatPayment", wcp);
			CurrentSale.setBelongedstore(CurrentStore);
			CurrentStore.addSales(CurrentSale);
			CurrentSale.setIsComplete(true);
			CurrentSale.setTime(LocalDate.now());
			
			
			refresh();
			// post-condition checking
			if (!(true && 
			wcp.getAmountTendered() == amount
			 && 
			wcp.getBelongedSale() == CurrentSale
			 && 
			CurrentSale.getAssoicatedPayment() == wcp
			 && 
			wcp.getOpenID() == openID
			 && 
			StandardOPs.includes(((List<WechatPayment>)EntityManager.getAllInstancesOf("WechatPayment")), wcp)
			 && 
			CurrentSale.getBelongedstore() == CurrentStore
			 && 
			StandardOPs.includes(CurrentStore.getSales(), CurrentSale)
			 && 
			CurrentSale.getIsComplete() == true
			 && 
			CurrentSale.getTime().isEqual(LocalDate.now())
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
		//string parameters: [openID] 
		//all relevant vars : wcp CurrentSale
		//all relevant entities : WechatPayment 
	}  
	
	static {opINVRelatedEntity.put("makeWechatPayment", Arrays.asList("WechatPayment",""));}
	 
	@SuppressWarnings("unchecked")
	public boolean makeAliPayment(float amount, String aliPayAccount) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* previous state in post-condition*/
 
		/* check precondition */
		if (StandardOPs.oclIsundefined(CurrentSale) == false && CurrentSale.getIsComplete() == false && CurrentSale.getIsReadytoPay() == true && services.thirdPartyAliPaymentService(amount, aliPayAccount)) 
		{ 
			/* Logic here */
			AliPayment ap = null;
			ap = (AliPayment) EntityManager.createObject("AliPayment");
			ap.setAmountTendered(amount);
			ap.setBelongedSale(CurrentSale);
			CurrentSale.setAssoicatedPayment(ap);
			ap.setAlipayAccount(aliPayAccount);
			EntityManager.addObject("AliPayment", ap);
			CurrentSale.setBelongedstore(CurrentStore);
			CurrentStore.addSales(CurrentSale);
			CurrentSale.setIsComplete(true);
			CurrentSale.setTime(LocalDate.now());
			
			
			refresh();
			// post-condition checking
			if (!(true && 
			ap.getAmountTendered() == amount
			 && 
			ap.getBelongedSale() == CurrentSale
			 && 
			CurrentSale.getAssoicatedPayment() == ap
			 && 
			ap.getAlipayAccount() == aliPayAccount
			 && 
			StandardOPs.includes(((List<AliPayment>)EntityManager.getAllInstancesOf("AliPayment")), ap)
			 && 
			CurrentSale.getBelongedstore() == CurrentStore
			 && 
			StandardOPs.includes(CurrentStore.getSales(), CurrentSale)
			 && 
			CurrentSale.getIsComplete() == true
			 && 
			CurrentSale.getTime().isEqual(LocalDate.now())
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
		//string parameters: [aliPayAccount] 
		//all relevant vars : ap CurrentSale
		//all relevant entities : AliPayment 
	}  
	
	static {opINVRelatedEntity.put("makeAliPayment", Arrays.asList("AliPayment",""));}
	 
	
	
	
	/* temp property for controller */
	private SalesLineItem CurrentSaleLine;
	private Sale CurrentSale;
	private PaymentMethod CurrentPaymentMethod;
			
	/* all get and set functions for temp property*/
	public SalesLineItem getCurrentSaleLine() {
		return CurrentSaleLine;
	}	
	
	public void setCurrentSaleLine(SalesLineItem currentsaleline) {
		this.CurrentSaleLine = currentsaleline;
	}
	public Sale getCurrentSale() {
		return CurrentSale;
	}	
	
	public void setCurrentSale(Sale currentsale) {
		this.CurrentSale = currentsale;
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
