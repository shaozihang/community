package com.tree.community.schedule;

import com.tree.community.mapper.UserLikeMapper;
import com.tree.community.model.UserLikeExample;
import com.tree.community.service.UserLikeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class LikeTask {

    @Autowired
    private UserLikeService userLikeService;

    @Autowired
    private UserLikeMapper userLikeMapper;

    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void likechedule(){
        userLikeService.transLikedFromRedisToDB();
        userLikeService.transLikedCountFromRedisToDB();
        UserLikeExample userLikeExample = new UserLikeExample();
        userLikeExample.createCriteria()
                .andStatusEqualTo(0);
        userLikeMapper.deleteByExample(userLikeExample);
        log.info("The time is now about like {}",new Date());
    }
}
