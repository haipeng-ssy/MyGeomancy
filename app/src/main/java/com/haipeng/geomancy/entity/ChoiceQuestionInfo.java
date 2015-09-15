package com.haipeng.geomancy.entity;

/**
 * Created by Administrator on 2015/3/1.
 * 每道题的选项和答案
 */
public class ChoiceQuestionInfo {
    String title;
    String choice_first;
    String choice_second;

    public String getChoice_first() {
        return choice_first;
    }

    public String getChoice_second() {
        return choice_second;
    }

    public String getTitle() {
        return title;
    }

    public void setChoice_first(String choice_first) {
        this.choice_first = choice_first;
    }

    public void setChoice_second(String choice_second) {
        this.choice_second = choice_second;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
