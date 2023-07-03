package abin.service.impl;


import abin.entity.footprint;
import abin.mapper.footprintMapper;
import abin.service.footprintService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class FootprintServiceImpl extends ServiceImpl<footprintMapper, footprint> implements footprintService {

}
