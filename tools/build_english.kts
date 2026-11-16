/* Builds consent_rules.json and consent_lexicon.json, English only.

   The four original rules are kept exactly as they are in consent_rules.json.
   Everything new is generated from the word lists below so the coverage is
   easy to extend: add a word to a list, run this script, and every phrase
   built from it appears. Running it again never duplicates anything.

   Run from anywhere inside the project:
       kotlinc -script tools/build_english.kts */

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

/* A block of phrases separated by "|" or line breaks */
fun L(block: String): List<String> =
    block.replace("\n", "|").split("|").map { it.trim() }.filter { it.isNotEmpty() }

/* Normalizes, removes duplicates, and drops any substring pattern that a
   shorter substring pattern already covers (same behaviour in the app, fewer
   comparisons). Exact patterns start with "=" and are dropped only if a
   substring pattern covers them. */
fun prune(patterns: List<String>): List<String> {
    val subs = ArrayList<String>()
    val exacts = ArrayList<String>()
    for (raw in patterns) {
        if (raw.startsWith("=")) exacts.add("=" + norm(raw.substring(1))) else subs.add(norm(raw))
    }
    val sorted = subs.filter { it.isNotEmpty() }.toSet()
        .sortedWith(compareBy<String> { it.length }.thenBy { it })
    val kept = ArrayList<String>()
    for (s in sorted) {
        if (kept.none { s.contains(it) }) kept.add(s)
    }
    val outExact = ArrayList<String>()
    val seen = HashSet<String>()
    for (e in exacts) {
        val body = e.substring(1)
        if (body.isEmpty() || e in seen) continue
        if (kept.any { body.contains(it) }) continue
        seen.add(e)
        outExact.add(e)
    }
    return kept + outExact.sorted()
}

fun combine(leads: List<String>, tails: List<String>, joiner: String = " "): List<String> =
    leads.flatMap { a -> tails.map { b -> (a + joiner + b).trim() } }

// Reject wording

val REJECT_VERBS = L("""reject|decline|refuse|deny|disallow|disagree with|dont accept|do not accept|dont allow|do not allow|
dont consent|do not consent|i reject|i decline|i refuse|i deny|i disagree with|i do not accept|i dont accept""")

val REJECT_LIGHT_VERBS = L("""disable|turn off|switch off|opt out of|say no to|no to|object to""")

/* "Block" only goes with tracking words. A browser's own cookie settings say
   "Allow cookies" and "Block all cookies", which looks just like a banner, and
   blocking every cookie there would sign the person out of every site. */
val BLOCK_OBJECTS = L("""tracking|trackers|all tracking|all trackers|marketing|advertising|targeting|analytics|
personalised ads|personalized ads|non essential trackers|all marketing|all analytics|all advertising""")

val REJECT_LIGHT_OBJECTS = L("""all|all cookies|everything|non essential|non essential cookies|optional|optional cookies|
additional cookies|unnecessary cookies|tracking|tracking cookies|trackers|marketing|marketing cookies|
analytics|analytics cookies|advertising|advertising cookies|targeting|personalised ads|personalized ads|
third party cookies|non essential trackers|all tracking|all trackers|all marketing|all analytics|
all advertising|all optional|all non essential|all third party cookies""")

val REJECT_ALL_OBJECTS = L("""all|everything|all cookies|all optional|all optional cookies|all non essential|
all non essential cookies|all non necessary|all non necessary cookies|all additional|all additional cookies|
all unnecessary|all unnecessary cookies|all tracking|all tracking cookies|all trackers|all third party|
all third party cookies|all marketing|all marketing cookies|all analytics|all advertising|all targeting|
all purposes|all vendors|all partners|all categories|all consent|all consents|all of them|all others|
all non functional|non essential|non essential cookies|non essential trackers|non necessary|
non necessary cookies|non required|non required cookies|nonessential|optional|optional cookies|
optional trackers|additional|additional cookies|unnecessary|unnecessary cookies|unessential|extra cookies|
tracking|tracking cookies|tracking technologies|trackers|marketing|marketing cookies|targeting|
targeting cookies|advertising|advertising cookies|ad cookies|analytics|analytics cookies|
personalised ads|personalized ads|personalisation|personalization|third party cookies|cookies|cookie|
the use of cookies|data collection|data sharing|data processing|consent|non functional|statistics cookies|
preference cookies|social media cookies""")

val REJECT_TAILS = L("and close|and continue|and exit|and save|and leave|and proceed|and go on|and browse|and read")

val REJECT_INTRANSITIVE = L("""reject|decline|refuse|deny|disagree""")

