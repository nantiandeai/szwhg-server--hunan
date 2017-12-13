package com.creatoo.szwhg.core.model;

import java.util.List;

/**
 * Created by yunyan on 2017/8/21.
 */
public interface TreeNode<T> {
    String getId();
    TreeNode<T> getParent();
    int getSeqno();
    List<T> getChildren();
}
