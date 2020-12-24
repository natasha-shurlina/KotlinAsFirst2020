@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence")

package lesson7.task1

import kotlinx.html.attributes.stringSetDecode
import ru.spbstu.kotlin.typeclass.classes.Monoid.Companion.plus
import java.io.File
import kotlin.collections.ArrayList
import kotlin.math.max

// Урок 7: работа с файлами
// Урок интегральный, поэтому его задачи имеют сильно увеличенную стоимость
// Максимальное количество баллов = 55
// Рекомендуемое количество баллов = 20
// Вместе с предыдущими уроками (пять лучших, 3-7) = 55/103

/**
 * Пример
 *
 * Во входном файле с именем inputName содержится некоторый текст.
 * Вывести его в выходной файл с именем outputName, выровняв по левому краю,
 * чтобы длина каждой строки не превосходила lineLength.
 * Слова в слишком длинных строках следует переносить на следующую строку.
 * Слишком короткие строки следует дополнять словами из следующей строки.
 * Пустые строки во входном файле обозначают конец абзаца,
 * их следует сохранить и в выходном файле
 */
fun alignFile(inputName: String, lineLength: Int, outputName: String) {
    val writer = File(outputName).bufferedWriter()
    var currentLineLength = 0
    fun append(word: String) {
        if (currentLineLength > 0) {
            if (word.length + currentLineLength >= lineLength) {
                writer.newLine()
                currentLineLength = 0
            } else {
                writer.write(" ")
                currentLineLength++
            }
        }
        writer.write(word)
        currentLineLength += word.length
    }
    for (line in File(inputName).readLines()) {
        if (line.isEmpty()) {
            writer.newLine()
            if (currentLineLength > 0) {
                writer.newLine()
                currentLineLength = 0
            }
            continue
        }
        for (word in line.split(Regex("\\s+"))) {
            append(word)
        }
    }
    writer.close()
}

/**
 * Простая (8 баллов)
 *
 * Во входном файле с именем inputName содержится некоторый текст.
 * Некоторые его строки помечены на удаление первым символом _ (подчёркивание).
 * Перенести в выходной файл с именем outputName все строки входного файла, убрав при этом помеченные на удаление.
 * Все остальные строки должны быть перенесены без изменений, включая пустые строки.
 * Подчёркивание в середине и/или в конце строк значения не имеет.
 */
fun deleteMarked(inputName: String, outputName: String) {
    val writer = File(outputName).bufferedWriter()
    File(inputName).forEachLine { line ->
        if (line.startsWith("_")) return@forEachLine
        else {
            writer.write(line)
            writer.newLine()
        }
    }
    writer.close()
}

/**
 * Средняя (14 баллов)
 *
 * Во входном файле с именем inputName содержится некоторый текст.
 * На вход подаётся список строк substrings.
 * Вернуть ассоциативный массив с числом вхождений каждой из строк в текст.
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 *
 */
fun countSubstrings(inputName: String, substrings: List<String>): Map<String, Int> = TODO()


/**
 * Средняя (12 баллов)
 *
 * В русском языке, как правило, после букв Ж, Ч, Ш, Щ пишется И, А, У, а не Ы, Я, Ю.
 * Во входном файле с именем inputName содержится некоторый текст на русском языке.
 * Проверить текст во входном файле на соблюдение данного правила и вывести в выходной
 * файл outputName текст с исправленными ошибками.
 *
 * Регистр заменённых букв следует сохранять.
 *
 * Исключения (жюри, брошюра, парашют) в рамках данного задания обрабатывать не нужно
 *
 */
fun sibilants(inputName: String, outputName: String) {
    val writer = File(outputName).bufferedWriter()
    val correction = mapOf('ы' to 'и', 'я' to 'а', 'ю' to 'у', 'Ы' to 'И', 'Я' to 'А', 'Ю' to 'У')
    val letters = setOf('ж', 'ч', 'ш', 'щ', 'Ж', 'Ч', 'Ш', 'Щ')
    File(inputName).forEachLine {
        val lineWritten = StringBuilder(it)
        for (i in 0 until it.length - 1) {
            if ((it[i] in letters) && (it[i + 1] in correction))
                lineWritten[i + 1] = correction[it[i + 1]]!!
        }
        writer.write(lineWritten.toString())
        writer.newLine()
    }
    writer.close()
}


