/* Builds one JSON file per language in app/src/main/assets/lang from the word
   lists in tools/langdata/<code>.kts.

   Each data file is a small Kotlin script that sets code, name, status, conj
   (the words that mean "and" in that language), an optional enabled flag and
   pack, a map from the keys listed in KEYS to lists of phrases. This builder
   reads those files as data and does not run them, so they may only hold
   plain string literals, true or false, listOf and mapOf (an empty list is
   written listOf<String>() so the file stays valid Kotlin). Anything else is
   reported with its file and line.

   Phrases are written plainly, with accents and capitals if you like. They
   are folded exactly the way ConsentText.normalize does it in the app,
   duplicates are removed, and a variant with "&" in place of the word for
   "and" is added, because the app turns "&" into the English word "and"
   before it compares anything.

   Phrases starting with "=" must equal the whole label. Anything else matches
   when the label contains it.

   Run from anywhere inside the project:
       kotlinc -script tools/build_languages.kts */

import java.io.File
import java.text.Normalizer

val KEYS = listOf(
    "reject", "necessary", "manage", "confirm", "fallbackReject",
    "rejectButton", "expandSection",
    "anchor", "bareAnchor", "strongAnchor", "neverClick", "acceptGuard",
    "acceptWords", "switchEssential", "switchNonEssential", "switchKeepOn",
    "adsPurpose", "statsPurpose", "topLevelWords", "cookieContext",
    "sensitiveScreen"
)

fun projectRoot(): File {
    var dir: File? = File("").absoluteFile
    while (dir != null) {
        if (File(dir, "settings.gradle.kts").isFile && File(dir, "tools").isDirectory) return dir
        dir = dir.parentFile
    }
    System.err.println("Run this from inside the ConsentGuard project")
    kotlin.system.exitProcess(2)
}

// Text folding, kept identical to ConsentText.normalize in ConsentRules.kt

val FOLD = mapOf(
    'ٱ' to 'ا', 'ى' to 'ي', 'ƙ' to 'k',
    'ɗ' to 'd', 'ɓ' to 'b', 'ƴ' to 'y'
)

fun fold(input: String): String {
    val decomposed = Normalizer.normalize(input, Normalizer.Form.NFD)
    val out = StringBuilder(decomposed.length)
    for (c in decomposed) {
        val code = c.code
        if (code in 0x0300..0x036F || code in 0x064B..0x065F || code == 0x0640) continue
        out.append(FOLD[c] ?: c)
    }
    return Normalizer.normalize(out, Normalizer.Form.NFC)
}

fun norm(raw: String): String {
    val input = if (raw.any { it.code > 0x7F }) fold(raw) else raw
    val out = StringBuilder(input.length)
    var lastWasSpace = true
    for (c in input) {
        when {
            c == '\'' || c == '’' || c == '‘' || c == '`' || c == 'ʼ' -> {}
            c == '&' -> {
                if (!lastWasSpace) out.append(' ')
                out.append("and ")
                lastWasSpace = true
            }
            c.isLetterOrDigit() -> {
                out.append(c.lowercaseChar())
                lastWasSpace = false
            }
            !lastWasSpace -> {
                out.append(' ')
                lastWasSpace = true
            }
        }
    }
    return out.toString().trim()
}

// Reading the data files

class DataError(message: String) : Exception(message)

/* A tokenizer and parser for the small part of Kotlin the data files use:
   val NAME = VALUE, where VALUE is a string, true, false, listOf(VALUE, ...)
   or mapOf(STRING to VALUE, ...). Comments are skipped. */
class DataReader(private val text: String, private val fileName: String) {
    private var pos = 0
    private var line = 1

    private fun fail(what: String): Nothing = throw DataError("$fileName:$line: $what")

    private fun skipSpaceAndComments() {
        while (pos < text.length) {
            val c = text[pos]
            when {
                c == '\n' -> { line++; pos++ }
                c.isWhitespace() -> pos++
                text.startsWith("//", pos) -> while (pos < text.length && text[pos] != '\n') pos++
                text.startsWith("/*", pos) -> {
                    val end = text.indexOf("*/", pos + 2)
                    if (end < 0) fail("comment is never closed")
                    line += text.substring(pos, end).count { it == '\n' }
                    pos = end + 2
                }
                else -> return
            }
        }
    }

    private fun peekWord(): String {
        skipSpaceAndComments()
        var end = pos
        while (end < text.length && (text[end].isLetterOrDigit() || text[end] == '_')) end++
        return text.substring(pos, end)
    }

