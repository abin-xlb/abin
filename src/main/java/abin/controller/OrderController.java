package abin.controller;

import abin.common.BaseContext;
import abin.common.R;
import abin.entity.*;
import abin.service.OrderDetailService;
import abin.service.OrderService;
import abin.service.ShoppingCartService;
import abin.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 订单
 */
@Slf4j
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private UserService userService;
    @Autowired
    private ShoppingCartService shoppingCartService;


    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单数据：{}",orders);
        orderService.submit(orders);
        return R.success("下单成功");
    }

    /**
     * 用户订单分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @Transactional
    @GetMapping("/userPage")
    public R<Page> page(int page, int pageSize) {
        //获取当前id
        Long userId = BaseContext.getCurrentId();
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        Page<OrdersDto> ordersDtoPage = new Page<>(page, pageSize);
        //条件构造器
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        //查询当前用户id订单数据
        queryWrapper.eq(userId != null, Orders::getUserId, userId);
        //按时间降序排序
        queryWrapper.orderByDesc(Orders::getOrderTime);
        orderService.page(pageInfo, queryWrapper);
        List<OrdersDto> list = pageInfo.getRecords().stream().map((item) -> {
            OrdersDto ordersDto = new OrdersDto();
            //获取orderId,然后根据这个id，去orderDetail表中查数据
            Long orderId = item.getId();
            LambdaQueryWrapper<OrderDetail> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(OrderDetail::getOrderId, orderId);
            List<OrderDetail> details = orderDetailService.list(wrapper);
            BeanUtils.copyProperties(item, ordersDto);
            //之后set一下属性
            ordersDto.setOrderDetails(details);
            return ordersDto;
        }).collect(Collectors.toList());
        BeanUtils.copyProperties(pageInfo, ordersDtoPage, "records");
        ordersDtoPage.setRecords(list);
        //日志输出看一下
        log.info("list:{}", list);
        return R.success(ordersDtoPage);
    }

//    @GetMapping("/userPage")
//    public R<Page> userPage(int page, int pageSize){
//        //构造分页构造器
//        Page<Orders> pageInfo = new Page<>(page, pageSize);
//
//        Page<OrdersDto> ordersDtoPage = new Page<>();
//
//        //构造条件构造器
//        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
//
//        //添加排序条件
//        queryWrapper.orderByDesc(Orders::getOrderTime);
//
//        //进行分页查询
//        orderService.page(pageInfo,queryWrapper);
//
//        //对象拷贝
//        BeanUtils.copyProperties(pageInfo,ordersDtoPage,"records");
//
//        List<Orders> records=pageInfo.getRecords();
//
//        List<OrdersDto> list = records.stream().map((item) -> {
//            OrdersDto ordersDto = new OrdersDto();
//
//            BeanUtils.copyProperties(item, ordersDto);
//            Long Id = item.getId();
//            //根据id查分类对象
//            Orders orders = orderService.getById(Id);
//            String number = orders.getNumber();
//            LambdaQueryWrapper<OrderDetail> lambdaQueryWrapper=new LambdaQueryWrapper<>();
//            lambdaQueryWrapper.eq(OrderDetail::getOrderId,number);
//            List<OrderDetail> orderDetailList = orderDetailService.list(lambdaQueryWrapper);
//            int num=0;
//
//            for(OrderDetail l:orderDetailList){
//                num+=l.getNumber().intValue();
//            }
//
//            ordersDto.setSumNum(num);
//            return ordersDto;
//        }).collect(Collectors.toList());
//
//        ordersDtoPage.setRecords(list);
//
//        return R.success(ordersDtoPage);
//    }

    //再来一单
    @Transactional
    @PostMapping("/again")
    public R<String> again(@RequestBody Orders order1){
        //取得orderId
        Long id = order1.getId();
        Orders orders = orderService.getById(id);
        //设置订单号码
        long orderId = IdWorker.getId();
        orders.setId(orderId);
        //设置订单号码
        String number = String.valueOf(IdWorker.getId());
        orders.setNumber(number);
        //设置下单时间
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        //向订单表中插入一条数据
        orderService.save(orders);
        //修改订单明细表
        LambdaQueryWrapper<OrderDetail> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderDetail::getOrderId,id);
        List<OrderDetail> list = orderDetailService.list(queryWrapper);
        list.stream().map((item)->{
            //订单明细表id
            long detailId = IdWorker.getId();
            //设置订单号码
            item.setOrderId(orderId);
            item.setId(detailId);
            return item;
        }).collect(Collectors.toList());

        //向订单明细表中插入多条数据
        orderDetailService.saveBatch(list);
        return R.success("再来一单");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String number,String beginTime,String endTime){
        //构造分页构造器
        Page<Orders> pageInfo = new Page<>(page, pageSize);

        Page<OrdersDto> ordersDtoPage=new Page<>();
        //构造条件构造器
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        //根据number进行模糊查询
        queryWrapper.like(!StringUtils.isEmpty(number),Orders::getNumber,number);
        //根据Datetime进行时间范围查询

//        log.info("开始时间：{}",beginTime);
//        log.info("结束时间：{}",endTime);
        if(beginTime!=null&&endTime!=null){
            queryWrapper.ge(Orders::getOrderTime,beginTime);
            queryWrapper.le(Orders::getOrderTime,endTime);
        }
        //添加排序条件
        queryWrapper.orderByDesc(Orders::getOrderTime);

        //进行分页查询
        orderService.page(pageInfo,queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,ordersDtoPage,"records");

        List<Orders> records=pageInfo.getRecords();

        List<OrdersDto> list=records.stream().map((item)->{
            OrdersDto ordersDto=new OrdersDto();

            BeanUtils.copyProperties(item,ordersDto);
            String name="用户"+item.getUserId();
            ordersDto.setUserName(name);
            LambdaQueryWrapper<OrderDetail> query = new LambdaQueryWrapper<>();
            Long orderId = item.getId();
            query.eq(OrderDetail::getOrderId,orderId);
            List<OrderDetail> list1 = orderDetailService.list(query);
            ordersDto.setOrderDetails(list1);

            return ordersDto;
        }).collect(Collectors.toList());

        ordersDtoPage.setRecords(list);
        return R.success(ordersDtoPage);
    }

    @PutMapping
    public R<String> send(@RequestBody Orders orders){
        Long id = orders.getId();
        Integer status = orders.getStatus();
        LambdaQueryWrapper<Orders> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getId,id);
        Orders one = orderService.getOne(queryWrapper);
        one.setStatus(status);
        orderService.updateById(one);
        return R.success("派送成功");
    }


    /**
     * 根据id查询菜品信息
     * @param id
     * @return
     */
    @GetMapping("/comment/{id}")
    public R<List<OrderDetail>> get(@PathVariable Long id){
        LambdaQueryWrapper<OrderDetail> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderDetail::getOrderId,id);
        List<OrderDetail> list = orderDetailService.list(queryWrapper);
        return R.success(list);
    }

    @PutMapping("/status")
    public R<String> status(Long id , int status){
        LambdaQueryWrapper<Orders> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getId,id);
        Orders one = orderService.getOne(queryWrapper);
        one.setStatus(status);
        orderService.updateById(one);
        return R.success("已评价");
    }


}