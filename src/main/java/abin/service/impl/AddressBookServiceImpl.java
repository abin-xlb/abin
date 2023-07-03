package abin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import abin.entity.AddressBook;
import abin.mapper.AddressBookMapper;
import abin.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {


}

