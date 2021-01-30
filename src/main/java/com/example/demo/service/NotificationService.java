package com.example.demo.service;

import com.example.demo.dto.NotificationDTO;
import com.example.demo.dto.PaginationDTO;
import com.example.demo.enums.NotificationStatusEnum;
import com.example.demo.enums.NotificationTypeEnum;
import com.example.demo.exception.CustomErrorCode;
import com.example.demo.exception.CustomException;
import com.example.demo.mapper.NotificationMapper;
import com.example.demo.mapper.QuestionMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.Notification;
import com.example.demo.model.NotificationExample;
import com.example.demo.model.Question;
import com.example.demo.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: codeape
 * @Date: 2021/1/26 22:21
 * @Version: 1.0
 */
@Service
public class NotificationService {
    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;

    /**
     * get all notification by receiver
     *
     * @param user
     * @param pageIndex
     * @param size
     * @return
     */
    public PaginationDTO<NotificationDTO> listByReceiver(User user, Integer pageIndex, Integer size) {
        PaginationDTO<NotificationDTO> pagination = new PaginationDTO();
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(user.getId());
        notificationExample.setOrderByClause("gmt_create desc");
        Integer totalCount = (int) notificationMapper.countByExample(notificationExample);
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

        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(notificationExample, new RowBounds(offset, size));
        List<NotificationDTO> notificationDTOS = notifications.stream().map(notification -> {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification, notificationDTO);
            //放入notifierUser
            notificationDTO.setNotifierUser(user);
            //放入question by outerId
            Question question = questionMapper.selectByPrimaryKey(notification.getOuterId());
            notificationDTO.setQuestion(question);
            //放入typeValue by type
            notificationDTO.setTypeValue(NotificationTypeEnum.nameOfType(notification.getType()));
            return notificationDTO;
        }).collect(Collectors.toList());
        pagination.setData(notificationDTOS);
        return pagination;
    }

    /**
     * update notification status by id
     *
     * @param notificationId
     * @param user
     * @return
     */
    public NotificationDTO read(Integer notificationId, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(notificationId);
        if (notification == null) {
            throw new CustomException(CustomErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if (user.getId() != notification.getReceiver()) {
            throw new CustomException(CustomErrorCode.NOTIFICATION_NOT_MATCH);
        }
        //更新阅读状态
        Notification updateNotification = new Notification();
        updateNotification.setId(notificationId);
        updateNotification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKeySelective(updateNotification);
        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification, notificationDTO);
        return notificationDTO;
    }

    public Long unReadCount(Integer receiver) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(receiver)
                .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        Long unreadCount = notificationMapper.countByExample(notificationExample);
        return unreadCount;
    }
}
