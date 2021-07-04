package com.example.health

import kotlin.jvm.internal.Intrinsics


class Data(str: String, str2: String) {
    val nutrientName: String
    val value: String

    operator fun component1(): String {
        return nutrientName
    }

    operator fun component2(): String {
        return value
    }

    fun copy(str: String, str2: String): Data {
        Intrinsics.checkNotNullParameter(str, "nutrientName")
        Intrinsics.checkNotNullParameter(str2, "value")
        return Data(str, str2)
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj) {
            return true
        }
        if (obj !is Data) {
            return false
        }
        val data = obj
        return Intrinsics.areEqual(nutrientName, data.nutrientName) && Intrinsics.areEqual(
            value,
            data.value
        )
    }

    override fun hashCode(): Int {
        val str = nutrientName
        var i = 0
        val hashCode = (str?.hashCode() ?: 0) * 31
        val str2 = value
        if (str2 != null) {
            i = str2.hashCode()
        }
        return hashCode + i
    }

    override fun toString(): String {
        return  nutrientName + ", value=" + value
    }

    companion object {
        fun  /* synthetic */`copy$default`(
            data: Data,
            str: String,
            str2: String,
            i: Int,
            obj: Any?
        ): Data {
            var str = str
            var str2 = str2
            if (i and 1 != 0) {
                str = data.nutrientName
            }
            if (i and 2 != 0) {
                str2 = data.value
            }
            return data.copy(str, str2)
        }
    }

    init {
        Intrinsics.checkNotNullParameter(str, "nutrientName")
        Intrinsics.checkNotNullParameter(str2, "value")
        nutrientName = str
        value = str2
    }
}