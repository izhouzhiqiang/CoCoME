# CoCoME

## 任务一：结构化自然语言建模

​	在本任务中，描述了作为客户的四种需求：浏览商品、添加商品、结账和退货；管理员的三种需求：管理退货、管理监控状态、分析销售数据；以及供应商的两种需求：接受采购订单和推荐新商品。满足**新增用户需求数量8条、系统需求数量15条**。

### ![image-20250401211033525](https://github.com/MiaJ321/LAB01-CoCoME/blob/main/CoCoME/image/image-20250401211033525.png?raw=true)

![image-20250401211606975](https://github.com/MiaJ321/LAB01-CoCoME/blob/main/CoCoME/image/image-20250401211606975.png?raw=true)

![image-20250401211731697](https://github.com/MiaJ321/LAB01-CoCoME/blob/main/CoCoME/image/image-20250401211731697.png?raw=true)

## 任务二：UML需求建模

### 1.用例图

​	**新增用户角色2个**：顾客和供应商。**新增用例9个**，分别是：管理员的analyzeSalesData、manageSurveillance、manageReturn；顾客的browseProducts、addProductsToCart、returnProducts、checkOut；供应商的recommendNewProducts、acceptPurchaseOrder。并且acceptPurchaseOrder里面include两个用例。

![image-20250401212117251](https://github.com/MiaJ321/LAB01-CoCoME/blob/main/CoCoME/image/image-20250401212117251.png?raw=true)

### 2.系统顺序图

​	**系统顺序图新增4个**，分别是acceptPurchaseOrder、checkOut、manageReturn、returnProducts。除此之外，还修改了原有的ProcessSale顺序图。相应的，**新增系统操作总数19个**，**新增系统合约数17个**。

![image-20250401212622151](https://github.com/MiaJ321/LAB01-CoCoME/blob/main/CoCoME/image/image-20250401212622151.png?raw=true)

![image-20250401212836172](https://github.com/MiaJ321/LAB01-CoCoME/blob/main/CoCoME/image/image-20250401212836172.png?raw=true)

![image-20250401212859750](https://github.com/MiaJ321/LAB01-CoCoME/blob/main/CoCoME/image/image-20250401212859750.png?raw=true)

![image-20250401212951731](https://github.com/MiaJ321/LAB01-CoCoME/blob/main/CoCoME/image/image-20250401212951731.png?raw=true)

![image-20250401213115255](https://github.com/MiaJ321/LAB01-CoCoME/blob/main/CoCoME/image/image-20250401213115255.png?raw=true)

### 3.概念类图

​	**新增类9个**，分别是ReturnRequest、SurveillanceDevice、SalesDataAnalysisResult、Customer、Alipayment、WechatPayment、Administrator、RecommendedProduct、PurchaseOrder。除此之外，还修改了若干类。

![image-20250401213704602](https://github.com/MiaJ321/LAB01-CoCoME/blob/main/CoCoME/image/image-20250401213704602.png?raw=true)

## 任务三：UML需求原型化与需求确认

​	原型化结果如下：

![image-20250331183600711](https://github.com/MiaJ321/LAB01-CoCoME/blob/main/CoCoME/image/image-20250331183600711.png?raw=true)

# **实验二：**RM2PT自动化架构设计和详细设计

## **任务**1：架构设计自动生成（RapidMS）

​	以下图片是生成过程中的所有图片，均为调整后的。

![image-20250405122628233](https://github.com/MiaJ321/LAB01-CoCoME/blob/main/CoCoME/image/image-20250405122628233.png?raw=true)



![image-20250405134817315](https://github.com/MiaJ321/LAB01-CoCoME/blob/main/CoCoME/image/image-20250405134817315.png?raw=true)

![image-20250405134907931](https://github.com/MiaJ321/LAB01-CoCoME/blob/main/CoCoME/image/image-20250405134907931.png?raw=true)

![image-20250405140551959](https://github.com/MiaJ321/LAB01-CoCoME/blob/main/CoCoME/image/image-20250405140551959.png?raw=true)

![image-20250405141400748](https://github.com/MiaJ321/LAB01-CoCoME/blob/main/CoCoME/image/image-20250405141400748.png?raw=true)

![image-20250405141424494](https://github.com/MiaJ321/LAB01-CoCoME/blob/main/CoCoME/image/image-20250405141424494.png?raw=true)

![image-20250405141513674](https://github.com/MiaJ321/LAB01-CoCoME/blob/main/CoCoME/image/image-20250405141513674.png?raw=true)

![image-20250405141445327](https://github.com/MiaJ321/LAB01-CoCoME/blob/main/CoCoME/image/image-20250405141445327.png?raw=true)



## **任务**2：面向对象详细设计自动生成（RM2DM）

​	下图是面向对象的详细设计：

![image-20250405164848349](https://github.com/MiaJ321/LAB01-CoCoME/blob/main/CoCoME/image/image-20250405164848349.png?raw=true)

![image-20250405164509191](https://github.com/MiaJ321/LAB01-CoCoME/blob/main/CoCoME/image/image-20250405164509191.png?raw=true)

![image-20250405165000250](https://github.com/MiaJ321/LAB01-CoCoME/blob/main/CoCoME/image/image-20250405165000250.png?raw=true)

## **任务3：大模型生成设计模型与微服务拆分**

### **1、设计模型：**

​	**核心关系说明：**

1. 聚合关系（◇）：Store聚合多个CashDesk，Sale聚合多个SalesLineItem
2. 组合关系（<>）：ProductCatalog组合多个InventoryItem
3. 继承关系（<|）：支付方式继承自Payment抽象类
4. 关联关系：
   - Customer与ShoppingCart（1对1）
   - StoreManager与Store（1对多）
   - Supplier与PurchaseOrder（1对多）
   - Sale与Payment（1对1）

​	**核心业务类层**：

![image-20250428165735846](https://github.com/MiaJ321/LAB01-CoCoME/blob/main/CoCoME/image/image-20250428165735846.png?raw=true)

![image-20250428165754716](https://github.com/MiaJ321/LAB01-CoCoME/blob/main/CoCoME/image/image-20250428165754716.png?raw=true)

​		**支付系统类层**：

![image-20250428165911352](https://github.com/MiaJ321/LAB01-CoCoME/blob/main/CoCoME/image/image-20250428165911352.png?raw=true)

​	**管理系统类层**：

![image-20250428170139331](https://github.com/MiaJ321/LAB01-CoCoME/blob/main/CoCoME/image/image-20250428170139331.png?raw=true)

​	**用户交互类层**：

![image-20250428170216153](https://github.com/MiaJ321/LAB01-CoCoME/blob/main/CoCoME/image/image-20250428170216153.png?raw=true)

### **2、微服务架构**

**1、收银服务：**

职责范围：
- 实现类图中 CashDesk/Sale/SalesLineItem 的核心逻辑
- 处理 processSale（用例图）、checkOut（类图）流程
- 管理收银台状态机（openCashDesk/closeCashDesk）

核心实体：
CashDesk(id, status), 
Sale(saleId, createTime), 
Payment(amount, paymentMethod)

关键接口：
POST /checkout/{deskId}/sales
PUT /checkout/desks/{id}/status

**2、库存服务**

职责范围：
- 实现 ProductCatalog/InventoryItem/Item 管理
- 支撑 ShowStockReports（用例图）、manageProductCatalog（类图）
- 处理产品价格变更（changePrice）

核心实体：
ProductCatalog(catalogId), 
Item(itemId, price), 
InventoryItem(stockLevel)

关键接口：
GET /inventory/products/{id}/stock
PATCH /inventory/items/{id}/price

**3、订单服务：**

职责范围：
- 实现 OrderProduct/PurchaseOrder/ReturnRequest 生命周期
- 处理 returnProducts（用例图）、acceptPurchaseOrder（类图）
- 管理订单状态流转（OrderStatus 状态机）

核心实体：
OrderProduct(orderId, status), 
ReturnRequest(requestId, reason), 
PurchaseOrder(poNumber)

关键接口：
POST /orders/{orderId}/returns
PUT /orders/purchase-orders/{id}/accept

**4、支付服务：**

职责范围：
- 实现 Payment 及其子类（Cash/WeChat/AliPayment）
- 处理多种支付方式集成（类图支付方式继承结构）
- 执行交易对账和退款流程

核心实体：
Payment(txnId, method), 
CashPayment(cashReceived), 
WeChatPayment(openId)

关键接口：
POST /payments/{saleId}/process
PUT /payments/{txnId}/refund

**5、供应商服务：**

职责范围：
- 实现 Supplier 管理和 browseProducts（用例图）
- 处理供应商资质审核（类图 Supplier 属性）
- 管理供应商关系（listSuppliers 用例）

核心实体：
Supplier(supplierId, contacts), 
RecommendedProduct(recommendationId)

关键接口：
GET /suppliers/{id}/products
POST /suppliers/{id}/recommendations

**6、分析服务：**

职责范围：
- 实现 SalesDataAnalysis/SalesTrend 分析功能
- 支撑 analyzeSalesData（用例图）和销售趋势分析
- 生成智能推荐（RecommendedProduct 实体）

核心实体：
SalesDataAnalysis(analysisId, dataset), 
SalesTrend(trendId, period)

关键接口：
GET /analytics/sales/trends?period=weekly
POST /analytics/recommendations/generate

**7、监控服务：**

职责范围：
- 实现 SurveillanceDev 设备管理（类图）
- 处理安防设备状态监控（lastMaintenance）
- 存储监控日志和报警事件

核心实体：
SurveillanceDev(deviceId, location), 
SecurityLog(logId, timestamp)

关键接口：
GET /surveillance/devices/{id}/status
POST /surveillance/alerts

**8、服务间交互关系：**

核心交互场景：

![image-20250428172108522](https://github.com/MiaJ321/LAB01-CoCoME/blob/main/CoCoME/image/image-20250428172108522.png?raw=true)
