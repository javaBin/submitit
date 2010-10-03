package subliftit.config

import no.java.submitit.config.{ConfigKey, ConfigValues}
import no.java.submitit.common.PropertyIOUtils

object Config {

  private object BaseConfig extends ConfigValues

  private var appConfig: collection.mutable.LinkedHashMap[ConfigKey, Option[String]] = _

  def initFromFile(fullFilePath: String) {
    if (appConfig != null) throw new Exception("App already configured from file. " +
            "This method should only be called in application initialisation.")
    val props = PropertyIOUtils.loadRessource(fullFilePath)
    appConfig = BaseConfig.mergeConfig(props)
  }

  def value(key: ConfigKey): Option[String] = {
    if (appConfig == null) throw new Exception("Application has not been initialised. Make sure to load config before calling the value method")
    appConfig.get(key).getOrElse(None)
  }

}
