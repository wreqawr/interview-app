package org.minglg.ai.agent.service.impl;

import cn.minglg.commons.model.response.ResponseCode;
import cn.minglg.commons.utils.JsonUtils;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.Client;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teaopenapi.models.OpenApiRequest;
import com.aliyun.teaopenapi.models.Params;
import com.aliyun.teautil.models.RuntimeOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.minglg.ai.agent.entity.AiAgentInstanceDescribeResponse;
import org.minglg.ai.agent.entity.GenerateMessageChatTokenResponse;
import org.minglg.ai.agent.properties.AiAgentProperties;
import org.minglg.ai.agent.service.AiAgentService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AiAgentServiceImpl
 *
 * @author chunlei.zcl
 */
@SuppressWarnings("unchecked")
@Slf4j
@RequiredArgsConstructor
public class AiAgentServiceImpl implements AiAgentService {

    private final Map<String, Client> clientByRegion = new ConcurrentHashMap<>();
    private final AiAgentProperties aiAgentProperties;

    /**
     * 根据指定区域获取客户端实例
     *
     * @param region 区域标识符，用于确定要获取的客户端实例
     * @return 返回对应区域的客户端实例，如果不存在则创建新的实例
     */
    public Client getClient(String region) {
        if (clientByRegion.containsKey(region)) {
            return clientByRegion.get(region);
        }
        // 使用 computeIfAbsent 方法在并发环境下安全地获取或创建 Client
        return clientByRegion.computeIfAbsent(region, this::createClient);
    }


