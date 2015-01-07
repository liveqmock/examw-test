package com.examw.test.service.publish.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.examw.test.dao.records.IQuestionDao;
import com.examw.test.domain.publish.StaticPage;
import com.examw.test.domain.records.Question;
import com.examw.test.model.records.QuestionInfo;
import com.examw.test.service.publish.impl.QuestionListTemplateProcess.QuestionListViewData;

/**
 * 常见问题详细模版处理。
 * 
 * @author yangyong
 * @since 2015年1月6日
 */
public class QuestionDetailTemplateProcess extends BaseTemplateProcess {
	private static final Logger logger = Logger.getLogger(QuestionDetailTemplateProcess.class);
	private IQuestionDao questionDao;
	/**
	 * 设置常见问题数据接口。
	 * @param questionDao 
	 *	  常见问题数据接口。
	 */
	public void setQuestionDao(IQuestionDao questionDao) {
		if(logger.isDebugEnabled()) logger.debug("注入常见问题数据接口...");
		this.questionDao = questionDao;
	}
	/*
	 * 模版处理。
	 * @see com.examw.test.service.publish.impl.BaseTemplateProcess#templateProcess()
	 */
	@Override
	protected List<StaticPage> templateProcess() throws Exception {
		if(logger.isDebugEnabled()) logger.debug("常见问题详细模版处理...");
		List<Question> questions = this.questionDao.findQuestions(new QuestionInfo(){
			private static final long serialVersionUID = 1L;
			@Override
			public String getSort() { return "createTime";}
			@Override
			public String getOrder() { return "desc";}
		});
		if(questions == null || questions.size() == 0) return null;
		List<StaticPage> list = new ArrayList<>();
		Map<String, Object> parameters = new HashMap<String, Object>();
		//最新试卷
		parameters.put("newsPapers", this.loadNewsPapers());
		//最热试卷
		parameters.put("hotsPapers", this.loadHotsPapers());
		for(Question question : questions){
			if(question == null) continue;
			parameters.put("question", new QuestionViewData(question.getId(), question.getTitle(), question.getContent(), question.getCreateTime()));
			list.add(new StaticPage(String.format("index-questions-%s", question.getId()), "/questions", this.createStaticPageContent(parameters)));
		}
		return list;
	}
	/**
	 * 常见问题显示数据。
	 * 
	 * @author yangyong
	 * @since 2015年1月6日
	 */
	public static class QuestionViewData extends QuestionListViewData{
		private static final long serialVersionUID = 1L;
		private String content; 
		/**
		 * 构造函数。
		 * @param id
		 * 常见问题ID。
		 * @param text
		 * 常见问题标题。
		 * @param content
		 * 常见问题内容。
		 * @param createTime
		 * 创建时间。
		 */
		public QuestionViewData(String id, String text, String content, Date createTime) {
			super(id, text,createTime);
			this.setContent(content);
		}
		/**
		 * 获取常见问题内容。
		 * @return 常见问题内容。
		 */
		public String getContent() {
			return content;
		}
		/**
		 * 设置常见问题内容。
		 * @param content 
		 *	  常见问题内容。
		 */
		public void setContent(String content) {
			this.content = content;
		}
	}
}