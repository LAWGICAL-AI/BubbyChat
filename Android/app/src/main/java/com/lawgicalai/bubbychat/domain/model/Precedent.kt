package com.lawgicalai.bubbychat.domain.model

data class Precedent(
    val id: String,
    val title: String,
    val court: String, // 법원
    val caseNumber: String, // 사건번호
    val date: String, // 선고일자
    val summary: String, // 요약
    val content: String, // 전문
    val category: String, // 분류 (예: 민사, 형사, 행정 등)
    val relatedLaws: List<String>, // 관련 법령
    val keywords: List<String>, // 키워드
)
