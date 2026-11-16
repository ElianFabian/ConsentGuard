/* Checks the bank and payment lists in SafeZoneWords.kt, the same way the
   app uses them. It fails when:
     * the name of an ordinary app (browser, messenger, mail, maps and so on)
       would be treated as a bank or money app,
     * a bank, money, email or chat app name from the sample lists would be
       missed, or
     * mail.com would be skipped (its pages and app show consent banners).

   Run from anywhere inside the project:
       kotlinc -script tools/check_safe_zone.kts */

import java.io.File
import java.text.Normalizer

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
val source = File(root, "app/src/main/kotlin/dev/macbuildssys/consentguard/SafeZoneWords.kt").readText()

/* The string literals of one list in SafeZoneWords.kt */
fun words(name: String): List<String> {
    val start = source.indexOf("val $name")
    require(start >= 0) { "SafeZoneWords.kt has no list called $name" }
    val end = source.indexOf("\n    )", start)
    require(end > start) { "the list $name in SafeZoneWords.kt is not closed the usual way" }
    return Regex("\"([^\"]+)\"").findAll(source.substring(start, end)).map { it.groupValues[1] }.toList()
}

/* Kept identical to SafeZoneWords.fold */
fun fold(text: String): String {
    val d = Normalizer.normalize(text.lowercase(), Normalizer.Form.NFD)
    val sb = StringBuilder(d.length)
    var lastSpace = true
    for (ch in d) {
        if (Character.getType(ch) == Character.NON_SPACING_MARK.toInt()) continue
        if (Character.isLetterOrDigit(ch)) {
            sb.append(ch)
            lastSpace = false
        } else if (!lastSpace) {
            sb.append(' ')
            lastSpace = true
        }
    }
    return Normalizer.normalize(sb.toString().trim(), Normalizer.Form.NFC)
}

val browser = words("BROWSER_WORDS")
val packageWords = words("PACKAGE_WORDS")
val packageExact = words("PACKAGE_EXACT")
val messagingPackages = words("MESSAGING_PACKAGES")
val bankParts = words("BANK_PARTS").map { fold(it) }
val moneyWords = words("MONEY_WORDS").map { fold(it) }
val messageWords = words("MESSAGE_WORDS").map { fold(it) }
val brands = words("BRANDS").map { fold(it) }

fun isMailDotCom(pkg: String, label: String): Boolean {
    val f = fold(label)
    return f == "mail com" || f.startsWith("mail com ") || f.endsWith(" mail com") || "mail.com" in pkg.lowercase()
}

fun labelHit(label: String): Boolean {
    val f = fold(label)
    if (f.isEmpty() || isMailDotCom("", label)) return false
    if (bankParts.any { f.contains(it) }) return true
    val padded = " $f "
    return (moneyWords + messageWords + brands).any { padded.contains(" $it ") }
}

fun packageHit(pkg: String): Boolean {
    val low = pkg.lowercase()
    if (browser.any { low.contains(it) }) return false
    if (packageExact.any { low == it || low.startsWith("$it.") }) return true
    return packageWords.any { low.contains(it) }
}

fun messagingHit(pkg: String, label: String): Boolean {
    if (isMailDotCom(pkg, label)) return false
    val low = pkg.lowercase()
    if (messagingPackages.any { low == it || low.startsWith("$it.") }) return true
    val padded = " ${fold(label)} "
    return messageWords.any { padded.contains(" $it ") }
}

fun split(block: String, separator: String): List<String> =
    block.trim().replace("\n", separator).split(separator).map { it.trim() }.filter { it.isNotEmpty() }

val ordinaryPackages = split(
    """
    com.android.chrome org.mozilla.firefox org.mozilla.fenix com.brave.browser com.opera.browser com.microsoft.emmx
    com.sec.android.app.sbrowser com.duckduckgo.mobile.android com.vivaldi.browser org.torproject.torbrowser
    com.google.android.youtube com.google.android.apps.maps com.android.vending
    com.facebook.katana com.instagram.android com.spotify.music com.netflix.mediaclient
    com.android.settings com.android.systemui com.sec.android.app.launcher
    com.linkedin.android com.android.camera2 org.videolan.vlc org.wikipedia
    """, " "
)

