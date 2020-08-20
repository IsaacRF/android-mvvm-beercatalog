package com.isaacrf.android_base_app.features.beer_list.services

import com.google.gson.*
import com.isaacrf.android_base_app.features.beer_list.models.Beer
import java.lang.reflect.Type


class BeerDeserializer : JsonDeserializer<Beer?> {
    @Throws(JsonParseException::class)
    override fun deserialize(
        paramJsonElement: JsonElement, paramType: Type?,
        paramJsonDeserializationContext: JsonDeserializationContext?
    ): Beer {
        val beer: Beer = Gson().fromJson(paramJsonElement.asJsonObject, Beer::class.java)
        beer.available = true
        return beer
    }
}