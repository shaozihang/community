package com.tree.community.service;

import com.tree.community.dto.BookMarkDTO;
import com.tree.community.mapper.BookMarkMapper;
import com.tree.community.mapper.CollectionMapper;
import com.tree.community.model.BookMark;
import com.tree.community.model.BookMarkExample;
import com.tree.community.model.Collection;
import com.tree.community.model.CollectionExample;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookMarkService {

    @Autowired
    private BookMarkMapper bookMarkMapper;

    @Autowired
    private CollectionMapper collectionMapper;

    public List<BookMarkDTO> getBookMark(Long id) {//通过用户id获取用户的所有收藏夹
        BookMarkExample example = new BookMarkExample();
        example.createCriteria()
                .andUserIdEqualTo(id);
        List<BookMark> bookMarks = bookMarkMapper.selectByExample(example);
        List<BookMarkDTO> bookMarkDTOList = new ArrayList<>();
        if(bookMarks.size() == 0){
            return bookMarkDTOList;
        }
        for (BookMark bookMark : bookMarks) {
            BookMarkDTO bookMarkDTO = new BookMarkDTO();
            BeanUtils.copyProperties(bookMark,bookMarkDTO);
            bookMarkDTO.setCollected(false);
            bookMarkDTOList.add(bookMarkDTO);
        }
        return bookMarkDTOList;
    }

    public List<BookMarkDTO> getBookMarkTwo(Long questionId, Long id) {//通过问题和用户id获取这个问题收藏到哪些收藏夹
        BookMarkExample example = new BookMarkExample();
        example.createCriteria()
                .andUserIdEqualTo(id);
        List<BookMark> bookMarks = bookMarkMapper.selectByExample(example);
        CollectionExample example1 = new CollectionExample();
        example1.createCriteria()
                .andUserIdEqualTo(id)
                .andQuestionIdEqualTo(questionId);
        List<Collection> collections = collectionMapper.selectByExample(example1);
        List<BookMarkDTO> bookMarkDTOList = new ArrayList<>();
        for (BookMark bookMark : bookMarks) {
            BookMarkDTO bookMarkDTO = new BookMarkDTO();
            BeanUtils.copyProperties(bookMark,bookMarkDTO);
            bookMarkDTO.setCollected(false);
            for (Collection collection : collections) {
                if(collection.getFolderId() == bookMark.getId()){
                    bookMarkDTO.setCollected(true);
                    break;
                }
            }
            bookMarkDTOList.add(bookMarkDTO);
        }
        return bookMarkDTOList;
    }

    @Transactional
    public int addBookMark(Long id, BookMark bookMark) {//通过用户id和收藏夹名称查询用户有无同名收藏夹，没有则新增收藏夹
        BookMarkExample example = new BookMarkExample();
        example.createCriteria()
                .andUserIdEqualTo(id)
                .andNameEqualTo(bookMark.getName());
        List<BookMark> bookMarks = bookMarkMapper.selectByExample(example);
        if(bookMarks.size() != 0){
            return -1;
        }
        bookMark.setUserId(id);
        bookMark.setGmtCreate(System.currentTimeMillis());
        bookMarkMapper.insert(bookMark);
        return 1;
    }

    public Long getBookMarkId(Long id, String name) {//通过用户id和收藏夹名称获取收藏夹id
        BookMarkExample example = new BookMarkExample();
        example.createCriteria()
                .andUserIdEqualTo(id)
                .andNameEqualTo(name);
        List<BookMark> bookMarks = bookMarkMapper.selectByExample(example);
        return bookMarks.get(0).getId();
    }

    public List<BookMarkDTO> getUserBookMark(Long id) {//通过用户id获取所有收藏夹和收藏数量
        BookMarkExample example = new BookMarkExample();
        example.createCriteria()
                .andUserIdEqualTo(id);
        example.setOrderByClause("gmt_create desc");
        List<BookMark> bookMarks = bookMarkMapper.selectByExample(example);
        List<BookMarkDTO> bookMarkDTOList = new ArrayList<>();
        for (BookMark bookMark : bookMarks) {
            BookMarkDTO bookMarkDTO = new BookMarkDTO();
            BeanUtils.copyProperties(bookMark,bookMarkDTO);
            CollectionExample example1 = new CollectionExample();
            example1.createCriteria()
                    .andFolderIdEqualTo(bookMark.getId());
            List<Collection> collectionList = collectionMapper.selectByExample(example1);
            bookMarkDTO.setCollectionCount(collectionList.size());
            bookMarkDTOList.add(bookMarkDTO);
        }
        return bookMarkDTOList;
    }

    public BookMarkDTO getBookMarkById(Long id) {
        BookMark bookMark = bookMarkMapper.selectByPrimaryKey(id);
        CollectionExample example = new CollectionExample();
        example.createCriteria()
                .andFolderIdEqualTo(id);
        List<Collection> collectionList = collectionMapper.selectByExample(example);
        BookMarkDTO bookMarkDTO = new BookMarkDTO();
        BeanUtils.copyProperties(bookMark,bookMarkDTO);
        bookMarkDTO.setCollectionCount(collectionList.size());
        return bookMarkDTO;
    }

    @Transactional
    public int updateBookMark(Long id, BookMark bookMark) {
        BookMarkExample example = new BookMarkExample();
        example.createCriteria()
                .andUserIdEqualTo(id)
                .andNameEqualTo(bookMark.getName());
        List<BookMark> bookMarks = bookMarkMapper.selectByExample(example);
        if(bookMarks.size() != 0){
            if(bookMarks.get(0).getId() != bookMark.getId()){
                return -1;
            }
        }
        bookMarkMapper.updateByPrimaryKeySelective(bookMark);
        return 1;
    }

    @Transactional
    public void deleteBookMark(Long id) {
        CollectionExample example = new CollectionExample();
        example.createCriteria()
                .andFolderIdEqualTo(id);
        collectionMapper.deleteByExample(example);
        bookMarkMapper.deleteByPrimaryKey(id);
    }

    public List<BookMarkDTO> getPublicBookMark(Long id) {//通过用户id获取用户公开的收藏夹和收藏数量
        BookMarkExample example = new BookMarkExample();
        example.createCriteria()
                .andUserIdEqualTo(id)
                .andIsPrivateEqualTo(false);
        example.setOrderByClause("gmt_create desc");
        List<BookMark> bookMarks = bookMarkMapper.selectByExample(example);
        List<BookMarkDTO> bookMarkDTOList = new ArrayList<>();
        for (BookMark bookMark : bookMarks) {
            BookMarkDTO bookMarkDTO = new BookMarkDTO();
            BeanUtils.copyProperties(bookMark,bookMarkDTO);
            CollectionExample example1 = new CollectionExample();
            example1.createCriteria()
                    .andFolderIdEqualTo(bookMark.getId());
            List<Collection> collectionList = collectionMapper.selectByExample(example1);
            bookMarkDTO.setCollectionCount(collectionList.size());
            bookMarkDTOList.add(bookMarkDTO);
        }
        return bookMarkDTOList;
    }
}
