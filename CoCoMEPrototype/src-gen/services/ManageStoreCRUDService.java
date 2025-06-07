package services;

import entities.*;  
import java.util.List;
import java.time.LocalDate;


public interface ManageStoreCRUDService {

	/* all system operations of the use case*/
	boolean createStore(int id, String name, String address, boolean isopened) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	Store queryStore(int id) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean modifyStore(int id, String name, String address, boolean isopened) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean deleteStore(int id) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	
	/* all get and set functions for temp property*/
	
	/* all get and set functions for temp property*/
	CashDesk getCurrentCashDesk();
	void setCurrentCashDesk(CashDesk currentcashdesk);
	Store getCurrentStore();
	void setCurrentStore(Store currentstore);
	
	/* invariant checking function */
}
