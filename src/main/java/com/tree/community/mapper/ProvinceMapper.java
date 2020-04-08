package com.tree.community.mapper;

import com.tree.community.model.Province;
import com.tree.community.model.ProvinceExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvinceMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table province
     *
     * @mbg.generated Tue Apr 07 16:36:22 CST 2020
     */
    long countByExample(ProvinceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table province
     *
     * @mbg.generated Tue Apr 07 16:36:22 CST 2020
     */
    int deleteByExample(ProvinceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table province
     *
     * @mbg.generated Tue Apr 07 16:36:22 CST 2020
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table province
     *
     * @mbg.generated Tue Apr 07 16:36:22 CST 2020
     */
    int insert(Province record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table province
     *
     * @mbg.generated Tue Apr 07 16:36:22 CST 2020
     */
    int insertSelective(Province record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table province
     *
     * @mbg.generated Tue Apr 07 16:36:22 CST 2020
     */
    List<Province> selectByExampleWithRowbounds(ProvinceExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table province
     *
     * @mbg.generated Tue Apr 07 16:36:22 CST 2020
     */
    List<Province> selectByExample(ProvinceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table province
     *
     * @mbg.generated Tue Apr 07 16:36:22 CST 2020
     */
    Province selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table province
     *
     * @mbg.generated Tue Apr 07 16:36:22 CST 2020
     */
    int updateByExampleSelective(@Param("record") Province record, @Param("example") ProvinceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table province
     *
     * @mbg.generated Tue Apr 07 16:36:22 CST 2020
     */
    int updateByExample(@Param("record") Province record, @Param("example") ProvinceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table province
     *
     * @mbg.generated Tue Apr 07 16:36:22 CST 2020
     */
    int updateByPrimaryKeySelective(Province record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table province
     *
     * @mbg.generated Tue Apr 07 16:36:22 CST 2020
     */
    int updateByPrimaryKey(Province record);
}