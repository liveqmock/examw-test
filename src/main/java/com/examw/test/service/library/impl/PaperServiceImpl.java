package com.examw.test.service.library.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import com.examw.test.dao.library.IPaperDao;
import com.examw.test.dao.library.ISourceDao;
import com.examw.test.dao.library.IStructureDao;
import com.examw.test.dao.settings.IAreaDao;
import com.examw.test.dao.settings.ISubjectDao;
import com.examw.test.domain.library.Paper;
import com.examw.test.domain.library.Source;
import com.examw.test.domain.library.Structure;
import com.examw.test.domain.library.StructureItem;
import com.examw.test.domain.settings.Area;
import com.examw.test.domain.settings.Subject;
import com.examw.test.model.library.PaperInfo;
import com.examw.test.service.impl.BaseDataServiceImpl;
import com.examw.test.service.library.IPaperService;
import com.examw.test.service.library.ItemStatus;
import com.examw.test.service.library.PaperStatus;
/**
 * 试卷服务接口实现类。
 * 
 * @author yangyong.
 * @since 2014-08-06.
 */
public class PaperServiceImpl extends BaseDataServiceImpl<Paper, PaperInfo> implements IPaperService {
	private static final Logger logger = Logger.getLogger(PaperServiceImpl.class);
	private IPaperDao paperDao;
	private ISourceDao sourceDao;
	private ISubjectDao subjectDao;
	private IStructureDao structureDao;
	private IAreaDao areaDao;
	private Map<Integer, String> statusMap;
	private Map<String, String> typeMap;	//修改为Map<String,String>
	/**
	 * 设置试卷数据接口。
	 * @param paperDao
	 * 试卷数据接口。
	 */
	public void setPaperDao(IPaperDao paperDao) {
		if (logger.isDebugEnabled())logger.debug("注入试卷数据接口...");
		this.paperDao = paperDao;
	}
	/**
	 * 设置来源数据接口。
	 * @param sourceDao
	 * 来源数据接口。
	 */
	public void setSourceDao(ISourceDao sourceDao) {
		if(logger.isDebugEnabled())logger.debug("注入来源数据接口...");
		this.sourceDao = sourceDao;
	}
	/**
	 * 设置所属科目数据接口。
	 * @param subjectDao
	 * 所属科目数据接口。
	 */
	public void setSubjectDao(ISubjectDao subjectDao) {
		if(logger.isDebugEnabled())logger.debug("注入科目数据接口...");
		this.subjectDao = subjectDao;
	}
	/**
	 * 设置试卷结构数据接口。
	 * @param structureDao
	 * 试卷结构数据接口。
	 */
	public void setStructureDao(IStructureDao structureDao) {
		if(logger.isDebugEnabled())logger.debug("注入试卷数据接口...");
		this.structureDao = structureDao;
	}
	/**
	 * 设置所属地区数据接口。
	 * @param areaDao 
	 *	  所属地区数据接口。
	 */
	public void setAreaDao(IAreaDao areaDao) {
		this.areaDao = areaDao;
	}
	/**
	 * 设置试卷类型集合。 
	 * @param typeMap
	 * 试卷类型集合。
	 */
	public void setTypeMap(Map<String, String> typeMap) {
		if(logger.isDebugEnabled()) logger.debug("注入试卷类型集合...");
		this.typeMap = typeMap;
	}
	/**
	 * 设置试卷状态集合。
	 * @param statusMap
	 * 试卷状态集合。
	 */
	public void setStatusMap(Map<Integer, String> statusMap) {
		if(logger.isDebugEnabled())logger.debug("注入试卷状态集合...");
		this.statusMap = statusMap;
	}
	/*
	 * 加载试卷类型名称。
	 * @see  com.examw.test.service.library.IPaperService#loadTypeName(java.lang.Integer)
	 */
	@Override
	public String loadTypeName(Integer type) {
		if(logger.isDebugEnabled())logger.debug(String.format("加载试卷类型[type=%d]名称...", type));
		if (type == null || this.typeMap == null || this.typeMap.size() == 0)return null;
		return this.typeMap.get(type.toString());
	}
	/*
	 * 加载试卷状态名称。
	 * @see com.examw.test.service.library.IPaperService#loadStatusName(java.lang.Integer)
	 */
	@Override
	public String loadStatusName(Integer status) {
		if(logger.isDebugEnabled())logger.debug(String.format("加载试卷状态[status=%d]名称...", status));
		if (status == null || this.statusMap == null || this.statusMap.size() == 0) return null;
		return this.statusMap.get(status);
	}
	/*
	 * 查询试卷数据。
	 * @see com.examw.test.service.impl.BaseDataServiceImpl#find(java.lang.Object)
	 */
	@Override
	protected List<Paper> find(PaperInfo info) {
		if (logger.isDebugEnabled())logger.debug("查询数据...");
		return this.paperDao.findPapers(info);
	}
	/*
	 * 试卷模型类型转换。
	 * @see  com.examw.test.service.impl.BaseDataServiceImpl#changeModel(java.lang.Object)
	 */
	@Override
	protected PaperInfo changeModel(Paper data) {
		if (logger.isDebugEnabled())logger.debug("试卷模型类型转换...");
		if (data == null) return null;
		PaperInfo info = new PaperInfo();
		BeanUtils.copyProperties(data, info, new String[] { "structures" });
		if (data.getSubject() != null) {
			info.setSubjectId(data.getSubject().getId());
			info.setSubjectName(data.getSubject().getName());
			if (data.getSubject().getExam() != null) {
				info.setExamId(data.getSubject().getExam().getId());
				info.setExamName(data.getSubject().getExam().getName());
			}
		}
		if (data.getSource() != null) {
			info.setSourceId(data.getSource().getId());
			info.setSourceName(data.getSource().getName());
		}
		if(data.getArea() != null){
			info.setAreaId(data.getArea().getId());
			info.setAreaName(data.getArea().getName());
		}
		if (!StringUtils.isEmpty(data.getType())) info.setTypeName(this.loadTypeName(data.getType()));
		if (!StringUtils.isEmpty(data.getStatus())) info.setStatusName(this.loadStatusName(data.getStatus()));
		return info;
	}
	/*
	 * 数据模型转换。
	 * @see com.examw.test.service.library.IPaperService#conversion(com.examw.test.domain.library.Paper)
	 */
	@Override
	public PaperInfo conversion(Paper paper) {
		if(logger.isDebugEnabled()) logger.debug("数据模型转换 Paper => PaperInfo ...");
		return this.changeModel(paper);
	}
	/*
	 * 查询试卷数据统计。
	 * @see com.examw.test.service.impl.BaseDataServiceImpl#total(java.lang.Object)
	 */
	@Override
	protected Long total(PaperInfo info) {
		if (logger.isDebugEnabled()) logger.debug("查询数据统计...");
		return this.paperDao.total(info);
	}
	/*
	 * 加载试卷基本数据。
	 * @see com.examw.test.service.library.IPaperService#loadPaper(java.lang.String)
	 */
	@Override
	public Paper loadPaper(String paperId) {
		if(logger.isDebugEnabled())logger.debug(String.format("加载试卷[%s]基本数据...", paperId));
		if(StringUtils.isEmpty(paperId)) return null;
		return this.paperDao.load(Paper.class, paperId);
	}
	/*
	 * 更新数据.
	 * @see  com.examw.test.service.impl.BaseDataServiceImpl#update(java.lang.Object)
	 */
	@Override
	public PaperInfo update(PaperInfo info) {
		if (logger.isDebugEnabled()) logger.debug("更新数据...");
		if (info == null) return null;
		boolean isAdded = false;
		Paper data = StringUtils.isEmpty(info.getId()) ? null : this.paperDao.load(Paper.class, info.getId());
		if (isAdded = (data == null)) {
			if (StringUtils.isEmpty(info.getId()))info.setId(UUID.randomUUID().toString());
			info.setCreateTime(new Date());
			info.setStatus(PaperStatus.NONE.getValue());
			data = new Paper();
		} else {
			if (data.getStatus() == PaperStatus.AUDIT.getValue()) throw new RuntimeException("试卷已审核，不能被修改！");
			if (data.getStatus() == PaperStatus.PUBLISH.getValue()) throw new RuntimeException("试卷已发布，不能被修改！");
			info.setStatus(data.getStatus());
			info.setCreateTime(data.getCreateTime());
			info.setPublishTime(data.getPublishTime());
		}
		info.setLastTime(new Date());
		BeanUtils.copyProperties(info, data);
		//科目。
		data.setSubject(StringUtils.isEmpty(info.getSubjectId()) ?  null : this.subjectDao.load(Subject.class,info.getSubjectId()));
		//来源。
		data.setSource(StringUtils.isEmpty(info.getSourceId()) ?  null : this.sourceDao.load(Source.class, info.getSourceId()));
		//地区
		data.setArea(StringUtils.isEmpty(info.getAreaId()) ?  null : this.areaDao.load(Area.class, info.getAreaId()));
		if (isAdded)this.paperDao.save(data);
		return this.changeModel(data);
	}
	/*
	 * 删除数据。
	 * @see com.examw.test.service.impl.BaseDataServiceImpl#delete(java.lang.String[])
	 */
	@Override
	public void delete(String[] ids) {
		if (logger.isDebugEnabled())logger.debug("删除数据....");
		if (ids == null || ids.length == 0) return;
		for (int i = 0; i < ids.length; i++) {
			Paper data = this.paperDao.load(Paper.class, ids[i]);
			if (data != null) {
				if (logger.isDebugEnabled())logger.debug(String.format("删除［%s］...", ids[i]));
				this.paperDao.delete(data);
			}
		}
	}
	/*
	 * 更新数据状态。 
	 * @see com.examw.test.service.library.IPaperService#updateStatus(java.lang.String, com.examw.test.service.library.PaperStatus)
	 */
	@Override
	public void updateStatus(String paperId, PaperStatus status) {
		if (logger.isDebugEnabled())logger.debug(String.format("更新状态：%1$s -> %2$s", paperId, status));
		String msg = null;
		if (StringUtils.isEmpty(paperId)) {
			logger.error(msg = "试卷ID为空！");
			throw new RuntimeException(msg);
		}
		if (status == null) status = PaperStatus.NONE;
		Paper data = this.paperDao.load(Paper.class, paperId);
		if (data == null) {
			msg = String.format("试卷［paperId=%s］不存在！", paperId);
		}
		if (status == PaperStatus.AUDIT) {
			// 将试卷下未审核的题目批量审核。
			List<Structure> structures = this.structureDao.loadStructures(data.getId());
			if(structures == null || structures.size() == 0){
				logger.error(msg = "没有试卷结构不能通过审核！");
				throw new RuntimeException(msg);
			}
			for (Structure structure : structures) {
				this.auditPaperItems(structure);
			} 
		}
		if (status == PaperStatus.PUBLISH) {
			data.setPublishTime(new Date());
		}
		data.setLastTime(new Date());
		if (logger.isDebugEnabled())logger.debug(String.format("更新状态［%1$s,%2$s］%3$s ＝> %4$s",
																			data.getId(), 
																			data.getName(), 
																			PaperStatus.convert(data.getStatus()), 
																			status));
		data.setStatus(status.getValue());
		//更新数据。
		this.paperDao.saveOrUpdate(data);
	}
	// 审核试卷下的题目。
	private void auditPaperItems(Structure structure) {
		if(logger.isDebugEnabled()) logger.debug("审核试卷试题....");
		String msg = null;
		if(structure == null || structure.getItems() == null || structure.getItems().size() == 0){
			logger.error(msg = (structure == null ? "试卷结构不存在！" : String.format("试卷结构［%s］下没有试题！", structure.getTitle())));
			throw new RuntimeException(msg);
		}
		int total = 0;
		for(StructureItem structureItem : structure.getItems()){
			if(structureItem == null || structureItem.getItem() == null) continue;
			total += structureItem.getItem().getCount();
			if(structureItem.getItem().getStatus() != ItemStatus.AUDIT.getValue()){
				structureItem.getItem().setStatus(ItemStatus.AUDIT.getValue());
			}
		}
		if(structure.getTotal() != total){
			logger.error(msg = String.format("试卷结构［%1$s］下试题设置数目［%2$d］与实际试题［%3$d］不一致！", structure.getTitle(), structure.getTotal(),total));
			throw new RuntimeException(msg);
		}
	}
}