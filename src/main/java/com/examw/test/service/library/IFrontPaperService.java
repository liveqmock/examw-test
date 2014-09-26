package com.examw.test.service.library;
/**
 * 前端试卷服务接口。
 * 
 * @author yangyong
 * @since 2014年9月23日
 */
public interface IFrontPaperService {
	/**
	 * 加载科目下的试卷数量。
	 * @param subjetsId
	 * 科目ID集合。
	 * @return
	 * 试卷数量。
	 */
	Integer loadPapersCount(String[] subjetsId);
	/**
	 * 加载科目下试题数量。
	 * @param subjectsId
	 * 科目ID集合。
	 * @return
	 * 试题数量。
	 */
	Integer loadItemsCount(String[] subjectsId);
	/**
	 * 是否包含真题试卷。
	 * @param subjectsId
	 * 科目ID集合。
	 * @return
	 */
	boolean hasRealItem(String[] subjectsId);
}