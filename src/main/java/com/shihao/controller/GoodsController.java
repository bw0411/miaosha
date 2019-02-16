package com.shihao.controller;


import com.shihao.domain.MiaoshaUser;
import com.shihao.redis.GoodsKey;
import com.shihao.redis.RedisService;
import com.shihao.service.GoodsService;
import com.shihao.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.AbstractContext;
import org.thymeleaf.context.IContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.context.webmvc.SpringWebMvcThymeleafRequestContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    GoodsService goodsService;

    @Autowired
    RedisService redisService;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;


    /**
     * 页面缓存
     * @param response
     * @param request
     * @param model
     * @param user
     * @return
     */
    @RequestMapping(value = "/to_list" ,produces = "text/html")
    @ResponseBody
    public String list(HttpServletResponse response,
                        HttpServletRequest request,
                        Model model,
                       MiaoshaUser user){
        String html = redisService.get(GoodsKey.goodsList, "", String.class);
        if(StringUtils.isNotBlank(html)){
            return html;
        }
        model.addAttribute("user",user);
        //查询商品列表
        List<GoodsVo> listGoodsVo = goodsService.listGoodsVo();
        model.addAttribute("goodsList",listGoodsVo);
        /*return "goods_list";*/
        //缓存技术
        WebContext ctx = new WebContext(request,response,
                request.getServletContext(),request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
        if(StringUtils.isNotBlank(html)){
            redisService.set(GoodsKey.goodsList,"",html);
        }
        return html;
    }


    /**
     * url缓存
     * @param model
     * @param user
     * @param request
     * @param response
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/to_detail/{goodsId}",produces = "text/html")
    @ResponseBody
    public String detail(Model model, MiaoshaUser user,
                       HttpServletRequest request,
                       HttpServletResponse response,
                       @PathVariable(name = "goodsId") Long goodsId){
        String html = redisService.get(GoodsKey.getById,""+goodsId,String.class);
        if(StringUtils.isNotBlank(html)){
            return html;
        }
        model.addAttribute("user",user);
        //查询商品列表
        GoodsVo goods = goodsService.getGoodsVoById(goodsId);
        model.addAttribute("goods",goods);
        //获取秒杀时间
        Long startAt =goods.getStartDate().getTime();
        Long endAt = goods.getEndDate().getTime();
        Long cur = System.currentTimeMillis();

        int miaoshaStatus = 0;//0还未开始 1进行中 2已结束
        int remainSeconds = 0;
        if(cur < startAt){//秒杀还未开始
            remainSeconds = (int)(startAt - cur)/1000;
        }else if(cur > endAt){//秒杀已结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else{//秒杀进行中
            miaoshaStatus = 1;
        }
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        //return "goods_detail";
        //缓存技术
        html = redisService.get(GoodsKey.getById, ""+goodsId, String.class);
        if(StringUtils.isNotBlank(html)){
            return html;
        }
        WebContext ctx = new WebContext(request,response,
                request.getServletContext(),request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", ctx);
        if(StringUtils.isNotBlank(html)){
            redisService.set(GoodsKey.getById,""+goodsId,html);
        }
        return html;
    }
}
