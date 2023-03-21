package com.cmput301w23t47.canary.view.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.cmput301w23t47.canary.R;
import com.cmput301w23t47.canary.callback.GetIndexCallback;
import com.cmput301w23t47.canary.controller.CommentCompareController;
import com.cmput301w23t47.canary.controller.QrCodeController;
import com.cmput301w23t47.canary.model.Comment;

import java.util.ArrayList;
import java.util.Collections;

/**
 * The adapter for the comments list; To be used with recycler view
 */
public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder>{

    private ArrayList<Comment> comments;

    public CommentListAdapter(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_comment_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        // TODO: Set image
        holder.commentPlayerUsername.setText(comment.getPlayerUsername());
        holder.commentDate.setText(QrCodeController.getFormattedDate(comment.getDate()));
        holder.commentBody.setText(comment.getMessage());
        setUserProfileImage(holder, comment.getPlayerUsername());
    }

    /**
     * Sets the profile page for the player
     * @param holder the holder for the view of comment
     * @param playerUsername the username of the player
     */
    private void setUserProfileImage(ViewHolder holder, String playerUsername) {
        String firstLetter = String.valueOf(playerUsername.charAt(0)).toUpperCase();
        TextDrawable drawable = TextDrawable.builder().buildRound(firstLetter, Color.BLACK);
        holder.commentPlayerImage.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    /**
     * Updates the list with the new comments
     * @param newComments the comments to display
     */
    public void updateList(ArrayList<Comment> newComments) {
        comments.clear();
        comments.addAll(newComments);
        Collections.sort(comments, new CommentCompareController()); // sort the list by date time
        notifyDataSetChanged();
    }

    public void addComment(Comment comment){
        comments.add(comment);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView commentPlayerUsername;
        private final TextView commentDate;
        private final TextView commentBody;
        private final ImageView commentPlayerImage;

        /**
         * Initializes a view and sets the score of the player
         * @param itemView the item view to fill in
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            commentPlayerUsername = itemView.findViewById(R.id.comment_username);
            commentDate = itemView.findViewById(R.id.comment_date);
            commentBody = itemView.findViewById(R.id.comment_body);
            commentPlayerImage = itemView.findViewById(R.id.player_profile_image);
        }
    }
}
