package abin.controller;

import abin.entity.*;
import abin.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import abin.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 套餐管理
 */

@RestController
@RequestMapping("/api/meal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private DishService dishService;

    @Autowired
    private favoriteService favoriteService;

    @Autowired
    private footprintService footprintService;
    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info("套餐信息：{}",setmealDto);

        setmealService.saveWithDish(setmealDto);

        return R.success("新增套餐成功");
    }

    /**
     * 套餐分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        //分页构造器对象
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> dtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据name进行like模糊查询
        queryWrapper.like(name != null,Setmeal::getName,name);
        //添加排序条件，根据更新时间降序排列
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo,queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");
        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            //对象拷贝
            BeanUtils.copyProperties(item,setmealDto);
            //分类id
            Long categoryId = item.getCategoryId();
            //根据分类id查询分类对象
            Category category = categoryService.getById(categoryId);
            if(category != null){
                //分类名称
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);
        return R.success(dtoPage);
    }


    /**
     * 修改菜品
     * @param setmealDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){
        log.info(setmealDto.toString());
        setmealService.updateWithDish(setmealDto);
        return R.success("修改菜品成功");
    }

    /**
     * 修改状态
     * @param setmealDto
     * @return
     */
    @PutMapping("/up")
    public R<String> updateStatus(@RequestBody SetmealDto setmealDto){
        log.info(setmealDto.toString());
        setmealService.updateById(setmealDto);
        return R.success("修改状态成功");
    }
    /**
     * 删除套餐
     * @param id
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> id){
        log.info("ids:{}",id);

        setmealService.removeWithDish(id);

        return R.success("套餐数据删除成功");
    }

    /**
     * 根据条件查询套餐数据
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null,Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(queryWrapper);

        return R.success(list);
    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public  R<SetmealDto> get(@PathVariable Long id){
        SetmealDto setmealDto =setmealService.getByIdWithMeal(id);
        return R.success(setmealDto);
    }


    /**
     * 移动端点击套餐图片查看套餐具体内容
     * 这里返回的是dto 对象，因为前端需要copies这个属性
     * 前端主要要展示的信息是:套餐中菜品的基本信息，图片，菜品描述，以及菜品的份数
     * @param SetmealId
     * @return
     */
    @GetMapping("/dish/{id}")
    public R<List<DishDto>> dish(@PathVariable("id") Long SetmealId){
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,SetmealId);
        //获取套餐里面的所有菜品  这个就是SetmealDish表里面的数据
        List<SetmealDish> list = setmealDishService.list(queryWrapper);
        List<DishDto> dishDtos = list.stream().map((setmealDish) -> {
            DishDto dishDto = new DishDto();
            //其实这个BeanUtils的拷贝是浅拷贝，这里要注意一下
            BeanUtils.copyProperties(setmealDish, dishDto);
            //这里是为了把套餐中的菜品的基本信息填充到dto中，比如菜品描述，菜品图片等菜品的基本信息
            Long dishId = setmealDish.getDishId();
            Dish dish = dishService.getById(dishId);
            BeanUtils.copyProperties(dish, dishDto);
            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtos);
    }

    /**
     * 根据id修改套餐收藏人数功能
     * @param setmeal
     * @return
     */
    @PutMapping("/like")
    public R<String> updateLike(@RequestBody Setmeal setmeal){
        log.info("修改分类信息：{}",setmeal);
        setmealService.updateById(setmeal);
        return R.success("修改成功");
    }


    /**
     * 查询收藏套餐数据
     * @return
     */
    @GetMapping("/like")
    public R<List<Setmeal>> getLike(Long id){
            LambdaQueryWrapper<favorite> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(favorite::getUserId,id).eq(favorite::getFavorite,1);
            List<favorite> favorite = favoriteService.list(wrapper);
            List<Setmeal> dishList = favorite.stream().map((item) -> {
                Long mealId = item.getMealId();
                if (mealId!= null){
                    LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
                    //添加条件，查询状态为1（起售状态）的菜品
                    queryWrapper.eq(Setmeal::getStatus, 1).eq(Setmeal::getId, mealId);
                    Setmeal setmeal = setmealService.getOne(queryWrapper);
                    return setmeal;
                }
                return null;
            }).filter(Objects::nonNull).collect(Collectors.toList());
            return R.success(dishList);
    }
    /**
     * 查询历史记录套餐数据
     * @return
     */
    @GetMapping("/foot")
    public R<List<Setmeal>> getFoot(Long id){
        //构造查询条件
        LambdaQueryWrapper<footprint> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(footprint::getUserId,id).eq(footprint::getFoot,1);
        List<footprint> footprints = footprintService.list(queryWrapper);
        List<Setmeal> collect = footprints.stream().map((item) -> {
            Long mealId = item.getMealId();
            if (mealId != null) {
                LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(Setmeal::getStatus, 1).eq(Setmeal::getId, mealId);
                Setmeal one = setmealService.getOne(wrapper);
                return one;
            }
            return null;

        }).filter(Objects::nonNull).collect(Collectors.toList());


        return R.success(collect);
    }
    /**
     * 根据id修改套餐信息
     * @param setmeal
     * @return
     */
    @PutMapping("/footprint")
    public R<String> updateFootprint(@RequestBody Setmeal setmeal){
        log.info("修改分类信息：{}",setmeal);
        setmealService.updateById(setmeal);
        return R.success("修改成功");
    }



    /**
     * 根据id修改套餐月售信息
     * @param shoppingCart
     * @return
     */
    @PutMapping("/sale")
    public R<String> updateSale(@RequestBody ShoppingCart shoppingCart){
        log.info("修改分类信息：{}",shoppingCart);
        Setmeal setmeal = new Setmeal();
        setmeal.setId(shoppingCart.getDishId());
        setmeal.setSale(setmeal.getSale()+shoppingCart.getNumber());
        setmealService.updateById(setmeal);
        return R.success("修改成功");
    }






}
