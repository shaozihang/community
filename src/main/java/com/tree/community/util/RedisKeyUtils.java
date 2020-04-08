package com.tree.community.util;

public class RedisKeyUtils {

    //保存用户点赞数据的key
    public static final String MAP_KEY_USER_LIKED = "MAP_USER_LIKED";

    //保存用户帖子被点赞数量的key
    public static final String MAP_KEY_USER_LIKED_COUNT = "MAP_USER_LIKED_COUNT";

    /**
     * 拼接被点赞的用户id和点赞的人的id作为key。格式 222222::333333
     * @param likedPostId 点赞所在帖子的id
     * @param likedUserId 被点赞的人id
     * @param likedPostId 点赞的人的id
     * @return
     */
    public static String getLikedKey(String questionId,String likedUserId, String likedPostId,Integer type){
        StringBuilder builder = new StringBuilder();
        builder.append(questionId);
        builder.append("::");
        builder.append(likedUserId);
        builder.append("::");
        builder.append(likedPostId);
        builder.append("::");
        builder.append(type);
        return builder.toString();
    }

    public static String getLikedKeyCount(String likedUserId,Integer type){
        StringBuilder builder = new StringBuilder();
        builder.append(likedUserId);
        builder.append("::");
        builder.append(type);
        return builder.toString();
    }
}

