INSERT INTO t_permission (permission_code, description)
VALUES
-- 求职者（JOB_SEEKER）功能权限
-- 简历管理模块
('ai-interview:resume:upload', '简历上传'),
('ai-interview:resume:modify', '在线简历编辑'),
('ai-interview:resume:template:choose', '简历模板选择'),
('ai-interview:resume:view', '简历查看'),
('ai-interview:resume:export', '简历导出'),
('ai-interview:resume:version:manage', '简历版本管理'),
('ai-interview:resume:parse', '简历AI解析'),
('ai-interview:resume:skill:extract', '技能标签提取'),
('ai-interview:resume:experience:analyze', '工作经验分析'),
('ai-interview:resume:education:recognize', '教育背景识别'),

-- 面试预约模块
('ai-interview:appointment:view', '查看可预约时间'),
('ai-interview:appointment:create', '面试预约'),
('ai-interview:appointment:modify', '修改面试预约'),
('ai-interview:appointment:cancel', '取消面试预约'),
('ai-interview:appointment:reminder:set', '面试提醒设置'),
('ai-interview:appointment:position:select', '选择面试岗位'),
('ai-interview:appointment:company:select', '选择面试公司'),

-- AI模拟面试模块
('ai-interview:mock:technical:start', '开始技术面试模拟'),
('ai-interview:mock:behavioral:start', '开始行为面试模拟'),
('ai-interview:mock:stress:start', '开始压力面试模拟'),
('ai-interview:mock:scenario:select', '面试场景选择'),
('ai-interview:mock:question:view', '查看面试题目'),
('ai-interview:mock:answer:submit', '提交面试答案'),
('ai-interview:mock:result:view', '查看模拟面试结果'),

-- 实时面试模块
('ai-interview:interview:video:join', '加入视频面试'),
('ai-interview:interview:screen:share', '屏幕共享'),
('ai-interview:interview:code:edit', '代码编辑器使用'),
('ai-interview:interview:timer:view', '面试计时器查看'),
('ai-interview:interview:record:start', '开始面试录制'),
('ai-interview:interview:record:stop', '停止面试录制'),

-- 面试记录模块
('ai-interview:history:view', '面试历史查看'),
('ai-interview:history:record:play', '面试录音回放'),
('ai-interview:history:video:play', '面试录像回放'),
('ai-interview:history:note:create', '创建面试笔记'),
('ai-interview:history:note:modify', '修改面试笔记'),
('ai-interview:history:feedback:view', '查看面试反馈'),

-- 面试报告模块
('ai-interview:report:personal:view', '个人面试报告查看'),
('ai-interview:report:performance:analyze', '面试表现分析'),
('ai-interview:report:skill:assess', '技能评估报告'),
('ai-interview:report:improvement:view', '改进建议查看'),
('ai-interview:report:trend:analyze', '历史趋势分析'),
('ai-interview:report:compare:view', '对比分析查看'),
('ai-interview:report:industry:compare', '行业平均水平对比'),
('ai-interview:report:growth:track', '个人成长轨迹'),

-- 个人中心模块
('ai-interview:profile:info:view', '个人信息查看'),
('ai-interview:profile:info:modify', '个人信息修改'),
('ai-interview:profile:avatar:upload', '头像上传'),
('ai-interview:profile:contact:manage', '联系方式管理'),
('ai-interview:profile:privacy:set', '隐私设置'),

-- 学习中心模块
('ai-interview:learning:skill:view', '面试技巧学习'),
('ai-interview:learning:improvement:view', '技能提升建议'),
('ai-interview:learning:news:view', '行业资讯查看'),
('ai-interview:learning:progress:track', '学习进度跟踪'),

-- HR功能权限
-- 候选人管理模块
('ai-interview:candidate:view', '候选人信息查看'),
('ai-interview:candidate:create', '候选人信息创建'),
('ai-interview:candidate:modify', '候选人信息修改'),
('ai-interview:candidate:delete', '候选人信息删除'),
('ai-interview:candidate:tag:manage', '候选人标签管理'),
('ai-interview:candidate:status:track', '候选人状态跟踪'),
('ai-interview:candidate:batch:operate', '候选人批量操作'),
('ai-interview:candidate:search', '候选人搜索'),
('ai-interview:candidate:filter', '候选人筛选'),

