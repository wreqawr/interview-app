-- 插入开发计划数据（基于当前进度重新规划）
INSERT INTO t_development_progress
(module_name, module_code, role_type, priority, status, frontend_progress, backend_progress, database_progress,
 overall_progress, estimated_hours, actual_hours, planned_start_date, planned_end_date, actual_start_date,
 actual_end_date, description, requirements, dependencies)
VALUES
-- 已完成模块（一个月前开始，现已完成）
('用户认证系统', 'auth_system', 'all', 5, 'completed', 100, 100, 100, 100, 16.0, 16.0, '2025-06-28', '2025-07-02',
 '2025-06-28', '2025-07-02', '用户登录、注册、权限验证系统', 'JWT token认证、角色权限控制、密码加密', NULL),
('数据库设计', 'database_design', 'all', 5, 'completed', 100, 100, 100, 100, 12.0, 12.0, '2025-06-28', '2025-06-30',
 '2025-06-28', '2025-06-30', '数据库表结构设计和初始化', '用户表、角色表、权限表、基础业务表', NULL),
('前端基础框架', 'frontend_framework', 'all', 5, 'completed', 100, 100, 100, 100, 20.0, 20.0, '2025-06-28',
 '2025-07-04', '2025-06-28', '2025-07-04', 'Vue3 + Element Plus基础框架搭建', '路由配置、状态管理、组件库集成', NULL),
('简历管理模块', 'resume_management', 'ROLE_JOB_SEEKER', 4, 'completed', 100, 100, 100, 100, 24.0, 24.0, '2025-07-05',
 '2025-07-12', '2025-07-05', '2025-07-12', '简历上传、编辑、模板选择、AI解析', '文件上传、在线编辑器、AI解析接口',
 'auth_system'),
('文件管理系统', 'file_management', 'all', 3, 'completed', 100, 100, 100, 100, 12.0, 12.0, '2025-07-13', '2025-07-17',
 '2025-07-13', '2025-07-17', '文件上传下载、格式转换、存储管理', '文件处理、格式转换、存储优化', 'auth_system'),

-- 第二阶段：求职者核心功能（当前阶段）
('面试预约模块', 'interview_booking', 'ROLE_JOB_SEEKER', 4, 'developing', 30, 20, 80, 40, 20.0, 8.0, '2025-07-18',
 '2025-07-25', '2025-07-18', NULL, '面试时间预约、岗位选择、提醒设置', '日历组件、预约逻辑、消息通知',
 'auth_system,resume_management'),
('个人中心', 'user_profile', 'ROLE_JOB_SEEKER', 3, 'developing', 40, 60, 100, 60, 12.0, 6.0, '2025-07-18', '2025-07-23',
 '2025-07-18', NULL, '个人信息管理、头像上传、隐私设置', '表单验证、文件上传、设置管理', 'auth_system'),
('消息通知系统', 'notification_system', 'all', 3, 'developing', 50, 30, 90, 50, 16.0, 8.0, '2025-07-18', '2025-07-25',
 '2025-07-18', NULL, '消息中心、通知推送、消息管理', '消息队列、推送服务、消息分类', 'auth_system'),

-- 第三阶段：面试功能（计划中）
('AI模拟面试', 'ai_mock_interview', 'ROLE_JOB_SEEKER', 4, 'pending', 0, 0, 0, 0, 32.0, 0, '2025-07-26', '2025-08-09',
 NULL, NULL, '技术面试、行为面试、压力面试模拟', 'AI对话、语音识别、实时评估', 'auth_system,interview_booking'),
('实时面试系统', 'real_time_interview', 'ROLE_JOB_SEEKER', 4, 'pending', 0, 0, 0, 0, 28.0, 0, '2025-08-10',
 '2025-08-23', NULL, NULL, '视频面试、屏幕共享、代码编辑器', 'WebRTC、屏幕录制、代码执行', 'auth_system,ai_mock_interview'),
('面试记录管理', 'interview_history', 'ROLE_JOB_SEEKER', 3, 'pending', 0, 0, 0, 0, 16.0, 0, '2025-08-24', '2025-08-30',
 NULL, NULL, '面试历史、录音回放、笔记管理', '音视频播放、笔记编辑器', 'real_time_interview'),
('面试报告系统', 'interview_reports', 'ROLE_JOB_SEEKER', 3, 'pending', 0, 0, 0, 0, 20.0, 0, '2025-08-31', '2025-09-07',
 NULL, NULL, '个人报告、技能评估、改进建议', '数据分析、图表展示、报告生成', 'ai_mock_interview,interview_history'),
('学习中心', 'learning_center', 'ROLE_JOB_SEEKER', 2, 'pending', 0, 0, 0, 0, 16.0, 0, '2025-09-08', '2025-09-15', NULL,
 NULL, '面试技巧学习、技能提升、行业资讯', '内容管理、学习进度跟踪', 'auth_system'),