    private fun word(): String {
        val w = peekWord()
        if (w.isEmpty()) fail("expected a name, found '${text.getOrNull(pos) ?: "end of file"}'")
        pos += w.length
        return w
    }

    private fun symbol(expected: Char) {
        skipSpaceAndComments()
        if (pos >= text.length || text[pos] != expected) {
            fail("expected '$expected', found '${text.getOrNull(pos) ?: "end of file"}'")
        }
        pos++
    }

    private fun peekSymbol(c: Char): Boolean {
        skipSpaceAndComments()
        return pos < text.length && text[pos] == c
    }

    private fun string(): String {
        symbol('"')
        val sb = StringBuilder()
        while (true) {
            if (pos >= text.length || text[pos] == '\n') fail("string is never closed")
            val c = text[pos++]
            when (c) {
                '"' -> return sb.toString()
                '$' -> fail("string templates are not allowed, write \\$ for a dollar sign")
                '\\' -> {
                    if (pos >= text.length) fail("string is never closed")
                    when (val e = text[pos++]) {
                        'n' -> sb.append('\n')
                        't' -> sb.append('\t')
                        'u' -> {
                            if (pos + 4 > text.length) fail("broken \\u escape")
                            sb.append(text.substring(pos, pos + 4).toInt(16).toChar())
                            pos += 4
                        }
                        '"', '\\', '\'', '$' -> sb.append(e)
                        else -> fail("unknown escape \\$e")
                    }
                }
                else -> sb.append(c)
            }
        }
    }

    /* Skips a type argument such as <String>, which an empty list needs to
       be valid Kotlin: listOf<String>(). */
    private fun skipTypeArguments() {
        if (!peekSymbol('<')) return
        val end = text.indexOf('>', pos)
        if (end < 0) fail("'<' is never closed")
        pos = end + 1
    }

    private fun value(): Any {
        if (peekSymbol('"')) return string()
        return when (val w = word()) {
            "true" -> true
            "false" -> false
            "listOf" -> {
                skipTypeArguments()
                symbol('(')
                val items = ArrayList<Any>()
                while (!peekSymbol(')')) {
                    items.add(value())
                    if (!peekSymbol(')')) symbol(',')
                }
                symbol(')')
                items
            }
            "mapOf" -> {
                skipTypeArguments()
                symbol('(')
                val map = LinkedHashMap<String, Any>()
                while (!peekSymbol(')')) {
                    val key = string()
                    if (word() != "to") fail("expected 'to' after \"$key\"")
                    if (map.put(key, value()) != null) fail("\"$key\" appears twice")
                    if (!peekSymbol(')')) symbol(',')
                }
                symbol(')')
                map
            }
            else -> fail("'$w' is not allowed here, only strings, true, false, listOf and mapOf")
        }
    }

    fun read(): Map<String, Any> {
        val values = LinkedHashMap<String, Any>()
        while (true) {
            skipSpaceAndComments()
            if (pos >= text.length) return values
            if (word() != "val") fail("expected 'val'")
            val name = word()
            symbol('=')
            if (values.put(name, value()) != null) fail("$name is set twice")
        }
    }
}

class LanguageData(
    val code: String,
    val name: String,
    val status: String,
    val enabled: Boolean,
    val conj: List<String>,
    val pack: Map<String, List<String>>
)

fun readLanguage(file: File): LanguageData {
    val values = DataReader(file.readText(), "tools/langdata/${file.name}").read()
    fun text(key: String): String =
        values[key] as? String ?: throw DataError("${file.name}: val $key must be a string")
    fun strings(raw: Any?, what: String): List<String> {
        val list = raw as? List<*> ?: throw DataError("${file.name}: $what must be listOf(...)")
        return list.map { it as? String ?: throw DataError("${file.name}: $what may only hold strings") }
    }
    val rawPack = values["pack"] as? Map<*, *> ?: throw DataError("${file.name}: val pack must be mapOf(...)")
    val pack = LinkedHashMap<String, List<String>>()
    for ((key, list) in rawPack) {
        if (key !in KEYS) throw DataError("${file.name}: unknown pack key \"$key\", allowed are ${KEYS.joinToString()}")
        pack[key as String] = strings(list, "\"$key\"")
    }
    val unknown = values.keys - setOf("code", "name", "status", "enabled", "conj", "pack")
    if (unknown.isNotEmpty()) throw DataError("${file.name}: unknown setting ${unknown.joinToString()}")
    val code = text("code")
    if (file.nameWithoutExtension != code) throw DataError("${file.name}: code is \"$code\", it must match the file name")
    return LanguageData(
        code = code,
        name = text("name"),
        status = text("status"),
        enabled = values["enabled"] as? Boolean ?: true,
        conj = strings(values["conj"] ?: emptyList<String>(), "conj"),
        pack = pack
    )
}

