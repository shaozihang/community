package com.tree.community.mapper;

import com.tree.community.model.Collection;
import com.tree.community.model.CollectionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table collection
     *
     * @mbg.generated Tue Apr 07 16:36:22 CST 2020
     */
    long countByExample(CollectionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table collection
     *
     * @mbg.generated Tue Apr 07 16:36:22 CST 2020
     */
    int deleteByExample(CollectionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table collection
     *
     * @mbg.generated Tue Apr 07 16:36:22 CST 2020
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table collection
     *
     * @mbg.generated Tue Apr 07 16:36:22 CST 2020
     */
    int insert(Collection record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table collection
     *
     * @mbg.generated Tue Apr 07 16:36:22 CST 2020
     */
    int insertSelective(Collection record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table collection
     *
     * @mbg.generated Tue Apr 07 16:36:22 CST 2020
     */
    List<Collection> selectByExampleWithRowbounds(CollectionExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table collection
     *
     * @mbg.generated Tue Apr 07 16:36:22 CST 2020
     */
    List<Collection> selectByExample(CollectionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table collection
     *
     * @mbg.generated Tue Apr 07 16:36:22 CST 2020
     */
    Collection selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table collection
     *
     * @mbg.generated Tue Apr 07 16:36:22 CST 2020
     */
    int updateByExampleSelective(@Param("record") Collection record, @Param("example") CollectionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table collection
     *
     * @mbg.generated Tue Apr 07 16:36:22 CST 2020
     */
    int updateByExample(@Param("record") Collection record, @Param("example") CollectionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table collection
     *
     * @mbg.generated Tue Apr 07 16:36:22 CST 2020
     */
    int updateByPrimaryKeySelective(Collection record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table collection
     *
     * @mbg.generated Tue Apr 07 16:36:22 CST 2020
     */
    int updateByPrimaryKey(Collection record);
}