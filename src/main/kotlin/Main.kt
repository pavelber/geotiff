import org.geotools.coverage.grid.GridCoordinates2D
import org.geotools.gce.geotiff.GeoTiffReader
import java.io.File


// Data from https://eogdata.mines.edu/download_dnb_composites.html
fun main() {
    val path =
        "C:\\Users\\pavelb\\Downloads\\lightpollution\\SVDNB_npp_20191201-20191231_75N060W_vcmcfg_v10_c202001140900.avg_rade9h.tif"
    val reader = GeoTiffReader(File(path))
    val meta = reader.metadata

    val coordinateReferenceSystem = reader.coordinateReferenceSystem

    val coverage2D = reader.read(null)
    val crs = coverage2D.coordinateReferenceSystem2D
    val env = coverage2D.envelope2D

    println(env.minX)
    println(env.maxX)
    println(env.minY)
    println(env.maxY)
    println(env.contains(30.0, 33.0))
    val gg = coverage2D.getGridGeometry()
    val numBands = coverage2D.getNumSampleDimensions()
    val data = DoubleArray(numBands)
    val gridBounds = gg.getGridRange2D().getBounds()
    var y = gridBounds.y
    var ny = 0
    while (ny < gridBounds.height) {
        var x = gridBounds.x
        var nx = 0
        while (nx < gridBounds.width) {
            val gridCoord = GridCoordinates2D(x, y)
            val worldCoord = gg.gridToWorld(gridCoord)
            coverage2D.evaluate(gridCoord, data)
            // just writing to console in this example
            System.out.printf(
                "%.4f %.4f",
                worldCoord.getOrdinate(0), worldCoord.getOrdinate(1)
            )
            for (band in 0 until numBands) {
                System.out.printf(" %.4f", data[band])
            }
            println()
            x++
            nx++
        }
        y++
        ny++
    }

}


