package abin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import abin.entity.Category;

public interface CategoryService extends IService<Category> {

    public void remove(Long id);

}
