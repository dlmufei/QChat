package com.tencent.qchat.activity;

import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.tencent.qchat.R;
import com.tencent.qchat.http.RetrofitHelper;
import com.tencent.qchat.utils.UserUtil;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import rx.Subscriber;

/**
 * 嘉宾对问题进行回答
 *
 * Created by cliffyan on 2016/9/2.
 */
public class NewAnswerActivity extends BaseActivity {
    @BindView(R.id.title)
    TextView mTitleView;
    @BindView(R.id.more_tv)
    TextView mPostView;

    @BindView(R.id.answer_title)
    TextView mAnswerTitleView;

    @BindView(R.id.et_question)
    EditText mQuestionEdit;
    @BindView(R.id.tv_textNum)
    TextView mTextNum;

    int question_id = -1;
    String question_title = null;

    public static final String ANSWER_TYPE_TEXT = "text";
    public static final String ANSWER_TYPE_VOICE = "voice";

    public static final int REQ_CODE = 0x01;
    public static final String Q_ID = "qid";
    public static final String Q_TITLE = "qtitle";


    @OnTextChanged(R.id.et_question)
    protected void to_textNumChange() {
        mTextNum.setText(mQuestionEdit.getText().length() + "/80");
    }

    @OnClick(R.id.back)
    protected void to_back() {
        onBackPressed();
    }

    @OnClick(R.id.more_tv)
    protected void to_post_ques() {
        String content = mQuestionEdit.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            showToast("请先输入回答内容");
            return;
        }

        RetrofitHelper.getInstance().addAnswer(UserUtil.getToken(this), question_id, ANSWER_TYPE_TEXT, content, new Subscriber<JsonObject>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                showToast(e.getMessage());
            }

            @Override
            public void onNext(JsonObject jsonObject) {
                Log.i("jsonObject", jsonObject.toString());//TODO
                showToast("发布成功");
                NewAnswerActivity.this.finish();


            }
        });
    }

    @Override
    public int initLayoutRes() {
        return R.layout.activity_new_answer;
    }

    @Override
    public void onWillLoadView() {
        question_id = getIntent().getIntExtra(Q_ID, -1);
        question_title = getIntent().getStringExtra(Q_TITLE);
    }

    @Override
    public void onDidLoadView() {
        mTitleView.setText("回答");
        mPostView.setText("发布");
        mQuestionEdit.setHint("请输入想回答的答案");
        mAnswerTitleView.setText(question_title);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        playExitAnimation();
    }


}
