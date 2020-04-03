package com.tree.community.mapper;

import com.tree.community.model.Collection;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollectionExtMapper {

    Integer getCollectionCount(Long id);

    void addCollection(List<Collection> collectionList);

    void deleteCollection(@Param("userId") Long id, @Param("questionId") Long questionId, @Param("folderIds") List<Long> deleteFolderId);
}
