package com.examw.test.controllers.api;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.examw.model.Json;
import com.examw.test.domain.security.User;
import com.examw.test.model.library.PaperInfo;
import com.examw.test.model.library.StructureInfo;
import com.examw.test.model.library.StructureItemInfo;
import com.examw.test.model.settings.ExamInfo;
import com.examw.test.service.library.IItemService;
import com.examw.test.service.library.IPaperItemService;
import com.examw.test.service.library.IPaperService;
import com.examw.test.service.library.IPaperStructureService;
import com.examw.test.service.library.ItemType;
import com.examw.test.service.library.PaperStatus;
import com.examw.test.service.security.IUserAuthorization;
import com.examw.test.service.settings.IExamService;
import com.examw.test.support.PasswordHelper;
import com.examw.utils.MD5Util;
/**
 *  客户端导入控制器。
 * 
 * @author yangyong
 * @since 2014年9月4日
 */
@Controller
@RequestMapping(value = { "/api/imports" })
public class ClientImportsController {
	private static final Logger logger = Logger.getLogger(ClientImportsController.class);
	//注入用户服务。
	@Resource
	private IUserAuthorization userAuthorization;
	//注入密码服务。
	@Resource
	private PasswordHelper passwordHelper;
	//注入考试服务。
	@Resource
	private IExamService examService;
	//注入试卷服务。
	@Resource
	private IPaperService paperService;
	//注入试卷结构服务。
	@Resource
	private IPaperStructureService paperStructureService;
	//注入试卷试题服务。
	@Resource
	private IPaperItemService paperItemService;
	//注入试题服务。
	@Resource
	private IItemService itemService;
	/**
	 * 身份验证。
	 * @param username
	 * @param token
	 * md5(username+md5(username+password))
	 * @return
	 */
	@RequestMapping(value = {"/authen/{username}"}, method = {RequestMethod.POST})
	@ResponseBody
	public Json authention(@PathVariable String username,String token){
		if(logger.isDebugEnabled()) logger.debug(String.format("客户端身份［username=%1$s, token=%2$s］验证...", username, token));
		Json result = new Json();
		try {
			User user = this.userAuthorization.loadUserByAccount(username);
			if(user == null){
				result.setSuccess(false);
				result.setMsg(String.format("用户[%s]不存在！", username));
				return result;
			}
			String ntoken = MD5Util.MD5(String.format("%1$s%2$s",username, MD5Util.MD5(String.format("%1$s%2$s", username, this.passwordHelper.decryptAESPassword(user)))));
			if(logger.isDebugEnabled()) logger.debug(String.format("=>%s", ntoken));
			result.setSuccess(ntoken.equals(token));
			if(!result.isSuccess()){
				result.setMsg("密码错误！");
			}
		} catch (Exception e) {
			result.setSuccess(false);
		    result.setMsg(e.getMessage());
		}
		return result;
	}
	/**
	 * 加载全部考试数据。
	 * @return
	 */
	@RequestMapping(value = {"/exams"}, method = {RequestMethod.GET})
	@ResponseBody
	public List<ExamInfo> loadAllExams(){
		if(logger.isDebugEnabled())logger.debug("加载考试数据...");
		return this.examService.datagrid(new ExamInfo(){
			private static final long serialVersionUID = 1L;
			@Override
			public String getSort(){return "name";}
			@Override
			public String getOrder(){return "asc";}
		}).getRows();
	}
	/**
	 * 加载考试下的试卷数据。
	 * @param examId
	 * 所属考试ID。
	 * @return
	 */
	@RequestMapping(value = {"/papers/{examId}"}, method = { RequestMethod.GET })
	@ResponseBody
	public List<PaperInfo> loadPapers(@PathVariable final String examId){
		if(logger.isDebugEnabled()) logger.debug(String.format("加载考试［examid=%s］下的试卷数据...", examId));
		return this.paperService.datagrid(new PaperInfo(){
			private static final long serialVersionUID = 1L;
			@Override
			public String getExamId(){return examId;}
			@Override
			public Integer getStatus() { return PaperStatus.NONE.getValue();}
			@Override
			public String getSort(){return "name";}
			@Override
			public String getOrder(){return "asc";}
		}).getRows();
	}
	/**
	 * 加载试卷结构数据。
	 * @param paperId
	 * 所属试卷ID。
	 * @return
	 * 结构数据。
	 */
	@RequestMapping(value = {"/structures/{paperId}"}, method = { RequestMethod.GET })
	@ResponseBody
	public List<StructureInfo> loadStructures(@PathVariable final String paperId){
		if(logger.isDebugEnabled()) logger.debug(String.format("加载试卷［paperId=%s］的结构...", paperId));
		return this.paperStructureService.loadStructures(paperId);
	}
	/**
	 * 加载试题类型。
	 * @return
	 */
	@RequestMapping(value = {"/itemtypes"}, method = { RequestMethod.GET})
	@ResponseBody
	public List<String[]> loadItemTypes(){
		if(logger.isDebugEnabled()) logger.debug("加载试题类型集合...");
		List<String[]> list = new ArrayList<>();
		list.add(new String[]{ String.valueOf(ItemType.SINGLE.getValue()), this.itemService.loadTypeName(ItemType.SINGLE.getValue()) });//单选
		list.add(new String[]{ String.valueOf(ItemType.MULTY.getValue()), this.itemService.loadTypeName(ItemType.MULTY.getValue()) });//多选
		list.add(new String[]{ String.valueOf(ItemType.UNCERTAIN.getValue()), this.itemService.loadTypeName(ItemType.UNCERTAIN.getValue()) });//不定向选
		list.add(new String[]{ String.valueOf(ItemType.JUDGE.getValue()), this.itemService.loadTypeName(ItemType.JUDGE.getValue()) });//判断
		list.add(new String[]{ String.valueOf(ItemType.QANDA.getValue()), this.itemService.loadTypeName(ItemType.QANDA.getValue()) });//问答
		list.add(new String[]{ String.valueOf(ItemType.SHARE_TITLE.getValue()), this.itemService.loadTypeName(ItemType.SHARE_TITLE.getValue()) });//共提干
		list.add(new String[]{ String.valueOf(ItemType.SHARE_ANSWER.getValue()), this.itemService.loadTypeName(ItemType.SHARE_ANSWER.getValue()) });//共答案
		return list;
	}
	/**
	 * 更新试题。
	 * @param info
	 * @return
	 */
	@RequestMapping(value="/update/{paperId}/{structureId}", method = RequestMethod.POST)
	@ResponseBody
	public Json update(@PathVariable String paperId,@PathVariable String structureId,@RequestBody StructureItemInfo info){
		if(logger.isDebugEnabled()) logger.debug(String.format("更新试卷［%s］..", paperId));
		Json result = new Json();
		try {
			info.setPaperId(paperId);
			info.setStructureId(structureId);
			this.paperItemService.update(info);
			result.setSuccess(true);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMsg(e.getMessage());
			logger.error(e);
		}
		return result;
	}
}