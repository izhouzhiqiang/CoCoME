package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class CardPayment extends Payment  implements Serializable {
	
	/* all primary attributes */
	private String CardAccountNumber;
	private LocalDate ExpiryDate;
	
	/* all references */
	
	/* all get and set functions */
	public String getCardAccountNumber() {
		return CardAccountNumber;
	}	
	
	public void setCardAccountNumber(String cardaccountnumber) {
		this.CardAccountNumber = cardaccountnumber;
	}
	public LocalDate getExpiryDate() {
		return ExpiryDate;
	}	
	
	public void setExpiryDate(LocalDate expirydate) {
		this.ExpiryDate = expirydate;
	}
	
	/* all functions for reference*/
	


}
