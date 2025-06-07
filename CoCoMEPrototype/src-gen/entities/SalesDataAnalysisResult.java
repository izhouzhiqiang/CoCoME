package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class SalesDataAnalysisResult implements Serializable {
	
	/* all primary attributes */
	private int AnalysisId;
	private LocalDate AnalysisTime;
	private float TotalSalesAmount;
	private String BestSellingProducts;
	private String SalesTrend;
	
	/* all references */
	private Administrator Administrator; 
	private Sale Sale; 
	
	/* all get and set functions */
	public int getAnalysisId() {
		return AnalysisId;
	}	
	
	public void setAnalysisId(int analysisid) {
		this.AnalysisId = analysisid;
	}
	public LocalDate getAnalysisTime() {
		return AnalysisTime;
	}	
	
	public void setAnalysisTime(LocalDate analysistime) {
		this.AnalysisTime = analysistime;
	}
	public float getTotalSalesAmount() {
		return TotalSalesAmount;
	}	
	
	public void setTotalSalesAmount(float totalsalesamount) {
		this.TotalSalesAmount = totalsalesamount;
	}
	public String getBestSellingProducts() {
		return BestSellingProducts;
	}	
	
	public void setBestSellingProducts(String bestsellingproducts) {
		this.BestSellingProducts = bestsellingproducts;
	}
	public String getSalesTrend() {
		return SalesTrend;
	}	
	
	public void setSalesTrend(String salestrend) {
		this.SalesTrend = salestrend;
	}
	
	/* all functions for reference*/
	public Administrator getAdministrator() {
		return Administrator;
	}	
	
	public void setAdministrator(Administrator administrator) {
		this.Administrator = administrator;
	}			
	public Sale getSale() {
		return Sale;
	}	
	
	public void setSale(Sale sale) {
		this.Sale = sale;
	}			
	

	/* invarints checking*/
	public boolean SalesDataAnalysisResult_UniqueAnalysisId() {
		
		if (StandardOPs.isUnique(((List<SalesDataAnalysisResult>)EntityManager.getAllInstancesOf("SalesDataAnalysisResult")), "AnalysisId")) {
			return true;
		} else {
			return false;
		}
	}
	
	//check all invariants
	public boolean checkAllInvairant() {
		
		if (SalesDataAnalysisResult_UniqueAnalysisId()) {
			return true;
		} else {
			return false;
		}
	}	
	
	//check invariant by inv name
	public boolean checkInvariant(String INVname) {
		
		try {
			Method m = this.getClass().getDeclaredMethod(INVname);
			return (boolean) m.invoke(this);
					
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	
	}	
	
	public final static ArrayList<String> allInvariantCheckingFunction = new ArrayList<String>(Arrays.asList("SalesDataAnalysisResult_UniqueAnalysisId"));

}