/**
 * Средняя (15 баллов)
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 * Вывести его в выходной файл с именем outputName, выровняв по центру
 * относительно самой длинной строки.
 *
 * Выравнивание следует производить путём добавления пробелов в начало строки.
 *
 *
 * Следующие правила должны быть выполнены:
 * 1) Пробелы в начале и в конце всех строк не следует сохранять.
 * 2) В случае невозможности выравнивания строго по центру, строка должна быть сдвинута в ЛЕВУЮ сторону
 * 3) Пустые строки не являются особым случаем, их тоже следует выравнивать
 * 4) Число строк в выходном файле должно быть равно числу строк во входном (в т. ч. пустых)
 *
 */
fun centerFile(inputName: String, outputName: String) {
    val writer = File(outputName).bufferedWriter()
    var maxLineLength = 0
    File(inputName).forEachLine { line ->
        if (line.trim().length > maxLineLength) maxLineLength = line.trim().length
    }
    fun centered(a: String, b: Int): String = " ".repeat((b - a.length) / 2) + a
    File(inputName).forEachLine { line ->
        writer.write(centered(line.trim(), maxLineLength))
        writer.newLine()
    }
    writer.close()
}

/**
 * Сложная (20 баллов)
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 * Вывести его в выходной файл с именем outputName, выровняв по левому и правому краю относительно
 * самой длинной строки.
 * Выравнивание производить, вставляя дополнительные пробелы между словами: равномерно по всей строке
 *
 * Слова внутри строки отделяются друг от друга одним или более пробелом.
 *
 * Следующие правила должны быть выполнены:
 * 1) Каждая строка входного и выходного файла не должна начинаться или заканчиваться пробелом.
 * 2) Пустые строки или строки из пробелов трансформируются в пустые строки без пробелов.
 * 3) Строки из одного слова выводятся без пробелов.
 * 4) Число строк в выходном файле должно быть равно числу строк во входном (в т. ч. пустых).
 *
 * Равномерность определяется следующими формальными правилами:
 * 5) Число пробелов между каждыми двумя парами соседних слов не должно отличаться более, чем на 1.
 * 6) Число пробелов между более левой парой соседних слов должно быть больше или равно числу пробелов
 *    между более правой парой соседних слов.
 *
 * Следует учесть, что входной файл может содержать последовательности из нескольких пробелов  между слвоами. Такие
 * последовательности следует учитывать при выравнивании и при необходимости избавляться от лишних пробелов.
 * Из этого следуют следующие правила:
 * 7) В самой длинной строке каждая пара соседних слов должна быть отделена В ТОЧНОСТИ одним пробелом
 * 8) Если входной файл удовлетворяет требованиям 1-7, то он должен быть в точности идентичен выходному файлу
 */
fun alignFileByWidth(inputName: String, outputName: String) {
    val writer = File(outputName).bufferedWriter()
    var maxLineLength = 0
    File(inputName).forEachLine {
        var lengthLine = 0
        val withoutExtraSpaces = it.trim().split(Regex("""\s+"""))
        for (el in withoutExtraSpaces.indices) {
            lengthLine += withoutExtraSpaces[el].length + 1
        }
        lengthLine--
        if (lengthLine > maxLineLength) maxLineLength = lengthLine
    }
    File(inputName).forEachLine { line ->
        var result = StringBuilder(line.trim())
        val word = result.toString().split(Regex("""\s+"""))
        if (word.size == 1) {
            writer.write(word[0])
            writer.newLine()
            return@forEachLine
        }
        result = StringBuilder(word.joinToString(" "))
        var n = 0
        var ind = word[n].length
        var repetitiveSpaces = 0
        var countSpaces = 0
        while (result.length != maxLineLength) {
            result.insert(ind, " ")
            countSpaces++
            n++
            ind += word[n].length + repetitiveSpaces + 2
            if (countSpaces % (word.size - 1) == 0) {
                n = 0
                repetitiveSpaces++
                ind = word[n].length
            }
        }
        writer.write(result.toString())
        writer.newLine()
    }
    writer.close()
}


