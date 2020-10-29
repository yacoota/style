package com.itstyle.blog.common.util;

import com.itstyle.blog.common.constant.MsgConstant;
import com.itstyle.blog.common.constant.SystemConstant;
import com.itstyle.blog.common.entity.Result;

/**
 * 通用工具类
 */
public class CommonUtils {

	/**
	 * 对象是否为空
	 *
	 * @param obj
	 * @return
	 */
	public static boolean isNullOrEmpty(Object obj) {
		if (obj == null) {
			return true;
		}
		return false;
	}


	/**
	 * 查询详情提示
	 * 
	 * @param data
	 * @return
	 */
	public static Result msg(Object data) {
		if (isNullOrEmpty(data)) {
			return Result.error(MsgConstant.MSG_INIT_FORM);
		}
		return Result.ok().put(SystemConstant.DATA_ROWS, data);
	}

	/**
	 * 返回数据
	 * 
	 * @param data
	 * @return
	 */
	public static Result msgNotCheckNull(Object data) {
		return Result.ok().put(SystemConstant.DATA_ROWS, data);
	}

}
