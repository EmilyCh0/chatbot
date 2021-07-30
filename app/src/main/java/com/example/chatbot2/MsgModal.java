package com.example.chatbot2;

import android.util.Log;

public class MsgModal {
    private String answer; // json 답변 key랑 똑같이!

    public MsgModal(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        Log.v("checkmodalanswer",answer);
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
