package abin.controller;

import abin.common.BaseContext;
import abin.common.R;
import abin.entity.*;
import abin.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 评论
 */
@RestController
@RequestMapping("/api/comment")
@Slf4j
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;
    /**
     * 新增评论
     * @param comment
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Comment comment) {
        comment.setCreateTime(LocalDateTime.now());
        comment.setCreateUser(BaseContext.getCurrentId());
        log.info(comment.toString());
        commentService.save(comment);
        return R.success("评论添加成功");
    }

    /**
     * 评论查询 好评 (5星)
     * @return
     */
    @GetMapping("/star")
    public R<List<CommentDto>> star() {
        LambdaQueryWrapper < Comment > queryWrapper = new LambdaQueryWrapper<>();
        //添加排序条件，根据star进行排序
        queryWrapper.gt(Comment::getStar, 4).orderByDesc(Comment::getCreateTime);

        List<Comment> list = commentService.list(queryWrapper);

        List<CommentDto> commentDtoList = list.stream().map((item) -> {
            CommentDto commentDto = new CommentDto();
            BeanUtils.copyProperties(item, commentDto);
            Long userId = item.getUserId();
            User user = userService.getById(userId);
            if (user != null) {
                String avatar = user.getAvatar();
                String name = user.getName();
                commentDto.setImg(avatar);
                commentDto.setUserName(name);
            }
            Long orderId = item.getOrderId();
            Long dishId = item.getDishId();
            Long setmealId = item.getSetmealId();
            LambdaQueryWrapper<OrderDetail> Wrapper = new LambdaQueryWrapper<>();
            Wrapper.eq(OrderDetail::getOrderId,orderId);
            if (dishId  != null ){
                Wrapper.eq(OrderDetail::getDishId,dishId);
                OrderDetail orderDetail = orderDetailService.getOne(Wrapper);
                String name = orderDetail.getName();
                commentDto.setDishName(name);
            }
            if (setmealId  != null ){
                Wrapper.eq(OrderDetail::getSetmealId,setmealId);
                OrderDetail orderDetail = orderDetailService.getOne(Wrapper);
                String name = orderDetail.getName();
                commentDto.setMealName(name);
            }

            return commentDto;
        }).collect(Collectors.toList());


        return R.success(commentDtoList);
    }

    /**
     * 评论查询 根据时间
     * @return
     */
    @GetMapping("/time")
    public R<List<CommentDto>> time() {
        LambdaQueryWrapper < Comment > queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.orderByDesc(Comment::getCreateTime);

        List<Comment> list = commentService.list(queryWrapper);

        List<CommentDto> commentDtoList = list.stream().map((item) -> {
            CommentDto commentDto = new CommentDto();
            BeanUtils.copyProperties(item, commentDto);
            Long userId = item.getUserId();
            User user = userService.getById(userId);
            if (user != null) {
                String avatar = user.getAvatar();
                String name = user.getName();
                commentDto.setImg(avatar);
                commentDto.setUserName(name);
            }
            Long orderId = item.getOrderId();
            Long dishId = item.getDishId();
            Long setmealId = item.getSetmealId();
            LambdaQueryWrapper<OrderDetail> Wrapper = new LambdaQueryWrapper<>();
            Wrapper.eq(OrderDetail::getOrderId,orderId);
            if (dishId  != null ){
                Wrapper.eq(OrderDetail::getDishId,dishId);
                OrderDetail orderDetail = orderDetailService.getOne(Wrapper);
                String name = orderDetail.getName();
                commentDto.setDishName(name);
            }
            if (setmealId  != null ){
                Wrapper.eq(OrderDetail::getSetmealId,setmealId);
                OrderDetail orderDetail = orderDetailService.getOne(Wrapper);
                String name = orderDetail.getName();
                commentDto.setMealName(name);
            }

            return commentDto;
        }).collect(Collectors.toList());


        return R.success(commentDtoList);
    }


    /**
     * 评论查询 等级大于3
     * @return
     */
    @GetMapping("/page")
    public R<List<CommentDto>> page() {
        LambdaQueryWrapper <Comment> queryWrapper = new LambdaQueryWrapper<>();
        //添加排序条件，根据star进行排序
        queryWrapper.gt(Comment::getStar, 2).orderByDesc(Comment::getCreateTime).last("LIMIT 10");

        List<Comment> commentList = commentService.list(queryWrapper);

        List<CommentDto> commentDtoList = commentList.stream().map((item) -> {
            CommentDto commentDto = new CommentDto();
            BeanUtils.copyProperties(item, commentDto);
            Long userId = item.getUserId();
            User user = userService.getById(userId);
            if (user != null) {
                String avatar = user.getAvatar();
                String name = user.getName();
                commentDto.setImg(avatar);
                commentDto.setUserName(name);
            }
            Long orderId = item.getOrderId();
            Long dishId = item.getDishId();
            Long setmealId = item.getSetmealId();
            LambdaQueryWrapper<OrderDetail> Wrapper = new LambdaQueryWrapper<>();
            Wrapper.eq(OrderDetail::getOrderId,orderId);
            if (dishId  != null ){
                Wrapper.eq(OrderDetail::getDishId,dishId);
                OrderDetail orderDetail = orderDetailService.getOne(Wrapper);
                String name = orderDetail.getName();
                commentDto.setDishName(name);
            }
            if (setmealId  != null ){
                Wrapper.eq(OrderDetail::getSetmealId,setmealId);
                OrderDetail orderDetail = orderDetailService.getOne(Wrapper);
                String name = orderDetail.getName();
                commentDto.setMealName(name);
            }

            return commentDto;
        }).collect(Collectors.toList());


        return R.success(commentDtoList);
    }

    /**
     * 评论查询 全部
     *
     * @return
     */
    @GetMapping("/all")
    public R<List<CommentDto>> all() {
        List<Comment> commentList = commentService.list();

        List<CommentDto> commentDtoList = commentList.stream().map((item) -> {
            CommentDto commentDto = new CommentDto();
            BeanUtils.copyProperties(item, commentDto);
            Long userId = item.getUserId();
            User user = userService.getById(userId);
            if (user != null) {
                String avatar = user.getAvatar();
                String name = user.getName();
                commentDto.setImg(avatar);
                commentDto.setUserName(name);
            }
            Long orderId = item.getOrderId();
            Long dishId = item.getDishId();
            Long setmealId = item.getSetmealId();
            LambdaQueryWrapper<OrderDetail> Wrapper = new LambdaQueryWrapper<>();
            Wrapper.eq(OrderDetail::getOrderId,orderId);
            if (dishId  != null ){
                Wrapper.eq(OrderDetail::getDishId,dishId);
                OrderDetail orderDetail = orderDetailService.getOne(Wrapper);
                String name = orderDetail.getName();
                commentDto.setDishName(name);
            }
            if (setmealId  != null ){
                Wrapper.eq(OrderDetail::getSetmealId,setmealId);
                OrderDetail orderDetail = orderDetailService.getOne(Wrapper);
                String name = orderDetail.getName();
                commentDto.setMealName(name);
            }
            return commentDto;
        }).collect(Collectors.toList());

        return R.success(commentDtoList);
    }



}
