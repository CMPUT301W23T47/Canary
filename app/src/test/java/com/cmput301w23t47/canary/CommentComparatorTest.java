package com.cmput301w23t47.canary;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.cmput301w23t47.canary.controller.CommentCompareController;
import com.cmput301w23t47.canary.model.Comment;
import com.google.common.collect.Comparators;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

/**
 * Tests the comparator for sorting comments
 */
public class CommentComparatorTest {
    /**
     * Tests the comparator to have the comments sorted by date
     */
    @Test
    public void testComparator() {
        ArrayList<Comment> comments = new ArrayList<>();
        comments.add(new Comment("player1", "mess1", new Date(15000)));
        comments.add(new Comment("player1", "mess1", new Date(16000)));
        comments.add(new Comment("player1", "mess1", new Date(17000)));
        comments.add(new Comment("player1", "mess1", new Date(18000)));
        comments.add(new Comment("player1", "mess1", new Date(19000)));
        comments.sort(new CommentCompareController()); // sort the list by date time
        // verify the final order; should be ascending by date
        assertTrue(Comparators.isInOrder(comments, (Object o1, Object o2) ->
                (int)(
                        ( (Comment)o2).getDate().compareTo( ((Comment)o1).getDate()) )
                )
        );
    }
}
