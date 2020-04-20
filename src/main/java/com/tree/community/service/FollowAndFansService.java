package com.tree.community.service;

import com.tree.community.dto.FollowAndFandsDTO;
import com.tree.community.dto.PaginationDTO;
import com.tree.community.mapper.FollowAndFansMapper;
import com.tree.community.mapper.UserMapper;
import com.tree.community.model.FollowAndFans;
import com.tree.community.model.FollowAndFansExample;
import com.tree.community.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class FollowAndFansService {

    @Autowired
    private FollowAndFansMapper followAndFansMapper;

    @Autowired
    private UserMapper userMapper;

    @Transactional
    public void addOrCancelFollow(FollowAndFans followAndFans) {
        User meUser = userMapper.selectByPrimaryKey(followAndFans.getFromUserId());
        User otherUser = userMapper.selectByPrimaryKey(followAndFans.getToUserId());
        if(followAndFans.getStatus() == 0){
            FollowAndFansExample followAndFansExample = new FollowAndFansExample();
            followAndFansExample.createCriteria()
                    .andFromUserIdEqualTo(followAndFans.getToUserId())
                    .andToUserIdEqualTo(followAndFans.getFromUserId());
            List<FollowAndFans> result = followAndFansMapper.selectByExample(followAndFansExample);
            meUser.setFollowCount(meUser.getFollowCount()+1);
            userMapper.updateByPrimaryKeySelective(meUser);
            otherUser.setFansCount(otherUser.getFansCount()+1);
            userMapper.updateByPrimaryKeySelective(otherUser);
            followAndFans.setGmtCreate(System.currentTimeMillis());
            followAndFans.setGmtModified(System.currentTimeMillis());
            if(result.size() == 0){
                followAndFans.setStatus(1);
                followAndFansMapper.insert(followAndFans);
            }else{
                followAndFans.setStatus(2);
                followAndFansMapper.insert(followAndFans);
            }
        }else if(followAndFans.getStatus() == 1 || followAndFans.getStatus() == 2){
            FollowAndFansExample followAndFansExample = new FollowAndFansExample();
            followAndFansExample.createCriteria()
                    .andFromUserIdEqualTo(followAndFans.getFromUserId())
                    .andToUserIdEqualTo(followAndFans.getToUserId());
            followAndFansMapper.deleteByExample(followAndFansExample);
            meUser.setFollowCount(meUser.getFollowCount()-1);
            userMapper.updateByPrimaryKeySelective(meUser);
            otherUser.setFansCount(otherUser.getFansCount()-1);
            userMapper.updateByPrimaryKeySelective(otherUser);
        }
    }

    @Transactional
    public int getFollowStatus(Long meId, Long otherId) {
        FollowAndFansExample followAndFansExample = new FollowAndFansExample();
        followAndFansExample.createCriteria()
                .andFromUserIdEqualTo(meId)
                .andToUserIdEqualTo(otherId);
        List<FollowAndFans> result = followAndFansMapper.selectByExample(followAndFansExample);
        FollowAndFansExample followAndFansExample1 = new FollowAndFansExample();
        followAndFansExample1.createCriteria()
                .andFromUserIdEqualTo(otherId)
                .andToUserIdEqualTo(meId);
        List<FollowAndFans> result1 = followAndFansMapper.selectByExample(followAndFansExample1);
        int followStatus;
        if(result.size() == 0){
            if(result1.size() != 0){
                followStatus = -1;
            }else {
                followStatus = 0;
            }
        }else{
            FollowAndFansExample followAndFansExample2 = new FollowAndFansExample();
            followAndFansExample2.createCriteria()
                    .andIdEqualTo(result.get(0).getId());
            FollowAndFans followAndFans = new FollowAndFans();
            if(result1.size() == 0){
                followAndFans.setStatus(1);
                followAndFansMapper.updateByExampleSelective(followAndFans,followAndFansExample2);
                followStatus = 1;
            }else{
                followAndFans.setStatus(2);
                followAndFansMapper.updateByExampleSelective(followAndFans,followAndFansExample2);
                followStatus = 2;
            }
        }
        return followStatus;
    }

    public PaginationDTO getFollowList(Long otherId, User meUser,Integer page,Integer size,Integer type) {
        PaginationDTO paginationDTO = new PaginationDTO();

        Integer totalPage;

        FollowAndFansExample followAndFansExample = new FollowAndFansExample();
        if(type == 1){
            followAndFansExample.createCriteria().andFromUserIdEqualTo(otherId);
        }else if(type == 2){
            followAndFansExample.createCriteria().andToUserIdEqualTo(otherId);
        }
        Integer totalCount = (int) followAndFansMapper.countByExample(followAndFansExample);

        if(totalCount % size ==0){
            totalPage = totalCount / size;
        }else{
            totalPage = totalCount / size + 1;
        }

        if(page < 1){
            page = 1;
        }
        if(page>totalPage){
            page = totalPage;
        }

        paginationDTO.setPagination(totalPage,page);

        //分页
        Integer offset = page < 1 ? 0 : size*(page-1);
        FollowAndFansExample example1 = new FollowAndFansExample();
        if(type == 1){
            example1.createCriteria().andFromUserIdEqualTo(otherId);
        }else if(type == 2){
            example1.createCriteria().andToUserIdEqualTo(otherId);
        }
        example1.setOrderByClause("gmt_create desc");
        List<FollowAndFans> followAndFans = followAndFansMapper.selectByExampleWithRowbounds(example1, new RowBounds(offset, size));

        if(followAndFans.size() == 0){
            return paginationDTO;
        }

        List<FollowAndFandsDTO> followAndFandsDTOList = new ArrayList<>();

        for (FollowAndFans follow : followAndFans) {
            User user = null;
            if(type == 1){
                user = userMapper.selectByPrimaryKey(follow.getToUserId());
            }else if(type == 2){
                user = userMapper.selectByPrimaryKey(follow.getFromUserId());
            }
            FollowAndFandsDTO followAndFandsDTO = new FollowAndFandsDTO();
            followAndFandsDTO.setUser(user);
            int followStatus = 0;
            if(meUser != null){
                if(type == 1){
                    followStatus = getFollowStatus(meUser.getId(), follow.getToUserId());
                }else if(type == 2){
                    followStatus = getFollowStatus(meUser.getId(), follow.getFromUserId());
                }
            }
            if(followStatus == -1){
                followStatus = 0;
                followAndFandsDTO.setFollowOtherStatus(-1);
            }else{
                followAndFandsDTO.setFollowOtherStatus(0);
            }
            followAndFandsDTO.setFollowStatus(followStatus);
            followAndFandsDTOList.add(followAndFandsDTO);
        }
        paginationDTO.setData(followAndFandsDTOList);
        return paginationDTO;
    }
}
