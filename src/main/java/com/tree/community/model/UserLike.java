package com.tree.community.model;

public class UserLike {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_like.id
     *
     * @mbg.generated Tue Mar 03 20:59:56 CST 2020
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_like.liked_user_id
     *
     * @mbg.generated Tue Mar 03 20:59:56 CST 2020
     */
    private Long likedUserId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_like.liked_post_id
     *
     * @mbg.generated Tue Mar 03 20:59:56 CST 2020
     */
    private Long likedPostId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_like.type
     *
     * @mbg.generated Tue Mar 03 20:59:56 CST 2020
     */
    private Integer type;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_like.status
     *
     * @mbg.generated Tue Mar 03 20:59:56 CST 2020
     */
    private Integer status;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_like.gmt_cteate
     *
     * @mbg.generated Tue Mar 03 20:59:56 CST 2020
     */
    private Long gmtCteate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_like.gmt_motified
     *
     * @mbg.generated Tue Mar 03 20:59:56 CST 2020
     */
    private Long gmtMotified;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_like.id
     *
     * @return the value of user_like.id
     *
     * @mbg.generated Tue Mar 03 20:59:56 CST 2020
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_like.id
     *
     * @param id the value for user_like.id
     *
     * @mbg.generated Tue Mar 03 20:59:56 CST 2020
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_like.liked_user_id
     *
     * @return the value of user_like.liked_user_id
     *
     * @mbg.generated Tue Mar 03 20:59:56 CST 2020
     */
    public Long getLikedUserId() {
        return likedUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_like.liked_user_id
     *
     * @param likedUserId the value for user_like.liked_user_id
     *
     * @mbg.generated Tue Mar 03 20:59:56 CST 2020
     */
    public void setLikedUserId(Long likedUserId) {
        this.likedUserId = likedUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_like.liked_post_id
     *
     * @return the value of user_like.liked_post_id
     *
     * @mbg.generated Tue Mar 03 20:59:56 CST 2020
     */
    public Long getLikedPostId() {
        return likedPostId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_like.liked_post_id
     *
     * @param likedPostId the value for user_like.liked_post_id
     *
     * @mbg.generated Tue Mar 03 20:59:56 CST 2020
     */
    public void setLikedPostId(Long likedPostId) {
        this.likedPostId = likedPostId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_like.type
     *
     * @return the value of user_like.type
     *
     * @mbg.generated Tue Mar 03 20:59:56 CST 2020
     */
    public Integer getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_like.type
     *
     * @param type the value for user_like.type
     *
     * @mbg.generated Tue Mar 03 20:59:56 CST 2020
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_like.status
     *
     * @return the value of user_like.status
     *
     * @mbg.generated Tue Mar 03 20:59:56 CST 2020
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_like.status
     *
     * @param status the value for user_like.status
     *
     * @mbg.generated Tue Mar 03 20:59:56 CST 2020
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_like.gmt_cteate
     *
     * @return the value of user_like.gmt_cteate
     *
     * @mbg.generated Tue Mar 03 20:59:56 CST 2020
     */
    public Long getGmtCteate() {
        return gmtCteate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_like.gmt_cteate
     *
     * @param gmtCteate the value for user_like.gmt_cteate
     *
     * @mbg.generated Tue Mar 03 20:59:56 CST 2020
     */
    public void setGmtCteate(Long gmtCteate) {
        this.gmtCteate = gmtCteate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_like.gmt_motified
     *
     * @return the value of user_like.gmt_motified
     *
     * @mbg.generated Tue Mar 03 20:59:56 CST 2020
     */
    public Long getGmtMotified() {
        return gmtMotified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_like.gmt_motified
     *
     * @param gmtMotified the value for user_like.gmt_motified
     *
     * @mbg.generated Tue Mar 03 20:59:56 CST 2020
     */
    public void setGmtMotified(Long gmtMotified) {
        this.gmtMotified = gmtMotified;
    }

    public UserLike(){}

    public UserLike(Long likedUserId, Long likedPostId, Integer type, Integer status) {
        this.likedUserId = likedUserId;
        this.likedPostId = likedPostId;
        this.type = type;
        this.status = status;
    }
}