package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class WechatPayment extends Payment  implements Serializable {
	
	/* all primary attributes */
	private String OpenID;
	
	/* all references */
	
	/* all get and set functions */
	public String getOpenID() {
		return OpenID;
	}	
	
	public void setOpenID(String openid) {
		this.OpenID = openid;
	}
	
	/* all functions for reference*/
	


}
