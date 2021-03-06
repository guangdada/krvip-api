package com.ikoori.vip.server.modular.biz.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ikoori.vip.common.constant.state.CouponCodeStatus;
import com.ikoori.vip.common.exception.BussinessException;
import com.ikoori.vip.common.persistence.dao.CouponCodeMapper;
import com.ikoori.vip.common.persistence.dao.CouponMapper;
import com.ikoori.vip.common.persistence.model.Coupon;
import com.ikoori.vip.common.persistence.model.CouponCode;
import com.ikoori.vip.server.config.properties.GunsProperties;
import com.ikoori.vip.server.modular.biz.dao.CouponDao;
import com.ikoori.vip.server.modular.biz.service.ICouponService;
import com.ikoori.vip.server.modular.biz.service.IStoreCouponService;

/**
 * 优惠券Dao
 *
 * @author chengxg
 * @Date 2017-08-04 12:20:55
 */
@Service
public class CouponServiceImpl implements ICouponService {
	@Autowired
	CouponDao couponDao;
	@Autowired
	CouponMapper couponMapper;
	@Autowired
	GunsProperties gunsProperties;
	@Autowired
	IStoreCouponService storeCouponService;
	@Autowired
	CouponCodeMapper couponCodeMapper;
	@Override
	public Integer deleteById(Long id) {
		return couponMapper.deleteById(id);
	}

	@Override
	public Integer updateById(Coupon coupon) {
		return couponMapper.updateById(coupon);
	}

	@Override
	public Coupon selectById(Long id) {
		return couponMapper.selectById(id);
	}

	@Override
	public Integer insert(Coupon coupon) {
		return couponMapper.insert(coupon);
	}
	
	/**
	 * 分页查询
	 * @Title: getCouponList   
	 * @param merchantId
	 * @param isExpired
	 * @param isInvalid
	 * @param type
	 * @param storeId
	 * @param page
	 * @param name
	 * @param orderByField
	 * @param isAsc
	 * @return
	 * @date:   2017年9月18日 下午2:38:29 
	 * @author: chengxg
	 */
	@Override
	public List<Map<String, Object>> getCouponList(Long merchantId, Boolean isExpired, Boolean isInvalid, Integer type,
			Long storeId, Page<Coupon> page, String name, String orderByField, boolean isAsc) {
		return couponDao.getCouponList(merchantId, isExpired, isInvalid, type, storeId, page, name, orderByField,
				isAsc);
	}

	/**
	 * 查询优惠券
	 * @Title: selectByCondition   
	 * @param condition
	 * @return
	 * @date:   2017年9月18日 下午2:35:17 
	 * @author: chengxg
	 */
	@Override
	public List<Coupon> selectByCondition(Map<String, Object> condition) {
		Wrapper<Coupon> w = new EntityWrapper<>();
		w.eq("status", 1).eq("is_invalid", 1).eq("merchant_id",
				condition.get("merchantId"));
		if(condition.get("type") != null){
			w.eq("type", condition.get("type"));
		}
		return couponMapper.selectList(w);
	}
	
	/**
	 * 查询“已制卡”的券码
	 * @Title: getCouponCode   
	 * @param merchantId
	 * @param num
	 * @return
	 * @date:   2017年10月16日 下午4:07:38 
	 * @author: chengxg
	 */
	public List<CouponCode> getCouponCode(Long merchantId,Integer num){
		Wrapper<CouponCode> w = new EntityWrapper<>();
		w.eq("merchant_id", merchantId);
		w.eq("status", 1);
		w.eq("use_status", CouponCodeStatus.madecard.getCode());
		w.last(" limit " + num);
		return couponCodeMapper.selectList(w);
	}
	
