package services;

import entities.*;  
import java.util.List;
import java.time.LocalDate;


public interface ManageSupplierCRUDService {

	/* all system operations of the use case*/
	boolean createSupplier(int id, String name) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	Supplier querySupplier(int id) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean modifySupplier(int id, String name) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean deleteSupplier(int id) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	
	/* all get and set functions for temp property*/
	
	/* all get and set functions for temp property*/
	CashDesk getCurrentCashDesk();
	void setCurrentCashDesk(CashDesk currentcashdesk);
	Store getCurrentStore();
	void setCurrentStore(Store currentstore);
	
	/* invariant checking function */
}
