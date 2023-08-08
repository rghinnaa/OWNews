package com.akb.ownews.utils

object Const {
//    const val API_KEY = "ab1dec080fe444888485072081e5a13c"
    const val API_KEY = "2824ff20bc7f4116b84dd7807f6b3768"
    const val NEWS_EVERYTHING = "everything"
    const val NEWS_TOP_HEADLINE = "top-headlines"
    const val NEWS_SOURCE = "top-headlines/sources"

    const val textPlain = "text/plain"

    const val ON_BOARDING_DATA = "ON_BOARDING_DATA"

    object SortBy {
        const val RELEVANCY = "relevancy"
        const val POPULARITY = "popularity"
        const val PUBLISHED_AT = "publishedAt"
    }

    object Database {
        const val DATABASE_NAME = "NEWS_DATABASE"
    }

    object Entity {
        const val BOOKMARK = "bookmark_entity"
    }

    object SavedStateHandle {
        const val CATEGORY_SAVED_STATE_KEY = "category_saved_state_key"
        const val SOURCE_SAVED_STATE_KEY = "source_saved_state_key"
    }
}