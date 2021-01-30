package com.example.demo.service;

import com.example.demo.dto.PaginationDTO;
import com.example.demo.dto.QuestionDTO;
import com.example.demo.dto.QuestionQueryDTO;
import com.example.demo.exception.CustomErrorCode;
import com.example.demo.exception.CustomException;
import com.example.demo.mapper.QuestionExtMapper;
import com.example.demo.mapper.QuestionMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.Question;
import com.example.demo.model.QuestionExample;
import com.example.demo.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: codeape
 * @Date: 2021/1/16 21:38
 * @Version: 1.0
 */
@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;
    @Autowired
    private UserMapper userMapper;

    public PaginationDTO<QuestionDTO> list(String search,Integer pageIndex, Integer size) {
        if (StringUtils.isNotBlank(search)){
            String[] tags = StringUtils.split(search, " ");
            search = Arrays.stream(tags).collect(Collectors.joining("|"));
        }
        PaginationDTO<QuestionDTO> pagination = new PaginationDTO();
        List<QuestionDTO> questionDTOS = new ArrayList<>();
        //search all question by search
        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);
        Integer totalCount = questionExtMapper.countBySearch(questionQueryDTO);
        Integer totalPage;
        //计算totalpage
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        //判断pageIndex合法性
        if (pageIndex < 1) {
            pageIndex = 1;
        }
        if (pageIndex > totalPage) {
            pageIndex = totalPage;
        }
        pagination.setTotalPage(totalPage);
        pagination.setPagiNation(pageIndex);
        //计算offset和size offset=(currentPage-1)*size
        Integer offset = size * (pageIndex - 1);

        questionQueryDTO.setPage(offset);
        questionQueryDTO.setSize(size);
        List<Question> questions = questionExtMapper.selectBySearch(questionQueryDTO);
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        pagination.setData(questionDTOS);
        return pagination;
    }

    public PaginationDTO<QuestionDTO> listByUserId(Integer userId, Integer pageIndex, Integer size) {
        PaginationDTO<QuestionDTO> pagination = new PaginationDTO();
        List<QuestionDTO> questionDTOS = new ArrayList<>();
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria()
                .andCreatorEqualTo(userId);
        Integer totalCount = (int) questionMapper.countByExample(questionExample);
        Integer totalPage;
        //计算totalpage
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        //判断pageIndex合法性
        if (pageIndex < 1) {
            pageIndex = 1;
        }
        if (pageIndex > totalPage) {
            pageIndex = totalPage;
        }
        pagination.setTotalPage(totalPage);
        pagination.setPagiNation(pageIndex);
        //计算offset和size offset=(currentPage-1)*size
        Integer offset = size * (pageIndex - 1);

        QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andCreatorEqualTo(userId);
        example.setOrderByClause("gmt_create desc");
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        pagination.setData(questionDTOS);
        return pagination;
    }

    public QuestionDTO findById(Integer id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question == null) {
            throw new CustomException(CustomErrorCode.QUESTION_NOT_FOUNT);
        }
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question, User user) {
        //if question is exist
        if (question.getId() == null) {
            //create
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insert(question);
        } else {
            //update
            //if question is created by user
            if (user.getId() != question.getCreator()) {
                throw new CustomException(CustomErrorCode.QUESTION_NOT_FOUNT);
            }
            question.setGmtModified(question.getGmtCreate());
            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria()
                    .andIdEqualTo(question.getId());
            questionMapper.updateByExampleSelective(question, questionExample);
        }
    }

    public void incView(Integer id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);
    }

    public List<QuestionDTO> selectRelated(QuestionDTO queryQuestionDto) {
        //检查输入
        if (StringUtils.isBlank(queryQuestionDto.getTag())) {
            return new ArrayList<>();
        }
        //拼接tag
        String regexpTag = queryQuestionDto.getTag().replace(",", "|");
        //查询tag的问题
        Question queryQuestion = new Question();
        queryQuestion.setId(queryQuestionDto.getId());
        queryQuestion.setTag(regexpTag);
        List<Question> questions = questionExtMapper.selectRelated(queryQuestion);
        //返回数据
        List<QuestionDTO> questionDTOS= questions.stream().map(q -> {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(q, questionDTO);
            return questionDTO;
        }).collect(Collectors.toList());
        return questionDTOS;
    }
}
