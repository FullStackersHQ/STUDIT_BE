package com.studit.backend.domain.StudyPartcpntMgnt;

public enum StudyStatus {
    FAil, // (스터디참여당시사용자포인트<0)  즉 음수
    COMPLETE,  // (스터디참여당시사용자포인트> 스터디참여당시사용자포인트) 보다큼
    REFUND, //(스터디참여당시사용자포인트=) 똑같음
}