	/**
	 * 查询优惠券已发行的数量
	 * @Title: getPublishCount   
	 * @param couponId
	 * @param merchantId
	 * @return
	 * @date:   2017年10月16日 下午4:20:11 
	 * @author: chengxg
	 */
	public Integer getPublishCount(Long couponId,Long merchantId){
		Wrapper<CouponCode> w = new EntityWrapper<>();
		w.eq("coupon_id", couponId);
		w.eq("merchant_id", merchantId);
		w.eq("status", 1);
		w.gt("use_status", CouponCodeStatus.madecard.getCode());
		return couponCodeMapper.selectCount(w);
	}
	
	
	/**
	 * 批量发行现金券
	 * @Title: publish   
	 * @param couponId
	 * @param num
s	 * @date:   2017年10月16日 下午2:02:56 
	 * @author: chengxg
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void publishCoupon(Long couponId,Integer num){
		Coupon coupon = couponMapper.selectById(couponId);
		if(coupon == null){
			throw new BussinessException(500, "现金券不存在");
		}
		List<CouponCode> codes = getCouponCode(coupon.getMerchantId(), num);
		if(CollectionUtils.isEmpty(codes)){
			throw new BussinessException(500, "券码库券码数为0，请先制卡");
		}
		if(codes.size() < num){
			throw new BussinessException(500, "券码库剩余券码数：" + codes.size() + ",请先制卡");
		}
		
		// 已发行数量
		Integer count = getPublishCount(couponId,coupon.getMerchantId());
		Integer left = coupon.getTotal() - count;
		if(left < num){
			throw new BussinessException(500, "已发行:" + count + ",剩余可发行数量：" + left);
		}
		
		for(CouponCode code : codes){
			code.setUseStatus(CouponCodeStatus.publish.getCode());
			code.setCouponId(couponId);
			code.updateById();
		}
	}
	
	
	/**
	 * 优惠券保存
	 * @Title: saveCoupon   
	 * @param coupon
	 * @date:   2017年9月18日 下午2:34:36 
	 * @author: chengxg
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void saveCoupon(Coupon coupon,String storeIds){
		if (coupon.getValue() != null) {
			coupon.setOriginValue(coupon.getValue() * 100);
		}
		if(!coupon.isIsAtLeast()){
			coupon.setAtLeast(null);
			coupon.setOriginAtLeast(null);
			coupon.setUseCondition("不限制");
		}
		if (coupon.getAtLeast() != null) {
			coupon.setOriginAtLeast(coupon.getAtLeast() * 100);
			coupon.setUseCondition("满" + coupon.getAtLeast() + "元使用");
		}
		
		coupon.setStartTime(coupon.getStartAt().getTime());
		coupon.setEndTime(coupon.getEndAt().getTime());
		coupon.setLimitStore(StringUtils.isBlank(storeIds) ? false : true);
		if(coupon.getId() != null){
			Coupon couponDb = couponMapper.selectById(coupon.getId());
			if (coupon.getTotal() < couponDb.getGetCount()) {
				throw new BussinessException(500, "发放总量不能小于领取数量" + couponDb.getGetCount());
			}
			int stock = coupon.getTotal() - couponDb.getTotal();
			couponDb.setStock(couponDb.getStock() + stock);
			couponDb.setName(coupon.getName());
			couponDb.setTotal(coupon.getTotal());
			couponDb.setValue(coupon.getValue());
			couponDb.setType(coupon.getType());
			couponDb.setIsAtLeast(coupon.isIsAtLeast());
			couponDb.setIsShare(coupon.isIsShare());
			couponDb.setAtLeast(coupon.getAtLeast());
			couponDb.setCardId(coupon.getCardId());
			couponDb.setQuota(coupon.getQuota());
			couponDb.setDescription(coupon.getDescription());
			couponDb.setServicePhone(coupon.getServicePhone());
			couponDb.setStartAt(coupon.getStartAt());
			couponDb.setEndAt(coupon.getEndAt());
			couponDb.setUpdateTime(new Date());
			couponMapper.updateAllColumnById(couponDb);
		}else{
			// 优惠券别名，用于领取的时候替代ID
			coupon.setAlias(UUID.randomUUID().toString().replaceAll("-", ""));
			coupon.setUrl(gunsProperties.getClientUrl() + "/coupon/tofetch/" + coupon.getAlias());
			couponMapper.insert(coupon);
		}
		
		// 保存优惠券可用店铺
		storeCouponService.saveStoreCoupon(coupon.getId(), storeIds);
	}
}
