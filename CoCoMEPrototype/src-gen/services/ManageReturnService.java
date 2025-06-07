package services;

import entities.*;  
import java.util.List;
import java.time.LocalDate;


public interface ManageReturnService {

	/* all system operations of the use case*/
	boolean reviewReturnRequest(int requestID) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean approveOrRejectReturn(boolean isApproved) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean processRefund(float amount, String paymentMethod) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	
	/* all get and set functions for temp property*/
	ReturnRequest getCurrentReturnRequest();
	void setCurrentReturnRequest(ReturnRequest currentreturnrequest);
	Item getCurrentItem();
	void setCurrentItem(Item currentitem);
	Sale getCurrentSale();
	void setCurrentSale(Sale currentsale);
	
	/* all get and set functions for temp property*/
	CashDesk getCurrentCashDesk();
	void setCurrentCashDesk(CashDesk currentcashdesk);
	Store getCurrentStore();
	void setCurrentStore(Store currentstore);
	
	/* invariant checking function */
}
