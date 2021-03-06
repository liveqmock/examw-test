package com.examw.test.dao.library;

import java.util.List;

import com.examw.test.dao.IBaseDao;
import com.examw.test.domain.library.Structure;
import com.examw.test.domain.library.StructureItem;
import com.examw.test.model.library.StructureItemInfo;
/**
 * 试卷结构数据接口。
 * 
 * @author yangyong
 * @since 2014年8月14日
 */
public interface IStructureDao extends IBaseDao<Structure> {
	/**
	 * 加载试卷下的结构数据。
	 * @param paperId
	 * 所属试卷ID。
	 * @return
	 */
	List<Structure> loadStructures(String paperId);
	/**
	 * 加载试卷试题。
	 * @param structureId
	 * 所属试卷结构ID。
	 * @param itemId
	 * 所属试题ID。
	 * @return
	 * 试卷试题。
	 */
	StructureItem loadStructureItem(String structureId, String itemId);
	/**
	 *  查询试卷试题。
	 * @param info
	 * 查询条件。
	 * @return
	 * 试题集合。
	 */
	List<StructureItem> findItems(StructureItemInfo info);
	/**
	 * 统计查询试卷试题。
	 * @param info
	 * 查询条件。
	 * @return
	 * 统计结果。
	 */
	Long totalItems(StructureItemInfo info);
	/**
	 * 加载试卷结构最大排序号。
	 * @param paperId
	 * 所属试卷ID。
	 * @return
	 */
	Integer loadPaperMaxOrder(String paperId);
	/**
	 * 加载试卷结构下试题最大排序号。
	 * @param structureId
	 * 结构ID。
	 * @return
	 */
	Integer loadItemMaxOrder(String structureId);
	/**
	 * 统计结构下的试题数量。
	 * @param structureId
	 * 所属结构ID。
	 * @return
	 * 试题数量。
	 */
	Long totalStructureItems(String structureId);
	/**
	 * 统计试题被试卷关联次数。
	 * @param itemId
	 * 所属试题ID。
	 * @return
	 * 关联次数。
	 */
	Long totalItemCount(String itemId);
	/**
	 * 删除试卷结构试题。
	 * @param structureId
	 * 所属试卷结构ID。
	 * @param itemId
	 * 试题ID。
	 */
	Integer deleteStructureItems(String structureId,String itemId);
}