package com.nvr.costreamer.feeder

/**
 * Created by vinay.varma on 10/19/14.
 */
class FeederConfig extends Serializable

class zCoFeederConfig(val fileName: String) extends FeederConfig

class RabbitFeederConfig(val host: String, val exchange: String, val queue: String) extends FeederConfig
