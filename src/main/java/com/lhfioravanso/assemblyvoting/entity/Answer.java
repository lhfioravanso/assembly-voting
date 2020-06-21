package com.lhfioravanso.assemblyvoting.entity;

public enum Answer {

    NO("Não"),
    YES("Sim");

    private String answer;

    Answer(String answer){
        this.answer = answer;
    }

    public String getAnswer(){
        return answer;
    }

    public void setAnswer(String answer){
        this.answer = answer;
    }
}
