package abin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import abin.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User>{
}
