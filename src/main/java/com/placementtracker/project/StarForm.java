package com.placementtracker.project;

import com.placementtracker.common.util.InputValidator;

public class StarForm {

    private final String situation;
    private final String task;
    private final String action;
    private final String result;

    public StarForm(String situation, String task, String action, String result) {
        this.situation = situation;
        this.task = task;
        this.action = action;
        this.result = result;
    }

    public String getSituation() {
        return situation;
    }

    public String getTask() {
        return task;
    }

    public String getAction() {
        return action;
    }

    public String getResult() {
        return result;
    }

    public boolean isComplete() {
        return InputValidator.isNonEmpty(situation)
                && InputValidator.isNonEmpty(task)
                && InputValidator.isNonEmpty(action)
                && InputValidator.isNonEmpty(result);
    }

    public String toFormattedString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  Situation: ").append(situation).append("\n");
        sb.append("  Task:      ").append(task).append("\n");
        sb.append("  Action:    ").append(action).append("\n");
        sb.append("  Result:    ").append(result);
        return sb.toString();
    }
}