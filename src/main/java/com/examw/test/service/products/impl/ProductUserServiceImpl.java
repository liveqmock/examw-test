package com.examw.test.service.products.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import com.examw.test.dao.products.IProductUserDao;
import com.examw.test.domain.products.ProductUser;
import com.examw.test.model.products.ProductUserInfo;
import com.examw.test.service.impl.BaseDataServiceImpl;
import com.examw.test.service.products.IProductUserService;

/**
 * 
 * @author fengwei.
 * @since 2014年8月12日 上午8:57:35.
 */
public class ProductUserServiceImpl extends BaseDataServiceImpl<ProductUser,ProductUserInfo> implements IProductUserService{
	private static final Logger logger = Logger.getLogger(ProductUserServiceImpl.class);
	private IProductUserDao productUserDao;
	private Map<Integer, String> statusMap;
	
	/**
	 * 设置 产品用户数据接口
	 * @param ProductUserDao
	 * 
	 */
	public void setProductUserDao(IProductUserDao ProductUserDao) {
		this.productUserDao = ProductUserDao;
	}
	
	/**
	 * 设置 状态名称映射
	 * @param statusMap
	 * 
	 */
	public void setStatusMap(Map<Integer, String> statusMap) {
		this.statusMap = statusMap;
	}

	@Override
	protected List<ProductUser> find(ProductUserInfo info) {
		if (logger.isDebugEnabled())	logger.debug("查询[产品用户]数据...");
		return this.productUserDao.findProductUsers(info);
	}

	@Override
	protected ProductUserInfo changeModel(ProductUser data) {
		if (logger.isDebugEnabled())	logger.debug("[产品用户]数据模型转换...");
		if (data == null)
			return null;
		ProductUserInfo info = new ProductUserInfo();
		BeanUtils.copyProperties(data, info);
		info.setStatusName(this.loadStatusName(data.getStatus()));
		return info;
	}

	@Override
	protected Long total(ProductUserInfo info) {
		if (logger.isDebugEnabled())	logger.debug("查询统计...");
		return productUserDao.total(info);
	}

	@Override
	public ProductUserInfo update(ProductUserInfo info) {
		if (logger.isDebugEnabled())	logger.debug("更新数据...");
		if(info == null) return null;
		boolean isAdded = false;
		ProductUser  data = StringUtils.isEmpty(info.getId()) ?  null : this.productUserDao.load(ProductUser.class, info.getId());
		if(isAdded = (data == null)){
			if(StringUtils.isEmpty(info.getId())){
				info.setId(UUID.randomUUID().toString());
			}
			data = new ProductUser();
			info.setCreateTime(new Date());
		}
		BeanUtils.copyProperties(info, data);
		//新增数据。
		if(isAdded){
			this.productUserDao.save(data);
		}else{
			data.setLastTime(new Date());
			info.setLastTime(data.getLastTime());
		}
		info.setStatusName(this.loadStatusName(data.getStatus()));
		return info;
	}

	@Override
	public void delete(String[] ids) {
		if (logger.isDebugEnabled())
			logger.debug("删除数据...");
		if (ids == null || ids.length == 0)
			return;
		for (int i = 0; i < ids.length; i++) {
			if (StringUtils.isEmpty(ids[i]))
				continue;
			ProductUser data = this.productUserDao.load(ProductUser.class, ids[i]);
			if (data != null) {
				logger.debug("删除产品用户数据：" + ids[i]);
				this.productUserDao.delete(data);
			}
		}
	}
	
	@Override
	public Integer loadMaxCode() {
		if(logger.isDebugEnabled()) logger.debug("加载最大代码值...");
		List<ProductUser> sources = this.productUserDao.findProductUsers(new ProductUserInfo(){
			private static final long serialVersionUID = 1L;
			@Override
			public String getSort() {return "code"; } 
			@Override
			public String getOrder() { return "desc";}
		});
		if(sources != null && sources.size() > 0){
			return new Integer(sources.get(0).getCode());
		}
		return null;
	}
	/*
	 * 加载状态名称。
	 * @see com.examw.test.service.products.IProductUserService#loadStatusName(java.lang.Integer)
	 */
	@Override
	public String loadStatusName(Integer status) {
		if(status == null || this.statusMap == null || this.statusMap.size() == 0) return null;
		return this.statusMap.get(status);
	}
}