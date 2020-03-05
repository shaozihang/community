package com.tree.community.service;

import com.tree.community.dto.LikedCountDTO;
import com.tree.community.mapper.CommentMapper;
import com.tree.community.mapper.QuestionMapper;
import com.tree.community.mapper.UserLikeMapper;
import com.tree.community.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserLikeService {

    @Autowired
    private UserLikeMapper userLikeMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private CommentMapper commentMapper;

    /**
     * 通过被点赞的帖子或评论id和点赞人id查询是否存在点赞记录
     */
    @Transactional
    public Integer selectlikeStatus(Long id, Long userId, Integer type) {
        Integer status = redisService.selectlikeStatus(id, userId, type);
        if(status == 2){
            UserLikeExample userLikeExample = new UserLikeExample();
            userLikeExample.createCriteria()
                    .andLikedUserIdEqualTo(id)
                    .andLikedPostIdEqualTo(userId)
                    .andTypeEqualTo(type);
            List<UserLike> userLikes = userLikeMapper.selectByExample(userLikeExample);
            if(userLikes.size() == 0){
                status = 0;
            }else{
                status = userLikes.get(0).getStatus();
            }
        }
        return status;
    }

    /**
     * 将Redis里的点赞数据存入数据库中
     */
    @Transactional
    public void transLikedFromRedisToDB() {
        List<UserLike> list = redisService.getLikedDataFromRedis();
        for (UserLike like : list) {
            UserLikeExample userLikeExample = new UserLikeExample();
            userLikeExample.createCriteria()
                    .andLikedUserIdEqualTo(like.getLikedUserId())
                    .andLikedPostIdEqualTo(like.getLikedPostId())
                    .andTypeEqualTo(like.getType());
            List<UserLike> userLikes = userLikeMapper.selectByExample(userLikeExample);
            if (userLikes.size() == 0){
                //没有记录，直接存入
                like.setGmtCteate(System.currentTimeMillis());
                like.setGmtMotified(System.currentTimeMillis());
                userLikeMapper.insert(like);
            }else{
                //有记录，需要更新
                like.setGmtMotified(System.currentTimeMillis());
                UserLikeExample example = new UserLikeExample();
                example.createCriteria()
                        .andIdEqualTo(userLikes.get(0).getId());
                userLikeMapper.updateByExampleSelective(like, example);
            }
        }
    }

    /**
     * 将Redis中的点赞数量数据存入数据库
     */
    @Transactional
    public void transLikedCountFromRedisToDB() {
        List<LikedCountDTO> list = redisService.getLikedCountFromRedis();
        for (LikedCountDTO dto : list) {
            if(dto.getType() == 1){
                Question question = questionMapper.selectByPrimaryKey(dto.getId());
                if(question != null){
                    question.setLikeCount(question.getLikeCount()+dto.getLikedCount());
                    QuestionExample questionExample = new QuestionExample();
                    questionExample.createCriteria()
                            .andIdEqualTo(dto.getId());
                    questionMapper.updateByExampleSelective(question, questionExample);
                }
            }else if(dto.getType() == 2){
                Comment comment = commentMapper.selectByPrimaryKey(dto.getId());
                if(comment != null){
                    comment.setLikeCount(comment.getLikeCount()+dto.getLikedCount());
                    CommentExample commentExample = new CommentExample();
                    commentExample.createCriteria()
                            .andIdEqualTo(dto.getId());
                    commentMapper.updateByExampleSelective(comment, commentExample);
                }
            }
        }
    }
}
