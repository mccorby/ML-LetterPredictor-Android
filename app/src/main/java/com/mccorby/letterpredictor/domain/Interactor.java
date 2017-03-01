package com.mccorby.letterpredictor.domain;

public interface Interactor<T> {
    void execute(InteractorCallback<T> callback);
}
