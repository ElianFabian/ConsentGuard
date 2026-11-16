/* Writes mock consent banners to testpages/ so ConsentGuard can be tried on a
   phone without visiting real sites. These are simple pages written for this
   project, not copies of any real banner. Each page shows a line at the top
   that says what happened (PASS or FAIL), so nothing needs to be logged.

   Write the pages:
       kotlinc -script tools/make_testpages.kts
   Write them and serve testpages/ on port 8000 (Ctrl+C stops it):
       kotlinc -script tools/make_testpages.kts serve
   Phone: connect over USB, run
       adb reverse tcp:8000 tcp:8000
   then open http://localhost:8000 in Chrome or Firefox on the phone. To try
   translation, use Chrome's Translate or Firefox's Translations on the
   English pages, and the other pages as they are. */

import com.sun.net.httpserver.HttpServer
import java.io.File
import java.net.InetAddress
import java.net.InetSocketAddress

fun projectRoot(): File {
    var dir: File? = File("").absoluteFile
    while (dir != null) {
        if (File(dir, "settings.gradle.kts").isFile && File(dir, "tools").isDirectory) return dir
        dir = dir.parentFile
    }
    System.err.println("Run this from inside the ConsentGuard project")
    kotlin.system.exitProcess(2)
}

class Wording(
    val dir: String, val lead: String, val accept: String, val reject: String, val manage: String,
    val save: String, val pay: String, val essential: String, val stats: String, val ads: String, val pers: String
)

val L = linkedMapOf(
    "en" to Wording(
        "ltr", "We use cookies and similar technologies to personalise content and measure visits.",
        "Accept all", "Reject all", "Manage options", "Save choices", "Subscribe for 4.99 a month",
        "Strictly necessary (always active)", "Analytics cookies", "Marketing cookies", "Personalised ads"
    ),
    "de" to Wording(
        "ltr", "Wir verwenden Cookies, um Inhalte zu personalisieren und Besuche zu messen.",
        "Alle akzeptieren", "Alle ablehnen", "Einstellungen verwalten", "Auswahl speichern",
        "Jetzt abonnieren für 4,99 im Monat", "Unbedingt erforderlich (immer aktiv)", "Statistik Cookies",
        "Marketing Cookies", "Personalisierte Werbung"
    ),
    "fr" to Wording(
        "ltr", "Nous utilisons des cookies pour personnaliser le contenu et mesurer les visites.",
        "Tout accepter", "Tout refuser", "Paramétrer les cookies", "Enregistrer mes choix",
        "S'abonner pour 4,99 par mois", "Strictement nécessaires (toujours actifs)", "Cookies statistiques",
        "Cookies publicitaires", "Publicités personnalisées"
    ),
    "es" to Wording(
        "ltr", "Utilizamos cookies para personalizar el contenido y medir las visitas.",
        "Aceptar todo", "Rechazar todo", "Configurar cookies", "Guardar preferencias", "Suscribirse por 4,99 al mes",
        "Estrictamente necesarias (siempre activas)", "Cookies analíticas", "Cookies publicitarias",
        "Anuncios personalizados"
    ),
    "sw" to Wording(
        "ltr", "Tunatumia vidakuzi kubinafsisha maudhui na kupima ziara.",
        "Kubali zote", "Kataa zote", "Dhibiti chaguo", "Hifadhi mapendeleo", "Jisajili kwa 4.99 kwa mwezi",
        "Muhimu kabisa (inatumika kila wakati)", "Vidakuzi vya takwimu", "Vidakuzi vya matangazo",
        "Matangazo yaliyobinafsishwa"
    ),
    "zu" to Wording(
        "ltr", "Sisebenzisa amakhukhi ukwenza okuqukethwe ngokwezifiso nokulinganisa ukuvakasha.",
        "Yamukela konke", "Yenqaba konke", "Lawula izinketho", "Londoloza izintandokazi", "Bhalisela ngo 4.99 ngenyanga",
        "Okudingekayo kakhulu (njalo kuyasebenza)", "Amakhukhi izibalo", "Amakhukhi okukhangisa",
        "Izikhangiso ezenziwe ngokwezifiso"
    ),
    "af" to Wording(
        "ltr", "Ons gebruik koekies om inhoud te verpersoonlik en besoeke te meet.",
        "Aanvaar alles", "Weier alles", "Bestuur opsies", "Stoor voorkeure", "Teken in vir 4.99 per maand",
        "Streng noodsaaklik (altyd aktief)", "Statistiese koekies", "Bemarking koekies", "Gepersonaliseerde advertensies"
    ),
    "ar" to Wording(
        "rtl", "نحن نستخدم ملفات تعريف الارتباط لتخصيص المحتوى وقياس الزيارات.",
        "قبول الكل", "رفض الكل", "إدارة الخيارات", "حفظ التفضيلات", "اشترك مقابل 4.99 في الشهر",
        "ضرورية للغاية (نشطة دائما)", "ملفات تعريف الارتباط الإحصائية", "ملفات تعريف الارتباط الإعلانية",
        "الإعلانات المخصصة"
    )
)

