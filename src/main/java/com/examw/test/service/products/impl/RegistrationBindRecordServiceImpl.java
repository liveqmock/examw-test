package com.examw.test.service.products.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;

import com.examw.test.dao.products.IRegistrationBindRecordDao;
import com.examw.test.domain.products.RegistrationBindRecord;
import com.examw.test.model.products.RegistrationBindRecordInfo;
import com.examw.test.service.impl.BaseDataServiceImpl;
import com.examw.test.service.products.IRegistrationBindRecordService;

/**
 * 注册码绑定记录服务接口实现类
 * @author fengwei.
 * @since 2014年8月15日 上午9:53:49.
 */
public class RegistrationBindRecordServiceImpl extends BaseDataServiceImpl<RegistrationBindRecord,RegistrationBindRecordInfo> implements IRegistrationBindRecordService{
	private static final Logger logger = Logger.getLogger(RegistrationBindRecordServiceImpl.class);
	private IRegistrationBindRecordDao registrationBindRecordDao;
	
	/**
	 * 设置 注册码绑定记录数据接口
	 * @param registrationBindRecordDao
	 * 
	 */
	public void setRegistrationBindRecordDao(
			IRegistrationBindRecordDao registrationBindRecordDao) {
		this.registrationBindRecordDao = registrationBindRecordDao;
	}
	/*
	 * 查询数据
	 * @see com.examw.test.service.impl.BaseDataServiceImpl#find(java.lang.Object)
	 */
	@Override
	protected List<RegistrationBindRecord> find(RegistrationBindRecordInfo info) {
		if(logger.isDebugEnabled()) logger.debug("查询绑定记录数据...");
		return this.registrationBindRecordDao.findRecords(info);
	}
	/*
	 * 模型转化
	 * @see com.examw.test.service.impl.BaseDataServiceImpl#changeModel(java.lang.Object)
	 */
	@Override
	protected RegistrationBindRecordInfo changeModel(RegistrationBindRecord data) {
		if(data == null) return null;
		RegistrationBindRecordInfo info = new RegistrationBindRecordInfo();
		BeanUtils.copyProperties(data, info);
		if(data.getRegistration()!=null){
			info.setRegistrationId(data.getRegistration().getId());
			info.setRegistrationCode(data.getRegistration().getCode());
		}
		if(data.getSoftwareType()!=null){
			info.setSoftwareTypeId(data.getSoftwareType().getId());
			info.setSoftwareTypeName(data.getSoftwareType().getName());
		}
		return info;
	}
	/*
	 * 查询统计
	 * @see com.examw.test.service.impl.BaseDataServiceImpl#total(java.lang.Object)
	 */
	@Override
	protected Long total(RegistrationBindRecordInfo info) {
		return this.registrationBindRecordDao.total(info);
	}
	
	@Override
	public RegistrationBindRecordInfo update(RegistrationBindRecordInfo info) {
		
		return null;
	}
	@Override
	public void delete(String[] ids) {
	}

}