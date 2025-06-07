package services;

import entities.*;  
import java.util.List;
import java.time.LocalDate;


public interface ProcessSaleService {

	/* all system operations of the use case*/
	boolean makeNewSale() throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean enterItem(int barcode, int quantity) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	float endSale() throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean makeCashPayment(float amount) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean makeCardPayment(String cardAccountNumber, LocalDate expiryDate, float fee) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean makeWechatPayment(float amount, String openID) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean makeAliPayment(float amount, String aliPayAccount) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	
	/* all get and set functions for temp property*/
	SalesLineItem getCurrentSaleLine();
	void setCurrentSaleLine(SalesLineItem currentsaleline);
	Sale getCurrentSale();
	void setCurrentSale(Sale currentsale);
	PaymentMethod getCurrentPaymentMethod();
	void setCurrentPaymentMethod(PaymentMethod currentpaymentmethod);
	
	/* all get and set functions for temp property*/
	CashDesk getCurrentCashDesk();
	void setCurrentCashDesk(CashDesk currentcashdesk);
	Store getCurrentStore();
	void setCurrentStore(Store currentstore);
	
	/* invariant checking function */
}
