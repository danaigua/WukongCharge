package com.zhicheng.wukongcharge.entity;
/**
 * 电池实体类
 * @author 章家宝
 *
 */
public class Charger {
	private String CompanyCode;				//网关序列号
	private Long StationId;					//网关关联电站的ID
	public String getCompanyCode() {
		return CompanyCode;
	}
	public void setCompanyCode(String companyCode) {
		CompanyCode = companyCode;
	}
	public Long getStationId() {
		return StationId;
	}
	public void setStationId(Long stationId) {
		StationId = stationId;
	}
	
	
	

}
