package com.example.demo.service;

import com.example.demo.dto.PaginationDTO;
import com.example.demo.dto.QuestionDTO;
import com.example.demo.exception.CustomErrorCode;
import com.example.demo.exception.CustomException;
import com.example.demo.mapper.QuestionMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.Question;
import com.example.demo.model.QuestionExample;
import com.example.demo.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    private UserMapper userMapper;
    public PaginationDTO<QuestionDTO> list(Integer pageIndex, Integer size) {
        PaginationDTO<QuestionDTO> pagination=new PaginationDTO();
        List<QuestionDTO> questionDTOS=new ArrayList<>();
        Integer totalCount=(int)questionMapper.countByExample(new QuestionExample());
        Integer totalPage;
        //计算totalpage
        if (totalCount%size==0){
            totalPage=totalCount/size;
        }else{
            totalPage=totalCount/size+1;
        }
        //判断pageIndex合法性
        if (pageIndex<1){
            pageIndex=1;
        }
        if (pageIndex>totalPage){
            pageIndex=totalPage;
        }
        pagination.setTotalPage(totalPage);
        pagination.setPagiNation(pageIndex);
        //计算offset和size offset=(currentPage-1)*size
        Integer offset=size*(pageIndex-1);
        List<Question> questions=questionMapper.selectByExampleWithRowbounds(new QuestionExample(), new RowBounds(offset, size));
        for (Question question : questions) {
            User user=userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO=new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        pagination.setData(questionDTOS);
        return pagination;
    }

    public PaginationDTO<QuestionDTO> listByUserId(Integer userId, Integer pageIndex, Integer size) {
        PaginationDTO<QuestionDTO> pagination=new PaginationDTO();
        List<QuestionDTO> questionDTOS=new ArrayList<>();
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria()
                .andCreatorEqualTo(userId);
        Integer totalCount=(int)questionMapper.countByExample(questionExample);
        Integer totalPage;
        //计算totalpage
        if (totalCount%size==0){
            totalPage=totalCount/size;
        }else{
            totalPage=totalCount/size+1;
        }
        //判断pageIndex合法性
        if (pageIndex<1){
            pageIndex=1;
        }
        if (pageIndex>totalPage){
            pageIndex=totalPage;
        }
        pagination.setTotalPage(totalPage);
        pagination.setPagiNation(pageIndex);
        //计算offset和size offset=(currentPage-1)*size
        Integer offset=size*(pageIndex-1);

        QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andCreatorEqualTo(userId);
        List<Question> questions=questionMapper.selectByExampleWithBLOBsWithRowbounds(example,new RowBounds(offset,size));
        for (Question question : questions) {
            User user=userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO=new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        pagination.setData(questionDTOS);
        return pagination;
    }

    public QuestionDTO findById(Integer id) {
        Question question=questionMapper.selectByPrimaryKey(id);
        if (question==null){
            throw new CustomException(CustomErrorCode.QUESTION_NOT_FOUNT);
        }
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        QuestionDTO questionDTO=new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question, User user) {
        //if question is exist
        if(question.getId()==null){
            //create
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insert(question);
        }else {
            //update
            //if question is created by user
            if (user.getId()!=question.getCreator()){
                throw new CustomException(CustomErrorCode.QUESTION_NOT_FOUNT);
            }
            question.setGmtModified(question.getGmtCreate());
            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria()
                    .andIdEqualTo(question.getId());
            questionMapper.updateByExampleSelective(question, questionExample);
        }
    }
}
