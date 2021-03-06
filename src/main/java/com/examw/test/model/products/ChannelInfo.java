package com.examw.test.model.products;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.examw.model.Paging;

/**
 * 销售渠道信息。
 * @author fengwei.
 * @since 2014年8月11日 下午3:15:30.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ChannelInfo extends Paging{
	private static final long serialVersionUID = 1L;
	private String id,name;
	private Integer code;
	/**
	 * 获取渠道ID。
	 * @return 渠道ID。
	 */
	public String getId() {
		return id;
	}
	/**
	 * 设置渠道ID。
	 * @param id 
	 *	 渠道ID。
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 获取渠道代码。
	 * @return 渠道代码。
	 */
	public Integer getCode() {
		return code;
	}
	/**
	 * 设置渠道代码。
	 * @param code 
	 *	  渠道代码。
	 */
	public void setCode(Integer code) {
		this.code = code;
	}
	/**
	 * 获取渠道名称。
	 * @return 渠道名称。
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置渠道名称。
	 * @param name 
	 *	 渠道名称。
	 */
	public void setName(String name) {
		this.name = name;
	}
}