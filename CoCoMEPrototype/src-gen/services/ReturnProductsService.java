package services;

import entities.*;  
import java.util.List;
import java.time.LocalDate;


public interface ReturnProductsService {

	/* all system operations of the use case*/
	boolean initiateReturn(int orderID, int itemID) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean checkReturnEligibility(int requestID) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean provideReturnReason(String reason) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	
	/* all get and set functions for temp property*/
	ReturnRequest getCurrentReturnRequest();
	void setCurrentReturnRequest(ReturnRequest currentreturnrequest);
	OrderProduct getCurrentOrder();
	void setCurrentOrder(OrderProduct currentorder);
	Item getCurrentItem();
	void setCurrentItem(Item currentitem);
	
	/* all get and set functions for temp property*/
	CashDesk getCurrentCashDesk();
	void setCurrentCashDesk(CashDesk currentcashdesk);
	Store getCurrentStore();
	void setCurrentStore(Store currentstore);
	
	/* invariant checking function */
}
