package com.jeasoon.bubblekit.ui.chat;

import android.content.res.ColorStateList;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.jeasoon.bubblekit.R;

import java.util.List;
import java.util.Objects;


public class MessageAdapter extends ListAdapter<Message, MessageAdapter.ViewHolder> {

    private List<Message> mMessageList;
    private ColorStateList tintColorIncoming;
    private ColorStateList tintColorOutgoing;
    private int padding_vertical;
    private int padding_horizontal_long;
    private int padding_horizontal_short;

    MessageAdapter(List<Message> messageList) {
        super(new DiffCallback());
        mMessageList = messageList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Message message = mMessageList.get(position);
        if (tintColorIncoming == null) {
            tintColorIncoming = ColorStateList.valueOf(ContextCompat.getColor(holder.tvMsg.getContext(), R.color.message_incoming));
            tintColorOutgoing = ColorStateList.valueOf(ContextCompat.getColor(holder.tvMsg.getContext(), R.color.message_outgoing));
        }
        if (padding_vertical == 0) {
            padding_vertical = holder.tvMsg.getContext().getResources().getDimensionPixelSize(R.dimen.message_item_vertical_padding);
            padding_horizontal_long = holder.tvMsg.getContext().getResources().getDimensionPixelSize(R.dimen.message_item_horizontal_padding_long);
            padding_horizontal_short = holder.tvMsg.getContext().getResources().getDimensionPixelSize(R.dimen.message_item_horizontal_padding_short);
        }

        holder.tvName.setText(message.getSender().getName());
        holder.tvName.setGravity(message.isIncoming() ? Gravity.START : Gravity.END);
        holder.ivIncoming.setVisibility(message.isIncoming() ? View.VISIBLE : View.INVISIBLE);
        holder.ivOutgoing.setVisibility(!message.isIncoming() ? View.VISIBLE : View.INVISIBLE);
        holder.ivIncoming.setImageIcon(message.getSender().getIcon());
        holder.ivOutgoing.setImageIcon(message.getSender().getIcon());
        holder.tvMsg.setText(message.getContent());
        holder.tvMsg.setBackgroundResource(message.isIncoming() ? R.drawable.message_incoming : R.drawable.message_outgoing);
        ViewCompat.setBackgroundTintList(holder.tvMsg, message.isIncoming() ? tintColorIncoming : tintColorOutgoing);
        holder.tvMsg.setPadding(
                message.isIncoming() ? padding_horizontal_long : padding_horizontal_short,
                padding_vertical,
                message.isIncoming() ? padding_horizontal_short : padding_horizontal_long,
                padding_vertical);
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) holder.tvMsg.getLayoutParams();
        lp.leftToLeft = message.isIncoming() ? R.id.tv_name : -1;
        lp.rightToRight = !message.isIncoming() ? R.id.tv_name : -1;
        holder.tvMsg.setLayoutParams(lp);
        if (holder.tvMsg.getMaxWidth() == Integer.MAX_VALUE) {
            holder.tvMsg.post(new Runnable() {
                @Override
                public void run() {
                    holder.tvMsg.setMaxWidth(holder.tvName.getWidth());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    private static class DiffCallback extends DiffUtil.ItemCallback<Message> {
        @Override
        public boolean areItemsTheSame(@NonNull Message oldItem, @NonNull Message newItem) {
            return Objects.equals(oldItem.getReceiver().getKey(), newItem.getReceiver().getKey());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Message oldItem, @NonNull Message newItem) {
            return Objects.equals(oldItem.getContent(), newItem.getContent());
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvMsg;
        TextView tvName;
        ImageView ivIncoming;
        ImageView ivOutgoing;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMsg = itemView.findViewById(R.id.message);
            tvName = itemView.findViewById(R.id.tv_name);
            ivIncoming = itemView.findViewById(R.id.iv_incoming);
            ivOutgoing = itemView.findViewById(R.id.iv_outgoing);
        }
    }

}