val SCENARIOS = linkedMapOf(
    "simple" to "Test page. Expect a green PASS bar without touching anything.",
    "manage" to "Two step test page. Expect a green PASS bar without touching anything.",
    "wall" to "Test page. This one must stay on WAITING. It must never turn green.",
    "late" to "Slow test page. The box appears after three seconds. Expect a green PASS bar.",
    "sneaky" to "Test page with a tricky layout. Expect a green PASS bar without touching anything."
)

fun page(code: String, dir: String, scenario: String, note: String, body: String, hidden: String, late: String) = """<!doctype html>
<html lang="$code" dir="$dir"><head><meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Test $scenario $code</title>
<style>
body{font:16px/1.5 sans-serif;margin:0;padding:12px 12px 280px}
#verdict{position:sticky;top:0;background:#222;color:#fff;padding:8px 12px;margin:-12px -12px 12px;font-weight:bold}
#verdict.pass{background:#1b7a2b} #verdict.fail{background:#b3261e}
#banner{position:fixed;left:0;right:0;bottom:0;background:#fff;border-top:3px solid #444;padding:14px;box-shadow:0 -4px 14px #0004;max-height:70vh;overflow:auto}
button{font-size:16px;padding:10px 14px;margin:4px 4px 4px 0}
.big{background:#0a58ca;color:#fff;border:0;font-size:18px;padding:14px 20px}
a.small{font-size:11px;color:#999;margin-inline-start:8px}
label{display:block;margin:10px 0}
</style></head><body>
<div id="verdict">WAITING</div>
<h1>$scenario test ($code)</h1>
<p>$note</p>
<div id="banner" role="dialog" aria-modal="true" $hidden>
$body
</div>
<script>
var v=document.getElementById('verdict'), b=document.getElementById('banner');
function done(ok,msg){v.textContent=(ok?'PASS ':'FAIL ')+msg;v.className=ok?'pass':'fail';b.hidden=true;}
function rejected(){done(true,'rejected');}
function accepted(){done(false,'accepted');}
function save(){
  var on=[].filter.call(document.querySelectorAll('#panel input'),function(i){return i.checked&&!i.disabled;});
  var ess=document.querySelector('#panel input[disabled]');
  if(on.length){done(false,on.length+' optional switch(es) still on');}
  else if(!ess||!ess.checked){done(false,'required switch was turned off');}
  else{done(true,'all optional off, required kept');}
}
function openPanel(){document.getElementById('main').hidden=true;document.getElementById('panel').hidden=false;}
$late
</script></body></html>"""

