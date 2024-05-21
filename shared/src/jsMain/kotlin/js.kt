//Skeleton (dovrà implementare le interfaccie del common)
fun main(){

}

class JsPlatform : Platform {
    override val name: String = "JavaScript"
}

actual fun getPlatform() : Platform = JsPlatform()