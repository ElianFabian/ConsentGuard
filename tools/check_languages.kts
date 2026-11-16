/* Checks the language packs together with the English lists, the way the app
   combines them. Fails (exit code 1) when:
     * a pack file is missing a key or has a malformed phrase,
     * a generated phrase is not already in the folded form the app compares
       (a sign that a builder and ConsentText.normalize no longer agree),
     * a label that is meant to ACCEPT would also be matched by a reject,
       necessary only, manage or confirm phrase of any language (so a tap on
       an accept button could be mistaken for a choice), or
     * a reject, necessary only, manage or confirm phrase is itself an accept
       guard phrase of any language.

   Run from anywhere inside the project:
       kotlinc -script tools/check_languages.kts */

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

// Reading JSON

class JsonReader(private val text: String, private val fileName: String) {
    private var pos = 0

    private fun fail(what: String): Nothing = throw IllegalStateException("$fileName: $what at offset $pos")

    private fun skip() {
        while (pos < text.length && text[pos].isWhitespace()) pos++
    }

    private fun expect(c: Char) {
        skip()
        if (pos >= text.length || text[pos] != c) fail("expected '$c'")
        pos++
    }

    fun value(): Any? {
        skip()
        if (pos >= text.length) fail("unexpected end")
        return when (val c = text[pos]) {
            '{' -> {
                pos++
                val map = LinkedHashMap<String, Any?>()
                skip()
                if (text[pos] == '}') {
                    pos++
                } else {
                    do {
                        skip()
                        val key = string()
                        expect(':')
                        map[key] = value()
                        skip()
                        val more = text[pos] == ','
                        if (more) pos++
                    } while (more)
                    expect('}')
                }
                map
            }
            '[' -> {
                pos++
                val list = ArrayList<Any?>()
                skip()
                if (text[pos] == ']') {
                    pos++
                } else {
                    do {
                        list.add(value())
                        skip()
                        val more = text[pos] == ','
                        if (more) pos++
                    } while (more)
                    expect(']')
                }
                list
            }
            '"' -> string()
            else -> {
                val start = pos
                while (pos < text.length && (text[pos].isLetterOrDigit() || text[pos] in "+-.")) pos++
                when (val word = text.substring(start, pos)) {
                    "true" -> true
                    "false" -> false
                    "null" -> null
                    else -> word.toLongOrNull() ?: word.toDoubleOrNull() ?: fail("unexpected '$c'")
                }
            }
        }
    }

    private fun string(): String {
        expect('"')
        val sb = StringBuilder()
        while (true) {
            if (pos >= text.length) fail("string is never closed")
            val c = text[pos++]
            if (c == '"') return sb.toString()
            if (c != '\\') {
                sb.append(c)
                continue
            }
            when (val e = text[pos++]) {
                'n' -> sb.append('\n')
                't' -> sb.append('\t')
                'r' -> sb.append('\r')
                'b' -> sb.append('\b')
                'f' -> sb.append('\u000C')
                'u' -> {
                    sb.append(text.substring(pos, pos + 4).toInt(16).toChar())
                    pos += 4
                }
                else -> sb.append(e)
            }
        }
    }
}

fun readJson(file: File): Any? = JsonReader(file.readText(), file.name).value()

/* The same matching the app's PatternList does */
class Patterns(raw: List<String>) {
    val subs = ArrayList<String>()
    val exacts = HashSet<String>()
    private val padded: List<String>

    init {
        for (p in raw) {
            val body = norm(if (p.startsWith("=")) p.substring(1) else p)
            if (body.isEmpty()) continue
            if (p.startsWith("=")) exacts.add(body) else subs.add(body)
        }
        padded = subs.map { " $it " }
    }

    fun matches(text: String): Boolean {
        val t = norm(text)
        return t.isNotEmpty() && (t in exacts || subs.any { t.contains(it) })
    }

    fun matchesWords(text: String): Boolean {
        val t = norm(text)
        if (t.isEmpty()) return false
        if (t in exacts) return true
        val pt = " $t "
        return padded.any { pt.contains(it) }
    }
}

