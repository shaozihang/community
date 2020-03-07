package com.tree.community.mapper;

import com.tree.community.model.UserLike;
import com.tree.community.model.UserLikeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLikeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_like
     *
     * @mbg.generated Fri Mar 06 16:29:26 CST 2020
     */
    long countByExample(UserLikeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_like
     *
     * @mbg.generated Fri Mar 06 16:29:26 CST 2020
     */
    int deleteByExample(UserLikeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_like
     *
     * @mbg.generated Fri Mar 06 16:29:26 CST 2020
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_like
     *
     * @mbg.generated Fri Mar 06 16:29:26 CST 2020
     */
    int insert(UserLike record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_like
     *
     * @mbg.generated Fri Mar 06 16:29:26 CST 2020
     */
    int insertSelective(UserLike record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_like
     *
     * @mbg.generated Fri Mar 06 16:29:26 CST 2020
     */
    List<UserLike> selectByExampleWithRowbounds(UserLikeExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_like
     *
     * @mbg.generated Fri Mar 06 16:29:26 CST 2020
     */
    List<UserLike> selectByExample(UserLikeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_like
     *
     * @mbg.generated Fri Mar 06 16:29:26 CST 2020
     */
    UserLike selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_like
     *
     * @mbg.generated Fri Mar 06 16:29:26 CST 2020
     */
    int updateByExampleSelective(@Param("record") UserLike record, @Param("example") UserLikeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_like
     *
     * @mbg.generated Fri Mar 06 16:29:26 CST 2020
     */
    int updateByExample(@Param("record") UserLike record, @Param("example") UserLikeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_like
     *
     * @mbg.generated Fri Mar 06 16:29:26 CST 2020
     */
    int updateByPrimaryKeySelective(UserLike record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_like
     *
     * @mbg.generated Fri Mar 06 16:29:26 CST 2020
     */
    int updateByPrimaryKey(UserLike record);
}