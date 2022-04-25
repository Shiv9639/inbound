package com.lcl.scs.dallas.model;

import java.sql.Date;

public class DallasHeader {
	private String store;
	private String MMS_PO;
	private String outbound_order;
	private Date delivery_date;
	private String delivery_time;
	private String DC;
	private String LW;
	private String mms_vendor_code;
	private String order_status;
	private Date order_routed_date_time;
	private String route;
	private String stop;
	private Date order_create_date_time;
	private Date order_change_date_time;
	private String carton;
	private String carton_status;
	private String ship_method_type;
	private String ship_method_desc;
	private String carton_cube;
	private String carton_weight;
	private String carton_units;
	private String carton_lines;
	private Date carton_create_date_time;
	private String file_reference;
	private String BOL;
	private String fedex_tracking_number;
	private String carrier;
	private String allocated_quatity;
	private String outbound_order_quantity;
	

	
	public DallasHeader(String store, String mMS_PO, String outbound_order, Date delivery_date, String delivery_time,
			String dC, String lW, String mms_vendor_code, String order_status, Date order_routed_date_time, String route,
			String stop, Date order_create_date_time, Date order_change_date_time, String carton, String carton_status,
			String ship_method_type, String ship_method_desc, String carton_cube, String carton_weight,
			String carton_units, String carton_lines, Date carton_create_date_time, String file_reference, String BOL, String fedex_tracking_number, String carrier, String allocated_quantity, String outbound_order_quantity) {
		super();
		this.store = store;
		MMS_PO = mMS_PO;
		this.outbound_order = outbound_order;
		this.delivery_date = delivery_date;
		this.delivery_time = delivery_time;
		DC = dC;
		LW = lW;
		this.mms_vendor_code = mms_vendor_code;
		this.order_status = order_status;
		this.order_routed_date_time = order_routed_date_time;
		this.route = route;
		this.stop = stop;
		this.order_create_date_time = order_create_date_time;
		this.order_change_date_time = order_change_date_time;
		this.carton = carton;
		this.carton_status = carton_status;
		this.ship_method_type = ship_method_type;
		this.ship_method_desc = ship_method_desc;
		this.carton_cube = carton_cube;
		this.carton_weight = carton_weight;
		this.carton_units = carton_units;
		this.carton_lines = carton_lines;
		this.carton_create_date_time = carton_create_date_time;
		this.file_reference = file_reference;
		this.BOL = BOL;
		this.fedex_tracking_number = fedex_tracking_number;
		this.carrier = carrier;
		this.allocated_quatity = allocated_quantity;
		this.outbound_order_quantity = outbound_order_quantity;
	}
	public String getStore() {
		return store;
	}
	public void setStore(String store) {
		this.store = store;
	}
	public String getMMS_PO() {
		return MMS_PO;
	}
	public void setMMS_PO(String mMS_PO) {
		MMS_PO = mMS_PO;
	}
	public String getOutbound_order() {
		return outbound_order;
	}
	public void setOutbound_order(String outbound_order) {
		this.outbound_order = outbound_order;
	}
	public Date getDelivery_date() {
		return delivery_date;
	}
	public void setDelivery_date(Date delivery_date) {
		this.delivery_date = delivery_date;
	}
	public String getDelivery_time() {
		return delivery_time;
	}
	public void setDelivery_time(String delivery_time) {
		this.delivery_time = delivery_time;
	}
	public String getDC() {
		return DC;
	}
	public void setDC(String dC) {
		DC = dC;
	}
	public String getLW() {
		return LW;
	}
	public void setLW(String lW) {
		LW = lW;
	}
	public String getMms_vendor_code() {
		return mms_vendor_code;
	}
	public void setMms_vendor_code(String mms_vendor_code) {
		this.mms_vendor_code = mms_vendor_code;
	}
	public String getOrder_status() {
		return order_status;
	}
	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}
	public Date getOrder_routed_date_time() {
		return order_routed_date_time;
	}
	public void setOrder_routed_date_time(Date order_routed_date_time) {
		this.order_routed_date_time = order_routed_date_time;
	}
	public String getRoute() {
		return route;
	}
	public void setRoute(String route) {
		this.route = route;
	}
	public String getStop() {
		return stop;
	}
	public void setStop(String stop) {
		this.stop = stop;
	}
	public Date getOrder_create_date_time() {
		return order_create_date_time;
	}
	public void setOrder_create_date_time(Date order_create_date_time) {
		this.order_create_date_time = order_create_date_time;
	}
	public Date getOrder_change_date_time() {
		return order_change_date_time;
	}
	public void setOrder_change_date_time(Date order_change_date_time) {
		this.order_change_date_time = order_change_date_time;
	}
	public String getCarton() {
		return carton;
	}
	public void setCarton(String carton) {
		this.carton = carton;
	}
	public String getCarton_status() {
		return carton_status;
	}
	public void setCarton_status(String carton_status) {
		this.carton_status = carton_status;
	}
	public String getShip_method_type() {
		return ship_method_type;
	}
	public void setShip_method_type(String ship_method_type) {
		this.ship_method_type = ship_method_type;
	}
	public String getShip_method_desc() {
		return ship_method_desc;
	}
	public void setShip_method_desc(String ship_method_desc) {
		this.ship_method_desc = ship_method_desc;
	}
	public String getCarton_cube() {
		return carton_cube;
	}
	public void setCarton_cube(String carton_cube) {
		this.carton_cube = carton_cube;
	}
	public String getCarton_weight() {
		return carton_weight;
	}
	public void setCarton_weight(String carton_weight) {
		this.carton_weight = carton_weight;
	}
	public String getCarton_units() {
		return carton_units;
	}
	public void setCarton_units(String carton_units) {
		this.carton_units = carton_units;
	}
	public String getCarton_lines() {
		return carton_lines;
	}
	public void setCarton_lines(String carton_lines) {
		this.carton_lines = carton_lines;
	}
	public Date getCarton_create_date_time() {
		return carton_create_date_time;
	}
	public void setCarton_create_date_time(Date carton_create_date_time) {
		this.carton_create_date_time = carton_create_date_time;
	}
	
	public String getFile_reference() {
		return file_reference;
	}
	public void setFile_reference(String file_reference) {
		this.file_reference = file_reference;
	}
	public String getBOL() {
		return BOL;
	}
	public void setBOL(String bOL) {
		BOL = bOL;
	}
	public String getFedex_tracking_number() {
		return fedex_tracking_number;
	}
	public void setFedex_tracking_number(String fedex_tracking_number) {
		this.fedex_tracking_number = fedex_tracking_number;
	}
	public String getCarrier() {
		return carrier;
	}
	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}
	
	public String getAllocated_quatity() {
		return allocated_quatity;
	}
	public void setAllocated_quatity(String allocated_quatity) {
		this.allocated_quatity = allocated_quatity;
	}
	public String getOutbound_order_quantity() {
		return outbound_order_quantity;
	}
	public void setOutbound_order_quantity(String outbound_order_quantity) {
		this.outbound_order_quantity = outbound_order_quantity;
	}
	
}
