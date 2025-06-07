package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class SurveillanceDevice implements Serializable {
	
	/* all primary attributes */
	private int DeviceId;
	private String Location;
	private String Status;
	private LocalDate LastMaintenanceTime;
	
	/* all references */
	private Store Store; 
	private Administrator Administrator; 
	
	/* all get and set functions */
	public int getDeviceId() {
		return DeviceId;
	}	
	
	public void setDeviceId(int deviceid) {
		this.DeviceId = deviceid;
	}
	public String getLocation() {
		return Location;
	}	
	
	public void setLocation(String location) {
		this.Location = location;
	}
	public String getStatus() {
		return Status;
	}	
	
	public void setStatus(String status) {
		this.Status = status;
	}
	public LocalDate getLastMaintenanceTime() {
		return LastMaintenanceTime;
	}	
	
	public void setLastMaintenanceTime(LocalDate lastmaintenancetime) {
		this.LastMaintenanceTime = lastmaintenancetime;
	}
	
	/* all functions for reference*/
	public Store getStore() {
		return Store;
	}	
	
	public void setStore(Store store) {
		this.Store = store;
	}			
	public Administrator getAdministrator() {
		return Administrator;
	}	
	
	public void setAdministrator(Administrator administrator) {
		this.Administrator = administrator;
	}			
	

	/* invarints checking*/
	public boolean SurveillanceDevice_UniqueSurveillanceDeviceId() {
		
		if (StandardOPs.isUnique(((List<SurveillanceDevice>)EntityManager.getAllInstancesOf("SurveillanceDevice")), "DeviceId")) {
			return true;
		} else {
			return false;
		}
	}
	
	//check all invariants
	public boolean checkAllInvairant() {
		
		if (SurveillanceDevice_UniqueSurveillanceDeviceId()) {
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
	
	public final static ArrayList<String> allInvariantCheckingFunction = new ArrayList<String>(Arrays.asList("SurveillanceDevice_UniqueSurveillanceDeviceId"));

}