// Building

/* The phrase itself, plus a form with "&" instead of each "and" word */
fun variants(phrase: String, conj: List<String>): List<String> {
    val out = mutableListOf(phrase)
    val exact = phrase.startsWith("=")
    val body = if (exact) phrase.substring(1) else phrase
    val prefix = if (exact) "=" else ""
    val words = body.split(" ")
    for (c in conj) {
        if (c in words) out.add(prefix + words.joinToString(" ") { if (it == c) "&" else it })
    }
    return out
}

fun clean(items: List<String>, conj: List<String>, keepStar: Boolean): List<String> {
    val seen = HashSet<String>()
    val out = ArrayList<String>()
    for (raw in items) {
        for (v in variants(raw.trim(), conj)) {
            val star = keepStar && v.endsWith("*")
            val exact = v.startsWith("=")
            val body = norm(
                when {
                    exact -> v.substring(1)
                    star -> v.trimEnd('*')
                    else -> v
                }
            )
            if (body.isEmpty()) continue
            val key = (if (exact) "=" else "") + body + (if (star) "*" else "")
            if (seen.add(key)) out.add(key)
        }
    }
    return out
}

// JSON output, laid out the way the files have always been written

fun jsonString(s: String): String {
    val sb = StringBuilder("\"")
    for (c in s) {
        when {
            c == '"' -> sb.append("\\\"")
            c == '\\' -> sb.append("\\\\")
            c == '\n' -> sb.append("\\n")
            c == '\r' -> sb.append("\\r")
            c == '\t' -> sb.append("\\t")
            c == '\b' -> sb.append("\\b")
            c == '\u000C' -> sb.append("\\f")
            c.code < 0x20 -> sb.append("\\u%04x".format(c.code))
            else -> sb.append(c)
        }
    }
    return sb.append('"').toString()
}

fun toJson(value: Any?, indent: Int, level: Int = 0): String {
    val pad = " ".repeat(indent * (level + 1))
    val end = " ".repeat(indent * level)
    return when (value) {
        null -> "null"
        is String -> jsonString(value)
        is Boolean, is Int, is Long -> value.toString()
        is Map<*, *> -> if (value.isEmpty()) "{}" else value.entries.joinToString(",\n", "{\n", "\n$end}") {
            pad + jsonString(it.key as String) + ": " + toJson(it.value, indent, level + 1)
        }
        is List<*> -> if (value.isEmpty()) "[]" else value.joinToString(",\n", "[\n", "\n$end]") {
            pad + toJson(it, indent, level + 1)
        }
        else -> throw IllegalArgumentException("cannot write ${value::class}")
    }
}

val root = projectRoot()
val dataDir = File(root, "tools/langdata")
val outDir = File(root, "app/src/main/assets/lang")
outDir.mkdirs()

var total = 0
try {
    val files = dataDir.listFiles { f -> f.isFile && f.name.endsWith(".kts") && !f.name.startsWith("_") }
        ?.sortedBy { it.name }.orEmpty()
    if (files.isEmpty()) throw DataError("no language files in tools/langdata")
    for (file in files) {
        val lang = readLanguage(file)
        val conj = lang.conj.map { norm(it) }
        val pack = LinkedHashMap<String, Any>()
        pack["language"] = lang.code
        pack["name"] = lang.name
        pack["status"] = lang.status
        pack["enabled"] = lang.enabled
        var phrases = 0
        for (key in KEYS) {
            val cleaned = clean(lang.pack[key].orEmpty(), conj, keepStar = key == "acceptWords")
            pack[key] = cleaned
            phrases += cleaned.size
        }
        File(outDir, "${lang.code}.json").writeText(toJson(pack, 1) + "\n")
        total += phrases
        println("%-3s %-12s %-9s %5d phrases".format(lang.code, lang.name, lang.status, phrases))
    }
    println("total $total")
} catch (e: DataError) {
    System.err.println("PROBLEM: ${e.message}")
    kotlin.system.exitProcess(1)
}