val CONTINUE_WITHOUT = L("""without accepting|without agreeing|without consenting|without consent|without tracking|
without cookies|without allowing|without personalisation|without personalization|without personalised ads|
without personalized ads|without giving consent|without accepting cookies|without agreeing to cookies|
without any cookies|without optional cookies|without marketing cookies|without third party cookies|
without accepting anything|without accepting all|without allowing cookies|without allowing tracking""")

val REJECT_HAND = L("""i disagree|i do not agree|i dont agree|no i do not accept|not agree|do not agree|dont agree|
disagree and close|disagree and continue|disagree to all|disagree with all|disagree with everything|
do not sell|do not share|dont sell my|dont share my|opt out of sale|opt out of the sale|opt out of sharing|
opt out of targeted advertising|opt out of advertising|opt out of all|opt out of non essential|
opt out of marketing|opt out of analytics|opt out of tracking|limit the use of my sensitive|
object all|object to all|objecting to all|object to everything|legitimate interest object all|
no thanks|no thank you|no i decline|no i refuse|no i reject|
turn off all|turn off non essential|turn off all cookies|turn off tracking|switch off all|
disable all|disable all cookies|disable non essential|disable tracking|disable analytics|
do not consent|i do not consent|dont consent|i dont consent|
say no to all|say no to cookies|say no to tracking|i say no|no i do not|no i dont|
dont enable|do not enable|dont turn on|do not turn on""")

val REJECT_EXACT = L("""=reject|=reject all|=decline|=decline all|=refuse|=refuse all|=deny|=deny all|=disagree|
=i disagree|=i decline|=i reject|=i refuse|=no thanks|=no thank you|=opt out|=optout|=opt out all|
=block all|=disable all|=turn off all|=dont accept|=do not accept|=dont allow|=do not allow|
=dont agree|=do not agree|=dont consent|=do not consent|
=no i dont accept|=no i do not accept""")

// Necessary only wording
// Substring forms must contain an "only" style word, because a phrase like
// "allow essential and optional cookies" (a real accept everything label) would
// otherwise match "allow essential". Forms without "only" are exact match forms.

val NECESSARY_STEMS = L("""necessary|essential|essentials|required|functional|mandatory|technical|
strictly necessary|strictly required|strictly essential|necessary and functional|essential and functional|
required and functional|necessary and essential|necessary and required|functional and necessary""")

// Extra stems that only make sense in exact match forms
val NECESSARY_EXACT_STEMS = L("""necessary|essential|essentials|required|functional|mandatory|basic|minimal|technical|
strictly necessary""")

val NECESSARY_NOUNS = listOf("", " cookies", " cookie")

val NECESSARY_LEADS = L("""accept|allow|use|save|keep|enable|continue with|proceed with|agree to|permit|select|choose|confirm|
approve|go with|stay with|stick to|browse with|store|i accept|i agree to|i allow|i want|submit""")

val NECESSARY_HAND = L("""only the necessary|only the essential|only the essentials|only the required|only the mandatory|
only the basic|only the minimum|only the bare minimum|only what is necessary|only whats necessary|
only what is essential|only whats essential|only what is required|only whats required|
only what you need|only what we need|only what is needed|only whats needed|only those needed|
only those required|only those necessary|only the ones needed|only the ones required|
only the ones necessary|just the basics|just the minimum|just what is necessary|just whats necessary|
just what is essential|just whats essential|bare minimum|the bare minimum|minimum cookies|minimal cookies|
minimum required|minimum necessary|minimum essential|basic cookies only|necessary cookies alone|
essential cookies alone|accept only what|allow only what|use only what|save only what|
reject optional and continue|decline optional and continue""")

val NECESSARY_EXACT_EXTRA = L("""=necessary only|=essential only|=required only|=functional only|=only necessary|=only essential|
=only required|=necessary cookies|=essential cookies|=required cookies|=functional cookies|
=necessary cookies only|=essential cookies only|=required cookies only|=strictly necessary cookies|
=strictly necessary|=strictly necessary only|=continue with necessary|=continue with essential|
=continue with required|=continue with necessary cookies|=continue with essential cookies|
=proceed with necessary|=proceed with essential|=use necessary cookies|=use essential cookies|
=use required cookies|=use only necessary cookies|=use necessary cookies only|=accept necessary|
=accept essential|=accept required|=accept necessary cookies|=accept essential cookies|
=accept required cookies|=accept functional cookies|=accept functional|=allow necessary|=allow essential|
=allow required|=allow necessary cookies|=allow essential cookies|=allow required cookies|
=allow functional cookies|=allow functional|=save necessary|=save essential|=necessary and functional|
=essential and functional|=only functional|=functional cookies only""")

// Manage and preferences wording

