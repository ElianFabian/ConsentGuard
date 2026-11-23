# Languages

CookieRejecter inherits the ConsentGuard banner word lists described below. It matches those languages regardless of the selected app interface language.

## Google Funding Choices

The screenshot-assisted review matches English, Spanish, French, German, Italian, Portuguese, Japanese, Simplified Chinese, Russian, and Ukrainian labels in Google’s European consent forms. It checks that every switch in the recognized panels is off before offering confirmation. Google allows publishers to customize some labels, and an unfamiliar switch label in a recognized panel stops the review.

English has been tested on live MacroDroid and Google UMP forms. The Google test form had 18 purpose switches and 249 vendor switches. After confirmation, its saved TCF purpose and vendor settings had zero consent or legitimate-interest flags enabled. Both languages passed emulator tests using sample Android WebView forms with enabled and disabled switches in each panel. Google’s Android test app served the English form with Spanish set as the primary Android language, so validation on a live Spanish Android form is still pending.

The eight newer profiles use labels from Google’s localized message guides. Each passed an Android emulator test that opened the preferences from the banner, switched off enabled purpose and vendor choices, and waited for manual confirmation before saving. Live Android validation for those locales is still pending. Google lists Korean and Indonesian for IDFA messages. Its European message list covers Portuguese for Portugal.

[Supported languages](https://support.google.com/admob/answer/10107561?hl=en#supported-languages) · [French](https://support.google.com/admob/answer/10114014?hl=fr) · [German](https://support.google.com/admob/answer/10114014?hl=de) · [Italian](https://support.google.com/admob/answer/10114014?hl=it) · [Portuguese](https://support.google.com/admob/answer/10114014?hl=pt-PT) · [Japanese](https://support.google.com/admob/answer/10114014?hl=ja) · [Simplified Chinese](https://support.google.com/admob/answer/10114014?hl=zh-Hans) · [Russian](https://support.google.com/admob/answer/10114014?hl=ru) · [Ukrainian](https://support.google.com/admob/answer/10114014?hl=uk)

## What is included

| Language | Code |
| --- | --- |
| English | en | 
| German | de |
| French | fr | 
| Spanish | es | 
| Italian | it | 
| Dutch | nl | 
| Portuguese | pt | 
| Kiswahili | sw | 
| Afrikaans | af | 
| Arabic | ar | 
| Zulu | zu | 
| Xhosa | xh | 
| Hausa | ha | 
| Somali | so | 
| Yoruba | yo | 
| Igbo | ig | 
| Amharic | am | 

The phrases were compared with the labels that consent tools (OneTrust, Usercentrics, Didomi, Sourcepoint, Cookiebot and the Google consent form many apps show) use in that language. It does not mean a native speaker has read every phrase. It does not make it press the wrong button, because every pack is checked against the accept guard lists of all the other packs (see below).

## What changed in 0.0.2

- The Google consent form that many Android apps show ("Consent", "Do not consent", "Manage options", "Confirm choices") is now recognised in English, German, French, Spanish, Italian, Dutch and Portuguese. Before, the German and Dutch versions, and the Italian one with its "Acconsenti" and "Nega il consenso" buttons, were not recognised as banners at all.

- More ways of saying no: "Continue without consent" and its German, French and Italian forms, "Deselect all", "Close and reject", "No cookies, thanks", "Rather not", "Object to legitimate interest", and the "Not now" style dismissals as a last resort.

- More accept and pay wording that must never be tapped, such as "Continue with recommended cookies", "Allow and continue", "Continue with ads", "Use for free" and the German "Mit Werbung lesen" walls with contentpass or freechoice.

- The IAB TCF 2.2 purpose names ("Use limited data to select advertising", "Measure content performance" and the rest) count as consent panel wording, so preference panels are recognised more reliably.

- Withdraw and revoke buttons ("Withdraw consent", "Einwilligung widerrufen", "Retirer mon consentement" and so on) are no longer treated as reject buttons. They belong to privacy centres and account pages a person opened on purpose.
- "Block all cookies" and "Block third party cookies" are no longer reject wording. A browser's own cookie settings use them, and pressing them there would sign the person out of every site.

- Refusal and necessary only wording was compared with the button patterns DuckDuckGo's autoconsent uses, and the gaps were filled in English, German, French, Spanish, Italian, Dutch and Portuguese ("Weiger alle", "Je n'accepte rien", "Ohne Akzeptieren weiter", "Solo cookies técnicas", "Seguir sem aceitar" and more).

- Dutch refusals such as "Geen cookies toestaan" and "Ik accepteer geen cookies" were read as accept buttons, because "cookies toestaan" and "ik accepteer" matched anywhere in a label. Those accept phrases now have to be the whole label.

- One or two letter fallback words in Hausa, Igbo and Yoruba are now exact phrases. As plain phrases they matched inside ordinary words, "ki" inside "cookies" for example.

## How it stays safe across languages

- Accent marks are folded away before matching, so "Paramétrer" and "Parametrer" match the same phrase.

- Short or ambiguous phrases are written as exact phrases (marked with `=` in the pack files), so "no acepto" is never mistaken for "acepto".

- `kotlinc -script tools/check_languages.kts` fails if any label that should accept could also be read as a reject, necessary only, manage or confirm phrase in any language, or the other way round. It also fails on a plain phrase shorter than three letters, and on a generated phrase that is not folded the way the app folds text.

- If a screen looks like a sign in, payment or bank page, ConsentGuard stays out of it, in every language.

