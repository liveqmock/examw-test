package com.examw.test.controllers.library;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.examw.model.DataGrid;
import com.examw.model.Json;
import com.examw.model.TreeNode;
import com.examw.test.domain.library.Item;
import com.examw.test.domain.library.Paper;
import com.examw.test.domain.security.Right;
import com.examw.test.model.library.PaperInfo;
import com.examw.test.model.library.StructureInfo;
import com.examw.test.service.library.IItemService;
import com.examw.test.service.library.IPaperService;
import com.examw.test.service.library.PaperStatus;

/**
 * 试卷控制器。
 * 
 * @author yangyong
 * @since 2014年8月8日
 */
@Controller
@RequestMapping(value = "/library/paper")
public class PaperController {
	private static final Logger logger = Logger.getLogger(PaperController.class);
	//注入试卷服务。
	@Resource
	private IPaperService paperService;
	//注入题目服务。
	@Resource
	private IItemService itemService;
	/**
	 * 列表页面。
	 * @param model
	 * @return
	 */
	@RequiresPermissions({ModuleConstant.LIBRARY_PAPER + ":" + Right.VIEW})
	@RequestMapping(value={"","/list"}, method = RequestMethod.GET)
	public String list(Model model){
		if(logger.isDebugEnabled()) logger.debug("加载列表页面...");
		model.addAttribute("PER_UPDATE", ModuleConstant.LIBRARY_PAPER + ":" + Right.UPDATE);
		model.addAttribute("PER_DELETE", ModuleConstant.LIBRARY_PAPER + ":" + Right.DELETE);
		
		model.addAttribute("PAPER_STATUS_NONE", PaperStatus.NONE.getValue());
		
		return "library/paper_list";
	}
	/**
	 * 加载列表页面数据。
	 * @param info
	 * @return
	 */
	@RequiresPermissions({ModuleConstant.LIBRARY_SOURCE + ":" + Right.VIEW})
	@RequestMapping(value="/datagrid", method = RequestMethod.POST)
	@ResponseBody
	public DataGrid<PaperInfo> datagrid(PaperInfo info){
		if(logger.isDebugEnabled()) logger.debug("加载列表页面数据...");
		return this.paperService.datagrid(info);
	}
	/**
	 * 加载全部数据。
	 * @param examId
	 * @param subjectId
	 * @return
	 */
	@RequestMapping(value="/all", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public List<PaperInfo> all(final String examId,final String subjectId){
		return this.paperService.datagrid(new PaperInfo(){
			private static final long serialVersionUID = 1L;
			@Override
			public String getExamId(){return examId;}
			@Override
			public String getSubjectId(){return subjectId;}
			@Override
			public String getSort(){return "name";}
			@Override
			public String getOrder(){return "asc";}
		}).getRows();
	}
	/**
	 * 加载编辑页面。
	 * @param model
	 * @return
	 */
	@RequiresPermissions({ModuleConstant.LIBRARY_PAPER + ":" + Right.UPDATE})
	@RequestMapping(value="/edit", method = RequestMethod.GET)
	public String edit(String examId,String subjectId,Model model){
		if(logger.isDebugEnabled()) logger.debug("加载编辑页面...");
		model.addAttribute("CURRENT_EXAM_ID", StringUtils.isEmpty(examId) ? "" : examId.trim());
		model.addAttribute("CURRENT_SUBJECT_ID", StringUtils.isEmpty(subjectId) ? "" : subjectId.trim());
		
		model.addAttribute("CURRENT_YEAR", new SimpleDateFormat("yyyy").format(new Date()));
		
		model.addAttribute("TYPE_REAL_VALUE", Paper.TYPE_REAL);
		model.addAttribute("TYPE_REAL_NAME", this.paperService.loadTypeName(Paper.TYPE_REAL));
		model.addAttribute("TYPE_SIMU_VALUE", Paper.TYPE_SIMU);
		model.addAttribute("TYPE_SIMU_NAME", this.paperService.loadTypeName(Paper.TYPE_SIMU));
		model.addAttribute("TYPE_FORECAST_VALUE", Paper.TYPE_FORECAST);
		model.addAttribute("TYPE_FORECAST_NAME", this.paperService.loadTypeName(Paper.TYPE_FORECAST));
		model.addAttribute("TYPE_PRACTICE_VALUE", Paper.TYPE_PRACTICE);
		model.addAttribute("TYPE_PRACTICE_NAME", this.paperService.loadTypeName(Paper.TYPE_PRACTICE));
		
		return "library/paper_edit";
	}
	/**
	 * 更新数据。
	 * @param info
	 * @return
	 */
	@RequiresPermissions({ModuleConstant.LIBRARY_PAPER + ":" + Right.UPDATE})
	@RequestMapping(value="/update", method = RequestMethod.POST)
	@ResponseBody
	public Json update(PaperInfo info){
		if(logger.isDebugEnabled()) logger.debug("更新数据...");
		Json result = new Json();
		try {
			 result.setData(this.paperService.update(info));
			 result.setSuccess(true);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMsg(e.getMessage());
			logger.error("更新数据发生异常", e);
		}
		return result;
	}
	/**
	 * 删除数据。
	 * @param id
	 * @return
	 */
	@RequiresPermissions({ModuleConstant.LIBRARY_PAPER + ":" + Right.DELETE})
	@RequestMapping(value="/delete", method = RequestMethod.POST)
	@ResponseBody
	public Json delete(String id){
		if(logger.isDebugEnabled()) logger.debug(String.format("删除数据［%s］...", id));
		Json result = new Json();
		try {
			this.paperService.delete(id.split("\\|"));
			result.setSuccess(true);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMsg(e.getMessage());
			logger.error(String.format("删除数据[%s]时发生异常:",id), e);
		}
		return result;
	}
	/**
	 * 加载试卷结构列表页面。
	 * @param model
	 * @return
	 */
	@RequiresPermissions({ModuleConstant.LIBRARY_PAPER + ":" + Right.VIEW})
	@RequestMapping(value="/structure/{paperId}", method = RequestMethod.GET)
	public String structureList(@PathVariable String paperId, Model model){
		if(logger.isDebugEnabled()) logger.debug(String.format("加载试卷［paperId = %s］结构列表页面...", paperId));
		model.addAttribute("CURRENT_PAPER_ID", paperId);
		
		model.addAttribute("PER_UPDATE", ModuleConstant.LIBRARY_PAPER + ":" + Right.UPDATE);
		model.addAttribute("PER_DELETE", ModuleConstant.LIBRARY_PAPER + ":" + Right.DELETE);
		
		return "library/paper_structure_list";
	}
	/**
	 * 加载试卷结构树数据。
	 * @param paperId
	 * 所属试卷ID。
	 * @return
	 */
	@RequestMapping(value="/structure/tree/{paperId}", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public List<TreeNode> structureTree(@PathVariable String paperId,String ignoreStructureId){
		if(logger.isDebugEnabled()) logger.debug(String.format("加载试卷［paperId = %s］结构树数据...", paperId));
		List<TreeNode> treeNodes = new ArrayList<>();
		if(!StringUtils.isEmpty(paperId)){
			List<StructureInfo> list = this.paperService.loadStructures(paperId);
			if(list != null && list.size() > 0){
				for(StructureInfo info : list){
					if(info == null) continue;
					TreeNode node = new TreeNode();
					if(this.createStructureTree(info, node, ignoreStructureId)){
						treeNodes.add(node);
					}
				}
			}
		}
		return treeNodes;
	}
	//创建结构树。
	private boolean createStructureTree(StructureInfo source, TreeNode target, String ignoreStructureId){
		if(source == null || target == null) return false;
		if(StringUtils.isEmpty(source.getId()) || StringUtils.isEmpty(source.getTitle())) return false;
		if(!StringUtils.isEmpty(ignoreStructureId) && source.getId().equalsIgnoreCase(ignoreStructureId)) return false;
		
		target.setId(source.getId());
		target.setText(source.getTitle());
		
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("pid", source.getPid());
		attributes.put("id", source.getId());
		attributes.put("title", source.getTitle());
		attributes.put("type", source.getType());
		attributes.put("score", source.getScore());
		attributes.put("orderNo", source.getOrderNo());
		target.setAttributes(attributes);
		
		if(source.getChildren() != null && source.getChildren().size() > 0){
			List<TreeNode> children = new ArrayList<>();
			for(StructureInfo info : source.getChildren()){
				if(info == null) continue;
				TreeNode node = new TreeNode();
				if(this.createStructureTree(info, node, ignoreStructureId)){
					children.add(node);
				}
			}
			if(children.size() > 0) target.setChildren(children);
		}
		return true;
	}
	/**
	 * 加载试卷结构编辑页面。
	 * @param model
	 * @return
	 */
	@RequiresPermissions({ModuleConstant.LIBRARY_PAPER + ":" + Right.UPDATE})
	@RequestMapping(value="/structure/edit/{paperId}", method = RequestMethod.GET)
	public String structureEdit(@PathVariable String paperId,String ignoreStructureId,Model model){
		if(logger.isDebugEnabled()) logger.debug(String.format("加载试卷［paperId = %s］结构编辑页面...", paperId));
		model.addAttribute("CURRENT_PAPER_ID", paperId);
		model.addAttribute("IGNORE_STRUCTURE_ID", StringUtils.isEmpty(ignoreStructureId) ? "" : ignoreStructureId);
		this.addItemTypes(model);
		return "library/paper_structure_edit";
	}
	//添加题型
	private void addItemTypes(Model model){
		//单选
		model.addAttribute("TYPE_SINGLE_VALUE", Item.TYPE_SINGLE);
		model.addAttribute("TYPE_SINGLE_NAME", this.itemService.loadTypeName(Item.TYPE_SINGLE));
		//多选
		model.addAttribute("TYPE_MULTY_VALUE", Item.TYPE_MULTY);
		model.addAttribute("TYPE_MULTY_NAME", this.itemService.loadTypeName(Item.TYPE_MULTY));
		//不定向选
		model.addAttribute("TYPE_UNCERTAIN_VALUE", Item.TYPE_UNCERTAIN);
		model.addAttribute("TYPE_UNCERTAIN_NAME", this.itemService.loadTypeName(Item.TYPE_UNCERTAIN));
		//判断
		model.addAttribute("TYPE_JUDGE_VALUE", Item.TYPE_JUDGE);
		model.addAttribute("TYPE_JUDGE_NAME", this.itemService.loadTypeName(Item.TYPE_JUDGE));
		//问答
		model.addAttribute("TYPE_QANDA_VALUE", Item.TYPE_QANDA);
		model.addAttribute("TYPE_QANDA_NAME", this.itemService.loadTypeName(Item.TYPE_QANDA));
		//共提干
		model.addAttribute("TYPE_SHARE_TITLE_VALUE", Item.TYPE_SHARE_TITLE);
		model.addAttribute("TYPE_SHARE_TITLE_NAME", this.itemService.loadTypeName(Item.TYPE_SHARE_TITLE));
		//共答案
		model.addAttribute("TYPE_SHARE_ANSWER_VALUE", Item.TYPE_SHARE_ANSWER);
		model.addAttribute("TYPE_SHARE_ANSWER_NAME", this.itemService.loadTypeName(Item.TYPE_SHARE_ANSWER));
	}
	/**
	 * 更新试卷结构数据。
	 * @param paperId
	 * 所属试卷ID。
	 * @param info
	 * 试卷结构数据。
	 * @return
	 */
	@RequiresPermissions({ModuleConstant.LIBRARY_PAPER + ":" + Right.UPDATE})
	@RequestMapping(value = "/structure/update/{paperId}", method = RequestMethod.POST)
	@ResponseBody
	public Json structureUpdate(@PathVariable String paperId,StructureInfo info){
		if(logger.isDebugEnabled()) logger.debug(String.format("更新试卷［%s］结构数据...", paperId));
		Json result = new Json();
		try {
			 this.paperService.updateStructure(paperId, info);
			 result.setSuccess(true);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMsg(e.getMessage());
			logger.error("更新数据发生异常", e);
		}
		return result;
	}
	/**
	 * 删除试卷结构数据。
	 * @param paperId
	 * 所属试卷ID。
	 * @param id
	 * 删除的结构ID。
	 * @return
	 */
	@RequiresPermissions({ModuleConstant.LIBRARY_PAPER + ":" + Right.DELETE})
	@RequestMapping(value = "/structure/delete/{paperId}", method = RequestMethod.POST)
	@ResponseBody
	public Json structureDelete(@PathVariable String paperId,String structureId){
		if(logger.isDebugEnabled()) logger.debug(String.format("删除试卷［%1$s］结构数据［%2$s］...", paperId, structureId));
		Json result = new Json();
		try {
			this.paperService.deleteStructure(paperId, structureId);
			result.setSuccess(true);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMsg(e.getMessage());
			logger.error(String.format("删除数据[%s]时发生异常:",structureId), e);
		}
		return result;
	}
}