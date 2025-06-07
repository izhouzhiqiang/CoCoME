package services;

import entities.*;  
import java.util.List;
import java.time.LocalDate;


public interface AcceptPurchaseOrderService {

	/* all system operations of the use case*/
	boolean receivePurchaseOrder(int orderID) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	PurchaseOrder reviewOrderDetails(int orderID) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean confirmOrderAcceptance(boolean isAccepted) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	
	/* all get and set functions for temp property*/
	PurchaseOrder getCurrentReceivedOrder();
	void setCurrentReceivedOrder(PurchaseOrder currentreceivedorder);
	List<Item> getCurrentProductList();
	void setCurrentProductList(List<Item> currentproductlist);
	
	/* all get and set functions for temp property*/
	CashDesk getCurrentCashDesk();
	void setCurrentCashDesk(CashDesk currentcashdesk);
	Store getCurrentStore();
	void setCurrentStore(Store currentstore);
	
	/* invariant checking function */
}
