package gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TabPane;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.Tooltip;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import java.io.File;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;
import java.time.LocalDate;
import java.util.LinkedList;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import gui.supportclass.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.util.Callback;
import services.*;
import services.impl.*;
import java.time.format.DateTimeFormatter;
import java.lang.reflect.Method;

import entities.*;

public class PrototypeController implements Initializable {


	DateTimeFormatter dateformatter;
	 
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
		cocomesystem_service = ServiceManager.createCoCoMESystem();
		thirdpartyservices_service = ServiceManager.createThirdPartyServices();
		processsaleservice_service = ServiceManager.createProcessSaleService();
		managestorecrudservice_service = ServiceManager.createManageStoreCRUDService();
		manageproductcatalogcrudservice_service = ServiceManager.createManageProductCatalogCRUDService();
		managecashdeskcrudservice_service = ServiceManager.createManageCashDeskCRUDService();
		managecashiercrudservice_service = ServiceManager.createManageCashierCRUDService();
		manageitemcrudservice_service = ServiceManager.createManageItemCRUDService();
		managesuppliercrudservice_service = ServiceManager.createManageSupplierCRUDService();
		cocomeorderproducts_service = ServiceManager.createCoCoMEOrderProducts();
		checkoutservice_service = ServiceManager.createCheckOutService();
		returnproductsservice_service = ServiceManager.createReturnProductsService();
		managereturnservice_service = ServiceManager.createManageReturnService();
		acceptpurchaseorderservice_service = ServiceManager.createAcceptPurchaseOrderService();
				
		this.dateformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
	   	 //prepare data for contract
	   	 prepareData();
	   	 
	   	 //generate invariant panel
	   	 genereateInvairantPanel();
	   	 
		 //Actor Threeview Binding
		 actorTreeViewBinding();
		 
		 //Generate
		 generatOperationPane();
		 genereateOpInvariantPanel();
		 
		 //prilimariry data
		 try {
			DataFitService.fit();
		 } catch (PreconditionException e) {
			// TODO Auto-generated catch block
		 	e.printStackTrace();
		 }
		 
		 //generate class statistic
		 classStatisicBingding();
		 
		 //generate object statistic
		 generateObjectTable();
		 
		 //genereate association statistic
		 associationStatisicBingding();

