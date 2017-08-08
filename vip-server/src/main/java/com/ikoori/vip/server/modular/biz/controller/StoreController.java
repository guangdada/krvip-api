package com.ikoori.vip.server.modular.biz.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.plugins.Page;
import com.ikoori.vip.common.annotion.Permission;
import com.ikoori.vip.common.constant.factory.PageFactory;
import com.ikoori.vip.common.exception.BizExceptionEnum;
import com.ikoori.vip.common.exception.BussinessException;
import com.ikoori.vip.common.persistence.model.Store;
import com.ikoori.vip.common.util.ToolUtil;
import com.ikoori.vip.server.common.controller.BaseController;
import com.ikoori.vip.server.modular.biz.service.IStoreService;
import com.ikoori.vip.server.modular.biz.warpper.StoreWarpper;

/**
 * 门店控制器
 *
 * @author chengxg
 * @Date 2017-08-07 17:52:18
 */
@Controller
@RequestMapping("/store")
public class StoreController extends BaseController {

    private String PREFIX = "/biz/store/";
    @Autowired
	IStoreService storeService;

    /**
     * 跳转到门店首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "store.html";
    }

    /**
     * 跳转到添加门店
     */
    @RequestMapping("/store_add")
    public String storeAdd() {
        return PREFIX + "store_add.html";
    }

    /**
     * 跳转到修改门店
     */
    @RequestMapping("/store_update/{storeId}")
    public String storeUpdate(@PathVariable Long storeId, Model model) {
    	Store store = storeService.selectById(storeId);
    	model.addAttribute(store);
        return PREFIX + "store_edit.html";
    }

    /**
     * 获取门店列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
    	Page<Store> page = new PageFactory<Store>().defaultPage();
    	List<Map<String, Object>> result = storeService.getStoreList(page,condition,page.getOrderByField(), page.isAsc());
    	page.setRecords((List<Store>) new StoreWarpper(result).warp());
        return super.packForBT(page);
    }

    /**
     * 新增门店
     */
    @RequestMapping(value = "/add")
    @Permission
    @ResponseBody
    public Object add(Store store) {
    	storeService.insert(store);
        return super.SUCCESS_TIP;
    }

    /**
     * 删除门店
     */
    @RequestMapping(value = "/delete")
    @Permission
    @ResponseBody
    public Object delete(@RequestParam Long storeId) {
        storeService.deleteById(storeId);
        return SUCCESS_TIP;
    }


    /**
     * 修改门店
     */
    @RequestMapping(value = "/update")
    @Permission
    @ResponseBody
    public Object update(Store store) {
    	if (ToolUtil.isEmpty(store) || store.getId() == null) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
    	storeService.updateById(store);
        return super.SUCCESS_TIP;
    }

    /**
     * 门店详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