/**
 * Средняя (14 баллов)
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 *
 * Вернуть ассоциативный массив, содержащий 20 наиболее часто встречающихся слов с их количеством.
 * Если в тексте менее 20 различных слов, вернуть все слова.
 * Вернуть ассоциативный массив с числом слов больше 20, если 20-е, 21-е, ..., последнее слова
 * имеют одинаковое количество вхождений (см. также тест файла input/onegin.txt).
 *
 * Словом считается непрерывная последовательность из букв (кириллических,
 * либо латинских, без знаков препинания и цифр).
 * Цифры, пробелы, знаки препинания считаются разделителями слов:
 * Привет, привет42, привет!!! -привет?!
 * ^ В этой строчке слово привет встречается 4 раза.
 *
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 * Ключи в ассоциативном массиве должны быть в нижнем регистре.
 *
 */
fun top20Words(inputName: String): Map<String, Int> = TODO()

/**
 * Средняя (14 баллов)
 *
 * Реализовать транслитерацию текста из входного файла в выходной файл посредством динамически задаваемых правил.

 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 *
 * В ассоциативном массиве dictionary содержится словарь, в котором некоторым символам
 * ставится в соответствие строчка из символов, например
 * mapOf('з' to "zz", 'р' to "r", 'д' to "d", 'й' to "y", 'М' to "m", 'и' to "yy", '!' to "!!!")
 *
 * Необходимо вывести в итоговый файл с именем outputName
 * содержимое текста с заменой всех символов из словаря на соответствующие им строки.
 *
 * При этом регистр символов в словаре должен игнорироваться,
 * но при выводе символ в верхнем регистре отображается в строку, начинающуюся с символа в верхнем регистре.
 *
 * Пример.
 * Входной текст: Здравствуй, мир!
 *
 * заменяется на
 *
 * Выходной текст: Zzdrавствуy, mир!!!
 *
 * Пример 2.
 *
 * Входной текст: Здравствуй, мир!
 * Словарь: mapOf('з' to "zZ", 'р' to "r", 'д' to "d", 'й' to "y", 'М' to "m", 'и' to "YY", '!' to "!!!")
 *
 * заменяется на
 *
 * Выходной текст: Zzdrавствуy, mир!!!
 *
 * Обратите внимание: данная функция не имеет возвращаемого значения
 */
fun transliterate(inputName: String, dictionary: Map<Char, String>, outputName: String) {
    TODO()
}

/**
 * Средняя (12 баллов)
 *
 * Во входном файле с именем inputName имеется словарь с одним словом в каждой строчке.
 * Выбрать из данного словаря наиболее длинное слово,
 * в котором все буквы разные, например: Неряшливость, Четырёхдюймовка.
 * Вывести его в выходной файл с именем outputName.
 * Если во входном файле имеется несколько слов с одинаковой длиной, в которых все буквы разные,
 * в выходной файл следует вывести их все через запятую.
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 *
 * Пример входного файла:
 * Карминовый
 * Боязливый
 * Некрасивый
 * Остроумный
 * БелогЛазый
 * ФиолетОвый

 * Соответствующий выходной файл:
 * Карминовый, Некрасивый
 *
 * Обратите внимание: данная функция не имеет возвращаемого значения
 */
fun chooseLongestChaoticWord(inputName: String, outputName: String) {
    TODO()
}

/**
 * Сложная (22 балла)
 *
 * Реализовать транслитерацию текста в заданном формате разметки в формат разметки HTML.
 *
 * Во входном файле с именем inputName содержится текст, содержащий в себе элементы текстовой разметки следующих типов:
 * - *текст в курсивном начертании* -- курсив
 * - **текст в полужирном начертании** -- полужирный
 * - ~~зачёркнутый текст~~ -- зачёркивание
 *
 * Следует вывести в выходной файл этот же текст в формате HTML:
 * - <i>текст в курсивном начертании</i>
 * - <b>текст в полужирном начертании</b>
 * - <s>зачёркнутый текст</s>
 *
 * Кроме того, все абзацы исходного текста, отделённые друг от друга пустыми строками, следует обернуть в теги <p>...</p>,
 * а весь текст целиком в теги <html><body>...</body></html>.
 *
 * Все остальные части исходного текста должны остаться неизменными с точностью до наборов пробелов и переносов строк.
 * Отдельно следует заметить, что открывающая последовательность из трёх звёздочек (***) должна трактоваться как "<b><i>"
 * и никак иначе.
 *
 * При решении этой и двух следующих задач полезно прочитать статью Википедии "Стек".
 *
 * Пример входного файла:
Lorem ipsum *dolor sit amet*, consectetur **adipiscing** elit.
Vestibulum lobortis, ~~Est vehicula rutrum *suscipit*~~, ipsum ~~lib~~ero *placerat **tortor***,

Suspendisse ~~et elit in enim tempus iaculis~~.
 *
 * Соответствующий выходной файл:
<html>
<body>
<p>
Lorem ipsum <i>dolor sit amet</i>, consectetur <b>adipiscing</b> elit.
Vestibulum lobortis. <s>Est vehicula rutrum <i>suscipit</i></s>, ipsum <s>lib</s>ero <i>placerat <b>tortor</b></i>.
</p>
<p>
Suspendisse <s>et elit in enim tempus iaculis</s>.
</p>
</body>
</html>
 *
 * (Отступы и переносы строк в примере добавлены для наглядности, при решении задачи их реализовывать не обязательно)
 */
