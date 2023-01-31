package com.polarbookshop.catalogservice.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Created by IntelliJ IDEA.
 * @author Mario Arias
 * Date: 30/1/23
 * Time: 6:00 pm
 */
@ConfigurationProperties(prefix = "polar")
class PolarProperties {
    /**
     * A message to welcome users
     */
    lateinit var greeting: String
}
