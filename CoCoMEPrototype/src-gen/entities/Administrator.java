package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class Administrator implements Serializable {
	
	/* all primary attributes */
	private int Id;
	private String Name;
	private String Username;
	private String Password;
	
	/* all references */
	private Store ManagedStore; 
	private List<ReturnRequest> HandledReturnRequests = new LinkedList<ReturnRequest>(); 
	private List<SalesDataAnalysisResult> ConductedDataAnalysis = new LinkedList<SalesDataAnalysisResult>(); 
	private List<SurveillanceDevice> MonitoredDevices = new LinkedList<SurveillanceDevice>(); 
	
	/* all get and set functions */
	public int getId() {
		return Id;
	}	
	
	public void setId(int id) {
		this.Id = id;
	}
	public String getName() {
		return Name;
	}	
	
	public void setName(String name) {
		this.Name = name;
	}
	public String getUsername() {
		return Username;
	}	
	
	public void setUsername(String username) {
		this.Username = username;
	}
	public String getPassword() {
		return Password;
	}	
	
	public void setPassword(String password) {
		this.Password = password;
	}
	
	/* all functions for reference*/
	public Store getManagedStore() {
		return ManagedStore;
	}	
	
	public void setManagedStore(Store store) {
		this.ManagedStore = store;
	}			
	public List<ReturnRequest> getHandledReturnRequests() {
		return HandledReturnRequests;
	}	
	
	public void addHandledReturnRequests(ReturnRequest returnrequest) {
		this.HandledReturnRequests.add(returnrequest);
	}
	
	public void deleteHandledReturnRequests(ReturnRequest returnrequest) {
		this.HandledReturnRequests.remove(returnrequest);
	}
	public List<SalesDataAnalysisResult> getConductedDataAnalysis() {
		return ConductedDataAnalysis;
	}	
	
	public void addConductedDataAnalysis(SalesDataAnalysisResult salesdataanalysisresult) {
		this.ConductedDataAnalysis.add(salesdataanalysisresult);
	}
	
	public void deleteConductedDataAnalysis(SalesDataAnalysisResult salesdataanalysisresult) {
		this.ConductedDataAnalysis.remove(salesdataanalysisresult);
	}
	public List<SurveillanceDevice> getMonitoredDevices() {
		return MonitoredDevices;
	}	
	
	public void addMonitoredDevices(SurveillanceDevice surveillancedevice) {
		this.MonitoredDevices.add(surveillancedevice);
	}
	
	public void deleteMonitoredDevices(SurveillanceDevice surveillancedevice) {
		this.MonitoredDevices.remove(surveillancedevice);
	}
	

	/* invarints checking*/
	public boolean Administrator_UniqueAdministratorId() {
		
		if (StandardOPs.isUnique(((List<Administrator>)EntityManager.getAllInstancesOf("Administrator")), "Id")) {
			return true;
		} else {
			return false;
		}
	}
	
	//check all invariants
	public boolean checkAllInvairant() {
		
		if (Administrator_UniqueAdministratorId()) {
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
	
	public final static ArrayList<String> allInvariantCheckingFunction = new ArrayList<String>(Arrays.asList("Administrator_UniqueAdministratorId"));

}