// Phrases that are specific enough to match on their own, with no verb needed
val MANAGE_BARE = L("""cookie settings|cookie setting|cookies settings|cookie preferences|cookie preference|cookies preferences|
cookie options|cookies options|cookie choices|cookies choices|cookie controls|cookie management|cookie manager|
cookie categories|cookie details|cookies details|cookie selection|cookie configuration|
privacy settings|privacy preferences|privacy options|privacy choices|privacy controls|privacy center|
privacy centre|privacy manager|privacy dashboard|data privacy settings|data privacy preferences|
data privacy choices|data preferences|data settings|data choices|consent settings|consent preferences|
consent options|consent choices|consent management|consent categories|consent details|
tracking preferences|tracking settings|tracking options|tracking choices|tracking controls|
ad preferences|ad settings|ad choices|ads settings|ads preferences|advertising preferences|
advertising settings|advertising choices|marketing preferences|marketing settings|marketing choices|
personalisation settings|personalization settings|personalisation choices|personalization choices|
storage preferences|storage settings|storage options|preference center|preferences center|
preference centre|preferences centre|list of partners|list of vendors|partner list|vendor list""")

// Words that are too general to match alone, so they need one of these verbs in front
val MANAGE_VERBS = L("""manage|customize|customise|personalize|personalise|adjust|configure|review|choose|select|control|change|edit|
set|update|manage your|manage my|customize your|customize my|customise your|customise my|
change your|change my|adjust your|adjust my|edit your|edit my|review your|review my|
choose your|choose my|set your|set my|update your|update my|control your|control my""")

val MANAGE_OBJECTS = L("""cookies|cookie|privacy|consent|consents|tracking|purposes|partners|vendors|third parties|
our partners|our vendors|preferences|settings|options|choices|details|cookie consent|
data|ads|ad options|advertising|marketing|personalisation|personalization""")

val MANAGE_SHOW_VERBS = L("show|view|see|display|open|expand")
val MANAGE_SHOW_OBJECTS = L("""purposes|all purposes|partners|all partners|vendors|all vendors|details|more details|full details|
all details|our partners|our vendors|third parties|cookie details|cookies|cookie list|list of cookies|
purposes and partners|purposes and vendors|vendor details|partner details|purpose details|
special purposes|special features|features|options|preferences|choices|more options|more choices""")

val MANAGE_HAND = L("""more options|more choices|more settings|more preferences|more controls|other options|other choices|
options and settings|choices and settings|settings and options|more information and settings|
more info and settings|learn more and customize|learn more and customise|learn more and manage|
learn more and set|learn more and choose|read more and customize|read more and customise|
read more and manage|more and customize|more and customise|customize choices|customise choices|
customize preferences|customise preferences|customize settings|customise settings|customize options|
customise options|customize cookies|customise cookies|customize consent|customise consent|
let me choose|let me decide|let me customize|let me customise|let me pick|let me select|
let me manage|let me set|let me control|let me adjust|let me configure|let me see|
i want to choose|i want to customize|i want to customise|i want to decide|i want to manage|
i want to set|i want to control|i want to adjust|choose for myself|choose for yourself|
choose what to share|choose what i share|choose what you share|choose my cookies|
choose which cookies|choose your cookies|choose cookies|choose categories|
select which cookies|select cookies|select categories|
individual settings|individual privacy settings|individual preferences|individual choices|
individual cookie settings|individual cookie preferences|individual consent|
personal settings|personal preferences|personal choices|advanced settings|advanced options|
advanced preferences|advanced cookie settings|advanced cookie options|advanced privacy settings|
advanced controls|advanced configuration|advanced choices|advanced consent|advanced consent settings|
detailed settings|detailed options|detailed preferences|detailed view|detailed information|
detailed cookie settings|granular settings|granular controls|granular choices|granular consent|
fine tune|fine tuning|tailor your choices|tailor your preferences|tailor your experience|
opt out options|opt out settings|opt out preferences|opt out choices|opt out controls|
your choices|your privacy choices|your privacy options|your privacy settings|your privacy controls|
your cookie choices|your cookie options|your cookie settings|your cookie preferences|
your consent choices|your consent options|your consent settings|your consent preferences|
your data choices|your data options|your data settings|your data preferences|
your ad choices|your ad options|your ad settings|your ad preferences|
your advertising choices|your advertising options|your advertising settings|your advertising preferences|
your tracking choices|your tracking options|your tracking settings|your tracking preferences|
my choices|my privacy choices|my cookie choices|my cookie settings|my cookie preferences|
my consent choices|my consent settings|my consent preferences|my ad choices|my ad settings|
my data choices|my data settings|my privacy settings|my privacy preferences|
view options|view preferences|view choices|see options|see preferences|see choices|
show options|show preferences|show choices|show more options|show more choices|
open preferences|open choices|go to preferences|go to choices|
cookies and privacy settings|cookies and privacy preferences|privacy and cookie settings|
privacy and cookie preferences|privacy and consent settings|privacy and consent preferences|
privacy and data settings|privacy and data preferences|privacy and ad settings|
cookie and privacy settings|cookie and privacy preferences|cookie and consent settings|
cookie and consent preferences|cookie and tracking settings|cookie and tracking preferences|
consent and privacy settings|consent and privacy preferences|consent and cookie settings|
consent and cookie preferences|consent and tracking settings|consent and tracking preferences|
preference center|preferences center|preference centre|preferences centre|
cookie preference center|cookie preferences center|privacy preference center|
privacy preferences center|consent preference center|consent preferences center|
cookie management center|privacy management center|consent management center|
cookie control|cookie controls|privacy control|privacy controls|consent control|consent controls|
tracking control|tracking controls|ad control|ad controls|data control|data controls|
cookie consent options|cookie consent settings|cookie consent preferences|cookie consent choices|
cookie consent details|cookie consent manager|cookie consent management|
cookies consent options|cookies consent settings|cookies consent preferences|
cookies consent choices|cookies consent details|cookies consent manager""")

