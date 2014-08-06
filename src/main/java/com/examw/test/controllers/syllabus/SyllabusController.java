package com.examw.test.controllers.syllabus;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.examw.model.Json;
import com.examw.model.TreeNode;
import com.examw.test.domain.security.Right;
import com.examw.test.model.syllabus.SyllabusInfo;
import com.examw.test.service.syllabus.ISyllabusService;
/**
 * 大纲控制器。
 * @author lq.
 * @since 2014-8-06.
 */
@Controller
@RequestMapping(value = "/syllabus/syllabus")
public class SyllabusController {
	private static final Logger logger = Logger.getLogger(PressController.class);
	//大纲服务。
	@Resource
	private ISyllabusService syllabusService;
	/**
	 * 获取列表页面。
	 * @return
	 */
	@RequiresPermissions({ModuleConstant.SYLLABUSS_SYLLABUS+ ":" + Right.VIEW})
	@RequestMapping(value={"","/list"}, method = RequestMethod.GET)
	public String list(Model model){
		if(logger.isDebugEnabled()) logger.debug("加载列表页面...");
		return "syllabus/syllabus_list";
	}
	/**
	 * 获取编辑页面。
	 * @return
	 * 编辑页面。
	 */
	@RequiresPermissions({ModuleConstant.SYLLABUSS_SYLLABUS+ ":" + Right.VIEW})
	@RequestMapping(value="/edit", method = RequestMethod.GET)
	public String edit(Model model){
		if(logger.isDebugEnabled()) logger.debug("加载编辑页面...");
		return "syllabus/syllabus_edit";
	}
	/**
	 * 更新数据。
	 * @param info
	 * 更新源数据。
	 * @return
	 * 更新后数据。
	 */
	@RequiresPermissions({ModuleConstant.SYLLABUSS_SYLLABUS+ ":" + Right.VIEW})
	@RequestMapping(value="/update", method = RequestMethod.POST)
	@ResponseBody
	public Json update(SyllabusInfo info){
		if(logger.isDebugEnabled()) logger.debug("更新数据...");
		Json result = new Json();
		try {
			result.setData(this.syllabusService.update(info));
			result.setSuccess(true);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMsg(e.getMessage());
			logger.error("更新大纲数据发生异常", e);
		}
		return result;
	}
	/**
	 * 删除数据。
	 * @param id
	 * @return
	 */
	@RequiresPermissions({ModuleConstant.SYLLABUSS_SYLLABUS+ ":" + Right.VIEW})
	@RequestMapping(value="/delete", method = RequestMethod.POST)
	@ResponseBody
	public Json delete(String id){
		if(logger.isDebugEnabled()) logger.debug("删除数据［"+ id +"］...");
		Json result = new Json();
		try {
			this.syllabusService.delete(id.split("\\|"));
			result.setSuccess(true);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMsg(e.getMessage());
			logger.error("删除数据["+id+"]时发生异常:", e);
		}
		return result;
	}
	/**
	 * 加载部门下的岗位树.
	 * @param deptId
	 * @param ignore
	 * @return
	 */
	@RequestMapping(value="/tree/{subId}", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public List<TreeNode> tree(@PathVariable String subId,String ignore){
		if(logger.isDebugEnabled()) logger.debug("加载科目［"+subId+"］下的大纲［ignore="+ignore+"］树...");
		return this.syllabusService.loadSyllabuss(subId, ignore);
	}
}