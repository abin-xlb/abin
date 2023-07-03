package abin.controller;

import abin.entity.*;
import abin.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
 * 菜品管理
 */
@RestController
@RequestMapping("/api/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private favoriteService favoriteService;

    @Autowired
    private footprintService footprintService;
    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());

        dishService.saveWithFlavor(dishDto);

        return R.success("新增菜品成功");
    }

    /**
     * 菜品信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){

        //构造分页构造器对象
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(name != null,Dish::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        //执行分页查询
        dishService.page(pageInfo,queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){

        DishDto dishDto = dishService.getByIdWithFlavor(id);

        return R.success(dishDto);
    }

    /**
     * 修改菜品
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());

        dishService.updateWithFlavor(dishDto);

        return R.success("修改菜品成功");
    }


    /**
     * 根据id删除分类
     * @param id
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long id){
        log.info("删除分类，id为：{}",id);
        dishService.removeById(id);
        dishFlavorService.removeById(id);
        return R.success("分类信息删除成功");
    }


    /**
     * 根据条件查询对应的菜品数据
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null ,Dish::getCategoryId,dish.getCategoryId());
        //添加条件，查询状态为1（起售状态）的菜品
        queryWrapper.eq(Dish::getStatus,1);

        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);

        List<DishDto> dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            //当前菜品的id
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
            //SQL:select * from dish_flavor where dish_id = ?
            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());



        return R.success(dishDtoList);
    }


    /**
     * 根据id修改菜品收藏人数功能
     * @param Dish
     * @return
     */
    @PutMapping("/like")
    public R<String> updateLike(@RequestBody Dish Dish){
        log.info("修改分类信息：{}",Dish);
        dishService.updateById(Dish);
        return R.success("修改收藏人数成功");
    }

    /**
     * 根据id修改菜品用户收藏功能
     * @param Dish
     * @return
     */
    @PutMapping("/userlike")
    public R<String> userLike(@RequestBody Dish Dish,Long id){
        log.info("修改分类信息：{}",Dish);
        dishService.updateById(Dish);
        return R.success("修改成功");
    }


    /**
     * 查询收藏菜品数据
     * @return
     */
    @GetMapping("/getdishlike")
    public R<List<Dish>> getdishlike(Long id){
        LambdaQueryWrapper<favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(favorite::getUserId,id).eq(favorite::getFavorite,1);
        List<favorite> favorite = favoriteService.list(wrapper);
        List<Dish> dishList = favorite.stream().map((item) -> {
            Long dishId = item.getDishId();
            if (dishId!= null){
                LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
                //添加条件，查询状态为1（起售状态）的菜品
                queryWrapper.eq(Dish::getStatus, 1).eq(Dish::getId, dishId);
                Dish dish = dishService.getOne(queryWrapper);
                return dish;
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        return R.success(dishList);
    }

    /**
     * 查询足迹菜品数据
     * @return
     */
    @GetMapping("/getfoot")
    public R<List<Dish>> getDishFoot(Long id){
        LambdaQueryWrapper<footprint> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(footprint::getUserId,id).eq(footprint::getFoot,1);
        List<footprint> footprint = footprintService.list(wrapper);
        List<Dish> dishList = footprint.stream().map((item) -> {
            Long dishId = item.getDishId();
            if (dishId != null){
                LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
                //添加条件，查询状态为1（起售状态）的菜品
                queryWrapper.eq(Dish::getStatus, 1).eq(Dish::getId, dishId);
                Dish dish = dishService.getOne(queryWrapper);
                return dish;
            }

            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        //构造查询条件
        return R.success(dishList);
    }


    /**
     * 根据id修改菜品被用户游览信息
     * @param Dish
     * @return
     */
    @PutMapping("/footprint")
    public R<String> updateFootprint(@RequestBody Dish Dish){
        log.info("修改分类信息：{}",Dish);
        dishService.updateById(Dish);
        return R.success("修改成功");
    }

    /**
     * 查询历史记录菜品数据
     * @return
     */
    @GetMapping("/Foot")
    public R<List<Dish>> getFoot(){
        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getFootprint,1);
        //添加条件，查询状态为1（起售状态）的菜品
        queryWrapper.eq(Dish::getStatus,1);
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);

        return R.success(list);
    }


    /**
     * 根据id修改菜品月售信息
     * @param shoppingCart
     * @return
     */
    @PutMapping("/sale")
    public R<String> updateSale(@RequestBody ShoppingCart shoppingCart){
        log.info("修改分类信息：{}",shoppingCart);
        if(shoppingCart.getDishId() == null){
            return R.success("未修改");
        }
        Long Id = shoppingCart.getDishId();
        Dish dish = dishService.getById(Id);
        int sale = dish.getSale();
        dish.setSale(sale+shoppingCart.getNumber());
        dishService.updateById(dish);
        return R.success("修改成功");
    }



    /**
     * 查询月售展示热销图片
     * @return
     */
    @GetMapping("/sale")
    public R<List<Object>> getSale(){
        //构造查询条件
        QueryWrapper<Dish> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().select(Dish::getImage,Dish::getId)
                .orderByDesc(Dish::getSale)
                .last("limit 4");
        List<Dish> list = dishService.list(queryWrapper);

        QueryWrapper<Setmeal> Wrapper = new QueryWrapper<>();
        Wrapper.lambda().select(Setmeal::getImage,Setmeal::getId)
                .orderByDesc(Setmeal::getSale)
                .last("limit 2");
        List<Setmeal> list1 = setmealService.list(Wrapper);

        List<Object> combinedList = Stream.concat(
                list.stream().map(dish -> (Object) dish),
                list1.stream().map(setmeal -> (Object) setmeal)
        )
                .collect(Collectors.toList());

        return R.success(combinedList);
    }




}
