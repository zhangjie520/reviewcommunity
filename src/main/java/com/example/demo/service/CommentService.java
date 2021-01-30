package com.example.demo.service;

import com.example.demo.dto.CommentDTO;
import com.example.demo.enums.CommentTypeEnum;
import com.example.demo.enums.NotificationStatusEnum;
import com.example.demo.enums.NotificationTypeEnum;
import com.example.demo.exception.CustomErrorCode;
import com.example.demo.exception.CustomException;
import com.example.demo.mapper.*;
import com.example.demo.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: codeape
 * @Date: 2021/1/20 21:30
 * @Version: 1.0
 */
@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private CommentExtMapper commentExtMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;
    @Autowired
    private NotificationMapper notificationMapper;

    public void insert(Comment comment) {
        //if checking input is null
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomException(CustomErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomException(CustomErrorCode.TYPE_PARAM_ERROR);
        }
        //comment question or comment comment
        if (comment.getType() == CommentTypeEnum.QUESTION.getType()) {
            //comment question
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null) {
                throw new CustomException(CustomErrorCode.QUESTION_NOT_FOUNT);
            }
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);
            //add notification
            CreateNotification(comment, question.getCreator(), question.getId(), NotificationTypeEnum.QUESTION);
        } else {
            //comment comment
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId()); //dbComment=parentComment
            if (dbComment == null) {
                throw new CustomException(CustomErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1L);
            commentExtMapper.incCommentCount(parentComment);
            //add notification
            CreateNotification(comment, comment.getCommentator(), dbComment.getParentId(), NotificationTypeEnum.COMMENT);
        }
    }

    /**
     * 创建通知
     *
     * @param comment
     * @param receiver
     * @param questionId
     * @param type
     */

    public void CreateNotification(Comment comment, Integer receiver, Integer questionId, NotificationTypeEnum type) {
        //若是作者评论的自己的评论或问题则不需通知
        if(receiver==comment.getCommentator()){
            return;
        }
        Notification notification = new Notification();
        notification.setNotifier(comment.getCommentator());
        notification.setReceiver(receiver);
        notification.setOuterId(questionId);
        notification.setOuterTitle("");  //暂定
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setType(type.getType());
        notification.setGmtCreate(System.currentTimeMillis());
        notificationMapper.insert(notification);
    }

    /**
     * 通过quesitonID和类型查询二级评论
     *
     * @param questionId
     * @param type
     * @return
     */

    public List<CommentDTO> listByQuestionId(Integer questionId, CommentTypeEnum type) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(questionId)
                .andTypeEqualTo(type.getType());
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if (comments.size() == 0) {
            return new ArrayList<>();
        }
        //使用lambda获取去重的评论人
        Set<Integer> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Integer> userIds = new ArrayList<>();
        userIds.addAll(commentators);
        //获取评论人并转化为map
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Integer, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));
        //convert comment to commentDTO
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());
        return commentDTOS;
    }
}
