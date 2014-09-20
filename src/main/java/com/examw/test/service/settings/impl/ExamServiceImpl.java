package com.examw.test.service.settings.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import com.examw.test.dao.settings.IAreaDao;
import com.examw.test.dao.settings.ICategoryDao;
import com.examw.test.dao.settings.IExamDao;
import com.examw.test.domain.settings.Area;
import com.examw.test.domain.settings.Category;
import com.examw.test.domain.settings.Exam;
import com.examw.test.model.settings.ExamInfo;
import com.examw.test.service.impl.BaseDataServiceImpl;
import com.examw.test.service.settings.IExamService;

/**
 * 考试服务接口实现类
 * @author fengwei.
 * @since 2014年8月7日 上午10:00:32.
 */
public class ExamServiceImpl extends BaseDataServiceImpl<Exam, ExamInfo> implements IExamService {
	private static final Logger logger = Logger.getLogger(ExamServiceImpl.class);
	private IAreaDao areaDao;
	private ICategoryDao categoryDao;
	private IExamDao examDao;
	/**
	 * 设置 地区数据接口
	 * @param areaDao
	 * 
	 */
	public void setAreaDao(IAreaDao areaDao) {
		this.areaDao = areaDao;
	}
	/**
	 * 设置 考试分类接口
	 * @param categoryDao
	 * 
	 */
	public void setCategoryDao(ICategoryDao categoryDao) {
		this.categoryDao = categoryDao;
	}
	/**
	 * 设置 考试接口
	 * @param examDao
	 * 
	 */
	public void setExamDao(IExamDao examDao) {
		this.examDao = examDao;
	}
	/*
	 * 查询数据
	 * @see com.examw.test.service.impl.BaseDataServiceImpl#find(java.lang.Object)
	 */
	@Override
	protected List<Exam> find(ExamInfo info) {
		if (logger.isDebugEnabled())
			logger.debug("查询[考试]数据...");
		return this.examDao.findExams(info);
	}
	/*
	 * 数据模型转换
	 * @see com.examw.test.service.impl.BaseDataServiceImpl#changeModel(java.lang.Object)
	 */
	@Override
	protected ExamInfo changeModel(Exam data) {
		if(data == null) return null;
		ExamInfo info = new ExamInfo();
		BeanUtils.copyProperties(data, info);
		if(data.getCategory() != null){
			info.setCategoryId(data.getCategory().getId());
			info.setCategoryName(data.getCategory().getName());
		}
		Set<Area> areas = null;
		if((areas = data.getAreas()) != null){
			List<String> listAreaId = new ArrayList<>(), listAreaName = new ArrayList<>();
			for(Area area : areas){
				if(area == null) continue;
				listAreaId.add(area.getId());
				listAreaName.add(area.getName());
			}
			info.setAreaId(listAreaId.toArray(new String[0]));
			info.setAreaName(listAreaName.toArray(new String[0]));
		}
		return info;
	}
	/*
	 * 类型转换。
	 * @see com.examw.test.service.settings.IExamService#conversion(com.examw.test.domain.settings.Exam)
	 */
	@Override
	public ExamInfo conversion(Exam exam) {
		if(logger.isDebugEnabled()) logger.debug("考试类型转换...");
		return this.changeModel(exam);
	}
	/*
	 * 查询数据总数
	 * @see com.examw.test.service.impl.BaseDataServiceImpl#total(java.lang.Object)
	 */
	@Override
	protected Long total(ExamInfo info) {
		return this.examDao.total(info);		
	}
	/*
	 * 更新或插入数据
	 * @see com.examw.test.service.impl.BaseDataServiceImpl#update(java.lang.Object)
	 */
	@Override
	public ExamInfo update(ExamInfo info) {
		if(info == null) return null;
		boolean isAdded = false;
		Exam data = StringUtils.isEmpty(info.getId()) ? null : this.examDao.load(Exam.class, info.getId());
		if(isAdded = (data == null)){
			if(StringUtils.isEmpty(info.getId())){
				info.setId(UUID.randomUUID().toString());
			}
			data = new Exam();
		}
		BeanUtils.copyProperties(info, data);
		//设置考试分类
		if(!StringUtils.isEmpty(info.getCategoryId()) && (data.getCategory() == null || !data.getCategory().getId().equalsIgnoreCase(info.getCategoryId()))){
			Category category = this.categoryDao.load(Category.class, info.getCategoryId());
			if(category != null) data.setCategory(category);
		}
		if(data.getCategory() != null){
			info.setCategoryName(data.getCategory().getName());
		}
		//设置地区
		List<String> listAreaName = new ArrayList<>();
		Set<Area> areas = new HashSet<>();
		if(info.getAreaId() != null){
			for(String areaId : info.getAreaId()){
				if(StringUtils.isEmpty(areaId)) continue;
				Area area = this.areaDao.load(Area.class, areaId);
				if(area != null){
					areas.add(area);
					listAreaName.add(area.getName());
				}
			}
		}
		data.setAreas(areas);
		info.setAreaName(listAreaName.toArray(new String[0]));
		if(isAdded)this.examDao.save(data);
		return info;
	}
	/*
	 * 删除数据
	 * @see com.examw.test.service.impl.BaseDataServiceImpl#delete(java.lang.String[])
	 */
	@Override
	public void delete(String[] ids) {
		if(ids == null || ids.length == 0) return;
		for(int i = 0; i < ids.length; i++){
			Exam data = this.examDao.load(Exam.class, ids[i]);
			if(data != null){
				this.examDao.delete(data);
			}
		}
	}
	/*
	 * 加载最大的代码值
	 * @see com.examw.test.service.settings.IExamService#loadMaxCode()
	 */
	@Override
	public Integer loadMaxCode() {
		return this.examDao.loadMaxCode();
	}
	/*
	 * 加载
	 * @see com.examw.test.service.settings.IExamService#loadExam(java.lang.String)
	 */
	@Override
	public Exam loadExam(String examId) {
		return this.examDao.load(Exam.class, examId);
	}
}
