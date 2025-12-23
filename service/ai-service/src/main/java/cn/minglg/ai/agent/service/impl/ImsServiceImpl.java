package cn.minglg.ai.agent.service.impl;

import cn.minglg.ai.agent.dto.req.AiAgentInstanceDescribeRequestDto;
import cn.minglg.ai.agent.dto.req.GenerateMessageChatTokenRequestDto;
import cn.minglg.ai.agent.dto.req.RtcAuthTokenRequestDto;
import cn.minglg.ai.agent.dto.res.AiAgentInstanceDescribeResponse;
import cn.minglg.ai.agent.dto.res.GenerateMessageChatTokenResponse;
import cn.minglg.ai.agent.dto.res.RtcAuthTokenResponse;
import cn.minglg.ai.agent.properties.AiAgentProperties;
import cn.minglg.ai.agent.service.AiAgentService;
import cn.minglg.ai.agent.service.ImsService;
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
     *
     * @param rtcAuthTokenRequestDto RTC认证令牌请求参数对象，包含频道ID和用户ID等信息
     * @return RtcAuthTokenResponse 包含认证令牌和时间戳的响应对象
     */
    @Override
    public RtcAuthTokenResponse getRtcAuthToken(RtcAuthTokenRequestDto rtcAuthTokenRequestDto) {
        String channelId = rtcAuthTokenRequestDto.getChannelId();
        // 如果频道ID为空，则生成一个随机的频道ID
        if (StringUtils.isBlank(channelId)) {
            channelId = UUID.randomUUID().toString().replaceAll("-", "");
        }
        long timestamp = getClientTimestamp();
        // 生成客户端的rtcAuthToken，基于客户端传的userid
        String rtcAuthToken = createBase64Token(channelId, rtcAuthTokenRequestDto.getUserId(), timestamp);
        log.info("getRtcAuthToken, params: {}, rtcAuthToken:{}", rtcAuthTokenRequestDto, rtcAuthToken);

        return RtcAuthTokenResponse.builder().authToken(rtcAuthToken).timestamp(timestamp).build();
    }


    /**
     * 生成消息聊天令牌
     *
     * @param request 包含生成令牌所需参数的请求对象，包含AI代理ID、角色、用户ID、过期时间和区域信息
     * @return GenerateMessageChatTokenResponse 生成的消息聊天令牌响应对象
     */
    @Override
    public GenerateMessageChatTokenResponse generateMessageChatToken(GenerateMessageChatTokenRequestDto request) {
        return aiAgentService.generateMessageChatToken(request.getAiAgentId(), request.getRole(), request.getUserId(), request.getExpire(), request.getRegion());
    }


    /**
     * 描述AI代理实例的详细信息
     *
     * @param request 包含AI代理实例ID和区域信息的请求对象
     * @return AI代理实例的描述信息响应对象
     */
    @Override
    public AiAgentInstanceDescribeResponse describeAiAgentInstance(AiAgentInstanceDescribeRequestDto request) {
        // 调用AI代理服务获取实例描述信息
        return aiAgentService.describeAiAgentInstance(request.getAiAgentInstanceId(), request.getRegion());
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