val MANAGE_EXACT = L("""=settings|=preferences|=options|=customize|=customise|=customize settings|=customise settings|=choices|
=manage|=manage options|=manage choices|=manage settings|=manage preferences|=manage cookies|
=details|=configure|=show details|=view details|=more options|=more choices|=personalize|
=personalise|=set preferences|=my choices|=my preferences|=your choices|=your preferences|
=advanced settings|=advanced options|=cookie settings|=cookie preferences|=cookies settings|
=cookie options|=cookie choices|=privacy settings|=privacy preferences|=privacy options|
=privacy choices|=adjust settings|=adjust preferences|=edit settings|=edit preferences|
=review options|=review settings|=review preferences|=let me choose|
=customize choices|=customise choices|=customize preferences|=customise preferences|
=show purposes|=show vendors|=show partners|=view partners|=view vendors|
=view purposes|=see purposes|=see vendors|=see partners|=learn more and customize|
=learn more and customise|=manage consent|=consent settings|=consent preferences|
=consent options|=consent choices|=manage my cookies|=manage my preferences|=manage my choices|
=manage my consent|=manage my settings|=manage privacy|=manage privacy settings|
=manage privacy preferences|=manage tracking|=manage ads|=manage ad preferences|
=ad preferences|=ad settings|=ad choices|=ads settings|=tracking preferences|
=tracking settings|=tracking options|=privacy center|=privacy centre|=preference center|
=preference centre|=preferences center|=preferences centre|=privacy manager|=cookie manager""")

// Confirm and save wording

val CONFIRM_VERBS = L("save|confirm|apply|submit|update")
val CONFIRM_OBJECTS = L("""preferences|my preferences|your preferences|settings|my settings|your settings|choices|my choices|
your choices|selection|my selection|your selection|selected|my selected|changes|my changes|
your changes|cookie settings|cookie preferences|cookie choices|cookies settings|cookies preferences|
consent|my consent|consent settings|consent preferences|consent choices|privacy settings|
privacy preferences|privacy choices|configuration|my configuration|these settings|these choices|
these preferences|this selection|current settings|current choices|current selection|
and close|and exit|and continue|and proceed|and leave|and apply|and confirm|and done|and finish|
and go|and return|and back|and browse|and read""")

val CONFIRM_SELECT_LEADS = L("""accept|allow|agree to|use|continue with|proceed with|go with|permit|approve|enable|apply|choose""")
val CONFIRM_SELECT_OBJECTS = L("""selection|selected|my selection|my choices|my settings|my preferences|chosen|the selection|
these settings|these choices|these preferences|selected cookies|selected options|
selected categories|current selection|current settings|current choices|chosen cookies|
chosen options|chosen categories|my selected|your selection|your choices|your settings""")

val CONFIRM_HAND = L("""save and accept|save and apply|save and confirm|save and finish|save and go|save and return|
save and back|save and browse|save and read|close and save|done and save|apply and close|
apply and save|apply and continue|apply and exit|confirm and save|confirm and apply|
confirm and close|confirm and continue|confirm and exit|submit and close|submit and continue|
submit and exit|update and close|update and continue|update and exit|
confirm my choices|confirm my selection|confirm my preferences|confirm my settings|
confirm consent choices|confirm cookie choices|confirm privacy choices|confirm privacy settings|
accept my choices|accept my selection|accept my preferences|accept my settings|
accept these settings|accept these choices|accept these preferences|accept selected|accept selection|
allow selected|allow selection|allow my selection|allow my choices|allow chosen|allow the selection|
save selected|save selection|save my selection|save my choices|save my preferences|save my settings|
save my consent|save your choices|save your preferences|save your settings|save these settings|
save these choices|save this selection|save cookie settings|save cookie preferences|
save cookie choices|save consent settings|save privacy settings|save privacy preferences|
save privacy choices|save changes|save configuration|save consent""")

