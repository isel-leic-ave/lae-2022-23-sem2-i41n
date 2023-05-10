/**
 * Receiver - Iterable<T>
 * T - type parameter (i.e. generic parameter)
 */
fun <T, R> Iterable<T>.convert(transform: (T) -> R) : Iterable<R> {
    val destination = mutableListOf<R>()
    /*
    for (item in this)
        destination.add(transform(item))
    */
    val iter = this.iterator()
    while(iter.hasNext()) {
        val item = iter.next()
        destination.add(transform(item))
    }
    return destination
}

fun <T> Iterable<T>.where(predicate: (T) -> Boolean) : Iterable<T> {
    val destination = mutableListOf<T>()
    for (item in this)
        if(predicate(item))
        destination.add(item)
    return destination
}