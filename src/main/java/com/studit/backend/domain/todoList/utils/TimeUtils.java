package com.studit.backend.domain.todoList.utils;

public class TimeUtils {
    public static String formatDuration(long totalSeconds) {
        long hours = totalSeconds / 3600; // 1시간 = 3600초
        long minutes = (totalSeconds % 3600) / 60; // 나머지 초를 60으로 나누어 분으로 변환
        long seconds = totalSeconds % 60; // 나머지 초

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
