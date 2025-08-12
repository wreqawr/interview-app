-- 插入岗位测试数据
INSERT INTO t_jobs (company_id, hr_user_id, job_title, job_type, job_level, department, work_location, salary_min,
                    salary_max, salary_range, experience_years, education_level, job_description, job_requirements,
                    job_responsibilities, benefits, contact_info, application_deadline, status, is_featured, is_urgent,
                    tags, published_time)
VALUES
-- 高级Java开发工程师
(1, 6, '高级Java开发工程师', 'FULL_TIME', 'SENIOR', '技术部', '北京市朝阳区', 25.00, 40.00, '25K-40K', 5, 'BACHELOR',
 '负责公司核心业务系统的设计与开发，参与技术架构决策，指导初级开发人员。',
 '1. 5年以上Java开发经验，精通Spring Boot、Spring Cloud等框架\n2. 熟悉分布式系统设计，有微服务架构经验\n3. 掌握MySQL、Redis、消息队列等中间件\n4. 具备良好的代码设计能力和问题解决能力',
 '1. 参与系统架构设计和技术选型\n2. 负责核心模块的开发和维护\n3. 编写技术文档，参与代码评审\n4. 指导初级开发人员，提升团队技术水平',
 '五险一金、年终奖、股票期权、免费三餐、健身房、带薪年假',
 'hr@company.com / 010-12345678',
 DATE_ADD(CURDATE(), INTERVAL 30 DAY),
 'PUBLISHED', TRUE, FALSE,
 'Java,Spring,微服务,分布式',
 CURRENT_TIMESTAMP),

-- 前端开发工程师
(2, 6, '前端开发工程师', 'FULL_TIME', 'MIDDLE', '前端部', '深圳市南山区', 15.00, 25.00, '15K-25K', 3, 'BACHELOR',
 '负责公司产品的前端界面开发，与产品、设计、后端团队协作，打造优秀的用户体验。',
 '1. 3年以上前端开发经验，精通HTML、CSS、JavaScript\n2. 熟练使用Vue.js、React等主流框架\n3. 了解前端工程化，熟悉Webpack、Git等工具\n4. 具备良好的UI/UX设计感',
 '1. 根据产品需求开发前端页面\n2. 与后端API对接，实现数据交互\n3. 优化页面性能，提升用户体验\n4. 参与前端技术选型和架构设计',
 '五险一金、年终奖、免费下午茶、弹性工作制、技术培训',
 'hr@company.com / 0755-12345678',
 DATE_ADD(CURDATE(), INTERVAL 45 DAY),
 'PUBLISHED', FALSE, TRUE,
 '前端,Vue,React,JavaScript',
 CURRENT_TIMESTAMP),

-- 数据科学家
(3, 6, '数据科学家', 'FULL_TIME', 'EXPERT', '数据部', '上海市浦东新区', 35.00, 60.00, '35K-60K', 8, 'POSTGRADUATE',
 '负责公司数据分析和机器学习模型的开发，为业务决策提供数据支持，推动数据驱动文化。',
 '1. 8年以上数据分析或机器学习经验\n2. 精通Python、R等编程语言\n3. 熟悉机器学习算法，有深度学习经验\n4. 具备统计学背景，熟悉SQL、Hadoop等大数据技术',
 '1. 构建和优化机器学习模型\n2. 分析业务数据，提供洞察报告\n3. 设计数据产品，推动数据应用\n4. 指导初级数据分析师',
 '五险一金、年终奖、股票期权、免费咖啡、健身房、弹性工作制',
 'hr@company.com / 021-12345678',
 DATE_ADD(CURDATE(), INTERVAL 60 DAY),
 'PUBLISHED', TRUE, FALSE,
 '数据科学,机器学习,Python,深度学习',
 CURRENT_TIMESTAMP),

-- 产品经理
(4, 6, '产品经理', 'FULL_TIME', 'MIDDLE', '产品部', '广州市天河区', 18.00, 30.00, '18K-30K', 4, 'BACHELOR',
 '负责公司产品的规划、设计和迭代，与开发、设计、运营等团队协作，推动产品成功。',
 '1. 4年以上产品经理经验，有互联网产品经验\n2. 熟悉产品设计流程，具备用户研究能力\n3. 了解技术实现原理，能与开发团队有效沟通\n4. 具备数据分析能力，能基于数据做决策',
 '1. 负责产品需求分析和功能设计\n2. 编写产品需求文档，与开发团队协作\n3. 跟踪产品数据，持续优化产品体验\n4. 与运营团队协作，推动产品增长',
 '五险一金、年终奖、免费午餐、带薪年假、产品培训',
 'hr@company.com / 020-12345678',
 DATE_ADD(CURDATE(), INTERVAL 40 DAY),
 'PUBLISHED', FALSE, FALSE,
 '产品经理,需求分析,用户体验,数据分析',
 CURRENT_TIMESTAMP),

-- 运维工程师
(5, 6, '运维工程师', 'FULL_TIME', 'JUNIOR', '运维部', '杭州市西湖区', 12.00, 20.00, '12K-20K', 2, 'BACHELOR',
 '负责公司服务器和网络基础设施的维护，确保系统稳定运行，参与自动化运维建设。',
 '1. 2年以上运维经验，熟悉Linux系统管理\n2. 了解Docker、Kubernetes等容器技术\n3. 熟悉Shell脚本，具备基本的编程能力\n4. 了解网络协议，具备故障排查能力',
 '1. 负责服务器和网络设备的日常维护\n2. 监控系统运行状态，及时处理故障\n3. 参与自动化运维脚本的开发\n4. 协助开发团队进行系统部署',
 '五险一金、年终奖、免费晚餐、技术培训、带薪年假',
 'hr@company.com / 0571-12345678',
 DATE_ADD(CURDATE(), INTERVAL 35 DAY),
 'PUBLISHED', FALSE, FALSE,
 '运维,Linux,Docker,自动化',
 CURRENT_TIMESTAMP),

-- 测试工程师
(6, 6, '测试工程师', 'FULL_TIME', 'MIDDLE', '测试部', '成都市高新区', 15.00, 25.00, '15K-25K', 3, 'BACHELOR',
 '负责公司产品的质量保证，设计测试用例，执行测试计划，推动产品质量提升。',
 '1. 3年以上测试经验，熟悉软件测试理论和方法\n2. 了解自动化测试，熟悉Selenium等工具\n3. 具备基本的编程能力，能编写测试脚本\n4. 熟悉测试流程，具备缺陷管理经验',
 '1. 设计测试用例，制定测试计划\n2. 执行功能测试、性能测试等\n3. 编写自动化测试脚本\n4. 跟踪缺陷修复，验证产品质量',
 '五险一金、年终奖、免费下午茶、弹性工作制、测试培训',
 'hr@company.com / 028-12345678',
 DATE_ADD(CURDATE(), INTERVAL 50 DAY),
 'PUBLISHED', FALSE, FALSE,
 '测试,自动化,质量保证,缺陷管理',
 CURRENT_TIMESTAMP); 