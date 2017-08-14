package com.ikoori.vip.server.modular.biz.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.plugins.Page;
import com.ikoori.vip.common.persistence.model.MemberCard;

/**
 * 领取记录Service
 *
 * @author chengxg
 * @Date 2017-08-14 13:14:55
 */
public interface IMemberCardService {
	public Integer deleteById(Long id);
	public Integer updateById(MemberCard memberCard);
	public MemberCard selectById(Long id);
	public Integer insert(MemberCard memberCard);
	List<Map<String, Object>> getMemberCardList(Page<MemberCard> page,String name,String orderByField,boolean isAsc,Long merchantId,String cardNumber);
	
}
