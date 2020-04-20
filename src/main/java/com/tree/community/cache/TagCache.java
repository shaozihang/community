package com.tree.community.cache;

import com.tree.community.dto.TagDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TagCache {
    public static List<TagDTO> get(){
        ArrayList<TagDTO> tagDTOS = new ArrayList<>();
        TagDTO web = new TagDTO();
        web.setCategoryName("前端");
        web.setTags(Arrays.asList("javascript","vue.js","css","html","node.js","前端","react.js","jquery","css3","html5","es6","bootstrap","angular","edge"));

        TagDTO backEnd = new TagDTO();
        backEnd.setCategoryName("后端");
        backEnd.setTags(Arrays.asList("php","java","node.js","python","c++","c","golang","spring","django","springboot","flask","后端","C#","swoole","ruby","asp.net","ruby-on-rails","scala","rust","lavarel","爬虫"));

        TagDTO mobileTerminal = new TagDTO();
        mobileTerminal.setCategoryName("移动端");
        mobileTerminal.setTags(Arrays.asList("java","android","ios","objective-c","小程序","swift","react-native","xcode","android-studio","webapp","flutter","kotlin"));

        TagDTO db = new TagDTO();
        db.setCategoryName("数据库");
        db.setTags(Arrays.asList("mysql","redis","mongodb","sql","数据库","json","elasticsearch","nosql","memcached","postgresql","sqlite","mariadb"));

        TagDTO server = new TagDTO();
        server.setCategoryName("运维");
        server.setTags(Arrays.asList("linux","nginx","docker ","apache","centos","ubuntu","服务器","负载均衡","运维","ssh","vagrant","容器","jenkins","devops","debian","fabric"));

        tagDTOS.add(web);
        tagDTOS.add(backEnd);
        tagDTOS.add(mobileTerminal);
        tagDTOS.add(db);
        tagDTOS.add(server);
        return tagDTOS;
    }

    public static String filterInvalid(String tags){
        String[] split = tags.split(",");
        List<TagDTO> tagDTOS = get();

        List<String> tagList = tagDTOS.stream().flatMap(tag -> tag.getTags().stream()).collect(Collectors.toList());
        String invalid = Arrays.stream(split).filter(t -> !tagList.contains(t)).collect(Collectors.joining(","));
        return invalid;
    }
}


