import java.util.Optional
import java.util.OptionalDouble

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

fun <T, R> Sequence<T>.convert(transform: (T) -> R) : Sequence<R> {
    return sequence {
        val iter = this@convert.iterator()
        while(iter.hasNext()) {
            val item = iter.next()
            yield(transform(item))
        }
    }
    /*
    return object : Sequence<R> {
        override fun iterator(): Iterator<R> {
            return SequenceConvert(this@convert, transform)
        }
    }
    */
}

class SequenceConvert<T, R>(
    source: Sequence<T>,
    val transform: (T) -> R)
    : Iterator<R>
{
    val iterator = source.iterator()
    override fun hasNext() = iterator.hasNext()
    override fun next(): R = transform(iterator.next())
}

fun <T> Iterable<T>.where(predicate: (T) -> Boolean) : Iterable<T> {
    val destination = mutableListOf<T>()
    for (item in this)
        if(predicate(item))
        destination.add(item)
    return destination
}

fun <T> Sequence<T>.where(predicate: (T) -> Boolean) : Sequence<T> {
    return sequence {
        for (item in this@where)
            if(predicate(item))
                yield(item)
    }
    /*
    return object : Sequence<T> {
        override fun iterator(): Iterator<T> {
            return SequenceFilter(this@where, predicate)
        }
    }
    */
}

class SequenceFilter<T>(source: Sequence<T>, val predicate: (T) -> Boolean) : Iterator<T> {
    val iterator = source.iterator()
    var current = advance()

    private fun advance() : Optional<T> {
        while (iterator.hasNext()) {
            val item = iterator.next()
            if(predicate(item))
                return Optional.of(item)
        }
        return Optional.empty()
    }

    override fun hasNext() = current.isPresent

    override fun next(): T {
        val item = current.get()
        current = advance()
        return item
    }
}


fun foo() = sequence<Int> {
    yield(7)
    yield(6)
    yield(5)
}