package com.jeasoon.bubblekit.ui.chat;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jeasoon.bubblekit.R;
import com.jeasoon.bubblekit.constant.ChatConstant;
import com.jeasoon.bubblekit.ui.viewmodel.ChatViewModelProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatFragment extends Fragment implements ChatConstant {

    private EditText etInput;
    private ImageButton btnSend;
    private ImageButton btnRefresh;
    private RecyclerView rvMessages;
    private List<Message> mMessageList;
    private MessageAdapter mMessageAdapter;
    private ChatViewModel mChatViewModel;
    private String mViewModelName;
    private boolean isNotificationActivity;

    public ChatFragment() {
    }

    public ChatFragment(String viewModelName) {
        mViewModelName = viewModelName;
        isNotificationActivity = true;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        findViews();
        initData();
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        mChatViewModel.onResume(isNotificationActivity);
        etInput.post(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = Objects.requireNonNull(getContext()).getSystemService(InputMethodManager.class);
                Objects.requireNonNull(imm).hideSoftInputFromWindow(etInput.getWindowToken(), 0);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mChatViewModel.onPause();
        mChatViewModel.setCachedInput(etInput.getText().toString());
    }

    private void findViews() {
        View view = getView();
        assert view != null;
        etInput = view.findViewById(R.id.input);
        btnSend = view.findViewById(R.id.send);
        btnRefresh = view.findViewById(R.id.auto_fill);
        rvMessages = view.findViewById(R.id.messages);
    }

    private void initData() {
        mChatViewModel = getViewModel();
        mMessageList = new ArrayList<>();
        mMessageAdapter = new MessageAdapter(mMessageList);
    }

    private void initView() {
        LinearLayoutManager layoutManage = new LinearLayoutManager(getContext());
        layoutManage.setStackFromEnd(true);
        rvMessages.setLayoutManager(layoutManage);
        rvMessages.setAdapter(mMessageAdapter);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = etInput.getText().toString();
                if (TextUtils.isEmpty(input)) {
                    Toast.makeText(getContext(), R.string.bubble_error_send_empty_message, Toast.LENGTH_SHORT).show();
                    return;
                }
                mChatViewModel.sendMsg(input);
                etInput.setText(null);
            }
        });
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etInput.setText(mChatViewModel.getPoetryText());
            }
        });
        mChatViewModel.getMessageList().observe(Objects.requireNonNull(getActivity()), new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> list) {
                mMessageList.clear();
                mMessageList.addAll(list);
                mMessageAdapter.notifyDataSetChanged();
                final int targetPosition = mMessageList.size() - 1;
                if (targetPosition >= 0) {
                    rvMessages.post(new Runnable() {
                        @Override
                        public void run() {
                            rvMessages.smoothScrollToPosition(targetPosition);
                        }
                    });
                }
            }
        });
        mChatViewModel.getCachedInput().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                etInput.setText(s);
            }
        });
        String chatSessionName = getViewModel().getChatSessionName();
        if (!TextUtils.isEmpty(chatSessionName)) {
            getActivity().setTitle(chatSessionName);
        }
    }

    private ChatViewModel getViewModel() {
        if (mChatViewModel != null) {
            return mChatViewModel;
        }
        mViewModelName = mViewModelName != null ? mViewModelName : getViewModelClass().getName();
        return ChatViewModelProvider.getInstance().get(mViewModelName);
    }

    protected Class<? extends ChatViewModel> getViewModelClass() {
        return ChatViewModel.class;
    }

}