@Suppress("UNCHECKED_CAST")
fun strings(value: Any?): List<String> = (value as? List<Any?>)?.filterIsInstance<String>().orEmpty()

val root = projectRoot()
val assets = File(root, "app/src/main/assets")
val rules = (readJson(File(assets, "consent_rules.json")) as List<*>).map { it as Map<*, *> }
val lexicon = readJson(File(assets, "consent_lexicon.json")) as Map<*, *>
val packs = File(assets, "lang").listFiles { f -> f.name.endsWith(".json") }!!
    .sortedBy { it.name }
    .map { readJson(it) as Map<*, *> }

val errors = ArrayList<String>()

for (p in packs) {
    for (k in KEYS) {
        if (p[k] !is List<*>) errors.add("${p["language"]}: missing list $k")
    }
}

/* Generated phrases must already be folded. The four hand written rules are
   left out, the app folds those when it loads them. */
fun checkFolded(where: String, phrases: List<String>, star: Boolean = false) {
    for (raw in phrases) {
        val body = raw.removePrefix("=").let { if (star) it.removeSuffix("*") else it }
        if (body.isEmpty() || norm(body) != body) errors.add("$where phrase \"$raw\" is not in folded form")
    }
}
for (p in packs) {
    for (k in KEYS) checkFolded("${p["language"]}.$k", strings(p[k]), star = k == "acceptWords")
}
rules.filter { it["name"] == "english_wording" }.forEach { r ->
    for ((k, v) in r) if (k.toString().endsWith("TextPatterns")) checkFolded("english_wording.$k", strings(v))
}
for ((k, v) in lexicon) checkFolded("lexicon.$k", strings(v))

/* A plain phrase matches anywhere inside a label, so a very short one turns
   up inside ordinary words: a two letter "ki" is in "cookies". Those have to
   be exact phrases ("=ki"). */
val MIN_SUBSTRING = 3
for (p in packs) {
    for (key in listOf("reject", "necessary", "manage", "confirm", "fallbackReject", "rejectButton", "expandSection")) {
        for (raw in strings(p[key])) {
            if (!raw.startsWith("=") && raw.codePointCount(0, raw.length) < MIN_SUBSTRING) {
                errors.add("${p["language"]}.$key phrase \"$raw\" is too short to match inside labels, make it \"=$raw\"")
            }
        }
    }
}

fun gather(ruleKey: String, packKey: String): List<String> =
    rules.flatMap { strings(it[ruleKey]) } + packs.flatMap { strings(it[packKey]) }

val choices = linkedMapOf(
    "reject" to Patterns(gather("rejectTextPatterns", "reject")),
    "necessary" to Patterns(gather("necessaryTextPatterns", "necessary")),
    "manage" to Patterns(gather("manageTextPatterns", "manage")),
    "confirm" to Patterns(gather("confirmTextPatterns", "confirm"))
)
val acceptRaw = strings(lexicon["acceptGuardTextPatterns"]) + packs.flatMap { strings(it["acceptGuard"]) }
val accept = Patterns(acceptRaw)

// 1. Every accept label against every choice list
for (raw in acceptRaw) {
    val label = raw.removePrefix("=")
    for ((kind, pats) in choices) {
        if (pats.matches(label)) errors.add("accept label \"$label\" is matched by a $kind phrase")
    }
}

// 2. Every choice phrase, read as a label, against the accept guard
for (p in packs) {
    for (key in listOf("reject", "necessary", "manage", "confirm", "fallbackReject", "rejectButton")) {
        for (raw in strings(p[key])) {
            val label = raw.removePrefix("=")
            if (accept.matchesWords(label)) errors.add("${p["language"]}.$key phrase \"$label\" is an accept guard phrase")
        }
    }
}

for (e in errors.take(80)) println("PROBLEM: $e")
println("${errors.size} problem(s), ${packs.size} packs checked")
if (errors.isNotEmpty()) kotlin.system.exitProcess(1)