val CONFIRM_EXACT = L("""=save|=confirm|=apply|=done|=submit|=update|=finish|=finished|=complete|=save settings|
=save preferences|=save choices|=save selection|=save and close|=save and exit|=save and continue|
=confirm choices|=confirm selection|=confirm preferences|=confirm settings|=confirm my choices|
=apply settings|=apply changes|=submit preferences|=submit choices|=save my preferences|
=save my choices|=save my settings|=save changes|=update preferences|=update settings|
=update choices|=update consent|=allow selection|=allow selected|=accept selection|
=accept selected|=confirm and close|=confirm and continue|=confirm and exit|=save exit|
=save close|=save continue""")

// Fallback wording. Runs last, only when nothing else on the screen matched.
// The single words are matched as substrings of short labels only (see FALLBACK_MAX_LENGTH in
// the service), and the service skips any label that also says accept, agree or allow.
val FALLBACK_SUBSTRING = L("reject|decline|refuse|disagree|deny")
val FALLBACK_EXACT = L("""=reject|=reject all|=decline|=decline all|=refuse|=refuse all|=deny|=deny all|=disagree|
=i disagree|=no|=no thanks|=no thank you|=opt out|=dont accept|=do not accept|=dont allow|
=do not allow|=nope""")

// Provider wording, copied from what these tools commonly show on their banners.
// These are typical labels, not verified against every live site. Kept in one place so
// a label seen in the field can be added here in one line.
val PROVIDER_REJECT = L("""reject all cookies|reject all|deny all|decline all|refuse all|disagree and close|
refuse non essential cookies|decline optional cookies|reject non essential|reject optional|
reject additional cookies|i do not accept|i do not consent|do not consent|continue without accepting|
continue without agreeing|object all|object to all|opt out of all|decline non essential|
reject all non essential cookies|reject all optional cookies|reject all additional cookies|
reject unnecessary cookies|refuse optional cookies|deny non essential cookies|
deny optional cookies|deny additional cookies|do not allow cookies|do not allow all""")

val PROVIDER_MANAGE = L("""manage cookies|manage preferences|manage options|manage choices|manage consent|manage settings|
cookies settings|cookie settings|cookie preferences|cookie options|privacy settings|privacy preferences|
privacy choices|your privacy choices|more options|show purposes|manage my cookies|
learn more and customize|learn more and customise|individual privacy settings|let me choose|
choose cookies|set preferences|advanced settings|view preferences|manage services|manage vendors|
manage partners|show details|show vendors|options and settings|settings and options|
your ad choices|manage ad preferences|manage ad choices""")

val PROVIDER_CONFIRM = L("""confirm my choices|confirm choices|save and exit|save and close|save preferences|save my preferences|
save settings|save my settings|save selection|save my selection|allow selection|allow selected|
accept selection|accept selected|save and accept|save choices|save my choices|apply settings|
apply choices|apply preferences|submit preferences|submit my preferences|confirm selection|
confirm my selection|update preferences|update my preferences|update consent|save changes|
save and continue|confirm and continue|confirm and close""")

// Wording added in 0.0.2, collected from banners and consent forms in use in
// 2025 and 2026: the Google consent form many Android apps show (Consent, Do
// not consent, Manage options, Confirm choices), IAB TCF 2.2 and 2.3 purpose
// names, CNIL and Garante guidance on refusing, US opt out links, pay or okay
// walls, and phrasings other blockers had to add after field reports.
//
// Withdraw and revoke buttons are left out on purpose. They belong to privacy
// centres and account pages a person opened themselves, not to a banner, and
// tapping one there can do more than refuse cookies.

val REJECT_CURRENT = L("""deselect all|unselect all|uncheck all|untick all|turn all off|switch all off|
toggle all off|close and reject|close and decline|close and refuse|close and deny|no cookies|
object to legitimate interest|object to all legitimate interest|dont allow any|do not allow any|
reject non necessary|refuse non necessary|decline non necessary|do not consent to|i do not want cookies|
opt out of|=i confirm necessary|
=rather not|=no rather not|=id rather not|=i would rather not|=no i would rather not""")

