package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class AliPayment extends Payment  implements Serializable {
	
	/* all primary attributes */
	private String AlipayAccount;
	
	/* all references */
	
	/* all get and set functions */
	public String getAlipayAccount() {
		return AlipayAccount;
	}	
	
	public void setAlipayAccount(String alipayaccount) {
		this.AlipayAccount = alipayaccount;
	}
	
	/* all functions for reference*/
	


}
