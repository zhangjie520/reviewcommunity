package com.example.demo.mapper;

import com.example.demo.dto.QuestionDTO;
import com.example.demo.dto.QuestionQueryDTO;
import com.example.demo.model.Question;
import com.example.demo.model.QuestionExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface QuestionExtMapper {
    Integer incView(Question question);

    Integer incCommentCount(Question question);

    List<Question> selectRelated(Question queryQuestion);

    Integer countBySearch(QuestionQueryDTO questionQueryDTO);

    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);
}