package com.clnk.livecommerce.api.product

import com.clnk.livecommerce.api.library.product.ProductSearchCondition
import mu.KotlinLogging
import org.junit.jupiter.api.Test

private val logger = KotlinLogging.logger {}
internal class SearchProductConditionTest {

    @Test
    fun enumTest() {
        logger.debug { "]-----] SearchProductConditionTest::enumTest CreateProductReq[-----[ ${ProductSearchCondition.DESCRIPTION}" }
        logger.debug { "]-----] SearchProductConditionTest::enumTest CreateProductReq[-----[ ${ProductSearchCondition.DESCRIPTION.searchKey}" }
    }
}