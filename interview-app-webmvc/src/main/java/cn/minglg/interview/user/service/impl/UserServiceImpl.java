package cn.minglg.interview.user.service.impl;

import cn.minglg.authentication.pojo.SecurityUser;
import cn.minglg.authentication.service.RsaService;
import cn.minglg.authentication.utils.JsonUtils;
import cn.minglg.commons.model.response.GenericResponse;
import cn.minglg.commons.model.response.ResponseCode;
import cn.minglg.commons.model.user.pojo.Company;
import cn.minglg.commons.model.user.pojo.Role;
import cn.minglg.commons.model.user.pojo.User;
import cn.minglg.interview.common.properties.RegisterProperties;
import cn.minglg.interview.user.mapper.*;
import cn.minglg.interview.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final StringRedisTemplate redisTemplate;
    private final RsaService rsaService;
    private final RegisterProperties registerProperties;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public GenericResponse<?> register(User user) {
        String registerRoleName = user.getRoles().get(0).getRoleName().toString();
        // 首先获取所有的角色列表（优先从redis获取，如果获取不到再从mysql获取）
        // 防止添加无效角色
        String redisRoleKey = registerProperties.getRoleRedisKeyPrefix() + ":" + registerRoleName;
        String roleStr = redisTemplate.opsForValue().get(redisRoleKey);
        if (roleStr == null) {
            roleMapper.getAllRoleList()
                    .forEach(role -> {
                        String roleKey = registerProperties.getRoleRedisKeyPrefix() + ":" + role.getRoleName();
                        String roleValue = JsonUtils.toJsonStr(role);
                        redisTemplate.opsForValue().set(roleKey, roleValue);
                    });
        }
        roleStr = redisTemplate.opsForValue().get(redisRoleKey);
        // 角色不存在或者是不允许注册的角色
        List<String> notAllowRoles = registerProperties.getNotAllowRoles();
        if (roleStr == null || notAllowRoles.contains(registerRoleName)) {
            return GenericResponse.builder()
                    .code(ResponseCode.REGISTER_FAIL.getCode())
                    .message("注册失败，无效的角色：" + registerRoleName).build();
        }
        // 处理nickname
        String nickname = user.getNickname();
        if (nickname == null || nickname.trim().isEmpty()) {
            user.setNickname("用户" + UUID.randomUUID().toString().replace("-", "").substring(0, 10));
        }
        // 对前端传输过来的密码进行相应处理
        String encryptMessage = user.getPassword();
        try {
            String decryptPassword = rsaService.decrypt(encryptMessage);
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
            return GenericResponse.builder().code(ResponseCode.OK.getCode()).message("注册成功！").build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 根据用户名获取用户信息。
     *
     * @param userName 标识需要其数据的用户的用户名。
     * @return 完全填充的用户记录（永不为空）
     * @throws UsernameNotFoundException 授予权限
     */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user =  userMapper.getUserWithDetailsByUserName(userName);
        if (user == null) {
            throw new UsernameNotFoundException("登录账号不存在！");
        }
        return new SecurityUser(user);
    }

}
