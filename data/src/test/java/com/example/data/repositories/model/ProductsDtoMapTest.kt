package com.example.data.repositories.model

import com.example.data.model.*
import org.junit.Assert
import org.junit.Test

class ProductsDtoMapTest {

    @Test
    fun mapToDomain_invoked_mapTest() {
        // when
        val title = "Title"
        val price = 400L
        val thumbnail = "https://imageUri"
        val city = "city"
        val address = Address(cityName = city)
        val attributes = listOf(Attribute(name = "name", valueName = "valueName"))
        val result = Result(
            title = title,
            price = price,
            thumbnail = thumbnail,
            address = address,
            attributes = attributes,
            acceptsMercadopago = false,
            useThumbnailID = false,
            seller = Seller(carDealer = false, realEstateAgency = false)
        )
        val results = listOf(result)
        val serviceDTO = ProductsDto(results = results)
        // when
        val domainObject = serviceDTO.mapToDomain()
        // then
        Assert.assertEquals(1, domainObject.size)
        domainObject[0].let {
            Assert.assertEquals(title, it.title)
            Assert.assertEquals(price, it.price)
            Assert.assertEquals(thumbnail, it.thumbnailUri)
            Assert.assertEquals(city, it.cityLocation)
            Assert.assertEquals(
                attributes[0].name + "  :  " + attributes[0].valueName + System.lineSeparator(),
                it.attributes
            )
        }

    }
}