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

public class ManageSupplierCRUDServiceImpl implements ManageSupplierCRUDService, Serializable {
	
	
	public static Map<String, List<String>> opINVRelatedEntity = new HashMap<String, List<String>>();
	
	
	ThirdPartyServices services;
			
	public ManageSupplierCRUDServiceImpl() {
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
	public boolean createSupplier(int id, String name) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get supplier
		Supplier supplier = null;
		//no nested iterator --  iterator: any previous:any
		for (Supplier sup : (List<Supplier>)EntityManager.getAllInstancesOf("Supplier"))
		{
			if (sup.getId() == id)
			{
				supplier = sup;
				break;
			}
				
			
		}
		/* previous state in post-condition*/
 
		/* check precondition */
		if (StandardOPs.oclIsundefined(supplier) == true) 
		{ 
			/* Logic here */
			Supplier sup = null;
			sup = (Supplier) EntityManager.createObject("Supplier");
			sup.setId(id);
			sup.setName(name);
			EntityManager.addObject("Supplier", sup);
			
			
			refresh();
			// post-condition checking
			if (!(true && 
			sup.getId() == id
			 && 
			sup.getName() == name
			 && 
			StandardOPs.includes(((List<Supplier>)EntityManager.getAllInstancesOf("Supplier")), sup)
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
		//all relevant vars : sup
		//all relevant entities : Supplier
	}  
	
	static {opINVRelatedEntity.put("createSupplier", Arrays.asList("Supplier"));}
	 
	@SuppressWarnings("unchecked")
	public Supplier querySupplier(int id) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get supplier
		Supplier supplier = null;
		//no nested iterator --  iterator: any previous:any
		for (Supplier sup : (List<Supplier>)EntityManager.getAllInstancesOf("Supplier"))
		{
			if (sup.getId() == id)
			{
				supplier = sup;
				break;
			}
				
			
		}
		/* previous state in post-condition*/
 
		/* check precondition */
		if (StandardOPs.oclIsundefined(supplier) == false) 
		{ 
			/* Logic here */
			
			
			refresh();
			// post-condition checking
			if (!(true)) {
				throw new PostconditionException();
			}
			
			refresh(); return supplier;
		}
		else
		{
			throw new PreconditionException();
		}
	}  
	
	 
	@SuppressWarnings("unchecked")
	public boolean modifySupplier(int id, String name) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get supplier
		Supplier supplier = null;
		//no nested iterator --  iterator: any previous:any
		for (Supplier sup : (List<Supplier>)EntityManager.getAllInstancesOf("Supplier"))
		{
			if (sup.getId() == id)
			{
				supplier = sup;
				break;
			}
				
			
		}
		/* previous state in post-condition*/
 
		/* check precondition */
		if (StandardOPs.oclIsundefined(supplier) == false) 
		{ 
			/* Logic here */
			supplier.setId(id);
			supplier.setName(name);
			
			
			refresh();
			// post-condition checking
			if (!(supplier.getId() == id
			 && 
			supplier.getName() == name
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
		//all relevant vars : supplier
		//all relevant entities : Supplier
	}  
	
	static {opINVRelatedEntity.put("modifySupplier", Arrays.asList("Supplier"));}
	 
	@SuppressWarnings("unchecked")
	public boolean deleteSupplier(int id) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get supplier
		Supplier supplier = null;
		//no nested iterator --  iterator: any previous:any
		for (Supplier sup : (List<Supplier>)EntityManager.getAllInstancesOf("Supplier"))
		{
			if (sup.getId() == id)
			{
				supplier = sup;
				break;
			}
				
			
		}
		/* previous state in post-condition*/
 
		/* check precondition */
		if (StandardOPs.oclIsundefined(supplier) == false && StandardOPs.includes(((List<Supplier>)EntityManager.getAllInstancesOf("Supplier")), supplier)) 
		{ 
			/* Logic here */
			EntityManager.deleteObject("Supplier", supplier);
			
			
			refresh();
			// post-condition checking
			if (!(StandardOPs.excludes(((List<Supplier>)EntityManager.getAllInstancesOf("Supplier")), supplier)
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
		//all relevant vars : supplier
		//all relevant entities : Supplier
	}  
	
	static {opINVRelatedEntity.put("deleteSupplier", Arrays.asList("Supplier"));}
	 
	
	
	
	/* temp property for controller */
			
	/* all get and set functions for temp property*/
	
	/* invarints checking*/
	public final static ArrayList<String> allInvariantCheckingFunction = new ArrayList<String>(Arrays.asList());
			
}
