package abin.controller;

import abin.common.R;
import abin.entity.favorite;
import abin.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/like")
public class FavoriteController {

    @Autowired
    private UserService userService;

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private favoriteService favoriteService;

    /**
     * 修改个人用户收藏菜品
     * @return
     */
    @PostMapping("/dish")
    public R<String> addLike(Long dishId, Long userId, int likeId){

        if (likeId == 0){
            LambdaQueryWrapper<favorite> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(favorite::getUserId,userId).eq(favorite::getDishId,dishId).eq(favorite::getFavorite,1);
            favoriteService.remove(queryWrapper);
            return R.success("删除收藏成功");
        }
        favorite like = new favorite();
        like.setDishId(dishId);
        like.setUserId(userId);
        like.setFavorite(likeId);
        favoriteService.save(like);
        return R.success("添加收藏成功");
    }

    /**
     * 修改个人用户收藏套餐
     * @return
     */
    @PostMapping("/meal")
    public R<String> addLikemeal(Long mealId, Long userId, int likeId){

        if (likeId == 0){
            LambdaQueryWrapper<favorite> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(favorite::getUserId,userId).eq(favorite::getMealId,mealId).eq(favorite::getFavorite,1);
            favoriteService.remove(queryWrapper);
            return R.success("删除收藏成功");
        }
        favorite like = new favorite();
        like.setMealId(mealId);
        like.setUserId(userId);
        like.setFavorite(likeId);
        favoriteService.save(like);
        return R.success("添加收藏成功");
    }


    /**
     * 根据用户id获取个人用户收藏菜品
     * @param id
     * @return
     */
    @GetMapping("/getdish")
    public R<List<favorite>> getdish(Long id){
        LambdaQueryWrapper<favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(favorite::getUserId,id).eq(favorite::getFavorite,1);
        List<favorite> list = favoriteService.list(wrapper);
        return R.success(list);
    }

//    /**
//     * 根据用户id获取个人用户收藏套餐
//     * @param id
//     * @return
//     */
//    @GetMapping("/getmeal")
//    public R<List<favorite>> getmeal(Long id){
//        LambdaQueryWrapper<favorite> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(favorite::getUserId,id).eq(favorite::getFavorite,1);
//        List<favorite> list = favoriteService.list(wrapper);
//        return R.success(list);
//    }

    @DeleteMapping("/deleteDish")
    public R<String> deleteLike(Long id , Long userId){
        LambdaQueryWrapper<favorite> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(favorite::getDishId,id).eq(favorite::getUserId,userId);
        favoriteService.remove(queryWrapper);
        return R.success("删除成功");
    }


    @DeleteMapping("/deleteMeal")
    public R<String> deletemeal(Long id , Long userId){
        LambdaQueryWrapper<favorite> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(favorite::getMealId,id).eq(favorite::getUserId,userId);
        favoriteService.remove(queryWrapper);
        return R.success("删除成功");
    }
}
