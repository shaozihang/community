package com.tree.community.model;

public class BookMark {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column book_mark.id
     *
     * @mbg.generated Sun Apr 12 23:42:30 CST 2020
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column book_mark.user_id
     *
     * @mbg.generated Sun Apr 12 23:42:30 CST 2020
     */
    private Long userId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column book_mark.name
     *
     * @mbg.generated Sun Apr 12 23:42:30 CST 2020
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column book_mark.description
     *
     * @mbg.generated Sun Apr 12 23:42:30 CST 2020
     */
    private String description;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column book_mark.is_private
     *
     * @mbg.generated Sun Apr 12 23:42:30 CST 2020
     */
    private Boolean isPrivate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column book_mark.gmt_create
     *
     * @mbg.generated Sun Apr 12 23:42:30 CST 2020
     */
    private Long gmtCreate;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column book_mark.id
     *
     * @return the value of book_mark.id
     *
     * @mbg.generated Sun Apr 12 23:42:30 CST 2020
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column book_mark.id
     *
     * @param id the value for book_mark.id
     *
     * @mbg.generated Sun Apr 12 23:42:30 CST 2020
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column book_mark.user_id
     *
     * @return the value of book_mark.user_id
     *
     * @mbg.generated Sun Apr 12 23:42:30 CST 2020
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column book_mark.user_id
     *
     * @param userId the value for book_mark.user_id
     *
     * @mbg.generated Sun Apr 12 23:42:30 CST 2020
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column book_mark.name
     *
     * @return the value of book_mark.name
     *
     * @mbg.generated Sun Apr 12 23:42:30 CST 2020
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column book_mark.name
     *
     * @param name the value for book_mark.name
     *
     * @mbg.generated Sun Apr 12 23:42:30 CST 2020
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column book_mark.description
     *
     * @return the value of book_mark.description
     *
     * @mbg.generated Sun Apr 12 23:42:30 CST 2020
     */
    public String getDescription() {
        return description;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column book_mark.description
     *
     * @param description the value for book_mark.description
     *
     * @mbg.generated Sun Apr 12 23:42:30 CST 2020
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column book_mark.is_private
     *
     * @return the value of book_mark.is_private
     *
     * @mbg.generated Sun Apr 12 23:42:30 CST 2020
     */
    public Boolean getIsPrivate() {
        return isPrivate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column book_mark.is_private
     *
     * @param isPrivate the value for book_mark.is_private
     *
     * @mbg.generated Sun Apr 12 23:42:30 CST 2020
     */
    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column book_mark.gmt_create
     *
     * @return the value of book_mark.gmt_create
     *
     * @mbg.generated Sun Apr 12 23:42:30 CST 2020
     */
    public Long getGmtCreate() {
        return gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column book_mark.gmt_create
     *
     * @param gmtCreate the value for book_mark.gmt_create
     *
     * @mbg.generated Sun Apr 12 23:42:30 CST 2020
     */
    public void setGmtCreate(Long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}