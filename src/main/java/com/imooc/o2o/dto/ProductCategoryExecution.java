package com.imooc.o2o.dto;

import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.enums.ProductCategoryStateEnum;

import java.util.List;

/**
 * Created by Unruly Wind on 2019/1/8/008.
 *
 * @author BlueMelancholy
 * @desc:
 */
public class ProductCategoryExecution extends Throwable {
	private int state;
	private String stateInfo;
	private List<ProductCategory> productCategoryList;

	//操作失败的构造器
	public ProductCategoryExecution(ProductCategoryStateEnum productCategoryStateEnum) {
		this.state = productCategoryStateEnum.getState();
		this.stateInfo = productCategoryStateEnum.getStateInfo();
	}

	//操作成功的构造器
	public ProductCategoryExecution(ProductCategoryStateEnum productCategoryStateEnum, List<ProductCategory> productCategoryList) {
		this.state = productCategoryStateEnum.getState();
		this.stateInfo = productCategoryStateEnum.getStateInfo();
		this.productCategoryList = productCategoryList;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}

	public List<ProductCategory> getProductCategoryList() {
		return productCategoryList;
	}

	public void setProductCategoryList(List<ProductCategory> productCategoryList) {
		this.productCategoryList = productCategoryList;
	}
}
