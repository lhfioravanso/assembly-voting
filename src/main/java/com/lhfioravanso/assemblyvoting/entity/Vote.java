package com.lhfioravanso.assemblyvoting.entity;

import java.util.Objects;

public class Vote {
    private String cpf;
    private Answer answer;

    public Vote(String cpf, Answer answer) {
        this.cpf = cpf;
        this.answer = answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vote vote = (Vote) o;
        return Objects.equals(cpf, vote.cpf) &&
                answer == vote.answer;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpf, answer);
    }
}
