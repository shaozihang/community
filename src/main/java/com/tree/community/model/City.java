package com.tree.community.model;

public class City {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column city.id
     *
     * @mbg.generated Sun Mar 29 23:25:11 CST 2020
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column city.city_code
     *
     * @mbg.generated Sun Mar 29 23:25:11 CST 2020
     */
    private String cityCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column city.name
     *
     * @mbg.generated Sun Mar 29 23:25:11 CST 2020
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column city.province_code
     *
     * @mbg.generated Sun Mar 29 23:25:11 CST 2020
     */
    private String provinceCode;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column city.id
     *
     * @return the value of city.id
     *
     * @mbg.generated Sun Mar 29 23:25:11 CST 2020
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column city.id
     *
     * @param id the value for city.id
     *
     * @mbg.generated Sun Mar 29 23:25:11 CST 2020
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column city.city_code
     *
     * @return the value of city.city_code
     *
     * @mbg.generated Sun Mar 29 23:25:11 CST 2020
     */
    public String getCityCode() {
        return cityCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column city.city_code
     *
     * @param cityCode the value for city.city_code
     *
     * @mbg.generated Sun Mar 29 23:25:11 CST 2020
     */
    public void setCityCode(String cityCode) {
        this.cityCode = cityCode == null ? null : cityCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column city.name
     *
     * @return the value of city.name
     *
     * @mbg.generated Sun Mar 29 23:25:11 CST 2020
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column city.name
     *
     * @param name the value for city.name
     *
     * @mbg.generated Sun Mar 29 23:25:11 CST 2020
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column city.province_code
     *
     * @return the value of city.province_code
     *
     * @mbg.generated Sun Mar 29 23:25:11 CST 2020
     */
    public String getProvinceCode() {
        return provinceCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column city.province_code
     *
     * @param provinceCode the value for city.province_code
     *
     * @mbg.generated Sun Mar 29 23:25:11 CST 2020
     */
    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode == null ? null : provinceCode.trim();
    }
}