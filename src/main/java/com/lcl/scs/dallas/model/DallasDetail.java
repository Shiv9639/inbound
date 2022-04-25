package com.lcl.scs.dallas.model;

public class DallasDetail {
	
	private String outbound_order;
	private String store;
	private String carton;
	private String dallas_product;
	private String dallas_prod_desc;
	private String upc;
	private String prod_retail_qty;
	private String prod_cube;
	private String prod_wgt;
	private String ship_pack_size;
	private String case_unit;
	private String file_reference;
	private String allocated_quantity;
	
	public DallasDetail(String outbound_order, String store, String carton, String dallas_product,
			String dallas_prod_desc, String upc, String prod_retail_qty, String prod_cube, String prod_wgt,
			String ship_pack_size, String case_unit, String file_reference, String allocated_quantity) {
		super();
		this.outbound_order = outbound_order;
		this.store = store;
		this.carton = carton;
		this.dallas_product = dallas_product;
		this.dallas_prod_desc = dallas_prod_desc;
		this.upc = upc;
		this.prod_retail_qty = prod_retail_qty;
		this.prod_cube = prod_cube;
		this.prod_wgt = prod_wgt;
		this.ship_pack_size = ship_pack_size;
		this.case_unit = case_unit;
		this.file_reference = file_reference;
		this.allocated_quantity = allocated_quantity;
	}

	public String getOutbound_order() {
		return outbound_order;
	}

	public void setOutbound_order(String outbound_order) {
		this.outbound_order = outbound_order;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public String getCarton() {
		return carton;
	}

	public void setCarton(String carton) {
		this.carton = carton;
	}

	public String getDallas_product() {
		return dallas_product;
	}

	public void setDallas_product(String dallas_product) {
		this.dallas_product = dallas_product;
	}

	public String getDallas_prod_desc() {
		return dallas_prod_desc;
	}

	public void setDallas_prod_desc(String dallas_prod_desc) {
		this.dallas_prod_desc = dallas_prod_desc;
	}

	public String getUpc() {
		return upc;
	}

	public void setUpc(String upc) {
		this.upc = upc;
	}

	public String getProd_retail_qty() {
		return prod_retail_qty;
	}

	public void setProd_retail_qty(String prod_retail_qty) {
		this.prod_retail_qty = prod_retail_qty;
	}

	public String getProd_cube() {
		return prod_cube;
	}

	public void setProd_cube(String prod_cube) {
		this.prod_cube = prod_cube;
	}

	public String getProd_wgt() {
		return prod_wgt;
	}

	public void setProd_wgt(String prod_wgt) {
		this.prod_wgt = prod_wgt;
	}

	public String getShip_pack_size() {
		return ship_pack_size;
	}

	public void setShip_pack_size(String ship_pack_size) {
		this.ship_pack_size = ship_pack_size;
	}

	public String getCase_unit() {
		return case_unit;
	}

	public void setCase_unit(String case_unit) {
		this.case_unit = case_unit;
	}

	public String getFile_reference() {
		return file_reference;
	}

	public void setFile_reference(String file_reference) {
		this.file_reference = file_reference;
	}

	public String getAllocated_quantity() {
		return allocated_quantity;
	}

	public void setAllocated_quantity(String allocated_quantity) {
		this.allocated_quantity = allocated_quantity;
	}
	
	
	
	
	
	
}
