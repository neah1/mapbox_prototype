package com.example.mapboxprototype

enum class AggregateFunction {
    AVERAGE, MAX, MIN
}

sealed class Sensor {
    enum class SensorType {
        RADON, TEMP, CO2, PM25, VOC, HUMIDITY, LIGHT
    }

    sealed class VirtualSensorType(val name: String) : Sensor() {
        object VirusRisk : VirtualSensorType("virus_risk") {
            enum class Type {
                TRANSMISSION_RISK, STALE_AIR, TRANSMISSION_EFFICIENCY, SURVIVAL_RATE
            }
        }

        object Occupancy : VirtualSensorType("occupancy") {
            enum class Type {
                VENTILATION_RATE, OCCUPANTS, OCCUPANTS_LOWER, OCCUPANTS_UPPER, ACH, PRESENCE
            }
        }
    }
}

enum class SensorType {
    RADON,
    TEMP,
    CO2,
    PM25,
    VOC,
    HUMIDITY,
    LIGHT,
    VIRUS_RISK,
    OCCUPANCY
}

enum class VirtualSensorType {
    VIRUS_RISK {
        override val subtypes = VirusRiskSubtype.entries
    },
    OCCUPANCY {
        override val subtypes = OccupancySubtype.entries
    };

    abstract val subtypes: List<Enum<*>>

    enum class VirusRiskSubtype {
        TRANSMISSION_RISK,
        STALE_AIR,
        TRANSMISSION_EFFICIENCY,
        SURVIVAL_RATE
    }

    enum class OccupancySubtype {
        VENTILATION_RATE,
        OCCUPANTS,
        OCCUPANTS_LOWER,
        OCCUPANTS_UPPER,
        ACH,
        PRESENCE
    }
}

val sensorThresholds: Map<SensorType, List<Int>> = mapOf(
    SensorType.RADON to listOf(100, 150),
    SensorType.TEMP to listOf(18, 25),
    SensorType.CO2 to listOf(800, 1000),
    SensorType.PM25 to listOf(10, 25),
    SensorType.VOC to listOf(250, 2000),
    SensorType.HUMIDITY to listOf(25, 30, 60, 70),
    SensorType.VIRUS_RISK to listOf(1, 4, 7, 10)
)

val occupancyThresholds: Map<VirtualSensorType.OccupancySubtype, List<Int>> = mapOf(
    VirtualSensorType.OccupancySubtype.VENTILATION_RATE to listOf(0, 3, 5, 10, 15, 30),
    VirtualSensorType.OccupancySubtype.OCCUPANTS to listOf(0, 5, 10, 15, 20, 25, 50, 100, 500),
    VirtualSensorType.OccupancySubtype.OCCUPANTS_LOWER to listOf(
        0,
        5,
        10,
        15,
        20,
        25,
        50,
        100,
        500
    ),
    VirtualSensorType.OccupancySubtype.OCCUPANTS_UPPER to listOf(
        0,
        5,
        10,
        15,
        20,
        25,
        50,
        100,
        500
    ),
    VirtualSensorType.OccupancySubtype.ACH to listOf(0, 3, 5, 10, 15, 30),
    VirtualSensorType.OccupancySubtype.PRESENCE to listOf(0, 5, 15, 30, 45, 60)
)

val sensorUnits: Map<SensorType, String> = mapOf(
    SensorType.RADON to "Bq/m³",
    SensorType.TEMP to "C",
    SensorType.CO2 to "ppm",
    SensorType.PM25 to "µg/m³",
    SensorType.VOC to "ppb",
    SensorType.HUMIDITY to "%",
    SensorType.VIRUS_RISK to "score"
)

val occupancyUnits: Map<VirtualSensorType.OccupancySubtype, String> = mapOf(
    VirtualSensorType.OccupancySubtype.VENTILATION_RATE to "m³/h",
    VirtualSensorType.OccupancySubtype.OCCUPANTS to "occupants",
    VirtualSensorType.OccupancySubtype.OCCUPANTS_LOWER to "occupants",
    VirtualSensorType.OccupancySubtype.OCCUPANTS_UPPER to "occupants",
    VirtualSensorType.OccupancySubtype.ACH to "ACH",
    VirtualSensorType.OccupancySubtype.PRESENCE to "minutes"
)