fun body(scenario: String, t: Wording): String {
    val pay = "<button type=\"button\" onclick=\"v.textContent='FAIL pay pressed'\">${t.pay}</button>"
    return when (scenario) {
        "simple", "late" ->
            "<div id=\"main\"><p>${t.lead}</p><button class=\"big\" type=\"button\" onclick=\"accepted()\">${t.accept}</button>" +
                "<button type=\"button\" onclick=\"rejected()\">${t.reject}</button></div>"
        "sneaky" ->
            "<div id=\"main\"><p>${t.lead}</p><button class=\"big\" type=\"button\" onclick=\"accepted()\">${t.accept}</button>" +
                "<a class=\"small\" href=\"#\" role=\"button\" onclick=\"rejected();return false\">${t.reject}</a></div>"
        "wall" ->
            "<div id=\"main\"><p>${t.lead}</p><button class=\"big\" type=\"button\" onclick=\"accepted()\">${t.accept}</button>$pay</div>"
        else -> {
            val panel = "<div id=\"panel\" hidden><label><input type=\"checkbox\" role=\"switch\" checked disabled> ${t.essential}</label>" +
                "<label><input type=\"checkbox\" role=\"switch\" checked> ${t.stats}</label>" +
                "<label><input type=\"checkbox\" role=\"switch\" checked> ${t.ads}</label>" +
                "<label><input type=\"checkbox\" role=\"switch\" checked> ${t.pers}</label>" +
                "<button type=\"button\" onclick=\"save()\">${t.save}</button></div>"
            "<div id=\"main\"><p>${t.lead}</p><button class=\"big\" type=\"button\" onclick=\"accepted()\">${t.accept}</button>" +
                "<button type=\"button\" onclick=\"openPanel()\">${t.manage}</button></div>$panel"
        }
    }
}

val out = File(projectRoot(), "testpages")
out.mkdirs()
val links = ArrayList<Triple<String, String, String>>()
for ((scenario, note) in SCENARIOS) {
    for ((code, t) in L) {
        val hidden = if (scenario == "late") "hidden" else ""
        val late = if (scenario == "late") "setTimeout(function(){b.hidden=false;},3000);" else ""
        val name = "${scenario}_$code.html"
        File(out, name).writeText(page(code, t.dir, scenario, note, body(scenario, t), hidden, late))
        links.add(Triple(scenario, code, name))
    }
}
val index = StringBuilder(
    "<!doctype html><meta charset=\"utf-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">" +
        "<title>Test pages</title><body style=\"font:16px/1.6 sans-serif;padding:12px\"><h1>Test pages</h1>"
)
for ((scenario, note) in SCENARIOS) {
    index.append("<h2>$scenario</h2><p>$note</p><p>")
    index.append(links.filter { it.first == scenario }.joinToString(" ") { "<a href=\"${it.third}\">${it.second}</a>" })
    index.append("</p>")
}
File(out, "index.html").writeText(index.toString())
println("wrote ${links.size} pages to ${out.path}")

/* A small static file server for the pages, so nothing else needs to be
   installed. It only answers on this computer (localhost), which is what
   adb reverse forwards the phone to. */
if (args.firstOrNull() == "serve") {
    val port = args.getOrNull(1)?.toIntOrNull() ?: 8000
    val base = out.canonicalFile
    val server = HttpServer.create(InetSocketAddress(InetAddress.getLoopbackAddress(), port), 0)
    server.createContext("/") { exchange ->
        val path = exchange.requestURI.path.trimStart('/').ifEmpty { "index.html" }
        val file = File(base, path).canonicalFile
        val inside = file.path.startsWith(base.path + File.separator)
        if (exchange.requestMethod != "GET" || !inside || !file.isFile) {
            exchange.sendResponseHeaders(404, -1)
        } else {
            val bytes = file.readBytes()
            exchange.responseHeaders.add("Content-Type", "text/html; charset=utf-8")
            exchange.responseHeaders.add("Cache-Control", "no-store")
            exchange.sendResponseHeaders(200, bytes.size.toLong())
            exchange.responseBody.use { it.write(bytes) }
        }
        exchange.close()
    }
    server.start()
    println("serving ${base.path} at http://localhost:$port (Ctrl+C stops it)")
    Thread.currentThread().join()
}
