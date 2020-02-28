package com.tree.community.schedule;

import com.tree.community.cache.HotTagCache;
import com.tree.community.mapper.QuestionMapper;
import com.tree.community.model.Question;
import com.tree.community.model.QuestionExample;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class HotTagTasks {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private HotTagCache hotTagCache;

    @Scheduled(fixedRate = 1000 * 60 * 60 * 4)
    public void hotTagSchedule(){
        int offset = 0;
        int limit = 10;
        List<Question> list = new ArrayList<>();

        Map<String, Integer> priorities = new HashMap<>();
        while (offset == 0 || list.size() == limit){
            list = questionMapper.selectByExampleWithRowbounds(new QuestionExample(), new RowBounds(offset,limit));
            for (Question question : list) {

                String[] tags = StringUtils.split(question.getTag(), ",");
                for (String tag : tags) {
                    Integer priority = priorities.get(tag);
                    if(priority != null){
                        priorities.put(tag,priority + 5 + question.getCommentCount());
                    }else{
                        priorities.put(tag,5 + question.getCommentCount());
                    }
                }
            }
            offset += limit;
        }
        hotTagCache.updateTags(priorities);
        log.info("The time is now {}",new Date());
    }
}