data class SensorData(
    val serialnumber: String,
    val voc_yellow_hours: Int,
    val voc_red_hours: Int,
    val voc_hours: Int,
    val co2_yellow_hours: Int,
    val co2_red_hours: Int,
    val co2_hours: Int,
    val pm25_yellow_hours: Int,
    val pm25_red_hours: Int,
    val pm25_hours: Int,
    val humidity_low_red_hours: Int,
    val humidity_50_hours: Int,
    val humidity_high_red_hours: Int,
    val humidity_hours: Int,
    val radon_red_hours: Int,
    val radon_500_hours: Int,
    val radon_1000_hours: Int,
    val radon_hours: Int,
    val radon_average: Double,
    val temp_average: Double,
    val co2_average: Double,
    val pm25_average: Double,
    val voc_average: Double,
    val humidity_average: Double,
    val light_average: Double,
    val radon_min: Double,
    val temp_min: Double,
    val co2_min: Double,
    val pm25_min: Double,
    val voc_min: Double,
    val humidity_min: Double,
    val light_min: Double,
    val radon_max: Double,
    val temp_max: Double,
    val co2_max: Double,
    val pm25_max: Double,
    val voc_max: Double,
    val humidity_max: Double,
    val light_max: Double,
    val sum_handwaves: Int,
    val year: Int,
    val vs_virusrisk_transmissionrisk_average: Double,
    val vs_virusrisk_staleair_average: Double,
    val vs_virusrisk_transmissionefficiency_average: Double,
    val vs_virusrisk_survivalrate_average: Double,
    val vs_virusrisk_transmissionrisk_min: Double,
    val vs_virusrisk_staleair_min: Double,
    val vs_virusrisk_transmissionefficiency_min: Double,
    val vs_virusrisk_survivalrate_min: Double,
    val vs_virusrisk_transmissionrisk_max: Double,
    val vs_virusrisk_staleair_max: Double,
    val vs_virusrisk_transmissionefficiency_max: Double,
    val vs_virusrisk_survivalrate_max: Double,
    val vs_occupancy_ventilationrate_average: Double,
    val vs_occupancy_occupants_average: Double,
    val vs_occupancy_occupantsupper_average: Double,
    val vs_occupancy_occupantslower_average: Double,
    val vs_occupancy_ach_average: Double,
    val vs_occupancy_presence_average: Double,
    val vs_occupancy_ventilationrate_min: Double,
    val vs_occupancy_occupants_min: Double,
    val vs_occupancy_occupantsupper_min: Double,
    val vs_occupancy_occupantslower_min: Double,
    val vs_occupancy_ach_min: Double,
    val vs_occupancy_presence_min: Double,
    val vs_occupancy_ventilationrate_max: Double,
    val vs_occupancy_occupants_max: Double,
    val vs_occupancy_occupantsupper_max: Double,
    val vs_occupancy_occupantslower_max: Double,
    val vs_occupancy_ach_max: Double,
    val vs_occupancy_presence_max: Double,
    val vs_occupancy_ventilationrate_sum: Double,
    val vs_occupancy_occupants_sum: Double,
    val vs_occupancy_occupantsupper_sum: Double,
    val vs_occupancy_occupantslower_sum: Double,
    val vs_occupancy_ach_sum: Double,
    val vs_occupancy_presence_sum: Double,
    val geohash: String,
    val lat: Double,
    val long: Double,
    val week: String
)

