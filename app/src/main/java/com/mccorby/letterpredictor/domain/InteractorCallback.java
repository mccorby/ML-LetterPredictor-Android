package com.mccorby.letterpredictor.domain;

public interface InteractorCallback<T> {

    void onSuccess(T result);
    void onError();
}
