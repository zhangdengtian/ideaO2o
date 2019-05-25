package com.imooc.o2o.web.fronted;

import com.imooc.o2o.dto.AwardExecution;
import com.imooc.o2o.entity.Award;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.UserShopMap;
import com.imooc.o2o.service.AwardService;
import com.imooc.o2o.service.UserShopMapService;
import com.imooc.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Unruly Wind on 2019/3/11/011.
 *
 * @author BlueMelancholy
 * @desc:
 */
@Controller
@RequestMapping("/fronted")
public class ShopAwardController {
	@Autowired
	private UserShopMapService userShopMapService;
	@Autowired
	private AwardService awardService;

	/**
	 * 列出店铺设定的奖品列表
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listawardsbyshop", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listAwardsByShop(HttpServletRequest request) {
		Map<String, Object> modelMap = Collections.synchronizedMap(new HashMap<>(10));
		// 获取分页信息
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		// 获取店铺Id
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		// 空值判断
		if ((pageIndex > -1) && (pageSize > -1) && (shopId > -1)) {
			// 获取前端可能输入的奖品名模糊查询
			String awardName = HttpServletRequestUtil.getString(request, "awardName");
			Award awardCondition = new Award();
			if (awardName != null) {
				awardCondition.setAwardName(awardName);
			}
			awardCondition.setShopId(shopId);
			awardCondition.setEnableStatus(1);
			// 传入查询条件分页获取奖品信息
			AwardExecution ae = awardService.getAwardList(awardCondition, pageIndex, pageSize);
			modelMap.put("awardList", ae.getAwardList());
			modelMap.put("count", ae.getCount());
			modelMap.put("success", true);
			// 从Session中获取用户信息，主要是为了显示该用户在本店铺的积分
			PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
			// 空值判断
			if (user != null && user.getUserId() != null) {
				// 获取该用户在本店铺的积分信息
				UserShopMap userShopMap = userShopMapService.getUserShopMap(user.getUserId(), shopId);
				if (userShopMap == null) {
					modelMap.put("totalPoint", 0);
				} else {
					modelMap.put("totalPoint", userShopMap.getPoint());
				}
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
		}

		return modelMap;
	}
}