fun markdownToHtmlSimple(inputName: String, outputName: String) {
    TODO()
}

/**
 * Сложная (23 балла)
 *
 * Реализовать транслитерацию текста в заданном формате разметки в формат разметки HTML.
 *
 * Во входном файле с именем inputName содержится текст, содержащий в себе набор вложенных друг в друга списков.
 * Списки бывают двух типов: нумерованные и ненумерованные.
 *
 * Каждый элемент ненумерованного списка начинается с новой строки и символа '*', каждый элемент нумерованного списка --
 * с новой строки, числа и точки. Каждый элемент вложенного списка начинается с отступа из пробелов, на 4 пробела большего,
 * чем список-родитель. Максимально глубина вложенности списков может достигать 6. "Верхние" списки файла начинются
 * прямо с начала строки.
 *
 * Следует вывести этот же текст в выходной файл в формате HTML:
 * Нумерованный список:
 * <ol>
 *     <li>Раз</li>
 *     <li>Два</li>
 *     <li>Три</li>
 * </ol>
 *
 * Ненумерованный список:
 * <ul>
 *     <li>Раз</li>
 *     <li>Два</li>
 *     <li>Три</li>
 * </ul>
 *
 * Кроме того, весь текст целиком следует обернуть в теги <html><body><p>...</p></body></html>
 *
 * Все остальные части исходного текста должны остаться неизменными с точностью до наборов пробелов и переносов строк.
 *
 * Пример входного файла:
///////////////////////////////начало файла/////////////////////////////////////////////////////////////////////////////
 * Утка по-пекински
 * Утка
 * Соус
 * Салат Оливье
1. Мясо
 * Или колбаса
2. Майонез
3. Картофель
4. Что-то там ещё
 * Помидоры
 * Фрукты
1. Бананы
23. Яблоки
1. Красные
2. Зелёные
///////////////////////////////конец файла//////////////////////////////////////////////////////////////////////////////
 *
 *
 * Соответствующий выходной файл:
///////////////////////////////начало файла/////////////////////////////////////////////////////////////////////////////
<html>
<body>
<p>
<ul>
<li>
Утка по-пекински
<ul>
<li>Утка</li>
<li>Соус</li>
</ul>
</li>
<li>
Салат Оливье
<ol>
<li>Мясо
<ul>
<li>Или колбаса</li>
</ul>
</li>
<li>Майонез</li>
<li>Картофель</li>
<li>Что-то там ещё</li>
</ol>
</li>
<li>Помидоры</li>
<li>Фрукты
<ol>
<li>Бананы</li>
<li>Яблоки
<ol>
<li>Красные</li>
<li>Зелёные</li>
</ol>
</li>
</ol>
</li>
</ul>
</p>
</body>
</html>
///////////////////////////////конец файла//////////////////////////////////////////////////////////////////////////////
 * (Отступы и переносы строк в примере добавлены для наглядности, при решении задачи их реализовывать не обязательно)
 */
fun markdownToHtmlLists(inputName: String, outputName: String) {
    TODO()
}

/**
 * Очень сложная (30 баллов)
 *
 * Реализовать преобразования из двух предыдущих задач одновременно над одним и тем же файлом.
 * Следует помнить, что:
 * - Списки, отделённые друг от друга пустой строкой, являются разными и должны оказаться в разных параграфах выходного файла.
 *
 */
fun markdownToHtml(inputName: String, outputName: String) {
    TODO()
}

/**
 * Средняя (12 баллов)
 *
 * Вывести в выходной файл процесс умножения столбиком числа lhv (> 0) на число rhv (> 0).
 *
 * Пример (для lhv == 19935, rhv == 111):
19935
 *    111
--------
19935
+ 19935
+19935
--------
2212785
 * Используемые пробелы, отступы и дефисы должны в точности соответствовать примеру.
 * Нули в множителе обрабатывать так же, как и остальные цифры:
235
 *  10
-----
0
+235
-----
2350
 *
 */
