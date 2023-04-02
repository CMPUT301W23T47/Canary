package com.cmput301w23t47.canary.controller;

import com.cmput301w23t47.canary.model.Comment;

import java.util.Comparator;

public class CommentCompareController implements Comparator<Comment> {

    @Override
    public int compare(Comment comment1, Comment comment2) {
        return comment2.getDate().compareTo(comment1.getDate());
    }
}