-- 简历筛选模块
('ai-interview:resume:filter:ai', 'AI智能筛选'),
('ai-interview:resume:filter:keyword', '关键词匹配筛选'),
('ai-interview:resume:filter:skill', '技能匹配筛选'),
('ai-interview:resume:filter:score', '简历评分'),
('ai-interview:resume:filter:batch', '批量筛选操作'),

-- 候选人沟通模块
('ai-interview:communication:message:send', '发送消息通知'),
('ai-interview:communication:invitation:send', '发送面试邀请'),
('ai-interview:communication:feedback:collect', '收集反馈'),
('ai-interview:communication:record:view', '沟通记录查看'),
('ai-interview:communication:template:manage', '消息模板管理'),

-- 面试管理模块
('ai-interview:interview:arrange:create', '创建面试安排'),
('ai-interview:interview:arrange:modify', '修改面试安排'),
('ai-interview:interview:arrange:cancel', '取消面试安排'),
('ai-interview:interview:interviewer:assign', '面试官分配'),
('ai-interview:interview:room:manage', '面试房间管理'),
('ai-interview:interview:process:set', '面试流程设置'),
('ai-interview:interview:monitor:real-time', '实时面试监控'),
('ai-interview:interview:quality:assess', '面试质量评估'),
('ai-interview:interview:interviewer:evaluate', '面试官表现评价'),
('ai-interview:interview:exception:handle', '异常情况处理'),

-- 面试评估模块
('ai-interview:evaluation:score:create', '创建面试评分'),
('ai-interview:evaluation:score:modify', '修改面试评分'),
('ai-interview:evaluation:dimension:manage', '评估维度管理'),
('ai-interview:evaluation:template:manage', '评估模板管理'),
('ai-interview:evaluation:result:summary', '评估结果汇总'),
('ai-interview:evaluation:report:generate', '生成评估报告'),

-- 数据分析模块
('ai-interview:analytics:funnel:analyze', '招聘漏斗分析'),
('ai-interview:analytics:source:statistics', '候选人来源统计'),
('ai-interview:analytics:pass-rate:analyze', '面试通过率分析'),
('ai-interview:analytics:efficiency:assess', '招聘效率评估'),
('ai-interview:analytics:quality:report', '面试质量报告'),
('ai-interview:analytics:interviewer:report', '面试官表现报告'),
('ai-interview:analytics:candidate:report', '候选人评估报告'),
('ai-interview:analytics:trend:analyze', '招聘趋势分析'),

-- 企业管理模块
('ai-interview:company:info:view', '公司信息查看'),
('ai-interview:company:info:modify', '公司信息修改'),
('ai-interview:company:department:manage', '部门管理'),
('ai-interview:company:position:manage', '岗位管理'),
('ai-interview:company:requirement:manage', '招聘需求管理'),
('ai-interview:company:interviewer:manage', '面试官管理'),
('ai-interview:company:interviewer:permission:set', '面试官权限设置'),
('ai-interview:company:interviewer:training:record', '面试官培训记录'),
('ai-interview:company:interviewer:performance:evaluate', '面试官绩效评估'),
('ai-interview:company:process:design', '招聘流程设计'),
('ai-interview:company:process:configure', '面试环节配置'),
('ai-interview:company:process:standard:set', '评估标准设置'),
('ai-interview:company:process:template:manage', '流程模板管理'),

-- 系统管理员功能权限
-- 用户管理模块
('ai-interview:admin:user:register:audit', '用户注册审核'),
('ai-interview:admin:user:info:view', '用户信息查看'),
('ai-interview:admin:user:info:modify', '用户信息修改'),
('ai-interview:admin:user:status:control', '用户状态控制'),
('ai-interview:admin:user:password:reset', '密码重置'),
('ai-interview:admin:user:delete', '用户删除'),
('ai-interview:admin:role:define', '角色定义'),
('ai-interview:admin:role:assign', '角色分配'),
('ai-interview:admin:permission:configure', '权限配置'),
('ai-interview:admin:permission:group:manage', '权限组管理'),
('ai-interview:admin:permission:audit', '权限审计'),

-- 企业用户管理模块
('ai-interview:admin:company:register:audit', '企业注册审核'),
('ai-interview:admin:company:info:manage', '企业信息管理'),
('ai-interview:admin:company:certification:manage', '企业认证管理'),
('ai-interview:admin:company:status:control', '企业状态控制'),
('ai-interview:admin:company:blacklist:manage', '企业黑名单管理'),

