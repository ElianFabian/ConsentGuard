/* Fails when the app could reach the network. Checks the source manifest,
   and when given a built APK manifest or a merged manifest, that one too.

       kotlinc -script tools/check_no_network.kts
       kotlinc -script tools/check_no_network.kts path/to/AndroidManifest.xml

   The built merged manifest is normally at
       app/build/intermediates/merged_manifests/release/processReleaseManifest/AndroidManifest.xml
   (the exact folder name depends on the Android Gradle plugin version). */

import java.io.File

fun projectRoot(): File {
    var dir: File? = File("").absoluteFile
    while (dir != null) {
        if (File(dir, "settings.gradle.kts").isFile && File(dir, "tools").isDirectory) return dir
        dir = dir.parentFile
    }
    System.err.println("Run this from inside the ConsentGuard project")
    kotlin.system.exitProcess(2)
}

val FORBIDDEN = listOf(
    "INTERNET", "ACCESS_NETWORK_STATE", "ACCESS_WIFI_STATE", "CHANGE_NETWORK_STATE", "NFC", "BLUETOOTH"
)

/* Code that could talk to the network, or hand data to another app to send */
val SOURCE_WORDS = listOf(
    "java.net.", "javax.net.", "okhttp", "HttpURLConnection", "HttpsURLConnection",
    "Socket(", "WebView(", "ACTION_VIEW", "ACTION_SEND", "ACTION_SENDTO", "DownloadManager",
    "Uri.parse(\"http", "retrofit", "volley"
)

val xmlComment = Regex("<!--.*?-->", RegexOption.DOT_MATCHES_ALL)
val blockComment = Regex("/\\*.*?\\*/", RegexOption.DOT_MATCHES_ALL)
val permissionTag = Regex("<uses-permission[^>]*>")

/* A permission marked tools:node="remove" is taken out, so it does not count */
fun checkManifest(file: File): List<String> {
    val text = xmlComment.replace(file.readText(), "")
    val problems = ArrayList<String>()
    for (match in permissionTag.findAll(text)) {
        val tag = match.value
        if ("tools:node=\"remove\"" in tag) continue
        /* A prefix match on purpose, so BLUETOOTH_CONNECT, BLUETOOTH_SCAN
           and NFC_TRANSACTION_EVENT are caught as well. */
        for (name in FORBIDDEN) {
            if ("android.permission.$name" in tag) problems.add("${file.path} asks for $name")
        }
    }
    return problems
}

fun checkSources(root: File): List<String> {
    val problems = ArrayList<String>()
    File(root, "app/src/main").walkTopDown()
        .filter { it.isFile && (it.name.endsWith(".kt") || it.name.endsWith(".java")) }
        .sortedBy { it.path }
        .forEach { file ->
            val text = blockComment.replace(file.readText(), "")
            for (line in text.lines()) {
                val code = line.substringBefore("//")
                for (word in SOURCE_WORDS) {
                    if (word in code) problems.add("${file.path} uses $word")
                }
            }
        }
    return problems
}

val root = projectRoot()
val manifests = if (args.isNotEmpty()) args.map { File(it) } else listOf(File(root, "app/src/main/AndroidManifest.xml"))
val problems = ArrayList<String>()
for (m in manifests) {
    if (!m.isFile) {
        problems.add("${m.path} does not exist")
        continue
    }
    problems += checkManifest(m)
}
problems += checkSources(root)
for (p in problems) println("PROBLEM: $p")
println(if (problems.isEmpty()) "no network permissions found" else "${problems.size} problem(s)")
if (problems.isNotEmpty()) kotlin.system.exitProcess(1)
