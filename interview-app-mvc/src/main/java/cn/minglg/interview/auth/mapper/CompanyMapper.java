package cn.minglg.interview.auth.mapper;

import cn.minglg.interview.auth.pojo.Company;
import org.apache.ibatis.annotations.Param;

/**
 * ClassName:CompanyMapper
 * Package:cn.minglg.interview.mapper
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/7/13
 * @Version 1.0
 */
public interface CompanyMapper {
    /**
     * 根据用户ID获取关联的公司
     *
     * @param userId 用户ID
     * @return 公司信息
     */
    Company getCompanyByUserId(@Param("userId") Long userId);

    /**
     * 获取或创建公司ID
     *
     * @param company 公司对象
     */
    void upsertCompany(@Param("company") Company company);
}
