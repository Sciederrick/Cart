package com.israteneda.notekeeper

data class CourseInfo(val courseId: String, val title: String) {
    override fun toString(): String {
        return title
    }
}

data class ListInfo(var course: CourseInfo? = null, var title:String? = null, var text: String? = null)