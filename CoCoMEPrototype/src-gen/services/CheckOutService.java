package services;

import entities.*;  
import java.util.List;
import java.time.LocalDate;


public interface CheckOutService {

	/* all system operations of the use case*/
	boolean addItemToCheckout(int itemID, int quantity) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean selectPaymentMethod(String paymentMethod) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean processPayment(float amount, String paymentDetails) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean printReceipt() throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	
	/* all get and set functions for temp property*/
	Sale getCurrentCheckout();
	void setCurrentCheckout(Sale currentcheckout);
	SalesLineItem getCurrentCheckoutLine();
	void setCurrentCheckoutLine(SalesLineItem currentcheckoutline);
	PaymentMethod getCurrentPaymentMethod();
	void setCurrentPaymentMethod(PaymentMethod currentpaymentmethod);
	
	/* all get and set functions for temp property*/
	CashDesk getCurrentCashDesk();
	void setCurrentCashDesk(CashDesk currentcashdesk);
	Store getCurrentStore();
	void setCurrentStore(Store currentstore);
	
	/* invariant checking function */
}
