package com.poolup.poolup.subject.enums;

public enum SubjectName {
    운영체제, 네트워크, 컴퓨터구조,
    AWS,
    JAVA, Spring,
    JavaScript, React;

    @Override
    public String toString() {
        return name();
    }
}
