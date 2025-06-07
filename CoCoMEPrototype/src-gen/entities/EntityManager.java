package entities;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.lang.reflect.Method;
import java.util.Map;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.File;

public class EntityManager {

	private static Map<String, List> AllInstance = new HashMap<String, List>();
	
	private static List<Store> StoreInstances = new LinkedList<Store>();
	private static List<ProductCatalog> ProductCatalogInstances = new LinkedList<ProductCatalog>();
	private static List<CashDesk> CashDeskInstances = new LinkedList<CashDesk>();
	private static List<Sale> SaleInstances = new LinkedList<Sale>();
	private static List<Cashier> CashierInstances = new LinkedList<Cashier>();
	private static List<SalesLineItem> SalesLineItemInstances = new LinkedList<SalesLineItem>();
	private static List<Item> ItemInstances = new LinkedList<Item>();
	private static List<Payment> PaymentInstances = new LinkedList<Payment>();
	private static List<CashPayment> CashPaymentInstances = new LinkedList<CashPayment>();
	private static List<CardPayment> CardPaymentInstances = new LinkedList<CardPayment>();
	private static List<WechatPayment> WechatPaymentInstances = new LinkedList<WechatPayment>();
	private static List<AliPayment> AliPaymentInstances = new LinkedList<AliPayment>();
	private static List<OrderEntry> OrderEntryInstances = new LinkedList<OrderEntry>();
	private static List<Supplier> SupplierInstances = new LinkedList<Supplier>();
	private static List<OrderProduct> OrderProductInstances = new LinkedList<OrderProduct>();
	private static List<Customer> CustomerInstances = new LinkedList<Customer>();
	private static List<ReturnRequest> ReturnRequestInstances = new LinkedList<ReturnRequest>();
	private static List<SurveillanceDevice> SurveillanceDeviceInstances = new LinkedList<SurveillanceDevice>();
	private static List<SalesDataAnalysisResult> SalesDataAnalysisResultInstances = new LinkedList<SalesDataAnalysisResult>();
	private static List<PurchaseOrder> PurchaseOrderInstances = new LinkedList<PurchaseOrder>();
	private static List<RecommendedProduct> RecommendedProductInstances = new LinkedList<RecommendedProduct>();
	private static List<Administrator> AdministratorInstances = new LinkedList<Administrator>();

	
	/* Put instances list into Map */
	static {
		AllInstance.put("Store", StoreInstances);
		AllInstance.put("ProductCatalog", ProductCatalogInstances);
		AllInstance.put("CashDesk", CashDeskInstances);
		AllInstance.put("Sale", SaleInstances);
		AllInstance.put("Cashier", CashierInstances);
		AllInstance.put("SalesLineItem", SalesLineItemInstances);
		AllInstance.put("Item", ItemInstances);
		AllInstance.put("Payment", PaymentInstances);
		AllInstance.put("CashPayment", CashPaymentInstances);
		AllInstance.put("CardPayment", CardPaymentInstances);
		AllInstance.put("WechatPayment", WechatPaymentInstances);
		AllInstance.put("AliPayment", AliPaymentInstances);
		AllInstance.put("OrderEntry", OrderEntryInstances);
		AllInstance.put("Supplier", SupplierInstances);
		AllInstance.put("OrderProduct", OrderProductInstances);
		AllInstance.put("Customer", CustomerInstances);
		AllInstance.put("ReturnRequest", ReturnRequestInstances);
		AllInstance.put("SurveillanceDevice", SurveillanceDeviceInstances);
		AllInstance.put("SalesDataAnalysisResult", SalesDataAnalysisResultInstances);
		AllInstance.put("PurchaseOrder", PurchaseOrderInstances);
		AllInstance.put("RecommendedProduct", RecommendedProductInstances);
		AllInstance.put("Administrator", AdministratorInstances);
	} 
		
