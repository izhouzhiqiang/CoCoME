package services;

import entities.*;  
import java.util.List;
import java.time.LocalDate;


public interface ManageCashDeskCRUDService {

	/* all system operations of the use case*/
	boolean createCashDesk(int id, String name, boolean isopened) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	CashDesk queryCashDesk(int id) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean modifyCashDesk(int id, String name, boolean isopened) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean deleteCashDesk(int id) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	
	/* all get and set functions for temp property*/
	
	/* all get and set functions for temp property*/
	CashDesk getCurrentCashDesk();
	void setCurrentCashDesk(CashDesk currentcashdesk);
	Store getCurrentStore();
	void setCurrentStore(Store currentstore);
	
	/* invariant checking function */
}