-- 第四阶段：HR管理功能
('候选人管理', 'candidate_management', 'ROLE_HR', 4, 'pending', 0, 0, 0, 0, 24.0, 0, '2025-09-16', '2025-09-27', NULL,
 NULL, '候选人信息管理、标签管理、状态跟踪', 'CRUD操作、批量处理、搜索筛选', 'auth_system,resume_management'),
('简历筛选系统', 'resume_screening', 'ROLE_HR', 4, 'pending', 0, 0, 0, 0, 20.0, 0, '2025-09-28', '2025-10-05', NULL,
 NULL, 'AI智能筛选、关键词匹配、评分系统', 'AI算法、筛选规则、评分模型', 'candidate_management,resume_management'),
('面试管理', 'interview_management', 'ROLE_HR', 4, 'pending', 0, 0, 0, 0, 28.0, 0, '2025-10-06', '2025-10-19', NULL,
 NULL, '面试安排、面试官分配、流程管理', '日程管理、权限控制、流程配置', 'candidate_management,interview_booking'),
('面试评估', 'interview_evaluation', 'ROLE_HR', 4, 'pending', 0, 0, 0, 0, 20.0, 0, '2025-10-20', '2025-10-27', NULL,
 NULL, '评分系统、评估模板、结果汇总', '评分表单、模板管理、数据统计', 'interview_management,interview_reports'),
('数据分析', 'data_analytics', 'ROLE_HR', 3, 'pending', 0, 0, 0, 0, 24.0, 0, '2025-10-28', '2025-11-08', NULL, NULL,
 '招聘漏斗、通过率分析、趋势报告', '数据可视化、统计图表、报告生成', 'interview_evaluation,candidate_management'),
('企业管理', 'company_management', 'ROLE_HR', 3, 'pending', 0, 0, 0, 0, 16.0, 0, '2025-11-09', '2025-11-16', NULL, NULL,
 '公司信息、部门管理、岗位管理', '组织架构、权限管理', 'auth_system'),

-- 第五阶段：管理员功能
('用户管理', 'user_management', 'ROLE_ADMIN', 4, 'pending', 0, 0, 0, 0, 20.0, 0, '2025-11-17', '2025-11-26', NULL, NULL,
 '用户账户管理、角色分配、状态控制', '用户CRUD、角色权限、状态管理', 'auth_system'),
('企业管理', 'admin_company_management', 'ROLE_ADMIN', 4, 'pending', 0, 0, 0, 0, 16.0, 0, '2025-11-27', '2025-12-04',
 NULL, NULL, '企业审核、认证管理、套餐管理', '审核流程、认证验证、套餐配置', 'auth_system,company_management'),
('系统配置', 'system_configuration', 'ROLE_ADMIN', 4, 'pending', 0, 0, 0, 0, 20.0, 0, '2025-12-05', '2025-12-14', NULL,
 NULL, '系统参数、功能开关、AI模型配置', '配置管理、开关控制、模型管理', 'auth_system'),
('数据管理', 'data_management', 'ROLE_ADMIN', 3, 'pending', 0, 0, 0, 0, 16.0, 0, '2025-12-15', '2025-12-22', NULL, NULL,
 '数据统计、备份恢复、归档管理', '数据统计、备份策略、归档流程', 'auth_system,data_analytics'),
('监控日志', 'monitoring_logs', 'ROLE_ADMIN', 3, 'pending', 0, 0, 0, 0, 20.0, 0, '2025-12-23', '2025-12-30', NULL, NULL,
 '系统监控、操作日志、安全审计', '性能监控、日志记录、安全检测', 'auth_system'),

-- 第六阶段：系统优化和测试
('帮助支持系统', 'help_support', 'all', 2, 'pending', 0, 0, 0, 0, 12.0, 0, '2026-01-01', '2026-01-07', NULL, NULL,
 '在线帮助、常见问题、客服联系', '帮助文档、FAQ、客服系统', 'auth_system'),
('性能优化', 'performance_optimization', 'all', 3, 'pending', 0, 0, 0, 0, 20.0, 0, '2026-01-08', '2026-01-17', NULL,
 NULL, '前端优化、后端优化、数据库优化', '缓存策略、代码优化、查询优化', 'all_modules'),
('安全加固', 'security_enhancement', 'all', 4, 'pending', 0, 0, 0, 0, 16.0, 0, '2026-01-18', '2026-01-25', NULL, NULL,
 '安全漏洞修复、权限加固、数据加密', '安全扫描、漏洞修复、加密升级', 'all_modules'),
('系统测试', 'system_testing', 'all', 4, 'pending', 0, 0, 0, 0, 24.0, 0, '2026-01-26', '2026-02-08', NULL, NULL,
 '功能测试、性能测试、安全测试', '单元测试、集成测试、压力测试', 'all_modules'),
('部署上线', 'deployment_launch', 'all', 5, 'pending', 0, 0, 0, 0, 12.0, 0, '2026-02-09', '2026-02-13', NULL, NULL,
 '生产环境部署、监控配置、上线准备', '环境配置、监控部署、上线检查', 'system_testing');