fun printMultiplicationProcess(lhv: Int, rhv: Int, outputName: String) {
    TODO()
}


/**
 * Сложная (25 баллов)
 *
 * Вывести в выходной файл процесс деления столбиком числа lhv (> 0) на число rhv (> 0).
 *
 * Пример (для lhv == 19935, rhv == 22):
19935 | 22
-198     906
----
13
-0
--
135
-132
----
3

 * Используемые пробелы, отступы и дефисы должны в точности соответствовать примеру.
 *
 */
fun printDivisionProcess(lhv: Int, rhv: Int, outputName: String) {
    TODO()
}

/**Есть файл, в котором схематично изображено поле для игры в крестики-нолики на доске 15х15, а именно:
- 15 строк
- в каждой строке строго 15 символов
- пустая клетка обозначается -, крестик х, нолик о

Функция, которую нужно написать, принимает как параметры имя этого файла и очередность хода
(крестики или нолики).
Необходимо определить, существует ли ход, приводящий эту сторону к победе
(для этого нужно составить 5 своих знаков в ряд по вертикали, горизонтали или диагонали).
Если он не существует, следует вернуть null,
если существует — координаты клетки, куда нужно сходить.

Необходимо написать функцию и тесты к ней.
 */
enum class Player(val value: Char) {
    X('X'), O('O'), VOID('-'), EMPTY(' ')
}

fun main(inputName: String): Any? {
    val size = 15 // размер квадратного поля
    val winnerQuality = 5 // условие победы
    val map: ArrayList<Array<Pair<Pair<Int, Int>, Char>>> = ArrayList()


    // инициализация map для избежания ошибки NullPointerException
    for (i in 0 until size) {
        map.add(Array(size) { Pair(Pair(0, 0), Player.EMPTY.value) })
    }

    for (i in map.indices) {
        for (j in map.indices) {
            map[i][j] = Pair(Pair(i, j), Player.VOID.value)
        }
    }


    // читаем данные и сохраняем в map
    var k = 0
    File(inputName).forEachLine { line ->
        val values = line.split(' ')
        for (i in values.indices) {
            map[k][i] = Pair(Pair(k, i), values[i][0])
        }
        k++
    }

    val player = Player.X.value    // игрок
    val enemy = Player.O.value     // противник

    // проверяем, не победил ли уже противник
    val winningMoveEnemy = mutableListOf<String>()  // выигрышный ход противника
    var resultEnemy: ArrayList<Pair<Pair<Int, Int>, Char>>? = null   // нашлись ходы, куда можно сходить

    // Смотрим на то, сколько значений имеется в ряд у противника
    for (i in winnerQuality downTo 0) {
        if (resultEnemy != null) break
        for (j in 0..4) {
            if (resultEnemy == null) resultEnemy = map.onTransportation(j).toSquares().checkDiagonal(i, enemy, player)
            // переворачиваем map на 90 градусов j раз (ищем по горизонтали и вертикали), разбиваем на квадраты 5х5,
            // проверяем по диагонали (сколько значений подряд, кто игрок, кто противник)
            if (resultEnemy == null) resultEnemy = map.onTransportation(j).toSquares().checkLines(i, enemy, player)
            // переворачиваем map на 90 градусов j раз (ищем по горизонтали и вертикали), разбиваем на квадраты 5х5,
            // проверяем по вертикали и горизонтали (сколько значений подряд, кто игрок, кто противник)

            if (resultEnemy != null) {
                resultEnemy.forEach {
                    winningMoveEnemy += "${it.first.first + 1} - ${it.first.second + 1};"
                    // добавляем точку, в которую можнет сходить противник для выигрыша
                }
                break
            }
        }
    }

    val winningMove = mutableListOf<String>()  // выигрышный ход
    var result: ArrayList<Pair<Pair<Int, Int>, Char>>? = null   // нашлись ходы, куда можно сходить
    // отсчет от 4 до 0, сколько значений имеется в ряд (если для победы нужно 5, то мы проверяем,
    // есть ли у нас 4 значения подряд и тд)
    for (i in (winnerQuality - 1) downTo 0) {
        if (result != null) break
        for (j in 0..4) {
            if (result == null) result = map.onTransportation(j).toSquares().checkDiagonal(i, player, enemy)
            if (result == null) result = map.onTransportation(j).toSquares().checkLines(i, player, enemy)
            if (result != null) {
                result.forEach {
                    winningMove += "${it.first.first + 1} - ${it.first.second + 1};"
                    // добавляем точку, в которую можно сходить в выигрышный ход
                }
                break
            }
        }
    }

    return if (winningMoveEnemy.isEmpty()) null
    else winningMove.joinToString(" ")
}