val ordinaryLabels = split(
    """
    Chrome|Firefox|Brave|Opera|Samsung Internet|Tor Browser|DuckDuckGo|Vivaldi|Settings|Camera|Maps
    YouTube|Photos|Clock|Calculator|Spotify|Files|Phone|Contacts|Calendar|Notes|LinkedIn|Instagram|Facebook
    Netflix|Play Store|Weather|Wikipedia|VLC|Translate|Drive|Docs|Keep Notes|Reddit|Pinterest|Uber|Bolt
    Amazon Shopping|Jumia|Booking.com|Airbnb|Duolingo|Kindle|Pocket|Podcasts|Radio|Compass|Torch|Recorder
    Gallery|Music|Google|Gemini|Fitbit|Strava|BBC News|Der Spiegel|Le Monde|El País|Zeit Online
    """, "|"
)

val shouldMatch = split(
    """
    M-PESA|Gmail|GMX Mail|WhatsApp|WhatsApp Business|Signal|Telegram|Messages|Outlook|Thunderbird|K-9 Mail
    Proton Mail|Messenger|My Airtel|NALA|LemFi|Wise|Sendwave|Taptap Send|Remitly|WorldRemit|Samsung Pay
    Samsung Wallet|Google Wallet|PayPal|Cash App|Venmo|Revolut|Monzo|N26|Klarna|Equity Mobile|KCB Mobile
    Absa Banking|Capitec Banking|FNB Banking|CRDB SimBanking|Kuda|OPay|PalmPay|Moniepoint|bKash|Paytm
    PhonePe|BHIM|GPay|HDFC Bank|Telebirr|CBE Birr|Fawry|Alipay|WeChat Pay|GCash|Nubank|Itaú|PicPay
    Mercado Pago|Sparkasse|Volksbank|ING Banking|DKB|Commerzbank|Chase|Wells Fargo|Citi Mobile|Bank of Kigali
    Tigo Pesa|Mixx by Yas|MTN MoMo|Ecocash|Flutterwave|Paystack|Chipper Cash|Barclays|Lloyds|Halifax
    Santander|BBVA|Bizum|Twint|银行|はやしの銀行|بنك مصر|Сбербанк|Kaspi.kz|Halyk|Vietcombank|Techcombank
    ACLEDA|ABA Mobile|Maybank2u|Touch n Go eWallet|KB국민은행|신한은행|카카오뱅크|भारतीय स्टेट बैंक
    ইসলামী ব্যাংক|ธนาคารกรุงเทพ
    """, "|"
)

val messagingSamples = listOf(
    "com.whatsapp" to "WhatsApp", "org.thoughtcrime.securesms" to "Signal", "org.telegram.messenger" to "Telegram",
    "com.google.android.gm" to "Gmail", "com.microsoft.office.outlook" to "Outlook", "x.y" to "Proton Mail",
    "x.y" to "GMX Mail", "x.y" to "Yahoo Mail", "x.y" to "Messages", "x.y" to "Email", "x.y" to "Samsung Email",
    "x.y" to "Thunderbird", "x.y" to "WhatsApp Business", "x.y" to "Messenger"
)

val problems = ArrayList<String>()
for ((pkg, label) in messagingSamples) {
    if (!messagingHit(pkg, label)) problems.add("messaging app not skipped: $label")
}
for ((pkg, label) in listOf("com.mail.android.x" to "mail.com", "x.y" to "Mail.com Mail")) {
    if (messagingHit(pkg, label) || labelHit(label)) problems.add("mail.com must not be skipped: $label")
}
for (pkg in ordinaryPackages) {
    if (packageHit(pkg)) problems.add("ordinary package would be skipped: $pkg")
}
for (label in ordinaryLabels) {
    if (labelHit(label) || messagingHit("x.y", label)) problems.add("ordinary app name would be skipped: $label")
}
val missed = shouldMatch.filter { !labelHit(it) }
for (m in missed) problems.add("not recognised: $m")

for (p in problems) println("PROBLEM: $p")
println(
    "${packageWords.size} package words, ${packageExact.size} package names, ${bankParts.size} bank parts, " +
        "${moneyWords.size} money words, ${brands.size} brands, ${problems.size - missed.size} problem(s), " +
        "${missed.size} missed"
)
if (problems.isNotEmpty()) kotlin.system.exitProcess(1)
