package services;

import entities.*;  
import java.util.List;
import java.time.LocalDate;


public interface ManageCashierCRUDService {

	/* all system operations of the use case*/
	boolean createCashier(int id, String name) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	Cashier queryCashier(int id) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean modifyCashier(int id, String name) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean deleteCashier(int id) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	
	/* all get and set functions for temp property*/
	
	/* all get and set functions for temp property*/
	CashDesk getCurrentCashDesk();
	void setCurrentCashDesk(CashDesk currentcashdesk);
	Store getCurrentStore();
	void setCurrentStore(Store currentstore);
	
	/* invariant checking function */
}