fun createEmptyArray(size: Int): ArrayList<Array<Pair<Pair<Int, Int>, Char>>> { //обнуление
    val result = ArrayList<Array<Pair<Pair<Int, Int>, Char>>>()
    for (i in 0 until size) {
        result.add(Array(size) { Pair(Pair(0, 0), Player.EMPTY.value) })
        for (j in 0 until size) {
            result[i][j] = Pair(Pair(i, j), Player.VOID.value)
        }
    }
    return result
}

fun ArrayList<Array<Pair<Pair<Int, Int>, Char>>>.onTransportation(  // переворачиваем map
    count: Int
): ArrayList<Array<Pair<Pair<Int, Int>, Char>>> {

    var result = createEmptyArray(size)  // значение, перенесенное с одной координаты на другую
    val tmp = ArrayList(this)
    for (k in 0 until count) {
        for (i in tmp.indices) {
            for (j in tmp.indices) {
                result[j][tmp.size - 1 - i] = tmp[i][j]
            }
        }
        tmp.clear()
        tmp.addAll(result)
        result = createEmptyArray(size)
    }
    return tmp
}


fun ArrayList<Array<Pair<Pair<Int, Int>, Char>>>.toSquares()   // разбиение на квадраты 5x5
        : ArrayList<ArrayList<Array<Pair<Pair<Int, Int>, Char>>>> {
    val block = 5 // размер блока (победный квадрат)
    val result = ArrayList<ArrayList<Array<Pair<Pair<Int, Int>, Char>>>>() // складываем мини-квадраты
    var tmpArray: ArrayList<Array<Pair<Pair<Int, Int>, Char>>> // зесь хранятся разные мини-квадраты
    // генерация мини-квадратов (после точек 1..15 - 10 и 10 - 1..15 разбить на маленькие
    // квадраты не удастся, так как они буду выходить за границы квадрата 15х15
    for (k in 0 until size - block) {
        for (l in 0 until size - block) {
            tmpArray = createEmptyArray(block)
            for (i in 0 until block) {
                for (j in 0 until block) {
                    tmpArray[i][j] = this[i + k][j + l]
                }
            }
            result.add(tmpArray)
        }
    }
    return result
}


fun ArrayList<ArrayList<Array<Pair<Pair<Int, Int>, Char>>>>.checkDiagonal(  // проверка от верхнего левого угла к верхнему правому
    checkValueCount: Int,
    char: Char,
    enemy: Char
): ArrayList<Pair<Pair<Int, Int>, Char>>? {
    var points: ArrayList<Pair<Pair<Int, Int>, Char>>
    this.forEach { square ->
        var check = 0 //нужный символ
        var enemyCount = 0 //противник
        points = ArrayList()  // точки, в которые можно сходить
        for (i in square.indices) {
            if (square[i][i].second == enemy) enemyCount++   // если есть противник
            if (square[i][i].second == char) check++     // если есть нужный символ
            if (square[i][i].second == Player.VOID.value) points.add(square[i][i])
        }
        if (check == checkValueCount && enemyCount <= 0) return points
    }
    return null
}

fun ArrayList<ArrayList<Array<Pair<Pair<Int, Int>, Char>>>>.checkLines(
    checkValueCount: Int,
    char: Char,
    enemy: Char
): ArrayList<Pair<Pair<Int, Int>, Char>>? {
    var points: ArrayList<Pair<Pair<Int, Int>, Char>>
    this.forEach { square ->
        for (i in square.indices) {
            var check = 0
            var enemyCount = 0
            points = ArrayList()
            for (j in square.indices) {
                if (square[i][j].second == enemy) enemyCount++
                if (square[i][j].second == char) check++
                if (square[i][j].second == Player.VOID.value) points.add(square[i][j])
            }

            if (check == checkValueCount && enemyCount <= 0) {
                return points
            }
        }
    }
    return null
}
