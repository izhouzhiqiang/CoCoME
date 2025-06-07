package services;

import entities.*;  
import java.util.List;
import java.time.LocalDate;


public interface CoCoMEOrderProducts {

	/* all system operations of the use case*/
	boolean makeNewOrder(int orderid) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	List<Item> listAllOutOfStoreProducts() throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean orderItem(int barcode, int quantity) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean chooseSupplier(int supplierID) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean placeOrder() throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	
	/* all get and set functions for temp property*/
	OrderProduct getCurrentOrderProduct();
	void setCurrentOrderProduct(OrderProduct currentorderproduct);
	
	/* all get and set functions for temp property*/
	CashDesk getCurrentCashDesk();
	void setCurrentCashDesk(CashDesk currentcashdesk);
	Store getCurrentStore();
	void setCurrentStore(Store currentstore);
	
	/* invariant checking function */
}
