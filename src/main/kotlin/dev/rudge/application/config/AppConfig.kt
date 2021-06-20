package dev.rudge.application.config

import dev.rudge.application.config.ModulesConfig.allModules
import dev.rudge.application.web.ErrorExceptionMapping
import dev.rudge.application.web.Router
import io.javalin.Javalin
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
}

