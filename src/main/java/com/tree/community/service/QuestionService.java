package com.tree.community.service;

import com.tree.community.dto.PaginationDTO;
import com.tree.community.dto.QuestionDTO;
import com.tree.community.dto.QuestionQueryDTO;
import com.tree.community.enums.QuestionTypeEnum;
import com.tree.community.exception.CustomizeErrorCode;
import com.tree.community.exception.CustomizeException;
import com.tree.community.mapper.*;
import com.tree.community.model.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CollectionExtMapper collectionExtMapper;

    public PaginationDTO list(String search, String tag, Integer page, Integer size,String type,String sort) {
        if(StringUtils.isNotBlank(search)){
            search = StringUtils.replace(search, " ", "|");
        }

        PaginationDTO paginationDTO = new PaginationDTO();

        Integer totalPage;

        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);
        questionQueryDTO.setTag(tag);
        questionQueryDTO.setType(type);
        if(StringUtils.isNotBlank(sort)){
            if(sort.equals("hot7")){
                questionQueryDTO.setSort(7);
            }else if(sort.equals("hot30")){
                questionQueryDTO.setSort(32);
            }else if(sort.equals("no")){
                questionQueryDTO.setSort(0);
            }
        }
        Integer totalCount = questionExtMapper.countBySearch(questionQueryDTO);

        if(totalCount % size ==0){
            totalPage = totalCount / size;
        }else{
            totalPage = totalCount / size + 1;
        }

        if(page < 1){
            page = 1;
        }
        if(page>totalPage){
            page = totalPage;
        }

        paginationDTO.setPagination(totalPage,page);

        //分页
        Integer offset = page < 1 ? 0 : size*(page-1);
        questionQueryDTO.setPage(offset);
        questionQueryDTO.setSize(size);
        List<Question> questions = null;
        if(StringUtils.isNotBlank(sort)){
            if(sort.equals("hot7")){
                questions = questionExtMapper.selectBySearch(questionQueryDTO);
            }else if(sort.equals("hot30")){
                questions = questionExtMapper.selectBySearch(questionQueryDTO);
            }else if(sort.equals("no")){
                questions = questionExtMapper.selectBySearch(questionQueryDTO);
            }else if(sort.equals("new")){
                questions = questionExtMapper.selectBySearch(questionQueryDTO);
            }
        }else{
            questions = questionExtMapper.selectBySearch(questionQueryDTO);
        }
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTO.setTypeName(QuestionTypeEnum.nameOfType(question.getType()));
            Integer collectionCount = collectionExtMapper.getCollectionCount(question.getId());
            questionDTO.setCollectionCount(collectionCount);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setData(questionDTOList);
        return paginationDTO;
    }

    public PaginationDTO list(Long userId, Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();

        Integer totalPage;

        QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andCreatorEqualTo(userId);
        Integer totalCount = (int) questionMapper.countByExample(example);
        paginationDTO.setQuCount(totalCount);

        if(totalCount % size ==0){
            totalPage = totalCount / size;
        }else{
            totalPage = totalCount / size + 1;
        }

        if(page < 1){
            page = 1;
        }
        if(page>totalPage){
            page = totalPage;
        }

        paginationDTO.setPagination(totalPage,page);

        //分页
        Integer offset = page < 1 ? 0 : size*(page-1);
        QuestionExample example1 = new QuestionExample();
        example1.createCriteria()
                .andCreatorEqualTo(userId);
        example1.setOrderByClause("gmt_create desc");
        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(example1,new RowBounds(offset,size));

        if(questions.size() == 0){
            return paginationDTO;
        }

        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTO.setTypeName(QuestionTypeEnum.nameOfType(question.getType()));
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setData(questionDTOList);
        return paginationDTO;
    }

    public QuestionDTO getById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if(question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setTypeName(QuestionTypeEnum.nameOfType(question.getType()));
        questionDTO.setUser(user);
        Integer collectionCount = collectionExtMapper.getCollectionCount(id);
        questionDTO.setCollectionCount(collectionCount);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if(question.getId() == null){
            //创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(System.currentTimeMillis());
            questionMapper.insertSelective(question);
        }else{
            //更新
            question.setGmtModified(System.currentTimeMillis());
            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andIdEqualTo(question.getId());
            int updated = questionMapper.updateByExampleSelective(question, example);
            if(updated != 1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void incView(Long id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);
    }

    public List<Question> selectRelated(QuestionDTO questionDTO) {
        if(StringUtils.isBlank(questionDTO.getTag())){
            return new ArrayList<>();
        }
        String replace = StringUtils.replace(questionDTO.getTag(), ",", "|");
        Question question = new Question();
        question.setId(questionDTO.getId());
        question.setTag(replace);

        List<Question> questions = questionExtMapper.selectRelated(question);
        return questions;
    }

    public int getQuCountById(Long id) {
        QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andCreatorEqualTo(id);
        int quCount = (int) questionMapper.countByExample(example);
        return quCount;
    }

    public List<Question> getQuestionByIds(List<Long> questionIds) {
        List<Question> questions = questionExtMapper.getQuestionByIds(questionIds);
        return questions;
    }
}
