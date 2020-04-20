package com.tree.community.service;

import com.tree.community.mapper.CollectionExtMapper;
import com.tree.community.mapper.CollectionMapper;
import com.tree.community.model.Collection;
import com.tree.community.model.CollectionExample;
import com.tree.community.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CollectionService {

    @Autowired
    private CollectionMapper collectionMapper;

    @Autowired
    private CollectionExtMapper collectionExtMapper;

    @Autowired
    private QuestionService questionService;

    public int getCollectionStatus(Long id, Long userId) {
        CollectionExample example = new CollectionExample();
        example.createCriteria()
                .andQuestionIdEqualTo(id)
                .andUserIdEqualTo(userId);
        List<Collection> collections = collectionMapper.selectByExample(example);
        int status;
        if(collections.size() == 0){
            status = 0;
        }else{
            status = 1;
        }
        return status;
    }

    @Transactional
    public void addCollection(Long userId, Map<String, String> map) {
        String[] folderIds = map.get("folderId").split("-");
        List<Collection> collectionList = new ArrayList<>();
        for (String folderId : folderIds) {
            Collection collection = new Collection();
            collection.setUserId(userId);
            collection.setQuestionId(Long.valueOf(map.get("questionId")));
            collection.setFolderId(Long.valueOf(folderId));
            collection.setGmtCreate(System.currentTimeMillis());
            collectionList.add(collection);
        }
        collectionExtMapper.addCollection(collectionList);
    }

    @Transactional
    public void updateCollection(Long id, Map<String, String> map) {
        String[] folderIds = map.get("folderId").split("-");
        CollectionExample example = new CollectionExample();
        example.createCriteria()
                .andUserIdEqualTo(id)
                .andQuestionIdEqualTo(Long.valueOf(map.get("questionId")));
        List<Collection> collectionList = collectionMapper.selectByExample(example);
        List<Long> addFolderId = addFolderId(collectionList, folderIds);
        List<Long> deleteFolderId = deleteFolderId(collectionList, folderIds);
        List<Collection> addList = new ArrayList<>();
        if(addFolderId.size() != 0){
            for (Long folderId : addFolderId) {
                Collection collection = new Collection();
                collection.setUserId(id);
                collection.setQuestionId(Long.valueOf(map.get("questionId")));
                collection.setFolderId(Long.valueOf(folderId));
                collection.setGmtCreate(System.currentTimeMillis());
                addList.add(collection);
            }
            collectionExtMapper.addCollection(addList);
        }
        if(deleteFolderId.size() != 0){
            collectionExtMapper.deleteCollection(id,Long.valueOf(map.get("questionId")),deleteFolderId);
        }

    }

    public List<Long> addFolderId(List<Collection> str1, String str2[]) {//str1为原本的，str2为修改后的
        List<Long> addFolderId = new ArrayList<>();
        for(int j=0;j<str2.length;j++){
            boolean flag = true;
            for(int i=0;i<str1.size();i++){
                if(Long.valueOf(str2[j]) == str1.get(i).getFolderId()){
                    flag = false;
                    break;
                }
            }
            if(flag){
                addFolderId.add(Long.valueOf(str2[j]));
            }
        }
        return addFolderId;
    }

    public List<Long> deleteFolderId(List<Collection> str1, String str2[]) {
        List<Long> deleteFolderId = new ArrayList<>();
        for(int j=0;j<str1.size();j++){
            boolean flag = true;
            for(int i=0;i<str2.length;i++){
                if(str1.get(j).getFolderId() == Long.valueOf(str2[i])){
                    flag = false;
                    break;
                }
            }
            if(flag){
                deleteFolderId.add(str1.get(j).getFolderId());
            }
        }
        return deleteFolderId;
    }

    @Transactional
    public void deleteCollection(Long id, String questionId) {
        CollectionExample example = new CollectionExample();
        example.createCriteria()
                .andUserIdEqualTo(id)
                .andQuestionIdEqualTo(Long.valueOf(questionId));
        collectionMapper.deleteByExample(example);
    }

    public List<Question> getCollectionByFolderId(Long id) {//通过收藏夹id获取对应的收藏
        CollectionExample example = new CollectionExample();
        example.createCriteria()
                .andFolderIdEqualTo(id);
        List<Collection> collectionList = collectionMapper.selectByExample(example);
        List<Question> questions = null;
        if(collectionList.size() == 0){
            return questions;
        }
        List<Long> questionIds = new ArrayList<>();
        for (Collection collection : collectionList) {
            questionIds.add(collection.getQuestionId());
        }
        questions = questionService.getQuestionByIds(questionIds);
        return questions;
    }

    @Transactional
    public void delCollection(Map<String, String> map) {
        CollectionExample example = new CollectionExample();
        example.createCriteria()
                .andQuestionIdEqualTo(Long.valueOf(map.get("questionId")))
                .andFolderIdEqualTo(Long.valueOf(map.get("id")));
        collectionMapper.deleteByExample(example);
    }
}
