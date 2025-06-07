package services;

import entities.*;  
import java.util.List;
import java.time.LocalDate;


public interface ManageProductCatalogCRUDService {

	/* all system operations of the use case*/
	boolean createProductCatalog(int id, String name) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	ProductCatalog queryProductCatalog(int id) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean modifyProductCatalog(int id, String name) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean deleteProductCatalog(int id) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	
	/* all get and set functions for temp property*/
	
	/* all get and set functions for temp property*/
	CashDesk getCurrentCashDesk();
	void setCurrentCashDesk(CashDesk currentcashdesk);
	Store getCurrentStore();
	void setCurrentStore(Store currentstore);
	
	/* invariant checking function */
}
