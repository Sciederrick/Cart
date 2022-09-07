package com.israteneda.notekeeper

object DataManager {
    val lists = HashMap<String, ListInfo>()
    val listItems = ArrayList<ListItem>()

    init {
        initializeListItems()
        initializeLists()
    }

    fun loadLists(vararg listIds: Int): List<ListInfo> {
        simulateLoadDelay()
        val list: List<ListInfo>

        if(listIds.isEmpty())
            list = convertHashmapValuesToArrayList()
        else {
            list = ArrayList(listIds.size)
            val lists = convertHashmapValuesToArrayList()

            for(listId in listIds)
                lists[listId]?.let { list.add(it) }
        }
        return list
    }

    private fun convertHashmapValuesToArrayList() :ArrayList<ListInfo>{
        val tmp = ArrayList<ListInfo>()
        lists.values.forEach { tmp.add(it) }
        return tmp
    }

//    fun loadNote(listId: Int) = lists[listId]

//    fun isLastListId(listId: Int) = listId == lists.lastIndex

    private fun idOfList(list: ListInfo) = lists.values.indexOf(list)

    fun listIdsAsIntArray(list: List<ListInfo>): IntArray {
        val listIds = IntArray(list.size)
        for(index in 0..list.lastIndex)
            listIds[index] = idOfList(list[index])
        return listIds
    }

    private fun simulateLoadDelay() {
        Thread.sleep(1000)
    }

//    fun addList(id: String, listTitle: String, listTags: List<String>? = null): Int {
//        val list = ListInfo(id, listTitle, listTags)
//        lists.add(list)
//        return lists.lastIndex
//    }

//    fun findList(id: String): ListInfo? {
//        for (list in lists)
//            if (id == list.id)
//                return list
//        return null
//    }

    private fun initializeLists() {
        var list = ListInfo(listId="construction_project", title="Construction Project", tags=listOf("construction", "brick laying"))
        lists[list.listId] = list

        list = ListInfo(listId="android_development", title="Android Development", tags=listOf("work"))
        lists[list.listId] = list

        list = ListInfo(listId="shopping", title="Shopping")
        lists[list.listId] = list
    }

    private fun initializeListItems() {
        var listItem = ListItem(list=lists["construction_project"], title="Site Clearing", description="Clear land")
        listItems.add(listItem)

        listItem = ListItem(list=lists["construction_project"], title="Foundation", description="Pour Concrete, lay nylon and bricks")
        listItems.add(listItem)

        listItem = ListItem(list=lists["construction_project"], title="Plinth beam & Slab", description="Not sure how to do it yet")
        listItems.add(listItem)

        listItem = ListItem(list=lists["construction_project"], title="Superstructure", description="Leave this to the experts")
        listItems.add(listItem)

        listItem = ListItem(list=lists["construction_project"], title="Brick Laying", description="Hire a company to do this")
        listItems.add(listItem)

        listItem = ListItem(list=lists["android_development"], title="Android Programming with Intents", description="Learn to open external processes")
        listItems.add(listItem)

        listItem = ListItem(list=lists["android_development"], title="Android Themes & Styles", description="Modularize styles for flexibility")
        listItems.add(listItem)

        listItem = ListItem(list=lists["shopping"], title="Formal Rubber Shoes", description="For work")
        listItems.add(listItem)

        listItem = ListItem(list=lists["shopping"], title="Casual Shorts", description="For home wear")
        listItems.add(listItem)

        listItem = ListItem(list=lists["shopping"], title="Chinese Collar Shirts", description="For the office")
        listItems.add(listItem)
    }

}