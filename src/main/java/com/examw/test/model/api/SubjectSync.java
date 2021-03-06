package com.examw.test.model.api;

import java.io.Serializable;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * 考试科目同步数据。
 * 
 * @author yangyong
 * @since 2015年2月27日
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public class SubjectSync implements Serializable {
	private static final long serialVersionUID = 1L;
	private String code,name;
	/**
	 * 获取科目代码。
	 * @return 科目代码。
	 */
	public String getCode() {
		return code;
	}
	/**
	 * 设置科目代码。
	 * @param code 
	 *	  科目代码。
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * 获取科目名称。
	 * @return 科目名称。
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置科目名称。
	 * @param name 
	 *	  科目名称。
	 */
	public void setName(String name) {
		this.name = name;
	}
}