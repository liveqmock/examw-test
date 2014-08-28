package com.examw.test.controllers.syllabus;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.examw.model.DataGrid;
import com.examw.model.Json;
import com.examw.test.domain.security.Right;
import com.examw.test.model.syllabus.KnowledgeInfo;
import com.examw.test.model.syllabus.TextBookInfo;
import com.examw.test.service.syllabus.ITextBookService;
/**
 * 教材控制器。
 * @author lq.
 * @since 2014-8-06.
 */
@Controller
@RequestMapping(value = "/syllabus/textbook")
public class TextBookController {
	private static final Logger logger = Logger.getLogger(TextBookController.class);
	//教材服务。
	@Resource
	private ITextBookService bookService;
	/**
	 * 获取列表页面。
	 * @return
	 */
	@RequiresPermissions({ModuleConstant.SYLLABUS_TEXTBOOK+ ":" + Right.VIEW})
	@RequestMapping(value={"","/list"}, method = RequestMethod.GET)
	public String list(Model model){
		if(logger.isDebugEnabled()) logger.debug("加载列表页面...");
		model.addAttribute("PER_UPDATE",ModuleConstant.SYLLABUS_TEXTBOOK + ":" + Right.UPDATE);
		model.addAttribute("PER_DELETE",ModuleConstant.SYLLABUS_TEXTBOOK + ":" + Right.DELETE);
		return "syllabus/book_list";
	}
	/**
	 * 获取编辑页面。
	 * @return
	 * 编辑页面。
	 */
	@RequiresPermissions({ModuleConstant.SYLLABUS_TEXTBOOK+ ":" + Right.VIEW})
	@RequestMapping(value="/edit", method = RequestMethod.GET)
	public String edit(String examId,String subId,Model model){
		if(logger.isDebugEnabled()) logger.debug("加载编辑页面...");
		model.addAttribute("CURRENT_EXAM_ID", examId);
		model.addAttribute("CURRENT_SUB_ID", subId);
		return "syllabus/book_edit";
	}
	/**
	 * 获取知识点编辑页面。
	 * @return
	 * 编辑页面。
	 */
	@RequiresPermissions({ModuleConstant.SYLLABUS_TEXTBOOK+ ":" + Right.VIEW})
	@RequestMapping(value="/syll/edit/{bookId}", method = RequestMethod.GET)
	public String editKnow(@PathVariable String bookId,Model model){
		if(logger.isDebugEnabled()) logger.debug("加载知识点编辑页面...");
		model.addAttribute("CURRENT_BOOK_ID", bookId);
		model.addAttribute("PER_UPDATE",ModuleConstant.SYLLABUS_TEXTBOOK + ":" + Right.UPDATE);
		model.addAttribute("PER_DELETE",ModuleConstant.SYLLABUS_TEXTBOOK + ":" + Right.DELETE);
		return "syllabus/know_edit";
	}
	/**
	 * 教材下知识点数据列表。
	 * @param info
	 * 查询知识点。
	 * @return
	 * 查询结果。
	 */
	@RequiresPermissions({ModuleConstant.SYLLABUS_TEXTBOOK+ ":" + Right.VIEW})
	@RequestMapping(value="/{bookId}/datagrid", method = RequestMethod.POST)
	@ResponseBody
	public DataGrid<KnowledgeInfo> dgStructureItems(@PathVariable String bookId, KnowledgeInfo info){
		if(logger.isDebugEnabled()) logger.debug("加载教材下的［"+bookId+"］知识点数据列表...");
		return this.bookService.findKnowledge(bookId, info);
	}
	/**
	 * 获取添加知识点页面。
	 * @return
	 * 添加页面。
	 */
	@RequiresPermissions({ModuleConstant.SYLLABUS_TEXTBOOK+ ":" + Right.VIEW})
	@RequestMapping(value="/add", method = RequestMethod.GET)
	public String add(String bookId,String syllId,Model model){
		if(logger.isDebugEnabled()) logger.debug("加载添加知识点页面...");
		model.addAttribute("CURRENT_BOOK_ID", bookId);
		model.addAttribute("CURRENT_SYLL_ID", syllId);
		return "syllabus/know_add";
	}
	/**
	 * 查询数据。
	 * @return
	 */
	@RequiresPermissions({ModuleConstant.SYLLABUS_TEXTBOOK + ":" + Right.VIEW})
	@RequestMapping(value="/datagrid", method = RequestMethod.POST)
	@ResponseBody
	public DataGrid<TextBookInfo> datagrid(TextBookInfo info){
		if(logger.isDebugEnabled()) logger.debug("加载列表数据...");
		return this.bookService.datagrid(info);
	}
	/**
	 * 更新数据。
	 * @param info
	 * 更新源数据。
	 * @return
	 * 更新后数据。
	 */
	@RequiresPermissions({ModuleConstant.SYLLABUS_TEXTBOOK+ ":" + Right.VIEW})
	@RequestMapping(value="/update", method = RequestMethod.POST)
	@ResponseBody
	public Json update(TextBookInfo info){
		if(logger.isDebugEnabled()) logger.debug("更新数据...");
		Json result = new Json();
		try {
			result.setData(this.bookService.update(info));
			result.setSuccess(true);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMsg(e.getMessage());
			logger.error("更新教材数据发生异常", e);
		}
		return result;
	}
	/**
	 * 更新知识点数据。
	 * @param info
	 * 更新源数据。
	 * @return
	 * 更新后数据。
	 */
	@RequiresPermissions({ModuleConstant.SYLLABUS_TEXTBOOK+ ":" + Right.VIEW})
	@RequestMapping(value="/{bookId}/update", method = RequestMethod.POST)
	@ResponseBody
	public Json updateStructure(@PathVariable String bookId, KnowledgeInfo info){
		if(logger.isDebugEnabled()) logger.debug("更新知识点数据...");
		Json result = new Json();
		try {
			result.setData(this.bookService.updateKnowledge(bookId, info));
			 result.setSuccess(true);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMsg(e.getMessage());
			logger.error("更新知识点数据发生异常", e);
		}
		return result;
	}
	/**
	 * 加载知识点代码值。
	 * @return
	 */
	@RequiresPermissions({ModuleConstant.SYLLABUS_TEXTBOOK + ":" + Right.VIEW})
	@RequestMapping(value="/code", method = RequestMethod.GET)
	@ResponseBody
	public String[] code(){
		Integer max = this.bookService.loadMaxCode();
		if(max == null) max = 0;
		return new String[]{ String.format("%06d", max + 1) };
	}
	/**
	 * 删除数据。
	 * @param id
	 * @return
	 */
	@RequiresPermissions({ModuleConstant.SYLLABUS_TEXTBOOK + ":" + Right.VIEW})
	@RequestMapping(value="/delete", method = RequestMethod.POST)
	@ResponseBody
	public Json delete(String id){
		if(logger.isDebugEnabled()) logger.debug("删除数据［"+ id +"］...");
		Json result = new Json();
		try {
			this.bookService.delete(id.split("\\|"));
			result.setSuccess(true);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMsg(e.getMessage());
			logger.error("删除数据教材["+id+"]时发生异常:", e);
		}
		return result;
	}
}