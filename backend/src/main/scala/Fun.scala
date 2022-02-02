package funstack.backend

object Fun {
  val config = Config.load()

  val authOption = config.cognitoUserPoolId.map(new Auth(_))

  val wsOption =
    config.eventsSnsTopic.map(new WsOperationsAWS(_))
     .orElse(config.devEnvironment.map(env => new WsOperationsDev(env.send_subscription)))
     .map(new Ws(_))

  case class MissingModuleException(name: String) extends Exception(s"Missing module: $name")

  lazy val auth = authOption.getOrElse(throw MissingModuleException("auth"))
  lazy val ws   = wsOption.getOrElse(throw MissingModuleException("ws"))
}
