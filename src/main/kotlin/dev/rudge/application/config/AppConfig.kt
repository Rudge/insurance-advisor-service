package dev.rudge.application.config

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import dev.rudge.application.config.ModulesConfig.allModules
import dev.rudge.application.web.ErrorExceptionMapping
import dev.rudge.application.web.Router
import io.javalin.Javalin
import io.javalin.plugin.json.JavalinJackson
import java.text.SimpleDateFormat
import org.eclipse.jetty.server.Server
import org.koin.core.KoinProperties
import org.koin.standalone.KoinComponent
import org.koin.standalone.StandAloneContext
import org.koin.standalone.getProperty
import org.koin.standalone.inject

class AppConfig : KoinComponent {
    private val router: Router by inject()

    fun setup(): Javalin {
        StandAloneContext.startKoin(
            allModules,
            KoinProperties(true, true)
        )
        this.configureMapper()
        val app = Javalin.create { config ->
            config.apply {
                enableWebjars()
                enableCorsForAllOrigins()
                contextPath = getProperty("context")
                addStaticFiles("/swagger")
                addSinglePageRoot("", "/swagger/swagger-ui.html")
                server {
                    Server(getProperty("server_port") as Int)
                }
            }
        }.events {
            it.serverStopping {
                StandAloneContext.stopKoin()
            }
        }
        router.register(app)
        ErrorExceptionMapping.register(app)
        return app
    }

    private fun configureMapper() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        JavalinJackson.configure(
            jacksonObjectMapper()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .setDateFormat(dateFormat)
                .configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, true)
        )
    }
}
