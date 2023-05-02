val alphabet = mapOf(
    " " to 0,
    "а" to 1,
    "б" to 2,
    "в" to 3,
    "г" to 4,
    "д" to 5,
    "е" to 6,
    "ё" to 7,
    "ж" to 8,
    "з" to 9,
    "и" to 10,
    "й" to 11,
    "к" to 12,
    "л" to 13,
    "м" to 14,
    "н" to 15,
    "о" to 16,
    "п" to 17,
    "р" to 18,
    "с" to 19,
    "т" to 20,
    "у" to 21,
    "ф" to 22,
    "х" to 23,
    "ц" to 24,
    "ч" to 25,
    "ш" to 26,
    "щ" to 27,
    "ъ" to 28,
    "ь" to 29,
    "ы" to 30,
    "э" to 31,
    "ю" to 32,
    "я" to 33
)

fun main() {/*
    val mTest = 55207
    val tTest = 25236
    val arrTest = arrayOf(103, 107, 211, 430, 863, 1718, 3449, 6907, 13807, 27610)*/
    val myT = 503
    val myM = 1027
    val myArr = arrayOf(1, 2, 4, 8, 16, 32, 64, 128, 256, 512)
    while (true) {
        println("Введите \"шифр\" для шифрования или любой символ для дишифрования (end)")
        val read = readln()
        when (read) {
            "end" -> break
            "шифр" -> {
                println("Введите фразу")
                val phrase = readln().lowercase().split("")
                // val test = Cipher(mTest, tTest, arrTest, phrase = phrase)
                val myValues = Cipher(myM, myT, myArr, phrase = phrase)
                println("Моя версия: " + myValues.encode.joinToString(" "))
            }

            else -> {
                println("Введите суммы")
                val numbers = readln().split(" ").map { it.toInt() }
                // val test = Cipher(mTest, tTest, arrTest, numbers = numbers)
                val myValues = Cipher(myM, myT, myArr, numbers = numbers)
                println("Моя версия: " + myValues.decode.joinToString("").replace("[", "").replace("]", ""))
            }
        }
    }
}


class Cipher(
    private val m: Int,
    private val t: Int,
    private val vectorA: Array<Int>,
    val phrase: List<String> = emptyList(),
    val numbers: List<Int> = emptyList()
) {
    private val evclide = evclide()
    val q = q()
    val p = p()
    val u = u()
    private val bKey = vectorA.map { item -> (t * item) % m }           //Нахождение ключа B
    val encode = encode()
    val decode = decode()

    private fun decode(): List<String> {                                      //Расшифровка букв
        var text = mutableListOf<String>()
        for (number in numbers) {
            var temp = (u * number) % m
            var binary = ""
            for (a in vectorA.reversedArray()) {
                binary += if (temp / a != 1) 0 else 1
                temp %= a
            }
            val aKey = Integer.parseInt(binary.reversed(), 2)
            val letter = alphabet.filterValues { it == aKey }.keys
            text.add(letter.toString())
        }
        return text
    }

    private fun encode(): List<Int> {                                         //Шифрование букв
        var list = mutableListOf<Int>()
        val binary = mutableListOf<String>()
        for (item in phrase) {
            val bin = alphabet[item]
            if (bin != null) {
                binary.add(bin.toString(2).padStart(10, '0'))
            }
        }
        for (bin in binary) {
            var sum = 0
            for (b in bin.indices) {
                if (bin[b] == '1') sum += bKey[b]
            }
            list.add(sum)
        }

        return list
    }

    private fun u(): Int {                              //Нахождение U
        val sign = if (q.size % 2 == 0) -1 else 1
        val u = sign * q[q.size - 2]
        return if (u > 0) u else u + q.last()
    }

    private fun p(): List<Int> {                          //Нахождение Pn последовательности
        var pq = mutableListOf<Int>(0, 0, 1)
        for (index in evclide.indices) {
            pq.add(
                pq[index + 2] * evclide[index] + pq[index + 1]
            )
        }

        return pq
    }

    private fun q(): List<Int> {                          //Нахождение Qn последовательности
        var pq = mutableListOf<Int>(0, 1, 0)
        for (index in evclide.indices) {
            pq.add(
                pq[index + 2] * evclide[index] + pq[index + 1]
            )
        }

        return pq
    }


    private fun evclide(): List<Int> {                  //Нахождение мод по алгоритму Эвклида
        var delimoe = t
        var _delimoe = t
        var delitel = m
        var _delitel = m
        var array = mutableListOf<Int>()
        while (delitel != 0) {
            array.add(delimoe / delitel)
            delimoe = _delitel
            delitel = _delimoe % delitel
            _delimoe = delimoe
            _delitel = delitel
        }
        return array
    }

}