package com.pullup.problem.util;

public class AnswerUtil {

    private AnswerUtil() {
    }

    /**
     * 답변 전처리: 띄어쓰기 제거, 대소문자 무시(영어), 숫자는 그대로 유지
     */
    public static String normalizeAnswer(String answer) {
        if (answer == null) {
            return "";
        }

        // 1. 양 끝 공백 제거, 띄어쓰기 제거
        answer = answer.trim().replaceAll("\\s+", "");

        // 2. 영어라면 소문자로 변환
        if (answer.matches(".*[a-zA-Z].*")) {
            return answer.toLowerCase();
        }

        // 3. 숫자는 변환 없이 그대로 반환
        if (answer.matches("\\d+")) {
            return answer;
        }

        // 4. 한글도 그대로 반환 (초성 비교 X, 변환 X)
        return answer;
    }

}
