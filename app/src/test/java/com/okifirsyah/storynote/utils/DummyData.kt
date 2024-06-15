package com.okifirsyah.storynote.utils

import com.okifirsyah.data.local.model.StoryModel

object DummyData {

    fun emailDummy() = "nana12@example.com"
    fun passwordDummy() = "nana12"
    fun tokenDummy() =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLVA5aDhudDFKc2ZwakZaWVQiLCJpYXQiOjE3MTc0ODQ0NzN9.6KdyTfDNdQav7KTsFXNOie2LcyHhFybmnqRj21Piqmk"

    fun generateStoryDummy(): List<StoryModel> {
        val items = arrayListOf<StoryModel>()
        for (i in 0 until 20) {
            val story = StoryModel(
                id = "story-_KC1ISR_xc0_7m9f",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1717792870048_459c48fe6310e979a037.jpg",
                createdAt = 1717795610,
                name = "nana12",
                description = "Story Description",
                latitude = (-8.174986).toFloat(),
                longitude = (113.698420).toFloat(),
            )
            items.add(story)
        }
        return items
    }

    fun emptyListStoryDummy(): List<StoryModel> = emptyList()


}