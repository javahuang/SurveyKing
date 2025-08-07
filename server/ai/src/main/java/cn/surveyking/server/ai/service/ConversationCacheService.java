package cn.surveyking.server.ai.service;

import cn.surveyking.server.ai.domain.ChatRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 对话缓存服务
 * 用于缓存用户对话消息，默认保留10分钟
 * 
 * @author zzr
 */
@Service
public class ConversationCacheService {

    private final ConcurrentHashMap<String, ConversationCache> conversationMap = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public ConversationCacheService() {
        // 每分钟清理一次过期的对话
        scheduler.scheduleAtFixedRate(this::cleanExpiredConversations, 1, 1, TimeUnit.MINUTES);
    }

    /**
     * 创建新对话缓存
     */
    public void createConversation(String conversationId) {
        conversationMap.put(conversationId, new ConversationCache());
    }

    /**
     * 添加消息到对话
     */
    public void addMessage(String conversationId, ChatRequest.EnterMessage message) {
        ConversationCache cache = conversationMap.get(conversationId);
        if (cache != null) {
            cache.addMessage(message);
            cache.updateLastAccessTime();
        }
    }

    /**
     * 获取对话历史消息
     */
    public List<ChatRequest.EnterMessage> getMessages(String conversationId) {
        ConversationCache cache = conversationMap.get(conversationId);
        if (cache != null) {
            cache.updateLastAccessTime();
            return cache.getMessages();
        }
        return new ArrayList<>();
    }

    /**
     * 关闭并清理对话
     */
    public void closeConversation(String conversationId) {
        conversationMap.remove(conversationId);
    }

    /**
     * 清理过期的对话（超过10分钟未访问）
     */
    private void cleanExpiredConversations() {
        LocalDateTime now = LocalDateTime.now();
        List<String> expiredIds = conversationMap.entrySet().stream()
                .filter(entry -> ChronoUnit.MINUTES.between(entry.getValue().getLastAccessTime(), now) > 10)
                .map(entry -> entry.getKey())
                .collect(Collectors.toList());

        expiredIds.forEach(conversationMap::remove);

        if (!expiredIds.isEmpty()) {
            System.out.println("清理了 " + expiredIds.size() + " 个过期对话缓存");
        }
    }

    /**
     * 对话缓存内部类
     */
    private static class ConversationCache {
        private final List<ChatRequest.EnterMessage> messages = new java.util.ArrayList<>();
        private LocalDateTime lastAccessTime = LocalDateTime.now();

        public void addMessage(ChatRequest.EnterMessage message) {
            messages.add(message);
        }

        public List<ChatRequest.EnterMessage> getMessages() {
            return Collections.unmodifiableList(new ArrayList<>(messages));
        }

        public void updateLastAccessTime() {
            this.lastAccessTime = LocalDateTime.now();
        }

        public LocalDateTime getLastAccessTime() {
            return lastAccessTime;
        }
    }
}