package cn.minglg.interview.auth.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * ClassName:UserCompanyMapper
 * Package:cn.minglg.interview.auth.mapper
 * Description:用户-公司映射
 *
 * @Author kfzx-minglg
 * @Create 2025/7/20
 * @Version 1.0
 */
public interface UserCompanyMapper {
    /**
     * 添加用户-公司映射
     *
     * @param userId    用户id
     * @param companyId 公司id
     */
    void addUserCompany(@Param("userId") Long userId, @Param("companyId") Long companyId);
}