val FALLBACK_CURRENT = L("""=not now|=no not now|=not right now|=maybe later""")

val ACCEPT_GUARD_CURRENT = L("""accept optional|allow optional|allow and continue|allow and close|allow and proceed|
continue with recommended|recommended cookies|allow recommended|use recommended|agree to everything|
consent to everything|yes i consent|=yes please|accept and support|consent and support|
continue using cookies|continue with all cookies|continue with cookies""")

val NEVER_CLICK_CURRENT = L("""with ads|free with ads|use for free|contentpass|freechoice|go premium|premium|
become a member""")

/* IAB TCF 2.2 purpose and feature names as consent forms show them, and the
   heading of the second screen of the Google consent form. They only ever
   appear on a consent panel. */
val TCF_PURPOSES = L("""use limited data to select advertising|create profiles for personalised advertising|
create profiles for personalized advertising|use profiles to select personalised advertising|
use profiles to select personalized advertising|create profiles to personalise content|
create profiles to personalize content|use profiles to select personalised content|
use profiles to select personalized content|measure advertising performance|measure content performance|
understand audiences through statistics|develop and improve services|use limited data to select content|
use precise geolocation data|actively scan device characteristics|manage your data""")

val ANCHOR_CURRENT = L("""consent or pay|pay or consent|limit the use of my sensitive|global privacy control|
our trusted partners|we and our vendors|inmobi|cookiehub|cookiepro|consentmo|admiral|uniconsent|
trustcommander|commanders act|pubtech|sibbo|truendo""")

val STRONG_ANCHOR_CURRENT = L("""vendor preferences|partner preferences|inmobi|cookiehub|cookiepro|consentmo|
uniconsent|trustcommander|commanders act|pubtech|sibbo|truendo""")

val MANAGE_CURRENT = L("""vendor preferences|partner preferences|manage vendor preferences|manage partner preferences""")

val SENSITIVE_CURRENT = L("""upi pin|enter otp|one time password|card verification code""")

// Lexicon

val ANCHOR = L("""legitimate interest|list of partners|advertising and content|store and or access information|
cookie policy|cookie consent|gdpr|cookie|cookies|we value your privacy|we use cookies|this site uses cookies|
this website uses cookies|we and our partners|your privacy choices|privacy choices|privacy preferences|
personalised ads|personalized ads|ad measurement|content measurement|audience insights|
consent choices|your consent|manage consent|consent preferences|consent management|
do not sell or share|do not sell my|tracking technologies|data processing purposes|
our partners|our vendors|third party vendors|special purposes|special features|
device identifiers|precise geolocation|transparency and consent framework|iab europe|tcf|
strictly necessary|always active|privacy center|privacy centre|preference center|preference centre|
by continuing to use|by continuing to browse|by using this site|by using this website|
we care about your privacy|your privacy matters|privacy is important to us|
we respect your privacy|about your privacy|your data your choice|your data your choices|
onetrust|cookiebot|didomi|usercentrics|quantcast|sourcepoint|trustarc|iubenda|funding choices|
consentmanager|osano|termly|cookieyes|cookie script|complianz|borlabs|axeptio|klaro|ketch|
transcend|securiti|cookiefirst|cookie information|cookie law|eprivacy|ccpa|cpra|lgpd""")

val STRONG_ANCHOR = L("""strictly necessary|necessary cookies|essential cookies|functional cookies|performance cookies|
analytics cookies|marketing cookies|targeting cookies|advertising cookies|statistics cookies|
statistic cookies|preference cookies|preferences cookies|social media cookies|
cookie categories|categories of cookies|cookie preferences|cookie settings|
legitimate interest|list of partners|our partners|our vendors|third party vendors|
store and or access information|always active|always on|cookie consent|
consent preferences|manage consent|privacy preferences|data processing purposes|
special purposes|special features|tracking technologies|personalised ads|personalized ads|
onetrust|cookiebot|didomi|usercentrics|quantcast|sourcepoint|trustarc|iubenda|
consentmanager|osano|termly|cookieyes|cookie script|complianz|borlabs|axeptio|klaro|ketch|
transcend|securiti|cookiefirst|cookie information""")

val NEVER_CLICK = L("""pay|paying|payment|payments|paywall|pay or okay|subscribe|subscribed|subscriber|subscription|
subscriptions|purchase|buy|checkout|sign in|sign up|log in|login|register|create account|
create an account|join|membership|upgrade|trial|free trial|start trial|donate|donation|
support us|support our journalism|become a supporter|ad free|ad free version|
remove ads|go ad free|without ads|for only|per month|per year|a month|a year|monthly|yearly""")