    /**
     * 创建阿里云ICE客户端
     *
     * @param region 区域标识符，用于构建访问域名
     * @return 配置好的Client实例
     * @throws RuntimeException 当创建客户端过程中发生异常时抛出
     */
    private Client createClient(String region) {
        try {
            Config config = new Config()
                    // 您的 AccessKey ID
                    .setAccessKeyId(aiAgentProperties.getAccess().getKeyId())
                    // 您的 AccessKey Secret
                    .setAccessKeySecret(aiAgentProperties.getAccess().getKeySecret());
            // 访问的域名
            config.endpoint = String.format("ice.%s.aliyuncs.com", region);
            return new Client(config);
        } catch (Exception e) {
            log.error("createClient error. e:{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }


    /**
     * 生成消息聊天令牌
     * 该方法通过调用API接口生成用于消息聊天的认证令牌，支持指定AI代理、角色、用户等参数
     *
     * @param aiAgentId AI代理ID，用于标识特定的AI代理服务
     * @param role      用户角色，用于权限控制和功能限制
     * @param userId    用户ID，用于标识调用接口的用户
     * @param expire    令牌过期时间，单位为秒
     * @param region    服务区域，用于指定API调用的目标区域
     * @return GenerateMessageChatTokenResponse 包含生成的令牌信息的响应对象
     */
    @Override
    public GenerateMessageChatTokenResponse generateMessageChatToken(String aiAgentId, String role, String userId, Integer expire, String region) {
        Params params = new Params()
                // 接口名称
                .setAction("GenerateMessageChatToken")
                // 接口版本
                .setVersion("2020-11-09")
                // 接口协议
                .setProtocol("HTTPS")
                // 接口 HTTP 方法
                .setMethod("POST")
                .setAuthType("AK")
                .setStyle("HTTPS")
                // 接口 PATH
                .setPathname("/")
                // 接口请求体内容格式
                .setReqBodyType("json")
                // 接口响应体内容格式
                .setBodyType("json");

        // 构造查询参数
        Map<String, Object> queries = new HashMap<>();
        queries.put("AIAgentId", aiAgentId);
        queries.put("Role", role);
        queries.put("UserId", userId);
        queries.put("Expire", expire);
        RuntimeOptions runtime = new RuntimeOptions();
        String requestId = StringUtils.EMPTY;
        String message = StringUtils.EMPTY;
        String errCode = StringUtils.EMPTY;

        try {
            OpenApiRequest request = new OpenApiRequest()
                    .setQuery(com.aliyun.openapiutil.Client.query(queries));

            // 获取指定区域的客户端实例
            Client localClient = getClient(region);
            long start = System.currentTimeMillis();
            log.info("generateMessageChatToken, queries:{}, region:{}", JsonUtils.toJsonStr(queries), region);

            // 发起 API 调用并记录耗时
            Map<String, ?> response = localClient.callApi(params, request, runtime);
            log.info("generateMessageChatToken, response:{}, cost:{}ms", JsonUtils.toJsonStr(response), (System.currentTimeMillis() - start));

            // 处理成功响应数据
            if (response != null) {
                if (response.containsKey("statusCode")) {
                    Integer statusCode = (Integer) response.get("statusCode");
                    if (200 == statusCode) {
                        Map<String, Object> body = (Map<String, Object>) response.get("body");
                        String appId = (String) body.get("AppId");
                        String token = (String) body.get("Token");
                        String userIdResponse = (String) body.get("UserId");
                        String nonce = (String) body.get("Nonce");
                        String roleResponse = (String) body.get("Role");
                        long timestamp = (Long) body.get("TimeStamp");
                        String appSign = (String) body.get("AppSign");

                        requestId = (String) body.get("RequestId");

                        return GenerateMessageChatTokenResponse.builder()
                                .appId(appId)
                                .token(token)
                                .userId(userIdResponse)
                                .nonce(nonce)
                                .role(roleResponse)
                                .timestamp(timestamp)
                                .appSign(appSign)
                                .code(ResponseCode.OK)
                                .requestId(requestId)
                                .build();
                    }
                }
            }
        } catch (TeaException e) {
            log.error("generateMessageChatToken Tea error. e:{}", e.getMessage());
            requestId = e.getData().get("RequestId").toString();
            message = e.getMessage();
            errCode = e.getCode();
        } catch (NullPointerException e) {
            message = e.getMessage();
            log.error("generateMessageChatToken NullPointerException error, region:{}", region);
        } catch (Exception e) {
            message = e.getMessage();
            log.error("generateMessageChatToken error. e:{}", e.getMessage());
        }

        // 构建并返回异常情况下的统一响应结构
        return GenerateMessageChatTokenResponse.builder()
                .message(message)
                .requestId(requestId)
                .errorCode(errCode)
                .build();
    }


    /**
     * 查询AI智能体实例详情
     * 通过实例ID和区域信息调用OpenAPI接口获取AI智能体实例的详细信息
     *
     * @param aiAgentInstanceId AI智能体实例ID
     * @param region            区域标识符
     * @return AiAgentInstanceDescribeResponse 包含实例详情的响应对象
     */
    @Override
    public AiAgentInstanceDescribeResponse describeAiAgentInstance(String aiAgentInstanceId, String region) {
        // 构造接口请求的基本参数
        Params params = new Params()
                // 接口名称
                .setAction("DescribeAIAgentInstance")
                // 接口版本
                .setVersion("2020-11-09")
                // 接口协议
                .setProtocol("HTTPS")
                // 接口 HTTP 方法
                .setMethod("POST")
                .setAuthType("AK")
                .setStyle("HTTPS")
                // 接口 PATH
                .setPathname("/")
                // 接口请求体内容格式
                .setReqBodyType("json")
                // 接口响应体内容格式
                .setBodyType("json");

        // 设置请求参数
        Map<String, Object> queries = new HashMap<>();
        queries.put("InstanceId", aiAgentInstanceId);

        // 初始化运行时选项和变量
        RuntimeOptions runtime = new RuntimeOptions();
        String requestId = StringUtils.EMPTY;
        String message = StringUtils.EMPTY;
        String errCode = StringUtils.EMPTY;
        try {
            // 构建OpenAPI请求对象
            OpenApiRequest request = new OpenApiRequest()
                    .setQuery(com.aliyun.openapiutil.Client.query(queries));

            // 获取指定region的客户端并发起调用
            Client localClient = getClient(region);
            long start = System.currentTimeMillis();
            log.info("describeAiAgentInstance, queries:{}, region:{}", JsonUtils.toJsonStr(queries), region);
            Map<String, ?> response = localClient.callApi(params, request, runtime);
            log.info("describeAiAgentInstance, response:{}, cost:{}ms", JsonUtils.toJsonStr(response), (System.currentTimeMillis() - start));

            // 处理正常响应结果
            if (response != null && response.containsKey("statusCode")) {
                Integer statusCode = (Integer) response.get("statusCode");
                if (200 == statusCode) {
                    Map<String, Object> body = (Map<String, Object>) response.get("body");
                    Map<String, Object> instance = (Map<String, Object>) body.get("Instance");

                    // 提取关键字段
                    String callLogUrl = (String) instance.get("CallLogUrl");
                    String runtimeConfig = JsonUtils.toJsonStr(instance.get("RuntimeConfig"));
                    String status = (String) instance.get("Status");
                    String template_config = JsonUtils.toJsonStr(instance.get("TemplateConfig"));
                    String user_data = (String) instance.get("UserData");
                    requestId = (String) body.get("RequestId");

                    // 构造成功响应对象
                    return AiAgentInstanceDescribeResponse.builder()
                            .callLogUrl(callLogUrl)
                            .runtimeConfig(runtimeConfig)
                            .status(status)
                            .templateConfig(template_config)
                            .userData(user_data)
                            .code(ResponseCode.OK)
                            .requestId(requestId)
                            .build();
                }
            }
        } catch (TeaException e) {
            // 捕获 Tea 异常，并记录错误信息
            log.error("describeAiAgentInstance Tea error. e:{}", e.getMessage());
            requestId = e.getData().get("RequestId").toString();
            message = e.getMessage();
            errCode = e.getCode();
        } catch (NullPointerException e) {
            // 捕获空指针异常
            message = e.getMessage();
            log.error("describeAiAgentInstance NullPointerException error, region:{}", region);
        } catch (Exception e) {
            // 兜底捕获其他所有异常
            message = e.getMessage();
            log.error("describeAiAgentInstance error. e:{}", e.getMessage());
        }

        // 构造失败响应对象
        return AiAgentInstanceDescribeResponse.builder()
                .message(message)
                .requestId(requestId)
                .errorCode(errCode)
                .build();
    }

}
