package services.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import services.*;
	
public class ServiceManager {
	
	private static Map<String, List> AllServiceInstance = new HashMap<String, List>();
	
	private static List<CoCoMESystem> CoCoMESystemInstances = new LinkedList<CoCoMESystem>();
	private static List<ThirdPartyServices> ThirdPartyServicesInstances = new LinkedList<ThirdPartyServices>();
	private static List<ProcessSaleService> ProcessSaleServiceInstances = new LinkedList<ProcessSaleService>();
	private static List<ManageStoreCRUDService> ManageStoreCRUDServiceInstances = new LinkedList<ManageStoreCRUDService>();
	private static List<ManageProductCatalogCRUDService> ManageProductCatalogCRUDServiceInstances = new LinkedList<ManageProductCatalogCRUDService>();
	private static List<ManageCashDeskCRUDService> ManageCashDeskCRUDServiceInstances = new LinkedList<ManageCashDeskCRUDService>();
	private static List<ManageCashierCRUDService> ManageCashierCRUDServiceInstances = new LinkedList<ManageCashierCRUDService>();
	private static List<ManageItemCRUDService> ManageItemCRUDServiceInstances = new LinkedList<ManageItemCRUDService>();
	private static List<ManageSupplierCRUDService> ManageSupplierCRUDServiceInstances = new LinkedList<ManageSupplierCRUDService>();
	private static List<CoCoMEOrderProducts> CoCoMEOrderProductsInstances = new LinkedList<CoCoMEOrderProducts>();
	private static List<CheckOutService> CheckOutServiceInstances = new LinkedList<CheckOutService>();
	private static List<ReturnProductsService> ReturnProductsServiceInstances = new LinkedList<ReturnProductsService>();
	private static List<ManageReturnService> ManageReturnServiceInstances = new LinkedList<ManageReturnService>();
	private static List<AcceptPurchaseOrderService> AcceptPurchaseOrderServiceInstances = new LinkedList<AcceptPurchaseOrderService>();
	
	static {
		AllServiceInstance.put("CoCoMESystem", CoCoMESystemInstances);
		AllServiceInstance.put("ThirdPartyServices", ThirdPartyServicesInstances);
		AllServiceInstance.put("ProcessSaleService", ProcessSaleServiceInstances);
		AllServiceInstance.put("ManageStoreCRUDService", ManageStoreCRUDServiceInstances);
		AllServiceInstance.put("ManageProductCatalogCRUDService", ManageProductCatalogCRUDServiceInstances);
		AllServiceInstance.put("ManageCashDeskCRUDService", ManageCashDeskCRUDServiceInstances);
		AllServiceInstance.put("ManageCashierCRUDService", ManageCashierCRUDServiceInstances);
		AllServiceInstance.put("ManageItemCRUDService", ManageItemCRUDServiceInstances);
		AllServiceInstance.put("ManageSupplierCRUDService", ManageSupplierCRUDServiceInstances);
		AllServiceInstance.put("CoCoMEOrderProducts", CoCoMEOrderProductsInstances);
		AllServiceInstance.put("CheckOutService", CheckOutServiceInstances);
		AllServiceInstance.put("ReturnProductsService", ReturnProductsServiceInstances);
		AllServiceInstance.put("ManageReturnService", ManageReturnServiceInstances);
		AllServiceInstance.put("AcceptPurchaseOrderService", AcceptPurchaseOrderServiceInstances);
	} 
	
	public static List getAllInstancesOf(String ClassName) {
			 return AllServiceInstance.get(ClassName);
	}	
	
	public static CoCoMESystem createCoCoMESystem() {
		CoCoMESystem s = new CoCoMESystemImpl();
		CoCoMESystemInstances.add(s);
		return s;
	}
	public static ThirdPartyServices createThirdPartyServices() {
		ThirdPartyServices s = new ThirdPartyServicesImpl();
		ThirdPartyServicesInstances.add(s);
		return s;
	}
	public static ProcessSaleService createProcessSaleService() {
		ProcessSaleService s = new ProcessSaleServiceImpl();
		ProcessSaleServiceInstances.add(s);
		return s;
	}
	public static ManageStoreCRUDService createManageStoreCRUDService() {
		ManageStoreCRUDService s = new ManageStoreCRUDServiceImpl();
		ManageStoreCRUDServiceInstances.add(s);
		return s;
	}
	public static ManageProductCatalogCRUDService createManageProductCatalogCRUDService() {
		ManageProductCatalogCRUDService s = new ManageProductCatalogCRUDServiceImpl();
		ManageProductCatalogCRUDServiceInstances.add(s);
		return s;
	}
	public static ManageCashDeskCRUDService createManageCashDeskCRUDService() {
		ManageCashDeskCRUDService s = new ManageCashDeskCRUDServiceImpl();
		ManageCashDeskCRUDServiceInstances.add(s);
		return s;
	}
	public static ManageCashierCRUDService createManageCashierCRUDService() {
		ManageCashierCRUDService s = new ManageCashierCRUDServiceImpl();
		ManageCashierCRUDServiceInstances.add(s);
		return s;
	}
	public static ManageItemCRUDService createManageItemCRUDService() {
		ManageItemCRUDService s = new ManageItemCRUDServiceImpl();
		ManageItemCRUDServiceInstances.add(s);
		return s;
	}
	public static ManageSupplierCRUDService createManageSupplierCRUDService() {
		ManageSupplierCRUDService s = new ManageSupplierCRUDServiceImpl();
		ManageSupplierCRUDServiceInstances.add(s);
		return s;
	}
	public static CoCoMEOrderProducts createCoCoMEOrderProducts() {
		CoCoMEOrderProducts s = new CoCoMEOrderProductsImpl();
		CoCoMEOrderProductsInstances.add(s);
		return s;
	}
	public static CheckOutService createCheckOutService() {
		CheckOutService s = new CheckOutServiceImpl();
		CheckOutServiceInstances.add(s);
		return s;
	}
	public static ReturnProductsService createReturnProductsService() {
		ReturnProductsService s = new ReturnProductsServiceImpl();
		ReturnProductsServiceInstances.add(s);
		return s;
	}
	public static ManageReturnService createManageReturnService() {
		ManageReturnService s = new ManageReturnServiceImpl();
		ManageReturnServiceInstances.add(s);
		return s;
	}
	public static AcceptPurchaseOrderService createAcceptPurchaseOrderService() {
		AcceptPurchaseOrderService s = new AcceptPurchaseOrderServiceImpl();
		AcceptPurchaseOrderServiceInstances.add(s);
		return s;
	}
}	
