package com.examw.test.dao.library;

import java.util.Date;
import java.util.List;

import com.examw.test.dao.IBaseDao;
import com.examw.test.domain.library.PaperRelease;
import com.examw.test.model.library.PaperInfo;
/**
 * 试卷发布数据接口。
 * 
 * @author yangyong
 * @since 2014年8月16日
 */
public interface IPaperReleaseDao extends IBaseDao<PaperRelease> {
	/**
	 * 按试卷类型科目地区加载已发布的试卷集合。
	 * @param paperTypes
	 * 试卷类型。
	 * @param subjectsId
	 * 科目ID集合。
	 * @param areasId
	 * 地区ID集合
	 * @param createTime
	 * 生成时间 [时间范围查询]
	 * @param page
	 * 页码。
	 * @param rows
	 * 行数。
	 * @return
	 */
	List<PaperRelease> loadReleases(Integer[] paperTypes,String[] subjectsId, String[] areasId,Date createTime,Integer page,Integer rows);
	/**
	 * 试卷是否已发布。
	 * @param paperId
	 * 试卷ID。
	 * @return
	 */
	boolean hasRelease(String paperId);
	/**
	 * 删除试卷发布。
	 * @param paperId
	 * 试卷ID。
	 */
	void deleteRelease(String paperId);
	/**
	 * 清理发布。
	 */
	void clearRelease();
	/**
	 * 加载科目下的试卷数量。
	 * @param paperType
	 * 试卷类型。
	 * @param subjetsId
	 * 科目ID集合。
	 * @return
	 * 试卷数量。
	 */
	Integer loadPapersCount(Integer[] paperType,String[] subjetsId,String areaId);
	/**
	 * 加载科目下试题数量。
	 * @param paperType
	 * 试卷类型。
	 * @param subjectsId
	 * 科目ID集合。
	 * @return
	 * 试题数量。
	 */
	Integer loadItemsCount(Integer[] paperType,String[] subjectsId,String areaId);
	/**
	 * 加载试卷下试题数量。
	 * @param paperId
	 * 试卷ID。
	 * @return
	 * 试题数量。
	 */
	Integer loadItemsCount(String paperId);
	/**
	 * 是否包含真题试卷。
	 * @param subjectsId
	 * 科目ID集合。
	 * @return
	 */
	boolean hasRealItem(String[] subjectsId);
	/**
	 * 加载最新试卷集合。
	 * @param examId
	 * 所属考试ID。
	 * @param top
	 * 最新数据条数。
	 * @return
	 * 最新试卷集合。
	 */
	List<PaperRelease> loadNewsReleases(String examId,Integer top);
	/**
	 * 查询试卷发布数据。
	 * @param info
	 * 查询条件。
	 * @return
	 * 数据集合。
	 */
	List<PaperRelease> findPaperReleases(PaperInfo info);
	/**
	 * 查询试卷发布数据统计。
	 * @param info
	 * 查询条件。
	 * @return
	 * 试卷发布数据统计。
	 */
	Long totalPaperReleases(PaperInfo info);
	/**
	 * 查找试卷状态为为审核的发布数据。
	 */
	List<PaperRelease> findTopPapersStatusNone(Integer top);
}