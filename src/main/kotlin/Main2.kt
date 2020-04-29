import org.geotools.coverage.grid.GridCoverage2D
import org.geotools.gce.geotiff.GeoTiffReader
import org.geotools.geometry.DirectPosition2D
import org.opengis.geometry.DirectPosition
import java.io.File


const val CELL_SIZE = 0.025

const val LAT_START = 29.0
const val LAT_END = 34.0
const val LONG_START = 34.0
const val LONG_END = 36.0

// Data from https://eogdata.mines.edu/download_dnb_composites.html
fun main() {
    val path =
        "C:\\Users\\pavelb\\Downloads\\lightpollution\\SVDNB_npp_20191201-20191231_75N060W_vcmcfg_v10_c202001140900.avg_rade9h.tif"
    val reader = GeoTiffReader(File(path))

    val coverage2D = reader.read(null)
    val numBands = coverage2D.numSampleDimensions
    val step = CELL_SIZE
    val d = CELL_SIZE / 2
    var lat = LAT_START - step
    while (lat < LAT_END) {
        var long = LONG_START - step
        while (long < LONG_END) {
            val value = getValue(numBands, coverage2D, reader, long, lat)
            val value1 = getValue(numBands, coverage2D, reader, long + d, lat + d)
            val value2 = getValue(numBands, coverage2D, reader, long + d, lat - d)
            val value3 = getValue(numBands, coverage2D, reader, long - d, lat + d)
            val value4 = getValue(numBands, coverage2D, reader, long - d, lat - d)

            val v = (value+value1+value2+value3+value4)/5.0

            println("$lat,$long,$v")

            long += step
        }
        lat += step
    }


}

private fun getValue(
    numBands: Int,
    coverage2D: GridCoverage2D,
    reader: GeoTiffReader,
    long: Double,
    lat: Double
): Double {
    val data = DoubleArray(numBands)
    val v = coverage2D.evaluate(DirectPosition2D(reader.coordinateReferenceSystem, long, lat) as DirectPosition, data)
    val value = v[0]
    return value
}


