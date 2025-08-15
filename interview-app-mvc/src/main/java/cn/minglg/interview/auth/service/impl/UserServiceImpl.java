package cn.minglg.interview.auth.service.impl;

import cn.minglg.interview.auth.mapper.*;
import cn.minglg.interview.auth.pojo.Company;
import cn.minglg.interview.auth.pojo.Role;
import cn.minglg.interview.auth.pojo.User;
import cn.minglg.interview.auth.service.UserService;
import cn.minglg.interview.common.constant.response.ResponseCode;
import cn.minglg.interview.common.properties.GlobalProperties;
import cn.minglg.interview.common.response.R;
import cn.minglg.interview.common.utils.JsonUtils;
import cn.minglg.interview.common.utils.RsaUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.KeyPair;
import java.util.List;
import java.util.UUID;

/**
 * ClassName:UserServiceImpl
 * Package:cn.minglg.interview.service.impl
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/6/13
 * @Version 1.0
 */
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final UserRoleMapper userRoleMapper;
    private final UserCompanyMapper userCompanyMapper;
    private final CompanyMapper companyMapper;
    private final PasswordEncoder passwordEncoder;
    private final KeyPair keyPair;
    private final StringRedisTemplate redisTemplate;
    private final GlobalProperties globalProperties;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public R register(User user) {
        String registerRoleName = user.getRoles().get(0).getRoleName().toString();
        // 首先获取所有的角色列表（优先从redis获取，如果获取不到再从mysql获取）
        // 防止添加无效角色
        String redisRoleKey = globalProperties.getRegister().getRoleRedisKeyPrefix() + ":" + registerRoleName;
        String roleStr = redisTemplate.opsForValue().get(redisRoleKey);
        if (roleStr == null) {
            roleMapper.getAllRoleList()
                    .forEach(role -> {
                        String roleKey = globalProperties.getRegister().getRoleRedisKeyPrefix() + ":" + role.getRoleName();
                        String roleValue = JsonUtils.toJsonStr(role);
                        redisTemplate.opsForValue().set(roleKey, roleValue);
                    });
        }
        roleStr = redisTemplate.opsForValue().get(redisRoleKey);
        // 角色不存在或者是不允许注册的角色
        List<String> notAllowRoles = globalProperties.getRegister().getNotAllowRoles();
        if (roleStr == null || notAllowRoles.contains(registerRoleName)) {
            return R.builder()
                    .code(ResponseCode.REGISTER_FAIL.getCode())
                    .message("注册失败，无效的角色：" + registerRoleName).build();
        }
        // 处理nickname
        String nickname = user.getNickname();
        if (nickname == null || nickname.trim().isEmpty()) {
            user.setNickname("用户" + UUID.randomUUID().toString().replace("-", "").substring(0, 10));
        }
        // 对前端传输过来的密码进行相应处理
        long timeoutSeconds = globalProperties.getAuth().getRequestTimeoutSeconds();
        String encryptMessage = user.getPassword();
        try {
            String decryptPassword = RsaUtils.decrypt(encryptMessage, keyPair.getPrivate(), timeoutSeconds);
            Integer roleId = JsonUtils.toBean(roleStr, Role.class).getRoleId();
            String encryptPassword = passwordEncoder.encode(decryptPassword);
            user.setPassword(encryptPassword);
            // 第一步：添加用户基本信息，并返回用户id
            userMapper.addUser(user);
            Long userId = user.getUserId();
            // 第二步：添加用户-角色映射信息
            userRoleMapper.addUserRole(userId, roleId);
            // 第三步：判断是否有公司信息需要添加，如有，也应该一并添加
            Company company = user.getCompany();
            if (company != null) {
                String companyName = company.getCompanyName();
                if (companyName != null && !companyName.trim().isEmpty()) {
                    // 第四步：获取公司的id
                    companyMapper.upsertCompany(company);
                    Long companyId = company.getCompanyId();
                    // 第五步：添加用户-公司映射关系
                    userCompanyMapper.addUserCompany(userId, companyId);
                }
            }
            return R.builder().code(ResponseCode.OK.getCode()).message("注册成功！").build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 根据用户名定位用户。在实际实现中，搜索
     * 可能区分大小写或不区分大小写，具体取决于
     * 已配置 implementation 实例。在这种情况下，
     * 返回的对象可能具有与 What 不同的 username 大小写
     * 实际上是被请求的..
     *
     * @param userName 标识需要其数据的用户的用户名。
     * @return 完全填充的用户记录（永不为空）
     * @throws UsernameNotFoundException 授予权限
     */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userMapper.getUserWithDetailsByUserName(userName);
        if (user == null) {
            throw new UsernameNotFoundException("登录账号不存在！");
        }
        return user;
    }

}