val ACCEPT_GUARD = L("""accept all|accept everything|allow all|agree to all|agree with all|consent to all|enable all|
turn on all|opt in|opt in to all|accept and continue|accept and close|accept and proceed|
accept and go|accept and read|accept and browse|accept and enter|accept cookies|accept all cookies|
accept the cookies|allow cookies|allow all cookies|agree and continue|agree and close|
agree and proceed|agree and go|agree and read|agree and enter|yes i accept|yes i agree|
yes im happy|yes i am happy|im happy|i am happy|im ok with|i am ok with|sounds good|thats fine|
that is fine|fine by me|continue and accept|close and accept|ok got it|allow essential and optional|
accept essential and optional|allow all and continue|accept recommended|accept recommended settings|
accept default|accept defaults|i consent|consent and continue|consent and close|
personalised ads and content|personalized ads and content|yes to all|
=i accept|=i agree|=accept|=agree|=allow|=consent|=ok|=okay|=yes|=got it|=understood|=continue|
=proceed|=accept cookies|=allow cookies|=enable|=enable cookies|=opt in|=i understand|=okay got it""")

val SWITCH_ESSENTIAL = L("""strictly necessary|strictly required|necessary|essential|required|mandatory|always active|always on|
always enabled|always allowed|cannot be disabled|cannot be turned off|cannot be switched off|
cant be disabled|cant be turned off|cant be switched off|technically required|
strictly essential|absolutely necessary|absolutely essential|essentials|necessity|needed|
required for|compulsory|obligatory|technical|technically necessary|technically essential|
site to function|website to function|site to work|website to work|work properly|function properly|
basic function|core function|always present|always required|
cannot be declined|cant be declined|cannot be refused|cant be refused|cannot be rejected|cant be rejected|
cannot be deactivated|cant be deactivated|cannot be unchecked|cant be unchecked|
cannot be changed|cant be changed|cannot be opted out|cant be opted out|no opt out""")

val SWITCH_NON_ESSENTIAL = L("""non essential|non necessary|non required|non mandatory|not essential|not necessary|not required|
unnecessary|unessential|nonessential|optional|additional|extra|voluntary""")

val SWITCH_KEEP_ON = L("""opt out|do not sell|do not share|dont sell|dont share|do not track|dont track|limit use|limit the use|
limit my|restrict|block|prevent|protect|global privacy control|gpc|remember me|
remember my choice|remember my choices|remember my settings|remember my preferences|
remember my decision|remember this|dont show again|do not show again|dont ask again|
do not ask again|stay signed in|keep me signed in|keep me logged in|stay logged in""")

val ADS_PURPOSE = L("""advert|marketing|targeting|personalis|personaliz|ad selection|select basic ads|retarget|behavioural|
behavioral|interest based|profile|profiling|tailored ads|social media|sponsor|ad personalisation|
ad personalization|create a personalised ads profile|create a personalized ads profile""")

val STATS_PURPOSE = L("""measure|measurement|audience|analytic|performance|statistic|insight|reporting|metrics|usage data|
site usage|visitor|traffic|improve our|research""")

val SENSITIVE_SCREEN = L("""card number|credit card number|credit card|debit card|card holder|cardholder|name on card|expiry date|
expiration date|security code|cvv|cvc|iban|account number|sort code|routing number|
enter your pin|payment details|payment method|pay now|place order|complete purchase""")

// Building

fun buildReject(): List<String> =
    combine(REJECT_VERBS, REJECT_ALL_OBJECTS) +
        combine(REJECT_LIGHT_VERBS, REJECT_LIGHT_OBJECTS) +
        combine(listOf("block"), BLOCK_OBJECTS) +
        combine(REJECT_INTRANSITIVE, REJECT_TAILS) +
        CONTINUE_WITHOUT +
        REJECT_HAND +
        REJECT_EXACT +
        PROVIDER_REJECT +
        REJECT_CURRENT

fun buildNecessary(): List<String> {
    val out = ArrayList<String>()
    val stems = NECESSARY_STEMS.flatMap { s -> NECESSARY_NOUNS.map { s + it } }
    // Substring forms always carry an only style word
    out += combine(listOf("only", "just", "only the", "just the", "only what is", "only whats"), stems)
    out += stems.map { "$it only" }
    out += combine(NECESSARY_LEADS.map { "$it only" }, stems)
    out += combine(NECESSARY_LEADS.map { "only $it" }, stems)
    out += NECESSARY_HAND
    // Forms without an only word are exact match forms
    val exactStems = NECESSARY_EXACT_STEMS.flatMap { s -> NECESSARY_NOUNS.map { s + it } }
    val exactLeads = L(
        "accept|allow|use|save|keep|enable|continue with|proceed with|agree to|permit|select|choose|confirm|" +
            "i accept|i agree to|i allow"
    )
    out += exactLeads.flatMap { lead -> exactStems.map { "=$lead $it" } }
    out += NECESSARY_EXACT_EXTRA
    return out
}

