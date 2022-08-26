package com.israteneda.notekeeper

object DataManager {
    val courses = HashMap<String, CourseInfo>()
    val lists = ArrayList<ListInfo>()

    init {
        initializeCourses()
        initializeNotes()
    }

    fun loadLists(vararg noteIds: Int): List<ListInfo> {
        simulateLoadDelay()
        val noteList: List<ListInfo>

        if(noteIds.isEmpty())
            noteList = lists
        else {
            noteList = ArrayList<ListInfo>(noteIds.size)
            for(noteId in noteIds)
                noteList.add(lists[noteId])
        }
        return noteList
    }

    fun loadNote(noteId: Int) = lists[noteId]

    fun isLastNoteId(noteId: Int) = noteId == lists.lastIndex

    private fun idOfNote(note: ListInfo) = lists.indexOf(note)

    fun noteIdsAsIntArray(notes: List<ListInfo>): IntArray {
        val noteIds = IntArray(notes.size)
        for(index in 0..notes.lastIndex)
            noteIds[index] = DataManager.idOfNote(notes[index])
        return noteIds
    }

    private fun simulateLoadDelay() {
        Thread.sleep(1000)
    }

    fun addNote(course: CourseInfo, noteTitle: String, noteText: String): Int {
        val note = ListInfo(course, noteTitle, noteText)
        lists.add(note)
        return lists.lastIndex
    }

    fun findNote(course: CourseInfo, noteTitle: String, noteText: String): ListInfo? {
        for (note in lists)
            if (course == note.course && noteTitle == note.title && noteText == note.text)
                return note
        return null
    }

    private fun initializeCourses() {
        var course = CourseInfo(title = "Java Fundamentals: The Java Language", courseId = "java_lang")
        courses[course.courseId] = course

        course = CourseInfo("android_intents", "Android Programming with Intents")
        courses[course.courseId] = course

        course = CourseInfo(courseId = "android_async", title = "Android Async Programming and Services")
        courses[course.courseId] = course


        course = CourseInfo("java_core", "Java Fundamentals: The Core Platform")
        courses[course.courseId] = course
    }

    fun initializeNotes() {
        var note = ListInfo(courses["android_intents"] as CourseInfo, "Dynamic intent resolution", "Wow intents allow components to be resolved at runtime")
        lists.add(note)

        note = ListInfo(courses["android_intents"] as CourseInfo, "Deleting intents", "PendingIntents are powerful, they delegate much more than just a component invocation")
        lists.add(note)

        note = ListInfo(courses["android_async"] as CourseInfo, "Service default threads", "Did you know that by default an Android Service will tie up the UI thread")
        lists.add(note)

        note = ListInfo(courses["java_lang"] as CourseInfo, "Parameters", "Leverage variable-length parameters list")
        lists.add(note)

        note = ListInfo(courses["java_lang"] as CourseInfo, "Anonymous classes", "Anonymous classes simplify implementing one use type")
        lists.add(note)

        note = ListInfo(courses["java_core"] as CourseInfo, "Compiler options", "The .jar options isn't compatible with the -cp option")
        lists.add(note)

        note = ListInfo(courses["java_core"] as CourseInfo, "Serialization", "Remember to include Serial/VersionUID to assure version compatibility")
        lists.add(note)
    }
}