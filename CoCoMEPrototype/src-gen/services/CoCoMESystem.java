package services;

import entities.*;  
import java.util.List;
import java.time.LocalDate;


public interface CoCoMESystem {

	/* all system operations of the use case*/
	boolean openCashDesk(int cashDeskID) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean closeCashDesk(int cashDeskID) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean openStore(int storeID) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean closeStore(int storeID) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean changePrice(int barcode, float newPrice) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean receiveOrderedProduct(int orderID) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	List<Supplier> listSuppliers() throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	List<Item> showStockReports() throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	
	/* all get and set functions for temp property*/
	CashDesk getCurrentCashDesk();
	void setCurrentCashDesk(CashDesk currentcashdesk);
	Store getCurrentStore();
	void setCurrentStore(Store currentstore);
	
	
	/* invariant checking function */
}
