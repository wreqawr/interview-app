package cn.minglg.ai.agent.service.impl;

import cn.minglg.ai.agent.dto.req.AiAgentInstanceDescribeRequestDto;
import cn.minglg.ai.agent.dto.req.GenerateMessageChatTokenRequestDto;
import cn.minglg.ai.agent.dto.req.RtcAuthTokenRequestDto;
import cn.minglg.ai.agent.dto.res.AiAgentInstanceDescribeResponse;
import cn.minglg.ai.agent.dto.res.GenerateMessageChatTokenResponse;
import cn.minglg.ai.agent.properties.AiAgentProperties;
import cn.minglg.ai.agent.service.AiAgentService;
import cn.minglg.ai.agent.service.ImsService;
import cn.minglg.commons.model.response.GenericResponse;
import cn.minglg.commons.model.response.ResponseCode;
import cn.minglg.commons.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * ImsServiceImpl
 *
 * @author chunlei.zcl
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ImsServiceImpl implements ImsService {
    private final AiAgentService aiAgentService;
    private final AiAgentProperties aiAgentProperties;

    /**
     * 获取RTC认证令牌
     * 该方法根据请求参数生成RTC认证令牌，如果频道ID为空则自动生成随机频道ID
     *
     * @param rtcAuthTokenRequestDto RTC认证令牌请求DTO，包含频道ID和用户ID等信息
     * @return GenericResponse<?> 包含RTC认证令牌、时间戳和频道ID的通用响应对象
     */
    @Override
    public GenericResponse<?> getRtcAuthToken(RtcAuthTokenRequestDto rtcAuthTokenRequestDto) {
        String channelId = rtcAuthTokenRequestDto.getChannelId();
        // 如果频道ID为空，则生成一个随机的频道ID
        if (StringUtils.isBlank(channelId)) {
            channelId = UUID.randomUUID().toString().replaceAll("-", "");
        }
        long timestamp = getClientTimestamp();
        // 生成客户端的rtcAuthToken，基于客户端传的userid
        String rtcAuthToken = createBase64Token(channelId, rtcAuthTokenRequestDto.getUserId(), timestamp);
        log.info("getRtcAuthToken, params: {}, rtcAuthToken:{}", rtcAuthTokenRequestDto, rtcAuthToken);
        // 构建返回数据映射
        Map<String, Object> map = new HashMap<>(1);
        map.put("rtc_auth_token", rtcAuthToken);
        map.put("timestamp", timestamp);
        map.put("channel_id", rtcAuthTokenRequestDto.getChannelId());

        return GenericResponse.builder()
                .code(ResponseCode.OK.getCode())
                .data(map)
                .message("获取RTC认证令牌成功！")
                .build();
    }


    /**
     * 生成消息聊天Token
     * 调用AI代理服务生成聊天会话Token，并根据服务调用结果构建统一响应
     *
     * @param request 包含生成Token所需参数的请求对象，包括AI代理ID、角色、用户ID、过期时间、区域等信息
     * @return GenericResponse<GenerateMessageChatTokenResponse> 统一响应对象，包含响应码、数据和消息
     */
    @Override
    public GenericResponse<GenerateMessageChatTokenResponse> generateMessageChatToken(GenerateMessageChatTokenRequestDto request) {
        GenerateMessageChatTokenResponse response = aiAgentService.generateMessageChatToken(request.getAiAgentId(), request.getRole(), request.getUserId(), request.getExpire(), request.getRegion());
        Integer code;
        String message;

        // 处理服务调用成功的情况
        if (response != null && response.getCode() == ResponseCode.OK) {
            code = ResponseCode.OK.getCode();
            message = "生成会话Token成功！";
        } else {
            code = ResponseCode.GENERATE_MESSAGE_CHAT_TOKEN_ERROR.getCode();
            message = "生成会话Token失败！";
        }
        return GenericResponse.<GenerateMessageChatTokenResponse>builder()
                .code(code)
                .data(response)
                .message(message)
                .build();
    }

    /**
     * 描述AI代理实例信息
     * 该方法调用AI代理服务获取指定实例的描述信息，并封装成通用响应格式返回
     *
     * @param request AI代理实例描述请求DTO，包含实例ID和地区信息
     * @return GenericResponse<AiAgentInstanceDescribeResponse> 通用响应对象，包含响应码、数据和消息
     */
    @Override
    public GenericResponse<AiAgentInstanceDescribeResponse> describeAiAgentInstance(AiAgentInstanceDescribeRequestDto request) {
        // 调用AI代理服务获取实例描述信息
        AiAgentInstanceDescribeResponse response = aiAgentService.describeAiAgentInstance(request.getAiAgentInstanceId(), request.getRegion());
        Integer code;
        String message;
        if (response == null || response.getCode() != ResponseCode.OK) {
            code = ResponseCode.DESCRIBE_AI_AGENT_INSTANCE_ERROR.getCode();
            message = "获取AI代理实例信息失败！";
        } else {
            code = ResponseCode.OK.getCode();
            message = "获取AI代理实例信息成功！";
        }
        return GenericResponse.<AiAgentInstanceDescribeResponse>builder()
                .code(code)
                .data(response)
                .message(message)
                .build();
    }


    /**
     * 获取客户端时间戳
     *
     * @return 返回未来24小时后的时间戳（以秒为单位）
     */
    private long getClientTimestamp() {
        /* 过期时间戳最大24小时 */
        return Instant.now().plus(24, ChronoUnit.HOURS).getEpochSecond();
    }


    /**
     * 创建Base64编码的认证令牌
     *
     * @param channelId 频道ID
     * @param userId    用户ID
     * @param timestamp 时间戳
     * @return Base64编码的认证令牌字符串
     */
    private String createBase64Token(String channelId, String userId, long timestamp) {
        // 获取直播麦克风应用ID和密钥
        String liveMicAppId = aiAgentProperties.getLiveMic().getAppId();
        String liveMicAppKey = aiAgentProperties.getLiveMic().getAppKey();

        // 构造RTC认证字符串并进行SHA256加密
        String rtcAuthStr = String.format("%s%s%s%s%d", liveMicAppId, liveMicAppKey, channelId, userId, timestamp);
        String rtcAuth = sha256(rtcAuthStr);

        // 构建令牌JSON对象
        Map<String, Object> tokenMap = Map.of(
                "appid", liveMicAppId,
                "channelid", channelId,
                "userid", userId,
                "nonce", "",
                "timestamp", timestamp,
                "token", rtcAuth
        );
        String tokenJson = JsonUtils.toJsonStr(tokenMap);
        // 将JSON对象转换为Base64编码的字符串并返回
        return Base64.getEncoder().encodeToString(tokenJson.getBytes(StandardCharsets.UTF_8));
    }


    /**
     * 字符串签名
     *
     * @param input 输入源
     * @return 返回签名
     */
    private static String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