		 //set listener 
		 setListeners();
	}
	
	/**
	 * deepCopyforTreeItem (Actor Generation)
	 */
	TreeItem<String> deepCopyTree(TreeItem<String> item) {
		    TreeItem<String> copy = new TreeItem<String>(item.getValue());
		    for (TreeItem<String> child : item.getChildren()) {
		        copy.getChildren().add(deepCopyTree(child));
		    }
		    return copy;
	}
	
	/**
	 * check all invariant and update invariant panel
	 */
	public void invairantPanelUpdate() {
		
		try {
			
			for (Entry<String, Label> inv : entity_invariants_label_map.entrySet()) {
				String invname = inv.getKey();
				String[] invt = invname.split("_");
				String entityName = invt[0];
				for (Object o : EntityManager.getAllInstancesOf(entityName)) {				
					 Method m = o.getClass().getMethod(invname);
					 if ((boolean)m.invoke(o) == false) {
						 inv.getValue().setStyle("-fx-max-width: Infinity;" + 
									"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #af0c27 100%);" +
								    "-fx-padding: 6px;" +
								    "-fx-border-color: black;");
						 break;
					 }
				}				
			}
			
			for (Entry<String, Label> inv : service_invariants_label_map.entrySet()) {
				String invname = inv.getKey();
				String[] invt = invname.split("_");
				String serviceName = invt[0];
				for (Object o : ServiceManager.getAllInstancesOf(serviceName)) {				
					 Method m = o.getClass().getMethod(invname);
					 if ((boolean)m.invoke(o) == false) {
						 inv.getValue().setStyle("-fx-max-width: Infinity;" + 
									"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #af0c27 100%);" +
								    "-fx-padding: 6px;" +
								    "-fx-border-color: black;");
						 break;
					 }
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	

	/**
	 * check op invariant and update op invariant panel
	 */		
	public void opInvairantPanelUpdate() {
		
		try {
			
			for (Entry<String, Label> inv : op_entity_invariants_label_map.entrySet()) {
				String invname = inv.getKey();
				String[] invt = invname.split("_");
				String entityName = invt[0];
				for (Object o : EntityManager.getAllInstancesOf(entityName)) {
					 Method m = o.getClass().getMethod(invname);
					 if ((boolean)m.invoke(o) == false) {
						 inv.getValue().setStyle("-fx-max-width: Infinity;" + 
									"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #af0c27 100%);" +
								    "-fx-padding: 6px;" +
								    "-fx-border-color: black;");
						 break;
					 }
				}
			}
			
			for (Entry<String, Label> inv : op_service_invariants_label_map.entrySet()) {
				String invname = inv.getKey();
				String[] invt = invname.split("_");
				String serviceName = invt[0];
				for (Object o : ServiceManager.getAllInstancesOf(serviceName)) {
					 Method m = o.getClass().getMethod(invname);
					 if ((boolean)m.invoke(o) == false) {
						 inv.getValue().setStyle("-fx-max-width: Infinity;" + 
									"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #af0c27 100%);" +
								    "-fx-padding: 6px;" +
								    "-fx-border-color: black;");
						 break;
					 }
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* 
	*	generate op invariant panel 
	*/
	public void genereateOpInvariantPanel() {
		
		opInvariantPanel = new HashMap<String, VBox>();
		op_entity_invariants_label_map = new LinkedHashMap<String, Label>();
		op_service_invariants_label_map = new LinkedHashMap<String, Label>();
		
		VBox v;
		List<String> entities;
		v = new VBox();
		
		//entities invariants
		entities = CoCoMESystemImpl.opINVRelatedEntity.get("openStore");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("openStore" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("CoCoMESystem")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("openStore", v);
		
		v = new VBox();
		
		//entities invariants
		entities = CoCoMESystemImpl.opINVRelatedEntity.get("closeStore");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("closeStore" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("CoCoMESystem")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("closeStore", v);
		
		v = new VBox();
		
		//entities invariants
		entities = CoCoMESystemImpl.opINVRelatedEntity.get("openCashDesk");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("openCashDesk" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("CoCoMESystem")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("openCashDesk", v);
		
		v = new VBox();
		
		//entities invariants
		entities = CoCoMESystemImpl.opINVRelatedEntity.get("closeCashDesk");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("closeCashDesk" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("CoCoMESystem")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("closeCashDesk", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ProcessSaleServiceImpl.opINVRelatedEntity.get("makeNewSale");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("makeNewSale" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ProcessSaleService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("makeNewSale", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ProcessSaleServiceImpl.opINVRelatedEntity.get("enterItem");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("enterItem" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ProcessSaleService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("enterItem", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ProcessSaleServiceImpl.opINVRelatedEntity.get("endSale");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("endSale" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ProcessSaleService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("endSale", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ProcessSaleServiceImpl.opINVRelatedEntity.get("makeCashPayment");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("makeCashPayment" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ProcessSaleService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("makeCashPayment", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ProcessSaleServiceImpl.opINVRelatedEntity.get("makeCardPayment");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("makeCardPayment" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ProcessSaleService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("makeCardPayment", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ProcessSaleServiceImpl.opINVRelatedEntity.get("makeWechatPayment");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("makeWechatPayment" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ProcessSaleService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("makeWechatPayment", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ProcessSaleServiceImpl.opINVRelatedEntity.get("makeAliPayment");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("makeAliPayment" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ProcessSaleService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("makeAliPayment", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ThirdPartyServicesImpl.opINVRelatedEntity.get("thirdPartyCardPaymentService");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("thirdPartyCardPaymentService" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ThirdPartyServices")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("thirdPartyCardPaymentService", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ThirdPartyServicesImpl.opINVRelatedEntity.get("thirdPartyWechatPaymentService");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("thirdPartyWechatPaymentService" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ThirdPartyServices")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("thirdPartyWechatPaymentService", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ThirdPartyServicesImpl.opINVRelatedEntity.get("thirdPartyAliPaymentService");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("thirdPartyAliPaymentService" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ThirdPartyServices")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("thirdPartyAliPaymentService", v);
		
		v = new VBox();
		
		//entities invariants
		entities = CoCoMEOrderProductsImpl.opINVRelatedEntity.get("makeNewOrder");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("makeNewOrder" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("CoCoMEOrderProducts")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("makeNewOrder", v);
		
		v = new VBox();
		
		//entities invariants
		entities = CoCoMEOrderProductsImpl.opINVRelatedEntity.get("listAllOutOfStoreProducts");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("listAllOutOfStoreProducts" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("CoCoMEOrderProducts")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("listAllOutOfStoreProducts", v);
		
		v = new VBox();
		
		//entities invariants
		entities = CoCoMEOrderProductsImpl.opINVRelatedEntity.get("orderItem");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("orderItem" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("CoCoMEOrderProducts")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("orderItem", v);
		
		v = new VBox();
		
		//entities invariants
		entities = CoCoMEOrderProductsImpl.opINVRelatedEntity.get("chooseSupplier");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("chooseSupplier" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("CoCoMEOrderProducts")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("chooseSupplier", v);
		
		v = new VBox();
		
		//entities invariants
		entities = CoCoMEOrderProductsImpl.opINVRelatedEntity.get("placeOrder");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("placeOrder" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("CoCoMEOrderProducts")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("placeOrder", v);
		
		v = new VBox();
		
		//entities invariants
		entities = CoCoMESystemImpl.opINVRelatedEntity.get("changePrice");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("changePrice" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("CoCoMESystem")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("changePrice", v);
		
		v = new VBox();
		
		//entities invariants
		entities = CoCoMESystemImpl.opINVRelatedEntity.get("receiveOrderedProduct");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("receiveOrderedProduct" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("CoCoMESystem")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("receiveOrderedProduct", v);
		
		v = new VBox();
		
		//entities invariants
		entities = CoCoMESystemImpl.opINVRelatedEntity.get("listSuppliers");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("listSuppliers" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("CoCoMESystem")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("listSuppliers", v);
		
		v = new VBox();
		
		//entities invariants
		entities = CoCoMESystemImpl.opINVRelatedEntity.get("showStockReports");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("showStockReports" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("CoCoMESystem")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("showStockReports", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageStoreCRUDServiceImpl.opINVRelatedEntity.get("createStore");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("createStore" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageStoreCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("createStore", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageStoreCRUDServiceImpl.opINVRelatedEntity.get("queryStore");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("queryStore" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageStoreCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("queryStore", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageStoreCRUDServiceImpl.opINVRelatedEntity.get("modifyStore");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("modifyStore" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageStoreCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("modifyStore", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageStoreCRUDServiceImpl.opINVRelatedEntity.get("deleteStore");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("deleteStore" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageStoreCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("deleteStore", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageProductCatalogCRUDServiceImpl.opINVRelatedEntity.get("createProductCatalog");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("createProductCatalog" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageProductCatalogCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("createProductCatalog", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageProductCatalogCRUDServiceImpl.opINVRelatedEntity.get("queryProductCatalog");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("queryProductCatalog" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageProductCatalogCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("queryProductCatalog", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageProductCatalogCRUDServiceImpl.opINVRelatedEntity.get("modifyProductCatalog");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("modifyProductCatalog" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageProductCatalogCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("modifyProductCatalog", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageProductCatalogCRUDServiceImpl.opINVRelatedEntity.get("deleteProductCatalog");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("deleteProductCatalog" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageProductCatalogCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("deleteProductCatalog", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageCashDeskCRUDServiceImpl.opINVRelatedEntity.get("createCashDesk");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("createCashDesk" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageCashDeskCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("createCashDesk", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageCashDeskCRUDServiceImpl.opINVRelatedEntity.get("queryCashDesk");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("queryCashDesk" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageCashDeskCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("queryCashDesk", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageCashDeskCRUDServiceImpl.opINVRelatedEntity.get("modifyCashDesk");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("modifyCashDesk" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageCashDeskCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("modifyCashDesk", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageCashDeskCRUDServiceImpl.opINVRelatedEntity.get("deleteCashDesk");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("deleteCashDesk" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageCashDeskCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("deleteCashDesk", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageCashierCRUDServiceImpl.opINVRelatedEntity.get("createCashier");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("createCashier" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageCashierCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("createCashier", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageCashierCRUDServiceImpl.opINVRelatedEntity.get("queryCashier");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("queryCashier" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageCashierCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("queryCashier", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageCashierCRUDServiceImpl.opINVRelatedEntity.get("modifyCashier");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("modifyCashier" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageCashierCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("modifyCashier", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageCashierCRUDServiceImpl.opINVRelatedEntity.get("deleteCashier");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("deleteCashier" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageCashierCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("deleteCashier", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageItemCRUDServiceImpl.opINVRelatedEntity.get("createItem");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("createItem" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageItemCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("createItem", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageItemCRUDServiceImpl.opINVRelatedEntity.get("queryItem");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("queryItem" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageItemCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("queryItem", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageItemCRUDServiceImpl.opINVRelatedEntity.get("modifyItem");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("modifyItem" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageItemCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("modifyItem", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageItemCRUDServiceImpl.opINVRelatedEntity.get("deleteItem");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("deleteItem" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageItemCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("deleteItem", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageSupplierCRUDServiceImpl.opINVRelatedEntity.get("createSupplier");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("createSupplier" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageSupplierCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("createSupplier", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageSupplierCRUDServiceImpl.opINVRelatedEntity.get("querySupplier");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("querySupplier" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageSupplierCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("querySupplier", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageSupplierCRUDServiceImpl.opINVRelatedEntity.get("modifySupplier");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("modifySupplier" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageSupplierCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("modifySupplier", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageSupplierCRUDServiceImpl.opINVRelatedEntity.get("deleteSupplier");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("deleteSupplier" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageSupplierCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("deleteSupplier", v);
		
		v = new VBox();
		
		//entities invariants
		entities = CheckOutServiceImpl.opINVRelatedEntity.get("addItemToCheckout");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("addItemToCheckout" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("CheckOutService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("addItemToCheckout", v);
		
		v = new VBox();
		
		//entities invariants
		entities = CheckOutServiceImpl.opINVRelatedEntity.get("selectPaymentMethod");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("selectPaymentMethod" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("CheckOutService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("selectPaymentMethod", v);
		
		v = new VBox();
		
		//entities invariants
		entities = CheckOutServiceImpl.opINVRelatedEntity.get("processPayment");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("processPayment" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("CheckOutService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("processPayment", v);
		
		v = new VBox();
		
		//entities invariants
		entities = CheckOutServiceImpl.opINVRelatedEntity.get("printReceipt");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("printReceipt" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("CheckOutService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("printReceipt", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ReturnProductsServiceImpl.opINVRelatedEntity.get("initiateReturn");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("initiateReturn" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ReturnProductsService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("initiateReturn", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ReturnProductsServiceImpl.opINVRelatedEntity.get("checkReturnEligibility");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("checkReturnEligibility" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ReturnProductsService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("checkReturnEligibility", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ReturnProductsServiceImpl.opINVRelatedEntity.get("provideReturnReason");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("provideReturnReason" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ReturnProductsService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("provideReturnReason", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageReturnServiceImpl.opINVRelatedEntity.get("reviewReturnRequest");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("reviewReturnRequest" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageReturnService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("reviewReturnRequest", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageReturnServiceImpl.opINVRelatedEntity.get("approveOrRejectReturn");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("approveOrRejectReturn" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageReturnService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("approveOrRejectReturn", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageReturnServiceImpl.opINVRelatedEntity.get("processRefund");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("processRefund" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageReturnService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("processRefund", v);
		
		v = new VBox();
		
		//entities invariants
		entities = AcceptPurchaseOrderServiceImpl.opINVRelatedEntity.get("receivePurchaseOrder");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("receivePurchaseOrder" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("AcceptPurchaseOrderService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("receivePurchaseOrder", v);
		
		v = new VBox();
		
		//entities invariants
		entities = AcceptPurchaseOrderServiceImpl.opINVRelatedEntity.get("reviewOrderDetails");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("reviewOrderDetails" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("AcceptPurchaseOrderService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("reviewOrderDetails", v);
		
		v = new VBox();
		
		//entities invariants
		entities = AcceptPurchaseOrderServiceImpl.opINVRelatedEntity.get("confirmOrderAcceptance");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("confirmOrderAcceptance" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("AcceptPurchaseOrderService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("confirmOrderAcceptance", v);
		
		
	}
	
	
	/*
	*  generate invariant panel
	*/
	public void genereateInvairantPanel() {
		
		service_invariants_label_map = new LinkedHashMap<String, Label>();
		entity_invariants_label_map = new LinkedHashMap<String, Label>();
		
		//entity_invariants_map
		VBox v = new VBox();
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			Label l = new Label(inv.getKey());
			l.setStyle("-fx-max-width: Infinity;" + 
					"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
				    "-fx-padding: 6px;" +
				    "-fx-border-color: black;");
			
			Tooltip tp = new Tooltip();
			tp.setText(inv.getValue());
			l.setTooltip(tp);
			
			service_invariants_label_map.put(inv.getKey(), l);
			v.getChildren().add(l);
			
		}
		//entity invariants
		for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
			
			String INVname = inv.getKey();
			Label l = new Label(INVname);
			if (INVname.contains("AssociationInvariants")) {
				l.setStyle("-fx-max-width: Infinity;" + 
					"-fx-background-color: linear-gradient(to right, #099b17 0%, #F0FFFF 100%);" +
				    "-fx-padding: 6px;" +
				    "-fx-border-color: black;");
			} else {
				l.setStyle("-fx-max-width: Infinity;" + 
									"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
								    "-fx-padding: 6px;" +
								    "-fx-border-color: black;");
			}	
			Tooltip tp = new Tooltip();
			tp.setText(inv.getValue());
			l.setTooltip(tp);
			
			entity_invariants_label_map.put(inv.getKey(), l);
			v.getChildren().add(l);
			
		}
		ScrollPane scrollPane = new ScrollPane(v);
		scrollPane.setFitToWidth(true);
		all_invariant_pane.setMaxHeight(850);
		
		all_invariant_pane.setContent(scrollPane);
	}	
	
	
	
	/* 
	*	mainPane add listener
	*/
	public void setListeners() {
		 mainPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
			 
			 	if (newTab.getText().equals("System State")) {
			 		System.out.println("refresh all");
			 		refreshAll();
			 	}
		    
		    });
	}
	
	
	//checking all invariants
	public void checkAllInvariants() {
		
		invairantPanelUpdate();
	
	}	
	
	//refresh all
	public void refreshAll() {
		
		invairantPanelUpdate();
		classStatisticUpdate();
		generateObjectTable();
	}
	
	
	//update association
	public void updateAssociation(String className) {
		
		for (AssociationInfo assoc : allassociationData.get(className)) {
			assoc.computeAssociationNumber();
		}
		
	}
	
	public void updateAssociation(String className, int index) {
		
		for (AssociationInfo assoc : allassociationData.get(className)) {
			assoc.computeAssociationNumber(index);
		}
		
	}	
	
	public void generateObjectTable() {
		
		allObjectTables = new LinkedHashMap<String, TableView>();
		
		TableView<Map<String, String>> tableStore = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableStore_Id = new TableColumn<Map<String, String>, String>("Id");
		tableStore_Id.setMinWidth("Id".length()*10);
		tableStore_Id.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Id"));
		    }
		});	
		tableStore.getColumns().add(tableStore_Id);
		TableColumn<Map<String, String>, String> tableStore_Name = new TableColumn<Map<String, String>, String>("Name");
		tableStore_Name.setMinWidth("Name".length()*10);
		tableStore_Name.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Name"));
		    }
		});	
		tableStore.getColumns().add(tableStore_Name);
		TableColumn<Map<String, String>, String> tableStore_Address = new TableColumn<Map<String, String>, String>("Address");
		tableStore_Address.setMinWidth("Address".length()*10);
		tableStore_Address.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Address"));
		    }
		});	
		tableStore.getColumns().add(tableStore_Address);
		TableColumn<Map<String, String>, String> tableStore_IsOpened = new TableColumn<Map<String, String>, String>("IsOpened");
		tableStore_IsOpened.setMinWidth("IsOpened".length()*10);
		tableStore_IsOpened.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("IsOpened"));
		    }
		});	
		tableStore.getColumns().add(tableStore_IsOpened);
		
		//table data
		ObservableList<Map<String, String>> dataStore = FXCollections.observableArrayList();
		List<Store> rsStore = EntityManager.getAllInstancesOf("Store");
		for (Store r : rsStore) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			unit.put("Id", String.valueOf(r.getId()));
			if (r.getName() != null)
				unit.put("Name", String.valueOf(r.getName()));
			else
				unit.put("Name", "");
			if (r.getAddress() != null)
				unit.put("Address", String.valueOf(r.getAddress()));
			else
				unit.put("Address", "");
			unit.put("IsOpened", String.valueOf(r.getIsOpened()));

			dataStore.add(unit);
		}
		
		tableStore.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableStore.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("Store", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableStore.setItems(dataStore);
		allObjectTables.put("Store", tableStore);
		
		TableView<Map<String, String>> tableProductCatalog = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableProductCatalog_Id = new TableColumn<Map<String, String>, String>("Id");
		tableProductCatalog_Id.setMinWidth("Id".length()*10);
		tableProductCatalog_Id.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Id"));
		    }
		});	
		tableProductCatalog.getColumns().add(tableProductCatalog_Id);
		TableColumn<Map<String, String>, String> tableProductCatalog_Name = new TableColumn<Map<String, String>, String>("Name");
		tableProductCatalog_Name.setMinWidth("Name".length()*10);
		tableProductCatalog_Name.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Name"));
		    }
		});	
		tableProductCatalog.getColumns().add(tableProductCatalog_Name);
		
		//table data
		ObservableList<Map<String, String>> dataProductCatalog = FXCollections.observableArrayList();
		List<ProductCatalog> rsProductCatalog = EntityManager.getAllInstancesOf("ProductCatalog");
		for (ProductCatalog r : rsProductCatalog) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			unit.put("Id", String.valueOf(r.getId()));
			if (r.getName() != null)
				unit.put("Name", String.valueOf(r.getName()));
			else
				unit.put("Name", "");

			dataProductCatalog.add(unit);
		}
		
		tableProductCatalog.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableProductCatalog.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("ProductCatalog", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableProductCatalog.setItems(dataProductCatalog);
		allObjectTables.put("ProductCatalog", tableProductCatalog);
		
		TableView<Map<String, String>> tableCashDesk = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableCashDesk_Id = new TableColumn<Map<String, String>, String>("Id");
		tableCashDesk_Id.setMinWidth("Id".length()*10);
		tableCashDesk_Id.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Id"));
		    }
		});	
		tableCashDesk.getColumns().add(tableCashDesk_Id);
		TableColumn<Map<String, String>, String> tableCashDesk_Name = new TableColumn<Map<String, String>, String>("Name");
		tableCashDesk_Name.setMinWidth("Name".length()*10);
		tableCashDesk_Name.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Name"));
		    }
		});	
		tableCashDesk.getColumns().add(tableCashDesk_Name);
		TableColumn<Map<String, String>, String> tableCashDesk_IsOpened = new TableColumn<Map<String, String>, String>("IsOpened");
		tableCashDesk_IsOpened.setMinWidth("IsOpened".length()*10);
		tableCashDesk_IsOpened.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("IsOpened"));
		    }
		});	
		tableCashDesk.getColumns().add(tableCashDesk_IsOpened);
		
		//table data
		ObservableList<Map<String, String>> dataCashDesk = FXCollections.observableArrayList();
		List<CashDesk> rsCashDesk = EntityManager.getAllInstancesOf("CashDesk");
		for (CashDesk r : rsCashDesk) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			unit.put("Id", String.valueOf(r.getId()));
			if (r.getName() != null)
				unit.put("Name", String.valueOf(r.getName()));
			else
				unit.put("Name", "");
			unit.put("IsOpened", String.valueOf(r.getIsOpened()));

			dataCashDesk.add(unit);
		}
		
		tableCashDesk.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableCashDesk.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("CashDesk", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableCashDesk.setItems(dataCashDesk);
		allObjectTables.put("CashDesk", tableCashDesk);
		
		TableView<Map<String, String>> tableSale = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableSale_Time = new TableColumn<Map<String, String>, String>("Time");
		tableSale_Time.setMinWidth("Time".length()*10);
		tableSale_Time.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Time"));
		    }
		});	
		tableSale.getColumns().add(tableSale_Time);
		TableColumn<Map<String, String>, String> tableSale_IsComplete = new TableColumn<Map<String, String>, String>("IsComplete");
		tableSale_IsComplete.setMinWidth("IsComplete".length()*10);
		tableSale_IsComplete.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("IsComplete"));
		    }
		});	
		tableSale.getColumns().add(tableSale_IsComplete);
		TableColumn<Map<String, String>, String> tableSale_Amount = new TableColumn<Map<String, String>, String>("Amount");
		tableSale_Amount.setMinWidth("Amount".length()*10);
		tableSale_Amount.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Amount"));
		    }
		});	
		tableSale.getColumns().add(tableSale_Amount);
		TableColumn<Map<String, String>, String> tableSale_IsReadytoPay = new TableColumn<Map<String, String>, String>("IsReadytoPay");
		tableSale_IsReadytoPay.setMinWidth("IsReadytoPay".length()*10);
		tableSale_IsReadytoPay.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("IsReadytoPay"));
		    }
		});	
		tableSale.getColumns().add(tableSale_IsReadytoPay);
		TableColumn<Map<String, String>, String> tableSale_PaymentMethod = new TableColumn<Map<String, String>, String>("PaymentMethod");
		tableSale_PaymentMethod.setMinWidth("PaymentMethod".length()*10);
		tableSale_PaymentMethod.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("PaymentMethod"));
		    }
		});	
		tableSale.getColumns().add(tableSale_PaymentMethod);
		TableColumn<Map<String, String>, String> tableSale_IsPaid = new TableColumn<Map<String, String>, String>("IsPaid");
		tableSale_IsPaid.setMinWidth("IsPaid".length()*10);
		tableSale_IsPaid.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("IsPaid"));
		    }
		});	
		tableSale.getColumns().add(tableSale_IsPaid);
		TableColumn<Map<String, String>, String> tableSale_PaymentDetails = new TableColumn<Map<String, String>, String>("PaymentDetails");
		tableSale_PaymentDetails.setMinWidth("PaymentDetails".length()*10);
		tableSale_PaymentDetails.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("PaymentDetails"));
		    }
		});	
		tableSale.getColumns().add(tableSale_PaymentDetails);
		TableColumn<Map<String, String>, String> tableSale_OrderId = new TableColumn<Map<String, String>, String>("OrderId");
		tableSale_OrderId.setMinWidth("OrderId".length()*10);
		tableSale_OrderId.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("OrderId"));
		    }
		});	
		tableSale.getColumns().add(tableSale_OrderId);
		
		//table data
		ObservableList<Map<String, String>> dataSale = FXCollections.observableArrayList();
		List<Sale> rsSale = EntityManager.getAllInstancesOf("Sale");
		for (Sale r : rsSale) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			if (r.getTime() != null)
				unit.put("Time", r.getTime().format(dateformatter));
			else
				unit.put("Time", "");
			unit.put("IsComplete", String.valueOf(r.getIsComplete()));
			unit.put("Amount", String.valueOf(r.getAmount()));
			unit.put("IsReadytoPay", String.valueOf(r.getIsReadytoPay()));
			if (r.getPaymentMethod() != null)
				unit.put("PaymentMethod", String.valueOf(r.getPaymentMethod()));
			else
				unit.put("PaymentMethod", "");
			unit.put("IsPaid", String.valueOf(r.getIsPaid()));
			if (r.getPaymentDetails() != null)
				unit.put("PaymentDetails", String.valueOf(r.getPaymentDetails()));
			else
				unit.put("PaymentDetails", "");
			unit.put("OrderId", String.valueOf(r.getOrderId()));

			dataSale.add(unit);
		}
		
		tableSale.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableSale.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("Sale", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableSale.setItems(dataSale);
		allObjectTables.put("Sale", tableSale);
		
		TableView<Map<String, String>> tableCashier = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableCashier_Id = new TableColumn<Map<String, String>, String>("Id");
		tableCashier_Id.setMinWidth("Id".length()*10);
		tableCashier_Id.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Id"));
		    }
		});	
		tableCashier.getColumns().add(tableCashier_Id);
		TableColumn<Map<String, String>, String> tableCashier_Name = new TableColumn<Map<String, String>, String>("Name");
		tableCashier_Name.setMinWidth("Name".length()*10);
		tableCashier_Name.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Name"));
		    }
		});	
		tableCashier.getColumns().add(tableCashier_Name);
		
		//table data
		ObservableList<Map<String, String>> dataCashier = FXCollections.observableArrayList();
		List<Cashier> rsCashier = EntityManager.getAllInstancesOf("Cashier");
		for (Cashier r : rsCashier) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			unit.put("Id", String.valueOf(r.getId()));
			if (r.getName() != null)
				unit.put("Name", String.valueOf(r.getName()));
			else
				unit.put("Name", "");

			dataCashier.add(unit);
		}
		
		tableCashier.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableCashier.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("Cashier", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableCashier.setItems(dataCashier);
		allObjectTables.put("Cashier", tableCashier);
		
		TableView<Map<String, String>> tableSalesLineItem = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableSalesLineItem_Quantity = new TableColumn<Map<String, String>, String>("Quantity");
		tableSalesLineItem_Quantity.setMinWidth("Quantity".length()*10);
		tableSalesLineItem_Quantity.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Quantity"));
		    }
		});	
		tableSalesLineItem.getColumns().add(tableSalesLineItem_Quantity);
		TableColumn<Map<String, String>, String> tableSalesLineItem_Subamount = new TableColumn<Map<String, String>, String>("Subamount");
		tableSalesLineItem_Subamount.setMinWidth("Subamount".length()*10);
		tableSalesLineItem_Subamount.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Subamount"));
		    }
		});	
		tableSalesLineItem.getColumns().add(tableSalesLineItem_Subamount);
		TableColumn<Map<String, String>, String> tableSalesLineItem_BelongedCheckout = new TableColumn<Map<String, String>, String>("BelongedCheckout");
		tableSalesLineItem_BelongedCheckout.setMinWidth("BelongedCheckout".length()*10);
		tableSalesLineItem_BelongedCheckout.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("BelongedCheckout"));
		    }
		});	
		tableSalesLineItem.getColumns().add(tableSalesLineItem_BelongedCheckout);
		
		//table data
		ObservableList<Map<String, String>> dataSalesLineItem = FXCollections.observableArrayList();
		List<SalesLineItem> rsSalesLineItem = EntityManager.getAllInstancesOf("SalesLineItem");
		for (SalesLineItem r : rsSalesLineItem) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			unit.put("Quantity", String.valueOf(r.getQuantity()));
			unit.put("Subamount", String.valueOf(r.getSubamount()));
			unit.put("BelongedCheckout", String.valueOf(r.getBelongedCheckout()));

			dataSalesLineItem.add(unit);
		}
		
		tableSalesLineItem.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableSalesLineItem.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("SalesLineItem", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableSalesLineItem.setItems(dataSalesLineItem);
		allObjectTables.put("SalesLineItem", tableSalesLineItem);
		
		TableView<Map<String, String>> tableItem = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableItem_Barcode = new TableColumn<Map<String, String>, String>("Barcode");
		tableItem_Barcode.setMinWidth("Barcode".length()*10);
		tableItem_Barcode.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Barcode"));
		    }
		});	
		tableItem.getColumns().add(tableItem_Barcode);
		TableColumn<Map<String, String>, String> tableItem_Name = new TableColumn<Map<String, String>, String>("Name");
		tableItem_Name.setMinWidth("Name".length()*10);
		tableItem_Name.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Name"));
		    }
		});	
		tableItem.getColumns().add(tableItem_Name);
		TableColumn<Map<String, String>, String> tableItem_Price = new TableColumn<Map<String, String>, String>("Price");
		tableItem_Price.setMinWidth("Price".length()*10);
		tableItem_Price.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Price"));
		    }
		});	
		tableItem.getColumns().add(tableItem_Price);
		TableColumn<Map<String, String>, String> tableItem_StockNumber = new TableColumn<Map<String, String>, String>("StockNumber");
		tableItem_StockNumber.setMinWidth("StockNumber".length()*10);
		tableItem_StockNumber.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("StockNumber"));
		    }
		});	
		tableItem.getColumns().add(tableItem_StockNumber);
		TableColumn<Map<String, String>, String> tableItem_OrderPrice = new TableColumn<Map<String, String>, String>("OrderPrice");
		tableItem_OrderPrice.setMinWidth("OrderPrice".length()*10);
		tableItem_OrderPrice.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("OrderPrice"));
		    }
		});	
		tableItem.getColumns().add(tableItem_OrderPrice);
		TableColumn<Map<String, String>, String> tableItem_ItemID = new TableColumn<Map<String, String>, String>("ItemID");
		tableItem_ItemID.setMinWidth("ItemID".length()*10);
		tableItem_ItemID.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("ItemID"));
		    }
		});	
		tableItem.getColumns().add(tableItem_ItemID);
		
		//table data
		ObservableList<Map<String, String>> dataItem = FXCollections.observableArrayList();
		List<Item> rsItem = EntityManager.getAllInstancesOf("Item");
		for (Item r : rsItem) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			unit.put("Barcode", String.valueOf(r.getBarcode()));
			if (r.getName() != null)
				unit.put("Name", String.valueOf(r.getName()));
			else
				unit.put("Name", "");
			unit.put("Price", String.valueOf(r.getPrice()));
			unit.put("StockNumber", String.valueOf(r.getStockNumber()));
			unit.put("OrderPrice", String.valueOf(r.getOrderPrice()));
			unit.put("ItemID", String.valueOf(r.getItemID()));

			dataItem.add(unit);
		}
		
		tableItem.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableItem.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("Item", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableItem.setItems(dataItem);
		allObjectTables.put("Item", tableItem);
		
		TableView<Map<String, String>> tablePayment = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tablePayment_AmountTendered = new TableColumn<Map<String, String>, String>("AmountTendered");
		tablePayment_AmountTendered.setMinWidth("AmountTendered".length()*10);
		tablePayment_AmountTendered.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("AmountTendered"));
		    }
		});	
		tablePayment.getColumns().add(tablePayment_AmountTendered);
		
		//table data
		ObservableList<Map<String, String>> dataPayment = FXCollections.observableArrayList();
		List<Payment> rsPayment = EntityManager.getAllInstancesOf("Payment");
		for (Payment r : rsPayment) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			unit.put("AmountTendered", String.valueOf(r.getAmountTendered()));

			dataPayment.add(unit);
		}
		
		tablePayment.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tablePayment.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("Payment", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tablePayment.setItems(dataPayment);
		allObjectTables.put("Payment", tablePayment);
		
		TableView<Map<String, String>> tableCashPayment = new TableView<Map<String, String>>();

		//super entity attribute column
		TableColumn<Map<String, String>, String> tableCashPayment_AmountTendered = new TableColumn<Map<String, String>, String>("AmountTendered");
		tableCashPayment_AmountTendered.setMinWidth("AmountTendered".length()*10);
		tableCashPayment_AmountTendered.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("AmountTendered"));
		    }
		});	
		tableCashPayment.getColumns().add(tableCashPayment_AmountTendered);
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableCashPayment_Balance = new TableColumn<Map<String, String>, String>("Balance");
		tableCashPayment_Balance.setMinWidth("Balance".length()*10);
		tableCashPayment_Balance.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Balance"));
		    }
		});	
		tableCashPayment.getColumns().add(tableCashPayment_Balance);
		
		//table data
		ObservableList<Map<String, String>> dataCashPayment = FXCollections.observableArrayList();
		List<CashPayment> rsCashPayment = EntityManager.getAllInstancesOf("CashPayment");
		for (CashPayment r : rsCashPayment) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			unit.put("AmountTendered", String.valueOf(r.getAmountTendered()));
			
			unit.put("Balance", String.valueOf(r.getBalance()));

			dataCashPayment.add(unit);
		}
		
		tableCashPayment.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableCashPayment.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("CashPayment", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableCashPayment.setItems(dataCashPayment);
		allObjectTables.put("CashPayment", tableCashPayment);
		
		TableView<Map<String, String>> tableCardPayment = new TableView<Map<String, String>>();

		//super entity attribute column
		TableColumn<Map<String, String>, String> tableCardPayment_AmountTendered = new TableColumn<Map<String, String>, String>("AmountTendered");
		tableCardPayment_AmountTendered.setMinWidth("AmountTendered".length()*10);
		tableCardPayment_AmountTendered.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("AmountTendered"));
		    }
		});	
		tableCardPayment.getColumns().add(tableCardPayment_AmountTendered);
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableCardPayment_CardAccountNumber = new TableColumn<Map<String, String>, String>("CardAccountNumber");
		tableCardPayment_CardAccountNumber.setMinWidth("CardAccountNumber".length()*10);
		tableCardPayment_CardAccountNumber.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("CardAccountNumber"));
		    }
		});	
		tableCardPayment.getColumns().add(tableCardPayment_CardAccountNumber);
		TableColumn<Map<String, String>, String> tableCardPayment_ExpiryDate = new TableColumn<Map<String, String>, String>("ExpiryDate");
		tableCardPayment_ExpiryDate.setMinWidth("ExpiryDate".length()*10);
		tableCardPayment_ExpiryDate.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("ExpiryDate"));
		    }
		});	
		tableCardPayment.getColumns().add(tableCardPayment_ExpiryDate);
		
		//table data
		ObservableList<Map<String, String>> dataCardPayment = FXCollections.observableArrayList();
		List<CardPayment> rsCardPayment = EntityManager.getAllInstancesOf("CardPayment");
		for (CardPayment r : rsCardPayment) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			unit.put("AmountTendered", String.valueOf(r.getAmountTendered()));
			
			if (r.getCardAccountNumber() != null)
				unit.put("CardAccountNumber", String.valueOf(r.getCardAccountNumber()));
			else
				unit.put("CardAccountNumber", "");
			if (r.getExpiryDate() != null)
				unit.put("ExpiryDate", r.getExpiryDate().format(dateformatter));
			else
				unit.put("ExpiryDate", "");

			dataCardPayment.add(unit);
		}
		
		tableCardPayment.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableCardPayment.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("CardPayment", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableCardPayment.setItems(dataCardPayment);
		allObjectTables.put("CardPayment", tableCardPayment);
		
		TableView<Map<String, String>> tableWechatPayment = new TableView<Map<String, String>>();

		//super entity attribute column
		TableColumn<Map<String, String>, String> tableWechatPayment_AmountTendered = new TableColumn<Map<String, String>, String>("AmountTendered");
		tableWechatPayment_AmountTendered.setMinWidth("AmountTendered".length()*10);
		tableWechatPayment_AmountTendered.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("AmountTendered"));
		    }
		});	
		tableWechatPayment.getColumns().add(tableWechatPayment_AmountTendered);
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableWechatPayment_OpenID = new TableColumn<Map<String, String>, String>("OpenID");
		tableWechatPayment_OpenID.setMinWidth("OpenID".length()*10);
		tableWechatPayment_OpenID.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("OpenID"));
		    }
		});	
		tableWechatPayment.getColumns().add(tableWechatPayment_OpenID);
		
		//table data
		ObservableList<Map<String, String>> dataWechatPayment = FXCollections.observableArrayList();
		List<WechatPayment> rsWechatPayment = EntityManager.getAllInstancesOf("WechatPayment");
		for (WechatPayment r : rsWechatPayment) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			unit.put("AmountTendered", String.valueOf(r.getAmountTendered()));
			
			if (r.getOpenID() != null)
				unit.put("OpenID", String.valueOf(r.getOpenID()));
			else
				unit.put("OpenID", "");

			dataWechatPayment.add(unit);
		}
		
		tableWechatPayment.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableWechatPayment.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("WechatPayment", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableWechatPayment.setItems(dataWechatPayment);
		allObjectTables.put("WechatPayment", tableWechatPayment);
		
		TableView<Map<String, String>> tableAliPayment = new TableView<Map<String, String>>();

		//super entity attribute column
		TableColumn<Map<String, String>, String> tableAliPayment_AmountTendered = new TableColumn<Map<String, String>, String>("AmountTendered");
		tableAliPayment_AmountTendered.setMinWidth("AmountTendered".length()*10);
		tableAliPayment_AmountTendered.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("AmountTendered"));
		    }
		});	
		tableAliPayment.getColumns().add(tableAliPayment_AmountTendered);
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableAliPayment_AlipayAccount = new TableColumn<Map<String, String>, String>("AlipayAccount");
		tableAliPayment_AlipayAccount.setMinWidth("AlipayAccount".length()*10);
		tableAliPayment_AlipayAccount.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("AlipayAccount"));
		    }
		});	
		tableAliPayment.getColumns().add(tableAliPayment_AlipayAccount);
		
		//table data
		ObservableList<Map<String, String>> dataAliPayment = FXCollections.observableArrayList();
		List<AliPayment> rsAliPayment = EntityManager.getAllInstancesOf("AliPayment");
		for (AliPayment r : rsAliPayment) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			unit.put("AmountTendered", String.valueOf(r.getAmountTendered()));
			
			if (r.getAlipayAccount() != null)
				unit.put("AlipayAccount", String.valueOf(r.getAlipayAccount()));
			else
				unit.put("AlipayAccount", "");

			dataAliPayment.add(unit);
		}
		
		tableAliPayment.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableAliPayment.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("AliPayment", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableAliPayment.setItems(dataAliPayment);
		allObjectTables.put("AliPayment", tableAliPayment);
		
		TableView<Map<String, String>> tableOrderEntry = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableOrderEntry_Quantity = new TableColumn<Map<String, String>, String>("Quantity");
		tableOrderEntry_Quantity.setMinWidth("Quantity".length()*10);
		tableOrderEntry_Quantity.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Quantity"));
		    }
		});	
		tableOrderEntry.getColumns().add(tableOrderEntry_Quantity);
		TableColumn<Map<String, String>, String> tableOrderEntry_SubAmount = new TableColumn<Map<String, String>, String>("SubAmount");
		tableOrderEntry_SubAmount.setMinWidth("SubAmount".length()*10);
		tableOrderEntry_SubAmount.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("SubAmount"));
		    }
		});	
		tableOrderEntry.getColumns().add(tableOrderEntry_SubAmount);
		
		//table data
		ObservableList<Map<String, String>> dataOrderEntry = FXCollections.observableArrayList();
		List<OrderEntry> rsOrderEntry = EntityManager.getAllInstancesOf("OrderEntry");
		for (OrderEntry r : rsOrderEntry) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			unit.put("Quantity", String.valueOf(r.getQuantity()));
			unit.put("SubAmount", String.valueOf(r.getSubAmount()));

			dataOrderEntry.add(unit);
		}
		
		tableOrderEntry.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableOrderEntry.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("OrderEntry", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableOrderEntry.setItems(dataOrderEntry);
		allObjectTables.put("OrderEntry", tableOrderEntry);
		
		TableView<Map<String, String>> tableSupplier = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableSupplier_Id = new TableColumn<Map<String, String>, String>("Id");
		tableSupplier_Id.setMinWidth("Id".length()*10);
		tableSupplier_Id.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Id"));
		    }
		});	
		tableSupplier.getColumns().add(tableSupplier_Id);
		TableColumn<Map<String, String>, String> tableSupplier_Name = new TableColumn<Map<String, String>, String>("Name");
		tableSupplier_Name.setMinWidth("Name".length()*10);
		tableSupplier_Name.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Name"));
		    }
		});	
		tableSupplier.getColumns().add(tableSupplier_Name);
		
		//table data
		ObservableList<Map<String, String>> dataSupplier = FXCollections.observableArrayList();
		List<Supplier> rsSupplier = EntityManager.getAllInstancesOf("Supplier");
		for (Supplier r : rsSupplier) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			unit.put("Id", String.valueOf(r.getId()));
			if (r.getName() != null)
				unit.put("Name", String.valueOf(r.getName()));
			else
				unit.put("Name", "");

			dataSupplier.add(unit);
		}
		
		tableSupplier.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableSupplier.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("Supplier", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableSupplier.setItems(dataSupplier);
		allObjectTables.put("Supplier", tableSupplier);
		
		TableView<Map<String, String>> tableOrderProduct = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableOrderProduct_Id = new TableColumn<Map<String, String>, String>("Id");
		tableOrderProduct_Id.setMinWidth("Id".length()*10);
		tableOrderProduct_Id.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Id"));
		    }
		});	
		tableOrderProduct.getColumns().add(tableOrderProduct_Id);
		TableColumn<Map<String, String>, String> tableOrderProduct_Time = new TableColumn<Map<String, String>, String>("Time");
		tableOrderProduct_Time.setMinWidth("Time".length()*10);
		tableOrderProduct_Time.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Time"));
		    }
		});	
		tableOrderProduct.getColumns().add(tableOrderProduct_Time);
		TableColumn<Map<String, String>, String> tableOrderProduct_OrderStatus = new TableColumn<Map<String, String>, String>("OrderStatus");
		tableOrderProduct_OrderStatus.setMinWidth("OrderStatus".length()*10);
		tableOrderProduct_OrderStatus.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("OrderStatus"));
		    }
		});	
		tableOrderProduct.getColumns().add(tableOrderProduct_OrderStatus);
		TableColumn<Map<String, String>, String> tableOrderProduct_Amount = new TableColumn<Map<String, String>, String>("Amount");
		tableOrderProduct_Amount.setMinWidth("Amount".length()*10);
		tableOrderProduct_Amount.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Amount"));
		    }
		});	
		tableOrderProduct.getColumns().add(tableOrderProduct_Amount);
		
		//table data
		ObservableList<Map<String, String>> dataOrderProduct = FXCollections.observableArrayList();
		List<OrderProduct> rsOrderProduct = EntityManager.getAllInstancesOf("OrderProduct");
		for (OrderProduct r : rsOrderProduct) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			unit.put("Id", String.valueOf(r.getId()));
			if (r.getTime() != null)
				unit.put("Time", r.getTime().format(dateformatter));
			else
				unit.put("Time", "");
			unit.put("OrderStatus", String.valueOf(r.getOrderStatus()));
			unit.put("Amount", String.valueOf(r.getAmount()));

			dataOrderProduct.add(unit);
		}
		
		tableOrderProduct.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableOrderProduct.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("OrderProduct", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableOrderProduct.setItems(dataOrderProduct);
		allObjectTables.put("OrderProduct", tableOrderProduct);
		
		TableView<Map<String, String>> tableCustomer = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableCustomer_Id = new TableColumn<Map<String, String>, String>("Id");
		tableCustomer_Id.setMinWidth("Id".length()*10);
		tableCustomer_Id.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Id"));
		    }
		});	
		tableCustomer.getColumns().add(tableCustomer_Id);
		TableColumn<Map<String, String>, String> tableCustomer_Name = new TableColumn<Map<String, String>, String>("Name");
		tableCustomer_Name.setMinWidth("Name".length()*10);
		tableCustomer_Name.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Name"));
		    }
		});	
		tableCustomer.getColumns().add(tableCustomer_Name);
		TableColumn<Map<String, String>, String> tableCustomer_ContactInfo = new TableColumn<Map<String, String>, String>("ContactInfo");
		tableCustomer_ContactInfo.setMinWidth("ContactInfo".length()*10);
		tableCustomer_ContactInfo.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("ContactInfo"));
		    }
		});	
		tableCustomer.getColumns().add(tableCustomer_ContactInfo);
		TableColumn<Map<String, String>, String> tableCustomer_MembershipLevel = new TableColumn<Map<String, String>, String>("MembershipLevel");
		tableCustomer_MembershipLevel.setMinWidth("MembershipLevel".length()*10);
		tableCustomer_MembershipLevel.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("MembershipLevel"));
		    }
		});	
		tableCustomer.getColumns().add(tableCustomer_MembershipLevel);
		
		//table data
		ObservableList<Map<String, String>> dataCustomer = FXCollections.observableArrayList();
		List<Customer> rsCustomer = EntityManager.getAllInstancesOf("Customer");
		for (Customer r : rsCustomer) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			unit.put("Id", String.valueOf(r.getId()));
			if (r.getName() != null)
				unit.put("Name", String.valueOf(r.getName()));
			else
				unit.put("Name", "");
			if (r.getContactInfo() != null)
				unit.put("ContactInfo", String.valueOf(r.getContactInfo()));
			else
				unit.put("ContactInfo", "");
			if (r.getMembershipLevel() != null)
				unit.put("MembershipLevel", String.valueOf(r.getMembershipLevel()));
			else
				unit.put("MembershipLevel", "");

			dataCustomer.add(unit);
		}
		
		tableCustomer.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableCustomer.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("Customer", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableCustomer.setItems(dataCustomer);
		allObjectTables.put("Customer", tableCustomer);
		
		TableView<Map<String, String>> tableReturnRequest = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableReturnRequest_RequestId = new TableColumn<Map<String, String>, String>("RequestId");
		tableReturnRequest_RequestId.setMinWidth("RequestId".length()*10);
		tableReturnRequest_RequestId.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("RequestId"));
		    }
		});	
		tableReturnRequest.getColumns().add(tableReturnRequest_RequestId);
		TableColumn<Map<String, String>, String> tableReturnRequest_OrderId = new TableColumn<Map<String, String>, String>("OrderId");
		tableReturnRequest_OrderId.setMinWidth("OrderId".length()*10);
		tableReturnRequest_OrderId.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("OrderId"));
		    }
		});	
		tableReturnRequest.getColumns().add(tableReturnRequest_OrderId);
		TableColumn<Map<String, String>, String> tableReturnRequest_ItemId = new TableColumn<Map<String, String>, String>("ItemId");
		tableReturnRequest_ItemId.setMinWidth("ItemId".length()*10);
		tableReturnRequest_ItemId.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("ItemId"));
		    }
		});	
		tableReturnRequest.getColumns().add(tableReturnRequest_ItemId);
		TableColumn<Map<String, String>, String> tableReturnRequest_ReturnTime = new TableColumn<Map<String, String>, String>("ReturnTime");
		tableReturnRequest_ReturnTime.setMinWidth("ReturnTime".length()*10);
		tableReturnRequest_ReturnTime.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("ReturnTime"));
		    }
		});	
		tableReturnRequest.getColumns().add(tableReturnRequest_ReturnTime);
		TableColumn<Map<String, String>, String> tableReturnRequest_ReturnReason = new TableColumn<Map<String, String>, String>("ReturnReason");
		tableReturnRequest_ReturnReason.setMinWidth("ReturnReason".length()*10);
		tableReturnRequest_ReturnReason.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("ReturnReason"));
		    }
		});	
		tableReturnRequest.getColumns().add(tableReturnRequest_ReturnReason);
		TableColumn<Map<String, String>, String> tableReturnRequest_IsApproved = new TableColumn<Map<String, String>, String>("IsApproved");
		tableReturnRequest_IsApproved.setMinWidth("IsApproved".length()*10);
		tableReturnRequest_IsApproved.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("IsApproved"));
		    }
		});	
		tableReturnRequest.getColumns().add(tableReturnRequest_IsApproved);
		TableColumn<Map<String, String>, String> tableReturnRequest_IsReviewed = new TableColumn<Map<String, String>, String>("IsReviewed");
		tableReturnRequest_IsReviewed.setMinWidth("IsReviewed".length()*10);
		tableReturnRequest_IsReviewed.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("IsReviewed"));
		    }
		});	
		tableReturnRequest.getColumns().add(tableReturnRequest_IsReviewed);
		
		//table data
		ObservableList<Map<String, String>> dataReturnRequest = FXCollections.observableArrayList();
		List<ReturnRequest> rsReturnRequest = EntityManager.getAllInstancesOf("ReturnRequest");
		for (ReturnRequest r : rsReturnRequest) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			unit.put("RequestId", String.valueOf(r.getRequestId()));
			unit.put("OrderId", String.valueOf(r.getOrderId()));
			unit.put("ItemId", String.valueOf(r.getItemId()));
			if (r.getReturnTime() != null)
				unit.put("ReturnTime", r.getReturnTime().format(dateformatter));
			else
				unit.put("ReturnTime", "");
			if (r.getReturnReason() != null)
				unit.put("ReturnReason", String.valueOf(r.getReturnReason()));
			else
				unit.put("ReturnReason", "");
			unit.put("IsApproved", String.valueOf(r.getIsApproved()));
			unit.put("IsReviewed", String.valueOf(r.getIsReviewed()));

			dataReturnRequest.add(unit);
		}
		
		tableReturnRequest.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableReturnRequest.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("ReturnRequest", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableReturnRequest.setItems(dataReturnRequest);
		allObjectTables.put("ReturnRequest", tableReturnRequest);
		
		TableView<Map<String, String>> tableSurveillanceDevice = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableSurveillanceDevice_DeviceId = new TableColumn<Map<String, String>, String>("DeviceId");
		tableSurveillanceDevice_DeviceId.setMinWidth("DeviceId".length()*10);
		tableSurveillanceDevice_DeviceId.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("DeviceId"));
		    }
		});	
		tableSurveillanceDevice.getColumns().add(tableSurveillanceDevice_DeviceId);
		TableColumn<Map<String, String>, String> tableSurveillanceDevice_Location = new TableColumn<Map<String, String>, String>("Location");
		tableSurveillanceDevice_Location.setMinWidth("Location".length()*10);
		tableSurveillanceDevice_Location.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Location"));
		    }
		});	
		tableSurveillanceDevice.getColumns().add(tableSurveillanceDevice_Location);
		TableColumn<Map<String, String>, String> tableSurveillanceDevice_Status = new TableColumn<Map<String, String>, String>("Status");
		tableSurveillanceDevice_Status.setMinWidth("Status".length()*10);
		tableSurveillanceDevice_Status.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Status"));
		    }
		});	
		tableSurveillanceDevice.getColumns().add(tableSurveillanceDevice_Status);
		TableColumn<Map<String, String>, String> tableSurveillanceDevice_LastMaintenanceTime = new TableColumn<Map<String, String>, String>("LastMaintenanceTime");
		tableSurveillanceDevice_LastMaintenanceTime.setMinWidth("LastMaintenanceTime".length()*10);
		tableSurveillanceDevice_LastMaintenanceTime.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("LastMaintenanceTime"));
		    }
		});	
		tableSurveillanceDevice.getColumns().add(tableSurveillanceDevice_LastMaintenanceTime);
		
		//table data
		ObservableList<Map<String, String>> dataSurveillanceDevice = FXCollections.observableArrayList();
		List<SurveillanceDevice> rsSurveillanceDevice = EntityManager.getAllInstancesOf("SurveillanceDevice");
		for (SurveillanceDevice r : rsSurveillanceDevice) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			unit.put("DeviceId", String.valueOf(r.getDeviceId()));
			if (r.getLocation() != null)
				unit.put("Location", String.valueOf(r.getLocation()));
			else
				unit.put("Location", "");
			if (r.getStatus() != null)
				unit.put("Status", String.valueOf(r.getStatus()));
			else
				unit.put("Status", "");
			if (r.getLastMaintenanceTime() != null)
				unit.put("LastMaintenanceTime", r.getLastMaintenanceTime().format(dateformatter));
			else
				unit.put("LastMaintenanceTime", "");

			dataSurveillanceDevice.add(unit);
		}
		
		tableSurveillanceDevice.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableSurveillanceDevice.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("SurveillanceDevice", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableSurveillanceDevice.setItems(dataSurveillanceDevice);
		allObjectTables.put("SurveillanceDevice", tableSurveillanceDevice);
		
		TableView<Map<String, String>> tableSalesDataAnalysisResult = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableSalesDataAnalysisResult_AnalysisId = new TableColumn<Map<String, String>, String>("AnalysisId");
		tableSalesDataAnalysisResult_AnalysisId.setMinWidth("AnalysisId".length()*10);
		tableSalesDataAnalysisResult_AnalysisId.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("AnalysisId"));
		    }
		});	
		tableSalesDataAnalysisResult.getColumns().add(tableSalesDataAnalysisResult_AnalysisId);
		TableColumn<Map<String, String>, String> tableSalesDataAnalysisResult_AnalysisTime = new TableColumn<Map<String, String>, String>("AnalysisTime");
		tableSalesDataAnalysisResult_AnalysisTime.setMinWidth("AnalysisTime".length()*10);
		tableSalesDataAnalysisResult_AnalysisTime.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("AnalysisTime"));
		    }
		});	
		tableSalesDataAnalysisResult.getColumns().add(tableSalesDataAnalysisResult_AnalysisTime);
		TableColumn<Map<String, String>, String> tableSalesDataAnalysisResult_TotalSalesAmount = new TableColumn<Map<String, String>, String>("TotalSalesAmount");
		tableSalesDataAnalysisResult_TotalSalesAmount.setMinWidth("TotalSalesAmount".length()*10);
		tableSalesDataAnalysisResult_TotalSalesAmount.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("TotalSalesAmount"));
		    }
		});	
		tableSalesDataAnalysisResult.getColumns().add(tableSalesDataAnalysisResult_TotalSalesAmount);
		TableColumn<Map<String, String>, String> tableSalesDataAnalysisResult_BestSellingProducts = new TableColumn<Map<String, String>, String>("BestSellingProducts");
		tableSalesDataAnalysisResult_BestSellingProducts.setMinWidth("BestSellingProducts".length()*10);
		tableSalesDataAnalysisResult_BestSellingProducts.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("BestSellingProducts"));
		    }
		});	
		tableSalesDataAnalysisResult.getColumns().add(tableSalesDataAnalysisResult_BestSellingProducts);
		TableColumn<Map<String, String>, String> tableSalesDataAnalysisResult_SalesTrend = new TableColumn<Map<String, String>, String>("SalesTrend");
		tableSalesDataAnalysisResult_SalesTrend.setMinWidth("SalesTrend".length()*10);
		tableSalesDataAnalysisResult_SalesTrend.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("SalesTrend"));
		    }
		});	
		tableSalesDataAnalysisResult.getColumns().add(tableSalesDataAnalysisResult_SalesTrend);
		
		//table data
		ObservableList<Map<String, String>> dataSalesDataAnalysisResult = FXCollections.observableArrayList();
		List<SalesDataAnalysisResult> rsSalesDataAnalysisResult = EntityManager.getAllInstancesOf("SalesDataAnalysisResult");
		for (SalesDataAnalysisResult r : rsSalesDataAnalysisResult) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			unit.put("AnalysisId", String.valueOf(r.getAnalysisId()));
			if (r.getAnalysisTime() != null)
				unit.put("AnalysisTime", r.getAnalysisTime().format(dateformatter));
			else
				unit.put("AnalysisTime", "");
			unit.put("TotalSalesAmount", String.valueOf(r.getTotalSalesAmount()));
			if (r.getBestSellingProducts() != null)
				unit.put("BestSellingProducts", String.valueOf(r.getBestSellingProducts()));
			else
				unit.put("BestSellingProducts", "");
			if (r.getSalesTrend() != null)
				unit.put("SalesTrend", String.valueOf(r.getSalesTrend()));
			else
				unit.put("SalesTrend", "");

			dataSalesDataAnalysisResult.add(unit);
		}
		
		tableSalesDataAnalysisResult.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableSalesDataAnalysisResult.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("SalesDataAnalysisResult", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableSalesDataAnalysisResult.setItems(dataSalesDataAnalysisResult);
		allObjectTables.put("SalesDataAnalysisResult", tableSalesDataAnalysisResult);
		
		TableView<Map<String, String>> tablePurchaseOrder = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tablePurchaseOrder_OrderId = new TableColumn<Map<String, String>, String>("OrderId");
		tablePurchaseOrder_OrderId.setMinWidth("OrderId".length()*10);
		tablePurchaseOrder_OrderId.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("OrderId"));
		    }
		});	
		tablePurchaseOrder.getColumns().add(tablePurchaseOrder_OrderId);
		TableColumn<Map<String, String>, String> tablePurchaseOrder_SupplierId = new TableColumn<Map<String, String>, String>("SupplierId");
		tablePurchaseOrder_SupplierId.setMinWidth("SupplierId".length()*10);
		tablePurchaseOrder_SupplierId.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("SupplierId"));
		    }
		});	
		tablePurchaseOrder.getColumns().add(tablePurchaseOrder_SupplierId);
		TableColumn<Map<String, String>, String> tablePurchaseOrder_StoreId = new TableColumn<Map<String, String>, String>("StoreId");
		tablePurchaseOrder_StoreId.setMinWidth("StoreId".length()*10);
		tablePurchaseOrder_StoreId.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("StoreId"));
		    }
		});	
		tablePurchaseOrder.getColumns().add(tablePurchaseOrder_StoreId);
		TableColumn<Map<String, String>, String> tablePurchaseOrder_OrderTime = new TableColumn<Map<String, String>, String>("OrderTime");
		tablePurchaseOrder_OrderTime.setMinWidth("OrderTime".length()*10);
		tablePurchaseOrder_OrderTime.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("OrderTime"));
		    }
		});	
		tablePurchaseOrder.getColumns().add(tablePurchaseOrder_OrderTime);
		TableColumn<Map<String, String>, String> tablePurchaseOrder_ExpectedDeliveryTime = new TableColumn<Map<String, String>, String>("ExpectedDeliveryTime");
		tablePurchaseOrder_ExpectedDeliveryTime.setMinWidth("ExpectedDeliveryTime".length()*10);
		tablePurchaseOrder_ExpectedDeliveryTime.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("ExpectedDeliveryTime"));
		    }
		});	
		tablePurchaseOrder.getColumns().add(tablePurchaseOrder_ExpectedDeliveryTime);
		TableColumn<Map<String, String>, String> tablePurchaseOrder_TotalAmount = new TableColumn<Map<String, String>, String>("TotalAmount");
		tablePurchaseOrder_TotalAmount.setMinWidth("TotalAmount".length()*10);
		tablePurchaseOrder_TotalAmount.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("TotalAmount"));
		    }
		});	
		tablePurchaseOrder.getColumns().add(tablePurchaseOrder_TotalAmount);
		TableColumn<Map<String, String>, String> tablePurchaseOrder_IsReceived = new TableColumn<Map<String, String>, String>("IsReceived");
		tablePurchaseOrder_IsReceived.setMinWidth("IsReceived".length()*10);
		tablePurchaseOrder_IsReceived.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("IsReceived"));
		    }
		});	
		tablePurchaseOrder.getColumns().add(tablePurchaseOrder_IsReceived);
		TableColumn<Map<String, String>, String> tablePurchaseOrder_IsAccepted = new TableColumn<Map<String, String>, String>("IsAccepted");
		tablePurchaseOrder_IsAccepted.setMinWidth("IsAccepted".length()*10);
		tablePurchaseOrder_IsAccepted.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("IsAccepted"));
		    }
		});	
		tablePurchaseOrder.getColumns().add(tablePurchaseOrder_IsAccepted);
		
		//table data
		ObservableList<Map<String, String>> dataPurchaseOrder = FXCollections.observableArrayList();
		List<PurchaseOrder> rsPurchaseOrder = EntityManager.getAllInstancesOf("PurchaseOrder");
		for (PurchaseOrder r : rsPurchaseOrder) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			unit.put("OrderId", String.valueOf(r.getOrderId()));
			unit.put("SupplierId", String.valueOf(r.getSupplierId()));
			unit.put("StoreId", String.valueOf(r.getStoreId()));
			if (r.getOrderTime() != null)
				unit.put("OrderTime", r.getOrderTime().format(dateformatter));
			else
				unit.put("OrderTime", "");
			if (r.getExpectedDeliveryTime() != null)
				unit.put("ExpectedDeliveryTime", r.getExpectedDeliveryTime().format(dateformatter));
			else
				unit.put("ExpectedDeliveryTime", "");
			unit.put("TotalAmount", String.valueOf(r.getTotalAmount()));
			unit.put("IsReceived", String.valueOf(r.getIsReceived()));
			unit.put("IsAccepted", String.valueOf(r.getIsAccepted()));

			dataPurchaseOrder.add(unit);
		}
		
		tablePurchaseOrder.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tablePurchaseOrder.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("PurchaseOrder", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tablePurchaseOrder.setItems(dataPurchaseOrder);
		allObjectTables.put("PurchaseOrder", tablePurchaseOrder);
		
		TableView<Map<String, String>> tableRecommendedProduct = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableRecommendedProduct_RecommendationId = new TableColumn<Map<String, String>, String>("RecommendationId");
		tableRecommendedProduct_RecommendationId.setMinWidth("RecommendationId".length()*10);
		tableRecommendedProduct_RecommendationId.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("RecommendationId"));
		    }
		});	
		tableRecommendedProduct.getColumns().add(tableRecommendedProduct_RecommendationId);
		TableColumn<Map<String, String>, String> tableRecommendedProduct_ProductId = new TableColumn<Map<String, String>, String>("ProductId");
		tableRecommendedProduct_ProductId.setMinWidth("ProductId".length()*10);
		tableRecommendedProduct_ProductId.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("ProductId"));
		    }
		});	
		tableRecommendedProduct.getColumns().add(tableRecommendedProduct_ProductId);
		TableColumn<Map<String, String>, String> tableRecommendedProduct_SupplierId = new TableColumn<Map<String, String>, String>("SupplierId");
		tableRecommendedProduct_SupplierId.setMinWidth("SupplierId".length()*10);
		tableRecommendedProduct_SupplierId.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("SupplierId"));
		    }
		});	
		tableRecommendedProduct.getColumns().add(tableRecommendedProduct_SupplierId);
		TableColumn<Map<String, String>, String> tableRecommendedProduct_RecommendationTime = new TableColumn<Map<String, String>, String>("RecommendationTime");
		tableRecommendedProduct_RecommendationTime.setMinWidth("RecommendationTime".length()*10);
		tableRecommendedProduct_RecommendationTime.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("RecommendationTime"));
		    }
		});	
		tableRecommendedProduct.getColumns().add(tableRecommendedProduct_RecommendationTime);
		TableColumn<Map<String, String>, String> tableRecommendedProduct_RecommendationReason = new TableColumn<Map<String, String>, String>("RecommendationReason");
		tableRecommendedProduct_RecommendationReason.setMinWidth("RecommendationReason".length()*10);
		tableRecommendedProduct_RecommendationReason.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("RecommendationReason"));
		    }
		});	
		tableRecommendedProduct.getColumns().add(tableRecommendedProduct_RecommendationReason);
		
		//table data
		ObservableList<Map<String, String>> dataRecommendedProduct = FXCollections.observableArrayList();
		List<RecommendedProduct> rsRecommendedProduct = EntityManager.getAllInstancesOf("RecommendedProduct");
		for (RecommendedProduct r : rsRecommendedProduct) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			unit.put("RecommendationId", String.valueOf(r.getRecommendationId()));
			unit.put("ProductId", String.valueOf(r.getProductId()));
			unit.put("SupplierId", String.valueOf(r.getSupplierId()));
			if (r.getRecommendationTime() != null)
				unit.put("RecommendationTime", r.getRecommendationTime().format(dateformatter));
			else
				unit.put("RecommendationTime", "");
			if (r.getRecommendationReason() != null)
				unit.put("RecommendationReason", String.valueOf(r.getRecommendationReason()));
			else
				unit.put("RecommendationReason", "");

			dataRecommendedProduct.add(unit);
		}
		
		tableRecommendedProduct.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableRecommendedProduct.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("RecommendedProduct", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableRecommendedProduct.setItems(dataRecommendedProduct);
		allObjectTables.put("RecommendedProduct", tableRecommendedProduct);
		
		TableView<Map<String, String>> tableAdministrator = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableAdministrator_Id = new TableColumn<Map<String, String>, String>("Id");
		tableAdministrator_Id.setMinWidth("Id".length()*10);
		tableAdministrator_Id.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Id"));
		    }
		});	
		tableAdministrator.getColumns().add(tableAdministrator_Id);
		TableColumn<Map<String, String>, String> tableAdministrator_Name = new TableColumn<Map<String, String>, String>("Name");
		tableAdministrator_Name.setMinWidth("Name".length()*10);
		tableAdministrator_Name.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Name"));
		    }
		});	
		tableAdministrator.getColumns().add(tableAdministrator_Name);
		TableColumn<Map<String, String>, String> tableAdministrator_Username = new TableColumn<Map<String, String>, String>("Username");
		tableAdministrator_Username.setMinWidth("Username".length()*10);
		tableAdministrator_Username.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Username"));
		    }
		});	
		tableAdministrator.getColumns().add(tableAdministrator_Username);
		TableColumn<Map<String, String>, String> tableAdministrator_Password = new TableColumn<Map<String, String>, String>("Password");
		tableAdministrator_Password.setMinWidth("Password".length()*10);
		tableAdministrator_Password.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Password"));
		    }
		});	
		tableAdministrator.getColumns().add(tableAdministrator_Password);
		
		//table data
		ObservableList<Map<String, String>> dataAdministrator = FXCollections.observableArrayList();
		List<Administrator> rsAdministrator = EntityManager.getAllInstancesOf("Administrator");
		for (Administrator r : rsAdministrator) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			unit.put("Id", String.valueOf(r.getId()));
			if (r.getName() != null)
				unit.put("Name", String.valueOf(r.getName()));
			else
				unit.put("Name", "");
			if (r.getUsername() != null)
				unit.put("Username", String.valueOf(r.getUsername()));
			else
				unit.put("Username", "");
			if (r.getPassword() != null)
				unit.put("Password", String.valueOf(r.getPassword()));
			else
				unit.put("Password", "");

			dataAdministrator.add(unit);
		}
		
		tableAdministrator.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableAdministrator.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("Administrator", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableAdministrator.setItems(dataAdministrator);
		allObjectTables.put("Administrator", tableAdministrator);
		

		
	}
	
	/* 
	* update all object tables with sub dataset
	*/ 
	public void updateStoreTable(List<Store> rsStore) {
			ObservableList<Map<String, String>> dataStore = FXCollections.observableArrayList();
			for (Store r : rsStore) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				unit.put("Id", String.valueOf(r.getId()));
				if (r.getName() != null)
					unit.put("Name", String.valueOf(r.getName()));
				else
					unit.put("Name", "");
				if (r.getAddress() != null)
					unit.put("Address", String.valueOf(r.getAddress()));
				else
					unit.put("Address", "");
				unit.put("IsOpened", String.valueOf(r.getIsOpened()));
				dataStore.add(unit);
			}
			
			allObjectTables.get("Store").setItems(dataStore);
	}
	public void updateProductCatalogTable(List<ProductCatalog> rsProductCatalog) {
			ObservableList<Map<String, String>> dataProductCatalog = FXCollections.observableArrayList();
			for (ProductCatalog r : rsProductCatalog) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				unit.put("Id", String.valueOf(r.getId()));
				if (r.getName() != null)
					unit.put("Name", String.valueOf(r.getName()));
				else
					unit.put("Name", "");
				dataProductCatalog.add(unit);
			}
			
			allObjectTables.get("ProductCatalog").setItems(dataProductCatalog);
	}
	public void updateCashDeskTable(List<CashDesk> rsCashDesk) {
			ObservableList<Map<String, String>> dataCashDesk = FXCollections.observableArrayList();
			for (CashDesk r : rsCashDesk) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				unit.put("Id", String.valueOf(r.getId()));
				if (r.getName() != null)
					unit.put("Name", String.valueOf(r.getName()));
				else
					unit.put("Name", "");
				unit.put("IsOpened", String.valueOf(r.getIsOpened()));
				dataCashDesk.add(unit);
			}
			
			allObjectTables.get("CashDesk").setItems(dataCashDesk);
	}
	public void updateSaleTable(List<Sale> rsSale) {
			ObservableList<Map<String, String>> dataSale = FXCollections.observableArrayList();
			for (Sale r : rsSale) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				if (r.getTime() != null)
					unit.put("Time", r.getTime().format(dateformatter));
				else
					unit.put("Time", "");
				unit.put("IsComplete", String.valueOf(r.getIsComplete()));
				unit.put("Amount", String.valueOf(r.getAmount()));
				unit.put("IsReadytoPay", String.valueOf(r.getIsReadytoPay()));
				if (r.getPaymentMethod() != null)
					unit.put("PaymentMethod", String.valueOf(r.getPaymentMethod()));
				else
					unit.put("PaymentMethod", "");
				unit.put("IsPaid", String.valueOf(r.getIsPaid()));
				if (r.getPaymentDetails() != null)
					unit.put("PaymentDetails", String.valueOf(r.getPaymentDetails()));
				else
					unit.put("PaymentDetails", "");
				unit.put("OrderId", String.valueOf(r.getOrderId()));
				dataSale.add(unit);
			}
			
			allObjectTables.get("Sale").setItems(dataSale);
	}
	public void updateCashierTable(List<Cashier> rsCashier) {
			ObservableList<Map<String, String>> dataCashier = FXCollections.observableArrayList();
			for (Cashier r : rsCashier) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				unit.put("Id", String.valueOf(r.getId()));
				if (r.getName() != null)
					unit.put("Name", String.valueOf(r.getName()));
				else
					unit.put("Name", "");
				dataCashier.add(unit);
			}
			
			allObjectTables.get("Cashier").setItems(dataCashier);
	}
	public void updateSalesLineItemTable(List<SalesLineItem> rsSalesLineItem) {
			ObservableList<Map<String, String>> dataSalesLineItem = FXCollections.observableArrayList();
			for (SalesLineItem r : rsSalesLineItem) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				unit.put("Quantity", String.valueOf(r.getQuantity()));
				unit.put("Subamount", String.valueOf(r.getSubamount()));
				unit.put("BelongedCheckout", String.valueOf(r.getBelongedCheckout()));
				dataSalesLineItem.add(unit);
			}
			
			allObjectTables.get("SalesLineItem").setItems(dataSalesLineItem);
	}
	public void updateItemTable(List<Item> rsItem) {
			ObservableList<Map<String, String>> dataItem = FXCollections.observableArrayList();
			for (Item r : rsItem) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				unit.put("Barcode", String.valueOf(r.getBarcode()));
				if (r.getName() != null)
					unit.put("Name", String.valueOf(r.getName()));
				else
					unit.put("Name", "");
				unit.put("Price", String.valueOf(r.getPrice()));
				unit.put("StockNumber", String.valueOf(r.getStockNumber()));
				unit.put("OrderPrice", String.valueOf(r.getOrderPrice()));
				unit.put("ItemID", String.valueOf(r.getItemID()));
				dataItem.add(unit);
			}
			
			allObjectTables.get("Item").setItems(dataItem);
	}
	public void updatePaymentTable(List<Payment> rsPayment) {
			ObservableList<Map<String, String>> dataPayment = FXCollections.observableArrayList();
			for (Payment r : rsPayment) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				unit.put("AmountTendered", String.valueOf(r.getAmountTendered()));
				dataPayment.add(unit);
			}
			
			allObjectTables.get("Payment").setItems(dataPayment);
	}
	public void updateCashPaymentTable(List<CashPayment> rsCashPayment) {
			ObservableList<Map<String, String>> dataCashPayment = FXCollections.observableArrayList();
			for (CashPayment r : rsCashPayment) {
				Map<String, String> unit = new HashMap<String, String>();
				
				unit.put("AmountTendered", String.valueOf(r.getAmountTendered()));
				
				unit.put("Balance", String.valueOf(r.getBalance()));
				dataCashPayment.add(unit);
			}
			
			allObjectTables.get("CashPayment").setItems(dataCashPayment);
	}
	public void updateCardPaymentTable(List<CardPayment> rsCardPayment) {
			ObservableList<Map<String, String>> dataCardPayment = FXCollections.observableArrayList();
			for (CardPayment r : rsCardPayment) {
				Map<String, String> unit = new HashMap<String, String>();
				
				unit.put("AmountTendered", String.valueOf(r.getAmountTendered()));
				
				if (r.getCardAccountNumber() != null)
					unit.put("CardAccountNumber", String.valueOf(r.getCardAccountNumber()));
				else
					unit.put("CardAccountNumber", "");
				if (r.getExpiryDate() != null)
					unit.put("ExpiryDate", r.getExpiryDate().format(dateformatter));
				else
					unit.put("ExpiryDate", "");
				dataCardPayment.add(unit);
			}
			
			allObjectTables.get("CardPayment").setItems(dataCardPayment);
	}
	public void updateWechatPaymentTable(List<WechatPayment> rsWechatPayment) {
			ObservableList<Map<String, String>> dataWechatPayment = FXCollections.observableArrayList();
			for (WechatPayment r : rsWechatPayment) {
				Map<String, String> unit = new HashMap<String, String>();
				
				unit.put("AmountTendered", String.valueOf(r.getAmountTendered()));
				
				if (r.getOpenID() != null)
					unit.put("OpenID", String.valueOf(r.getOpenID()));
				else
					unit.put("OpenID", "");
				dataWechatPayment.add(unit);
			}
			
			allObjectTables.get("WechatPayment").setItems(dataWechatPayment);
	}
	public void updateAliPaymentTable(List<AliPayment> rsAliPayment) {
			ObservableList<Map<String, String>> dataAliPayment = FXCollections.observableArrayList();
			for (AliPayment r : rsAliPayment) {
				Map<String, String> unit = new HashMap<String, String>();
				
				unit.put("AmountTendered", String.valueOf(r.getAmountTendered()));
				
				if (r.getAlipayAccount() != null)
					unit.put("AlipayAccount", String.valueOf(r.getAlipayAccount()));
				else
					unit.put("AlipayAccount", "");
				dataAliPayment.add(unit);
			}
			
			allObjectTables.get("AliPayment").setItems(dataAliPayment);
	}
	public void updateOrderEntryTable(List<OrderEntry> rsOrderEntry) {
			ObservableList<Map<String, String>> dataOrderEntry = FXCollections.observableArrayList();
			for (OrderEntry r : rsOrderEntry) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				unit.put("Quantity", String.valueOf(r.getQuantity()));
				unit.put("SubAmount", String.valueOf(r.getSubAmount()));
				dataOrderEntry.add(unit);
			}
			
			allObjectTables.get("OrderEntry").setItems(dataOrderEntry);
	}
	public void updateSupplierTable(List<Supplier> rsSupplier) {
			ObservableList<Map<String, String>> dataSupplier = FXCollections.observableArrayList();
			for (Supplier r : rsSupplier) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				unit.put("Id", String.valueOf(r.getId()));
				if (r.getName() != null)
					unit.put("Name", String.valueOf(r.getName()));
				else
					unit.put("Name", "");
				dataSupplier.add(unit);
			}
			
			allObjectTables.get("Supplier").setItems(dataSupplier);
	}
	public void updateOrderProductTable(List<OrderProduct> rsOrderProduct) {
			ObservableList<Map<String, String>> dataOrderProduct = FXCollections.observableArrayList();
			for (OrderProduct r : rsOrderProduct) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				unit.put("Id", String.valueOf(r.getId()));
				if (r.getTime() != null)
					unit.put("Time", r.getTime().format(dateformatter));
				else
					unit.put("Time", "");
				unit.put("OrderStatus", String.valueOf(r.getOrderStatus()));
				unit.put("Amount", String.valueOf(r.getAmount()));
				dataOrderProduct.add(unit);
			}
			
			allObjectTables.get("OrderProduct").setItems(dataOrderProduct);
	}
	public void updateCustomerTable(List<Customer> rsCustomer) {
			ObservableList<Map<String, String>> dataCustomer = FXCollections.observableArrayList();
			for (Customer r : rsCustomer) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				unit.put("Id", String.valueOf(r.getId()));
				if (r.getName() != null)
					unit.put("Name", String.valueOf(r.getName()));
				else
					unit.put("Name", "");
				if (r.getContactInfo() != null)
					unit.put("ContactInfo", String.valueOf(r.getContactInfo()));
				else
					unit.put("ContactInfo", "");
				if (r.getMembershipLevel() != null)
					unit.put("MembershipLevel", String.valueOf(r.getMembershipLevel()));
				else
					unit.put("MembershipLevel", "");
				dataCustomer.add(unit);
			}
			
			allObjectTables.get("Customer").setItems(dataCustomer);
	}
	public void updateReturnRequestTable(List<ReturnRequest> rsReturnRequest) {
			ObservableList<Map<String, String>> dataReturnRequest = FXCollections.observableArrayList();
			for (ReturnRequest r : rsReturnRequest) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				unit.put("RequestId", String.valueOf(r.getRequestId()));
				unit.put("OrderId", String.valueOf(r.getOrderId()));
				unit.put("ItemId", String.valueOf(r.getItemId()));
				if (r.getReturnTime() != null)
					unit.put("ReturnTime", r.getReturnTime().format(dateformatter));
				else
					unit.put("ReturnTime", "");
				if (r.getReturnReason() != null)
					unit.put("ReturnReason", String.valueOf(r.getReturnReason()));
				else
					unit.put("ReturnReason", "");
				unit.put("IsApproved", String.valueOf(r.getIsApproved()));
				unit.put("IsReviewed", String.valueOf(r.getIsReviewed()));
				dataReturnRequest.add(unit);
			}
			
			allObjectTables.get("ReturnRequest").setItems(dataReturnRequest);
	}
	public void updateSurveillanceDeviceTable(List<SurveillanceDevice> rsSurveillanceDevice) {
			ObservableList<Map<String, String>> dataSurveillanceDevice = FXCollections.observableArrayList();
			for (SurveillanceDevice r : rsSurveillanceDevice) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				unit.put("DeviceId", String.valueOf(r.getDeviceId()));
				if (r.getLocation() != null)
					unit.put("Location", String.valueOf(r.getLocation()));
				else
					unit.put("Location", "");
				if (r.getStatus() != null)
					unit.put("Status", String.valueOf(r.getStatus()));
				else
					unit.put("Status", "");
				if (r.getLastMaintenanceTime() != null)
					unit.put("LastMaintenanceTime", r.getLastMaintenanceTime().format(dateformatter));
				else
					unit.put("LastMaintenanceTime", "");
				dataSurveillanceDevice.add(unit);
			}
			
			allObjectTables.get("SurveillanceDevice").setItems(dataSurveillanceDevice);
	}
	public void updateSalesDataAnalysisResultTable(List<SalesDataAnalysisResult> rsSalesDataAnalysisResult) {
			ObservableList<Map<String, String>> dataSalesDataAnalysisResult = FXCollections.observableArrayList();
			for (SalesDataAnalysisResult r : rsSalesDataAnalysisResult) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				unit.put("AnalysisId", String.valueOf(r.getAnalysisId()));
				if (r.getAnalysisTime() != null)
					unit.put("AnalysisTime", r.getAnalysisTime().format(dateformatter));
				else
					unit.put("AnalysisTime", "");
				unit.put("TotalSalesAmount", String.valueOf(r.getTotalSalesAmount()));
				if (r.getBestSellingProducts() != null)
					unit.put("BestSellingProducts", String.valueOf(r.getBestSellingProducts()));
				else
					unit.put("BestSellingProducts", "");
				if (r.getSalesTrend() != null)
					unit.put("SalesTrend", String.valueOf(r.getSalesTrend()));
				else
					unit.put("SalesTrend", "");
				dataSalesDataAnalysisResult.add(unit);
			}
			
			allObjectTables.get("SalesDataAnalysisResult").setItems(dataSalesDataAnalysisResult);
	}
	public void updatePurchaseOrderTable(List<PurchaseOrder> rsPurchaseOrder) {
			ObservableList<Map<String, String>> dataPurchaseOrder = FXCollections.observableArrayList();
			for (PurchaseOrder r : rsPurchaseOrder) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				unit.put("OrderId", String.valueOf(r.getOrderId()));
				unit.put("SupplierId", String.valueOf(r.getSupplierId()));
				unit.put("StoreId", String.valueOf(r.getStoreId()));
				if (r.getOrderTime() != null)
					unit.put("OrderTime", r.getOrderTime().format(dateformatter));
				else
					unit.put("OrderTime", "");
				if (r.getExpectedDeliveryTime() != null)
					unit.put("ExpectedDeliveryTime", r.getExpectedDeliveryTime().format(dateformatter));
				else
					unit.put("ExpectedDeliveryTime", "");
				unit.put("TotalAmount", String.valueOf(r.getTotalAmount()));
				unit.put("IsReceived", String.valueOf(r.getIsReceived()));
				unit.put("IsAccepted", String.valueOf(r.getIsAccepted()));
				dataPurchaseOrder.add(unit);
			}
			
			allObjectTables.get("PurchaseOrder").setItems(dataPurchaseOrder);
	}
	public void updateRecommendedProductTable(List<RecommendedProduct> rsRecommendedProduct) {
			ObservableList<Map<String, String>> dataRecommendedProduct = FXCollections.observableArrayList();
			for (RecommendedProduct r : rsRecommendedProduct) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				unit.put("RecommendationId", String.valueOf(r.getRecommendationId()));
				unit.put("ProductId", String.valueOf(r.getProductId()));
				unit.put("SupplierId", String.valueOf(r.getSupplierId()));
				if (r.getRecommendationTime() != null)
					unit.put("RecommendationTime", r.getRecommendationTime().format(dateformatter));
				else
					unit.put("RecommendationTime", "");
				if (r.getRecommendationReason() != null)
					unit.put("RecommendationReason", String.valueOf(r.getRecommendationReason()));
				else
					unit.put("RecommendationReason", "");
				dataRecommendedProduct.add(unit);
			}
			
			allObjectTables.get("RecommendedProduct").setItems(dataRecommendedProduct);
	}
	public void updateAdministratorTable(List<Administrator> rsAdministrator) {
			ObservableList<Map<String, String>> dataAdministrator = FXCollections.observableArrayList();
			for (Administrator r : rsAdministrator) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				unit.put("Id", String.valueOf(r.getId()));
				if (r.getName() != null)
					unit.put("Name", String.valueOf(r.getName()));
				else
					unit.put("Name", "");
				if (r.getUsername() != null)
					unit.put("Username", String.valueOf(r.getUsername()));
				else
					unit.put("Username", "");
				if (r.getPassword() != null)
					unit.put("Password", String.valueOf(r.getPassword()));
				else
					unit.put("Password", "");
				dataAdministrator.add(unit);
			}
			
			allObjectTables.get("Administrator").setItems(dataAdministrator);
	}
	
	/* 
	* update all object tables
	*/ 
	public void updateStoreTable() {
			ObservableList<Map<String, String>> dataStore = FXCollections.observableArrayList();
			List<Store> rsStore = EntityManager.getAllInstancesOf("Store");
			for (Store r : rsStore) {
				Map<String, String> unit = new HashMap<String, String>();


				unit.put("Id", String.valueOf(r.getId()));
				if (r.getName() != null)
					unit.put("Name", String.valueOf(r.getName()));
				else
					unit.put("Name", "");
				if (r.getAddress() != null)
					unit.put("Address", String.valueOf(r.getAddress()));
				else
					unit.put("Address", "");
				unit.put("IsOpened", String.valueOf(r.getIsOpened()));
				dataStore.add(unit);
			}
			
			allObjectTables.get("Store").setItems(dataStore);
	}
	public void updateProductCatalogTable() {
			ObservableList<Map<String, String>> dataProductCatalog = FXCollections.observableArrayList();
			List<ProductCatalog> rsProductCatalog = EntityManager.getAllInstancesOf("ProductCatalog");
			for (ProductCatalog r : rsProductCatalog) {
				Map<String, String> unit = new HashMap<String, String>();


				unit.put("Id", String.valueOf(r.getId()));
				if (r.getName() != null)
					unit.put("Name", String.valueOf(r.getName()));
				else
					unit.put("Name", "");
				dataProductCatalog.add(unit);
			}
			
			allObjectTables.get("ProductCatalog").setItems(dataProductCatalog);
	}
	public void updateCashDeskTable() {
			ObservableList<Map<String, String>> dataCashDesk = FXCollections.observableArrayList();
			List<CashDesk> rsCashDesk = EntityManager.getAllInstancesOf("CashDesk");
			for (CashDesk r : rsCashDesk) {
				Map<String, String> unit = new HashMap<String, String>();


				unit.put("Id", String.valueOf(r.getId()));
				if (r.getName() != null)
					unit.put("Name", String.valueOf(r.getName()));
				else
					unit.put("Name", "");
				unit.put("IsOpened", String.valueOf(r.getIsOpened()));
				dataCashDesk.add(unit);
			}
			
			allObjectTables.get("CashDesk").setItems(dataCashDesk);
	}
	public void updateSaleTable() {
			ObservableList<Map<String, String>> dataSale = FXCollections.observableArrayList();
			List<Sale> rsSale = EntityManager.getAllInstancesOf("Sale");
			for (Sale r : rsSale) {
				Map<String, String> unit = new HashMap<String, String>();


				if (r.getTime() != null)
					unit.put("Time", r.getTime().format(dateformatter));
				else
					unit.put("Time", "");
				unit.put("IsComplete", String.valueOf(r.getIsComplete()));
				unit.put("Amount", String.valueOf(r.getAmount()));
				unit.put("IsReadytoPay", String.valueOf(r.getIsReadytoPay()));
				if (r.getPaymentMethod() != null)
					unit.put("PaymentMethod", String.valueOf(r.getPaymentMethod()));
				else
					unit.put("PaymentMethod", "");
				unit.put("IsPaid", String.valueOf(r.getIsPaid()));
				if (r.getPaymentDetails() != null)
					unit.put("PaymentDetails", String.valueOf(r.getPaymentDetails()));
				else
					unit.put("PaymentDetails", "");
				unit.put("OrderId", String.valueOf(r.getOrderId()));
				dataSale.add(unit);
			}
			
			allObjectTables.get("Sale").setItems(dataSale);
	}
	public void updateCashierTable() {
			ObservableList<Map<String, String>> dataCashier = FXCollections.observableArrayList();
			List<Cashier> rsCashier = EntityManager.getAllInstancesOf("Cashier");
			for (Cashier r : rsCashier) {
				Map<String, String> unit = new HashMap<String, String>();


				unit.put("Id", String.valueOf(r.getId()));
				if (r.getName() != null)
					unit.put("Name", String.valueOf(r.getName()));
				else
					unit.put("Name", "");
				dataCashier.add(unit);
			}
			
			allObjectTables.get("Cashier").setItems(dataCashier);
	}
	public void updateSalesLineItemTable() {
			ObservableList<Map<String, String>> dataSalesLineItem = FXCollections.observableArrayList();
			List<SalesLineItem> rsSalesLineItem = EntityManager.getAllInstancesOf("SalesLineItem");
			for (SalesLineItem r : rsSalesLineItem) {
				Map<String, String> unit = new HashMap<String, String>();


				unit.put("Quantity", String.valueOf(r.getQuantity()));
				unit.put("Subamount", String.valueOf(r.getSubamount()));
				unit.put("BelongedCheckout", String.valueOf(r.getBelongedCheckout()));
				dataSalesLineItem.add(unit);
			}
			
			allObjectTables.get("SalesLineItem").setItems(dataSalesLineItem);
	}
	public void updateItemTable() {
			ObservableList<Map<String, String>> dataItem = FXCollections.observableArrayList();
			List<Item> rsItem = EntityManager.getAllInstancesOf("Item");
			for (Item r : rsItem) {
				Map<String, String> unit = new HashMap<String, String>();


				unit.put("Barcode", String.valueOf(r.getBarcode()));
				if (r.getName() != null)
					unit.put("Name", String.valueOf(r.getName()));
				else
					unit.put("Name", "");
				unit.put("Price", String.valueOf(r.getPrice()));
				unit.put("StockNumber", String.valueOf(r.getStockNumber()));
				unit.put("OrderPrice", String.valueOf(r.getOrderPrice()));
				unit.put("ItemID", String.valueOf(r.getItemID()));
				dataItem.add(unit);
			}
			
			allObjectTables.get("Item").setItems(dataItem);
	}
	public void updatePaymentTable() {
			ObservableList<Map<String, String>> dataPayment = FXCollections.observableArrayList();
			List<Payment> rsPayment = EntityManager.getAllInstancesOf("Payment");
			for (Payment r : rsPayment) {
				Map<String, String> unit = new HashMap<String, String>();


				unit.put("AmountTendered", String.valueOf(r.getAmountTendered()));
				dataPayment.add(unit);
			}
			
			allObjectTables.get("Payment").setItems(dataPayment);
	}
	public void updateCashPaymentTable() {
			ObservableList<Map<String, String>> dataCashPayment = FXCollections.observableArrayList();
			List<CashPayment> rsCashPayment = EntityManager.getAllInstancesOf("CashPayment");
			for (CashPayment r : rsCashPayment) {
				Map<String, String> unit = new HashMap<String, String>();

				unit.put("AmountTendered", String.valueOf(r.getAmountTendered()));

				unit.put("Balance", String.valueOf(r.getBalance()));
				dataCashPayment.add(unit);
			}
			
			allObjectTables.get("CashPayment").setItems(dataCashPayment);
	}
	public void updateCardPaymentTable() {
			ObservableList<Map<String, String>> dataCardPayment = FXCollections.observableArrayList();
			List<CardPayment> rsCardPayment = EntityManager.getAllInstancesOf("CardPayment");
			for (CardPayment r : rsCardPayment) {
				Map<String, String> unit = new HashMap<String, String>();

				unit.put("AmountTendered", String.valueOf(r.getAmountTendered()));

				if (r.getCardAccountNumber() != null)
					unit.put("CardAccountNumber", String.valueOf(r.getCardAccountNumber()));
				else
					unit.put("CardAccountNumber", "");
				if (r.getExpiryDate() != null)
					unit.put("ExpiryDate", r.getExpiryDate().format(dateformatter));
				else
					unit.put("ExpiryDate", "");
				dataCardPayment.add(unit);
			}
			
			allObjectTables.get("CardPayment").setItems(dataCardPayment);
	}
	public void updateWechatPaymentTable() {
			ObservableList<Map<String, String>> dataWechatPayment = FXCollections.observableArrayList();
			List<WechatPayment> rsWechatPayment = EntityManager.getAllInstancesOf("WechatPayment");
			for (WechatPayment r : rsWechatPayment) {
				Map<String, String> unit = new HashMap<String, String>();

				unit.put("AmountTendered", String.valueOf(r.getAmountTendered()));

				if (r.getOpenID() != null)
					unit.put("OpenID", String.valueOf(r.getOpenID()));
				else
					unit.put("OpenID", "");
				dataWechatPayment.add(unit);
			}
			
			allObjectTables.get("WechatPayment").setItems(dataWechatPayment);
	}
	public void updateAliPaymentTable() {
			ObservableList<Map<String, String>> dataAliPayment = FXCollections.observableArrayList();
			List<AliPayment> rsAliPayment = EntityManager.getAllInstancesOf("AliPayment");
			for (AliPayment r : rsAliPayment) {
				Map<String, String> unit = new HashMap<String, String>();

				unit.put("AmountTendered", String.valueOf(r.getAmountTendered()));

				if (r.getAlipayAccount() != null)
					unit.put("AlipayAccount", String.valueOf(r.getAlipayAccount()));
				else
					unit.put("AlipayAccount", "");
				dataAliPayment.add(unit);
			}
			
			allObjectTables.get("AliPayment").setItems(dataAliPayment);
	}
	public void updateOrderEntryTable() {
			ObservableList<Map<String, String>> dataOrderEntry = FXCollections.observableArrayList();
			List<OrderEntry> rsOrderEntry = EntityManager.getAllInstancesOf("OrderEntry");
			for (OrderEntry r : rsOrderEntry) {
				Map<String, String> unit = new HashMap<String, String>();


				unit.put("Quantity", String.valueOf(r.getQuantity()));
				unit.put("SubAmount", String.valueOf(r.getSubAmount()));
				dataOrderEntry.add(unit);
			}
			
			allObjectTables.get("OrderEntry").setItems(dataOrderEntry);
	}
	public void updateSupplierTable() {
			ObservableList<Map<String, String>> dataSupplier = FXCollections.observableArrayList();
			List<Supplier> rsSupplier = EntityManager.getAllInstancesOf("Supplier");
			for (Supplier r : rsSupplier) {
				Map<String, String> unit = new HashMap<String, String>();


				unit.put("Id", String.valueOf(r.getId()));
				if (r.getName() != null)
					unit.put("Name", String.valueOf(r.getName()));
				else
					unit.put("Name", "");
				dataSupplier.add(unit);
			}
			
			allObjectTables.get("Supplier").setItems(dataSupplier);
	}
	public void updateOrderProductTable() {
			ObservableList<Map<String, String>> dataOrderProduct = FXCollections.observableArrayList();
			List<OrderProduct> rsOrderProduct = EntityManager.getAllInstancesOf("OrderProduct");
			for (OrderProduct r : rsOrderProduct) {
				Map<String, String> unit = new HashMap<String, String>();


				unit.put("Id", String.valueOf(r.getId()));
				if (r.getTime() != null)
					unit.put("Time", r.getTime().format(dateformatter));
				else
					unit.put("Time", "");
				unit.put("OrderStatus", String.valueOf(r.getOrderStatus()));
				unit.put("Amount", String.valueOf(r.getAmount()));
				dataOrderProduct.add(unit);
			}
			
			allObjectTables.get("OrderProduct").setItems(dataOrderProduct);
	}
	public void updateCustomerTable() {
			ObservableList<Map<String, String>> dataCustomer = FXCollections.observableArrayList();
			List<Customer> rsCustomer = EntityManager.getAllInstancesOf("Customer");
			for (Customer r : rsCustomer) {
				Map<String, String> unit = new HashMap<String, String>();


				unit.put("Id", String.valueOf(r.getId()));
				if (r.getName() != null)
					unit.put("Name", String.valueOf(r.getName()));
				else
					unit.put("Name", "");
				if (r.getContactInfo() != null)
					unit.put("ContactInfo", String.valueOf(r.getContactInfo()));
				else
					unit.put("ContactInfo", "");
				if (r.getMembershipLevel() != null)
					unit.put("MembershipLevel", String.valueOf(r.getMembershipLevel()));
				else
					unit.put("MembershipLevel", "");
				dataCustomer.add(unit);
			}
			
			allObjectTables.get("Customer").setItems(dataCustomer);
	}
	public void updateReturnRequestTable() {
			ObservableList<Map<String, String>> dataReturnRequest = FXCollections.observableArrayList();
			List<ReturnRequest> rsReturnRequest = EntityManager.getAllInstancesOf("ReturnRequest");
			for (ReturnRequest r : rsReturnRequest) {
				Map<String, String> unit = new HashMap<String, String>();


				unit.put("RequestId", String.valueOf(r.getRequestId()));
				unit.put("OrderId", String.valueOf(r.getOrderId()));
				unit.put("ItemId", String.valueOf(r.getItemId()));
				if (r.getReturnTime() != null)
					unit.put("ReturnTime", r.getReturnTime().format(dateformatter));
				else
					unit.put("ReturnTime", "");
				if (r.getReturnReason() != null)
					unit.put("ReturnReason", String.valueOf(r.getReturnReason()));
				else
					unit.put("ReturnReason", "");
				unit.put("IsApproved", String.valueOf(r.getIsApproved()));
				unit.put("IsReviewed", String.valueOf(r.getIsReviewed()));
				dataReturnRequest.add(unit);
			}
			
			allObjectTables.get("ReturnRequest").setItems(dataReturnRequest);
	}
	public void updateSurveillanceDeviceTable() {
			ObservableList<Map<String, String>> dataSurveillanceDevice = FXCollections.observableArrayList();
			List<SurveillanceDevice> rsSurveillanceDevice = EntityManager.getAllInstancesOf("SurveillanceDevice");
			for (SurveillanceDevice r : rsSurveillanceDevice) {
				Map<String, String> unit = new HashMap<String, String>();


				unit.put("DeviceId", String.valueOf(r.getDeviceId()));
				if (r.getLocation() != null)
					unit.put("Location", String.valueOf(r.getLocation()));
				else
					unit.put("Location", "");
				if (r.getStatus() != null)
					unit.put("Status", String.valueOf(r.getStatus()));
				else
					unit.put("Status", "");
				if (r.getLastMaintenanceTime() != null)
					unit.put("LastMaintenanceTime", r.getLastMaintenanceTime().format(dateformatter));
				else
					unit.put("LastMaintenanceTime", "");
				dataSurveillanceDevice.add(unit);
			}
			
			allObjectTables.get("SurveillanceDevice").setItems(dataSurveillanceDevice);
	}
	public void updateSalesDataAnalysisResultTable() {
			ObservableList<Map<String, String>> dataSalesDataAnalysisResult = FXCollections.observableArrayList();
			List<SalesDataAnalysisResult> rsSalesDataAnalysisResult = EntityManager.getAllInstancesOf("SalesDataAnalysisResult");
			for (SalesDataAnalysisResult r : rsSalesDataAnalysisResult) {
				Map<String, String> unit = new HashMap<String, String>();


				unit.put("AnalysisId", String.valueOf(r.getAnalysisId()));
				if (r.getAnalysisTime() != null)
					unit.put("AnalysisTime", r.getAnalysisTime().format(dateformatter));
				else
					unit.put("AnalysisTime", "");
				unit.put("TotalSalesAmount", String.valueOf(r.getTotalSalesAmount()));
				if (r.getBestSellingProducts() != null)
					unit.put("BestSellingProducts", String.valueOf(r.getBestSellingProducts()));
				else
					unit.put("BestSellingProducts", "");
				if (r.getSalesTrend() != null)
					unit.put("SalesTrend", String.valueOf(r.getSalesTrend()));
				else
					unit.put("SalesTrend", "");
				dataSalesDataAnalysisResult.add(unit);
			}
			
			allObjectTables.get("SalesDataAnalysisResult").setItems(dataSalesDataAnalysisResult);
	}
	public void updatePurchaseOrderTable() {
			ObservableList<Map<String, String>> dataPurchaseOrder = FXCollections.observableArrayList();
			List<PurchaseOrder> rsPurchaseOrder = EntityManager.getAllInstancesOf("PurchaseOrder");
			for (PurchaseOrder r : rsPurchaseOrder) {
				Map<String, String> unit = new HashMap<String, String>();


				unit.put("OrderId", String.valueOf(r.getOrderId()));
				unit.put("SupplierId", String.valueOf(r.getSupplierId()));
				unit.put("StoreId", String.valueOf(r.getStoreId()));
				if (r.getOrderTime() != null)
					unit.put("OrderTime", r.getOrderTime().format(dateformatter));
				else
					unit.put("OrderTime", "");
				if (r.getExpectedDeliveryTime() != null)
					unit.put("ExpectedDeliveryTime", r.getExpectedDeliveryTime().format(dateformatter));
				else
					unit.put("ExpectedDeliveryTime", "");
				unit.put("TotalAmount", String.valueOf(r.getTotalAmount()));
				unit.put("IsReceived", String.valueOf(r.getIsReceived()));
				unit.put("IsAccepted", String.valueOf(r.getIsAccepted()));
				dataPurchaseOrder.add(unit);
			}
			
			allObjectTables.get("PurchaseOrder").setItems(dataPurchaseOrder);
	}
	public void updateRecommendedProductTable() {
			ObservableList<Map<String, String>> dataRecommendedProduct = FXCollections.observableArrayList();
			List<RecommendedProduct> rsRecommendedProduct = EntityManager.getAllInstancesOf("RecommendedProduct");
			for (RecommendedProduct r : rsRecommendedProduct) {
				Map<String, String> unit = new HashMap<String, String>();


				unit.put("RecommendationId", String.valueOf(r.getRecommendationId()));
				unit.put("ProductId", String.valueOf(r.getProductId()));
				unit.put("SupplierId", String.valueOf(r.getSupplierId()));
				if (r.getRecommendationTime() != null)
					unit.put("RecommendationTime", r.getRecommendationTime().format(dateformatter));
				else
					unit.put("RecommendationTime", "");
				if (r.getRecommendationReason() != null)
					unit.put("RecommendationReason", String.valueOf(r.getRecommendationReason()));
				else
					unit.put("RecommendationReason", "");
				dataRecommendedProduct.add(unit);
			}
			
			allObjectTables.get("RecommendedProduct").setItems(dataRecommendedProduct);
	}
	public void updateAdministratorTable() {
			ObservableList<Map<String, String>> dataAdministrator = FXCollections.observableArrayList();
			List<Administrator> rsAdministrator = EntityManager.getAllInstancesOf("Administrator");
			for (Administrator r : rsAdministrator) {
				Map<String, String> unit = new HashMap<String, String>();


				unit.put("Id", String.valueOf(r.getId()));
				if (r.getName() != null)
					unit.put("Name", String.valueOf(r.getName()));
				else
					unit.put("Name", "");
				if (r.getUsername() != null)
					unit.put("Username", String.valueOf(r.getUsername()));
				else
					unit.put("Username", "");
				if (r.getPassword() != null)
					unit.put("Password", String.valueOf(r.getPassword()));
				else
					unit.put("Password", "");
				dataAdministrator.add(unit);
			}
			
			allObjectTables.get("Administrator").setItems(dataAdministrator);
	}
	
	public void classStatisicBingding() {
	
		 classInfodata = FXCollections.observableArrayList();
	 	 store = new ClassInfo("Store", EntityManager.getAllInstancesOf("Store").size());
	 	 classInfodata.add(store);
	 	 productcatalog = new ClassInfo("ProductCatalog", EntityManager.getAllInstancesOf("ProductCatalog").size());
	 	 classInfodata.add(productcatalog);
	 	 cashdesk = new ClassInfo("CashDesk", EntityManager.getAllInstancesOf("CashDesk").size());
	 	 classInfodata.add(cashdesk);
	 	 sale = new ClassInfo("Sale", EntityManager.getAllInstancesOf("Sale").size());
	 	 classInfodata.add(sale);
	 	 cashier = new ClassInfo("Cashier", EntityManager.getAllInstancesOf("Cashier").size());
	 	 classInfodata.add(cashier);
	 	 saleslineitem = new ClassInfo("SalesLineItem", EntityManager.getAllInstancesOf("SalesLineItem").size());
	 	 classInfodata.add(saleslineitem);
	 	 item = new ClassInfo("Item", EntityManager.getAllInstancesOf("Item").size());
	 	 classInfodata.add(item);
	 	 payment = new ClassInfo("Payment", EntityManager.getAllInstancesOf("Payment").size());
	 	 classInfodata.add(payment);
	 	 cashpayment = new ClassInfo("CashPayment", EntityManager.getAllInstancesOf("CashPayment").size());
	 	 classInfodata.add(cashpayment);
	 	 cardpayment = new ClassInfo("CardPayment", EntityManager.getAllInstancesOf("CardPayment").size());
	 	 classInfodata.add(cardpayment);
	 	 wechatpayment = new ClassInfo("WechatPayment", EntityManager.getAllInstancesOf("WechatPayment").size());
	 	 classInfodata.add(wechatpayment);
	 	 alipayment = new ClassInfo("AliPayment", EntityManager.getAllInstancesOf("AliPayment").size());
	 	 classInfodata.add(alipayment);
	 	 orderentry = new ClassInfo("OrderEntry", EntityManager.getAllInstancesOf("OrderEntry").size());
	 	 classInfodata.add(orderentry);
	 	 supplier = new ClassInfo("Supplier", EntityManager.getAllInstancesOf("Supplier").size());
	 	 classInfodata.add(supplier);
	 	 orderproduct = new ClassInfo("OrderProduct", EntityManager.getAllInstancesOf("OrderProduct").size());
	 	 classInfodata.add(orderproduct);
	 	 customer = new ClassInfo("Customer", EntityManager.getAllInstancesOf("Customer").size());
	 	 classInfodata.add(customer);
	 	 returnrequest = new ClassInfo("ReturnRequest", EntityManager.getAllInstancesOf("ReturnRequest").size());
	 	 classInfodata.add(returnrequest);
	 	 surveillancedevice = new ClassInfo("SurveillanceDevice", EntityManager.getAllInstancesOf("SurveillanceDevice").size());
	 	 classInfodata.add(surveillancedevice);
	 	 salesdataanalysisresult = new ClassInfo("SalesDataAnalysisResult", EntityManager.getAllInstancesOf("SalesDataAnalysisResult").size());
	 	 classInfodata.add(salesdataanalysisresult);
	 	 purchaseorder = new ClassInfo("PurchaseOrder", EntityManager.getAllInstancesOf("PurchaseOrder").size());
	 	 classInfodata.add(purchaseorder);
	 	 recommendedproduct = new ClassInfo("RecommendedProduct", EntityManager.getAllInstancesOf("RecommendedProduct").size());
	 	 classInfodata.add(recommendedproduct);
	 	 administrator = new ClassInfo("Administrator", EntityManager.getAllInstancesOf("Administrator").size());
	 	 classInfodata.add(administrator);
	 	 
		 class_statisic.setItems(classInfodata);
		 
		 //Class Statisic Binding
		 class_statisic.getSelectionModel().selectedItemProperty().addListener(
				 (observable, oldValue, newValue) ->  { 
				 										 //no selected object in table
				 										 objectindex = -1;
				 										 
				 										 //get lastest data, reflect updateTableData method
				 										 try {
												 			 Method updateob = this.getClass().getMethod("update" + newValue.getName() + "Table", null);
												 			 updateob.invoke(this);			 
												 		 } catch (Exception e) {
												 		 	 e.printStackTrace();
												 		 }		 										 
				 	
				 										 //show object table
				 			 				 			 TableView obs = allObjectTables.get(newValue.getName());
				 			 				 			 if (obs != null) {
				 			 				 				object_statics.setContent(obs);
				 			 				 				object_statics.setText("All Objects " + newValue.getName() + ":");
				 			 				 			 }
				 			 				 			 
				 			 				 			 //update association information
							 			 				 updateAssociation(newValue.getName());
				 			 				 			 
				 			 				 			 //show association information
				 			 				 			 ObservableList<AssociationInfo> asso = allassociationData.get(newValue.getName());
				 			 				 			 if (asso != null) {
				 			 				 			 	association_statisic.setItems(asso);
				 			 				 			 }
				 			 				 		  });
	}
	
	public void classStatisticUpdate() {
	 	 store.setNumber(EntityManager.getAllInstancesOf("Store").size());
	 	 productcatalog.setNumber(EntityManager.getAllInstancesOf("ProductCatalog").size());
	 	 cashdesk.setNumber(EntityManager.getAllInstancesOf("CashDesk").size());
	 	 sale.setNumber(EntityManager.getAllInstancesOf("Sale").size());
	 	 cashier.setNumber(EntityManager.getAllInstancesOf("Cashier").size());
	 	 saleslineitem.setNumber(EntityManager.getAllInstancesOf("SalesLineItem").size());
	 	 item.setNumber(EntityManager.getAllInstancesOf("Item").size());
	 	 payment.setNumber(EntityManager.getAllInstancesOf("Payment").size());
	 	 cashpayment.setNumber(EntityManager.getAllInstancesOf("CashPayment").size());
	 	 cardpayment.setNumber(EntityManager.getAllInstancesOf("CardPayment").size());
	 	 wechatpayment.setNumber(EntityManager.getAllInstancesOf("WechatPayment").size());
	 	 alipayment.setNumber(EntityManager.getAllInstancesOf("AliPayment").size());
	 	 orderentry.setNumber(EntityManager.getAllInstancesOf("OrderEntry").size());
	 	 supplier.setNumber(EntityManager.getAllInstancesOf("Supplier").size());
	 	 orderproduct.setNumber(EntityManager.getAllInstancesOf("OrderProduct").size());
	 	 customer.setNumber(EntityManager.getAllInstancesOf("Customer").size());
	 	 returnrequest.setNumber(EntityManager.getAllInstancesOf("ReturnRequest").size());
	 	 surveillancedevice.setNumber(EntityManager.getAllInstancesOf("SurveillanceDevice").size());
	 	 salesdataanalysisresult.setNumber(EntityManager.getAllInstancesOf("SalesDataAnalysisResult").size());
	 	 purchaseorder.setNumber(EntityManager.getAllInstancesOf("PurchaseOrder").size());
	 	 recommendedproduct.setNumber(EntityManager.getAllInstancesOf("RecommendedProduct").size());
	 	 administrator.setNumber(EntityManager.getAllInstancesOf("Administrator").size());
		
	}
	
	/**
	 * association binding
	 */
	public void associationStatisicBingding() {
		
		allassociationData = new HashMap<String, ObservableList<AssociationInfo>>();
		
		ObservableList<AssociationInfo> Store_association_data = FXCollections.observableArrayList();
		AssociationInfo Store_associatition_AssociationCashdeskes = new AssociationInfo("Store", "CashDesk", "AssociationCashdeskes", true);
		Store_association_data.add(Store_associatition_AssociationCashdeskes);
		AssociationInfo Store_associatition_Productcatalogs = new AssociationInfo("Store", "ProductCatalog", "Productcatalogs", true);
		Store_association_data.add(Store_associatition_Productcatalogs);
		AssociationInfo Store_associatition_Items = new AssociationInfo("Store", "Item", "Items", true);
		Store_association_data.add(Store_associatition_Items);
		AssociationInfo Store_associatition_Cashiers = new AssociationInfo("Store", "Cashier", "Cashiers", true);
		Store_association_data.add(Store_associatition_Cashiers);
		AssociationInfo Store_associatition_Sales = new AssociationInfo("Store", "Sale", "Sales", true);
		Store_association_data.add(Store_associatition_Sales);
		
		allassociationData.put("Store", Store_association_data);
		
		ObservableList<AssociationInfo> ProductCatalog_association_data = FXCollections.observableArrayList();
		AssociationInfo ProductCatalog_associatition_ContainedItems = new AssociationInfo("ProductCatalog", "Item", "ContainedItems", true);
		ProductCatalog_association_data.add(ProductCatalog_associatition_ContainedItems);
		
		allassociationData.put("ProductCatalog", ProductCatalog_association_data);
		
		ObservableList<AssociationInfo> CashDesk_association_data = FXCollections.observableArrayList();
		AssociationInfo CashDesk_associatition_ContainedSales = new AssociationInfo("CashDesk", "Sale", "ContainedSales", true);
		CashDesk_association_data.add(CashDesk_associatition_ContainedSales);
		AssociationInfo CashDesk_associatition_BelongedStore = new AssociationInfo("CashDesk", "Store", "BelongedStore", false);
		CashDesk_association_data.add(CashDesk_associatition_BelongedStore);
		
		allassociationData.put("CashDesk", CashDesk_association_data);
		
		ObservableList<AssociationInfo> Sale_association_data = FXCollections.observableArrayList();
		AssociationInfo Sale_associatition_Belongedstore = new AssociationInfo("Sale", "Store", "Belongedstore", false);
		Sale_association_data.add(Sale_associatition_Belongedstore);
		AssociationInfo Sale_associatition_BelongedCashDesk = new AssociationInfo("Sale", "CashDesk", "BelongedCashDesk", false);
		Sale_association_data.add(Sale_associatition_BelongedCashDesk);
		AssociationInfo Sale_associatition_ContainedSalesLine = new AssociationInfo("Sale", "SalesLineItem", "ContainedSalesLine", true);
		Sale_association_data.add(Sale_associatition_ContainedSalesLine);
		AssociationInfo Sale_associatition_AssoicatedPayment = new AssociationInfo("Sale", "Payment", "AssoicatedPayment", false);
		Sale_association_data.add(Sale_associatition_AssoicatedPayment);
		
		allassociationData.put("Sale", Sale_association_data);
		
		ObservableList<AssociationInfo> Cashier_association_data = FXCollections.observableArrayList();
		AssociationInfo Cashier_associatition_WorkedStore = new AssociationInfo("Cashier", "Store", "WorkedStore", false);
		Cashier_association_data.add(Cashier_associatition_WorkedStore);
		
		allassociationData.put("Cashier", Cashier_association_data);
		
		ObservableList<AssociationInfo> SalesLineItem_association_data = FXCollections.observableArrayList();
		AssociationInfo SalesLineItem_associatition_BelongedSale = new AssociationInfo("SalesLineItem", "Sale", "BelongedSale", false);
		SalesLineItem_association_data.add(SalesLineItem_associatition_BelongedSale);
		AssociationInfo SalesLineItem_associatition_BelongedItem = new AssociationInfo("SalesLineItem", "Item", "BelongedItem", false);
		SalesLineItem_association_data.add(SalesLineItem_associatition_BelongedItem);
		
		allassociationData.put("SalesLineItem", SalesLineItem_association_data);
		
		ObservableList<AssociationInfo> Item_association_data = FXCollections.observableArrayList();
		AssociationInfo Item_associatition_BelongedCatalog = new AssociationInfo("Item", "ProductCatalog", "BelongedCatalog", false);
		Item_association_data.add(Item_associatition_BelongedCatalog);
		
		allassociationData.put("Item", Item_association_data);
		
		ObservableList<AssociationInfo> Payment_association_data = FXCollections.observableArrayList();
		AssociationInfo Payment_associatition_BelongedSale = new AssociationInfo("Payment", "Sale", "BelongedSale", false);
		Payment_association_data.add(Payment_associatition_BelongedSale);
		
		allassociationData.put("Payment", Payment_association_data);
		
		ObservableList<AssociationInfo> CashPayment_association_data = FXCollections.observableArrayList();
		
		allassociationData.put("CashPayment", CashPayment_association_data);
		
		ObservableList<AssociationInfo> CardPayment_association_data = FXCollections.observableArrayList();
		
		allassociationData.put("CardPayment", CardPayment_association_data);
		
		ObservableList<AssociationInfo> WechatPayment_association_data = FXCollections.observableArrayList();
		
		allassociationData.put("WechatPayment", WechatPayment_association_data);
		
		ObservableList<AssociationInfo> AliPayment_association_data = FXCollections.observableArrayList();
		
		allassociationData.put("AliPayment", AliPayment_association_data);
		
		ObservableList<AssociationInfo> OrderEntry_association_data = FXCollections.observableArrayList();
		AssociationInfo OrderEntry_associatition_Item = new AssociationInfo("OrderEntry", "Item", "Item", false);
		OrderEntry_association_data.add(OrderEntry_associatition_Item);
		
		allassociationData.put("OrderEntry", OrderEntry_association_data);
		
		ObservableList<AssociationInfo> Supplier_association_data = FXCollections.observableArrayList();
		
		allassociationData.put("Supplier", Supplier_association_data);
		
		ObservableList<AssociationInfo> OrderProduct_association_data = FXCollections.observableArrayList();
		AssociationInfo OrderProduct_associatition_Supplier = new AssociationInfo("OrderProduct", "Supplier", "Supplier", false);
		OrderProduct_association_data.add(OrderProduct_associatition_Supplier);
		AssociationInfo OrderProduct_associatition_ContainedEntries = new AssociationInfo("OrderProduct", "OrderEntry", "ContainedEntries", true);
		OrderProduct_association_data.add(OrderProduct_associatition_ContainedEntries);
		
		allassociationData.put("OrderProduct", OrderProduct_association_data);
		
		ObservableList<AssociationInfo> Customer_association_data = FXCollections.observableArrayList();
		AssociationInfo Customer_associatition_Sales = new AssociationInfo("Customer", "Sale", "Sales", true);
		Customer_association_data.add(Customer_associatition_Sales);
		AssociationInfo Customer_associatition_ReturnRequests = new AssociationInfo("Customer", "ReturnRequest", "ReturnRequests", true);
		Customer_association_data.add(Customer_associatition_ReturnRequests);
		
		allassociationData.put("Customer", Customer_association_data);
		
		ObservableList<AssociationInfo> ReturnRequest_association_data = FXCollections.observableArrayList();
		AssociationInfo ReturnRequest_associatition_Customer = new AssociationInfo("ReturnRequest", "Customer", "Customer", false);
		ReturnRequest_association_data.add(ReturnRequest_associatition_Customer);
		AssociationInfo ReturnRequest_associatition_Sale = new AssociationInfo("ReturnRequest", "Sale", "Sale", false);
		ReturnRequest_association_data.add(ReturnRequest_associatition_Sale);
		AssociationInfo ReturnRequest_associatition_Item = new AssociationInfo("ReturnRequest", "Item", "Item", false);
		ReturnRequest_association_data.add(ReturnRequest_associatition_Item);
		
		allassociationData.put("ReturnRequest", ReturnRequest_association_data);
		
		ObservableList<AssociationInfo> SurveillanceDevice_association_data = FXCollections.observableArrayList();
		AssociationInfo SurveillanceDevice_associatition_Store = new AssociationInfo("SurveillanceDevice", "Store", "Store", false);
		SurveillanceDevice_association_data.add(SurveillanceDevice_associatition_Store);
		AssociationInfo SurveillanceDevice_associatition_Administrator = new AssociationInfo("SurveillanceDevice", "Administrator", "Administrator", false);
		SurveillanceDevice_association_data.add(SurveillanceDevice_associatition_Administrator);
		
		allassociationData.put("SurveillanceDevice", SurveillanceDevice_association_data);
		
		ObservableList<AssociationInfo> SalesDataAnalysisResult_association_data = FXCollections.observableArrayList();
		AssociationInfo SalesDataAnalysisResult_associatition_Administrator = new AssociationInfo("SalesDataAnalysisResult", "Administrator", "Administrator", false);
		SalesDataAnalysisResult_association_data.add(SalesDataAnalysisResult_associatition_Administrator);
		AssociationInfo SalesDataAnalysisResult_associatition_Sale = new AssociationInfo("SalesDataAnalysisResult", "Sale", "Sale", false);
		SalesDataAnalysisResult_association_data.add(SalesDataAnalysisResult_associatition_Sale);
		
		allassociationData.put("SalesDataAnalysisResult", SalesDataAnalysisResult_association_data);
		
		ObservableList<AssociationInfo> PurchaseOrder_association_data = FXCollections.observableArrayList();
		AssociationInfo PurchaseOrder_associatition_Supplier = new AssociationInfo("PurchaseOrder", "Supplier", "Supplier", false);
		PurchaseOrder_association_data.add(PurchaseOrder_associatition_Supplier);
		AssociationInfo PurchaseOrder_associatition_Store = new AssociationInfo("PurchaseOrder", "Store", "Store", false);
		PurchaseOrder_association_data.add(PurchaseOrder_associatition_Store);
		AssociationInfo PurchaseOrder_associatition_OrderProduct = new AssociationInfo("PurchaseOrder", "OrderProduct", "OrderProduct", false);
		PurchaseOrder_association_data.add(PurchaseOrder_associatition_OrderProduct);
		
		allassociationData.put("PurchaseOrder", PurchaseOrder_association_data);
		
		ObservableList<AssociationInfo> RecommendedProduct_association_data = FXCollections.observableArrayList();
		AssociationInfo RecommendedProduct_associatition_Supplier = new AssociationInfo("RecommendedProduct", "Supplier", "Supplier", false);
		RecommendedProduct_association_data.add(RecommendedProduct_associatition_Supplier);
		AssociationInfo RecommendedProduct_associatition_Item = new AssociationInfo("RecommendedProduct", "Item", "Item", false);
		RecommendedProduct_association_data.add(RecommendedProduct_associatition_Item);
		AssociationInfo RecommendedProduct_associatition_Store = new AssociationInfo("RecommendedProduct", "Store", "Store", false);
		RecommendedProduct_association_data.add(RecommendedProduct_associatition_Store);
		
		allassociationData.put("RecommendedProduct", RecommendedProduct_association_data);
		
		ObservableList<AssociationInfo> Administrator_association_data = FXCollections.observableArrayList();
		AssociationInfo Administrator_associatition_ManagedStore = new AssociationInfo("Administrator", "Store", "ManagedStore", false);
		Administrator_association_data.add(Administrator_associatition_ManagedStore);
		AssociationInfo Administrator_associatition_HandledReturnRequests = new AssociationInfo("Administrator", "ReturnRequest", "HandledReturnRequests", true);
		Administrator_association_data.add(Administrator_associatition_HandledReturnRequests);
		AssociationInfo Administrator_associatition_ConductedDataAnalysis = new AssociationInfo("Administrator", "SalesDataAnalysisResult", "ConductedDataAnalysis", true);
		Administrator_association_data.add(Administrator_associatition_ConductedDataAnalysis);
		AssociationInfo Administrator_associatition_MonitoredDevices = new AssociationInfo("Administrator", "SurveillanceDevice", "MonitoredDevices", true);
		Administrator_association_data.add(Administrator_associatition_MonitoredDevices);
		
		allassociationData.put("Administrator", Administrator_association_data);
		
		
		association_statisic.getSelectionModel().selectedItemProperty().addListener(
			    (observable, oldValue, newValue) ->  { 
	
							 		if (newValue != null) {
							 			 try {
							 			 	 if (newValue.getNumber() != 0) {
								 				 //choose object or not
								 				 if (objectindex != -1) {
									 				 Class[] cArg = new Class[1];
									 				 cArg[0] = List.class;
									 				 //reflect updateTableData method
										 			 Method updateob = this.getClass().getMethod("update" + newValue.getTargetClass() + "Table", cArg);
										 			 //find choosen object
										 			 Object selectedob = EntityManager.getAllInstancesOf(newValue.getSourceClass()).get(objectindex);
										 			 //reflect find association method
										 			 Method getAssociatedObject = selectedob.getClass().getMethod("get" + newValue.getAssociationName());
										 			 List r = new LinkedList();
										 			 //one or mulity?
										 			 if (newValue.getIsMultiple() == true) {
											 			 
											 			r = (List) getAssociatedObject.invoke(selectedob);
										 			 }
										 			 else {
										 				r.add(getAssociatedObject.invoke(selectedob));
										 			 }
										 			 //invoke update method
										 			 updateob.invoke(this, r);
										 			  
										 			 
								 				 }
												 //bind updated data to GUI
					 				 			 TableView obs = allObjectTables.get(newValue.getTargetClass());
					 				 			 if (obs != null) {
					 				 				object_statics.setContent(obs);
					 				 				object_statics.setText("Targets Objects " + newValue.getTargetClass() + ":");
					 				 			 }
					 				 		 }
							 			 }
							 			 catch (Exception e) {
							 				 e.printStackTrace();
							 			 }
							 		}
					 		  });
		
	}	
	
	

    //prepare data for contract
	public void prepareData() {
		
		//definition map
		definitions_map = new HashMap<String, String>();
		definitions_map.put("openStore", "sto:Store = Store.allInstance()->any(s:Store | s.Id = storeID)\r\r\n");
		definitions_map.put("closeStore", "sto:Store = Store.allInstance()->any(s:Store | s.Id = storeID)\r\r\n");
		definitions_map.put("openCashDesk", "cd:CashDesk = CashDesk.allInstance()->any(s:CashDesk | s.Id = cashDeskID)\r\r\n");
		definitions_map.put("closeCashDesk", "cd:CashDesk = CashDesk.allInstance()->any(s:CashDesk | s.Id = cashDeskID)\r\r\n");
		definitions_map.put("enterItem", "item:Item = Item.allInstance()->any(i:Item | i.Barcode = barcode)\r\r\n");
		definitions_map.put("endSale", "sls:Set(SalesLineItem) = CurrentSale.ContainedSalesLine\n\rsub:Set(Real) = sls->collect(s:SalesLineItem | s.Subamount)\r\r\n");
		definitions_map.put("makeNewOrder", "ordp:OrderProduct = OrderProduct.allInstance()->any(opp:OrderProduct | opp.Id = orderid)\r\r\n");
		definitions_map.put("orderItem", "item:Item = Item.allInstance()->any(i:Item | i.Barcode = barcode)\r\r\n");
		definitions_map.put("chooseSupplier", "sup:Supplier = Supplier.allInstance()->any(s:Supplier | s.Id = supplierID)\r\r\n");
		definitions_map.put("changePrice", "item:Item = Item.allInstance()->any(i:Item | i.Barcode = barcode)\r\r\n");
		definitions_map.put("receiveOrderedProduct", "op:OrderProduct = OrderProduct.allInstance()->any(i:OrderProduct | i.Id = orderID)\r\r\n");
		definitions_map.put("createStore", "store:Store = Store.allInstance()->any(sto:Store | sto.Id = id)\r\r\n");
		definitions_map.put("queryStore", "store:Store = Store.allInstance()->any(sto:Store | sto.Id = id)\r\r\n");
		definitions_map.put("modifyStore", "store:Store = Store.allInstance()->any(sto:Store | sto.Id = id)\r\r\n");
		definitions_map.put("deleteStore", "store:Store = Store.allInstance()->any(sto:Store | sto.Id = id)\r\r\n");
		definitions_map.put("createProductCatalog", "productcatalog:ProductCatalog = ProductCatalog.allInstance()->any(pro:ProductCatalog | pro.Id = id)\r\r\n");
		definitions_map.put("queryProductCatalog", "productcatalog:ProductCatalog = ProductCatalog.allInstance()->any(pro:ProductCatalog | pro.Id = id)\r\r\n");
		definitions_map.put("modifyProductCatalog", "productcatalog:ProductCatalog = ProductCatalog.allInstance()->any(pro:ProductCatalog | pro.Id = id)\r\r\n");
		definitions_map.put("deleteProductCatalog", "productcatalog:ProductCatalog = ProductCatalog.allInstance()->any(pro:ProductCatalog | pro.Id = id)\r\r\n");
		definitions_map.put("createCashDesk", "cashdesk:CashDesk = CashDesk.allInstance()->any(cas:CashDesk | cas.Id = id)\r\r\n");
		definitions_map.put("queryCashDesk", "cashdesk:CashDesk = CashDesk.allInstance()->any(cas:CashDesk | cas.Id = id)\r\r\n");
		definitions_map.put("modifyCashDesk", "cashdesk:CashDesk = CashDesk.allInstance()->any(cas:CashDesk | cas.Id = id)\r\r\n");
		definitions_map.put("deleteCashDesk", "cashdesk:CashDesk = CashDesk.allInstance()->any(cas:CashDesk | cas.Id = id)\r\r\n");
		definitions_map.put("createCashier", "cashier:Cashier = Cashier.allInstance()->any(cas:Cashier | cas.Id = id)\r\r\n");
		definitions_map.put("queryCashier", "cashier:Cashier = Cashier.allInstance()->any(cas:Cashier | cas.Id = id)\r\r\n");
		definitions_map.put("modifyCashier", "cashier:Cashier = Cashier.allInstance()->any(cas:Cashier | cas.Id = id)\r\r\n");
		definitions_map.put("deleteCashier", "cashier:Cashier = Cashier.allInstance()->any(cas:Cashier | cas.Id = id)\r\r\n");
		definitions_map.put("createItem", "item:Item = Item.allInstance()->any(ite:Item | ite.Barcode = barcode)\r\r\n");
		definitions_map.put("queryItem", "item:Item = Item.allInstance()->any(ite:Item | ite.Barcode = barcode)\r\r\n");
		definitions_map.put("modifyItem", "item:Item = Item.allInstance()->any(ite:Item | ite.Barcode = barcode)\r\r\n");
		definitions_map.put("deleteItem", "item:Item = Item.allInstance()->any(ite:Item | ite.Barcode = barcode)\r\r\n");
		definitions_map.put("createSupplier", "supplier:Supplier = Supplier.allInstance()->any(sup:Supplier | sup.Id = id)\r\r\n");
		definitions_map.put("querySupplier", "supplier:Supplier = Supplier.allInstance()->any(sup:Supplier | sup.Id = id)\r\r\n");
		definitions_map.put("modifySupplier", "supplier:Supplier = Supplier.allInstance()->any(sup:Supplier | sup.Id = id)\r\r\n");
		definitions_map.put("deleteSupplier", "supplier:Supplier = Supplier.allInstance()->any(sup:Supplier | sup.Id = id)\r\r\n");
		definitions_map.put("addItemToCheckout", "// Find Object\ritem:Item = Item.allInstance()->any(i:Item | i.ItemID = itemID)\r\r\n");
		definitions_map.put("initiateReturn", "order:Sale = Sale.allInstance()->any(od:Sale | od.OrderId = orderID)\n\ritem:Item = Item.allInstance()->any(i:Item | i.Barcode = itemID)\r\r\n");
		definitions_map.put("checkReturnEligibility", "currentRequest:ReturnRequest = ReturnRequest.allInstance()->any(rr:ReturnRequest | rr.RequestId = requestID)\r\r\n");
		definitions_map.put("reviewReturnRequest", "returnRequest:ReturnRequest = ReturnRequest.allInstance()->any(rr:ReturnRequest | rr.RequestId = requestID)\r\r\n");
		definitions_map.put("receivePurchaseOrder", "purchaseOrder:PurchaseOrder = PurchaseOrder.allInstance()->any(po:PurchaseOrder | po.OrderId = orderID)\r\r\n");
		definitions_map.put("reviewOrderDetails", "currentOrder:PurchaseOrder = PurchaseOrder.allInstance()->any(po:PurchaseOrder | po.OrderId = orderID)\r\r\n");
		
		//precondition map
		preconditions_map = new HashMap<String, String>();
		preconditions_map.put("openStore", "sto.oclIsUndefined() = false and\nsto.IsOpened = false\n");
		preconditions_map.put("closeStore", "sto.oclIsUndefined() = false and\nsto.IsOpened = true\n");
		preconditions_map.put("openCashDesk", "cd.oclIsUndefined() = false and\ncd.IsOpened = false and\nCurrentStore.oclIsUndefined() = false and\nCurrentStore.IsOpened = true\n");
		preconditions_map.put("closeCashDesk", "cd.oclIsUndefined() = false and\ncd.IsOpened = true and\nCurrentStore.oclIsUndefined() = false and\nCurrentStore.IsOpened = true\n");
		preconditions_map.put("makeNewSale", "CurrentCashDesk.oclIsUndefined() = false and\nCurrentCashDesk.IsOpened = true and\n(CurrentSale.oclIsUndefined() = true or\n(CurrentSale.oclIsUndefined() = false and\nCurrentSale.IsComplete = true\n)\n)\n");
		preconditions_map.put("enterItem", "CurrentSale.oclIsUndefined() = false and\nCurrentSale.IsComplete = false and\nitem.oclIsUndefined() = false and\nitem.StockNumber > 0\n");
		preconditions_map.put("endSale", "CurrentSale.oclIsUndefined() = false and\nCurrentSale.IsComplete = false and\nCurrentSale.IsReadytoPay = false\n");
		preconditions_map.put("makeCashPayment", "CurrentSale.oclIsUndefined() = false and\nCurrentSale.IsComplete = false and\nCurrentSale.IsReadytoPay = true and\namount >= CurrentSale.Amount\n");
		preconditions_map.put("makeCardPayment", "CurrentSale.oclIsUndefined() = false and\nCurrentSale.IsComplete = false and\nCurrentSale.IsReadytoPay = true and\nthirdPartyCardPaymentService(cardAccountNumber , expiryDate , fee)\n");
		preconditions_map.put("makeWechatPayment", "CurrentSale.oclIsUndefined() = false and\nCurrentSale.IsComplete = false and\nCurrentSale.IsReadytoPay = true and\nthirdPartyWechatPaymentService(amount , openID)\n");
		preconditions_map.put("makeAliPayment", "CurrentSale.oclIsUndefined() = false and\nCurrentSale.IsComplete = false and\nCurrentSale.IsReadytoPay = true and\nthirdPartyAliPaymentService(amount , aliPayAccount)\n");
		preconditions_map.put("thirdPartyCardPaymentService", "true");
		preconditions_map.put("thirdPartyWechatPaymentService", "true");
		preconditions_map.put("thirdPartyAliPaymentService", "true");
		preconditions_map.put("makeNewOrder", "ordp.oclIsUndefined() = true");
		preconditions_map.put("listAllOutOfStoreProducts", "true");
		preconditions_map.put("orderItem", "item.oclIsUndefined() = false");
		preconditions_map.put("chooseSupplier", "sup.oclIsUndefined() = false and\nCurrentOrderProduct.oclIsUndefined() = false\n");
		preconditions_map.put("placeOrder", "CurrentOrderProduct.oclIsUndefined() = false");
		preconditions_map.put("changePrice", "item.oclIsUndefined() = false");
		preconditions_map.put("receiveOrderedProduct", "op.oclIsUndefined() = false");
		preconditions_map.put("listSuppliers", "true");
		preconditions_map.put("showStockReports", "true");
		preconditions_map.put("createStore", "store.oclIsUndefined() = true");
		preconditions_map.put("queryStore", "store.oclIsUndefined() = false");
		preconditions_map.put("modifyStore", "store.oclIsUndefined() = false");
		preconditions_map.put("deleteStore", "store.oclIsUndefined() = false and\nStore.allInstance()->includes(store)\n");
		preconditions_map.put("createProductCatalog", "productcatalog.oclIsUndefined() = true");
		preconditions_map.put("queryProductCatalog", "productcatalog.oclIsUndefined() = false");
		preconditions_map.put("modifyProductCatalog", "productcatalog.oclIsUndefined() = false");
		preconditions_map.put("deleteProductCatalog", "productcatalog.oclIsUndefined() = false and\nProductCatalog.allInstance()->includes(productcatalog)\n");
		preconditions_map.put("createCashDesk", "cashdesk.oclIsUndefined() = true");
		preconditions_map.put("queryCashDesk", "cashdesk.oclIsUndefined() = false");
		preconditions_map.put("modifyCashDesk", "cashdesk.oclIsUndefined() = false");
		preconditions_map.put("deleteCashDesk", "cashdesk.oclIsUndefined() = false and\nCashDesk.allInstance()->includes(cashdesk)\n");
		preconditions_map.put("createCashier", "cashier.oclIsUndefined() = true");
		preconditions_map.put("queryCashier", "cashier.oclIsUndefined() = false");
		preconditions_map.put("modifyCashier", "cashier.oclIsUndefined() = false");
		preconditions_map.put("deleteCashier", "cashier.oclIsUndefined() = false and\nCashier.allInstance()->includes(cashier)\n");
		preconditions_map.put("createItem", "item.oclIsUndefined() = true");
		preconditions_map.put("queryItem", "item.oclIsUndefined() = false");
		preconditions_map.put("modifyItem", "item.oclIsUndefined() = false");
		preconditions_map.put("deleteItem", "item.oclIsUndefined() = false and\nItem.allInstance()->includes(item)\n");
		preconditions_map.put("createSupplier", "supplier.oclIsUndefined() = true");
		preconditions_map.put("querySupplier", "supplier.oclIsUndefined() = false");
		preconditions_map.put("modifySupplier", "supplier.oclIsUndefined() = false");
		preconditions_map.put("deleteSupplier", "supplier.oclIsUndefined() = false and\nSupplier.allInstance()->includes(supplier)\n");
		preconditions_map.put("addItemToCheckout", "CurrentCheckout.oclIsUndefined() = true and\nCurrentCheckout.IsComplete = false and\nitem.oclIsUndefined() = true and\nitem.StockNumber >= quantity\n");
		preconditions_map.put("selectPaymentMethod", "CurrentCheckout.oclIsUndefined() = false and\nCurrentCheckout.IsComplete = false\n");
		preconditions_map.put("processPayment", "CurrentCheckout.oclIsUndefined() = false and\nCurrentCheckout.PaymentMethod.oclIsUndefined() = false and\namount <= CurrentCheckout.Amount\n");
		preconditions_map.put("printReceipt", "CurrentCheckout.oclIsUndefined() = false and\nCurrentCheckout.IsPaid = true\n");
		preconditions_map.put("initiateReturn", "order.oclIsUndefined() = false and\nitem.oclIsUndefined() = false\n");
		preconditions_map.put("checkReturnEligibility", "CurrentReturnRequest.oclIsUndefined() = false");
		preconditions_map.put("provideReturnReason", "CurrentReturnRequest.oclIsUndefined() = false");
		preconditions_map.put("reviewReturnRequest", "returnRequest.oclIsUndefined() = false");
		preconditions_map.put("approveOrRejectReturn", "CurrentReturnRequest.oclIsUndefined() = false");
		preconditions_map.put("processRefund", "CurrentReturnRequest.oclIsUndefined() = false and\nCurrentReturnRequest.IsApproved = true\n");
		preconditions_map.put("receivePurchaseOrder", "purchaseOrder.oclIsUndefined() = false");
		preconditions_map.put("reviewOrderDetails", "CurrentReceivedOrder.oclIsUndefined() = false");
		preconditions_map.put("confirmOrderAcceptance", "CurrentReceivedOrder.oclIsUndefined() = false");
		
		//postcondition map
		postconditions_map = new HashMap<String, String>();
		postconditions_map.put("openStore", "self.CurrentStore = sto and\nsto.IsOpened = true and\nresult = true\n");
		postconditions_map.put("closeStore", "sto.IsOpened = false and\nresult = true\n");
		postconditions_map.put("openCashDesk", "self.CurrentCashDesk = cd and\ncd.IsOpened = true and\nresult = true\n");
		postconditions_map.put("closeCashDesk", "self.CurrentCashDesk = cd and\ncd.IsOpened = false and\nresult = true\n");
		postconditions_map.put("makeNewSale", "let s:Sale ins.oclIsNew() and\ns.BelongedCashDesk = CurrentCashDesk and\nCurrentCashDesk.ContainedSales->includes(s) and\ns.IsComplete = false and\ns.IsReadytoPay = false and\nSale.allInstance()->includes(s) and\nself.CurrentSale = s and\nresult = true\n");
		postconditions_map.put("enterItem", "let sli:SalesLineItem insli.oclIsNew() and\nself.CurrentSaleLine = sli and\nsli.BelongedSale = CurrentSale and\nCurrentSale.ContainedSalesLine->includes(sli) and\nsli.Quantity = quantity and\nsli.BelongedItem = item and\nitem.StockNumber = item.StockNumber@pre - quantity and\nsli.Subamount = item.Price * quantity and\nSalesLineItem.allInstance()->includes(sli) and\nresult = true\n");
		postconditions_map.put("endSale", "CurrentSale.Amount = sub.sum() and\nCurrentSale.IsReadytoPay = true and\nresult = CurrentSale.Amount\n");
		postconditions_map.put("makeCashPayment", "let cp:CashPayment incp.oclIsNew() and\ncp.AmountTendered = amount and\ncp.BelongedSale = CurrentSale and\nCurrentSale.AssoicatedPayment = cp and\nCurrentSale.Belongedstore = CurrentStore and\nCurrentStore.Sales->includes(CurrentSale) and\nCurrentSale.IsComplete = true and\nCurrentSale.Time.isEqual(Now) and\ncp.Balance = amount - CurrentSale.Amount and\nCashPayment.allInstance()->includes(cp) and\nresult = true\n");
		postconditions_map.put("makeCardPayment", "let cdp:CardPayment incdp.oclIsNew() and\ncdp.AmountTendered = fee and\ncdp.BelongedSale = CurrentSale and\nCurrentSale.AssoicatedPayment = cdp and\ncdp.CardAccountNumber = cardAccountNumber and\ncdp.ExpiryDate = expiryDate and\nCardPayment.allInstance()->includes(cdp) and\nCurrentSale.Belongedstore = CurrentStore and\nCurrentStore.Sales->includes(CurrentSale) and\nCurrentSale.IsComplete = true and\nCurrentSale.Time.isEqual(Now) and\nresult = true\n");
		postconditions_map.put("makeWechatPayment", "let wcp:WechatPayment inwcp.oclIsNew() and\nwcp.AmountTendered = amount and\nwcp.BelongedSale = CurrentSale and\nCurrentSale.AssoicatedPayment = wcp and\nwcp.OpenID = openID and\nWechatPayment.allInstance()->includes(wcp) and\nCurrentSale.Belongedstore = CurrentStore and\nCurrentStore.Sales->includes(CurrentSale) and\nCurrentSale.IsComplete = true and\nCurrentSale.Time.isEqual(Now) and\nresult = true\n");
		postconditions_map.put("makeAliPayment", "let ap:AliPayment inap.oclIsNew() and\nap.AmountTendered = amount and\nap.BelongedSale = CurrentSale and\nCurrentSale.AssoicatedPayment = ap and\nap.AlipayAccount = aliPayAccount and\nAliPayment.allInstance()->includes(ap) and\nCurrentSale.Belongedstore = CurrentStore and\nCurrentStore.Sales->includes(CurrentSale) and\nCurrentSale.IsComplete = true and\nCurrentSale.Time.isEqual(Now) and\nresult = true\n");
		postconditions_map.put("thirdPartyCardPaymentService", "result = true");
		postconditions_map.put("thirdPartyWechatPaymentService", "result = true");
		postconditions_map.put("thirdPartyAliPaymentService", "result = true");
		postconditions_map.put("makeNewOrder", "let op:OrderProduct inop.oclIsNew() and\nop.OrderStatus = OrderStatus::NEW and\nop.Id = orderid and\nop.Time.isEqual(Now) and\nOrderProduct.allInstance()->includes(op) and\nself.CurrentOrderProduct = op and\nresult = true\n");
		postconditions_map.put("listAllOutOfStoreProducts", "result = Item.allInstance()->select(item:Item | item.StockNumber = 0)");
		postconditions_map.put("orderItem", "let order:OrderEntry inorder.oclIsNew() and\norder.Quantity = quantity and\norder.subAmount = item.OrderPrice * quantity and\norder.Item = item and\nOrderEntry.allInstance()->includes(order) and\nCurrentOrderProduct.ContainedEntries->includes(order) and\nresult = true\n");
		postconditions_map.put("chooseSupplier", "CurrentOrderProduct.Supplier = sup and\nresult = true\n");
		postconditions_map.put("placeOrder", "CurrentOrderProduct.OrderStatus = OrderStatus::REQUESTED and\nCurrentOrderProduct.ContainedEntries->forAll(o:OrderEntry |\nCurrentOrderProduct.Amount = CurrentOrderProduct.Amount@pre + o.SubAmount)\nand\nresult = true\n");
		postconditions_map.put("changePrice", "item.Price = newPrice and\nresult = true\n");
		postconditions_map.put("receiveOrderedProduct", "op.OrderStatus = OrderStatus::RECEIVED and\nop.ContainedEntries->forAll(oe:OrderEntry |\noe.Item.StockNumber = oe.Item.StockNumber@pre + oe.Quantity)\nand\nresult = true\n");
		postconditions_map.put("listSuppliers", "result = Supplier.allInstance()");
		postconditions_map.put("showStockReports", "result = Item.allInstance()");
		postconditions_map.put("createStore", "let sto:Store insto.oclIsNew() and\nsto.Id = id and\nsto.Name = name and\nsto.Address = address and\nsto.IsOpened = isopened and\nStore.allInstance()->includes(sto) and\nresult = true\n");
		postconditions_map.put("queryStore", "result = store");
		postconditions_map.put("modifyStore", "store.Id = id and\nstore.Name = name and\nstore.Address = address and\nstore.IsOpened = isopened and\nresult = true\n");
		postconditions_map.put("deleteStore", "Store.allInstance()->excludes(store) and\nresult = true\n");
		postconditions_map.put("createProductCatalog", "let pro:ProductCatalog inpro.oclIsNew() and\npro.Id = id and\npro.Name = name and\nProductCatalog.allInstance()->includes(pro) and\nresult = true\n");
		postconditions_map.put("queryProductCatalog", "result = productcatalog");
		postconditions_map.put("modifyProductCatalog", "productcatalog.Id = id and\nproductcatalog.Name = name and\nresult = true\n");
		postconditions_map.put("deleteProductCatalog", "ProductCatalog.allInstance()->excludes(productcatalog) and\nresult = true\n");
		postconditions_map.put("createCashDesk", "let cas:CashDesk incas.oclIsNew() and\ncas.Id = id and\ncas.Name = name and\ncas.IsOpened = isopened and\nCashDesk.allInstance()->includes(cas) and\nresult = true\n");
		postconditions_map.put("queryCashDesk", "result = cashdesk");
		postconditions_map.put("modifyCashDesk", "cashdesk.Id = id and\ncashdesk.Name = name and\ncashdesk.IsOpened = isopened and\nresult = true\n");
		postconditions_map.put("deleteCashDesk", "CashDesk.allInstance()->excludes(cashdesk) and\nresult = true\n");
		postconditions_map.put("createCashier", "let cas:Cashier incas.oclIsNew() and\ncas.Id = id and\ncas.Name = name and\nCashier.allInstance()->includes(cas) and\nresult = true\n");
		postconditions_map.put("queryCashier", "result = cashier");
		postconditions_map.put("modifyCashier", "cashier.Id = id and\ncashier.Name = name and\nresult = true\n");
		postconditions_map.put("deleteCashier", "Cashier.allInstance()->excludes(cashier) and\nresult = true\n");
		postconditions_map.put("createItem", "let ite:Item inite.oclIsNew() and\nite.Barcode = barcode and\nite.Name = name and\nite.Price = price and\nite.StockNumber = stocknumber and\nite.OrderPrice = orderprice and\nItem.allInstance()->includes(ite) and\nresult = true\n");
		postconditions_map.put("queryItem", "result = item");
		postconditions_map.put("modifyItem", "item.Barcode = barcode and\nitem.Name = name and\nitem.Price = price and\nitem.StockNumber = stocknumber and\nitem.OrderPrice = orderprice and\nresult = true\n");
		postconditions_map.put("deleteItem", "Item.allInstance()->excludes(item) and\nresult = true\n");
		postconditions_map.put("createSupplier", "let sup:Supplier insup.oclIsNew() and\nsup.Id = id and\nsup.Name = name and\nSupplier.allInstance()->includes(sup) and\nresult = true\n");
		postconditions_map.put("querySupplier", "result = supplier");
		postconditions_map.put("modifySupplier", "supplier.Id = id and\nsupplier.Name = name and\nresult = true\n");
		postconditions_map.put("deleteSupplier", "Supplier.allInstance()->excludes(supplier) and\nresult = true\n");
		postconditions_map.put("addItemToCheckout", "let sli:SalesLineItem insli.oclIsNew() and\n// Add Links\nself.CurrentCheckoutLine = sli and\nsli.BelongedCheckout = CurrentCheckout and\nCurrentCheckout.ContainedSalesLine->includes(sli) and\nsli.BelongedItem = item and\n// Modify Attributes\nsli.Quantity = quantity and\nsli.Subamount = item.Price * quantity and\nitem.StockNumber = item.StockNumber@pre - quantity and\n// Add an Object\nSalesLineItem.allInstance()->includes(sli) and\nresult = true\n");
		postconditions_map.put("selectPaymentMethod", "// \u5047\u8BBESale\u7C7B\u4E2D\u6709PaymentMethod\u5C5E\u6027\u6765\u8BB0\u5F55\u652F\u4ED8\u65B9\u5F0F\nCurrentCheckout.PaymentMethod = paymentMethod and\nresult = true\n");
		postconditions_map.put("processPayment", "CurrentCheckout.IsPaid = true and\nCurrentCheckout.Amount = amount and\nCurrentCheckout.PaymentDetails = paymentDetails and\nresult = true\n");
		postconditions_map.put("printReceipt", "result = true");
		postconditions_map.put("initiateReturn", "let returnRequest:ReturnRequest inreturnRequest.oclIsNew() and\nreturnRequest.OrderId = order.OrderId and\nreturnRequest.ItemId = item.ItemID and\nReturnRequest.allInstance()->includes(returnRequest) and\nresult = true\n");
		postconditions_map.put("checkReturnEligibility", "result = true");
		postconditions_map.put("provideReturnReason", "CurrentReturnRequest.ReturnReason = reason and\nresult = true\n");
		postconditions_map.put("reviewReturnRequest", "returnRequest.IsReviewed = true and\nresult = true\n");
		postconditions_map.put("approveOrRejectReturn", "CurrentReturnRequest.IsApproved = isApproved and\nresult = true\n");
		postconditions_map.put("processRefund", "result = true");
		postconditions_map.put("receivePurchaseOrder", "purchaseOrder.IsReceived = true and\nresult = true\n");
		postconditions_map.put("reviewOrderDetails", "result = currentOrder");
		postconditions_map.put("confirmOrderAcceptance", "CurrentReceivedOrder.IsAccepted = isAccepted and\nresult = true\n");
		
		//service invariants map
		service_invariants_map = new LinkedHashMap<String, String>();
		
		//entity invariants map
		entity_invariants_map = new LinkedHashMap<String, String>();
		entity_invariants_map.put("Store_UniqueStoreId"," Store . allInstance() -> isUnique ( s : Store | s . Id )");
		entity_invariants_map.put("ProductCatalog_UniqueProductCatalogId"," ProductCatalog . allInstance() -> isUnique ( p : ProductCatalog | p . Id )");
		entity_invariants_map.put("CashDesk_UniqueCashDeskId"," CashDesk . allInstance() -> isUnique ( c : CashDesk | c . Id )");
		entity_invariants_map.put("Sale_AmountGreatAndEqualZero"," Amount >= 0");
		entity_invariants_map.put("Cashier_UniqueCashierID"," Cashier . allInstance() -> isUnique ( c : Cashier | c . Id )");
		entity_invariants_map.put("Item_UniqueBarcode"," Item . allInstance() -> isUnique ( u : Item | u . Barcode )");
		entity_invariants_map.put("Item_PriceGreatThanEqualZero"," Price >= 0");
		entity_invariants_map.put("Item_StockNumberGreatThanEqualZero"," StockNumber >= 0");
		entity_invariants_map.put("CashPayment_BalanceGreatAndEqualZero"," Balance >= 0");
		entity_invariants_map.put("Supplier_UniqueSupplier"," Supplier . allInstance() -> isUnique ( s : Supplier | s . Id )");
		entity_invariants_map.put("Customer_UniqueCustomerId"," Customer . allInstance() -> isUnique ( c : Customer | c . Id )");
		entity_invariants_map.put("ReturnRequest_UniqueReturnRequestId"," ReturnRequest . allInstance() -> isUnique ( rr : ReturnRequest | rr . RequestId )");
		entity_invariants_map.put("SurveillanceDevice_UniqueSurveillanceDeviceId"," SurveillanceDevice . allInstance() -> isUnique ( sd : SurveillanceDevice | sd . DeviceId )");
		entity_invariants_map.put("SalesDataAnalysisResult_UniqueAnalysisId"," SalesDataAnalysisResult . allInstance() -> isUnique ( sdar : SalesDataAnalysisResult | sdar . AnalysisId )");
		entity_invariants_map.put("PurchaseOrder_UniquePurchaseOrderId"," PurchaseOrder . allInstance() -> isUnique ( po : PurchaseOrder | po . OrderId )");
		entity_invariants_map.put("RecommendedProduct_UniqueRecommendationId"," RecommendedProduct . allInstance() -> isUnique ( rp : RecommendedProduct | rp . RecommendationId )");
		entity_invariants_map.put("Administrator_UniqueAdministratorId"," Administrator . allInstance() -> isUnique ( a : Administrator | a . Id )");
		
	}
	
	public void generatOperationPane() {
		
		 operationPanels = new LinkedHashMap<String, GridPane>();
		
		 // ==================== GridPane_openStore ====================
		 GridPane openStore = new GridPane();
		 openStore.setHgap(4);
		 openStore.setVgap(6);
		 openStore.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> openStore_content = openStore.getChildren();
		 Label openStore_storeID_label = new Label("storeID:");
		 openStore_storeID_label.setMinWidth(Region.USE_PREF_SIZE);
		 openStore_content.add(openStore_storeID_label);
		 GridPane.setConstraints(openStore_storeID_label, 0, 0);
		 
		 openStore_storeID_t = new TextField();
		 openStore_content.add(openStore_storeID_t);
		 openStore_storeID_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(openStore_storeID_t, 1, 0);
		 operationPanels.put("openStore", openStore);
		 
		 // ==================== GridPane_closeStore ====================
		 GridPane closeStore = new GridPane();
		 closeStore.setHgap(4);
		 closeStore.setVgap(6);
		 closeStore.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> closeStore_content = closeStore.getChildren();
		 Label closeStore_storeID_label = new Label("storeID:");
		 closeStore_storeID_label.setMinWidth(Region.USE_PREF_SIZE);
		 closeStore_content.add(closeStore_storeID_label);
		 GridPane.setConstraints(closeStore_storeID_label, 0, 0);
		 
		 closeStore_storeID_t = new TextField();
		 closeStore_content.add(closeStore_storeID_t);
		 closeStore_storeID_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(closeStore_storeID_t, 1, 0);
		 operationPanels.put("closeStore", closeStore);
		 
		 // ==================== GridPane_openCashDesk ====================
		 GridPane openCashDesk = new GridPane();
		 openCashDesk.setHgap(4);
		 openCashDesk.setVgap(6);
		 openCashDesk.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> openCashDesk_content = openCashDesk.getChildren();
		 Label openCashDesk_cashDeskID_label = new Label("cashDeskID:");
		 openCashDesk_cashDeskID_label.setMinWidth(Region.USE_PREF_SIZE);
		 openCashDesk_content.add(openCashDesk_cashDeskID_label);
		 GridPane.setConstraints(openCashDesk_cashDeskID_label, 0, 0);
		 
		 openCashDesk_cashDeskID_t = new TextField();
		 openCashDesk_content.add(openCashDesk_cashDeskID_t);
		 openCashDesk_cashDeskID_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(openCashDesk_cashDeskID_t, 1, 0);
		 operationPanels.put("openCashDesk", openCashDesk);
		 
		 // ==================== GridPane_closeCashDesk ====================
		 GridPane closeCashDesk = new GridPane();
		 closeCashDesk.setHgap(4);
		 closeCashDesk.setVgap(6);
		 closeCashDesk.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> closeCashDesk_content = closeCashDesk.getChildren();
		 Label closeCashDesk_cashDeskID_label = new Label("cashDeskID:");
		 closeCashDesk_cashDeskID_label.setMinWidth(Region.USE_PREF_SIZE);
		 closeCashDesk_content.add(closeCashDesk_cashDeskID_label);
		 GridPane.setConstraints(closeCashDesk_cashDeskID_label, 0, 0);
		 
		 closeCashDesk_cashDeskID_t = new TextField();
		 closeCashDesk_content.add(closeCashDesk_cashDeskID_t);
		 closeCashDesk_cashDeskID_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(closeCashDesk_cashDeskID_t, 1, 0);
		 operationPanels.put("closeCashDesk", closeCashDesk);
		 
		 // ==================== GridPane_makeNewSale ====================
		 GridPane makeNewSale = new GridPane();
		 makeNewSale.setHgap(4);
		 makeNewSale.setVgap(6);
		 makeNewSale.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> makeNewSale_content = makeNewSale.getChildren();
		 Label makeNewSale_label = new Label("This operation is no intput parameters..");
		 makeNewSale_label.setMinWidth(Region.USE_PREF_SIZE);
		 makeNewSale_content.add(makeNewSale_label);
		 GridPane.setConstraints(makeNewSale_label, 0, 0);
		 operationPanels.put("makeNewSale", makeNewSale);
		 
		 // ==================== GridPane_enterItem ====================
		 GridPane enterItem = new GridPane();
		 enterItem.setHgap(4);
		 enterItem.setVgap(6);
		 enterItem.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> enterItem_content = enterItem.getChildren();
		 Label enterItem_barcode_label = new Label("barcode:");
		 enterItem_barcode_label.setMinWidth(Region.USE_PREF_SIZE);
		 enterItem_content.add(enterItem_barcode_label);
		 GridPane.setConstraints(enterItem_barcode_label, 0, 0);
		 
		 enterItem_barcode_t = new TextField();
		 enterItem_content.add(enterItem_barcode_t);
		 enterItem_barcode_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(enterItem_barcode_t, 1, 0);
		 Label enterItem_quantity_label = new Label("quantity:");
		 enterItem_quantity_label.setMinWidth(Region.USE_PREF_SIZE);
		 enterItem_content.add(enterItem_quantity_label);
		 GridPane.setConstraints(enterItem_quantity_label, 0, 1);
		 
		 enterItem_quantity_t = new TextField();
		 enterItem_content.add(enterItem_quantity_t);
		 enterItem_quantity_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(enterItem_quantity_t, 1, 1);
		 operationPanels.put("enterItem", enterItem);
		 
		 // ==================== GridPane_endSale ====================
		 GridPane endSale = new GridPane();
		 endSale.setHgap(4);
		 endSale.setVgap(6);
		 endSale.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> endSale_content = endSale.getChildren();
		 Label endSale_label = new Label("This operation is no intput parameters..");
		 endSale_label.setMinWidth(Region.USE_PREF_SIZE);
		 endSale_content.add(endSale_label);
		 GridPane.setConstraints(endSale_label, 0, 0);
		 operationPanels.put("endSale", endSale);
		 
		 // ==================== GridPane_makeCashPayment ====================
		 GridPane makeCashPayment = new GridPane();
		 makeCashPayment.setHgap(4);
		 makeCashPayment.setVgap(6);
		 makeCashPayment.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> makeCashPayment_content = makeCashPayment.getChildren();
		 Label makeCashPayment_amount_label = new Label("amount:");
		 makeCashPayment_amount_label.setMinWidth(Region.USE_PREF_SIZE);
		 makeCashPayment_content.add(makeCashPayment_amount_label);
		 GridPane.setConstraints(makeCashPayment_amount_label, 0, 0);
		 
		 makeCashPayment_amount_t = new TextField();
		 makeCashPayment_content.add(makeCashPayment_amount_t);
		 makeCashPayment_amount_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(makeCashPayment_amount_t, 1, 0);
		 operationPanels.put("makeCashPayment", makeCashPayment);
		 
		 // ==================== GridPane_makeCardPayment ====================
		 GridPane makeCardPayment = new GridPane();
		 makeCardPayment.setHgap(4);
		 makeCardPayment.setVgap(6);
		 makeCardPayment.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> makeCardPayment_content = makeCardPayment.getChildren();
		 Label makeCardPayment_cardAccountNumber_label = new Label("cardAccountNumber:");
		 makeCardPayment_cardAccountNumber_label.setMinWidth(Region.USE_PREF_SIZE);
		 makeCardPayment_content.add(makeCardPayment_cardAccountNumber_label);
		 GridPane.setConstraints(makeCardPayment_cardAccountNumber_label, 0, 0);
		 
		 makeCardPayment_cardAccountNumber_t = new TextField();
		 makeCardPayment_content.add(makeCardPayment_cardAccountNumber_t);
		 makeCardPayment_cardAccountNumber_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(makeCardPayment_cardAccountNumber_t, 1, 0);
		 Label makeCardPayment_expiryDate_label = new Label("expiryDate (yyyy-MM-dd):");
		 makeCardPayment_expiryDate_label.setMinWidth(Region.USE_PREF_SIZE);
		 makeCardPayment_content.add(makeCardPayment_expiryDate_label);
		 GridPane.setConstraints(makeCardPayment_expiryDate_label, 0, 1);
		 
		 makeCardPayment_expiryDate_t = new TextField();
		 makeCardPayment_content.add(makeCardPayment_expiryDate_t);
		 makeCardPayment_expiryDate_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(makeCardPayment_expiryDate_t, 1, 1);
		 Label makeCardPayment_fee_label = new Label("fee:");
		 makeCardPayment_fee_label.setMinWidth(Region.USE_PREF_SIZE);
		 makeCardPayment_content.add(makeCardPayment_fee_label);
		 GridPane.setConstraints(makeCardPayment_fee_label, 0, 2);
		 
		 makeCardPayment_fee_t = new TextField();
		 makeCardPayment_content.add(makeCardPayment_fee_t);
		 makeCardPayment_fee_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(makeCardPayment_fee_t, 1, 2);
		 operationPanels.put("makeCardPayment", makeCardPayment);
		 
		 // ==================== GridPane_makeWechatPayment ====================
		 GridPane makeWechatPayment = new GridPane();
		 makeWechatPayment.setHgap(4);
		 makeWechatPayment.setVgap(6);
		 makeWechatPayment.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> makeWechatPayment_content = makeWechatPayment.getChildren();
		 Label makeWechatPayment_amount_label = new Label("amount:");
		 makeWechatPayment_amount_label.setMinWidth(Region.USE_PREF_SIZE);
		 makeWechatPayment_content.add(makeWechatPayment_amount_label);
		 GridPane.setConstraints(makeWechatPayment_amount_label, 0, 0);
		 
		 makeWechatPayment_amount_t = new TextField();
		 makeWechatPayment_content.add(makeWechatPayment_amount_t);
		 makeWechatPayment_amount_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(makeWechatPayment_amount_t, 1, 0);
		 Label makeWechatPayment_openID_label = new Label("openID:");
		 makeWechatPayment_openID_label.setMinWidth(Region.USE_PREF_SIZE);
		 makeWechatPayment_content.add(makeWechatPayment_openID_label);
		 GridPane.setConstraints(makeWechatPayment_openID_label, 0, 1);
		 
		 makeWechatPayment_openID_t = new TextField();
		 makeWechatPayment_content.add(makeWechatPayment_openID_t);
		 makeWechatPayment_openID_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(makeWechatPayment_openID_t, 1, 1);
		 operationPanels.put("makeWechatPayment", makeWechatPayment);
		 
		 // ==================== GridPane_makeAliPayment ====================
		 GridPane makeAliPayment = new GridPane();
		 makeAliPayment.setHgap(4);
		 makeAliPayment.setVgap(6);
		 makeAliPayment.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> makeAliPayment_content = makeAliPayment.getChildren();
		 Label makeAliPayment_amount_label = new Label("amount:");
		 makeAliPayment_amount_label.setMinWidth(Region.USE_PREF_SIZE);
		 makeAliPayment_content.add(makeAliPayment_amount_label);
		 GridPane.setConstraints(makeAliPayment_amount_label, 0, 0);
		 
		 makeAliPayment_amount_t = new TextField();
		 makeAliPayment_content.add(makeAliPayment_amount_t);
		 makeAliPayment_amount_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(makeAliPayment_amount_t, 1, 0);
		 Label makeAliPayment_aliPayAccount_label = new Label("aliPayAccount:");
		 makeAliPayment_aliPayAccount_label.setMinWidth(Region.USE_PREF_SIZE);
		 makeAliPayment_content.add(makeAliPayment_aliPayAccount_label);
		 GridPane.setConstraints(makeAliPayment_aliPayAccount_label, 0, 1);
		 
		 makeAliPayment_aliPayAccount_t = new TextField();
		 makeAliPayment_content.add(makeAliPayment_aliPayAccount_t);
		 makeAliPayment_aliPayAccount_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(makeAliPayment_aliPayAccount_t, 1, 1);
		 operationPanels.put("makeAliPayment", makeAliPayment);
		 
		 // ==================== GridPane_thirdPartyCardPaymentService ====================
		 GridPane thirdPartyCardPaymentService = new GridPane();
		 thirdPartyCardPaymentService.setHgap(4);
		 thirdPartyCardPaymentService.setVgap(6);
		 thirdPartyCardPaymentService.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> thirdPartyCardPaymentService_content = thirdPartyCardPaymentService.getChildren();
		 Label thirdPartyCardPaymentService_cardAccountNumber_label = new Label("cardAccountNumber:");
		 thirdPartyCardPaymentService_cardAccountNumber_label.setMinWidth(Region.USE_PREF_SIZE);
		 thirdPartyCardPaymentService_content.add(thirdPartyCardPaymentService_cardAccountNumber_label);
		 GridPane.setConstraints(thirdPartyCardPaymentService_cardAccountNumber_label, 0, 0);
		 
		 thirdPartyCardPaymentService_cardAccountNumber_t = new TextField();
		 thirdPartyCardPaymentService_content.add(thirdPartyCardPaymentService_cardAccountNumber_t);
		 thirdPartyCardPaymentService_cardAccountNumber_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(thirdPartyCardPaymentService_cardAccountNumber_t, 1, 0);
		 Label thirdPartyCardPaymentService_expiryDate_label = new Label("expiryDate (yyyy-MM-dd):");
		 thirdPartyCardPaymentService_expiryDate_label.setMinWidth(Region.USE_PREF_SIZE);
		 thirdPartyCardPaymentService_content.add(thirdPartyCardPaymentService_expiryDate_label);
		 GridPane.setConstraints(thirdPartyCardPaymentService_expiryDate_label, 0, 1);
		 
		 thirdPartyCardPaymentService_expiryDate_t = new TextField();
		 thirdPartyCardPaymentService_content.add(thirdPartyCardPaymentService_expiryDate_t);
		 thirdPartyCardPaymentService_expiryDate_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(thirdPartyCardPaymentService_expiryDate_t, 1, 1);
		 Label thirdPartyCardPaymentService_fee_label = new Label("fee:");
		 thirdPartyCardPaymentService_fee_label.setMinWidth(Region.USE_PREF_SIZE);
		 thirdPartyCardPaymentService_content.add(thirdPartyCardPaymentService_fee_label);
		 GridPane.setConstraints(thirdPartyCardPaymentService_fee_label, 0, 2);
		 
		 thirdPartyCardPaymentService_fee_t = new TextField();
		 thirdPartyCardPaymentService_content.add(thirdPartyCardPaymentService_fee_t);
		 thirdPartyCardPaymentService_fee_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(thirdPartyCardPaymentService_fee_t, 1, 2);
		 operationPanels.put("thirdPartyCardPaymentService", thirdPartyCardPaymentService);
		 
		 // ==================== GridPane_thirdPartyWechatPaymentService ====================
		 GridPane thirdPartyWechatPaymentService = new GridPane();
		 thirdPartyWechatPaymentService.setHgap(4);
		 thirdPartyWechatPaymentService.setVgap(6);
		 thirdPartyWechatPaymentService.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> thirdPartyWechatPaymentService_content = thirdPartyWechatPaymentService.getChildren();
		 Label thirdPartyWechatPaymentService_amount_label = new Label("amount:");
		 thirdPartyWechatPaymentService_amount_label.setMinWidth(Region.USE_PREF_SIZE);
		 thirdPartyWechatPaymentService_content.add(thirdPartyWechatPaymentService_amount_label);
		 GridPane.setConstraints(thirdPartyWechatPaymentService_amount_label, 0, 0);
		 
		 thirdPartyWechatPaymentService_amount_t = new TextField();
		 thirdPartyWechatPaymentService_content.add(thirdPartyWechatPaymentService_amount_t);
		 thirdPartyWechatPaymentService_amount_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(thirdPartyWechatPaymentService_amount_t, 1, 0);
		 Label thirdPartyWechatPaymentService_openID_label = new Label("openID:");
		 thirdPartyWechatPaymentService_openID_label.setMinWidth(Region.USE_PREF_SIZE);
		 thirdPartyWechatPaymentService_content.add(thirdPartyWechatPaymentService_openID_label);
		 GridPane.setConstraints(thirdPartyWechatPaymentService_openID_label, 0, 1);
		 
		 thirdPartyWechatPaymentService_openID_t = new TextField();
		 thirdPartyWechatPaymentService_content.add(thirdPartyWechatPaymentService_openID_t);
		 thirdPartyWechatPaymentService_openID_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(thirdPartyWechatPaymentService_openID_t, 1, 1);
		 operationPanels.put("thirdPartyWechatPaymentService", thirdPartyWechatPaymentService);
		 
		 // ==================== GridPane_thirdPartyAliPaymentService ====================
		 GridPane thirdPartyAliPaymentService = new GridPane();
		 thirdPartyAliPaymentService.setHgap(4);
		 thirdPartyAliPaymentService.setVgap(6);
		 thirdPartyAliPaymentService.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> thirdPartyAliPaymentService_content = thirdPartyAliPaymentService.getChildren();
		 Label thirdPartyAliPaymentService_amount_label = new Label("amount:");
		 thirdPartyAliPaymentService_amount_label.setMinWidth(Region.USE_PREF_SIZE);
		 thirdPartyAliPaymentService_content.add(thirdPartyAliPaymentService_amount_label);
		 GridPane.setConstraints(thirdPartyAliPaymentService_amount_label, 0, 0);
		 
		 thirdPartyAliPaymentService_amount_t = new TextField();
		 thirdPartyAliPaymentService_content.add(thirdPartyAliPaymentService_amount_t);
		 thirdPartyAliPaymentService_amount_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(thirdPartyAliPaymentService_amount_t, 1, 0);
		 Label thirdPartyAliPaymentService_aliPayAccount_label = new Label("aliPayAccount:");
		 thirdPartyAliPaymentService_aliPayAccount_label.setMinWidth(Region.USE_PREF_SIZE);
		 thirdPartyAliPaymentService_content.add(thirdPartyAliPaymentService_aliPayAccount_label);
		 GridPane.setConstraints(thirdPartyAliPaymentService_aliPayAccount_label, 0, 1);
		 
		 thirdPartyAliPaymentService_aliPayAccount_t = new TextField();
		 thirdPartyAliPaymentService_content.add(thirdPartyAliPaymentService_aliPayAccount_t);
		 thirdPartyAliPaymentService_aliPayAccount_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(thirdPartyAliPaymentService_aliPayAccount_t, 1, 1);
		 operationPanels.put("thirdPartyAliPaymentService", thirdPartyAliPaymentService);
		 
		 // ==================== GridPane_makeNewOrder ====================
		 GridPane makeNewOrder = new GridPane();
		 makeNewOrder.setHgap(4);
		 makeNewOrder.setVgap(6);
		 makeNewOrder.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> makeNewOrder_content = makeNewOrder.getChildren();
		 Label makeNewOrder_orderid_label = new Label("orderid:");
		 makeNewOrder_orderid_label.setMinWidth(Region.USE_PREF_SIZE);
		 makeNewOrder_content.add(makeNewOrder_orderid_label);
		 GridPane.setConstraints(makeNewOrder_orderid_label, 0, 0);
		 
		 makeNewOrder_orderid_t = new TextField();
		 makeNewOrder_content.add(makeNewOrder_orderid_t);
		 makeNewOrder_orderid_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(makeNewOrder_orderid_t, 1, 0);
		 operationPanels.put("makeNewOrder", makeNewOrder);
		 
		 // ==================== GridPane_listAllOutOfStoreProducts ====================
		 GridPane listAllOutOfStoreProducts = new GridPane();
		 listAllOutOfStoreProducts.setHgap(4);
		 listAllOutOfStoreProducts.setVgap(6);
		 listAllOutOfStoreProducts.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> listAllOutOfStoreProducts_content = listAllOutOfStoreProducts.getChildren();
		 Label listAllOutOfStoreProducts_label = new Label("This operation is no intput parameters..");
		 listAllOutOfStoreProducts_label.setMinWidth(Region.USE_PREF_SIZE);
		 listAllOutOfStoreProducts_content.add(listAllOutOfStoreProducts_label);
		 GridPane.setConstraints(listAllOutOfStoreProducts_label, 0, 0);
		 operationPanels.put("listAllOutOfStoreProducts", listAllOutOfStoreProducts);
		 
		 // ==================== GridPane_orderItem ====================
		 GridPane orderItem = new GridPane();
		 orderItem.setHgap(4);
		 orderItem.setVgap(6);
		 orderItem.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> orderItem_content = orderItem.getChildren();
		 Label orderItem_barcode_label = new Label("barcode:");
		 orderItem_barcode_label.setMinWidth(Region.USE_PREF_SIZE);
		 orderItem_content.add(orderItem_barcode_label);
		 GridPane.setConstraints(orderItem_barcode_label, 0, 0);
		 
		 orderItem_barcode_t = new TextField();
		 orderItem_content.add(orderItem_barcode_t);
		 orderItem_barcode_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(orderItem_barcode_t, 1, 0);
		 Label orderItem_quantity_label = new Label("quantity:");
		 orderItem_quantity_label.setMinWidth(Region.USE_PREF_SIZE);
		 orderItem_content.add(orderItem_quantity_label);
		 GridPane.setConstraints(orderItem_quantity_label, 0, 1);
		 
		 orderItem_quantity_t = new TextField();
		 orderItem_content.add(orderItem_quantity_t);
		 orderItem_quantity_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(orderItem_quantity_t, 1, 1);
		 operationPanels.put("orderItem", orderItem);
		 
		 // ==================== GridPane_chooseSupplier ====================
		 GridPane chooseSupplier = new GridPane();
		 chooseSupplier.setHgap(4);
		 chooseSupplier.setVgap(6);
		 chooseSupplier.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> chooseSupplier_content = chooseSupplier.getChildren();
		 Label chooseSupplier_supplierID_label = new Label("supplierID:");
		 chooseSupplier_supplierID_label.setMinWidth(Region.USE_PREF_SIZE);
		 chooseSupplier_content.add(chooseSupplier_supplierID_label);
		 GridPane.setConstraints(chooseSupplier_supplierID_label, 0, 0);
		 
		 chooseSupplier_supplierID_t = new TextField();
		 chooseSupplier_content.add(chooseSupplier_supplierID_t);
		 chooseSupplier_supplierID_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(chooseSupplier_supplierID_t, 1, 0);
		 operationPanels.put("chooseSupplier", chooseSupplier);
		 
		 // ==================== GridPane_placeOrder ====================
		 GridPane placeOrder = new GridPane();
		 placeOrder.setHgap(4);
		 placeOrder.setVgap(6);
		 placeOrder.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> placeOrder_content = placeOrder.getChildren();
		 Label placeOrder_label = new Label("This operation is no intput parameters..");
		 placeOrder_label.setMinWidth(Region.USE_PREF_SIZE);
		 placeOrder_content.add(placeOrder_label);
		 GridPane.setConstraints(placeOrder_label, 0, 0);
		 operationPanels.put("placeOrder", placeOrder);
		 
		 // ==================== GridPane_changePrice ====================
		 GridPane changePrice = new GridPane();
		 changePrice.setHgap(4);
		 changePrice.setVgap(6);
		 changePrice.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> changePrice_content = changePrice.getChildren();
		 Label changePrice_barcode_label = new Label("barcode:");
		 changePrice_barcode_label.setMinWidth(Region.USE_PREF_SIZE);
		 changePrice_content.add(changePrice_barcode_label);
		 GridPane.setConstraints(changePrice_barcode_label, 0, 0);
		 
		 changePrice_barcode_t = new TextField();
		 changePrice_content.add(changePrice_barcode_t);
		 changePrice_barcode_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(changePrice_barcode_t, 1, 0);
		 Label changePrice_newPrice_label = new Label("newPrice:");
		 changePrice_newPrice_label.setMinWidth(Region.USE_PREF_SIZE);
		 changePrice_content.add(changePrice_newPrice_label);
		 GridPane.setConstraints(changePrice_newPrice_label, 0, 1);
		 
		 changePrice_newPrice_t = new TextField();
		 changePrice_content.add(changePrice_newPrice_t);
		 changePrice_newPrice_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(changePrice_newPrice_t, 1, 1);
		 operationPanels.put("changePrice", changePrice);
		 
		 // ==================== GridPane_receiveOrderedProduct ====================
		 GridPane receiveOrderedProduct = new GridPane();
		 receiveOrderedProduct.setHgap(4);
		 receiveOrderedProduct.setVgap(6);
		 receiveOrderedProduct.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> receiveOrderedProduct_content = receiveOrderedProduct.getChildren();
		 Label receiveOrderedProduct_orderID_label = new Label("orderID:");
		 receiveOrderedProduct_orderID_label.setMinWidth(Region.USE_PREF_SIZE);
		 receiveOrderedProduct_content.add(receiveOrderedProduct_orderID_label);
		 GridPane.setConstraints(receiveOrderedProduct_orderID_label, 0, 0);
		 
		 receiveOrderedProduct_orderID_t = new TextField();
		 receiveOrderedProduct_content.add(receiveOrderedProduct_orderID_t);
		 receiveOrderedProduct_orderID_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(receiveOrderedProduct_orderID_t, 1, 0);
		 operationPanels.put("receiveOrderedProduct", receiveOrderedProduct);
		 
		 // ==================== GridPane_listSuppliers ====================
		 GridPane listSuppliers = new GridPane();
		 listSuppliers.setHgap(4);
		 listSuppliers.setVgap(6);
		 listSuppliers.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> listSuppliers_content = listSuppliers.getChildren();
		 Label listSuppliers_label = new Label("This operation is no intput parameters..");
		 listSuppliers_label.setMinWidth(Region.USE_PREF_SIZE);
		 listSuppliers_content.add(listSuppliers_label);
		 GridPane.setConstraints(listSuppliers_label, 0, 0);
		 operationPanels.put("listSuppliers", listSuppliers);
		 
		 // ==================== GridPane_showStockReports ====================
		 GridPane showStockReports = new GridPane();
		 showStockReports.setHgap(4);
		 showStockReports.setVgap(6);
		 showStockReports.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> showStockReports_content = showStockReports.getChildren();
		 Label showStockReports_label = new Label("This operation is no intput parameters..");
		 showStockReports_label.setMinWidth(Region.USE_PREF_SIZE);
		 showStockReports_content.add(showStockReports_label);
		 GridPane.setConstraints(showStockReports_label, 0, 0);
		 operationPanels.put("showStockReports", showStockReports);
		 
		 // ==================== GridPane_createStore ====================
		 GridPane createStore = new GridPane();
		 createStore.setHgap(4);
		 createStore.setVgap(6);
		 createStore.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> createStore_content = createStore.getChildren();
		 Label createStore_id_label = new Label("id:");
		 createStore_id_label.setMinWidth(Region.USE_PREF_SIZE);
		 createStore_content.add(createStore_id_label);
		 GridPane.setConstraints(createStore_id_label, 0, 0);
		 
		 createStore_id_t = new TextField();
		 createStore_content.add(createStore_id_t);
		 createStore_id_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(createStore_id_t, 1, 0);
		 Label createStore_name_label = new Label("name:");
		 createStore_name_label.setMinWidth(Region.USE_PREF_SIZE);
		 createStore_content.add(createStore_name_label);
		 GridPane.setConstraints(createStore_name_label, 0, 1);
		 
		 createStore_name_t = new TextField();
		 createStore_content.add(createStore_name_t);
		 createStore_name_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(createStore_name_t, 1, 1);
		 Label createStore_address_label = new Label("address:");
		 createStore_address_label.setMinWidth(Region.USE_PREF_SIZE);
		 createStore_content.add(createStore_address_label);
		 GridPane.setConstraints(createStore_address_label, 0, 2);
		 
		 createStore_address_t = new TextField();
		 createStore_content.add(createStore_address_t);
		 createStore_address_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(createStore_address_t, 1, 2);
		 Label createStore_isopened_label = new Label("isopened:");
		 createStore_isopened_label.setMinWidth(Region.USE_PREF_SIZE);
		 createStore_content.add(createStore_isopened_label);
		 GridPane.setConstraints(createStore_isopened_label, 0, 3);
		 
		 createStore_isopened_t = new TextField();
		 createStore_content.add(createStore_isopened_t);
		 createStore_isopened_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(createStore_isopened_t, 1, 3);
		 operationPanels.put("createStore", createStore);
		 
		 // ==================== GridPane_queryStore ====================
		 GridPane queryStore = new GridPane();
		 queryStore.setHgap(4);
		 queryStore.setVgap(6);
		 queryStore.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> queryStore_content = queryStore.getChildren();
		 Label queryStore_id_label = new Label("id:");
		 queryStore_id_label.setMinWidth(Region.USE_PREF_SIZE);
		 queryStore_content.add(queryStore_id_label);
		 GridPane.setConstraints(queryStore_id_label, 0, 0);
		 
		 queryStore_id_t = new TextField();
		 queryStore_content.add(queryStore_id_t);
		 queryStore_id_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(queryStore_id_t, 1, 0);
		 operationPanels.put("queryStore", queryStore);
		 
		 // ==================== GridPane_modifyStore ====================
		 GridPane modifyStore = new GridPane();
		 modifyStore.setHgap(4);
		 modifyStore.setVgap(6);
		 modifyStore.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> modifyStore_content = modifyStore.getChildren();
		 Label modifyStore_id_label = new Label("id:");
		 modifyStore_id_label.setMinWidth(Region.USE_PREF_SIZE);
		 modifyStore_content.add(modifyStore_id_label);
		 GridPane.setConstraints(modifyStore_id_label, 0, 0);
		 
		 modifyStore_id_t = new TextField();
		 modifyStore_content.add(modifyStore_id_t);
		 modifyStore_id_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(modifyStore_id_t, 1, 0);
		 Label modifyStore_name_label = new Label("name:");
		 modifyStore_name_label.setMinWidth(Region.USE_PREF_SIZE);
		 modifyStore_content.add(modifyStore_name_label);
		 GridPane.setConstraints(modifyStore_name_label, 0, 1);
		 
		 modifyStore_name_t = new TextField();
		 modifyStore_content.add(modifyStore_name_t);
		 modifyStore_name_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(modifyStore_name_t, 1, 1);
		 Label modifyStore_address_label = new Label("address:");
		 modifyStore_address_label.setMinWidth(Region.USE_PREF_SIZE);
		 modifyStore_content.add(modifyStore_address_label);
		 GridPane.setConstraints(modifyStore_address_label, 0, 2);
		 
		 modifyStore_address_t = new TextField();
		 modifyStore_content.add(modifyStore_address_t);
		 modifyStore_address_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(modifyStore_address_t, 1, 2);
		 Label modifyStore_isopened_label = new Label("isopened:");
		 modifyStore_isopened_label.setMinWidth(Region.USE_PREF_SIZE);
		 modifyStore_content.add(modifyStore_isopened_label);
		 GridPane.setConstraints(modifyStore_isopened_label, 0, 3);
		 
		 modifyStore_isopened_t = new TextField();
		 modifyStore_content.add(modifyStore_isopened_t);
		 modifyStore_isopened_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(modifyStore_isopened_t, 1, 3);
		 operationPanels.put("modifyStore", modifyStore);
		 
		 // ==================== GridPane_deleteStore ====================
		 GridPane deleteStore = new GridPane();
		 deleteStore.setHgap(4);
		 deleteStore.setVgap(6);
		 deleteStore.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> deleteStore_content = deleteStore.getChildren();
		 Label deleteStore_id_label = new Label("id:");
		 deleteStore_id_label.setMinWidth(Region.USE_PREF_SIZE);
		 deleteStore_content.add(deleteStore_id_label);
		 GridPane.setConstraints(deleteStore_id_label, 0, 0);
		 
		 deleteStore_id_t = new TextField();
		 deleteStore_content.add(deleteStore_id_t);
		 deleteStore_id_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(deleteStore_id_t, 1, 0);
		 operationPanels.put("deleteStore", deleteStore);
		 
		 // ==================== GridPane_createProductCatalog ====================
		 GridPane createProductCatalog = new GridPane();
		 createProductCatalog.setHgap(4);
		 createProductCatalog.setVgap(6);
		 createProductCatalog.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> createProductCatalog_content = createProductCatalog.getChildren();
		 Label createProductCatalog_id_label = new Label("id:");
		 createProductCatalog_id_label.setMinWidth(Region.USE_PREF_SIZE);
		 createProductCatalog_content.add(createProductCatalog_id_label);
		 GridPane.setConstraints(createProductCatalog_id_label, 0, 0);
		 
		 createProductCatalog_id_t = new TextField();
		 createProductCatalog_content.add(createProductCatalog_id_t);
		 createProductCatalog_id_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(createProductCatalog_id_t, 1, 0);
		 Label createProductCatalog_name_label = new Label("name:");
		 createProductCatalog_name_label.setMinWidth(Region.USE_PREF_SIZE);
		 createProductCatalog_content.add(createProductCatalog_name_label);
		 GridPane.setConstraints(createProductCatalog_name_label, 0, 1);
		 
		 createProductCatalog_name_t = new TextField();
		 createProductCatalog_content.add(createProductCatalog_name_t);
		 createProductCatalog_name_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(createProductCatalog_name_t, 1, 1);
		 operationPanels.put("createProductCatalog", createProductCatalog);
		 
		 // ==================== GridPane_queryProductCatalog ====================
		 GridPane queryProductCatalog = new GridPane();
		 queryProductCatalog.setHgap(4);
		 queryProductCatalog.setVgap(6);
		 queryProductCatalog.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> queryProductCatalog_content = queryProductCatalog.getChildren();
		 Label queryProductCatalog_id_label = new Label("id:");
		 queryProductCatalog_id_label.setMinWidth(Region.USE_PREF_SIZE);
		 queryProductCatalog_content.add(queryProductCatalog_id_label);
		 GridPane.setConstraints(queryProductCatalog_id_label, 0, 0);
		 
		 queryProductCatalog_id_t = new TextField();
		 queryProductCatalog_content.add(queryProductCatalog_id_t);
		 queryProductCatalog_id_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(queryProductCatalog_id_t, 1, 0);
		 operationPanels.put("queryProductCatalog", queryProductCatalog);
		 
		 // ==================== GridPane_modifyProductCatalog ====================
		 GridPane modifyProductCatalog = new GridPane();
		 modifyProductCatalog.setHgap(4);
		 modifyProductCatalog.setVgap(6);
		 modifyProductCatalog.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> modifyProductCatalog_content = modifyProductCatalog.getChildren();
		 Label modifyProductCatalog_id_label = new Label("id:");
		 modifyProductCatalog_id_label.setMinWidth(Region.USE_PREF_SIZE);
		 modifyProductCatalog_content.add(modifyProductCatalog_id_label);
		 GridPane.setConstraints(modifyProductCatalog_id_label, 0, 0);
		 
		 modifyProductCatalog_id_t = new TextField();
		 modifyProductCatalog_content.add(modifyProductCatalog_id_t);
		 modifyProductCatalog_id_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(modifyProductCatalog_id_t, 1, 0);
		 Label modifyProductCatalog_name_label = new Label("name:");
		 modifyProductCatalog_name_label.setMinWidth(Region.USE_PREF_SIZE);
		 modifyProductCatalog_content.add(modifyProductCatalog_name_label);
		 GridPane.setConstraints(modifyProductCatalog_name_label, 0, 1);
		 
		 modifyProductCatalog_name_t = new TextField();
		 modifyProductCatalog_content.add(modifyProductCatalog_name_t);
		 modifyProductCatalog_name_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(modifyProductCatalog_name_t, 1, 1);
		 operationPanels.put("modifyProductCatalog", modifyProductCatalog);
		 
		 // ==================== GridPane_deleteProductCatalog ====================
		 GridPane deleteProductCatalog = new GridPane();
		 deleteProductCatalog.setHgap(4);
		 deleteProductCatalog.setVgap(6);
		 deleteProductCatalog.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> deleteProductCatalog_content = deleteProductCatalog.getChildren();
		 Label deleteProductCatalog_id_label = new Label("id:");
		 deleteProductCatalog_id_label.setMinWidth(Region.USE_PREF_SIZE);
		 deleteProductCatalog_content.add(deleteProductCatalog_id_label);
		 GridPane.setConstraints(deleteProductCatalog_id_label, 0, 0);
		 
		 deleteProductCatalog_id_t = new TextField();
		 deleteProductCatalog_content.add(deleteProductCatalog_id_t);
		 deleteProductCatalog_id_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(deleteProductCatalog_id_t, 1, 0);
		 operationPanels.put("deleteProductCatalog", deleteProductCatalog);
		 
		 // ==================== GridPane_createCashDesk ====================
		 GridPane createCashDesk = new GridPane();
		 createCashDesk.setHgap(4);
		 createCashDesk.setVgap(6);
		 createCashDesk.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> createCashDesk_content = createCashDesk.getChildren();
		 Label createCashDesk_id_label = new Label("id:");
		 createCashDesk_id_label.setMinWidth(Region.USE_PREF_SIZE);
		 createCashDesk_content.add(createCashDesk_id_label);
		 GridPane.setConstraints(createCashDesk_id_label, 0, 0);
		 
		 createCashDesk_id_t = new TextField();
		 createCashDesk_content.add(createCashDesk_id_t);
		 createCashDesk_id_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(createCashDesk_id_t, 1, 0);
		 Label createCashDesk_name_label = new Label("name:");
		 createCashDesk_name_label.setMinWidth(Region.USE_PREF_SIZE);
		 createCashDesk_content.add(createCashDesk_name_label);
		 GridPane.setConstraints(createCashDesk_name_label, 0, 1);
		 
		 createCashDesk_name_t = new TextField();
		 createCashDesk_content.add(createCashDesk_name_t);
		 createCashDesk_name_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(createCashDesk_name_t, 1, 1);
		 Label createCashDesk_isopened_label = new Label("isopened:");
		 createCashDesk_isopened_label.setMinWidth(Region.USE_PREF_SIZE);
		 createCashDesk_content.add(createCashDesk_isopened_label);
		 GridPane.setConstraints(createCashDesk_isopened_label, 0, 2);
		 
		 createCashDesk_isopened_t = new TextField();
		 createCashDesk_content.add(createCashDesk_isopened_t);
		 createCashDesk_isopened_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(createCashDesk_isopened_t, 1, 2);
		 operationPanels.put("createCashDesk", createCashDesk);
		 
		 // ==================== GridPane_queryCashDesk ====================
		 GridPane queryCashDesk = new GridPane();
		 queryCashDesk.setHgap(4);
		 queryCashDesk.setVgap(6);
		 queryCashDesk.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> queryCashDesk_content = queryCashDesk.getChildren();
		 Label queryCashDesk_id_label = new Label("id:");
		 queryCashDesk_id_label.setMinWidth(Region.USE_PREF_SIZE);
		 queryCashDesk_content.add(queryCashDesk_id_label);
		 GridPane.setConstraints(queryCashDesk_id_label, 0, 0);
		 
		 queryCashDesk_id_t = new TextField();
		 queryCashDesk_content.add(queryCashDesk_id_t);
		 queryCashDesk_id_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(queryCashDesk_id_t, 1, 0);
		 operationPanels.put("queryCashDesk", queryCashDesk);
		 
		 // ==================== GridPane_modifyCashDesk ====================
		 GridPane modifyCashDesk = new GridPane();
		 modifyCashDesk.setHgap(4);
		 modifyCashDesk.setVgap(6);
		 modifyCashDesk.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> modifyCashDesk_content = modifyCashDesk.getChildren();
		 Label modifyCashDesk_id_label = new Label("id:");
		 modifyCashDesk_id_label.setMinWidth(Region.USE_PREF_SIZE);
		 modifyCashDesk_content.add(modifyCashDesk_id_label);
		 GridPane.setConstraints(modifyCashDesk_id_label, 0, 0);
		 
		 modifyCashDesk_id_t = new TextField();
		 modifyCashDesk_content.add(modifyCashDesk_id_t);
		 modifyCashDesk_id_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(modifyCashDesk_id_t, 1, 0);
		 Label modifyCashDesk_name_label = new Label("name:");
		 modifyCashDesk_name_label.setMinWidth(Region.USE_PREF_SIZE);
		 modifyCashDesk_content.add(modifyCashDesk_name_label);
		 GridPane.setConstraints(modifyCashDesk_name_label, 0, 1);
		 
		 modifyCashDesk_name_t = new TextField();
		 modifyCashDesk_content.add(modifyCashDesk_name_t);
		 modifyCashDesk_name_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(modifyCashDesk_name_t, 1, 1);
		 Label modifyCashDesk_isopened_label = new Label("isopened:");
		 modifyCashDesk_isopened_label.setMinWidth(Region.USE_PREF_SIZE);
		 modifyCashDesk_content.add(modifyCashDesk_isopened_label);
		 GridPane.setConstraints(modifyCashDesk_isopened_label, 0, 2);
		 
		 modifyCashDesk_isopened_t = new TextField();
		 modifyCashDesk_content.add(modifyCashDesk_isopened_t);
		 modifyCashDesk_isopened_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(modifyCashDesk_isopened_t, 1, 2);
		 operationPanels.put("modifyCashDesk", modifyCashDesk);
		 
		 // ==================== GridPane_deleteCashDesk ====================
		 GridPane deleteCashDesk = new GridPane();
		 deleteCashDesk.setHgap(4);
		 deleteCashDesk.setVgap(6);
		 deleteCashDesk.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> deleteCashDesk_content = deleteCashDesk.getChildren();
		 Label deleteCashDesk_id_label = new Label("id:");
		 deleteCashDesk_id_label.setMinWidth(Region.USE_PREF_SIZE);
		 deleteCashDesk_content.add(deleteCashDesk_id_label);
		 GridPane.setConstraints(deleteCashDesk_id_label, 0, 0);
		 
		 deleteCashDesk_id_t = new TextField();
		 deleteCashDesk_content.add(deleteCashDesk_id_t);
		 deleteCashDesk_id_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(deleteCashDesk_id_t, 1, 0);
		 operationPanels.put("deleteCashDesk", deleteCashDesk);
		 
		 // ==================== GridPane_createCashier ====================
		 GridPane createCashier = new GridPane();
		 createCashier.setHgap(4);
		 createCashier.setVgap(6);
		 createCashier.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> createCashier_content = createCashier.getChildren();
		 Label createCashier_id_label = new Label("id:");
		 createCashier_id_label.setMinWidth(Region.USE_PREF_SIZE);
		 createCashier_content.add(createCashier_id_label);
		 GridPane.setConstraints(createCashier_id_label, 0, 0);
		 
		 createCashier_id_t = new TextField();
		 createCashier_content.add(createCashier_id_t);
		 createCashier_id_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(createCashier_id_t, 1, 0);
		 Label createCashier_name_label = new Label("name:");
		 createCashier_name_label.setMinWidth(Region.USE_PREF_SIZE);
		 createCashier_content.add(createCashier_name_label);
		 GridPane.setConstraints(createCashier_name_label, 0, 1);
		 
		 createCashier_name_t = new TextField();
		 createCashier_content.add(createCashier_name_t);
		 createCashier_name_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(createCashier_name_t, 1, 1);
		 operationPanels.put("createCashier", createCashier);
		 
		 // ==================== GridPane_queryCashier ====================
		 GridPane queryCashier = new GridPane();
		 queryCashier.setHgap(4);
		 queryCashier.setVgap(6);
		 queryCashier.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> queryCashier_content = queryCashier.getChildren();
		 Label queryCashier_id_label = new Label("id:");
		 queryCashier_id_label.setMinWidth(Region.USE_PREF_SIZE);
		 queryCashier_content.add(queryCashier_id_label);
		 GridPane.setConstraints(queryCashier_id_label, 0, 0);
		 
		 queryCashier_id_t = new TextField();
		 queryCashier_content.add(queryCashier_id_t);
		 queryCashier_id_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(queryCashier_id_t, 1, 0);
		 operationPanels.put("queryCashier", queryCashier);
		 
		 // ==================== GridPane_modifyCashier ====================
		 GridPane modifyCashier = new GridPane();
		 modifyCashier.setHgap(4);
		 modifyCashier.setVgap(6);
		 modifyCashier.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> modifyCashier_content = modifyCashier.getChildren();
		 Label modifyCashier_id_label = new Label("id:");
		 modifyCashier_id_label.setMinWidth(Region.USE_PREF_SIZE);
		 modifyCashier_content.add(modifyCashier_id_label);
		 GridPane.setConstraints(modifyCashier_id_label, 0, 0);
		 
		 modifyCashier_id_t = new TextField();
		 modifyCashier_content.add(modifyCashier_id_t);
		 modifyCashier_id_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(modifyCashier_id_t, 1, 0);
		 Label modifyCashier_name_label = new Label("name:");
		 modifyCashier_name_label.setMinWidth(Region.USE_PREF_SIZE);
		 modifyCashier_content.add(modifyCashier_name_label);
		 GridPane.setConstraints(modifyCashier_name_label, 0, 1);
		 
		 modifyCashier_name_t = new TextField();
		 modifyCashier_content.add(modifyCashier_name_t);
		 modifyCashier_name_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(modifyCashier_name_t, 1, 1);
		 operationPanels.put("modifyCashier", modifyCashier);
		 
		 // ==================== GridPane_deleteCashier ====================
		 GridPane deleteCashier = new GridPane();
		 deleteCashier.setHgap(4);
		 deleteCashier.setVgap(6);
		 deleteCashier.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> deleteCashier_content = deleteCashier.getChildren();
		 Label deleteCashier_id_label = new Label("id:");
		 deleteCashier_id_label.setMinWidth(Region.USE_PREF_SIZE);
		 deleteCashier_content.add(deleteCashier_id_label);
		 GridPane.setConstraints(deleteCashier_id_label, 0, 0);
		 
		 deleteCashier_id_t = new TextField();
		 deleteCashier_content.add(deleteCashier_id_t);
		 deleteCashier_id_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(deleteCashier_id_t, 1, 0);
		 operationPanels.put("deleteCashier", deleteCashier);
		 
		 // ==================== GridPane_createItem ====================
		 GridPane createItem = new GridPane();
		 createItem.setHgap(4);
		 createItem.setVgap(6);
		 createItem.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> createItem_content = createItem.getChildren();
		 Label createItem_barcode_label = new Label("barcode:");
		 createItem_barcode_label.setMinWidth(Region.USE_PREF_SIZE);
		 createItem_content.add(createItem_barcode_label);
		 GridPane.setConstraints(createItem_barcode_label, 0, 0);
		 
		 createItem_barcode_t = new TextField();
		 createItem_content.add(createItem_barcode_t);
		 createItem_barcode_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(createItem_barcode_t, 1, 0);
		 Label createItem_name_label = new Label("name:");
		 createItem_name_label.setMinWidth(Region.USE_PREF_SIZE);
		 createItem_content.add(createItem_name_label);
		 GridPane.setConstraints(createItem_name_label, 0, 1);
		 
		 createItem_name_t = new TextField();
		 createItem_content.add(createItem_name_t);
		 createItem_name_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(createItem_name_t, 1, 1);
		 Label createItem_price_label = new Label("price:");
		 createItem_price_label.setMinWidth(Region.USE_PREF_SIZE);
		 createItem_content.add(createItem_price_label);
		 GridPane.setConstraints(createItem_price_label, 0, 2);
		 
		 createItem_price_t = new TextField();
		 createItem_content.add(createItem_price_t);
		 createItem_price_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(createItem_price_t, 1, 2);
		 Label createItem_stocknumber_label = new Label("stocknumber:");
		 createItem_stocknumber_label.setMinWidth(Region.USE_PREF_SIZE);
		 createItem_content.add(createItem_stocknumber_label);
		 GridPane.setConstraints(createItem_stocknumber_label, 0, 3);
		 
		 createItem_stocknumber_t = new TextField();
		 createItem_content.add(createItem_stocknumber_t);
		 createItem_stocknumber_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(createItem_stocknumber_t, 1, 3);
		 Label createItem_orderprice_label = new Label("orderprice:");
		 createItem_orderprice_label.setMinWidth(Region.USE_PREF_SIZE);
		 createItem_content.add(createItem_orderprice_label);
		 GridPane.setConstraints(createItem_orderprice_label, 0, 4);
		 
		 createItem_orderprice_t = new TextField();
		 createItem_content.add(createItem_orderprice_t);
		 createItem_orderprice_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(createItem_orderprice_t, 1, 4);
		 operationPanels.put("createItem", createItem);
		 
		 // ==================== GridPane_queryItem ====================
		 GridPane queryItem = new GridPane();
		 queryItem.setHgap(4);
		 queryItem.setVgap(6);
		 queryItem.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> queryItem_content = queryItem.getChildren();
		 Label queryItem_barcode_label = new Label("barcode:");
		 queryItem_barcode_label.setMinWidth(Region.USE_PREF_SIZE);
		 queryItem_content.add(queryItem_barcode_label);
		 GridPane.setConstraints(queryItem_barcode_label, 0, 0);
		 
		 queryItem_barcode_t = new TextField();
		 queryItem_content.add(queryItem_barcode_t);
		 queryItem_barcode_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(queryItem_barcode_t, 1, 0);
		 operationPanels.put("queryItem", queryItem);
		 
		 // ==================== GridPane_modifyItem ====================
		 GridPane modifyItem = new GridPane();
		 modifyItem.setHgap(4);
		 modifyItem.setVgap(6);
		 modifyItem.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> modifyItem_content = modifyItem.getChildren();
		 Label modifyItem_barcode_label = new Label("barcode:");
		 modifyItem_barcode_label.setMinWidth(Region.USE_PREF_SIZE);
		 modifyItem_content.add(modifyItem_barcode_label);
		 GridPane.setConstraints(modifyItem_barcode_label, 0, 0);
		 
		 modifyItem_barcode_t = new TextField();
		 modifyItem_content.add(modifyItem_barcode_t);
		 modifyItem_barcode_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(modifyItem_barcode_t, 1, 0);
		 Label modifyItem_name_label = new Label("name:");
		 modifyItem_name_label.setMinWidth(Region.USE_PREF_SIZE);
		 modifyItem_content.add(modifyItem_name_label);
		 GridPane.setConstraints(modifyItem_name_label, 0, 1);
		 
		 modifyItem_name_t = new TextField();
		 modifyItem_content.add(modifyItem_name_t);
		 modifyItem_name_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(modifyItem_name_t, 1, 1);
		 Label modifyItem_price_label = new Label("price:");
		 modifyItem_price_label.setMinWidth(Region.USE_PREF_SIZE);
		 modifyItem_content.add(modifyItem_price_label);
		 GridPane.setConstraints(modifyItem_price_label, 0, 2);
		 
		 modifyItem_price_t = new TextField();
		 modifyItem_content.add(modifyItem_price_t);
		 modifyItem_price_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(modifyItem_price_t, 1, 2);
		 Label modifyItem_stocknumber_label = new Label("stocknumber:");
		 modifyItem_stocknumber_label.setMinWidth(Region.USE_PREF_SIZE);
		 modifyItem_content.add(modifyItem_stocknumber_label);
		 GridPane.setConstraints(modifyItem_stocknumber_label, 0, 3);
		 
		 modifyItem_stocknumber_t = new TextField();
		 modifyItem_content.add(modifyItem_stocknumber_t);
		 modifyItem_stocknumber_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(modifyItem_stocknumber_t, 1, 3);
		 Label modifyItem_orderprice_label = new Label("orderprice:");
		 modifyItem_orderprice_label.setMinWidth(Region.USE_PREF_SIZE);
		 modifyItem_content.add(modifyItem_orderprice_label);
		 GridPane.setConstraints(modifyItem_orderprice_label, 0, 4);
		 
		 modifyItem_orderprice_t = new TextField();
		 modifyItem_content.add(modifyItem_orderprice_t);
		 modifyItem_orderprice_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(modifyItem_orderprice_t, 1, 4);
		 operationPanels.put("modifyItem", modifyItem);
		 
		 // ==================== GridPane_deleteItem ====================
		 GridPane deleteItem = new GridPane();
		 deleteItem.setHgap(4);
		 deleteItem.setVgap(6);
		 deleteItem.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> deleteItem_content = deleteItem.getChildren();
		 Label deleteItem_barcode_label = new Label("barcode:");
		 deleteItem_barcode_label.setMinWidth(Region.USE_PREF_SIZE);
		 deleteItem_content.add(deleteItem_barcode_label);
		 GridPane.setConstraints(deleteItem_barcode_label, 0, 0);
		 
		 deleteItem_barcode_t = new TextField();
		 deleteItem_content.add(deleteItem_barcode_t);
		 deleteItem_barcode_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(deleteItem_barcode_t, 1, 0);
		 operationPanels.put("deleteItem", deleteItem);
		 
		 // ==================== GridPane_createSupplier ====================
		 GridPane createSupplier = new GridPane();
		 createSupplier.setHgap(4);
		 createSupplier.setVgap(6);
		 createSupplier.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> createSupplier_content = createSupplier.getChildren();
		 Label createSupplier_id_label = new Label("id:");
		 createSupplier_id_label.setMinWidth(Region.USE_PREF_SIZE);
		 createSupplier_content.add(createSupplier_id_label);
		 GridPane.setConstraints(createSupplier_id_label, 0, 0);
		 
		 createSupplier_id_t = new TextField();
		 createSupplier_content.add(createSupplier_id_t);
		 createSupplier_id_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(createSupplier_id_t, 1, 0);
		 Label createSupplier_name_label = new Label("name:");
		 createSupplier_name_label.setMinWidth(Region.USE_PREF_SIZE);
		 createSupplier_content.add(createSupplier_name_label);
		 GridPane.setConstraints(createSupplier_name_label, 0, 1);
		 
		 createSupplier_name_t = new TextField();
		 createSupplier_content.add(createSupplier_name_t);
		 createSupplier_name_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(createSupplier_name_t, 1, 1);
		 operationPanels.put("createSupplier", createSupplier);
		 
		 // ==================== GridPane_querySupplier ====================
		 GridPane querySupplier = new GridPane();
		 querySupplier.setHgap(4);
		 querySupplier.setVgap(6);
		 querySupplier.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> querySupplier_content = querySupplier.getChildren();
		 Label querySupplier_id_label = new Label("id:");
		 querySupplier_id_label.setMinWidth(Region.USE_PREF_SIZE);
		 querySupplier_content.add(querySupplier_id_label);
		 GridPane.setConstraints(querySupplier_id_label, 0, 0);
		 
		 querySupplier_id_t = new TextField();
		 querySupplier_content.add(querySupplier_id_t);
		 querySupplier_id_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(querySupplier_id_t, 1, 0);
		 operationPanels.put("querySupplier", querySupplier);
		 
		 // ==================== GridPane_modifySupplier ====================
		 GridPane modifySupplier = new GridPane();
		 modifySupplier.setHgap(4);
		 modifySupplier.setVgap(6);
		 modifySupplier.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> modifySupplier_content = modifySupplier.getChildren();
		 Label modifySupplier_id_label = new Label("id:");
		 modifySupplier_id_label.setMinWidth(Region.USE_PREF_SIZE);
		 modifySupplier_content.add(modifySupplier_id_label);
		 GridPane.setConstraints(modifySupplier_id_label, 0, 0);
		 
		 modifySupplier_id_t = new TextField();
		 modifySupplier_content.add(modifySupplier_id_t);
		 modifySupplier_id_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(modifySupplier_id_t, 1, 0);
		 Label modifySupplier_name_label = new Label("name:");
		 modifySupplier_name_label.setMinWidth(Region.USE_PREF_SIZE);
		 modifySupplier_content.add(modifySupplier_name_label);
		 GridPane.setConstraints(modifySupplier_name_label, 0, 1);
		 
		 modifySupplier_name_t = new TextField();
		 modifySupplier_content.add(modifySupplier_name_t);
		 modifySupplier_name_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(modifySupplier_name_t, 1, 1);
		 operationPanels.put("modifySupplier", modifySupplier);
		 
		 // ==================== GridPane_deleteSupplier ====================
		 GridPane deleteSupplier = new GridPane();
		 deleteSupplier.setHgap(4);
		 deleteSupplier.setVgap(6);
		 deleteSupplier.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> deleteSupplier_content = deleteSupplier.getChildren();
		 Label deleteSupplier_id_label = new Label("id:");
		 deleteSupplier_id_label.setMinWidth(Region.USE_PREF_SIZE);
		 deleteSupplier_content.add(deleteSupplier_id_label);
		 GridPane.setConstraints(deleteSupplier_id_label, 0, 0);
		 
		 deleteSupplier_id_t = new TextField();
		 deleteSupplier_content.add(deleteSupplier_id_t);
		 deleteSupplier_id_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(deleteSupplier_id_t, 1, 0);
		 operationPanels.put("deleteSupplier", deleteSupplier);
		 
		 // ==================== GridPane_addItemToCheckout ====================
		 GridPane addItemToCheckout = new GridPane();
		 addItemToCheckout.setHgap(4);
		 addItemToCheckout.setVgap(6);
		 addItemToCheckout.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> addItemToCheckout_content = addItemToCheckout.getChildren();
		 Label addItemToCheckout_itemID_label = new Label("itemID:");
		 addItemToCheckout_itemID_label.setMinWidth(Region.USE_PREF_SIZE);
		 addItemToCheckout_content.add(addItemToCheckout_itemID_label);
		 GridPane.setConstraints(addItemToCheckout_itemID_label, 0, 0);
		 
		 addItemToCheckout_itemID_t = new TextField();
		 addItemToCheckout_content.add(addItemToCheckout_itemID_t);
		 addItemToCheckout_itemID_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(addItemToCheckout_itemID_t, 1, 0);
		 Label addItemToCheckout_quantity_label = new Label("quantity:");
		 addItemToCheckout_quantity_label.setMinWidth(Region.USE_PREF_SIZE);
		 addItemToCheckout_content.add(addItemToCheckout_quantity_label);
		 GridPane.setConstraints(addItemToCheckout_quantity_label, 0, 1);
		 
		 addItemToCheckout_quantity_t = new TextField();
		 addItemToCheckout_content.add(addItemToCheckout_quantity_t);
		 addItemToCheckout_quantity_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(addItemToCheckout_quantity_t, 1, 1);
		 operationPanels.put("addItemToCheckout", addItemToCheckout);
		 
		 // ==================== GridPane_selectPaymentMethod ====================
		 GridPane selectPaymentMethod = new GridPane();
		 selectPaymentMethod.setHgap(4);
		 selectPaymentMethod.setVgap(6);
		 selectPaymentMethod.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> selectPaymentMethod_content = selectPaymentMethod.getChildren();
		 Label selectPaymentMethod_paymentMethod_label = new Label("paymentMethod:");
		 selectPaymentMethod_paymentMethod_label.setMinWidth(Region.USE_PREF_SIZE);
		 selectPaymentMethod_content.add(selectPaymentMethod_paymentMethod_label);
		 GridPane.setConstraints(selectPaymentMethod_paymentMethod_label, 0, 0);
		 
		 selectPaymentMethod_paymentMethod_t = new TextField();
		 selectPaymentMethod_content.add(selectPaymentMethod_paymentMethod_t);
		 selectPaymentMethod_paymentMethod_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(selectPaymentMethod_paymentMethod_t, 1, 0);
		 operationPanels.put("selectPaymentMethod", selectPaymentMethod);
		 
		 // ==================== GridPane_processPayment ====================
		 GridPane processPayment = new GridPane();
		 processPayment.setHgap(4);
		 processPayment.setVgap(6);
		 processPayment.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> processPayment_content = processPayment.getChildren();
		 Label processPayment_amount_label = new Label("amount:");
		 processPayment_amount_label.setMinWidth(Region.USE_PREF_SIZE);
		 processPayment_content.add(processPayment_amount_label);
		 GridPane.setConstraints(processPayment_amount_label, 0, 0);
		 
		 processPayment_amount_t = new TextField();
		 processPayment_content.add(processPayment_amount_t);
		 processPayment_amount_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(processPayment_amount_t, 1, 0);
		 Label processPayment_paymentDetails_label = new Label("paymentDetails:");
		 processPayment_paymentDetails_label.setMinWidth(Region.USE_PREF_SIZE);
		 processPayment_content.add(processPayment_paymentDetails_label);
		 GridPane.setConstraints(processPayment_paymentDetails_label, 0, 1);
		 
		 processPayment_paymentDetails_t = new TextField();
		 processPayment_content.add(processPayment_paymentDetails_t);
		 processPayment_paymentDetails_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(processPayment_paymentDetails_t, 1, 1);
		 operationPanels.put("processPayment", processPayment);
		 
		 // ==================== GridPane_printReceipt ====================
		 GridPane printReceipt = new GridPane();
		 printReceipt.setHgap(4);
		 printReceipt.setVgap(6);
		 printReceipt.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> printReceipt_content = printReceipt.getChildren();
		 Label printReceipt_label = new Label("This operation is no intput parameters..");
		 printReceipt_label.setMinWidth(Region.USE_PREF_SIZE);
		 printReceipt_content.add(printReceipt_label);
		 GridPane.setConstraints(printReceipt_label, 0, 0);
		 operationPanels.put("printReceipt", printReceipt);
		 
		 // ==================== GridPane_initiateReturn ====================
		 GridPane initiateReturn = new GridPane();
		 initiateReturn.setHgap(4);
		 initiateReturn.setVgap(6);
		 initiateReturn.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> initiateReturn_content = initiateReturn.getChildren();
		 Label initiateReturn_orderID_label = new Label("orderID:");
		 initiateReturn_orderID_label.setMinWidth(Region.USE_PREF_SIZE);
		 initiateReturn_content.add(initiateReturn_orderID_label);
		 GridPane.setConstraints(initiateReturn_orderID_label, 0, 0);
		 
		 initiateReturn_orderID_t = new TextField();
		 initiateReturn_content.add(initiateReturn_orderID_t);
		 initiateReturn_orderID_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(initiateReturn_orderID_t, 1, 0);
		 Label initiateReturn_itemID_label = new Label("itemID:");
		 initiateReturn_itemID_label.setMinWidth(Region.USE_PREF_SIZE);
		 initiateReturn_content.add(initiateReturn_itemID_label);
		 GridPane.setConstraints(initiateReturn_itemID_label, 0, 1);
		 
		 initiateReturn_itemID_t = new TextField();
		 initiateReturn_content.add(initiateReturn_itemID_t);
		 initiateReturn_itemID_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(initiateReturn_itemID_t, 1, 1);
		 operationPanels.put("initiateReturn", initiateReturn);
		 
		 // ==================== GridPane_checkReturnEligibility ====================
		 GridPane checkReturnEligibility = new GridPane();
		 checkReturnEligibility.setHgap(4);
		 checkReturnEligibility.setVgap(6);
		 checkReturnEligibility.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> checkReturnEligibility_content = checkReturnEligibility.getChildren();
		 Label checkReturnEligibility_requestID_label = new Label("requestID:");
		 checkReturnEligibility_requestID_label.setMinWidth(Region.USE_PREF_SIZE);
		 checkReturnEligibility_content.add(checkReturnEligibility_requestID_label);
		 GridPane.setConstraints(checkReturnEligibility_requestID_label, 0, 0);
		 
		 checkReturnEligibility_requestID_t = new TextField();
		 checkReturnEligibility_content.add(checkReturnEligibility_requestID_t);
		 checkReturnEligibility_requestID_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(checkReturnEligibility_requestID_t, 1, 0);
		 operationPanels.put("checkReturnEligibility", checkReturnEligibility);
		 
		 // ==================== GridPane_provideReturnReason ====================
		 GridPane provideReturnReason = new GridPane();
		 provideReturnReason.setHgap(4);
		 provideReturnReason.setVgap(6);
		 provideReturnReason.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> provideReturnReason_content = provideReturnReason.getChildren();
		 Label provideReturnReason_reason_label = new Label("reason:");
		 provideReturnReason_reason_label.setMinWidth(Region.USE_PREF_SIZE);
		 provideReturnReason_content.add(provideReturnReason_reason_label);
		 GridPane.setConstraints(provideReturnReason_reason_label, 0, 0);
		 
		 provideReturnReason_reason_t = new TextField();
		 provideReturnReason_content.add(provideReturnReason_reason_t);
		 provideReturnReason_reason_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(provideReturnReason_reason_t, 1, 0);
		 operationPanels.put("provideReturnReason", provideReturnReason);
		 
		 // ==================== GridPane_reviewReturnRequest ====================
		 GridPane reviewReturnRequest = new GridPane();
		 reviewReturnRequest.setHgap(4);
		 reviewReturnRequest.setVgap(6);
		 reviewReturnRequest.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> reviewReturnRequest_content = reviewReturnRequest.getChildren();
		 Label reviewReturnRequest_requestID_label = new Label("requestID:");
		 reviewReturnRequest_requestID_label.setMinWidth(Region.USE_PREF_SIZE);
		 reviewReturnRequest_content.add(reviewReturnRequest_requestID_label);
		 GridPane.setConstraints(reviewReturnRequest_requestID_label, 0, 0);
		 
		 reviewReturnRequest_requestID_t = new TextField();
		 reviewReturnRequest_content.add(reviewReturnRequest_requestID_t);
		 reviewReturnRequest_requestID_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(reviewReturnRequest_requestID_t, 1, 0);
		 operationPanels.put("reviewReturnRequest", reviewReturnRequest);
		 
		 // ==================== GridPane_approveOrRejectReturn ====================
		 GridPane approveOrRejectReturn = new GridPane();
		 approveOrRejectReturn.setHgap(4);
		 approveOrRejectReturn.setVgap(6);
		 approveOrRejectReturn.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> approveOrRejectReturn_content = approveOrRejectReturn.getChildren();
		 Label approveOrRejectReturn_isApproved_label = new Label("isApproved:");
		 approveOrRejectReturn_isApproved_label.setMinWidth(Region.USE_PREF_SIZE);
		 approveOrRejectReturn_content.add(approveOrRejectReturn_isApproved_label);
		 GridPane.setConstraints(approveOrRejectReturn_isApproved_label, 0, 0);
		 
		 approveOrRejectReturn_isApproved_t = new TextField();
		 approveOrRejectReturn_content.add(approveOrRejectReturn_isApproved_t);
		 approveOrRejectReturn_isApproved_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(approveOrRejectReturn_isApproved_t, 1, 0);
		 operationPanels.put("approveOrRejectReturn", approveOrRejectReturn);
		 
		 // ==================== GridPane_processRefund ====================
		 GridPane processRefund = new GridPane();
		 processRefund.setHgap(4);
		 processRefund.setVgap(6);
		 processRefund.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> processRefund_content = processRefund.getChildren();
		 Label processRefund_amount_label = new Label("amount:");
		 processRefund_amount_label.setMinWidth(Region.USE_PREF_SIZE);
		 processRefund_content.add(processRefund_amount_label);
		 GridPane.setConstraints(processRefund_amount_label, 0, 0);
		 
		 processRefund_amount_t = new TextField();
		 processRefund_content.add(processRefund_amount_t);
		 processRefund_amount_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(processRefund_amount_t, 1, 0);
		 Label processRefund_paymentMethod_label = new Label("paymentMethod:");
		 processRefund_paymentMethod_label.setMinWidth(Region.USE_PREF_SIZE);
		 processRefund_content.add(processRefund_paymentMethod_label);
		 GridPane.setConstraints(processRefund_paymentMethod_label, 0, 1);
		 
		 processRefund_paymentMethod_t = new TextField();
		 processRefund_content.add(processRefund_paymentMethod_t);
		 processRefund_paymentMethod_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(processRefund_paymentMethod_t, 1, 1);
		 operationPanels.put("processRefund", processRefund);
		 
		 // ==================== GridPane_receivePurchaseOrder ====================
		 GridPane receivePurchaseOrder = new GridPane();
		 receivePurchaseOrder.setHgap(4);
		 receivePurchaseOrder.setVgap(6);
		 receivePurchaseOrder.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> receivePurchaseOrder_content = receivePurchaseOrder.getChildren();
		 Label receivePurchaseOrder_orderID_label = new Label("orderID:");
		 receivePurchaseOrder_orderID_label.setMinWidth(Region.USE_PREF_SIZE);
		 receivePurchaseOrder_content.add(receivePurchaseOrder_orderID_label);
		 GridPane.setConstraints(receivePurchaseOrder_orderID_label, 0, 0);
		 
		 receivePurchaseOrder_orderID_t = new TextField();
		 receivePurchaseOrder_content.add(receivePurchaseOrder_orderID_t);
		 receivePurchaseOrder_orderID_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(receivePurchaseOrder_orderID_t, 1, 0);
		 operationPanels.put("receivePurchaseOrder", receivePurchaseOrder);
		 
		 // ==================== GridPane_reviewOrderDetails ====================
		 GridPane reviewOrderDetails = new GridPane();
		 reviewOrderDetails.setHgap(4);
		 reviewOrderDetails.setVgap(6);
		 reviewOrderDetails.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> reviewOrderDetails_content = reviewOrderDetails.getChildren();
		 Label reviewOrderDetails_orderID_label = new Label("orderID:");
		 reviewOrderDetails_orderID_label.setMinWidth(Region.USE_PREF_SIZE);
		 reviewOrderDetails_content.add(reviewOrderDetails_orderID_label);
		 GridPane.setConstraints(reviewOrderDetails_orderID_label, 0, 0);
		 
		 reviewOrderDetails_orderID_t = new TextField();
		 reviewOrderDetails_content.add(reviewOrderDetails_orderID_t);
		 reviewOrderDetails_orderID_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(reviewOrderDetails_orderID_t, 1, 0);
		 operationPanels.put("reviewOrderDetails", reviewOrderDetails);
		 
		 // ==================== GridPane_confirmOrderAcceptance ====================
		 GridPane confirmOrderAcceptance = new GridPane();
		 confirmOrderAcceptance.setHgap(4);
		 confirmOrderAcceptance.setVgap(6);
		 confirmOrderAcceptance.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> confirmOrderAcceptance_content = confirmOrderAcceptance.getChildren();
		 Label confirmOrderAcceptance_isAccepted_label = new Label("isAccepted:");
		 confirmOrderAcceptance_isAccepted_label.setMinWidth(Region.USE_PREF_SIZE);
		 confirmOrderAcceptance_content.add(confirmOrderAcceptance_isAccepted_label);
		 GridPane.setConstraints(confirmOrderAcceptance_isAccepted_label, 0, 0);
		 
		 confirmOrderAcceptance_isAccepted_t = new TextField();
		 confirmOrderAcceptance_content.add(confirmOrderAcceptance_isAccepted_t);
		 confirmOrderAcceptance_isAccepted_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(confirmOrderAcceptance_isAccepted_t, 1, 0);
		 operationPanels.put("confirmOrderAcceptance", confirmOrderAcceptance);
		 
	}	

	public void actorTreeViewBinding() {
		
		 
		
		
		 
		TreeItem<String> treeRootcashier = new TreeItem<String>("Root node");
			TreeItem<String> subTreeRoot_processSale = new TreeItem<String>("processSale");
			subTreeRoot_processSale.getChildren().addAll(Arrays.asList(			 		    
					 	new TreeItem<String>("makeNewSale"),
					 	new TreeItem<String>("enterItem"),
					 	new TreeItem<String>("endSale"),
					 	new TreeItem<String>("makeCashPayment"),
					 	new TreeItem<String>("makeCardPayment"),
					 	new TreeItem<String>("makeWechatPayment"),
					 	new TreeItem<String>("makeAliPayment")
				 		));	
		
		treeRootcashier.getChildren().addAll(Arrays.asList(
			subTreeRoot_processSale,
			new TreeItem<String>("openCashDesk"),
			new TreeItem<String>("closeCashDesk")
					));
		
		treeRootcashier.setExpanded(true);

		actor_treeview_cashier.setShowRoot(false);
		actor_treeview_cashier.setRoot(treeRootcashier);
		
		//TreeView click, then open the GridPane for inputing parameters
		actor_treeview_cashier.getSelectionModel().selectedItemProperty().addListener(
						 (observable, oldValue, newValue) -> { 
						 								
						 							 //clear the previous return
													 operation_return_pane.setContent(new Label());
													 
						 							 clickedOp = newValue.getValue();
						 							 GridPane op = operationPanels.get(clickedOp);
						 							 VBox vb = opInvariantPanel.get(clickedOp);
						 							 
						 							 //op pannel
						 							 if (op != null) {
						 								 operation_paras.setContent(operationPanels.get(newValue.getValue()));
						 								 
						 								 ObservableList<Node> l = operationPanels.get(newValue.getValue()).getChildren();
						 								 choosenOperation = new LinkedList<TextField>();
						 								 for (Node n : l) {
						 								 	 if (n instanceof TextField) {
						 								 	 	choosenOperation.add((TextField)n);
						 								 	  }
						 								 }
						 								 
						 								 definition.setText(definitions_map.get(newValue.getValue()));
						 								 precondition.setText(preconditions_map.get(newValue.getValue()));
						 								 postcondition.setText(postconditions_map.get(newValue.getValue()));
						 								 
						 						     }
						 							 else {
						 								 Label l = new Label(newValue.getValue() + " is no contract information in requirement model.");
						 								 l.setPadding(new Insets(8, 8, 8, 8));
						 								 operation_paras.setContent(l);
						 							 }	
						 							 
						 							 //op invariants
						 							 if (vb != null) {
						 							 	ScrollPane scrollPane = new ScrollPane(vb);
						 							 	scrollPane.setFitToWidth(true);
						 							 	invariants_panes.setMaxHeight(200); 
						 							 	//all_invariant_pane.setContent(scrollPane);	
						 							 	
						 							 	invariants_panes.setContent(scrollPane);
						 							 } else {
						 							 	 Label l = new Label(newValue.getValue() + " is no related invariants");
						 							     l.setPadding(new Insets(8, 8, 8, 8));
						 							     invariants_panes.setContent(l);
						 							 }
						 							 
						 							 //reset pre- and post-conditions area color
						 							 precondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF ");
						 							 postcondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF");
						 							 //reset condition panel title
						 							 precondition_pane.setText("Precondition");
						 							 postcondition_pane.setText("Postcondition");
						 						} 
						 				);
		TreeItem<String> treeRootstoremanager = new TreeItem<String>("Root node");
			TreeItem<String> subTreeRoot_orderProducts = new TreeItem<String>("orderProducts");
			subTreeRoot_orderProducts.getChildren().addAll(Arrays.asList(
				 	new TreeItem<String>("makeNewOrder"),
				 	new TreeItem<String>("listAllOutOfStoreProducts"),
				 	new TreeItem<String>("orderItem"),
				 	new TreeItem<String>("chooseSupplier"),
				 	new TreeItem<String>("placeOrder")
				 	));
				
		
		treeRootstoremanager.getChildren().addAll(Arrays.asList(
			subTreeRoot_orderProducts,
			new TreeItem<String>("receiveOrderedProduct"),
			new TreeItem<String>("showStockReports"),
			new TreeItem<String>("changePrice"),
			new TreeItem<String>("listSuppliers"),
			new TreeItem<String>("openStore"),
			new TreeItem<String>("closeStore")
					));
		
		treeRootstoremanager.setExpanded(true);

		actor_treeview_storemanager.setShowRoot(false);
		actor_treeview_storemanager.setRoot(treeRootstoremanager);
		
		//TreeView click, then open the GridPane for inputing parameters
		actor_treeview_storemanager.getSelectionModel().selectedItemProperty().addListener(
						 (observable, oldValue, newValue) -> { 
						 								
						 							 //clear the previous return
													 operation_return_pane.setContent(new Label());
													 
						 							 clickedOp = newValue.getValue();
						 							 GridPane op = operationPanels.get(clickedOp);
						 							 VBox vb = opInvariantPanel.get(clickedOp);
						 							 
						 							 //op pannel
						 							 if (op != null) {
						 								 operation_paras.setContent(operationPanels.get(newValue.getValue()));
						 								 
						 								 ObservableList<Node> l = operationPanels.get(newValue.getValue()).getChildren();
						 								 choosenOperation = new LinkedList<TextField>();
						 								 for (Node n : l) {
						 								 	 if (n instanceof TextField) {
						 								 	 	choosenOperation.add((TextField)n);
						 								 	  }
						 								 }
						 								 
						 								 definition.setText(definitions_map.get(newValue.getValue()));
						 								 precondition.setText(preconditions_map.get(newValue.getValue()));
						 								 postcondition.setText(postconditions_map.get(newValue.getValue()));
						 								 
						 						     }
						 							 else {
						 								 Label l = new Label(newValue.getValue() + " is no contract information in requirement model.");
						 								 l.setPadding(new Insets(8, 8, 8, 8));
						 								 operation_paras.setContent(l);
						 							 }	
						 							 
						 							 //op invariants
						 							 if (vb != null) {
						 							 	ScrollPane scrollPane = new ScrollPane(vb);
						 							 	scrollPane.setFitToWidth(true);
						 							 	invariants_panes.setMaxHeight(200); 
						 							 	//all_invariant_pane.setContent(scrollPane);	
						 							 	
						 							 	invariants_panes.setContent(scrollPane);
						 							 } else {
						 							 	 Label l = new Label(newValue.getValue() + " is no related invariants");
						 							     l.setPadding(new Insets(8, 8, 8, 8));
						 							     invariants_panes.setContent(l);
						 							 }
						 							 
						 							 //reset pre- and post-conditions area color
						 							 precondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF ");
						 							 postcondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF");
						 							 //reset condition panel title
						 							 precondition_pane.setText("Precondition");
						 							 postcondition_pane.setText("Postcondition");
						 						} 
						 				);
		TreeItem<String> treeRootadministrator = new TreeItem<String>("Root node");
			TreeItem<String> subTreeRoot_manageStore = new TreeItem<String>("manageStore");
			subTreeRoot_manageStore.getChildren().addAll(Arrays.asList(
				 	new TreeItem<String>("createStore"),
				 	new TreeItem<String>("queryStore"),
				 	new TreeItem<String>("modifyStore"),
				 	new TreeItem<String>("deleteStore")
				 	));
				
			TreeItem<String> subTreeRoot_manageProductCatalog = new TreeItem<String>("manageProductCatalog");
			subTreeRoot_manageProductCatalog.getChildren().addAll(Arrays.asList(
				 	new TreeItem<String>("createProductCatalog"),
				 	new TreeItem<String>("queryProductCatalog"),
				 	new TreeItem<String>("modifyProductCatalog"),
				 	new TreeItem<String>("deleteProductCatalog")
				 	));
				
			TreeItem<String> subTreeRoot_manageCashDesk = new TreeItem<String>("manageCashDesk");
			subTreeRoot_manageCashDesk.getChildren().addAll(Arrays.asList(
				 	new TreeItem<String>("createCashDesk"),
				 	new TreeItem<String>("queryCashDesk"),
				 	new TreeItem<String>("modifyCashDesk"),
				 	new TreeItem<String>("deleteCashDesk")
				 	));
				
			TreeItem<String> subTreeRoot_manageCashier = new TreeItem<String>("manageCashier");
			subTreeRoot_manageCashier.getChildren().addAll(Arrays.asList(
				 	new TreeItem<String>("createCashier"),
				 	new TreeItem<String>("queryCashier"),
				 	new TreeItem<String>("modifyCashier"),
				 	new TreeItem<String>("deleteCashier")
				 	));
				
			TreeItem<String> subTreeRoot_manageItem = new TreeItem<String>("manageItem");
			subTreeRoot_manageItem.getChildren().addAll(Arrays.asList(
				 	new TreeItem<String>("createItem"),
				 	new TreeItem<String>("queryItem"),
				 	new TreeItem<String>("modifyItem"),
				 	new TreeItem<String>("deleteItem")
				 	));
				
			TreeItem<String> subTreeRoot_manageSupplier = new TreeItem<String>("manageSupplier");
			subTreeRoot_manageSupplier.getChildren().addAll(Arrays.asList(
				 	new TreeItem<String>("createSupplier"),
				 	new TreeItem<String>("querySupplier"),
				 	new TreeItem<String>("modifySupplier"),
				 	new TreeItem<String>("deleteSupplier")
				 	));
				
			TreeItem<String> subTreeRoot_manageReturn = new TreeItem<String>("manageReturn");
			subTreeRoot_manageReturn.getChildren().addAll(Arrays.asList(			 		    
					 	new TreeItem<String>("reviewReturnRequest"),
					 	new TreeItem<String>("inspectReturnedItem"),
					 	new TreeItem<String>("approveOrRejectReturn"),
					 	new TreeItem<String>("processRefund")
				 		));	
		
		treeRootadministrator.getChildren().addAll(Arrays.asList(
			subTreeRoot_manageStore,
			subTreeRoot_manageProductCatalog,
			subTreeRoot_manageCashDesk,
			subTreeRoot_manageCashier,
			subTreeRoot_manageItem,
			subTreeRoot_manageSupplier,
			new TreeItem<String>("manageSurveillance"),
			subTreeRoot_manageReturn,
			new TreeItem<String>("analyzeSalesData")
					));
		
		treeRootadministrator.setExpanded(true);

		actor_treeview_administrator.setShowRoot(false);
		actor_treeview_administrator.setRoot(treeRootadministrator);
		
		//TreeView click, then open the GridPane for inputing parameters
		actor_treeview_administrator.getSelectionModel().selectedItemProperty().addListener(
						 (observable, oldValue, newValue) -> { 
						 								
						 							 //clear the previous return
													 operation_return_pane.setContent(new Label());
													 
						 							 clickedOp = newValue.getValue();
						 							 GridPane op = operationPanels.get(clickedOp);
						 							 VBox vb = opInvariantPanel.get(clickedOp);
						 							 
						 							 //op pannel
						 							 if (op != null) {
						 								 operation_paras.setContent(operationPanels.get(newValue.getValue()));
						 								 
						 								 ObservableList<Node> l = operationPanels.get(newValue.getValue()).getChildren();
						 								 choosenOperation = new LinkedList<TextField>();
						 								 for (Node n : l) {
						 								 	 if (n instanceof TextField) {
						 								 	 	choosenOperation.add((TextField)n);
						 								 	  }
						 								 }
						 								 
						 								 definition.setText(definitions_map.get(newValue.getValue()));
						 								 precondition.setText(preconditions_map.get(newValue.getValue()));
						 								 postcondition.setText(postconditions_map.get(newValue.getValue()));
						 								 
						 						     }
						 							 else {
						 								 Label l = new Label(newValue.getValue() + " is no contract information in requirement model.");
						 								 l.setPadding(new Insets(8, 8, 8, 8));
						 								 operation_paras.setContent(l);
						 							 }	
						 							 
						 							 //op invariants
						 							 if (vb != null) {
						 							 	ScrollPane scrollPane = new ScrollPane(vb);
						 							 	scrollPane.setFitToWidth(true);
						 							 	invariants_panes.setMaxHeight(200); 
						 							 	//all_invariant_pane.setContent(scrollPane);	
						 							 	
						 							 	invariants_panes.setContent(scrollPane);
						 							 } else {
						 							 	 Label l = new Label(newValue.getValue() + " is no related invariants");
						 							     l.setPadding(new Insets(8, 8, 8, 8));
						 							     invariants_panes.setContent(l);
						 							 }
						 							 
						 							 //reset pre- and post-conditions area color
						 							 precondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF ");
						 							 postcondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF");
						 							 //reset condition panel title
						 							 precondition_pane.setText("Precondition");
						 							 postcondition_pane.setText("Postcondition");
						 						} 
						 				);
		TreeItem<String> treeRootcustomer = new TreeItem<String>("Root node");
			TreeItem<String> subTreeRoot_checkOut = new TreeItem<String>("checkOut");
			subTreeRoot_checkOut.getChildren().addAll(Arrays.asList(			 		    
					 	new TreeItem<String>("addItemToCheckout"),
					 	new TreeItem<String>("calculateTotal"),
					 	new TreeItem<String>("selectPaymentMethod"),
					 	new TreeItem<String>("processPayment"),
					 	new TreeItem<String>("printReceipt")
				 		));	
			TreeItem<String> subTreeRoot_returnProducts = new TreeItem<String>("returnProducts");
			subTreeRoot_returnProducts.getChildren().addAll(Arrays.asList(			 		    
					 	new TreeItem<String>("initiateReturn"),
					 	new TreeItem<String>("checkReturnEligibility"),
					 	new TreeItem<String>("provideReturnReason")
				 		));	
		
		treeRootcustomer.getChildren().addAll(Arrays.asList(
			new TreeItem<String>("browseProducts"),
			new TreeItem<String>("addProductsToCart"),
			subTreeRoot_checkOut,
			subTreeRoot_returnProducts
					));
		
		treeRootcustomer.setExpanded(true);

		actor_treeview_customer.setShowRoot(false);
		actor_treeview_customer.setRoot(treeRootcustomer);
		
		//TreeView click, then open the GridPane for inputing parameters
		actor_treeview_customer.getSelectionModel().selectedItemProperty().addListener(
						 (observable, oldValue, newValue) -> { 
						 								
						 							 //clear the previous return
													 operation_return_pane.setContent(new Label());
													 
						 							 clickedOp = newValue.getValue();
						 							 GridPane op = operationPanels.get(clickedOp);
						 							 VBox vb = opInvariantPanel.get(clickedOp);
						 							 
						 							 //op pannel
						 							 if (op != null) {
						 								 operation_paras.setContent(operationPanels.get(newValue.getValue()));
						 								 
						 								 ObservableList<Node> l = operationPanels.get(newValue.getValue()).getChildren();
						 								 choosenOperation = new LinkedList<TextField>();
						 								 for (Node n : l) {
						 								 	 if (n instanceof TextField) {
						 								 	 	choosenOperation.add((TextField)n);
						 								 	  }
						 								 }
						 								 
						 								 definition.setText(definitions_map.get(newValue.getValue()));
						 								 precondition.setText(preconditions_map.get(newValue.getValue()));
						 								 postcondition.setText(postconditions_map.get(newValue.getValue()));
						 								 
						 						     }
						 							 else {
						 								 Label l = new Label(newValue.getValue() + " is no contract information in requirement model.");
						 								 l.setPadding(new Insets(8, 8, 8, 8));
						 								 operation_paras.setContent(l);
						 							 }	
						 							 
						 							 //op invariants
						 							 if (vb != null) {
						 							 	ScrollPane scrollPane = new ScrollPane(vb);
						 							 	scrollPane.setFitToWidth(true);
						 							 	invariants_panes.setMaxHeight(200); 
						 							 	//all_invariant_pane.setContent(scrollPane);	
						 							 	
						 							 	invariants_panes.setContent(scrollPane);
						 							 } else {
						 							 	 Label l = new Label(newValue.getValue() + " is no related invariants");
						 							     l.setPadding(new Insets(8, 8, 8, 8));
						 							     invariants_panes.setContent(l);
						 							 }
						 							 
						 							 //reset pre- and post-conditions area color
						 							 precondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF ");
						 							 postcondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF");
						 							 //reset condition panel title
						 							 precondition_pane.setText("Precondition");
						 							 postcondition_pane.setText("Postcondition");
						 						} 
						 				);
		TreeItem<String> treeRootsupplier = new TreeItem<String>("Root node");
			TreeItem<String> subTreeRoot_acceptPurchaseOrder = new TreeItem<String>("acceptPurchaseOrder");
			subTreeRoot_acceptPurchaseOrder.getChildren().addAll(Arrays.asList(			 		    
					 	new TreeItem<String>("receivePurchaseOrder"),
					 	new TreeItem<String>("reviewOrderDetails"),
					 	new TreeItem<String>("confirmOrderAcceptance"),
					 	new TreeItem<String>("sendProducts"),
					 	new TreeItem<String>("updateOrderStatus")
				 		));	
		
		treeRootsupplier.getChildren().addAll(Arrays.asList(
			subTreeRoot_acceptPurchaseOrder,
			new TreeItem<String>("recommendNewProducts")
					));
		
		treeRootsupplier.setExpanded(true);

		actor_treeview_supplier.setShowRoot(false);
		actor_treeview_supplier.setRoot(treeRootsupplier);
		
		//TreeView click, then open the GridPane for inputing parameters
		actor_treeview_supplier.getSelectionModel().selectedItemProperty().addListener(
						 (observable, oldValue, newValue) -> { 
						 								
						 							 //clear the previous return
													 operation_return_pane.setContent(new Label());
													 
						 							 clickedOp = newValue.getValue();
						 							 GridPane op = operationPanels.get(clickedOp);
						 							 VBox vb = opInvariantPanel.get(clickedOp);
						 							 
						 							 //op pannel
						 							 if (op != null) {
						 								 operation_paras.setContent(operationPanels.get(newValue.getValue()));
						 								 
						 								 ObservableList<Node> l = operationPanels.get(newValue.getValue()).getChildren();
						 								 choosenOperation = new LinkedList<TextField>();
						 								 for (Node n : l) {
						 								 	 if (n instanceof TextField) {
						 								 	 	choosenOperation.add((TextField)n);
						 								 	  }
						 								 }
						 								 
						 								 definition.setText(definitions_map.get(newValue.getValue()));
						 								 precondition.setText(preconditions_map.get(newValue.getValue()));
						 								 postcondition.setText(postconditions_map.get(newValue.getValue()));
						 								 
						 						     }
						 							 else {
						 								 Label l = new Label(newValue.getValue() + " is no contract information in requirement model.");
						 								 l.setPadding(new Insets(8, 8, 8, 8));
						 								 operation_paras.setContent(l);
						 							 }	
						 							 
						 							 //op invariants
						 							 if (vb != null) {
						 							 	ScrollPane scrollPane = new ScrollPane(vb);
						 							 	scrollPane.setFitToWidth(true);
						 							 	invariants_panes.setMaxHeight(200); 
						 							 	//all_invariant_pane.setContent(scrollPane);	
						 							 	
						 							 	invariants_panes.setContent(scrollPane);
						 							 } else {
						 							 	 Label l = new Label(newValue.getValue() + " is no related invariants");
						 							     l.setPadding(new Insets(8, 8, 8, 8));
						 							     invariants_panes.setContent(l);
						 							 }
						 							 
						 							 //reset pre- and post-conditions area color
						 							 precondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF ");
						 							 postcondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF");
						 							 //reset condition panel title
						 							 precondition_pane.setText("Precondition");
						 							 postcondition_pane.setText("Postcondition");
						 						} 
						 				);
	}

	/**
	*    Execute Operation
	*/
	@FXML
	public void execute(ActionEvent event) {
		
		switch (clickedOp) {
		case "openStore" : openStore(); break;
		case "closeStore" : closeStore(); break;
		case "openCashDesk" : openCashDesk(); break;
		case "closeCashDesk" : closeCashDesk(); break;
		case "makeNewSale" : makeNewSale(); break;
		case "enterItem" : enterItem(); break;
		case "endSale" : endSale(); break;
		case "makeCashPayment" : makeCashPayment(); break;
		case "makeCardPayment" : makeCardPayment(); break;
		case "makeWechatPayment" : makeWechatPayment(); break;
		case "makeAliPayment" : makeAliPayment(); break;
		case "thirdPartyCardPaymentService" : thirdPartyCardPaymentService(); break;
		case "thirdPartyWechatPaymentService" : thirdPartyWechatPaymentService(); break;
		case "thirdPartyAliPaymentService" : thirdPartyAliPaymentService(); break;
		case "makeNewOrder" : makeNewOrder(); break;
		case "listAllOutOfStoreProducts" : listAllOutOfStoreProducts(); break;
		case "orderItem" : orderItem(); break;
		case "chooseSupplier" : chooseSupplier(); break;
		case "placeOrder" : placeOrder(); break;
		case "changePrice" : changePrice(); break;
		case "receiveOrderedProduct" : receiveOrderedProduct(); break;
		case "listSuppliers" : listSuppliers(); break;
		case "showStockReports" : showStockReports(); break;
		case "createStore" : createStore(); break;
		case "queryStore" : queryStore(); break;
		case "modifyStore" : modifyStore(); break;
		case "deleteStore" : deleteStore(); break;
		case "createProductCatalog" : createProductCatalog(); break;
		case "queryProductCatalog" : queryProductCatalog(); break;
		case "modifyProductCatalog" : modifyProductCatalog(); break;
		case "deleteProductCatalog" : deleteProductCatalog(); break;
		case "createCashDesk" : createCashDesk(); break;
		case "queryCashDesk" : queryCashDesk(); break;
		case "modifyCashDesk" : modifyCashDesk(); break;
		case "deleteCashDesk" : deleteCashDesk(); break;
		case "createCashier" : createCashier(); break;
		case "queryCashier" : queryCashier(); break;
		case "modifyCashier" : modifyCashier(); break;
		case "deleteCashier" : deleteCashier(); break;
		case "createItem" : createItem(); break;
		case "queryItem" : queryItem(); break;
		case "modifyItem" : modifyItem(); break;
		case "deleteItem" : deleteItem(); break;
		case "createSupplier" : createSupplier(); break;
		case "querySupplier" : querySupplier(); break;
		case "modifySupplier" : modifySupplier(); break;
		case "deleteSupplier" : deleteSupplier(); break;
		case "addItemToCheckout" : addItemToCheckout(); break;
		case "selectPaymentMethod" : selectPaymentMethod(); break;
		case "processPayment" : processPayment(); break;
		case "printReceipt" : printReceipt(); break;
		case "initiateReturn" : initiateReturn(); break;
		case "checkReturnEligibility" : checkReturnEligibility(); break;
		case "provideReturnReason" : provideReturnReason(); break;
		case "reviewReturnRequest" : reviewReturnRequest(); break;
		case "approveOrRejectReturn" : approveOrRejectReturn(); break;
		case "processRefund" : processRefund(); break;
		case "receivePurchaseOrder" : receivePurchaseOrder(); break;
		case "reviewOrderDetails" : reviewOrderDetails(); break;
		case "confirmOrderAcceptance" : confirmOrderAcceptance(); break;
		
		}
		
		System.out.println("execute buttion clicked");
		
		//checking relevant invariants
		opInvairantPanelUpdate();
	}

	/**
	*    Refresh All
	*/		
	@FXML
	public void refresh(ActionEvent event) {
		
		refreshAll();
		System.out.println("refresh all");
	}		
	
	/**
	*    Save All
	*/			
	@FXML
	public void save(ActionEvent event) {
		
		Stage stage = (Stage) mainPane.getScene().getWindow();
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save State to File");
		fileChooser.setInitialFileName("*.state");
		fileChooser.getExtensionFilters().addAll(
		         new ExtensionFilter("RMCode State File", "*.state"));
		
		File file = fileChooser.showSaveDialog(stage);
		
		if (file != null) {
			System.out.println("save state to file " + file.getAbsolutePath());				
			EntityManager.save(file);
		}
	}
	
	/**
	*    Load All
	*/			
	@FXML
	public void load(ActionEvent event) {
		
		Stage stage = (Stage) mainPane.getScene().getWindow();
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open State File");
		fileChooser.getExtensionFilters().addAll(
		         new ExtensionFilter("RMCode State File", "*.state"));
		
		File file = fileChooser.showOpenDialog(stage);
		
		if (file != null) {
			System.out.println("choose file" + file.getAbsolutePath());
			EntityManager.load(file); 
		}
		
		//refresh GUI after load data
		refreshAll();
	}
	
	
	//precondition unsat dialog
	public void preconditionUnSat() {
		
		Alert alert = new Alert(AlertType.WARNING, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(mainPane.getScene().getWindow());
        alert.getDialogPane().setContentText("Precondtion is not satisfied");
        alert.getDialogPane().setHeaderText(null);
        alert.showAndWait();	
	}
	
	//postcondition unsat dialog
	public void postconditionUnSat() {
		
		Alert alert = new Alert(AlertType.WARNING, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(mainPane.getScene().getWindow());
        alert.getDialogPane().setContentText("Postcondtion is not satisfied");
        alert.getDialogPane().setHeaderText(null);
        alert.showAndWait();	
	}

	public void thirdpartyServiceUnSat() {
		
		Alert alert = new Alert(AlertType.WARNING, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(mainPane.getScene().getWindow());
        alert.getDialogPane().setContentText("third party service is exception");
        alert.getDialogPane().setHeaderText(null);
        alert.showAndWait();	
	}		
	
	
	public void openStore() {
		
		System.out.println("execute openStore");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: openStore in service: CoCoMESystem ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(cocomesystem_service.openStore(
			Integer.valueOf(openStore_storeID_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void closeStore() {
		
		System.out.println("execute closeStore");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: closeStore in service: CoCoMESystem ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(cocomesystem_service.closeStore(
			Integer.valueOf(closeStore_storeID_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void openCashDesk() {
		
		System.out.println("execute openCashDesk");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: openCashDesk in service: CoCoMESystem ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(cocomesystem_service.openCashDesk(
			Integer.valueOf(openCashDesk_cashDeskID_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void closeCashDesk() {
		
		System.out.println("execute closeCashDesk");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: closeCashDesk in service: CoCoMESystem ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(cocomesystem_service.closeCashDesk(
			Integer.valueOf(closeCashDesk_cashDeskID_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void makeNewSale() {
		
		System.out.println("execute makeNewSale");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: makeNewSale in service: ProcessSaleService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(processsaleservice_service.makeNewSale(
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void enterItem() {
		
		System.out.println("execute enterItem");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: enterItem in service: ProcessSaleService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(processsaleservice_service.enterItem(
			Integer.valueOf(enterItem_barcode_t.getText()),
			Integer.valueOf(enterItem_quantity_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void endSale() {
		
		System.out.println("execute endSale");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: endSale in service: ProcessSaleService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(processsaleservice_service.endSale(
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void makeCashPayment() {
		
		System.out.println("execute makeCashPayment");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: makeCashPayment in service: ProcessSaleService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(processsaleservice_service.makeCashPayment(
			Float.valueOf(makeCashPayment_amount_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void makeCardPayment() {
		
		System.out.println("execute makeCardPayment");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: makeCardPayment in service: ProcessSaleService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(processsaleservice_service.makeCardPayment(
			makeCardPayment_cardAccountNumber_t.getText(),
			LocalDate.parse(makeCardPayment_expiryDate_t.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))						,
			Float.valueOf(makeCardPayment_fee_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void makeWechatPayment() {
		
		System.out.println("execute makeWechatPayment");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: makeWechatPayment in service: ProcessSaleService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(processsaleservice_service.makeWechatPayment(
			Float.valueOf(makeWechatPayment_amount_t.getText()),
			makeWechatPayment_openID_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void makeAliPayment() {
		
		System.out.println("execute makeAliPayment");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: makeAliPayment in service: ProcessSaleService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(processsaleservice_service.makeAliPayment(
			Float.valueOf(makeAliPayment_amount_t.getText()),
			makeAliPayment_aliPayAccount_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void thirdPartyCardPaymentService() {
		
		System.out.println("execute thirdPartyCardPaymentService");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: thirdPartyCardPaymentService in service: ThirdPartyServices ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(thirdpartyservices_service.thirdPartyCardPaymentService(
			thirdPartyCardPaymentService_cardAccountNumber_t.getText(),
			LocalDate.parse(thirdPartyCardPaymentService_expiryDate_t.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))						,
			Float.valueOf(thirdPartyCardPaymentService_fee_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void thirdPartyWechatPaymentService() {
		
		System.out.println("execute thirdPartyWechatPaymentService");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: thirdPartyWechatPaymentService in service: ThirdPartyServices ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(thirdpartyservices_service.thirdPartyWechatPaymentService(
			Float.valueOf(thirdPartyWechatPaymentService_amount_t.getText()),
			thirdPartyWechatPaymentService_openID_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void thirdPartyAliPaymentService() {
		
		System.out.println("execute thirdPartyAliPaymentService");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: thirdPartyAliPaymentService in service: ThirdPartyServices ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(thirdpartyservices_service.thirdPartyAliPaymentService(
			Float.valueOf(thirdPartyAliPaymentService_amount_t.getText()),
			thirdPartyAliPaymentService_aliPayAccount_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void makeNewOrder() {
		
		System.out.println("execute makeNewOrder");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: makeNewOrder in service: CoCoMEOrderProducts ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(cocomeorderproducts_service.makeNewOrder(
			Integer.valueOf(makeNewOrder_orderid_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void listAllOutOfStoreProducts() {
		
		System.out.println("execute listAllOutOfStoreProducts");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: listAllOutOfStoreProducts in service: CoCoMEOrderProducts ");
		
		try {
			//invoke op with parameters
					List<Item> result = cocomeorderproducts_service.listAllOutOfStoreProducts(
					);
				
					//binding result to GUI
					TableView<Map<String, String>> tableItem = new TableView<Map<String, String>>();
					TableColumn<Map<String, String>, String> tableItem_Barcode = new TableColumn<Map<String, String>, String>("Barcode");
					tableItem_Barcode.setMinWidth("Barcode".length()*10);
					tableItem_Barcode.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("Barcode"));
					    }
					});	
					tableItem.getColumns().add(tableItem_Barcode);
					TableColumn<Map<String, String>, String> tableItem_Name = new TableColumn<Map<String, String>, String>("Name");
					tableItem_Name.setMinWidth("Name".length()*10);
					tableItem_Name.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("Name"));
					    }
					});	
					tableItem.getColumns().add(tableItem_Name);
					TableColumn<Map<String, String>, String> tableItem_Price = new TableColumn<Map<String, String>, String>("Price");
					tableItem_Price.setMinWidth("Price".length()*10);
					tableItem_Price.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("Price"));
					    }
					});	
					tableItem.getColumns().add(tableItem_Price);
					TableColumn<Map<String, String>, String> tableItem_StockNumber = new TableColumn<Map<String, String>, String>("StockNumber");
					tableItem_StockNumber.setMinWidth("StockNumber".length()*10);
					tableItem_StockNumber.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("StockNumber"));
					    }
					});	
					tableItem.getColumns().add(tableItem_StockNumber);
					TableColumn<Map<String, String>, String> tableItem_OrderPrice = new TableColumn<Map<String, String>, String>("OrderPrice");
					tableItem_OrderPrice.setMinWidth("OrderPrice".length()*10);
					tableItem_OrderPrice.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("OrderPrice"));
					    }
					});	
					tableItem.getColumns().add(tableItem_OrderPrice);
					TableColumn<Map<String, String>, String> tableItem_ItemID = new TableColumn<Map<String, String>, String>("ItemID");
					tableItem_ItemID.setMinWidth("ItemID".length()*10);
					tableItem_ItemID.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("ItemID"));
					    }
					});	
					tableItem.getColumns().add(tableItem_ItemID);
					
					ObservableList<Map<String, String>> dataItem = FXCollections.observableArrayList();
					for (Item r : result) {
						Map<String, String> unit = new HashMap<String, String>();
						unit.put("Barcode", String.valueOf(r.getBarcode()));
						if (r.getName() != null)
							unit.put("Name", String.valueOf(r.getName()));
						else
							unit.put("Name", "");
						unit.put("Price", String.valueOf(r.getPrice()));
						unit.put("StockNumber", String.valueOf(r.getStockNumber()));
						unit.put("OrderPrice", String.valueOf(r.getOrderPrice()));
						unit.put("ItemID", String.valueOf(r.getItemID()));
						dataItem.add(unit);
					}
					
					tableItem.setItems(dataItem);
					operation_return_pane.setContent(tableItem);
				
			
			//return type is entity
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void orderItem() {
		
		System.out.println("execute orderItem");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: orderItem in service: CoCoMEOrderProducts ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(cocomeorderproducts_service.orderItem(
			Integer.valueOf(orderItem_barcode_t.getText()),
			Integer.valueOf(orderItem_quantity_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void chooseSupplier() {
		
		System.out.println("execute chooseSupplier");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: chooseSupplier in service: CoCoMEOrderProducts ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(cocomeorderproducts_service.chooseSupplier(
			Integer.valueOf(chooseSupplier_supplierID_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void placeOrder() {
		
		System.out.println("execute placeOrder");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: placeOrder in service: CoCoMEOrderProducts ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(cocomeorderproducts_service.placeOrder(
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void changePrice() {
		
		System.out.println("execute changePrice");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: changePrice in service: CoCoMESystem ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(cocomesystem_service.changePrice(
			Integer.valueOf(changePrice_barcode_t.getText()),
			Float.valueOf(changePrice_newPrice_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void receiveOrderedProduct() {
		
		System.out.println("execute receiveOrderedProduct");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: receiveOrderedProduct in service: CoCoMESystem ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(cocomesystem_service.receiveOrderedProduct(
			Integer.valueOf(receiveOrderedProduct_orderID_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void listSuppliers() {
		
		System.out.println("execute listSuppliers");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: listSuppliers in service: CoCoMESystem ");
		
		try {
			//invoke op with parameters
					List<Supplier> result = cocomesystem_service.listSuppliers(
					);
				
					//binding result to GUI
					TableView<Map<String, String>> tableSupplier = new TableView<Map<String, String>>();
					TableColumn<Map<String, String>, String> tableSupplier_Id = new TableColumn<Map<String, String>, String>("Id");
					tableSupplier_Id.setMinWidth("Id".length()*10);
					tableSupplier_Id.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("Id"));
					    }
					});	
					tableSupplier.getColumns().add(tableSupplier_Id);
					TableColumn<Map<String, String>, String> tableSupplier_Name = new TableColumn<Map<String, String>, String>("Name");
					tableSupplier_Name.setMinWidth("Name".length()*10);
					tableSupplier_Name.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("Name"));
					    }
					});	
					tableSupplier.getColumns().add(tableSupplier_Name);
					
					ObservableList<Map<String, String>> dataSupplier = FXCollections.observableArrayList();
					for (Supplier r : result) {
						Map<String, String> unit = new HashMap<String, String>();
						unit.put("Id", String.valueOf(r.getId()));
						if (r.getName() != null)
							unit.put("Name", String.valueOf(r.getName()));
						else
							unit.put("Name", "");
						dataSupplier.add(unit);
					}
					
					tableSupplier.setItems(dataSupplier);
					operation_return_pane.setContent(tableSupplier);
				
			
			//return type is entity
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void showStockReports() {
		
		System.out.println("execute showStockReports");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: showStockReports in service: CoCoMESystem ");
		
		try {
			//invoke op with parameters
					List<Item> result = cocomesystem_service.showStockReports(
					);
				
					//binding result to GUI
					TableView<Map<String, String>> tableItem = new TableView<Map<String, String>>();
					TableColumn<Map<String, String>, String> tableItem_Barcode = new TableColumn<Map<String, String>, String>("Barcode");
					tableItem_Barcode.setMinWidth("Barcode".length()*10);
					tableItem_Barcode.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("Barcode"));
					    }
					});	
					tableItem.getColumns().add(tableItem_Barcode);
					TableColumn<Map<String, String>, String> tableItem_Name = new TableColumn<Map<String, String>, String>("Name");
					tableItem_Name.setMinWidth("Name".length()*10);
					tableItem_Name.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("Name"));
					    }
					});	
					tableItem.getColumns().add(tableItem_Name);
					TableColumn<Map<String, String>, String> tableItem_Price = new TableColumn<Map<String, String>, String>("Price");
					tableItem_Price.setMinWidth("Price".length()*10);
					tableItem_Price.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("Price"));
					    }
					});	
					tableItem.getColumns().add(tableItem_Price);
					TableColumn<Map<String, String>, String> tableItem_StockNumber = new TableColumn<Map<String, String>, String>("StockNumber");
					tableItem_StockNumber.setMinWidth("StockNumber".length()*10);
					tableItem_StockNumber.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("StockNumber"));
					    }
					});	
					tableItem.getColumns().add(tableItem_StockNumber);
					TableColumn<Map<String, String>, String> tableItem_OrderPrice = new TableColumn<Map<String, String>, String>("OrderPrice");
					tableItem_OrderPrice.setMinWidth("OrderPrice".length()*10);
					tableItem_OrderPrice.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("OrderPrice"));
					    }
					});	
					tableItem.getColumns().add(tableItem_OrderPrice);
					TableColumn<Map<String, String>, String> tableItem_ItemID = new TableColumn<Map<String, String>, String>("ItemID");
					tableItem_ItemID.setMinWidth("ItemID".length()*10);
					tableItem_ItemID.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("ItemID"));
					    }
					});	
					tableItem.getColumns().add(tableItem_ItemID);
					
					ObservableList<Map<String, String>> dataItem = FXCollections.observableArrayList();
					for (Item r : result) {
						Map<String, String> unit = new HashMap<String, String>();
						unit.put("Barcode", String.valueOf(r.getBarcode()));
						if (r.getName() != null)
							unit.put("Name", String.valueOf(r.getName()));
						else
							unit.put("Name", "");
						unit.put("Price", String.valueOf(r.getPrice()));
						unit.put("StockNumber", String.valueOf(r.getStockNumber()));
						unit.put("OrderPrice", String.valueOf(r.getOrderPrice()));
						unit.put("ItemID", String.valueOf(r.getItemID()));
						dataItem.add(unit);
					}
					
					tableItem.setItems(dataItem);
					operation_return_pane.setContent(tableItem);
				
			
			//return type is entity
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void createStore() {
		
		System.out.println("execute createStore");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: createStore in service: ManageStoreCRUDService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(managestorecrudservice_service.createStore(
			Integer.valueOf(createStore_id_t.getText()),
			createStore_name_t.getText(),
			createStore_address_t.getText(),
			Boolean.valueOf(createStore_isopened_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void queryStore() {
		
		System.out.println("execute queryStore");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: queryStore in service: ManageStoreCRUDService ");
		
		try {
			//invoke op with parameters
				Store r = managestorecrudservice_service.queryStore(
				Integer.valueOf(queryStore_id_t.getText())
				);
			
				//binding result to GUI
				TableView<Map<String, String>> tableStore = new TableView<Map<String, String>>();
				TableColumn<Map<String, String>, String> tableStore_Id = new TableColumn<Map<String, String>, String>("Id");
				tableStore_Id.setMinWidth("Id".length()*10);
				tableStore_Id.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("Id"));
				    }
				});	
				tableStore.getColumns().add(tableStore_Id);
				TableColumn<Map<String, String>, String> tableStore_Name = new TableColumn<Map<String, String>, String>("Name");
				tableStore_Name.setMinWidth("Name".length()*10);
				tableStore_Name.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("Name"));
				    }
				});	
				tableStore.getColumns().add(tableStore_Name);
				TableColumn<Map<String, String>, String> tableStore_Address = new TableColumn<Map<String, String>, String>("Address");
				tableStore_Address.setMinWidth("Address".length()*10);
				tableStore_Address.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("Address"));
				    }
				});	
				tableStore.getColumns().add(tableStore_Address);
				TableColumn<Map<String, String>, String> tableStore_IsOpened = new TableColumn<Map<String, String>, String>("IsOpened");
				tableStore_IsOpened.setMinWidth("IsOpened".length()*10);
				tableStore_IsOpened.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("IsOpened"));
				    }
				});	
				tableStore.getColumns().add(tableStore_IsOpened);
				
				ObservableList<Map<String, String>> dataStore = FXCollections.observableArrayList();
				
					Map<String, String> unit = new HashMap<String, String>();
					unit.put("Id", String.valueOf(r.getId()));
					if (r.getName() != null)
						unit.put("Name", String.valueOf(r.getName()));
					else
						unit.put("Name", "");
					if (r.getAddress() != null)
						unit.put("Address", String.valueOf(r.getAddress()));
					else
						unit.put("Address", "");
					unit.put("IsOpened", String.valueOf(r.getIsOpened()));
					dataStore.add(unit);
				
				
				tableStore.setItems(dataStore);
				operation_return_pane.setContent(tableStore);					
					
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void modifyStore() {
		
		System.out.println("execute modifyStore");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: modifyStore in service: ManageStoreCRUDService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(managestorecrudservice_service.modifyStore(
			Integer.valueOf(modifyStore_id_t.getText()),
			modifyStore_name_t.getText(),
			modifyStore_address_t.getText(),
			Boolean.valueOf(modifyStore_isopened_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void deleteStore() {
		
		System.out.println("execute deleteStore");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: deleteStore in service: ManageStoreCRUDService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(managestorecrudservice_service.deleteStore(
			Integer.valueOf(deleteStore_id_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void createProductCatalog() {
		
		System.out.println("execute createProductCatalog");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: createProductCatalog in service: ManageProductCatalogCRUDService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(manageproductcatalogcrudservice_service.createProductCatalog(
			Integer.valueOf(createProductCatalog_id_t.getText()),
			createProductCatalog_name_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void queryProductCatalog() {
		
		System.out.println("execute queryProductCatalog");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: queryProductCatalog in service: ManageProductCatalogCRUDService ");
		
		try {
			//invoke op with parameters
				ProductCatalog r = manageproductcatalogcrudservice_service.queryProductCatalog(
				Integer.valueOf(queryProductCatalog_id_t.getText())
				);
			
				//binding result to GUI
				TableView<Map<String, String>> tableProductCatalog = new TableView<Map<String, String>>();
				TableColumn<Map<String, String>, String> tableProductCatalog_Id = new TableColumn<Map<String, String>, String>("Id");
				tableProductCatalog_Id.setMinWidth("Id".length()*10);
				tableProductCatalog_Id.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("Id"));
				    }
				});	
				tableProductCatalog.getColumns().add(tableProductCatalog_Id);
				TableColumn<Map<String, String>, String> tableProductCatalog_Name = new TableColumn<Map<String, String>, String>("Name");
				tableProductCatalog_Name.setMinWidth("Name".length()*10);
				tableProductCatalog_Name.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("Name"));
				    }
				});	
				tableProductCatalog.getColumns().add(tableProductCatalog_Name);
				
				ObservableList<Map<String, String>> dataProductCatalog = FXCollections.observableArrayList();
				
					Map<String, String> unit = new HashMap<String, String>();
					unit.put("Id", String.valueOf(r.getId()));
					if (r.getName() != null)
						unit.put("Name", String.valueOf(r.getName()));
					else
						unit.put("Name", "");
					dataProductCatalog.add(unit);
				
				
				tableProductCatalog.setItems(dataProductCatalog);
				operation_return_pane.setContent(tableProductCatalog);					
					
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void modifyProductCatalog() {
		
		System.out.println("execute modifyProductCatalog");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: modifyProductCatalog in service: ManageProductCatalogCRUDService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(manageproductcatalogcrudservice_service.modifyProductCatalog(
			Integer.valueOf(modifyProductCatalog_id_t.getText()),
			modifyProductCatalog_name_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void deleteProductCatalog() {
		
		System.out.println("execute deleteProductCatalog");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: deleteProductCatalog in service: ManageProductCatalogCRUDService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(manageproductcatalogcrudservice_service.deleteProductCatalog(
			Integer.valueOf(deleteProductCatalog_id_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void createCashDesk() {
		
		System.out.println("execute createCashDesk");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: createCashDesk in service: ManageCashDeskCRUDService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(managecashdeskcrudservice_service.createCashDesk(
			Integer.valueOf(createCashDesk_id_t.getText()),
			createCashDesk_name_t.getText(),
			Boolean.valueOf(createCashDesk_isopened_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void queryCashDesk() {
		
		System.out.println("execute queryCashDesk");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: queryCashDesk in service: ManageCashDeskCRUDService ");
		
		try {
			//invoke op with parameters
				CashDesk r = managecashdeskcrudservice_service.queryCashDesk(
				Integer.valueOf(queryCashDesk_id_t.getText())
				);
			
				//binding result to GUI
				TableView<Map<String, String>> tableCashDesk = new TableView<Map<String, String>>();
				TableColumn<Map<String, String>, String> tableCashDesk_Id = new TableColumn<Map<String, String>, String>("Id");
				tableCashDesk_Id.setMinWidth("Id".length()*10);
				tableCashDesk_Id.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("Id"));
				    }
				});	
				tableCashDesk.getColumns().add(tableCashDesk_Id);
				TableColumn<Map<String, String>, String> tableCashDesk_Name = new TableColumn<Map<String, String>, String>("Name");
				tableCashDesk_Name.setMinWidth("Name".length()*10);
				tableCashDesk_Name.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("Name"));
				    }
				});	
				tableCashDesk.getColumns().add(tableCashDesk_Name);
				TableColumn<Map<String, String>, String> tableCashDesk_IsOpened = new TableColumn<Map<String, String>, String>("IsOpened");
				tableCashDesk_IsOpened.setMinWidth("IsOpened".length()*10);
				tableCashDesk_IsOpened.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("IsOpened"));
				    }
				});	
				tableCashDesk.getColumns().add(tableCashDesk_IsOpened);
				
				ObservableList<Map<String, String>> dataCashDesk = FXCollections.observableArrayList();
				
					Map<String, String> unit = new HashMap<String, String>();
					unit.put("Id", String.valueOf(r.getId()));
					if (r.getName() != null)
						unit.put("Name", String.valueOf(r.getName()));
					else
						unit.put("Name", "");
					unit.put("IsOpened", String.valueOf(r.getIsOpened()));
					dataCashDesk.add(unit);
				
				
				tableCashDesk.setItems(dataCashDesk);
				operation_return_pane.setContent(tableCashDesk);					
					
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void modifyCashDesk() {
		
		System.out.println("execute modifyCashDesk");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: modifyCashDesk in service: ManageCashDeskCRUDService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(managecashdeskcrudservice_service.modifyCashDesk(
			Integer.valueOf(modifyCashDesk_id_t.getText()),
			modifyCashDesk_name_t.getText(),
			Boolean.valueOf(modifyCashDesk_isopened_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void deleteCashDesk() {
		
		System.out.println("execute deleteCashDesk");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: deleteCashDesk in service: ManageCashDeskCRUDService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(managecashdeskcrudservice_service.deleteCashDesk(
			Integer.valueOf(deleteCashDesk_id_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void createCashier() {
		
		System.out.println("execute createCashier");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: createCashier in service: ManageCashierCRUDService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(managecashiercrudservice_service.createCashier(
			Integer.valueOf(createCashier_id_t.getText()),
			createCashier_name_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void queryCashier() {
		
		System.out.println("execute queryCashier");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: queryCashier in service: ManageCashierCRUDService ");
		
		try {
			//invoke op with parameters
				Cashier r = managecashiercrudservice_service.queryCashier(
				Integer.valueOf(queryCashier_id_t.getText())
				);
			
				//binding result to GUI
				TableView<Map<String, String>> tableCashier = new TableView<Map<String, String>>();
				TableColumn<Map<String, String>, String> tableCashier_Id = new TableColumn<Map<String, String>, String>("Id");
				tableCashier_Id.setMinWidth("Id".length()*10);
				tableCashier_Id.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("Id"));
				    }
				});	
				tableCashier.getColumns().add(tableCashier_Id);
				TableColumn<Map<String, String>, String> tableCashier_Name = new TableColumn<Map<String, String>, String>("Name");
				tableCashier_Name.setMinWidth("Name".length()*10);
				tableCashier_Name.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("Name"));
				    }
				});	
				tableCashier.getColumns().add(tableCashier_Name);
				
				ObservableList<Map<String, String>> dataCashier = FXCollections.observableArrayList();
				
					Map<String, String> unit = new HashMap<String, String>();
					unit.put("Id", String.valueOf(r.getId()));
					if (r.getName() != null)
						unit.put("Name", String.valueOf(r.getName()));
					else
						unit.put("Name", "");
					dataCashier.add(unit);
				
				
				tableCashier.setItems(dataCashier);
				operation_return_pane.setContent(tableCashier);					
					
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void modifyCashier() {
		
		System.out.println("execute modifyCashier");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: modifyCashier in service: ManageCashierCRUDService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(managecashiercrudservice_service.modifyCashier(
			Integer.valueOf(modifyCashier_id_t.getText()),
			modifyCashier_name_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void deleteCashier() {
		
		System.out.println("execute deleteCashier");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: deleteCashier in service: ManageCashierCRUDService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(managecashiercrudservice_service.deleteCashier(
			Integer.valueOf(deleteCashier_id_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void createItem() {
		
		System.out.println("execute createItem");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: createItem in service: ManageItemCRUDService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(manageitemcrudservice_service.createItem(
			Integer.valueOf(createItem_barcode_t.getText()),
			createItem_name_t.getText(),
			Float.valueOf(createItem_price_t.getText()),
			Integer.valueOf(createItem_stocknumber_t.getText()),
			Float.valueOf(createItem_orderprice_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void queryItem() {
		
		System.out.println("execute queryItem");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: queryItem in service: ManageItemCRUDService ");
		
		try {
			//invoke op with parameters
				Item r = manageitemcrudservice_service.queryItem(
				Integer.valueOf(queryItem_barcode_t.getText())
				);
			
				//binding result to GUI
				TableView<Map<String, String>> tableItem = new TableView<Map<String, String>>();
				TableColumn<Map<String, String>, String> tableItem_Barcode = new TableColumn<Map<String, String>, String>("Barcode");
				tableItem_Barcode.setMinWidth("Barcode".length()*10);
				tableItem_Barcode.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("Barcode"));
				    }
				});	
				tableItem.getColumns().add(tableItem_Barcode);
				TableColumn<Map<String, String>, String> tableItem_Name = new TableColumn<Map<String, String>, String>("Name");
				tableItem_Name.setMinWidth("Name".length()*10);
				tableItem_Name.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("Name"));
				    }
				});	
				tableItem.getColumns().add(tableItem_Name);
				TableColumn<Map<String, String>, String> tableItem_Price = new TableColumn<Map<String, String>, String>("Price");
				tableItem_Price.setMinWidth("Price".length()*10);
				tableItem_Price.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("Price"));
				    }
				});	
				tableItem.getColumns().add(tableItem_Price);
				TableColumn<Map<String, String>, String> tableItem_StockNumber = new TableColumn<Map<String, String>, String>("StockNumber");
				tableItem_StockNumber.setMinWidth("StockNumber".length()*10);
				tableItem_StockNumber.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("StockNumber"));
				    }
				});	
				tableItem.getColumns().add(tableItem_StockNumber);
				TableColumn<Map<String, String>, String> tableItem_OrderPrice = new TableColumn<Map<String, String>, String>("OrderPrice");
				tableItem_OrderPrice.setMinWidth("OrderPrice".length()*10);
				tableItem_OrderPrice.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("OrderPrice"));
				    }
				});	
				tableItem.getColumns().add(tableItem_OrderPrice);
				TableColumn<Map<String, String>, String> tableItem_ItemID = new TableColumn<Map<String, String>, String>("ItemID");
				tableItem_ItemID.setMinWidth("ItemID".length()*10);
				tableItem_ItemID.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("ItemID"));
				    }
				});	
				tableItem.getColumns().add(tableItem_ItemID);
				
				ObservableList<Map<String, String>> dataItem = FXCollections.observableArrayList();
				
					Map<String, String> unit = new HashMap<String, String>();
					unit.put("Barcode", String.valueOf(r.getBarcode()));
					if (r.getName() != null)
						unit.put("Name", String.valueOf(r.getName()));
					else
						unit.put("Name", "");
					unit.put("Price", String.valueOf(r.getPrice()));
					unit.put("StockNumber", String.valueOf(r.getStockNumber()));
					unit.put("OrderPrice", String.valueOf(r.getOrderPrice()));
					unit.put("ItemID", String.valueOf(r.getItemID()));
					dataItem.add(unit);
				
				
				tableItem.setItems(dataItem);
				operation_return_pane.setContent(tableItem);					
					
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void modifyItem() {
		
		System.out.println("execute modifyItem");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: modifyItem in service: ManageItemCRUDService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(manageitemcrudservice_service.modifyItem(
			Integer.valueOf(modifyItem_barcode_t.getText()),
			modifyItem_name_t.getText(),
			Float.valueOf(modifyItem_price_t.getText()),
			Integer.valueOf(modifyItem_stocknumber_t.getText()),
			Float.valueOf(modifyItem_orderprice_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void deleteItem() {
		
		System.out.println("execute deleteItem");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: deleteItem in service: ManageItemCRUDService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(manageitemcrudservice_service.deleteItem(
			Integer.valueOf(deleteItem_barcode_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void createSupplier() {
		
		System.out.println("execute createSupplier");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: createSupplier in service: ManageSupplierCRUDService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(managesuppliercrudservice_service.createSupplier(
			Integer.valueOf(createSupplier_id_t.getText()),
			createSupplier_name_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void querySupplier() {
		
		System.out.println("execute querySupplier");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: querySupplier in service: ManageSupplierCRUDService ");
		
		try {
			//invoke op with parameters
				Supplier r = managesuppliercrudservice_service.querySupplier(
				Integer.valueOf(querySupplier_id_t.getText())
				);
			
				//binding result to GUI
				TableView<Map<String, String>> tableSupplier = new TableView<Map<String, String>>();
				TableColumn<Map<String, String>, String> tableSupplier_Id = new TableColumn<Map<String, String>, String>("Id");
				tableSupplier_Id.setMinWidth("Id".length()*10);
				tableSupplier_Id.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("Id"));
				    }
				});	
				tableSupplier.getColumns().add(tableSupplier_Id);
				TableColumn<Map<String, String>, String> tableSupplier_Name = new TableColumn<Map<String, String>, String>("Name");
				tableSupplier_Name.setMinWidth("Name".length()*10);
				tableSupplier_Name.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("Name"));
				    }
				});	
				tableSupplier.getColumns().add(tableSupplier_Name);
				
				ObservableList<Map<String, String>> dataSupplier = FXCollections.observableArrayList();
				
					Map<String, String> unit = new HashMap<String, String>();
					unit.put("Id", String.valueOf(r.getId()));
					if (r.getName() != null)
						unit.put("Name", String.valueOf(r.getName()));
					else
						unit.put("Name", "");
					dataSupplier.add(unit);
				
				
				tableSupplier.setItems(dataSupplier);
				operation_return_pane.setContent(tableSupplier);					
					
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void modifySupplier() {
		
		System.out.println("execute modifySupplier");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: modifySupplier in service: ManageSupplierCRUDService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(managesuppliercrudservice_service.modifySupplier(
			Integer.valueOf(modifySupplier_id_t.getText()),
			modifySupplier_name_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void deleteSupplier() {
		
		System.out.println("execute deleteSupplier");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: deleteSupplier in service: ManageSupplierCRUDService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(managesuppliercrudservice_service.deleteSupplier(
			Integer.valueOf(deleteSupplier_id_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void addItemToCheckout() {
		
		System.out.println("execute addItemToCheckout");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: addItemToCheckout in service: CheckOutService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(checkoutservice_service.addItemToCheckout(
			Integer.valueOf(addItemToCheckout_itemID_t.getText()),
			Integer.valueOf(addItemToCheckout_quantity_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void selectPaymentMethod() {
		
		System.out.println("execute selectPaymentMethod");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: selectPaymentMethod in service: CheckOutService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(checkoutservice_service.selectPaymentMethod(
			selectPaymentMethod_paymentMethod_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void processPayment() {
		
		System.out.println("execute processPayment");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: processPayment in service: CheckOutService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(checkoutservice_service.processPayment(
			Float.valueOf(processPayment_amount_t.getText()),
			processPayment_paymentDetails_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void printReceipt() {
		
		System.out.println("execute printReceipt");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: printReceipt in service: CheckOutService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(checkoutservice_service.printReceipt(
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void initiateReturn() {
		
		System.out.println("execute initiateReturn");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: initiateReturn in service: ReturnProductsService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(returnproductsservice_service.initiateReturn(
			Integer.valueOf(initiateReturn_orderID_t.getText()),
			Integer.valueOf(initiateReturn_itemID_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void checkReturnEligibility() {
		
		System.out.println("execute checkReturnEligibility");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: checkReturnEligibility in service: ReturnProductsService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(returnproductsservice_service.checkReturnEligibility(
			Integer.valueOf(checkReturnEligibility_requestID_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void provideReturnReason() {
		
		System.out.println("execute provideReturnReason");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: provideReturnReason in service: ReturnProductsService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(returnproductsservice_service.provideReturnReason(
			provideReturnReason_reason_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void reviewReturnRequest() {
		
		System.out.println("execute reviewReturnRequest");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: reviewReturnRequest in service: ManageReturnService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(managereturnservice_service.reviewReturnRequest(
			Integer.valueOf(reviewReturnRequest_requestID_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void approveOrRejectReturn() {
		
		System.out.println("execute approveOrRejectReturn");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: approveOrRejectReturn in service: ManageReturnService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(managereturnservice_service.approveOrRejectReturn(
			Boolean.valueOf(approveOrRejectReturn_isApproved_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void processRefund() {
		
		System.out.println("execute processRefund");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: processRefund in service: ManageReturnService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(managereturnservice_service.processRefund(
			Float.valueOf(processRefund_amount_t.getText()),
			processRefund_paymentMethod_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void receivePurchaseOrder() {
		
		System.out.println("execute receivePurchaseOrder");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: receivePurchaseOrder in service: AcceptPurchaseOrderService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(acceptpurchaseorderservice_service.receivePurchaseOrder(
			Integer.valueOf(receivePurchaseOrder_orderID_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void reviewOrderDetails() {
		
		System.out.println("execute reviewOrderDetails");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: reviewOrderDetails in service: AcceptPurchaseOrderService ");
		
		try {
			//invoke op with parameters
				PurchaseOrder r = acceptpurchaseorderservice_service.reviewOrderDetails(
				Integer.valueOf(reviewOrderDetails_orderID_t.getText())
				);
			
				//binding result to GUI
				TableView<Map<String, String>> tablePurchaseOrder = new TableView<Map<String, String>>();
				TableColumn<Map<String, String>, String> tablePurchaseOrder_OrderId = new TableColumn<Map<String, String>, String>("OrderId");
				tablePurchaseOrder_OrderId.setMinWidth("OrderId".length()*10);
				tablePurchaseOrder_OrderId.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("OrderId"));
				    }
				});	
				tablePurchaseOrder.getColumns().add(tablePurchaseOrder_OrderId);
				TableColumn<Map<String, String>, String> tablePurchaseOrder_SupplierId = new TableColumn<Map<String, String>, String>("SupplierId");
				tablePurchaseOrder_SupplierId.setMinWidth("SupplierId".length()*10);
				tablePurchaseOrder_SupplierId.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("SupplierId"));
				    }
				});	
				tablePurchaseOrder.getColumns().add(tablePurchaseOrder_SupplierId);
				TableColumn<Map<String, String>, String> tablePurchaseOrder_StoreId = new TableColumn<Map<String, String>, String>("StoreId");
				tablePurchaseOrder_StoreId.setMinWidth("StoreId".length()*10);
				tablePurchaseOrder_StoreId.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("StoreId"));
				    }
				});	
				tablePurchaseOrder.getColumns().add(tablePurchaseOrder_StoreId);
				TableColumn<Map<String, String>, String> tablePurchaseOrder_OrderTime = new TableColumn<Map<String, String>, String>("OrderTime");
				tablePurchaseOrder_OrderTime.setMinWidth("OrderTime".length()*10);
				tablePurchaseOrder_OrderTime.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("OrderTime"));
				    }
				});	
				tablePurchaseOrder.getColumns().add(tablePurchaseOrder_OrderTime);
				TableColumn<Map<String, String>, String> tablePurchaseOrder_ExpectedDeliveryTime = new TableColumn<Map<String, String>, String>("ExpectedDeliveryTime");
				tablePurchaseOrder_ExpectedDeliveryTime.setMinWidth("ExpectedDeliveryTime".length()*10);
				tablePurchaseOrder_ExpectedDeliveryTime.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("ExpectedDeliveryTime"));
				    }
				});	
				tablePurchaseOrder.getColumns().add(tablePurchaseOrder_ExpectedDeliveryTime);
				TableColumn<Map<String, String>, String> tablePurchaseOrder_TotalAmount = new TableColumn<Map<String, String>, String>("TotalAmount");
				tablePurchaseOrder_TotalAmount.setMinWidth("TotalAmount".length()*10);
				tablePurchaseOrder_TotalAmount.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("TotalAmount"));
				    }
				});	
				tablePurchaseOrder.getColumns().add(tablePurchaseOrder_TotalAmount);
				TableColumn<Map<String, String>, String> tablePurchaseOrder_IsReceived = new TableColumn<Map<String, String>, String>("IsReceived");
				tablePurchaseOrder_IsReceived.setMinWidth("IsReceived".length()*10);
				tablePurchaseOrder_IsReceived.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("IsReceived"));
				    }
				});	
				tablePurchaseOrder.getColumns().add(tablePurchaseOrder_IsReceived);
				TableColumn<Map<String, String>, String> tablePurchaseOrder_IsAccepted = new TableColumn<Map<String, String>, String>("IsAccepted");
				tablePurchaseOrder_IsAccepted.setMinWidth("IsAccepted".length()*10);
				tablePurchaseOrder_IsAccepted.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("IsAccepted"));
				    }
				});	
				tablePurchaseOrder.getColumns().add(tablePurchaseOrder_IsAccepted);
				
				ObservableList<Map<String, String>> dataPurchaseOrder = FXCollections.observableArrayList();
				
					Map<String, String> unit = new HashMap<String, String>();
					unit.put("OrderId", String.valueOf(r.getOrderId()));
					unit.put("SupplierId", String.valueOf(r.getSupplierId()));
					unit.put("StoreId", String.valueOf(r.getStoreId()));
					if (r.getOrderTime() != null)
						unit.put("OrderTime", r.getOrderTime().format(dateformatter));
					else
						unit.put("OrderTime", "");
					if (r.getExpectedDeliveryTime() != null)
						unit.put("ExpectedDeliveryTime", r.getExpectedDeliveryTime().format(dateformatter));
					else
						unit.put("ExpectedDeliveryTime", "");
					unit.put("TotalAmount", String.valueOf(r.getTotalAmount()));
					unit.put("IsReceived", String.valueOf(r.getIsReceived()));
					unit.put("IsAccepted", String.valueOf(r.getIsAccepted()));
					dataPurchaseOrder.add(unit);
				
				
				tablePurchaseOrder.setItems(dataPurchaseOrder);
				operation_return_pane.setContent(tablePurchaseOrder);					
					
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void confirmOrderAcceptance() {
		
		System.out.println("execute confirmOrderAcceptance");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: confirmOrderAcceptance in service: AcceptPurchaseOrderService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(acceptpurchaseorderservice_service.confirmOrderAcceptance(
			Boolean.valueOf(confirmOrderAcceptance_isAccepted_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}




	//select object index
	int objectindex;
	
	@FXML
	TabPane mainPane;

	@FXML
	TextArea log;
	
	@FXML
	TreeView<String> actor_treeview_cashier;
	@FXML
	TreeView<String> actor_treeview_storemanager;
	@FXML
	TreeView<String> actor_treeview_administrator;
	@FXML
	TreeView<String> actor_treeview_customer;
	@FXML
	TreeView<String> actor_treeview_supplier;
	


	@FXML
	TextArea definition;
	@FXML
	TextArea precondition;
	@FXML
	TextArea postcondition;
	@FXML
	TextArea invariants;
	
	@FXML
	TitledPane precondition_pane;
	@FXML
	TitledPane postcondition_pane;
	
	//chosen operation textfields
	List<TextField> choosenOperation;
	String clickedOp;
		
	@FXML
	TableView<ClassInfo> class_statisic;
	@FXML
	TableView<AssociationInfo> association_statisic;
	
	Map<String, ObservableList<AssociationInfo>> allassociationData;
	ObservableList<ClassInfo> classInfodata;
	
	CoCoMESystem cocomesystem_service;
	ThirdPartyServices thirdpartyservices_service;
	ProcessSaleService processsaleservice_service;
	ManageStoreCRUDService managestorecrudservice_service;
	ManageProductCatalogCRUDService manageproductcatalogcrudservice_service;
	ManageCashDeskCRUDService managecashdeskcrudservice_service;
	ManageCashierCRUDService managecashiercrudservice_service;
	ManageItemCRUDService manageitemcrudservice_service;
	ManageSupplierCRUDService managesuppliercrudservice_service;
	CoCoMEOrderProducts cocomeorderproducts_service;
	CheckOutService checkoutservice_service;
	ReturnProductsService returnproductsservice_service;
	ManageReturnService managereturnservice_service;
	AcceptPurchaseOrderService acceptpurchaseorderservice_service;
	
	ClassInfo store;
	ClassInfo productcatalog;
	ClassInfo cashdesk;
	ClassInfo sale;
	ClassInfo cashier;
	ClassInfo saleslineitem;
	ClassInfo item;
	ClassInfo payment;
	ClassInfo cashpayment;
	ClassInfo cardpayment;
	ClassInfo wechatpayment;
	ClassInfo alipayment;
	ClassInfo orderentry;
	ClassInfo supplier;
	ClassInfo orderproduct;
	ClassInfo customer;
	ClassInfo returnrequest;
	ClassInfo surveillancedevice;
	ClassInfo salesdataanalysisresult;
	ClassInfo purchaseorder;
	ClassInfo recommendedproduct;
	ClassInfo administrator;
		
	@FXML
	TitledPane object_statics;
	Map<String, TableView> allObjectTables;
	
	@FXML
	TitledPane operation_paras;
	
	@FXML
	TitledPane operation_return_pane;
	
	@FXML 
	TitledPane all_invariant_pane;
	
	@FXML
	TitledPane invariants_panes;
	
	Map<String, GridPane> operationPanels;
	Map<String, VBox> opInvariantPanel;
	
	//all textfiled or eumntity
	TextField openStore_storeID_t;
	TextField closeStore_storeID_t;
	TextField openCashDesk_cashDeskID_t;
	TextField closeCashDesk_cashDeskID_t;
	TextField enterItem_barcode_t;
	TextField enterItem_quantity_t;
	TextField makeCashPayment_amount_t;
	TextField makeCardPayment_cardAccountNumber_t;
	TextField makeCardPayment_expiryDate_t;
	TextField makeCardPayment_fee_t;
	TextField makeWechatPayment_amount_t;
	TextField makeWechatPayment_openID_t;
	TextField makeAliPayment_amount_t;
	TextField makeAliPayment_aliPayAccount_t;
	TextField thirdPartyCardPaymentService_cardAccountNumber_t;
	TextField thirdPartyCardPaymentService_expiryDate_t;
	TextField thirdPartyCardPaymentService_fee_t;
	TextField thirdPartyWechatPaymentService_amount_t;
	TextField thirdPartyWechatPaymentService_openID_t;
	TextField thirdPartyAliPaymentService_amount_t;
	TextField thirdPartyAliPaymentService_aliPayAccount_t;
	TextField makeNewOrder_orderid_t;
	TextField orderItem_barcode_t;
	TextField orderItem_quantity_t;
	TextField chooseSupplier_supplierID_t;
	TextField changePrice_barcode_t;
	TextField changePrice_newPrice_t;
	TextField receiveOrderedProduct_orderID_t;
	TextField createStore_id_t;
	TextField createStore_name_t;
	TextField createStore_address_t;
	TextField createStore_isopened_t;
	TextField queryStore_id_t;
	TextField modifyStore_id_t;
	TextField modifyStore_name_t;
	TextField modifyStore_address_t;
	TextField modifyStore_isopened_t;
	TextField deleteStore_id_t;
	TextField createProductCatalog_id_t;
	TextField createProductCatalog_name_t;
	TextField queryProductCatalog_id_t;
	TextField modifyProductCatalog_id_t;
	TextField modifyProductCatalog_name_t;
	TextField deleteProductCatalog_id_t;
	TextField createCashDesk_id_t;
	TextField createCashDesk_name_t;
	TextField createCashDesk_isopened_t;
	TextField queryCashDesk_id_t;
	TextField modifyCashDesk_id_t;
	TextField modifyCashDesk_name_t;
	TextField modifyCashDesk_isopened_t;
	TextField deleteCashDesk_id_t;
	TextField createCashier_id_t;
	TextField createCashier_name_t;
	TextField queryCashier_id_t;
	TextField modifyCashier_id_t;
	TextField modifyCashier_name_t;
	TextField deleteCashier_id_t;
	TextField createItem_barcode_t;
	TextField createItem_name_t;
	TextField createItem_price_t;
	TextField createItem_stocknumber_t;
	TextField createItem_orderprice_t;
	TextField queryItem_barcode_t;
	TextField modifyItem_barcode_t;
	TextField modifyItem_name_t;
	TextField modifyItem_price_t;
	TextField modifyItem_stocknumber_t;
	TextField modifyItem_orderprice_t;
	TextField deleteItem_barcode_t;
	TextField createSupplier_id_t;
	TextField createSupplier_name_t;
	TextField querySupplier_id_t;
	TextField modifySupplier_id_t;
	TextField modifySupplier_name_t;
	TextField deleteSupplier_id_t;
	TextField addItemToCheckout_itemID_t;
	TextField addItemToCheckout_quantity_t;
	TextField selectPaymentMethod_paymentMethod_t;
	TextField processPayment_amount_t;
	TextField processPayment_paymentDetails_t;
	TextField initiateReturn_orderID_t;
	TextField initiateReturn_itemID_t;
	TextField checkReturnEligibility_requestID_t;
	TextField provideReturnReason_reason_t;
	TextField reviewReturnRequest_requestID_t;
	TextField approveOrRejectReturn_isApproved_t;
	TextField processRefund_amount_t;
	TextField processRefund_paymentMethod_t;
	TextField receivePurchaseOrder_orderID_t;
	TextField reviewOrderDetails_orderID_t;
	TextField confirmOrderAcceptance_isAccepted_t;
	
	HashMap<String, String> definitions_map;
	HashMap<String, String> preconditions_map;
	HashMap<String, String> postconditions_map;
	HashMap<String, String> invariants_map;
	LinkedHashMap<String, String> service_invariants_map;
	LinkedHashMap<String, String> entity_invariants_map;
	LinkedHashMap<String, Label> service_invariants_label_map;
	LinkedHashMap<String, Label> entity_invariants_label_map;
	LinkedHashMap<String, Label> op_entity_invariants_label_map;
	LinkedHashMap<String, Label> op_service_invariants_label_map;
	

	
}
