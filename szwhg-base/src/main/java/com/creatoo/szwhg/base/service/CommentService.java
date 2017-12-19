package com.creatoo.szwhg.base.service;

import com.creatoo.szwhg.base.dao.CommentDao;
import com.creatoo.szwhg.base.model.Comment;
import com.creatoo.szwhg.base.model.CommentStatus;
import com.creatoo.szwhg.base.model.OperLog;
import com.creatoo.szwhg.core.util.GeneralPredicatesBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    @Autowired
    private CommentDao commentDao;

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
}