-- 企业服务模块
('ai-interview:admin:company:package:manage', '企业套餐管理'),
('ai-interview:admin:company:service:level:set', '服务级别设置'),
('ai-interview:admin:company:quota:manage', '企业配额管理'),
('ai-interview:admin:company:billing:manage', '企业账单管理'),

-- 系统配置模块
('ai-interview:admin:system:parameter:configure', '系统参数配置'),
('ai-interview:admin:system:function:switch', '功能开关控制'),
('ai-interview:admin:system:announcement:manage', '系统公告管理'),
('ai-interview:admin:system:help:manage', '帮助文档管理'),
('ai-interview:admin:ai:model:configure', 'AI模型配置'),
('ai-interview:admin:ai:model:version:manage', '模型版本管理'),
('ai-interview:admin:ai:model:performance:monitor', '模型性能监控'),
('ai-interview:admin:ai:model:training:manage', '模型训练管理'),
('ai-interview:admin:interview:question:manage', '面试题型管理'),
('ai-interview:admin:interview:scoring:configure', '评分标准配置'),
('ai-interview:admin:interview:process:template', '面试流程模板'),
('ai-interview:admin:interview:dimension:set', '评估维度设置'),

-- 数据管理模块
('ai-interview:admin:data:user:statistics', '用户活跃度统计'),
('ai-interview:admin:data:system:usage:statistics', '系统使用情况统计'),
('ai-interview:admin:data:interview:statistics', '面试数据统计'),
('ai-interview:admin:data:revenue:statistics', '收入统计'),
('ai-interview:admin:data:backup:strategy', '数据备份策略'),
('ai-interview:admin:data:backup:file:manage', '备份文件管理'),
('ai-interview:admin:data:recovery', '数据恢复'),
('ai-interview:admin:data:archive', '数据归档'),

-- 监控与日志模块
('ai-interview:admin:monitor:server:status', '服务器状态监控'),
('ai-interview:admin:monitor:performance:metrics', '性能指标监控'),
('ai-interview:admin:monitor:error:rate', '错误率监控'),
('ai-interview:admin:monitor:resource:usage', '资源使用监控'),
('ai-interview:admin:log:user:operation', '用户操作日志'),
('ai-interview:admin:log:system:operation', '系统操作日志'),
('ai-interview:admin:log:security:event', '安全事件日志'),
('ai-interview:admin:log:audit', '审计日志'),
('ai-interview:admin:security:login:detect', '异常登录检测'),
('ai-interview:admin:security:sensitive:monitor', '敏感操作监控'),
('ai-interview:admin:security:leak:detect', '数据泄露检测'),
('ai-interview:admin:security:incident:handle', '安全事件处理'),

-- 通用功能权限
-- 认证授权模块
('ai-interview:auth:login:password', '账号密码登录'),
('ai-interview:auth:login:sms', '手机验证码登录'),
('ai-interview:auth:login:third-party', '第三方登录'),
('ai-interview:auth:register', '用户注册'),
('ai-interview:auth:password:forgot', '忘记密码'),
('ai-interview:auth:security:set', '账号安全设置'),
('ai-interview:auth:role:control', '基于角色的权限控制'),
('ai-interview:auth:function:control', '功能级权限控制'),
('ai-interview:auth:data:control', '数据级权限控制'),
('ai-interview:auth:operation:audit', '操作审计'),

-- 消息通知模块
('ai-interview:notification:interview:send', '面试通知发送'),
('ai-interview:notification:system:send', '系统公告发送'),
('ai-interview:notification:reminder:send', '重要提醒发送'),
('ai-interview:notification:push:send', '消息推送'),
('ai-interview:notification:center:view', '消息中心查看'),
('ai-interview:notification:category:manage', '消息分类管理'),
('ai-interview:notification:setting:manage', '消息设置管理'),
('ai-interview:notification:history:view', '消息历史查看'),

-- 系统工具模块
('ai-interview:tool:file:upload', '文件上传'),
('ai-interview:tool:file:download', '文件下载'),
('ai-interview:tool:file:convert', '文件格式转换'),
('ai-interview:tool:file:preview', '文件预览'),
('ai-interview:tool:storage:manage', '存储空间管理'),
('ai-interview:tool:help:online', '在线帮助'),
('ai-interview:tool:faq:view', '常见问题查看'),
('ai-interview:tool:customer:contact', '联系客服'),
('ai-interview:tool:feedback:submit', '意见反馈提交');