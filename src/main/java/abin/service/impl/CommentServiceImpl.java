package abin.service.impl;

import abin.entity.Comment;
import abin.mapper.CommentMapper;
import abin.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService  {


}