	/* Save State */
	public static void save(File file) {
		
		try {
			
			ObjectOutputStream stateSave = new ObjectOutputStream(new FileOutputStream(file));
			
			stateSave.writeObject(StoreInstances);
			stateSave.writeObject(ProductCatalogInstances);
			stateSave.writeObject(CashDeskInstances);
			stateSave.writeObject(SaleInstances);
			stateSave.writeObject(CashierInstances);
			stateSave.writeObject(SalesLineItemInstances);
			stateSave.writeObject(ItemInstances);
			stateSave.writeObject(PaymentInstances);
			stateSave.writeObject(CashPaymentInstances);
			stateSave.writeObject(CardPaymentInstances);
			stateSave.writeObject(WechatPaymentInstances);
			stateSave.writeObject(AliPaymentInstances);
			stateSave.writeObject(OrderEntryInstances);
			stateSave.writeObject(SupplierInstances);
			stateSave.writeObject(OrderProductInstances);
			stateSave.writeObject(CustomerInstances);
			stateSave.writeObject(ReturnRequestInstances);
			stateSave.writeObject(SurveillanceDeviceInstances);
			stateSave.writeObject(SalesDataAnalysisResultInstances);
			stateSave.writeObject(PurchaseOrderInstances);
			stateSave.writeObject(RecommendedProductInstances);
			stateSave.writeObject(AdministratorInstances);
			
			stateSave.close();
					
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/* Load State */
	public static void load(File file) {
		
		try {
			
			ObjectInputStream stateLoad = new ObjectInputStream(new FileInputStream(file));
			
			try {
				
				StoreInstances =  (List<Store>) stateLoad.readObject();
				AllInstance.put("Store", StoreInstances);
				ProductCatalogInstances =  (List<ProductCatalog>) stateLoad.readObject();
				AllInstance.put("ProductCatalog", ProductCatalogInstances);
				CashDeskInstances =  (List<CashDesk>) stateLoad.readObject();
				AllInstance.put("CashDesk", CashDeskInstances);
				SaleInstances =  (List<Sale>) stateLoad.readObject();
				AllInstance.put("Sale", SaleInstances);
				CashierInstances =  (List<Cashier>) stateLoad.readObject();
				AllInstance.put("Cashier", CashierInstances);
				SalesLineItemInstances =  (List<SalesLineItem>) stateLoad.readObject();
				AllInstance.put("SalesLineItem", SalesLineItemInstances);
				ItemInstances =  (List<Item>) stateLoad.readObject();
				AllInstance.put("Item", ItemInstances);
				PaymentInstances =  (List<Payment>) stateLoad.readObject();
				AllInstance.put("Payment", PaymentInstances);
				CashPaymentInstances =  (List<CashPayment>) stateLoad.readObject();
				AllInstance.put("CashPayment", CashPaymentInstances);
				CardPaymentInstances =  (List<CardPayment>) stateLoad.readObject();
				AllInstance.put("CardPayment", CardPaymentInstances);
				WechatPaymentInstances =  (List<WechatPayment>) stateLoad.readObject();
				AllInstance.put("WechatPayment", WechatPaymentInstances);
				AliPaymentInstances =  (List<AliPayment>) stateLoad.readObject();
				AllInstance.put("AliPayment", AliPaymentInstances);
				OrderEntryInstances =  (List<OrderEntry>) stateLoad.readObject();
				AllInstance.put("OrderEntry", OrderEntryInstances);
				SupplierInstances =  (List<Supplier>) stateLoad.readObject();
				AllInstance.put("Supplier", SupplierInstances);
				OrderProductInstances =  (List<OrderProduct>) stateLoad.readObject();
				AllInstance.put("OrderProduct", OrderProductInstances);
				CustomerInstances =  (List<Customer>) stateLoad.readObject();
				AllInstance.put("Customer", CustomerInstances);
				ReturnRequestInstances =  (List<ReturnRequest>) stateLoad.readObject();
				AllInstance.put("ReturnRequest", ReturnRequestInstances);
				SurveillanceDeviceInstances =  (List<SurveillanceDevice>) stateLoad.readObject();
				AllInstance.put("SurveillanceDevice", SurveillanceDeviceInstances);
				SalesDataAnalysisResultInstances =  (List<SalesDataAnalysisResult>) stateLoad.readObject();
				AllInstance.put("SalesDataAnalysisResult", SalesDataAnalysisResultInstances);
				PurchaseOrderInstances =  (List<PurchaseOrder>) stateLoad.readObject();
				AllInstance.put("PurchaseOrder", PurchaseOrderInstances);
				RecommendedProductInstances =  (List<RecommendedProduct>) stateLoad.readObject();
				AllInstance.put("RecommendedProduct", RecommendedProductInstances);
				AdministratorInstances =  (List<Administrator>) stateLoad.readObject();
				AllInstance.put("Administrator", AdministratorInstances);
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
		
	/* create object */  
	public static Object createObject(String Classifer) {
		try
		{
			Class c = Class.forName("entities.EntityManager");
			Method createObjectMethod = c.getDeclaredMethod("create" + Classifer + "Object");
			return createObjectMethod.invoke(c);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/* add object */  
	public static Object addObject(String Classifer, Object ob) {
		try
		{
			Class c = Class.forName("entities.EntityManager");
			Method addObjectMethod = c.getDeclaredMethod("add" + Classifer + "Object", Class.forName("entities." + Classifer));
			return  (boolean) addObjectMethod.invoke(c, ob);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}	
	
	/* add objects */  
	public static Object addObjects(String Classifer, List obs) {
		try
		{
			Class c = Class.forName("entities.EntityManager");
			Method addObjectsMethod = c.getDeclaredMethod("add" + Classifer + "Objects", Class.forName("java.util.List"));
			return  (boolean) addObjectsMethod.invoke(c, obs);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/* Release object */
	public static boolean deleteObject(String Classifer, Object ob) {
		try
		{
			Class c = Class.forName("entities.EntityManager");
			Method deleteObjectMethod = c.getDeclaredMethod("delete" + Classifer + "Object", Class.forName("entities." + Classifer));
			return  (boolean) deleteObjectMethod.invoke(c, ob);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	/* Release objects */
	public static boolean deleteObjects(String Classifer, List obs) {
		try
		{
			Class c = Class.forName("entities.EntityManager");
			Method deleteObjectMethod = c.getDeclaredMethod("delete" + Classifer + "Objects", Class.forName("java.util.List"));
			return  (boolean) deleteObjectMethod.invoke(c, obs);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}		 	
	
	 /* Get all objects belongs to same class */
	public static List getAllInstancesOf(String ClassName) {
			 return AllInstance.get(ClassName);
	}	

   /* Sub-create object */
	public static Store createStoreObject() {
		Store o = new Store();
		return o;
	}
	
	public static boolean addStoreObject(Store o) {
		return StoreInstances.add(o);
	}
	
	public static boolean addStoreObjects(List<Store> os) {
		return StoreInstances.addAll(os);
	}
	
	public static boolean deleteStoreObject(Store o) {
		return StoreInstances.remove(o);
	}
	
	public static boolean deleteStoreObjects(List<Store> os) {
		return StoreInstances.removeAll(os);
	}
	public static ProductCatalog createProductCatalogObject() {
		ProductCatalog o = new ProductCatalog();
		return o;
	}
	
	public static boolean addProductCatalogObject(ProductCatalog o) {
		return ProductCatalogInstances.add(o);
	}
	
	public static boolean addProductCatalogObjects(List<ProductCatalog> os) {
		return ProductCatalogInstances.addAll(os);
	}
	
	public static boolean deleteProductCatalogObject(ProductCatalog o) {
		return ProductCatalogInstances.remove(o);
	}
	
	public static boolean deleteProductCatalogObjects(List<ProductCatalog> os) {
		return ProductCatalogInstances.removeAll(os);
	}
	public static CashDesk createCashDeskObject() {
		CashDesk o = new CashDesk();
		return o;
	}
	
	public static boolean addCashDeskObject(CashDesk o) {
		return CashDeskInstances.add(o);
	}
	
	public static boolean addCashDeskObjects(List<CashDesk> os) {
		return CashDeskInstances.addAll(os);
	}
	
	public static boolean deleteCashDeskObject(CashDesk o) {
		return CashDeskInstances.remove(o);
	}
	
	public static boolean deleteCashDeskObjects(List<CashDesk> os) {
		return CashDeskInstances.removeAll(os);
	}
	public static Sale createSaleObject() {
		Sale o = new Sale();
		return o;
	}
	
	public static boolean addSaleObject(Sale o) {
		return SaleInstances.add(o);
	}
	
	public static boolean addSaleObjects(List<Sale> os) {
		return SaleInstances.addAll(os);
	}
	
	public static boolean deleteSaleObject(Sale o) {
		return SaleInstances.remove(o);
	}
	
	public static boolean deleteSaleObjects(List<Sale> os) {
		return SaleInstances.removeAll(os);
	}
	public static Cashier createCashierObject() {
		Cashier o = new Cashier();
		return o;
	}
	
	public static boolean addCashierObject(Cashier o) {
		return CashierInstances.add(o);
	}
	
	public static boolean addCashierObjects(List<Cashier> os) {
		return CashierInstances.addAll(os);
	}
	
	public static boolean deleteCashierObject(Cashier o) {
		return CashierInstances.remove(o);
	}
	
	public static boolean deleteCashierObjects(List<Cashier> os) {
		return CashierInstances.removeAll(os);
	}
	public static SalesLineItem createSalesLineItemObject() {
		SalesLineItem o = new SalesLineItem();
		return o;
	}
	
	public static boolean addSalesLineItemObject(SalesLineItem o) {
		return SalesLineItemInstances.add(o);
	}
	
	public static boolean addSalesLineItemObjects(List<SalesLineItem> os) {
		return SalesLineItemInstances.addAll(os);
	}
	
	public static boolean deleteSalesLineItemObject(SalesLineItem o) {
		return SalesLineItemInstances.remove(o);
	}
	
	public static boolean deleteSalesLineItemObjects(List<SalesLineItem> os) {
		return SalesLineItemInstances.removeAll(os);
	}
	public static Item createItemObject() {
		Item o = new Item();
		return o;
	}
	
	public static boolean addItemObject(Item o) {
		return ItemInstances.add(o);
	}
	
	public static boolean addItemObjects(List<Item> os) {
		return ItemInstances.addAll(os);
	}
	
	public static boolean deleteItemObject(Item o) {
		return ItemInstances.remove(o);
	}
	
	public static boolean deleteItemObjects(List<Item> os) {
		return ItemInstances.removeAll(os);
	}
	public static Payment createPaymentObject() {
		Payment o = new Payment();
		return o;
	}
	
	public static boolean addPaymentObject(Payment o) {
		return PaymentInstances.add(o);
	}
	
	public static boolean addPaymentObjects(List<Payment> os) {
		return PaymentInstances.addAll(os);
	}
	
	public static boolean deletePaymentObject(Payment o) {
		return PaymentInstances.remove(o);
	}
	
	public static boolean deletePaymentObjects(List<Payment> os) {
		return PaymentInstances.removeAll(os);
	}
	public static CashPayment createCashPaymentObject() {
		CashPayment o = new CashPayment();
		return o;
	}
	
	public static boolean addCashPaymentObject(CashPayment o) {
		return CashPaymentInstances.add(o);
	}
	
	public static boolean addCashPaymentObjects(List<CashPayment> os) {
		return CashPaymentInstances.addAll(os);
	}
	
	public static boolean deleteCashPaymentObject(CashPayment o) {
		return CashPaymentInstances.remove(o);
	}
	
	public static boolean deleteCashPaymentObjects(List<CashPayment> os) {
		return CashPaymentInstances.removeAll(os);
	}
	public static CardPayment createCardPaymentObject() {
		CardPayment o = new CardPayment();
		return o;
	}
	
	public static boolean addCardPaymentObject(CardPayment o) {
		return CardPaymentInstances.add(o);
	}
	
	public static boolean addCardPaymentObjects(List<CardPayment> os) {
		return CardPaymentInstances.addAll(os);
	}
	
	public static boolean deleteCardPaymentObject(CardPayment o) {
		return CardPaymentInstances.remove(o);
	}
	
	public static boolean deleteCardPaymentObjects(List<CardPayment> os) {
		return CardPaymentInstances.removeAll(os);
	}
	public static WechatPayment createWechatPaymentObject() {
		WechatPayment o = new WechatPayment();
		return o;
	}
	
	public static boolean addWechatPaymentObject(WechatPayment o) {
		return WechatPaymentInstances.add(o);
	}
	
	public static boolean addWechatPaymentObjects(List<WechatPayment> os) {
		return WechatPaymentInstances.addAll(os);
	}
	
	public static boolean deleteWechatPaymentObject(WechatPayment o) {
		return WechatPaymentInstances.remove(o);
	}
	
	public static boolean deleteWechatPaymentObjects(List<WechatPayment> os) {
		return WechatPaymentInstances.removeAll(os);
	}
	public static AliPayment createAliPaymentObject() {
		AliPayment o = new AliPayment();
		return o;
	}
	
	public static boolean addAliPaymentObject(AliPayment o) {
		return AliPaymentInstances.add(o);
	}
	
	public static boolean addAliPaymentObjects(List<AliPayment> os) {
		return AliPaymentInstances.addAll(os);
	}
	
	public static boolean deleteAliPaymentObject(AliPayment o) {
		return AliPaymentInstances.remove(o);
	}
	
	public static boolean deleteAliPaymentObjects(List<AliPayment> os) {
		return AliPaymentInstances.removeAll(os);
	}
	public static OrderEntry createOrderEntryObject() {
		OrderEntry o = new OrderEntry();
		return o;
	}
	
	public static boolean addOrderEntryObject(OrderEntry o) {
		return OrderEntryInstances.add(o);
	}
	
	public static boolean addOrderEntryObjects(List<OrderEntry> os) {
		return OrderEntryInstances.addAll(os);
	}
	
	public static boolean deleteOrderEntryObject(OrderEntry o) {
		return OrderEntryInstances.remove(o);
	}
	
	public static boolean deleteOrderEntryObjects(List<OrderEntry> os) {
		return OrderEntryInstances.removeAll(os);
	}
	public static Supplier createSupplierObject() {
		Supplier o = new Supplier();
		return o;
	}
	
	public static boolean addSupplierObject(Supplier o) {
		return SupplierInstances.add(o);
	}
	
	public static boolean addSupplierObjects(List<Supplier> os) {
		return SupplierInstances.addAll(os);
	}
	
	public static boolean deleteSupplierObject(Supplier o) {
		return SupplierInstances.remove(o);
	}
	
	public static boolean deleteSupplierObjects(List<Supplier> os) {
		return SupplierInstances.removeAll(os);
	}
	public static OrderProduct createOrderProductObject() {
		OrderProduct o = new OrderProduct();
		return o;
	}
	
	public static boolean addOrderProductObject(OrderProduct o) {
		return OrderProductInstances.add(o);
	}
	
	public static boolean addOrderProductObjects(List<OrderProduct> os) {
		return OrderProductInstances.addAll(os);
	}
	
	public static boolean deleteOrderProductObject(OrderProduct o) {
		return OrderProductInstances.remove(o);
	}
	
	public static boolean deleteOrderProductObjects(List<OrderProduct> os) {
		return OrderProductInstances.removeAll(os);
	}
	public static Customer createCustomerObject() {
		Customer o = new Customer();
		return o;
	}
	
	public static boolean addCustomerObject(Customer o) {
		return CustomerInstances.add(o);
	}
	
	public static boolean addCustomerObjects(List<Customer> os) {
		return CustomerInstances.addAll(os);
	}
	
	public static boolean deleteCustomerObject(Customer o) {
		return CustomerInstances.remove(o);
	}
	
	public static boolean deleteCustomerObjects(List<Customer> os) {
		return CustomerInstances.removeAll(os);
	}
	public static ReturnRequest createReturnRequestObject() {
		ReturnRequest o = new ReturnRequest();
		return o;
	}
	
	public static boolean addReturnRequestObject(ReturnRequest o) {
		return ReturnRequestInstances.add(o);
	}
	
	public static boolean addReturnRequestObjects(List<ReturnRequest> os) {
		return ReturnRequestInstances.addAll(os);
	}
	
	public static boolean deleteReturnRequestObject(ReturnRequest o) {
		return ReturnRequestInstances.remove(o);
	}
	
	public static boolean deleteReturnRequestObjects(List<ReturnRequest> os) {
		return ReturnRequestInstances.removeAll(os);
	}
	public static SurveillanceDevice createSurveillanceDeviceObject() {
		SurveillanceDevice o = new SurveillanceDevice();
		return o;
	}
	
	public static boolean addSurveillanceDeviceObject(SurveillanceDevice o) {
		return SurveillanceDeviceInstances.add(o);
	}
	
	public static boolean addSurveillanceDeviceObjects(List<SurveillanceDevice> os) {
		return SurveillanceDeviceInstances.addAll(os);
	}
	
	public static boolean deleteSurveillanceDeviceObject(SurveillanceDevice o) {
		return SurveillanceDeviceInstances.remove(o);
	}
	
	public static boolean deleteSurveillanceDeviceObjects(List<SurveillanceDevice> os) {
		return SurveillanceDeviceInstances.removeAll(os);
	}
	public static SalesDataAnalysisResult createSalesDataAnalysisResultObject() {
		SalesDataAnalysisResult o = new SalesDataAnalysisResult();
		return o;
	}
	
	public static boolean addSalesDataAnalysisResultObject(SalesDataAnalysisResult o) {
		return SalesDataAnalysisResultInstances.add(o);
	}
	
	public static boolean addSalesDataAnalysisResultObjects(List<SalesDataAnalysisResult> os) {
		return SalesDataAnalysisResultInstances.addAll(os);
	}
	
	public static boolean deleteSalesDataAnalysisResultObject(SalesDataAnalysisResult o) {
		return SalesDataAnalysisResultInstances.remove(o);
	}
	
	public static boolean deleteSalesDataAnalysisResultObjects(List<SalesDataAnalysisResult> os) {
		return SalesDataAnalysisResultInstances.removeAll(os);
	}
	public static PurchaseOrder createPurchaseOrderObject() {
		PurchaseOrder o = new PurchaseOrder();
		return o;
	}
	
	public static boolean addPurchaseOrderObject(PurchaseOrder o) {
		return PurchaseOrderInstances.add(o);
	}
	
	public static boolean addPurchaseOrderObjects(List<PurchaseOrder> os) {
		return PurchaseOrderInstances.addAll(os);
	}
	
	public static boolean deletePurchaseOrderObject(PurchaseOrder o) {
		return PurchaseOrderInstances.remove(o);
	}
	
	public static boolean deletePurchaseOrderObjects(List<PurchaseOrder> os) {
		return PurchaseOrderInstances.removeAll(os);
	}
	public static RecommendedProduct createRecommendedProductObject() {
		RecommendedProduct o = new RecommendedProduct();
		return o;
	}
	
	public static boolean addRecommendedProductObject(RecommendedProduct o) {
		return RecommendedProductInstances.add(o);
	}
	
	public static boolean addRecommendedProductObjects(List<RecommendedProduct> os) {
		return RecommendedProductInstances.addAll(os);
	}
	
	public static boolean deleteRecommendedProductObject(RecommendedProduct o) {
		return RecommendedProductInstances.remove(o);
	}
	
	public static boolean deleteRecommendedProductObjects(List<RecommendedProduct> os) {
		return RecommendedProductInstances.removeAll(os);
	}
	public static Administrator createAdministratorObject() {
		Administrator o = new Administrator();
		return o;
	}
	
	public static boolean addAdministratorObject(Administrator o) {
		return AdministratorInstances.add(o);
	}
	
	public static boolean addAdministratorObjects(List<Administrator> os) {
		return AdministratorInstances.addAll(os);
	}
	
	public static boolean deleteAdministratorObject(Administrator o) {
		return AdministratorInstances.remove(o);
	}
	
	public static boolean deleteAdministratorObjects(List<Administrator> os) {
		return AdministratorInstances.removeAll(os);
	}
  
}