fun mapToSensorData(values: List<String>): SensorData {
    return SensorData(
        serialnumber = values[0],
        voc_yellow_hours = values[1].toInt(),
        voc_red_hours = values[2].toInt(),
        voc_hours = values[3].toInt(),
        co2_yellow_hours = values[4].toInt(),
        co2_red_hours = values[5].toInt(),
        co2_hours = values[6].toInt(),
        pm25_yellow_hours = values[7].toInt(),
        pm25_red_hours = values[8].toInt(),
        pm25_hours = values[9].toInt(),
        humidity_low_red_hours = values[10].toInt(),
        humidity_50_hours = values[11].toInt(),
        humidity_high_red_hours = values[12].toInt(),
        humidity_hours = values[13].toInt(),
        radon_red_hours = values[14].toInt(),
        radon_500_hours = values[15].toInt(),
        radon_1000_hours = values[16].toInt(),
        radon_hours = values[17].toInt(),
        radon_average = values[18].toDouble(),
        temp_average = values[19].toDouble(),
        co2_average = values[20].toDouble(),
        pm25_average = values[21].toDouble(),
        voc_average = values[22].toDouble(),
        humidity_average = values[23].toDouble(),
        light_average = values[24].toDouble(),
        radon_min = values[25].toDouble(),
        temp_min = values[26].toDouble(),
        co2_min = values[27].toDouble(),
        pm25_min = values[28].toDouble(),
        voc_min = values[29].toDouble(),
        humidity_min = values[30].toDouble(),
        light_min = values[31].toDouble(),
        radon_max = values[32].toDouble(),
        temp_max = values[33].toDouble(),
        co2_max = values[34].toDouble(),
        pm25_max = values[35].toDouble(),
        voc_max = values[36].toDouble(),
        humidity_max = values[37].toDouble(),
        light_max = values[38].toDouble(),
        sum_handwaves = values[39].toInt(),
        year = values[40].toInt(),
        vs_virusrisk_transmissionrisk_average = values[41].toDouble(),
        vs_virusrisk_staleair_average = values[42].toDouble(),
        vs_virusrisk_transmissionefficiency_average = values[43].toDouble(),
        vs_virusrisk_survivalrate_average = values[44].toDouble(),
        vs_virusrisk_transmissionrisk_min = values[45].toDouble(),
        vs_virusrisk_staleair_min = values[46].toDouble(),
        vs_virusrisk_transmissionefficiency_min = values[47].toDouble(),
        vs_virusrisk_survivalrate_min = values[48].toDouble(),
        vs_virusrisk_transmissionrisk_max = values[49].toDouble(),
        vs_virusrisk_staleair_max = values[50].toDouble(),
        vs_virusrisk_transmissionefficiency_max = values[51].toDouble(),
        vs_virusrisk_survivalrate_max = values[52].toDouble(),
        vs_occupancy_ventilationrate_average = values[53].toDouble(),
        vs_occupancy_occupants_average = values[54].toDouble(),
        vs_occupancy_occupantsupper_average = values[55].toDouble(),
        vs_occupancy_occupantslower_average = values[56].toDouble(),
        vs_occupancy_ach_average = values[57].toDouble(),
        vs_occupancy_presence_average = values[58].toDouble(),
        vs_occupancy_ventilationrate_min = values[59].toDouble(),
        vs_occupancy_occupants_min = values[60].toDouble(),
        vs_occupancy_occupantsupper_min = values[61].toDouble(),
        vs_occupancy_occupantslower_min = values[62].toDouble(),
        vs_occupancy_ach_min = values[63].toDouble(),
        vs_occupancy_presence_min = values[64].toDouble(),
        vs_occupancy_ventilationrate_max = values[65].toDouble(),
        vs_occupancy_occupants_max = values[66].toDouble(),
        vs_occupancy_occupantsupper_max = values[67].toDouble(),
        vs_occupancy_occupantslower_max = values[68].toDouble(),
        vs_occupancy_ach_max = values[69].toDouble(),
        vs_occupancy_presence_max = values[70].toDouble(),
        vs_occupancy_ventilationrate_sum = values[71].toDouble(),
        vs_occupancy_occupants_sum = values[72].toDouble(),
        vs_occupancy_occupantsupper_sum = values[73].toDouble(),
        vs_occupancy_occupantslower_sum = values[74].toDouble(),
        vs_occupancy_ach_sum = values[75].toDouble(),
        vs_occupancy_presence_sum = values[76].toDouble(),
        geohash = values[77],
        lat = values[78].toDouble(),
        long = values[79].toDouble(),
        week = values[80]
    )
}

