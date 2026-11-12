package com.interview.assessment

data class CharacterResponse(
    val info: Info,
    val results: List<Character>
)

data class Info(
    val next: String?
)

// 新增 Origin 和 Location 类（API 中它们是包含 name 的对象）
data class Origin(
    val name: String
)

data class Location(
    val name: String
)

data class Character(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val image: String,
    val gender: String,
    val origin: Origin,
    val location: Location,
    val episode: List<String>
)