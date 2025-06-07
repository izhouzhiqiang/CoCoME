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

public class ManageCashierCRUDServiceImpl implements ManageCashierCRUDService, Serializable {
	
	
	public static Map<String, List<String>> opINVRelatedEntity = new HashMap<String, List<String>>();
	
	
	ThirdPartyServices services;
			
	public ManageCashierCRUDServiceImpl() {
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
	public boolean createCashier(int id, String name) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get cashier
		Cashier cashier = null;
		//no nested iterator --  iterator: any previous:any
		for (Cashier cas : (List<Cashier>)EntityManager.getAllInstancesOf("Cashier"))
		{
			if (cas.getId() == id)
			{
				cashier = cas;
				break;
			}
				
			
		}
		/* previous state in post-condition*/
 
		/* check precondition */
		if (StandardOPs.oclIsundefined(cashier) == true) 
		{ 
			/* Logic here */
			Cashier cas = null;
			cas = (Cashier) EntityManager.createObject("Cashier");
			cas.setId(id);
			cas.setName(name);
			EntityManager.addObject("Cashier", cas);
			
			
			refresh();
			// post-condition checking
			if (!(true && 
			cas.getId() == id
			 && 
			cas.getName() == name
			 && 
			StandardOPs.includes(((List<Cashier>)EntityManager.getAllInstancesOf("Cashier")), cas)
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
		//all relevant vars : cas
		//all relevant entities : Cashier
	}  
	
	static {opINVRelatedEntity.put("createCashier", Arrays.asList("Cashier"));}
	 
	@SuppressWarnings("unchecked")
	public Cashier queryCashier(int id) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get cashier
		Cashier cashier = null;
		//no nested iterator --  iterator: any previous:any
		for (Cashier cas : (List<Cashier>)EntityManager.getAllInstancesOf("Cashier"))
		{
			if (cas.getId() == id)
			{
				cashier = cas;
				break;
			}
				
			
		}
		/* previous state in post-condition*/
 
		/* check precondition */
		if (StandardOPs.oclIsundefined(cashier) == false) 
		{ 
			/* Logic here */
			
			
			refresh();
			// post-condition checking
			if (!(true)) {
				throw new PostconditionException();
			}
			
			refresh(); return cashier;
		}
		else
		{
			throw new PreconditionException();
		}
	}  
	
	 
	@SuppressWarnings("unchecked")
	public boolean modifyCashier(int id, String name) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get cashier
		Cashier cashier = null;
		//no nested iterator --  iterator: any previous:any
		for (Cashier cas : (List<Cashier>)EntityManager.getAllInstancesOf("Cashier"))
		{
			if (cas.getId() == id)
			{
				cashier = cas;
				break;
			}
				
			
		}
		/* previous state in post-condition*/
 
		/* check precondition */
		if (StandardOPs.oclIsundefined(cashier) == false) 
		{ 
			/* Logic here */
			cashier.setId(id);
			cashier.setName(name);
			
			
			refresh();
			// post-condition checking
			if (!(cashier.getId() == id
			 && 
			cashier.getName() == name
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
		//all relevant vars : cashier
		//all relevant entities : Cashier
	}  
	
	static {opINVRelatedEntity.put("modifyCashier", Arrays.asList("Cashier"));}
	 
	@SuppressWarnings("unchecked")
	public boolean deleteCashier(int id) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get cashier
		Cashier cashier = null;
		//no nested iterator --  iterator: any previous:any
		for (Cashier cas : (List<Cashier>)EntityManager.getAllInstancesOf("Cashier"))
		{
			if (cas.getId() == id)
			{
				cashier = cas;
				break;
			}
				
			
		}
		/* previous state in post-condition*/
 
		/* check precondition */
		if (StandardOPs.oclIsundefined(cashier) == false && StandardOPs.includes(((List<Cashier>)EntityManager.getAllInstancesOf("Cashier")), cashier)) 
		{ 
			/* Logic here */
			EntityManager.deleteObject("Cashier", cashier);
			
			
			refresh();
			// post-condition checking
			if (!(StandardOPs.excludes(((List<Cashier>)EntityManager.getAllInstancesOf("Cashier")), cashier)
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
		//all relevant vars : cashier
		//all relevant entities : Cashier
	}  
	
	static {opINVRelatedEntity.put("deleteCashier", Arrays.asList("Cashier"));}
	 
	
	
	
	/* temp property for controller */
			
	/* all get and set functions for temp property*/
	
	/* invarints checking*/
	public final static ArrayList<String> allInvariantCheckingFunction = new ArrayList<String>(Arrays.asList());
			
}