fun buildManage(): List<String> =
    MANAGE_BARE +
        combine(MANAGE_VERBS, MANAGE_OBJECTS) +
        combine(MANAGE_SHOW_VERBS, MANAGE_SHOW_OBJECTS) +
        MANAGE_HAND +
        MANAGE_EXACT +
        PROVIDER_MANAGE +
        MANAGE_CURRENT

fun buildConfirm(): List<String> =
    combine(CONFIRM_VERBS, CONFIRM_OBJECTS) +
        combine(CONFIRM_SELECT_LEADS, CONFIRM_SELECT_OBJECTS) +
        CONFIRM_HAND +
        CONFIRM_EXACT +
        PROVIDER_CONFIRM

fun rule(
    name: String,
    reject: List<String>,
    necessary: List<String>,
    manage: List<String>,
    fallback: List<String>,
    confirm: List<String>
): Map<String, Any> = linkedMapOf(
    "name" to name,
    "rejectResourceIds" to emptyList<String>(),
    "rejectTextPatterns" to prune(reject),
    "necessaryResourceIds" to emptyList<String>(),
    "necessaryTextPatterns" to prune(necessary),
    "manageResourceIds" to emptyList<String>(),
    "manageTextPatterns" to prune(manage),
    "fallbackRejectTextPatterns" to prune(fallback),
    "confirmResourceIds" to emptyList<String>(),
    "confirmTextPatterns" to prune(confirm)
)

fun buildLexicon(): Map<String, List<String>> = linkedMapOf(
    "anchorTextPatterns" to ANCHOR + ANCHOR_CURRENT + TCF_PURPOSES,
    "strongAnchorTextPatterns" to STRONG_ANCHOR + STRONG_ANCHOR_CURRENT + TCF_PURPOSES,
    "neverClickTextPatterns" to NEVER_CLICK + NEVER_CLICK_CURRENT,
    "acceptGuardTextPatterns" to ACCEPT_GUARD + ACCEPT_GUARD_CURRENT,
    "switchEssentialTextPatterns" to SWITCH_ESSENTIAL,
    "switchNonEssentialTextPatterns" to SWITCH_NON_ESSENTIAL,
    "switchKeepOnTextPatterns" to SWITCH_KEEP_ON,
    "adsPurposeTextPatterns" to ADS_PURPOSE,
    "statsPurposeTextPatterns" to STATS_PURPOSE,
    "sensitiveScreenTextPatterns" to SENSITIVE_SCREEN + SENSITIVE_CURRENT
).mapValuesTo(LinkedHashMap()) { prune(it.value) }

// JSON in and out, laid out the way the files have always been written

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
        is Double -> if (value % 1.0 == 0.0) value.toLong().toString() + ".0" else value.toString()
        is Map<*, *> -> if (value.isEmpty()) "{}" else value.entries.joinToString(",\n", "{\n", "\n$end}") {
            pad + jsonString(it.key as String) + ": " + toJson(it.value, indent, level + 1)
        }
        is List<*> -> if (value.isEmpty()) "[]" else value.joinToString(",\n", "[\n", "\n$end]") {
            pad + toJson(it, indent, level + 1)
        }
        else -> throw IllegalArgumentException("cannot write ${value::class}")
    }
}

// Writing the files

val root = projectRoot()
val rulesFile = File(root, "app/src/main/assets/consent_rules.json")
val lexiconFile = File(root, "app/src/main/assets/consent_lexicon.json")

/* The rules as they are now, without any earlier english_wording rule, so
   running this again never produces a duplicate. */
val original = (JsonReader(rulesFile.readText(), rulesFile.name).value() as List<*>)
    .filter { (it as Map<*, *>)["name"] != "english_wording" }

val english = rule(
    "english_wording",
    reject = buildReject(),
    necessary = buildNecessary(),
    manage = buildManage(),
    fallback = FALLBACK_SUBSTRING + FALLBACK_EXACT + FALLBACK_CURRENT,
    confirm = buildConfirm()
)

val rules = original + english
rulesFile.writeText(toJson(rules, 2) + "\n")

val lexicon = buildLexicon()
lexiconFile.writeText(toJson(lexicon, 2) + "\n")

println("rules written: ${rules.size}")
for ((key, value) in english) {
    if (value is List<*> && value.isNotEmpty()) println("  english_wording $key ${value.size}")
}
for ((key, value) in lexicon) println("  lexicon $key ${value.size}")
