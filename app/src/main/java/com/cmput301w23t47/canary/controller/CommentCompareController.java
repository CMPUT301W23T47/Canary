package com.cmput301w23t47.canary.controller;

import com.cmput301w23t47.canary.model.Comment;

import java.util.Comparator;

/**
 * Comparator for the Comments, sorts it in descending order by date
 */
public class CommentCompareController implements Comparator<Comment> {
    /**
     * The comparator function for sorting the comments based on the date they were posted
     * Descending order by date
     * @param comment1 the first comment object
     * @param comment2 the second comment object
     * @return
     */
    @Override
    public int compare(Comment comment1, Comment comment2) {
        return comment2.getDate().compareTo(comment1.getDate());
    }
}
