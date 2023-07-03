package abin.controller;

import abin.entity.Dish;
import abin.entity.Setmeal;
import abin.service.DishService;
import abin.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import abin.common.R;
import abin.entity.Category;
import abin.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类管理
 */
@RestController
@RequestMapping("/api/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;
    /**
     * 新增分类
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category){
        log.info("category:{}",category);
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize){
        //分页构造器
        Page<Category> pageInfo = new Page<>(page,pageSize);
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加排序条件，根据sort进行排序
        queryWrapper.orderByAsc(Category::getSort);
        //分页查询
        categoryService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 根据id删除分类
     * @param id
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long id){
        log.info("删除分类，id为：{}",id);
        categoryService.remove(id);
        return R.success("分类信息删除成功");
    }

    /**
     * 根据id修改分类信息
     * @param category
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("修改分类信息：{}",category);
        categoryService.updateById(category);
        return R.success("修改分类信息成功");
    }

    /**
     * 根据条件查询分类数据
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(category.getType() != null,Category::getType,category.getType());
        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }


    /**
     * 根据图片信息查询唯一菜品或者套餐
     * @param image
     * @return
     */
    @GetMapping("/name")
    public R<Object> byName(String image){
        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(Dish::getImage,image);
        //添加排序条件
        Dish one = dishService.getOne(queryWrapper);
        if (one != null){
            return R.success(one);
        }
        LambdaQueryWrapper<Setmeal> Wrapper = new LambdaQueryWrapper<>();
        //添加条件
        Wrapper.eq(Setmeal::getImage,image);
        //添加排序条件
        Setmeal two = setmealService.getOne(Wrapper);
        return R.success(two);
    }

    /**
     * 根据图片信息查询分类信息
     * @param image
     * @return
     */
    @GetMapping("/img")
    public R<Category> byImg(String image){
        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(Dish::getImage,image);
        //添加排序条件
        Dish one = dishService.getOne(queryWrapper);
        if (one != null){
            Long categoryId = one.getCategoryId();
            LambdaQueryWrapper<Category> category = new LambdaQueryWrapper<>();
            category.eq(Category::getId,categoryId);
            Category c = categoryService.getOne(category);
            return R.success(c);
        }else{
            LambdaQueryWrapper<Setmeal> Wrapper = new LambdaQueryWrapper<>();
            //添加条件
            Wrapper.eq(Setmeal::getImage,image);
            //添加排序条件
            Setmeal two = setmealService.getOne(Wrapper);
            Long categoryId = two.getCategoryId();
            LambdaQueryWrapper<Category> category = new LambdaQueryWrapper<>();
            category.eq(Category::getId,categoryId);
            Category c = categoryService.getOne(category);
            return R.success(c);
        }


    }




}