fun getPropertyGetterMap(row: SensorData): Map<String, Double?> {
    return mapOf(
        "radon_max" to row.radon_max,
        "temp_max" to row.temp_max,
        "co2_max" to row.co2_max,
        "pm25_max" to row.pm25_max,
        "voc_max" to row.voc_max,
        "humidity_max" to row.humidity_max,
        "light_max" to row.light_max,
        "radon_min" to row.radon_min,
        "temp_min" to row.temp_min,
        "co2_min" to row.co2_min,
        "pm25_min" to row.pm25_min,
        "voc_min" to row.voc_min,
        "humidity_min" to row.humidity_min,
        "light_min" to row.light_min,
        "radon_average" to row.radon_average,
        "temp_average" to row.temp_average,
        "co2_average" to row.co2_average,
        "pm25_average" to row.pm25_average,
        "voc_average" to row.voc_average,
        "humidity_average" to row.humidity_average,
        "light_average" to row.light_average,
        "vs_virusrisk_transmissionrisk_average" to row.vs_virusrisk_transmissionrisk_average,
        "vs_virusrisk_staleair_average" to row.vs_virusrisk_staleair_average,
        "vs_virusrisk_transmissionefficiency_average" to row.vs_virusrisk_transmissionefficiency_average,
        "vs_virusrisk_survivalrate_average" to row.vs_virusrisk_survivalrate_average,
        "vs_virusrisk_transmissionrisk_min" to row.vs_virusrisk_transmissionrisk_min,
        "vs_virusrisk_staleair_min" to row.vs_virusrisk_staleair_min,
        "vs_virusrisk_transmissionefficiency_min" to row.vs_virusrisk_transmissionefficiency_min,
        "vs_virusrisk_survivalrate_min" to row.vs_virusrisk_survivalrate_min,
        "vs_virusrisk_transmissionrisk_max" to row.vs_virusrisk_transmissionrisk_max,
        "vs_virusrisk_staleair_max" to row.vs_virusrisk_staleair_max,
        "vs_virusrisk_transmissionefficiency_max" to row.vs_virusrisk_transmissionefficiency_max,
        "vs_virusrisk_survivalrate_max" to row.vs_virusrisk_survivalrate_max,
        "vs_occupancy_ventilationrate_average" to row.vs_occupancy_ventilationrate_average,
        "vs_occupancy_occupants_average" to row.vs_occupancy_occupants_average,
        "vs_occupancy_occupantsupper_average" to row.vs_occupancy_occupantsupper_average,
        "vs_occupancy_occupantslower_average" to row.vs_occupancy_occupantslower_average,
        "vs_occupancy_ach_average" to row.vs_occupancy_ach_average,
        "vs_occupancy_presence_average" to row.vs_occupancy_presence_average,
        "vs_occupancy_ventilationrate_min" to row.vs_occupancy_ventilationrate_min,
        "vs_occupancy_occupants_min" to row.vs_occupancy_occupants_min,
        "vs_occupancy_occupantsupper_min" to row.vs_occupancy_occupantsupper_min,
        "vs_occupancy_occupantslower_min" to row.vs_occupancy_occupantslower_min,
        "vs_occupancy_ach_min" to row.vs_occupancy_ach_min,
        "vs_occupancy_presence_min" to row.vs_occupancy_presence_min,
        "vs_occupancy_ventilationrate_max" to row.vs_occupancy_ventilationrate_max,
        "vs_occupancy_occupants_max" to row.vs_occupancy_occupants_max,
        "vs_occupancy_occupantsupper_max" to row.vs_occupancy_occupantsupper_max,
        "vs_occupancy_occupantslower_max" to row.vs_occupancy_occupantslower_max,
        "vs_occupancy_ach_max" to row.vs_occupancy_ach_max,
        "vs_occupancy_presence_max" to row.vs_occupancy_presence_max,
        "vs_occupancy_ventilationrate_sum" to row.vs_occupancy_ventilationrate_sum,
        "vs_occupancy_occupants_sum" to row.vs_occupancy_occupants_sum,
        "vs_occupancy_occupantsupper_sum" to row.vs_occupancy_occupantsupper_sum,
        "vs_occupancy_occupantslower_sum" to row.vs_occupancy_occupantslower_sum,
        "vs_occupancy_ach_sum" to row.vs_occupancy_ach_sum,
        "vs_occupancy_presence_sum" to row.vs_occupancy_presence_sum,
    )
}
