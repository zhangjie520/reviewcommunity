package com.example.demo.cache;

import com.example.demo.dto.TagDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: codeape
 * @Date: 2021/1/25 21:18
 * @Version: 1.0
 */
public class TagCache {
    public static List<TagDTO> get(){
        List<TagDTO> tagDTOS=new ArrayList<>();
        TagDTO program=new TagDTO();
        program.setCategoryName("开发语言");
        program.setTags(Arrays.asList("java","python","c#" ,"c"));
        tagDTOS.add(program);
        TagDTO framework=new TagDTO();
        framework.setCategoryName("平台框架");
        framework.setTags(Arrays.asList("thymeleaf","spring boot","spring mvc" ,"pygame","thymeleaf","spring boot","spring mvc" ,"pygame","thymeleaf","spring boot","spring mvc" ,"pygame"));
        tagDTOS.add(framework);
        return tagDTOS;
    }
    public static String filterInvalid(String tags){
        String[] split=StringUtils.split(tags,",");
        List<TagDTO> tagDTOS = get();
        List<String> tagLists = tagDTOS.stream().flatMap(tag -> tag.getTags().stream()).collect(Collectors.toList());
        String invalid = Arrays.stream(split).filter(t -> !tagLists.contains(t)).collect(Collectors.joining(","));
        return invalid;
    }
}
