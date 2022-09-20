package com.derrick.cart


data class ListItem(var list: ListInfo? = null,
                    var title: String? = null,
                    var description: String? = null,
                    var quantity: Float? = null,
                    var price: Double? = null,
                    var isDone: Boolean = false,
                    var hasSublist: Boolean = false)

data class ListInfo(val listId: String,
                    var title: String? = null,
                    val tags: List<String>? = null,
) {
    override fun toString(): String {
        return title?:""
    }
}