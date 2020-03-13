package com.tree.community.service;

import com.tree.community.dto.LikedCountDTO;
import com.tree.community.enums.LikedStatusEnum;
import com.tree.community.model.UserLike;
import com.tree.community.util.RedisKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 点赞。状态为1
     * @param likedUserId
     * @param likedPostId
     */
    public void saveLikedRedis(String likedUserId, String likedPostId,Integer type) {
        String key = RedisKeyUtils.getLikedKey(likedUserId, likedPostId,type);
        redisTemplate.opsForHash().put(RedisKeyUtils.MAP_KEY_USER_LIKED, key, LikedStatusEnum.LIKE.getCode());
    }

    /**
     * 取消点赞。将状态改变为0
     * @param likedUserId
     * @param likedPostId
     */
    public void unlikeFromRedis(String likedUserId, String likedPostId,Integer type) {
        String key = RedisKeyUtils.getLikedKey(likedUserId, likedPostId,type);
        redisTemplate.opsForHash().put(RedisKeyUtils.MAP_KEY_USER_LIKED, key, LikedStatusEnum.UNLIKE.getCode());
    }

    /**
     * 该用户的点赞数加1
     * @param likedUserId
     */
    public void incrementLikedCount(String likedUserId,Integer type) {
        String key = RedisKeyUtils.getLikedKeyCount(likedUserId,type);
        redisTemplate.opsForHash().increment(RedisKeyUtils.MAP_KEY_USER_LIKED_COUNT, key, 1);
    }

    /**
     * 该用户的点赞数减1
     * @param likedUserId
     */
    public void decrementLikedCount(String likedUserId,Integer type) {
        String key = RedisKeyUtils.getLikedKeyCount(likedUserId,type);
        redisTemplate.opsForHash().increment(RedisKeyUtils.MAP_KEY_USER_LIKED_COUNT, key, -1);
    }

    /**
     * 获取Redis中存储的所有点赞数据
     * @return
     */
    public List<UserLike> getLikedDataFromRedis() {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisKeyUtils.MAP_KEY_USER_LIKED, ScanOptions.NONE);
        List<UserLike> list = new ArrayList<>();
        while (cursor.hasNext()){
            Map.Entry<Object, Object> entry = cursor.next();
            String key = (String) entry.getKey();
            //分离出 likedUserId，likedPostId
            String[] split = key.split("::");
            String likedUserId = split[0];
            String likedPostId = split[1];
            Integer type = Integer.valueOf(split[2]);
            Integer value = (Integer) entry.getValue();

            //组装成 UserLike 对象
            UserLike userLike = new UserLike(Long.valueOf(likedUserId), Long.valueOf(likedPostId),type, value);
            list.add(userLike);

            //存到 list 后从 Redis 中删除
            redisTemplate.opsForHash().delete(RedisKeyUtils.MAP_KEY_USER_LIKED, key);
        }
        return list;
    }

    /**
     * 获取Redis中存储的所有点赞数量
     * @return
     */
    public List<LikedCountDTO> getLikedCountFromRedis() {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisKeyUtils.MAP_KEY_USER_LIKED_COUNT, ScanOptions.NONE);
        List<LikedCountDTO> list = new ArrayList<>();
        while (cursor.hasNext()){
            Map.Entry<Object, Object> map = cursor.next();
            String key = (String)map.getKey();
            //分离出 likedUserId，likedPostId
            String[] split = key.split("::");
            String id = split[0];
            String type = split[1];
            //将点赞数量存储在 LikedCountDT
            Integer value = (Integer) map.getValue();
            LikedCountDTO dto = new LikedCountDTO(Long.valueOf(id),Integer.valueOf(type), value);
            list.add(dto);
            //从Redis中删除这条记录
            redisTemplate.opsForHash().delete(RedisKeyUtils.MAP_KEY_USER_LIKED_COUNT, key);
        }
        return list;
    }


    /**
     * 根据被点赞的id和当前用户id查询点赞状态
     * @return
     */
    public Integer selectlikeStatus(Long likedUserId, Long likedPostId,Integer type) {
        String key = RedisKeyUtils.getLikedKey(String.valueOf(likedUserId.longValue()), String.valueOf(likedPostId.longValue()),type);
        Integer status = (Integer) redisTemplate.opsForHash().get(RedisKeyUtils.MAP_KEY_USER_LIKED, key);
        if(status == null){
            status = 2;
        }
        return status;
    }

    /**
     * 根据被点赞的id查询点赞数量
     * @return
     */
    public Integer selectlikeCount(Long likedUserId,Integer type){
        String key = RedisKeyUtils.getLikedKeyCount(String.valueOf(likedUserId),type);
        Integer likeCount = (Integer) redisTemplate.opsForHash().get(RedisKeyUtils.MAP_KEY_USER_LIKED_COUNT, key);
        return likeCount;
    }

    /**
     * 添加关注。状态为1
     */
    public void saveFollowRedis(String meId, String otherId) {
        redisTemplate.opsForZSet().add(meId+":follow",otherId,System.currentTimeMillis());
        redisTemplate.opsForZSet().add(otherId+":fans",meId,System.currentTimeMillis());
    }

    /**
     * 取消关注。状态为0
     */
    public void cancelFollowRedis(String meId, String otherId) {
        redisTemplate.opsForZSet().remove(meId+":follow",otherId);
        redisTemplate.opsForZSet().remove(otherId+":fans",meId);
    }

    /**
     * 我的关注数
     */
    public void FollowCountRedis(String meId) {
        redisTemplate.opsForZSet().size(meId+":follow");
    }

    /**
     * 我的粉丝数
     */
    public void FansCountRedis(String meId) {
        redisTemplate.opsForZSet().size(meId+":fans");
    }
}
