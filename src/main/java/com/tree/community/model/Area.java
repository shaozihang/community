package com.tree.community.model;

public class Area {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column area.id
     *
     * @mbg.generated Sun Apr 12 23:42:30 CST 2020
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column area.area_code
     *
     * @mbg.generated Sun Apr 12 23:42:30 CST 2020
     */
    private String areaCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column area.city_code
     *
     * @mbg.generated Sun Apr 12 23:42:30 CST 2020
     */
    private String cityCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column area.name
     *
     * @mbg.generated Sun Apr 12 23:42:30 CST 2020
     */
    private String name;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column area.id
     *
     * @return the value of area.id
     *
     * @mbg.generated Sun Apr 12 23:42:30 CST 2020
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column area.id
     *
     * @param id the value for area.id
     *
     * @mbg.generated Sun Apr 12 23:42:30 CST 2020
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column area.area_code
     *
     * @return the value of area.area_code
     *
     * @mbg.generated Sun Apr 12 23:42:30 CST 2020
     */
    public String getAreaCode() {
        return areaCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column area.area_code
     *
     * @param areaCode the value for area.area_code
     *
     * @mbg.generated Sun Apr 12 23:42:30 CST 2020
     */
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode == null ? null : areaCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column area.city_code
     *
     * @return the value of area.city_code
     *
     * @mbg.generated Sun Apr 12 23:42:30 CST 2020
     */
    public String getCityCode() {
        return cityCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column area.city_code
     *
     * @param cityCode the value for area.city_code
     *
     * @mbg.generated Sun Apr 12 23:42:30 CST 2020
     */
    public void setCityCode(String cityCode) {
        this.cityCode = cityCode == null ? null : cityCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column area.name
     *
     * @return the value of area.name
     *
     * @mbg.generated Sun Apr 12 23:42:30 CST 2020
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column area.name
     *
     * @param name the value for area.name
     *
     * @mbg.generated Sun Apr 12 23:42:30 CST 2020
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
}