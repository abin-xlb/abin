package abin.service.impl;

import abin.entity.favorite;
import abin.mapper.favoriteMapper;
import abin.service.favoriteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class FavoriteServiceImpl extends ServiceImpl<favoriteMapper, favorite> implements favoriteService {
}
