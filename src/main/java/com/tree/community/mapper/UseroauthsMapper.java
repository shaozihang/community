package com.tree.community.mapper;

import com.tree.community.model.Useroauths;
import com.tree.community.model.UseroauthsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

@Repository
public interface UseroauthsMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table useroauths
     *
     * @mbg.generated Tue Mar 03 20:59:56 CST 2020
     */
    long countByExample(UseroauthsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table useroauths
     *
     * @mbg.generated Tue Mar 03 20:59:56 CST 2020
     */
    int deleteByExample(UseroauthsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table useroauths
     *
     * @mbg.generated Tue Mar 03 20:59:56 CST 2020
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table useroauths
     *
     * @mbg.generated Tue Mar 03 20:59:56 CST 2020
     */
    int insert(Useroauths record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table useroauths
     *
     * @mbg.generated Tue Mar 03 20:59:56 CST 2020
     */
    int insertSelective(Useroauths record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table useroauths
     *
     * @mbg.generated Tue Mar 03 20:59:56 CST 2020
     */
    List<Useroauths> selectByExampleWithRowbounds(UseroauthsExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table useroauths
     *
     * @mbg.generated Tue Mar 03 20:59:56 CST 2020
     */
    List<Useroauths> selectByExample(UseroauthsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table useroauths
     *
     * @mbg.generated Tue Mar 03 20:59:56 CST 2020
     */
    Useroauths selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table useroauths
     *
     * @mbg.generated Tue Mar 03 20:59:56 CST 2020
     */
    int updateByExampleSelective(@Param("record") Useroauths record, @Param("example") UseroauthsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table useroauths
     *
     * @mbg.generated Tue Mar 03 20:59:56 CST 2020
     */
    int updateByExample(@Param("record") Useroauths record, @Param("example") UseroauthsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table useroauths
     *
     * @mbg.generated Tue Mar 03 20:59:56 CST 2020
     */
    int updateByPrimaryKeySelective(Useroauths record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table useroauths
     *
     * @mbg.generated Tue Mar 03 20:59:56 CST 2020
     */
    int updateByPrimaryKey(Useroauths record);
}