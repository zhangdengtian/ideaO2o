package com.imooc.o2o.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.cache.JedisUtil;
import com.imooc.o2o.dao.HeadLineDao;
import com.imooc.o2o.entity.HeadLine;
import com.imooc.o2o.exceptions.HeadLineOperateException;
import com.imooc.o2o.service.HeadLineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Unruly Wind on 2019/1/29/029.
 *
 * @author BlueMelancholy
 * @desc:
 */
@Service
@SuppressWarnings("SpringJavaAutowiringInspection")
public class HeadLineServiceImpl implements HeadLineService {
	@Autowired
	private HeadLineDao headLineDao;
	@Autowired
	private JedisUtil.Keys jedisKeys;
	@Autowired
	private JedisUtil.Strings jedisStrings;
	private static Logger logger = LoggerFactory.getLogger(HeadLineServiceImpl.class);

	/**
	 * 根据传入的查询条件查询头条信息（头条名）
	 *
	 * @param headLineCondition
	 * @return
	 * @throws IOException
	 */
	@Override
	@Transactional
	public List<HeadLine> getHeadLineList(HeadLine headLineCondition) throws IOException {
		// 定义redis的key前缀
		String key = HLLISTKEY;
		List<HeadLine> headLineList = null;
		ObjectMapper mapper = new ObjectMapper();
		if (headLineCondition != null && headLineCondition.getEnableStatus() != null) {
			key = key + "_" + headLineCondition.getEnableStatus();
		}
		if (!jedisKeys.exists(key)) {
			headLineList = headLineDao.queryHeadLine(headLineCondition);
			String jsonString;
			try {
				jsonString = mapper.writeValueAsString(headLineList);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new HeadLineOperateException(e.getMessage());
			}
			jedisStrings.set(key, jsonString);
		} else {
			String jsonString = jedisStrings.get(key);
			JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, HeadLine.class);
			try {
				headLineList = mapper.readValue(jsonString, javaType);
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new HeadLineOperateException(e.getMessage());
			}
		}
		return headLineList;
	}
}
