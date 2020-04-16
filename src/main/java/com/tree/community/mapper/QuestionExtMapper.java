package com.tree.community.mapper;

import com.tree.community.dto.QuestionQueryDTO;
import com.tree.community.model.Question;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionExtMapper {
    int incView(Question record);
    int incCommentCount(Question record);
    List<Question> selectRelated(Question question);

    Integer countBySearch(QuestionQueryDTO questionQueryDTO);

    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);

    List<Question> getQuestionByIds(List<Long> questionIds);

    void incLikeCount(Question qu);

    void decCommentCount(Question question);

    List<Question> getQuestion(@Param(value = "start") Integer start, @Param(value = "limit")Integer limit);

    int getQuestionCount();

    void questionEdit(Question question);

    List<Question> questionTop();
}
