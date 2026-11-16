/* Fails if the promises made in docs/PRIVACY.md stop being true in the source.

   Checks that the safe zone check comes before any screen is read, that
   logging is limited to debug builds, and that the manifest has no network
   permission.

   Run from anywhere inside the project:
       kotlinc -script tools/check_trust.kts */

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

val root = projectRoot()
val sourceDir = File(root, "app/src/main/kotlin/dev/macbuildssys/consentguard")
val service = File(sourceDir, "ConsentBlockerService.kt").readText()
val manifest = File(root, "app/src/main/AndroidManifest.xml").readText()
val problems = ArrayList<String>()

// 1. The safe zone check comes before any reading of the screen in scanAndActInner
val body = Regex("private fun scanAndActInner\\(.*?\\n    \\}\\n", RegexOption.DOT_MATCHES_ALL)
    .find(service)?.value.orEmpty()
val safe = body.indexOf("isSafeZone(packageName)")
val reads = listOf("judgeConsentScreen(", "getChild(", "findAccessibilityNodeInfosByText(", ".text")
    .map { body.indexOf(it) }
    .filter { it >= 0 }
if (body.isEmpty()) {
    problems.add("scanAndActInner was not found in ConsentBlockerService.kt")
} else if (safe < 0) {
    problems.add("scanAndActInner does not call isSafeZone")
} else if (reads.isNotEmpty() && reads.min() < safe) {
    problems.add("the screen is read before the safe zone check")
}

// 2. Log calls only inside debugLog, in every source file
val logCall = Regex("\\bLog\\.[dviwe]\\(")
sourceDir.listFiles { f -> f.name.endsWith(".kt") }!!.sortedBy { it.name }.forEach { file ->
    val lines = file.readLines()
    lines.forEachIndexed { index, line ->
        if (logCall.containsMatchIn(line)) {
            val context = lines.subList(maxOf(0, index - 5), index + 1).joinToString("\n")
            if ("BuildConfig.DEBUG" !in context && "fun debugLog" !in context) {
                problems.add("log call outside debugLog in ${file.name} at line ${index + 1}")
            }
        }
    }
}

// 3. No network permission, unless it is explicitly removed
val withoutComments = Regex("<!--.*?-->", RegexOption.DOT_MATCHES_ALL).replace(manifest, "")
Regex("<uses-permission[^>]*android\\.permission\\.INTERNET[^>]*>").findAll(withoutComments).forEach {
    if ("tools:node=\"remove\"" !in it.value) problems.add("INTERNET permission present")
}

println("${problems.size} problems")
for (p in problems) println("  $p")
if (problems.isNotEmpty()) kotlin.system.exitProcess(1)
