package com.rpm;

import com.rpm.domain.User;
import com.rpm.mapper.UserMapper;
import com.rpm.service.ISysLogLoginService;
import com.rpm.service.IUserService;
import com.rpm.vo.LoginNumVo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class RpmApplicationTests {
}
