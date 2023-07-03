package abin.controller;

import abin.common.R;
import abin.entity.Dish;
import abin.entity.Setmeal;
import abin.entity.favorite;
import abin.entity.footprint;
import abin.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/foot")
public class FootprintController {

    @Autowired
    private UserService userService;

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private footprintService footprintService;

    /**
     * 根据id修改用户足迹信息
     * @param userId
     * @param Id
     * @return
     */
    @PostMapping("/footprint")
    public R<String> addFootprint(Long userId, Long Id,int foot){
        if(foot == 1){
            return R.success("不需要修改");
        }
        footprint footprint = new footprint();
        footprint.setUserId(userId);
        footprint.setFoot(1);
        Dish dish = dishService.getById(Id);
        if (dish == null){
            Setmeal setmeal = setmealService.getById(Id);
            Long id = setmeal.getId();
            footprint.setMealId(id);
        }
        Long id = dish.getId();
        footprint.setDishId(id);
        footprintService.save(footprint);
        return R.success("添加足迹成功");
    }

    /**
     * 根据用户id获取用户菜品足迹信息
     * @param id
     * @return
     */
    @GetMapping("/getform")
    public R<List<footprint>> getFootprint(Long id){
        LambdaQueryWrapper<footprint> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(footprint::getUserId,id).eq(footprint::getFoot,1);
        List<footprint> list = footprintService.list(queryWrapper);
        return R.success(list);
    }


    @DeleteMapping("/deletefoot")
    public R<String> deleteFoot(Long id , Long userId){
        LambdaQueryWrapper<footprint> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(footprint::getDishId,id).eq(footprint::getUserId,userId);
        footprintService.remove(queryWrapper);
        return R.success("删除成功");
    }


}
