package com.creatoo.szwhg.base.service;

import com.creatoo.szwhg.base.dao.CommentDao;
import com.creatoo.szwhg.base.model.Comment;
import com.creatoo.szwhg.base.model.CommentStatus;
import com.creatoo.szwhg.base.model.OperLog;
import com.creatoo.szwhg.base.model.QComment;
import com.creatoo.szwhg.core.util.GeneralPredicatesBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentDao commentDao;
    @Value("${comment.audit.isopen}")
    private Boolean auditIsopen;

    public Page<Comment> findAll(String search, Pageable pageable){
        GeneralPredicatesBuilder builder=new GeneralPredicatesBuilder(Comment.class);
        BooleanExpression exp=builder.build(search);
        return commentDao.findAll(exp,pageable);
    }

    public void auditComment(String id,CommentStatus status){
        Comment comment=commentDao.findOne(id);
        comment.setStatus(status);
        commentDao.save(comment);
    }

    /**
     * 添加评论
     * @param comment
     * @return
     */
    public String addComment(Comment comment) {
        comment.setTime(LocalDateTime.now());
        if(auditIsopen){
            comment.setStatus(CommentStatus.Wait);
        } else {
            comment.setStatus(CommentStatus.Pass);
        }
        commentDao.save(comment) ;
        return comment.getId() ;
    }

    /**
     * 根据评论ID删除评论
     * @param commentId
     */
    public void deleteComment(String commentId) {
        commentDao.delete(commentId);
    }

}
